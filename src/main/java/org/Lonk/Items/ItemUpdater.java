package org.Lonk.Items;

import org.Lonk.RogueLonk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemUpdater implements Listener {

    @EventHandler
    public static void UpdatePlayerItems(PlayerJoinEvent event) {
        RogueLonk plugin = JavaPlugin.getPlugin(RogueLonk.class);
        Player player = event.getPlayer();
        NamespacedKey IDKey = new NamespacedKey("roguelonk", "id");
        for(int i = 0 ; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null) {
                if (item.getItemMeta().getPersistentDataContainer().has(IDKey, PersistentDataType.STRING)) {
                    ItemStack updatedItem = plugin.loadItemsByID(item.getItemMeta().getPersistentDataContainer().get(IDKey, PersistentDataType.STRING));
                    player.getInventory().setItem(i, updatedItem);
                }
            }
        }
    }

}
