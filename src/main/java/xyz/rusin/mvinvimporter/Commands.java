package xyz.rusin.mvinvimporter;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class Commands extends Command implements TabExecutor {
    public static final String COMMAND = "bsh";

    private final File dataFolder;
    private final Consumer<Runnable> runner;

    private List<String> availableFiles = new ArrayList<>();

    public Commands(File dataFolder, Consumer<Runnable> runner) {
        super(COMMAND);
        this.dataFolder = dataFolder;
        this.runner = runner;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        refresh();
        if (args.length == 0) {
            error(sender, "Usage: /bsh [filename.sh] [args]");
        }
        if (!availableFiles.contains(args[0])) {
            error(sender, "Cannot find script file named " + args[0]);
        }
        if (!sender.hasPermission(String.format("%s.%s", COMMAND, args[0]))) {
            error(sender, "No permission to run this script");
        }
        String fullCommand = String.join(" ", args);
        File shellFile = new File(dataFolder, fullCommand);
        msg(sender, "Starting command");
        runCommand(sender, shellFile.getAbsolutePath());
        msg(sender, "Command started successfully");
    }

    private void runCommand(CommandSender sender, String command) {
        runner.accept(() -> {
            try {
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String s;
                while ((s = reader.readLine()) != null) {
                    msg(sender, s);
                }
            } catch (IOException e) {
                error(sender, "Failed running command " + command);
            }
        });
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        refresh();
        if (args.length == 0 || (args.length == 1 && containsStartsWith(availableFiles, args[0]))) {
            return availableFiles;
        }
        return Collections.emptyList();
    }

    private void refresh() {
        availableFiles = Stream.of(dataFolder.list()).filter(name -> name.endsWith(".sh")).collect(toList());
    }

    private boolean containsStartsWith(List<String> list, String startsWith) {
        return list.stream().anyMatch(item -> item.toLowerCase().startsWith(startsWith.toLowerCase()));
    }

    private static void msg(CommandSender sender, String text) {
        sender.sendMessage(TextComponent.fromLegacyText(text, ChatColor.GREEN));
        System.out.println("Bsh plugin: " + text);
    }

    private static void error(CommandSender sender, String text) {
        sender.sendMessage(TextComponent.fromLegacyText(text, ChatColor.RED));
        System.out.println("Bsh plugin: " + text);
    }
}
