package me.workwolf.skeletondungeon.Events;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.th0rgal.oraxen.api.OraxenItems;
import me.workwolf.skeletondungeon.SkeletonDungeon;
import me.workwolf.skeletondungeon.Utils.ConfigManager.ConfigManager;
import me.workwolf.skeletondungeon.Utils.SkullUtils.SkullUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class onPlayerInteract implements Listener {

    private final SkeletonDungeon plugin;

    ConfigManager settings;
    SkullUtils skullUtils;

    public onPlayerInteract(SkeletonDungeon plugin) {
        this.plugin = plugin;
        this.settings = new ConfigManager(plugin);
        this.skullUtils = new SkullUtils();
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) throws SQLException {
        if (e.getClickedBlock() == null) return;
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!e.getClickedBlock().getType().equals(Material.PLAYER_HEAD)) return;
        if (!plugin.getSql().getName()) {
            plugin.getSql().addDungeon(settings.getDungeonName(), false);
        }

        Player p = e.getPlayer();

        ItemStack itemStack = p.getInventory().getItemInMainHand();
        String item_id = OraxenItems.getIdByItem(itemStack);

        if (item_id == null) return;
        if (!item_id.equals(settings.getItemId())) return;

        String base = skullUtils.getSkullBase64FromBlock(e.getClickedBlock());

        if (!settings.getBase64().equals(base)) return;

        Location teleportLoc = settings.getTeleportLocation();
        Location bossLoc = settings.getBossLocation();

        List<String> commands = settings.getEnterCommands();
        List<String> replacedName = new ArrayList<>();

        for (String i : commands) {
            replacedName.add(i.replace("{user}", p.getName()).replace("{messaggio}", settings.getTitleMsg()));
        }

        replacedName.forEach(i -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), i));

        int itemAmount = itemStack.getAmount();
        itemStack.setAmount(itemAmount - 1);
        p.teleport(teleportLoc);

        if (!plugin.getSql().getOpened(settings.getDungeonName())) {
            MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(settings.getBoss()).orElse(null);
            if (mob != null) {
                ActiveMob boss = mob.spawn(BukkitAdapter.adapt(bossLoc), 1);
                Entity entity = boss.getEntity().getBukkitEntity();
            }
            plugin.getSql().updateOpenedValue(settings.getDungeonName(), true);
        }

    }
}
