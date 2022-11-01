/*
package mineultra.game.center.game.games.pack.turboracers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Random;
import mineultra.core.common.util.Colorizer;
import static mineultra.game.center.GameType.TurboRacers;
import mineultra.game.center.centerManager;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.Game.GameState;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityArmorStand;
import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.ItemStack;
import net.minecraft.server.v1_9_R1.MathHelper;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldEvent;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_9_R1.World;
import net.minecraft.server.v1_9_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;


public class CustomMissile extends EntityArmorStand {

  public CustomMissile(World world) {
  super(world);
    }

  
  @Override
  public void setPositionRotation(double d0, double d1, double d2, float f, float f1)
  {
    super.setPositionRotation(d0, d1, d2, f, f1);
  }
  
  public void setLocation(Location l)
  {
    super.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
    world = ((CraftWorld)l.getWorld()).getHandle();
  }




int ran = 0;

public TurboRacers manager = null;


  
  
       Random r = new Random();
       
    public Vector getVelDir(Float yawb, Float pitchb)
  {
    Vector vector = new Vector();
    
    double rotX = yawb;
    double rotY = pitchb;
    
    vector.setY(-Math.sin(Math.toRadians(rotY)));
    
    double xz = Math.cos(Math.toRadians(rotY));
    
    vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
    vector.setZ(xz * Math.cos(Math.toRadians(rotX)));
    
    return vector;
  }  
    

    
    @Override
  public void collide(Entity entity)
  {
  }
 
public boolean directioned = false; 
public Player owner = null;
public int missiltype = -1;

boolean exploding = false;
Player toExplode = null;
Long started = null;

  public boolean hasCollided(){
      Block ab = this.getHandle().getLocation().clone().add(motX, +1.5, motZ).getBlock();
      Block ab2 = this.getHandle().getLocation().clone().add(motX, +2, motZ).getBlock();
      Block ab3 = this.getHandle().getLocation().clone().add(motX*2, +1.5, motZ*2).getBlock();
      Block ab4 = this.getHandle().getLocation().clone().add(motX*2, +2, motZ*2).getBlock();
      if(ab.getType().isSolid() && !ab.getType().name().toLowerCase().contains("stairs") && !ab.getType().name().toLowerCase().contains("step") 
              || ab.getType() == Material.BARRIER
         ||  ab2.getType().isSolid()&& !ab2.getType().name().toLowerCase().contains("stairs") && !ab2.getType().name().toLowerCase().contains("step") 
              || ab3.getType().isSolid() && !ab3.getType().name().toLowerCase().contains("stairs") && !ab3.getType().name().toLowerCase().contains("step") 
              || ab4.getType().isSolid()&& !ab4.getType().name().toLowerCase().contains("stairs") && !ab4.getType().name().toLowerCase().contains("step") ){

          return true;
      }else{

          return false;
      }
  }
  Vector launch = null;
  
        public float getYaw(Vector motion) {
            double dx = motion.getX();
            double dz = motion.getZ();
            double yaw = 0;
            // Set yaw
            if (dx != 0) {
                // Set yaw start value based on dx
                if (dx < 0) {
                    yaw = 1.5 * Math.PI;
                } else {
                    yaw = 0.5 * Math.PI;
                }
                yaw -= Math.atan(dz / dx);
            } else if (dz < 0) {
                yaw = Math.PI;
            }
            return (float) (-yaw * 180 / Math.PI - 90);
        }

  
public void redirect(){
    if(missiltype == -1){
        return;
    }
    Player closest = this.closest();

    
            if(this.hasCollided()){
            this.getHandle().getWorld().createExplosion(this.locX, this.locY, this.locZ, 1F, false, false);
       
            this.die();
            return;
        }

    if(missiltype == 0 && closest != null){
        Location cloc = closest.getLocation();
        Location tloc = this.getHandle().getLocation();
Vector vac = cloc.toVector().subtract(tloc.toVector());
      this.getHandle().setVelocity(vac.clone().multiply(0.1));
   if(exploding == true){
       this.getHandle().getWorld().createExplosion(this.locX, this.locY, this.locZ, 1F, false, false);
       this.exploding = false;
       manager.rotateBump(toExplode,0);
       this.die();
       return;
   }
    }
    else if(missiltype == 1){
        
        if(launch == null){
        launch = owner.getLocation().getDirection();
        }

        this.motX = launch.getX();
        this.motY = launch.getY();
        this.motZ = launch.getZ();
       
        
        if(exploding == true){
             this.getHandle().getWorld().createExplosion(this.locX, this.locY, this.locZ, 1F, false, false);
            this.exploding = false;
            manager.rotateBump(toExplode,0);
        this.die();
        return;
        }

    }else if(missiltype == 2 ){
        Player first = first();
        if(first != null){
        Location cloc = first.getLocation();
        Location tloc = this.getHandle().getLocation();
Vector vac = cloc.toVector().subtract(tloc.toVector());
      this.getHandle().setVelocity(vac.clone().multiply(0.1));
   if(exploding == true){
       this.getHandle().getWorld().createExplosion(this.locX, this.locY, this.locZ, 1F, false, false);
       this.exploding = false;
       manager.rotateBump(toExplode,0);
       this.die();
       return;
   }}
    }else if(missiltype == 3){
        if(launch == null){
        launch = owner.getLocation().getDirection();
        }

        this.motX = launch.getX();
        this.motY = launch.getY();
        this.motZ = launch.getZ();
       
        
        if(exploding == true){
             this.getHandle().getWorld().createExplosion(this.locX, this.locY, this.locZ, 1F, false, false);
            this.exploding = false;
            manager.rotateBump(toExplode,1);
        this.die();
        return;
        }
    }else if(missiltype == 4){
        if(launch == null){
        launch = owner.getLocation().getDirection();
        }

        this.motX = launch.getX();
        this.motY = launch.getY();
        this.motZ = launch.getZ();
       
        
        if(exploding == true){
             this.getHandle().getWorld().createExplosion(this.locX, this.locY, this.locZ, 1F, false, false);
            this.exploding = false;
            manager.rotateBump(toExplode,2);
        this.die();
        return;
        }
    }
}

public Player closest(){
    Player closest = null;
    double distance = -1;
    for(Player pe : manager.GetPlayers(true)){
        if(pe == owner){
            continue;
        }
        if(pe.getWorld() != this.getHandle().getWorld()){
            continue;
        }
        double  dist = this.getHandle().getLocation().distance(pe.getLocation());
        if(distance == -1){
            closest = pe;
            distance = dist;
        }else if(dist < distance){
            closest = pe;
            distance = dist;
        }
                
    }
    if(distance > 0 && distance < 5){
        this.exploding = true;
        toExplode = closest;
    }
    return closest;
}

public Player first(){
    Player first = null;
    double distance = -1;
    for(Player pe : manager.position.keySet()){
        if(pe == owner){
            continue;
        }
        if(pe.getWorld() != this.getHandle().getWorld()){
            continue;
        }
        if(manager.position.get(pe) == 1){
          first = pe;
          distance = this.getHandle().getLocation().distance(pe.getLocation());
        }
    }
    if(distance > 0 && distance < 3){
        this.exploding = true;
        toExplode = first;
    }
    return first;
}

double lastmotX = 0.1F;
double lastmotZ = 0.1F;
double lastmotY = 0.1F;
public void ge(float f, float f1)
  {

       
  
      
        float f5 = 0.91F;
        if (onGround) {
          f5 = world.getType(new BlockPosition(MathHelper.floor(locX), MathHelper.floor(this.getBoundingBox().b) - 1, MathHelper.floor(locZ))).getBlock().frictionFactor * 0.91F;
        }
        float f6 = 0.1627714F / (f5 * f5 * f5);
        float f3;
        if (onGround) {
          f3 = bI() * f6;
        } else {
          f3 = aM;
        }
        a(f, f1, f3);
        f5 = 0.91F;
        if (onGround) {
          f5 = world.getType(new BlockPosition(MathHelper.floor(locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(locZ))).getBlock().frictionFactor * 0.91F;
        }
        if (k_())
        {
          float f4 = 0.15F;
          motX = MathHelper.a(motX, -f4, f4);
          motZ = MathHelper.a(motZ, -f4, f4);
          fallDistance = 0.0F;
          if (motY < -0.15D) {
            motY = -0.15D;
          }
          boolean flag = (isSneaking());
          if ((flag) && (motY < 0.0D)) {
            motY = 0.0D;
          }
        }

        if(motX > 5){
          
        }else if(motX > 0){
            lastmotX = motX;
        }
        if(motX < -5){
         
        }else if(motX < 0){
            lastmotX = motX;
        }
        if(motZ > 5){
       
        }else  if(motZ > 0){
            lastmotZ = motZ;
        }
        if(motZ < -5){
     
        }else if(motZ < 0){
            lastmotZ = motZ;
        }


        move(motX, motY, motZ);
        if ((positionChanged) && (k_())) {
          motY = 0.2D;
        }
        if ((world.isClientSide) && ((!world.isLoaded(new BlockPosition((int)locX, 0, (int)locZ))) || (!world.getChunkAtWorldCoords(new BlockPosition((int)locX, 0, (int)locZ)).o())))
        {
          if (locY > 0.0D) {
            motY = -0.1D;
          } else {
            motY = 0.0D;
          }
        }
        else {
          motY -= 0.08D;
        }
        motY *= 0.9800000190734863D;
        motX *= f5;
        motZ *= f5;
      
    
    aA = aB;
    double d0 = locX - lastX;
    double d1 = locZ - lastZ;
    
    float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
    if (f2 > 1.0F) {
      f2 = 1.0F;
    }
    aB += (f2 - aB) * 0.4F;
    aC += aB;
  }

float amb = 0;

    @Override
  public void g(float sideMot, float forMot)
  {
if(!owner.isOnline()){
    this.die();
    return;
}
      
      
      
      if(started == null){
          started = System.currentTimeMillis()+15000L;
      }
      if(started < System.currentTimeMillis()){
          this.die();
          return;
      }
if(directioned && owner != null){
    this.velocityChanged = true;
     this.redirect();
forMot = 0.98F;

}

Vector vec =  this.getHandle().getVelocity();
 
PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_LARGE,true,(float)locX+(float)(vec.getX()*-1)*2,(float)locY,(float)locZ+(float)(vec.getZ()*-1)*2,(float)r.nextGaussian()/100, 0F ,(float)r.nextGaussian()/100,10,0);

 for(Player p : Bukkit.getOnlinePlayers()){
     if(p.getWorld() != this.getHandle().getWorld()){
         continue;
     }
    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
 }

this.yaw = this.getYaw(new Vector(motX/10,motY,motZ/10));

this.getHandle().setHeadPose(new EulerAngle(motX/10,motY,motZ/10));

this.velocityChanged = true; 
this.setGravity(true);
this.ge(sideMot, forMot);
} 
  
  CraftArmorStand stand = new CraftArmorStand((CraftServer) this.getBukkitEntity().getServer(),this);
  public CraftArmorStand getHandle(){
      return stand;
  }

  
    @Override
  public void t_()
  {
      for (int j = 0; j < 5; j++)
       {

        ItemStack itemstack = getEquipment(j);

          ((WorldServer)world).getTracker().a(this, new PacketPlayOutEntityEquipment(getId(), j, itemstack));
          if (itemstack != null) {
             
            this.getAttributeMap().a(itemstack.B());
          }
          if (itemstack != null) {
            this.getAttributeMap().b(itemstack.B());
          }

        }
this.m();
  }
  
    @Override
  protected void h()
  {
    super.h();
    initDatawatcher();
  }
  

    @Override
  protected void a(BlockPosition blockposition, net.minecraft.server.v1_9_R1.Block block)
  {
    super.a(blockposition, (net.minecraft.server.v1_9_R1.Block) block);
   
    this.a(blockposition.getX(), blockposition.getY(), blockposition.getZ(), block);
  }
  
  protected void a(int i, int j, int k, net.minecraft.server.v1_9_R1.Block block)
  {
    super.a(new BlockPosition(i, j, k), block);
    makeStepSound(i, j, k, block);
  }
  
  protected void makeStepSound(int i, int j, int k, net.minecraft.server.v1_9_R1.Block block)
  {
    makeStepSound();
  }
  
  protected void initDatawatcher() {}
  
  protected void makeStepSound() {}
  
  protected void doJumpAnimation() {}
  
    @Override
  public void b(NBTTagCompound nbttagcompound) {}
  
    @Override
  public boolean c(NBTTagCompound nbttagcompound)
  {
    return false;
  }
  
    @Override
  public void a(NBTTagCompound nbttagcompound) {}
  
    @Override
  public boolean d(NBTTagCompound nbttagcompound)
  {
    return false;
  }

    @Override
  public void e(NBTTagCompound nbttagcompound) {}
  
    @Override
    protected void bL()
  {
      
  }
  
}
  


*/