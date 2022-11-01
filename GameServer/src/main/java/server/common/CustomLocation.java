package server.common;

/**
 * Created by noname on 27/3/2017.
 */
public class CustomLocation {
    double locx;
    double locy;
    double locz;
    float yaw;
    float pitch;

    public CustomLocation(double x, double y, double z) {
        locx = x;
        locy = y;
        locz = z;
        yaw = 0;
        pitch = 0;
    }
    public CustomLocation(double x, double y, double z, float newYaw, float newPitch) {
        locx = x;
        locy = y;
        locz = z;
        yaw = newYaw;
        pitch = newPitch;
    }

    public CustomLocation addX(double morex){
        locx+=morex;
        return this;
    }

    public CustomLocation addY(double morey){
        locy+=morey;
        return this;
    }

    public CustomLocation addZ(double morez){
        locz+=morez;
        return this;
    }

    public double getX() {
        return locx;
    }

    public double getY() {
        return locy;
    }

    public double getZ() {
        return locz;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public CustomLocation mixWith(CustomLocation custom) {
        return new CustomLocation(locx + custom.getX(), locy + custom.getY(), locz + custom.getZ(), yaw + custom.getYaw(), pitch + custom.getPitch());
    }

    @Override
    public String toString() {
        return Double.valueOf(locx) + "," + Double.valueOf(locy) + "," + Double.valueOf(locz) + "," + Float.valueOf(yaw) + "," + Float.valueOf(pitch);
    }

    public static CustomLocation fromString(String str){
        String[] data = str.split(",");
        return new CustomLocation(Double.parseDouble(data[0]),Double.parseDouble(data[1]),Double.parseDouble(data[2]), data.length > 3 ? Float.parseFloat(data[3]) : 0, data.length > 4 ? Float.parseFloat(data[4]) : 0);
    }

    @Override
    public CustomLocation clone() {
        return new CustomLocation(locx, locy, locz, yaw, pitch);
    }
}
