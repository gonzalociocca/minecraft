package server.instance.loader.SkyWars.misc;

import org.bukkit.Material;

/**
 * Created by noname on 16/4/2017.
 */
public class CageBlock {
    int locX;
    int locY;
    int locZ;
    Material material;
    byte materialData;

    public CageBlock(int x, int y, int z, Material mat, byte data){
        locX = x;
        locY = y;
        locZ = z;
        material = mat;
        materialData = data;
    }
    public int getX(){
        return locX;
    }
    public int getY(){
        return locY;
    }
    public int getZ(){
        return locZ;
    }
    public Material getMaterial(){
        return material;
    }
    public byte getMaterialData(){
        return materialData;
    }
}
