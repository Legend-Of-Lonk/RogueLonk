package org.Lonk.Items;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public class CustomSkullCreator {

    public static ItemStack createSkull(OfflinePlayer player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(player);
        skull.setItemMeta(meta);
        return skull;
    }

    public static ItemStack createSkull(String textureValue) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        try {
            if (textureValue.startsWith("http")) {
                // It's a URL
                URL url = new URL(textureValue);
                textures.setSkin(url);
            } else {
                // It's a Base64 texture value
                String decodedValue = new String(Base64.getDecoder().decode(textureValue));
                String skinURL = extractSkinURL(decodedValue);
                if (skinURL != null) {
                    textures.setSkin(new URL(skinURL));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().getConsoleSender().sendMessage("§c<[!!!WARNING-ROGUELONK!!!]> Error creating skull with texture value: " + textureValue);
            return skull; // Return a regular skull if there's an error
        }

        profile.setTextures(textures);
        meta.setOwnerProfile(profile);
        skull.setItemMeta(meta);

        return skull;
    }

    private static String extractSkinURL(String jsonString) {
        // Simple JSON parsing to extract the URL
        // Note: In a production environment, use a proper JSON parser
        int urlIndex = jsonString.indexOf("\"url\":\"") + 7;
        int urlEndIndex = jsonString.indexOf("\"", urlIndex);
        return jsonString.substring(urlIndex, urlEndIndex);
    }

    public static void placeSkull(Location location, OfflinePlayer player) {
        Block block = location.getBlock();
        block.setType(Material.PLAYER_HEAD);

        Skull skull = (Skull) block.getState();
        skull.setOwningPlayer(player);
        skull.update();
    }

    public static void placeSkull(Location location, String textureValue) {
        Block block = location.getBlock();
        block.setType(Material.PLAYER_HEAD);

        Skull skull = (Skull) block.getState();

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        try {
            if (textureValue.startsWith("http")) {
                // It's a URL
                URL url = new URL(textureValue);
                textures.setSkin(url);
            } else {
                // It's a Base64 texture value
                String decodedValue = new String(Base64.getDecoder().decode(textureValue));
                String skinURL = extractSkinURL(decodedValue);
                if (skinURL != null) {
                    textures.setSkin(new URL(skinURL));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return; // Exit if there's an error
        }

        profile.setTextures(textures);
        skull.setOwnerProfile(profile);
        skull.update();
    }
}
