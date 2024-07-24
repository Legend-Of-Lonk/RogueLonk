package org.Lonk.NPC;

import org.Lonk.RogueLonk;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class npcManager {


    public static Entity LonkNpc;

    public static void init(){
        Bukkit.getConsoleSender().sendMessage("§a<[ROGUELONK]> Lonk NPC created!");
        CreateLonkNpc();

    }

    public static void makeNpcFacePlayer(){
        npcCreator npcCreator = new npcCreator();
        npcCreator.makeNpcsLookAtNearestPlayer();
    }

    private static void CreateLonkNpc(){
        Entity npc = Bukkit.getServer().getWorlds().get(0).spawnEntity(new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0), EntityType.HUSK);
        npc.remove(); // Remove the mob immediately after creation

        // Set the NPC's name and skin
        String name = "Lonk";
        String skin = "Lonkesorous";
        String ID = "lonk_npc";

        // Create the NPC
        LonkNpc = npcCreator.npcCreator(name, npc, skin, ID);
    }


}
