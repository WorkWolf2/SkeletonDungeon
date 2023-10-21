package me.workwolf.skeletondungeon.Utils.ConfigManager;

import me.workwolf.skeletondungeon.SkeletonDungeon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Float.parseFloat;

public class ConfigManager {

    private final SkeletonDungeon plugin;

    public ConfigManager(SkeletonDungeon plugin) {
        this.plugin = plugin;
    }

    public String getDungeonName() {
        return plugin.getConfig().getString("dungeon.name");
    }

    public String getItemId() {
        return plugin.getConfig().getString("dungeon.item-id");
    }

    public String getBase64() { return plugin.getConfig().getString("dungeon.base64-block"); }

    public String getBoss() { return plugin.getConfig().getString("dungeon.boss-name"); }

    public Location getTeleportLocation() {
        World world = Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("location.teleport.world")));
        double x = plugin.getConfig().getDouble("location.teleport.x");
        double y = plugin.getConfig().getDouble("location.teleport.y");
        double z = plugin.getConfig().getDouble("location.teleport.z");
        double yaw = plugin.getConfig().getDouble("location.teleport.yaw");
        double pitch = plugin.getConfig().getDouble("location.teleport.pitch");

        return new Location(world, x, y, z, parseFloat(Double.toString(yaw)), parseFloat(Double.toString(pitch)));
    }


    public Location getBossLocation() {
        World world = Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("location.boss.world")));
        double x = plugin.getConfig().getDouble("location.boss.x");
        double y = plugin.getConfig().getDouble("location.boss.y");
        double z = plugin.getConfig().getDouble("location.boss.z");
        double yaw = plugin.getConfig().getDouble("location.boss.yaw");
        double pitch = plugin.getConfig().getDouble("location.boss.pitch");

        return new Location(world, x, y, z, parseFloat(Double.toString(yaw)), parseFloat(Double.toString(pitch)));
    }

    public Location getSpawnLocation() {
        World world = Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("location.spawn.world")));
        double x = plugin.getConfig().getDouble("location.spawn.x");
        double y = plugin.getConfig().getDouble("location.spawn.y");
        double z = plugin.getConfig().getDouble("location.spawn.z");
        double yaw = plugin.getConfig().getDouble("location.spawn.yaw");
        double pitch = plugin.getConfig().getDouble("location.spawn.pitch");

        return new Location(world, x, y, z, parseFloat(Double.toString(yaw)), parseFloat(Double.toString(pitch)));
    }

    public World getBossWorld() {
        return Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("location.boss.world")));
    }

    public List<String> getRewardCommands() {
        return plugin.getConfig().getStringList("rewards");
    }

    public List<String> getEnterCommands() {
        return plugin.getConfig().getStringList("entering");
    }

    public String getTitleMsg() {
        return plugin.getConfig().getString("messages.title");
    }

    public String translate(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getExitMsg() {
        return this.translate(plugin.getConfig().getString("messages.exit"));
    }
}
