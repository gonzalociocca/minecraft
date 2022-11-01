package server.instance.loader.SkyWars.misc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Created by noname on 16/4/2017.
 */
public enum CageEnum {
    Default("",
            new CageBlock(0,0,1, Material.GLASS, (byte)0),
            new CageBlock(0,0,-1, Material.GLASS, (byte)0),
            new CageBlock(1,0,0, Material.GLASS, (byte)0),
            new CageBlock(-1,0,0, Material.GLASS, (byte)0),

            new CageBlock(0,1,1, Material.GLASS, (byte)0),
            new CageBlock(0,1,-1, Material.GLASS, (byte)0),
            new CageBlock(1,1,0, Material.GLASS, (byte)0),
            new CageBlock(-1,1,0, Material.GLASS, (byte)0),

            new CageBlock(0,-1,0, Material.GLASS, (byte)0),
            new CageBlock(0,2,0, Material.GLASS, (byte)0)
    );

    String _id;
    CageBlock[] _blocks;
    CageEnum(String id, CageBlock... blocks){
        _id = id;
        _blocks = blocks;
    }
    public String getID(){
        return _id;
    }

    public void applyAt(Location loc){
        World world = loc.getWorld();
        int blockX = loc.getBlockX();
        int blockY = loc.getBlockY();
        int blockZ = loc.getBlockZ();

        for(CageBlock cb : _blocks){
            Block block = world.getBlockAt(blockX + cb.getX(), blockY + cb.getY(), blockZ + cb.getZ());
            if(block.isEmpty()) {
                block.setTypeIdAndData(cb.getMaterial().getId(), cb.getMaterialData(), false);
            }
        }
    }
    public void removeAt(Location loc){
        World world = loc.getWorld();
        int blockX = loc.getBlockX();
        int blockY = loc.getBlockY();
        int blockZ = loc.getBlockZ();

        for(CageBlock cb : _blocks){
            Block block = world.getBlockAt(blockX + cb.getX(), blockY + cb.getY(), blockZ + cb.getZ());
            if(block.getType() == cb.getMaterial() && block.getData() == cb.getMaterialData()) {
                block.setType(Material.AIR, false);
            }
        }
    }


    public static CageEnum getByID(String id){
        if(id != null && !id.isEmpty()) {
            for (CageEnum num : CageEnum.values()) {
                if (id.equals(num.getID())) {
                    return num;
                }
            }
        }
        return CageEnum.Default;
    }


}
