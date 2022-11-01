
package mineultra.game.center.game.games.pack.turboracers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Random;
import mineultra.core.common.util.Colorizer;
import static mineultra.game.center.GameType.TurboRacers;
import mineultra.game.center.centerManager;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.Game.GameState;
import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.EntityArmorStand;
import net.minecraft.server.v1_8_R2.EntityLiving;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.EnumParticle;
import net.minecraft.server.v1_8_R2.ItemStack;
import net.minecraft.server.v1_8_R2.MathHelper;
import net.minecraft.server.v1_8_R2.NBTTagCompound;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R2.World;
import net.minecraft.server.v1_8_R2.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R2.CraftServer;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;


public class CustomItemDrop extends EntityArmorStand {

    public CustomItemDrop(World world) {
  super(world);
  
    }

  
    @Override
  public void setPositionRotation(double d0, double d1, double d2, float f, float f1)
  {
    super.setPositionRotation(d0, d1, d2, f, f1);
  }
  
  public void setLocation(Location l)
  {
    setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
    world = ((CraftWorld)l.getWorld()).getHandle();
  }


double multYaw = 90;

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
 
float amb  = -180;
boolean gived = false;
int giveval = 0;
ItemStack equip = null;

public boolean box = false;
public boolean coin = false;
    @Override
  public void g(float sideMot, float forMot)
  {

    if(coin){
     if(!gived){
     for(Player p : manager.GetPlayers(true)){
         if(gived){
             continue;
         }
         if(p.getWorld() != this.getHandle().getWorld()){
             continue;
         }

         if(p.getLocation().distanceSquared(this.getHandle().getLocation()) < 3){

             this.gived = true;
             p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.3F, 1.3F);
p.sendMessage(Colorizer.Color("&f&l+1 &bCoins"));

if(!this.manager.winnedcoins.containsKey(p)){
    manager.winnedcoins.put(p, 0);
}else{
    manager.winnedcoins.put(p, manager.winnedcoins.get(p)+1);
}
             if(this.getEquipment(4) != null){
             equip = this.getEquipment(4);
            this.setEquipment(4,CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.AIR)));
             }
             
                     
         }
     }}else{
         
         giveval++;
         if(giveval >= 25){
             giveval = 0;
             gived = false;
         this.setEquipment(4, equip);            
         }
     }
     }
    
    if(box){
     if(!gived){
     for(Player p : manager.GetPlayers(true)){
         if(gived){
             continue;
         }
         if(p.getWorld() != this.getHandle().getWorld()){
             continue;
         }

         if(p.getLocation().distanceSquared(this.getHandle().getLocation()) < 3){
if(manager.inbox.containsKey(p)){
    continue;
}
             this.gived = true;
             p.playSound(p.getLocation(), Sound.CREEPER_HISS, 1.3F, 1.3F);
p.sendMessage(Colorizer.Color("&9Agarraste un &fMisteryBox!"));
manager.sendBox(p);

             if(this.getEquipment(4) != null){
             equip = this.getEquipment(4);
            this.setEquipment(4,CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.AIR)));
             }
             
                     
         }
     }}else{
         
         giveval++;
         if(giveval >= 25){
             giveval = 0;
             gived = false;
         this.setEquipment(4, equip);            
         }
     }
     }
    
    
    
    if(amb > 180){
        amb = -180+(amb-180);
    }
    if(amb < -180){
        amb = 180-(amb+180);
    }
    this.yaw = amb;
    this.pitch = -90F;
    this.setYawPitch(yaw, pitch);
      amb+=12;
      Vector vec = this.getVelDir(amb,-90F);
      this.getHandle().setHeadPose(new EulerAngle(vec.getX(),vec.getY(),vec.getZ()));
    
    
    
this.setGravity(false);
  super.g(sideMot, forMot);
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
  protected void a(BlockPosition blockposition, net.minecraft.server.v1_8_R2.Block block)
  {
    super.a(blockposition, (net.minecraft.server.v1_8_R2.Block) block);
   
    this.a(blockposition.getX(), blockposition.getY(), blockposition.getZ(), block);
  }
  
  protected void a(int i, int j, int k, net.minecraft.server.v1_8_R2.Block block)
  {
    super.a(new BlockPosition(i, j, k), block);
    makeStepSound(i, j, k, block);
  }
  
  protected void makeStepSound(int i, int j, int k, net.minecraft.server.v1_8_R2.Block block)
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
  


