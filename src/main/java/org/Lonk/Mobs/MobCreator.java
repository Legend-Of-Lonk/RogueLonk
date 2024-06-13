package org.Lonk.Mobs;

import org.Lonk.RogueLonk;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class MobCreator {
    public static LivingEntity MobCreator(String ID, Entity entity, Double hp, Double dmg, Double Spd, ItemStack Cp, ItemStack Leg, ItemStack Boot, ItemStack helm, ItemStack MainHand, ItemStack OffHand){
        RogueLonk plugin = JavaPlugin.getPlugin(RogueLonk.class);
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

            plugin.saveMob(ID, (Creature) livingEntity);
            return livingEntity;
        }
        return null;
    }

}
