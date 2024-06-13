package org.Lonk.MobManager;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creature;
import org.bukkit.inventory.ItemStack;

public class MobCreator {
    public static void MobCreator(Creature mob, Double hp, Double dmg, Double Spd, ItemStack Cp, ItemStack Leg, ItemStack Boot, ItemStack helm, ItemStack MainHand, ItemStack OffHand){
        mob.setMaxHealth(hp);
        mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(Spd);
        mob.getEquipment().setHelmet(helm);
        mob.getEquipment().setChestplate(Cp);
        mob.getEquipment().setLeggings(Leg);
        mob.getEquipment().setBoots(Boot);
        mob.getEquipment().setItemInMainHand(MainHand);
        mob.getEquipment().setItemInOffHand(OffHand);
        mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(dmg);





    }

}
