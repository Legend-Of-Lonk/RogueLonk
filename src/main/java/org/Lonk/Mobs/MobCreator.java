package org.Lonk.Mobs;

import org.Lonk.Items.ItemManager;
import org.Lonk.RogueLonk;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MobCreator implements Listener {

    @EventHandler
    public void onMobDie(EntityDeathEvent event) {
        LivingEntity mob = event.getEntity();
        NamespacedKey key = new NamespacedKey("roguelonk", "drops");
        if (mob.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            event.getDrops().clear();
            String dropsString = mob.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            // Remove the brackets from the string and split it into an array
            String[] dropsArray = dropsString.substring(1, dropsString.length() - 1).split(", ");
            for (String drop : dropsArray) {
                // Split the drop into the item ID and the drop chance
                String[] parts = drop.split(":");
                String itemId = parts[0];
                double dropChance = Double.parseDouble(parts[1]);
                // Generate a random number between 0 and 1
                double random = Math.random();
                // If the random number is less than or equal to the drop chance
                if (random <= dropChance) {
                    // Retrieve the ItemStack from your ItemManager using the item ID
                    RogueLonk plugin = JavaPlugin.getPlugin(RogueLonk.class);
                    ItemStack item = plugin.loadItemsByID(itemId);
                    if (item != null) {
                        // Drop the item at the location of the mob
                        mob.getWorld().dropItemNaturally(mob.getLocation(), item);
                    }
                }
            }
        }
    }

    public static List<String> getAllMobIDs() {
        FileConfiguration mobConfig = RogueLonk.getPlugin(RogueLonk.class).getMobConfig();
        if (mobConfig != null) {
            Set<String> keys = mobConfig.getKeys(false); // Get all keys at the root level
            return new ArrayList<>(keys); // Convert the Set to a List and return it
        }
        return new ArrayList<>(); // Return an empty list if the mob config is null
    }

    public static LivingEntity MobDrops(String id, Map<ItemStack, Double> items, LivingEntity mob) {
        RogueLonk plugin = JavaPlugin.getPlugin(RogueLonk.class);
        List<String> drops = new ArrayList<>();
        for (Map.Entry<ItemStack, Double> entry : items.entrySet()) {
            ItemStack item = entry.getKey();
            double dropChance = entry.getValue();
            if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey("roguelonk", "id"), PersistentDataType.STRING)) {
                String itemId = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey("roguelonk", "id"), PersistentDataType.STRING);
                drops.add(itemId + ":" + dropChance);
            }
        }
        mob.getPersistentDataContainer().set(new NamespacedKey("roguelonk", "drops"), PersistentDataType.STRING, drops.toString());

        plugin.saveMob(id, (Creature) mob);
        return mob;
    }
    public static LivingEntity MobCreator(String ID, Entity entity, Double hp, Double dmg, Double Spd, ItemStack Cp, ItemStack Leg, ItemStack Boot, ItemStack helm, ItemStack MainHand, ItemStack OffHand,int age){
        RogueLonk plugin = JavaPlugin.getPlugin(RogueLonk.class);


        if (getAllMobIDs().contains(ID)) {
            Location loc = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
            LivingEntity existingMob = plugin.loadMobById(ID, loc);
            existingMob.remove();
            if (existingMob != null) {
                return existingMob;
            }
        }

        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.setMaxHealth(hp);
            livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(Spd);
            livingEntity.getEquipment().setHelmet(helm);
            livingEntity.getEquipment().setChestplate(Cp);
            livingEntity.getEquipment().setLeggings(Leg);
            livingEntity.getEquipment().setBoots(Boot);
            livingEntity.getEquipment().setItemInMainHand(MainHand);
            livingEntity.getEquipment().setItemInOffHand(OffHand);
            livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(dmg);

            NamespacedKey idKey = new NamespacedKey("roguelonk", "mobid");
            livingEntity.getPersistentDataContainer().set(idKey, PersistentDataType.STRING, ID);

            NamespacedKey ageKey = new NamespacedKey("roguelonk", "age");
            livingEntity.getPersistentDataContainer().set(ageKey, PersistentDataType.INTEGER, age);


            plugin.saveMob(ID, (Creature) livingEntity);
            return livingEntity;
        }
        return null;
    }

}
