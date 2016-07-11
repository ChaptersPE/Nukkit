package io.nukkit.scheduler;

import io.nukkit.plugin.IllegalPluginAccessException;
import io.nukkit.plugin.Plugin;
import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.Level;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Scheduler {
    private final AtomicInteger ids = new AtomicInteger(1);
    private volatile Task head = new Task();
    private final AtomicReference<Task> tail;
    private final PriorityQueue<Task> pending;
    private final List<Task> temp;
    private final ConcurrentHashMap<Integer, Task> runners;
    private volatile int currentTick;
    private final Executor executor;
    private AsyncDebugger debugHead;
    private AsyncDebugger debugTail;
    private static final int RECENT_TICKS = 30;

    public Scheduler() {
        this.tail = new AtomicReference<>(this.head);

        this.pending = new PriorityQueue<>(10, new Comparator<Task>() {
            public int compare(Task o1, Task o2) {
                return (int) (o1.getNextRun() - o2.getNextRun());
            }
        });
        this.temp = new ArrayList<>();
        this.runners = new ConcurrentHashMap<>();
        this.currentTick = -1;
        this.executor = Executors.newCachedThreadPool();
        this.debugHead = new AsyncDebugger(-1, null, null) {
            StringBuilder debugTo(StringBuilder string) {
                return string;
            }
        };
        this.debugTail = this.debugHead;
    }

    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task) {
        return this.scheduleSyncDelayedTask(plugin, task, 0L);
    }

    public Task runTask(Plugin plugin, Runnable runnable) {
        return this.runTaskLater(plugin, runnable, 0L);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task) {
        return this.scheduleAsyncDelayedTask(plugin, task, 0L);
    }

    public Task runTaskAsynchronously(Plugin plugin, Runnable runnable) {
        return this.runTaskLaterAsynchronously(plugin, runnable, 0L);
    }

    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        return this.scheduleSyncRepeatingTask(plugin, task, delay, -1L);
    }

    public Task runTaskLater(Plugin plugin, Runnable runnable, long delay) {
        return this.runTaskTimer(plugin, runnable, delay, -1L);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        return this.scheduleAsyncRepeatingTask(plugin, task, delay, -1L);
    }

    public Task runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long delay) {
        return this.runTaskTimerAsynchronously(plugin, runnable, delay, -1L);
    }

    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long period) {
        return this.runTaskTimer(plugin, runnable, delay, period).getTaskId();
    }

    public Task runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
        validate(plugin, runnable);
        if (delay < 0L) {
            delay = 0L;
        }

        if (period == 0L) {
            period = 1L;
        } else if (period < -1L) {
            period = -1L;
        }

        return this.handle(new Task(plugin, runnable, this.nextId(), period), delay);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long period) {
        return this.runTaskTimerAsynchronously(plugin, runnable, delay, period).getTaskId();
    }

    public Task runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long delay, long period) {
        validate(plugin, runnable);
        if (delay < 0L) {
            delay = 0L;
        }

        if (period == 0L) {
            period = 1L;
        } else if (period < -1L) {
            period = -1L;
        }

        return this.handle(new AsyncTask(this.runners, plugin, runnable, this.nextId(), period), delay);
    }

    public Future callSyncMethod(Plugin plugin, Callable task) {
        validate(plugin, task);
        NukkitFuture future = new NukkitFuture(task, plugin, this.nextId());
        this.handle(future, 0L);
        return future;
    }

    public void cancelTask(final int taskId) {
        if (taskId > 0) {
            Task task = this.runners.get(taskId);
            if (task != null) {
                task.cancel0();
            }

            task = new Task(new Runnable() {
                public void run() {
                    if (!this.check(Scheduler.this.temp)) {
                        this.check(Scheduler.this.pending);
                    }

                }

                private boolean check(Iterable collection) {
                    Iterator tasks = collection.iterator();

                    while (tasks.hasNext()) {
                        Task task = (Task) tasks.next();
                        if (task.getTaskId() == taskId) {
                            task.cancel0();
                            tasks.remove();
                            if (task.isSync()) {
                                Scheduler.this.runners.remove(taskId);
                            }

                            return true;
                        }
                    }

                    return false;
                }
            });
            this.handle(task, 0L);

            for (Task taskPending = this.head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
                if (taskPending == task) {
                    return;
                }

                if (taskPending.getTaskId() == taskId) {
                    taskPending.cancel0();
                }
            }

        }
    }

    public void cancelTasks(final Plugin plugin) {
        Validate.notNull(plugin, "Cannot cancel tasks of null plugin");
        Task task = new Task(new Runnable() {
            public void run() {
                this.check(Scheduler.this.pending);
                this.check(Scheduler.this.temp);
            }

            void check(Iterable collection) {
                Iterator tasks = collection.iterator();

                while (tasks.hasNext()) {
                    Task task = (Task) tasks.next();
                    if (task.getOwner().equals(plugin)) {
                        task.cancel0();
                        tasks.remove();
                        if (task.isSync()) {
                            Scheduler.this.runners.remove(task.getTaskId());
                        }
                    }
                }

            }
        });
        this.handle(task, 0L);

        Task runner;
        for (runner = this.head.getNext(); runner != null; runner = runner.getNext()) {
            if (runner == task) {
                return;
            }

            if (runner.getTaskId() != -1 && runner.getOwner().equals(plugin)) {
                runner.cancel0();
            }
        }

        Iterator iterator = this.runners.values().iterator();

        while (iterator.hasNext()) {
            runner = (Task) iterator.next();
            if (runner.getOwner().equals(plugin)) {
                runner.cancel0();
            }
        }

    }

    public void cancelAllTasks() {
        Task task = new Task(new Runnable() {
            public void run() {
                Iterator it = Scheduler.this.runners.values().iterator();

                while (it.hasNext()) {
                    Task task = (Task) it.next();
                    task.cancel0();
                    if (task.isSync()) {
                        it.remove();
                    }
                }

                Scheduler.this.pending.clear();
                Scheduler.this.temp.clear();
            }
        });
        this.handle(task, 0L);

        Task runner;
        for (runner = this.head.getNext(); runner != null && runner != task; runner = runner.getNext()) {
            runner.cancel0();
        }

        Iterator iterator = this.runners.values().iterator();

        while (iterator.hasNext()) {
            runner = (Task) iterator.next();
            runner.cancel0();
        }

    }

    public boolean isCurrentlyRunning(int taskId) {
        Task task = this.runners.get(taskId);
        if (task != null && !task.isSync()) {
            AsyncTask asyncTask = (AsyncTask) task;
            synchronized (asyncTask.getWorkers()) {
                return asyncTask.getWorkers().isEmpty();
            }
        } else {
            return false;
        }
    }

    public boolean isQueued(int taskId) {
        if (taskId <= 0) {
            return false;
        } else {
            Task task;
            for (task = this.head.getNext(); task != null; task = task.getNext()) {
                if (task.getTaskId() == taskId) {
                    if (task.getPeriod() >= -1L) {
                        return true;
                    }

                    return false;
                }
            }

            task = this.runners.get(taskId);
            return task != null && task.getPeriod() >= -1L;
        }
    }

    public List getActiveWorkers() {
        ArrayList<Worker> workers = new ArrayList<>();
        Iterator iterator = this.runners.values().iterator();

        while (iterator.hasNext()) {
            Task taskObj = (Task) iterator.next();
            if (!taskObj.isSync()) {
                AsyncTask task = (AsyncTask) taskObj;
                synchronized (task.getWorkers()) {
                    workers.addAll(task.getWorkers());
                }
            }
        }

        return workers;
    }

    public List getPendingTasks() {
        ArrayList<Task> truePending = new ArrayList<>();

        for (Task pending = this.head.getNext(); pending != null; pending = pending.getNext()) {
            if (pending.getTaskId() != -1) {
                truePending.add(pending);
            }
        }

        ArrayList<Task> pending = new ArrayList<>();
        Iterator iterator = this.runners.values().iterator();

        Task task;
        while (iterator.hasNext()) {
            task = (Task) iterator.next();
            if (task.getPeriod() >= -1L) {
                pending.add(task);
            }
        }

        iterator = truePending.iterator();

        while (iterator.hasNext()) {
            task = (Task) iterator.next();
            if (task.getPeriod() >= -1L && !pending.contains(task)) {
                pending.add(task);
            }
        }

        return pending;
    }

    public void mainThreadHeartbeat(int currentTick) {
        this.currentTick = currentTick;
        List<Task> temp = this.temp;
        this.parsePending();

        while (this.isReady(currentTick)) {
            Task task = (Task) this.pending.remove();
            if (task.getPeriod() < -1L) {
                if (task.isSync()) {
                    this.runners.remove(task.getTaskId(), task);
                }

                this.parsePending();
            } else {
                if (task.isSync()) {
                    try {
                        task.run();
                    } catch (Throwable e) {
                        task.getOwner().getLogger().log(Level.WARN, String.format("Task #%s for %s generated an exception", task.getTaskId(), task.getOwner().getDescription().getFullName()), e);
                    }

                    this.parsePending();
                } else {
                    this.debugTail = this.debugTail.setNext(new AsyncDebugger(currentTick + RECENT_TICKS, task.getOwner(), task.getTaskClass()));
                    this.executor.execute(task);
                }

                long period = task.getPeriod();
                if (period > 0L) {
                    task.setNextRun((long) currentTick + period);
                    temp.add(task);
                } else if (task.isSync()) {
                    this.runners.remove(task.getTaskId());
                }
            }
        }

        this.pending.addAll(temp);
        temp.clear();
        this.debugHead = this.debugHead.getNextHead(currentTick);
    }

    private void addTask(Task task) {
        AtomicReference<Task> tail = this.tail;

        Task tailTask;
        for (tailTask = tail.get(); !tail.compareAndSet(tailTask, task); tailTask = tail.get()) {
        }

        tailTask.setNext(task);
    }

    private Task handle(Task task, long delay) {
        task.setNextRun((long) this.currentTick + delay);
        this.addTask(task);
        return task;
    }

    private static void validate(Plugin plugin, Object task) {
        Validate.notNull(plugin, "Plugin cannot be null");
        Validate.notNull(task, "Task cannot be null");
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
        }
    }

    private int nextId() {
        return this.ids.incrementAndGet();
    }

    private void parsePending() {
        Task head = this.head;
        Task task = head.getNext();

        Task lastTask;
        for (lastTask = head; task != null; task = task.getNext()) {
            if (task.getTaskId() == -1) {
                task.run();
            } else if (task.getPeriod() >= -1L) {
                this.pending.add(task);
                this.runners.put(task.getTaskId(), task);
            }

            lastTask = task;
        }

        for (task = head; task != lastTask; task = head) {
            head = task.getNext();
            task.setNext(null);
        }

        this.head = lastTask;
    }

    private boolean isReady(int currentTick) {
        return !this.pending.isEmpty() && ((Task) this.pending.peek()).getNextRun() <= (long) currentTick;
    }

    public String toString() {
        int debugTick = this.currentTick;
        StringBuilder string = (new StringBuilder("Recent tasks from ")).append(debugTick - RECENT_TICKS).append('-').append(debugTick).append('{');
        this.debugHead.debugTo(string);
        return string.append('}').toString();
    }

    /**
     * @deprecated
     */
    @Deprecated
    public int scheduleSyncDelayedTask(Plugin plugin, NukkitRunnable task, long delay) {
        return this.scheduleSyncDelayedTask(plugin, (Runnable) task, delay);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public int scheduleSyncDelayedTask(Plugin plugin, NukkitRunnable task) {
        return this.scheduleSyncDelayedTask(plugin, (Runnable) task);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public int scheduleSyncRepeatingTask(Plugin plugin, NukkitRunnable task, long delay, long period) {
        return this.scheduleSyncRepeatingTask(plugin, (Runnable) task, delay, period);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public Task runTask(Plugin plugin, NukkitRunnable task) throws IllegalArgumentException {
        return this.runTask(plugin, (Runnable) task);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public Task runTaskAsynchronously(Plugin plugin, NukkitRunnable task) throws IllegalArgumentException {
        return this.runTaskAsynchronously(plugin, (Runnable) task);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public Task runTaskLater(Plugin plugin, NukkitRunnable task, long delay) throws IllegalArgumentException {
        return this.runTaskLater(plugin, (Runnable) task, delay);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public Task runTaskLaterAsynchronously(Plugin plugin, NukkitRunnable task, long delay) throws IllegalArgumentException {
        return this.runTaskLaterAsynchronously(plugin, (Runnable) task, delay);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public Task runTaskTimer(Plugin plugin, NukkitRunnable task, long delay, long period) throws IllegalArgumentException {
        return this.runTaskTimer(plugin, (Runnable) task, delay, period);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public Task runTaskTimerAsynchronously(Plugin plugin, NukkitRunnable task, long delay, long period) throws IllegalArgumentException {
        return this.runTaskTimerAsynchronously(plugin, (Runnable) task, delay, period);
    }
}
