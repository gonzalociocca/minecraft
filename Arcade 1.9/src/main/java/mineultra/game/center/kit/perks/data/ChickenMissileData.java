/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.game.center.kit.perks.data;

import org.bukkit.util.Vector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ChickenMissileData
{
    public Player Player;
    public Entity Chicken;
    public Vector Direction;
    public long Time;
    public double LastX;
    public double LastY;
    public double LastZ;
    
    public ChickenMissileData(final Player player, final Entity chicken) {
        super();
        this.Player = player;
        this.Chicken = chicken;
        this.Direction = player.getLocation().getDirection().multiply(0.6);
        this.Time = System.currentTimeMillis();
    }
    
    public boolean HasHitBlock() {
        if (this.LastX != 0.0 && this.LastY != 0.0 && this.LastZ != 0.0) {
            if (Math.abs(this.Chicken.getLocation().getX() - this.LastX) < Math.abs(this.Direction.getX() / 10.0)) {
                return true;
            }
            if (Math.abs(this.Chicken.getLocation().getY() - this.LastY) < Math.abs(this.Direction.getY() / 10.0) && (this.Direction.getY() > 0.0 || -0.02 > this.Direction.getY())) {
                return true;
            }
            if (Math.abs(this.Chicken.getLocation().getZ() - this.LastZ) < Math.abs(this.Direction.getZ() / 10.0)) {
                return true;
            }
        }
        this.LastX = this.Chicken.getLocation().getX();
        this.LastY = this.Chicken.getLocation().getY();
        this.LastZ = this.Chicken.getLocation().getZ();
        return false;
    }
}
