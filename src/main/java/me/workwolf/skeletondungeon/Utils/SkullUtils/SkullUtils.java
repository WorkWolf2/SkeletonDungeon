package me.workwolf.skeletondungeon.Utils.SkullUtils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.Base64;
import java.util.Objects;

public class SkullUtils {
    public SkullUtils() {
    }

    public String getSkullBase64FromBlock(Block block) {
        if (!block.getType().equals(Material.PLAYER_HEAD)) return null;

        Skull skull = (Skull) block.getState();
        PlayerTextures textures = Objects.requireNonNull(skull.getOwnerProfile()).getTextures();
        URL skinUrl = textures.getSkin();

        String encoder = "{\"textures\":{\"SKIN\":{\"url\":\""+ skinUrl +"\"}}}";

        return Base64.getEncoder().encodeToString(encoder.getBytes());
    }
}
