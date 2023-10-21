package me.workwolf.skeletondungeon;

import me.workwolf.skeletondungeon.Events.BossDeath;
import me.workwolf.skeletondungeon.Events.onPlayerInteract;
import me.workwolf.skeletondungeon.Utils.Database.Database;
import me.workwolf.skeletondungeon.Utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public final class SkeletonDungeon extends JavaPlugin {

    public Database sql;

    public Database getSql() {
        return sql;
    }

    @Override
    public void onEnable() {
        Logger.log(Logger.LogLevel.OUTLINE, "*********************");
        Logger.log(Logger.LogLevel.INFO, "Connettendo il database!");
        Conn();
        Logger.log(Logger.LogLevel.INFO, "Database Connesso con successo!");
        Logger.log(Logger.LogLevel.INFO, "Caricando i file di config!");
        saveDefaultConfig();
        Logger.log(Logger.LogLevel.INFO, "File di config caricati con successo!");
        getServer().getPluginManager().registerEvents(new onPlayerInteract(this), this);
        getServer().getPluginManager().registerEvents(new BossDeath(this), this);
        Logger.log(Logger.LogLevel.OUTLINE, "*********************");
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void Conn() {
        File file = new File(getDataFolder() + "/database.db");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            sql = new Database(this, file.getAbsolutePath());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
