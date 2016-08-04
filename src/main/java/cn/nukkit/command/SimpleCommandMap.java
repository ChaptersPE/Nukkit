package cn.nukkit.command;

import cn.nukkit.Server;
import cn.nukkit.command.defaults.*;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;

import java.util.*;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class SimpleCommandMap implements CommandMap {
    protected final Map<String, Command> knownCommands = new HashMap<>();

    private final Server server;

    public SimpleCommandMap(Server server) {
        this.server = server;
        this.setDefaultCommands();
    }

    private void setDefaultCommands() {
        this.register("nukkit", new VersionCommand("version"));
        this.register("nukkit", new PluginsCommand("plugins"));
        this.register("nukkit", new SeedCommand("seed"));
        this.register("nukkit", new HelpCommand("help"));
        this.register("nukkit", new StopCommand("stop"));
        this.register("nukkit", new TellCommand("tell"));
        this.register("nukkit", new DefaultGamemodeCommand("defaultgamemode"));
        this.register("nukkit", new BanCommand("ban"));
        this.register("nukkit", new BanIpCommand("ban-ip"));
        this.register("nukkit", new BanListCommand("banlist"));
        this.register("nukkit", new PardonCommand("pardon"));
        this.register("nukkit", new PardonIpCommand("pardon-ip"));
        this.register("nukkit", new SayCommand("say"));
        this.register("nukkit", new MeCommand("me"));
        this.register("nukkit", new ListCommand("list"));
        this.register("nukkit", new DifficultyCommand("difficulty"));
        this.register("nukkit", new KickCommand("kick"));
        this.register("nukkit", new OpCommand("op"));
        this.register("nukkit", new DeopCommand("deop"));
        this.register("nukkit", new WhitelistCommand("whitelist"));
        this.register("nukkit", new SaveOnCommand("save-on"));
        this.register("nukkit", new SaveOffCommand("save-off"));
        this.register("nukkit", new SaveCommand("save-all"));
        this.register("nukkit", new GiveCommand("give"));
        this.register("nukkit", new EffectCommand("effect"));
        this.register("nukkit", new EnchantCommand("enchant"));
        this.register("nukkit", new ParticleCommand("particle"));
        this.register("nukkit", new GamemodeCommand("gamemode"));
        this.register("nukkit", new KillCommand("kill"));
        this.register("nukkit", new SpawnpointCommand("spawnpoint"));
        this.register("nukkit", new SetWorldSpawnCommand("setworldspawn"));
        this.register("nukkit", new TeleportCommand("tp"));
        this.register("nukkit", new TimeCommand("time"));
        this.register("nukkit", new TimingsCommand("timings"));
        this.register("nukkit", new ReloadCommand("reload"));
        this.register("nukkit", new WeatherCommand("weather"));
        this.register("nukkit", new XpCommand("xp"));

        if ((boolean) this.server.getConfig("debug.commands", false)) {
            this.register("nukkit", new StatusCommand("status"));
            this.register("nukkit", new GarbageCollectorCommand("gc"));
            //this.register("nukkit", new DumpMemoryCommand("dumpmemory"));
        }
    }

    @Override
    public void registerAll(String fallbackPrefix, List<? extends Command> commands) {
        for (Command command : commands) {
            this.register(fallbackPrefix, command);
        }
    }

    @Override
    public boolean register(String fallbackPrefix, Command command) {
        return this.register(fallbackPrefix, command, null);
    }

    @Override
    public boolean register(String fallbackPrefix, Command command, String label) {
        if (label == null) {
            label = command.getName();
        }
        label = label.trim().toLowerCase();
        fallbackPrefix = fallbackPrefix.trim().toLowerCase();

        boolean registered = this.registerAlias(command, false, fallbackPrefix, label);

        List<String> aliases = new ArrayList<>(Arrays.asList(command.getAliases()));

        for (Iterator<String> iterator = aliases.iterator(); iterator.hasNext(); ) {
            String alias = iterator.next();
            if (!this.registerAlias(command, true, fallbackPrefix, alias)) {
                iterator.remove();
            }
        }
        command.setAliases(aliases.stream().toArray(String[]::new));

        if (!registered) {
            command.setLabel(fallbackPrefix + ":" + label);
        }

        command.register(this);

        return registered;
    }

    private boolean registerAlias(Command command, boolean isAlias, String fallbackPrefix, String label) {
        this.knownCommands.put(fallbackPrefix + ":" + label, command);

        //if you're registering a command alias that is already registered, then return false
        boolean alreadyRegistered = this.knownCommands.containsKey(label);
        Command existingCommand = this.knownCommands.get(label);
        boolean existingCommandIsNotVanilla = alreadyRegistered && !(existingCommand instanceof VanillaCommand);
        //basically, if we're an alias and it's already registered, or we're a vanilla command, then we can't override it
        if ((command instanceof VanillaCommand || isAlias) && alreadyRegistered && existingCommandIsNotVanilla) {
            return false;
        }

        //if you're registering a name (alias or label) which is identical to another command who's primary name is the same
        //so basically we can't override the main name of a command, but we can override aliases if we're not an alias

        //added the last statement which will allow us to override a VanillaCommand unconditionally
        if (alreadyRegistered && existingCommand.getLabel() != null && existingCommand.getLabel().equals(label) && existingCommandIsNotVanilla) {
            return false;
        }

        //you can now assume that the command is either uniquely named, or overriding another command's alias (and is not itself, an alias)

        if (!isAlias) {
            command.setLabel(label);
        }

        this.knownCommands.put(label, command);

        return true;
    }

    private String[] parseArguments(String cmdLine) {
        StringBuilder sb = new StringBuilder(cmdLine);
        Vector<String> args = new Vector<>();
        boolean notQuoted = true;
        int start = 0;

        for (int i = 0; i < sb.length(); i++) {
            if (i != 0 && sb.charAt(i - 1) == '\\') {
                if ((sb.charAt(i) == '"' || sb.charAt(i) == ' ')) {
                    sb.deleteCharAt(i - 1);
                    --i;
                    continue;
                }

                if (sb.charAt(i) == '\\') {
                    sb.deleteCharAt(i - 1);
                    continue;
                }
            }

            if (sb.charAt(i) == ' ' && notQuoted) {
                String arg = sb.substring(start, i);
                if (!arg.isEmpty()) {
                    args.add(arg);
                }
                start = i + 1;
            } else if (sb.charAt(i) == '"') {
                sb.deleteCharAt(i);
                --i;
                notQuoted = !notQuoted;
            }
        }

        String arg = sb.substring(start);
        if (!arg.isEmpty()) {
            args.add(arg);
        }
        return args.toArray(new String[0]);
    }

    @Override
    public boolean dispatch(CommandSender sender, String cmdLine) {
        String[] args = parseArguments(cmdLine);

        if (args.length == 0) {
            return false;
        }

        String sentCommandLabel = args[0].toLowerCase();
        String[] newargs = new String[args.length - 1];
        System.arraycopy(args, 1, newargs, 0, newargs.length);
        args = newargs;
        Command target = this.getCommand(sentCommandLabel);

        if (target == null) {
            return false;
        }

        target.timing.startTiming();

        try {
            target.execute(sender, sentCommandLabel, args);
        } catch (Exception e) {
            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.exception"));
            this.server.getLogger().critical(this.server.getLanguage().translateString("nukkit.command.exception", new String[]{cmdLine, target.toString(), Utils.getExceptionMessage(e)}));
            MainLogger logger = sender.getServer().getLogger();
            if (logger != null) {
                logger.logException(e);
            }
        }

        target.timing.stopTiming();

        return true;
    }

    @Override
    public void clearCommands() {
        for (Command command : this.knownCommands.values()) {
            command.unregister(this);
        }
        this.knownCommands.clear();
        this.setDefaultCommands();
    }

    @Override
    public Command getCommand(String name) {
        if (this.knownCommands.containsKey(name)) {
            return this.knownCommands.get(name);
        }
        return null;
    }

    public Map<String, Command> getCommands() {
        return knownCommands;
    }

    public void registerServerAliases() {
        Map<String, List<String>> values = this.server.getCommandAliases();
        for (Map.Entry<String, List<String>> entry : values.entrySet()) {
            String alias = entry.getKey();
            List<String> commandStrings = entry.getValue();
            if (alias.contains(" ") || alias.contains(":")) {
                this.server.getLogger().warning(this.server.getLanguage().translateString("nukkit.command.alias.illegal", alias));
                continue;
            }
            List<String> targets = new ArrayList<>();

            String bad = "";

            for (String commandString : commandStrings) {
                String[] args = commandString.split(" ");
                Command command = this.getCommand(args[0]);

                if (command == null) {
                    if (bad.length() > 0) {
                        bad += ", ";
                    }
                    bad += commandString;
                } else {
                    targets.add(commandString);
                }
            }

            if (bad.length() > 0) {
                this.server.getLogger().warning(this.server.getLanguage().translateString("nukkit.command.alias.notFound", new String[]{alias, bad}));
                continue;
            }

            if (!targets.isEmpty()) {
                this.knownCommands.put(alias.toLowerCase(), new FormattedCommandAlias(alias.toLowerCase(), targets));
            } else {
                this.knownCommands.remove(alias.toLowerCase());
            }
        }
    }
}
