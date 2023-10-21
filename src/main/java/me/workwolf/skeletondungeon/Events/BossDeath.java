package me.workwolf.skeletondungeon.Events;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import me.workwolf.skeletondungeon.SkeletonDungeon;
import me.workwolf.skeletondungeon.Utils.ConfigManager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BossDeath implements Listener{
    private final SkeletonDungeon plugin;
    ConfigManager settings;

    public BossDeath(SkeletonDungeon plugin) {
        this.plugin = plugin;
        this.settings = new ConfigManager(plugin);
    }

    @EventHandler
    public void onBossDeath(MythicMobDeathEvent e) throws SQLException {
        if (e.getKiller() instanceof Player && e.getKiller().getWorld().equals(settings.getBossWorld())) {
            Player p = (Player) e.getKiller();
            if (e.getMob().getMobType().equals(settings.getBoss())) {
                List<String> commands = settings.getRewardCommands();
                List<String> replacedName = new ArrayList<>();

                for (String i : commands) {
                    replacedName.add(i.replace("{user}", e.getKiller().getName()));
                }

                replacedName.forEach(i -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), i));

                plugin.getSql().updateOpenedValue(settings.getDungeonName(), false);

                Location back = settings.getSpawnLocation();
                p.sendMessage(settings.getExitMsg());
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        p.teleport(back);
                    }
                }, 10 * 20L);
                }
            }
        }
    }

