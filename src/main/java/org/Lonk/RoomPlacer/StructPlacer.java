package org.Lonk.RoomPlacer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class StructPlacer {
    public void placeStructure(Location location, String BuildPrefix, Integer MaxRooms) throws IOException {
        Random random = new Random();
        int randomNumber = random.nextInt(MaxRooms) + 1;
        NamespacedKey key = new NamespacedKey("minecraft", BuildPrefix + randomNumber);

        StructureManager manager = Bukkit.getStructureManager();
        File file = manager.getStructureFile(key);
        Structure struct = manager.loadStructure(file);

        Vector structSize = struct.getSize();
        Vector structCenter = structSize.clone().multiply(0.5);
        double VecZ = structSize.getZ();


        structCenter.setX(1);
        structCenter.setZ((VecZ/ -2.0) + 1.0);
        structCenter.setY(0);

        Location placeLocation = location.add(structCenter);

        struct.place(placeLocation, true, StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());
    }
}
