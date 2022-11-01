package me.gonzalociocca.minigame.misc;

/**
 * Created by noname on 27/3/2017.
 */
public class CustomLocation {
    double locx;
    double locy;
    double locz;
    public CustomLocation(double x, double y, double z){
        locx = x;
        locy = y;
        locz = z;
    }
    public double getX(){
        return locx;
    }
    public double getY(){
        return locy;
    }
    public double getZ(){
        return locz;
    }

    public CustomLocation  mixWith(CustomLocation custom){
        return new CustomLocation(locx+custom.getX(),locy+custom.getY(),locz+custom.getZ());
    }
    public void fuseWith(CustomLocation custom){
        locx = (locx%500)+custom.getX();
        locy = (locy%500)+custom.getY();
        locz = (locz%500)+custom.getZ();
    }

    @Override
    public String toString(){
        return locx+","+locy+","+locz;
    }

    public CustomLocation clone(){
        return new CustomLocation(locx,locy,locz);
    }
}
