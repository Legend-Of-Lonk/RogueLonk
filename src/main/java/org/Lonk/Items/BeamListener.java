package org.Lonk.Items;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class BeamListener implements @NotNull Listener {
    private static final NamespacedKey IDKEY = new NamespacedKey("roguelonk", "id");
    private static final NamespacedKey UseKey = new NamespacedKey("roguelonk", "usebeam");
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check if the item in the player's hand is Lonk's Murder Sword
        if (item.getItemMeta() == null) {
            return;
        }
        if (item.getItemMeta().getPersistentDataContainer().has(UseKey, PersistentDataType.BOOLEAN)) {
            ItemMeta meta = item.getItemMeta();

            // Retrieve the beam properties from the PersistentDataContainer
            Double beamDmg = meta.getPersistentDataContainer().get(new NamespacedKey("roguelonk", "beamdmg"), PersistentDataType.DOUBLE);
            String dustOptionsString = meta.getPersistentDataContainer().get(new NamespacedKey("roguelonk", "dustoptions"), PersistentDataType.STRING);
            Particle.DustOptions dustOptions = stringToDustOptions(dustOptionsString);
            String beamParticleString = meta.getPersistentDataContainer().get(new NamespacedKey("roguelonk", "beamparticle"), PersistentDataType.STRING);
            Particle beamParticle = Particle.valueOf(beamParticleString);
            Double beamLength = meta.getPersistentDataContainer().get(new NamespacedKey("roguelonk", "beamlength"), PersistentDataType.DOUBLE);
            Double beamRadius = meta.getPersistentDataContainer().get(new NamespacedKey("roguelonk", "beamradius"), PersistentDataType.DOUBLE);
            Boolean useDustOptions = meta.getPersistentDataContainer().get(new NamespacedKey("roguelonk", "usedustoptions"), PersistentDataType.BOOLEAN);
            Integer cooldown = meta.getPersistentDataContainer().get(new NamespacedKey("roguelonk", "cooldown"), PersistentDataType.INTEGER);

            // Use the retrieved properties to shoot the beam
            ParticleBeam.shootBeam(player, beamDmg, dustOptions, beamParticle, beamLength, beamRadius, useDustOptions, cooldown);
        }
    }

    public static Particle.DustOptions stringToDustOptions(String dustOptionsString) {
        String[] components = dustOptionsString.split(",");
        if (components.length != 4) {
            throw new IllegalArgumentException("Invalid DustOptions string: " + dustOptionsString);
        }

        int red = Integer.parseInt(components[0]);
        int green = Integer.parseInt(components[1]);
        int blue = Integer.parseInt(components[2]);
        float size = Float.parseFloat(components[3]);

        Color color = Color.fromRGB(red, green, blue);
        return new Particle.DustOptions(color, size);
    }
}
