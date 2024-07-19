package org.Lonk.Items;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.Lonk.RogueLonk;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemCreator {

    public static List<String> getAllItemIDs() {
        FileConfiguration itemsConfig = RogueLonk.getPlugin(RogueLonk.class).getItemsConfig();
        return new ArrayList<>(itemsConfig.getKeys(false));
    }

    public static void setToBeam(ItemStack item, Particle particle) {

    }
    public static ItemStack createItem(String ID, Material mat, double dmg, double hp, double spd, double armor, String name, String lore, Boolean enchGlint, Boolean unbreakable, String textureValue) {
        RogueLonk plugin = JavaPlugin.getPlugin(RogueLonk.class);

        if (getAllItemIDs().contains(ID)) {
            ItemStack existingItem= plugin.loadItemsByID(ID);
            if (existingItem != null) {
                Bukkit.getServer().getConsoleSender().sendMessage("§c<[!!!WARNING-ROGUELONK!!!]> Item with ID " + ID + " already exists!");
                return existingItem;
            }

        }


        // Create item
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        String nameMain = name;
        name = name.replace("_", " ");
        name = name.replace("&", "§");
        meta.setDisplayName(name);



        NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
        meta.getPersistentDataContainer().set(dmgKey, PersistentDataType.DOUBLE, dmg);
        NamespacedKey hpKey = new NamespacedKey("roguelonk", "hp");
        meta.getPersistentDataContainer().set(hpKey, PersistentDataType.DOUBLE, hp);
        NamespacedKey spdKey = new NamespacedKey("roguelonk", "spd");
        meta.getPersistentDataContainer().set(spdKey, PersistentDataType.DOUBLE, spd);
        NamespacedKey armorKey = new NamespacedKey("roguelonk", "armor");
        meta.getPersistentDataContainer().set(armorKey, PersistentDataType.DOUBLE, armor);
        NamespacedKey IDKey = new NamespacedKey("roguelonk", "id");
        meta.getPersistentDataContainer().set(IDKey, PersistentDataType.STRING, ID);

        String loreMain = lore;
        lore = "§7" + lore;
        lore = lore.replace("_", " ");
        lore = lore.replace("&", "§");
        lore = lore.replace("%nl%", "%nl%§7");
        String[] flavortextBroken = lore.split("%nl%");

        
        List<String> stats = new ArrayList<>();
        stats.add("§7Stats:");
        if (dmg > 0 || dmg < 0) {stats.add(" §7Damage: §c🗡§f" + dmg);}
        if (hp > 0 || hp < 0) {stats.add(" §7Health: §a❤§f" + hp);}
        if (spd > 0 || spd < 0) {stats.add(" §7Speed: §e🌠§f" + spd);}
        if (armor > 0 || armor < 0) {stats.add(" §7Armor: §b🛡§f" + armor);}
        stats.add(" ");
        stats.addAll(List.of(flavortextBroken));
        meta.setLore(stats);

        if (enchGlint) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            NamespacedKey enchGlintKey = new NamespacedKey("roguelonk", "enchglint");
            meta.getPersistentDataContainer().set(enchGlintKey, PersistentDataType.BOOLEAN, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        meta.setUnbreakable(unbreakable);
        NamespacedKey unbreakableKey = new NamespacedKey("roguelonk", "unbreakable");
        meta.getPersistentDataContainer().set(unbreakableKey, PersistentDataType.BOOLEAN, unbreakable);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);


        if (mat == Material.PLAYER_HEAD) {
            NamespacedKey textureKey = new NamespacedKey("roguelonk", "texture");
            meta.getPersistentDataContainer().set(textureKey, PersistentDataType.STRING, textureValue);
        }

        item.setItemMeta(meta);
        plugin.saveItem(ID, item);
        return item;

    }
}
