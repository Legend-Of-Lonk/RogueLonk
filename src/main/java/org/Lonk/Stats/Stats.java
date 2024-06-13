package org.Lonk.Stats;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.eclipse.sisu.launch.Main;

public class Stats implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        // Code here
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            //Armor
            ItemStack Helmet = player.getInventory().getHelmet();
            ItemStack Chestplate = player.getInventory().getChestplate();
            ItemStack Leggings = player.getInventory().getLeggings();
            ItemStack Boots = player.getInventory().getBoots();
            ItemStack MainHand = player.getInventory().getItemInMainHand();
            ItemStack OffHand = player.getInventory().getItemInOffHand();



            // DMG
            Double MainHandDmg = 0.0;
            Double OffHandDmg = 0.0;
            Double ChestplateDmg = 0.0;
            Double LeggingsDmg = 0.0;
            Double BootsDmg = 0.0;
            Double HelmetDmg = 0.0;

            if (MainHand != null) {
                NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                if (MainHand.getItemMeta() != null) {
                    if (MainHand.getItemMeta().getPersistentDataContainer().has(dmgKey, PersistentDataType.DOUBLE)) {
                        MainHandDmg = MainHand.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                    }
                }
            }
            if (OffHand != null) {
                NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                if (OffHand.getItemMeta() != null) {
                    OffHandDmg = OffHand.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                }
            }
            if (Chestplate != null) {
                NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                if (Chestplate.getItemMeta() != null) {
                    ChestplateDmg = Chestplate.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                }
            }
            if (Leggings != null) {
                NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                if (Leggings.getItemMeta() != null) {
                    LeggingsDmg = Leggings.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                }
            }
            if (Boots != null) {
                NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                if (Boots.getItemMeta() != null) {
                    BootsDmg = Boots.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                }
            }
            if (Helmet != null) {
                NamespacedKey dmgKey = new NamespacedKey("roguelonk", "dmg");
                if (Helmet.getItemMeta() != null) {
                    HelmetDmg = Helmet.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
                }
            }





            Double FinalDmg = MainHandDmg + OffHandDmg + ChestplateDmg + LeggingsDmg + BootsDmg + HelmetDmg;
            event.setDamage(FinalDmg);



        }
    }






}
