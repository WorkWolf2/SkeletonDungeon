package me.workwolf.skeletondungeon.Commands;

import me.workwolf.skeletondungeon.SkeletonDungeon;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class reload implements CommandExecutor {

    private final SkeletonDungeon plugin;

    public reload(SkeletonDungeon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) return true;

        if (!Objects.equals(args[0], "reload")) return true;

        if (!(sender instanceof ConsoleCommandSender) && !sender.hasPermission("skeletondungeon.command.reload")) {
            sender.sendMessage(ChatColor.RED + "Non hai il permesso!");
            return true;
        }

        plugin.reloadConfig();
        return true;
    }
}
