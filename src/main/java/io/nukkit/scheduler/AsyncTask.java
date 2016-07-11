package io.nukkit.scheduler;


import io.nukkit.plugin.Plugin;
import org.apache.commons.lang.UnhandledException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

class AsyncTask extends Task {
    private final LinkedList<Worker> workers = new LinkedList<>();
    private final Map<Integer, Task> runners;

    AsyncTask(Map<Integer, Task> runners, Plugin plugin, Runnable task, int id, long delay) {
        super(plugin, task, id, delay);
        this.runners = runners;
    }

    public boolean isSync() {
        return false;
    }

    public void run() {
        final Thread thread = Thread.currentThread();
        synchronized (this.workers) {
            if (this.getPeriod() == -2L) {
                return;
            }

            this.workers.add(new Worker() {
                @Override
                public Thread getThread() {
                    return thread;
                }

                @Override
                public int getTaskId() {
                    return AsyncTask.this.getTaskId();
                }

                @Override
                public Plugin getOwner() {
                    return AsyncTask.this.getOwner();
                }
            });
        }

        Throwable throwable = null;

        try {
            super.run();
        } catch (Throwable e) {
            throwable = e;
            throw new UnhandledException(String.format("Plugin %s generated an exception while executing task %s", this.getOwner().getDescription().getFullName(), this.getTaskId()), e);
        } finally {
            synchronized (this.workers) {
                try {
                    Iterator workers = this.workers.iterator();
                    boolean removed = false;

                    while (workers.hasNext()) {
                        if (((Worker) workers.next()).getThread() == thread) {
                            workers.remove();
                            removed = true;
                            break;
                        }
                    }

                    if (!removed) {
                        throw new IllegalStateException(String.format("Unable to remove worker %s on task %s for %s", thread.getName(), this.getTaskId(), this.getOwner().getDescription().getFullName()), throwable);
                    }
                } finally {
                    if (this.getPeriod() < 0L && this.workers.isEmpty()) {
                        this.runners.remove(this.getTaskId());
                    }

                }

            }
        }

    }

    LinkedList<Worker> getWorkers() {
        return this.workers;
    }

    @Override
    boolean cancel0() {
        synchronized (this.workers) {
            this.setPeriod(-2L);
            if (this.workers.isEmpty()) {
                this.runners.remove(this.getTaskId());
            }

            return true;
        }
    }
}
