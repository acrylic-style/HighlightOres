package xyz.acrylicstyle.highlightOre.util;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

import java.util.HashMap;

public class BlockDataCache extends HashMap<Material, CraftBlockData> {
    public CraftBlockData getOrCreate(Material material) {
        if (!this.containsKey(material)) this.put(material, ((CraftBlockData) material.createBlockData()));
        return this.get(material);
    }
}
