package org.Lonk.Mobs;

import org.Lonk.RogueLonk;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MobCreator {

    public static List<String> getAllMobIDs() {
        FileConfiguration mobConfig = RogueLonk.getPlugin(RogueLonk.class).getMobConfig();
        if (mobConfig != null) {
            Set<String> keys = mobConfig.getKeys(false); // Get all keys at the root level
            return new ArrayList<>(keys); // Convert the Set to a List and return it
        }
        return new ArrayList<>(); // Return an empty list if the mob config is null
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
