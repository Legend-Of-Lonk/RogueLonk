package org.Lonk.NPC.events;

import org.Lonk.Items.ItemManager;
import org.Lonk.RogueLonk;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class LonkNPC implements Listener {
    @EventHandler
    public void onPlayerRightClickNPC(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getPersistentDataContainer().has(new NamespacedKey("roguelonk", "npcid"), PersistentDataType.STRING)) {
            if (event.getRightClicked().getPersistentDataContainer().get(new NamespacedKey("roguelonk", "npcid"), PersistentDataType.STRING).equals("lonk_npc")) {
                Player player = event.getPlayer();
                Inventory gui = player.getServer().createInventory(player, 9, "Lonk");

                ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(" ");
                item.setItemMeta(meta);

                for (int i = 0; i < 9; i++) { gui.setItem(i, item); }

                ItemStack shop = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                ItemMeta shopMeta = shop.getItemMeta();
                shopMeta.setDisplayName("§aLonk's Shop");
                shop.setItemMeta(shopMeta);

                gui.setItem(4, shop);


                player.openInventory(gui);
            }
        }
    }

    @EventHandler
    public void OnPlayerInterract(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Lonk")) {
            event.setCancelled(true);
            if(event.getSlot() == 4) {
                event.getWhoClicked().closeInventory();

                Inventory gui = event.getWhoClicked().getServer().createInventory(event.getWhoClicked(), 9*3, "Lonk's Shop");
                for (int i = 0; i < 9*3; i++) {
                    ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(" ");
                    item.setItemMeta(meta);
                    gui.setItem(i, item);
                }

                gui.setItem(10, ItemManager.LonksMurderSword);
                gui.setItem(12, ItemManager.PlayerHeadItem);
                gui.setItem(14, ItemManager.chestplate);

                event.getWhoClicked().openInventory(gui);

            }
        }
    }


}
