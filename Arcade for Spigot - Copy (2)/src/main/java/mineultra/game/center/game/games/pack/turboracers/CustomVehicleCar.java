
package mineultra.game.center.game.games.pack.turboracers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Random;
import static mineultra.game.center.GameType.TurboRacers;
import mineultra.game.center.centerManager;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.Game.GameState;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;


public class CustomVehicleCar extends EntityArmorStand {

  public CustomVehicleCar(World world) {
  super(world);
  }

 EnumParticle trail = EnumParticle.CRIT;
 
  public void setTrail(int totrail){
      try{
          EnumParticle ret = byID(totrail);
          if(ret != null){
              trail = ret; 
          }         
      }catch(Exception e){
System.out.println("Trail no existente, ID: "+totrail);
      }
  }
  
  public EnumParticle byID(int id){
      EnumParticle ret = null;
  
    if(id == 0){
          ret = EnumParticle.CRIT;
    }else if(id==1){
        ret = EnumParticle.CRIT_MAGIC;
    }else if(id == 2){
        ret = EnumParticle.CLOUD;
    }else if(id == 3){
        ret = EnumParticle.DRIP_LAVA;
    }
    else if(id == 4){
        ret = EnumParticle.DRIP_WATER;
    }else if(id == 5){
        ret = EnumParticle.FIREWORKS_SPARK;
    }else if(id == 6){
        ret = EnumParticle.FLAME;
    }else if(id == 7){
        ret = EnumParticle.HEART;
    }else if(id == 8){
        ret = EnumParticle.TOWN_AURA;
    }else if(id == 9){
        ret = EnumParticle.LAVA;
    }else if(id == 10){
        ret = EnumParticle.NOTE;
    }else if(id == 11){
        ret = EnumParticle.PORTAL;
    }else if(id == 12){
        ret = EnumParticle.REDSTONE;
    }else if(id == 13){
        ret = EnumParticle.SLIME;
    }else if(id == 14){
        ret = EnumParticle.SPELL_WITCH;
    }else if(id == 15){
        ret = EnumParticle.SPELL;
    }
      return ret;
  }

  public void setVelocity(Vector vel)
  {
    motX = vel.getX();
    motY = vel.getY();
    motZ = vel.getZ();
    velocityChanged = true;
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
  HashMap<Player,Vector> oldvecs = new HashMap();
  
  float speedbase = 0.25F;
  float speedlevel = 0F;
  float moreyaw = 3.5F;
  float alttraction = 0.3675F;
  float sidetraction = 0.3675F;
  float boostjump = 0F; 
  float maxspeed = 75F;
  float maxspeedlevel = 300F;
  int speedyawset = 25;
  
  public void increaseMaxSpeed(int to){
     if(to > 0){
         maxspeed += to*4;
     }
  }
  
  public void reduceRotationSpeed(float efe){
     speedyawset +=efe*5;
  }
  
  public void reduceRotationSpeedIncrease(float efe){
      moreyaw = 3.5F-(efe/5);
  }
  
  String sound = "a";
  public String getSound(){
      return "horns.horn."+sound;
  }
  
  public void setSound(String type){
     sound = type;
  }
  
  public void setBaseSpeed(float efe){
      speedbase = efe;
  }
  
  public void speedBoost(float efe){
      speedlevel += efe;
  }
  
  public void multTractionSpeed(float efe){
      speedlevel += efe;
  }
       Random r = new Random();
       

boolean spark = false;
int sparkparticle = 18;


//0 or 18 or 24 or 11(note) or heart or mobappearence
      
//  mobappaerance hm

  
      
  
  

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
    
    int forceg = 0;
    int tick = 10;
    boolean cancollide = false;
  
    int particlequality = 1;
    
    public void setParticleQuality(int more){
        particlequality+=more;
    }
    
    @Override
  public void collide(Entity entity)
  {
  }
 
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
  
  
  float ala = 0F;
  float head = 0F;
  float flotar = 0F;
  float stupid = 0F;
  
  float accspeed = 0F;
  float decspeed = 0F;
  float drift = 0F;
  
  public void increaseTraction(float tract){
      if(tract > 0)
      alttraction *= tract/2;
  }
  
  public void increaseDrifting(float drift){
      if(drift > 0)
      drift+=drift/2;
  }
  
          
  
  
  public void increaseBoostDuration(float dura){
      if(dura > 0)
      decspeed+=dura;
  } 
  
  public void increaseAcceleration(float speed){
      if(speed > 0){
          accspeed+=speed;
      }
  }
  
  float bspeed = 0F;

  public void increaseBoostersSpeed(float speed){
  bspeed+=speed*3; 
  }
  
  boolean forceclip = false;
  
boolean motor = false;
  public void playMotor(Player pe){

if(motor == false){
pe.playSound(pe.getLocation(), "random.pop", 0.2F, 1F); 
motor = true;    
}else{
    motor = false;
}

    

  }
  
  @Override
  public void g(float sideMot, float forMot)
  {
      
    if (passenger == null )
    {
        System.out.println("Removing Car Reason: Passenger is Null");
this.getHandle().remove();
      return;
    }
    if (passenger.getWorld() != this.getWorld())
    {
        System.out.println("Removing Car Reason: Different World");
this.getHandle().remove();
      return;
    }
    
    
        sideMot = ((EntityLiving)passenger).aZ * 0.5F;
    forMot = ((EntityLiving)passenger).ba;

Player pe = Bukkit.getPlayer(passenger.getName());
    Location loc = this.getHandle().getLocation();
    Block te = loc.getBlock();
    Block te2 = loc.subtract(0, 1, 0).getBlock();
    byte dura = te.getData();
    byte dura2 = te2.getData();
    if(te.getTypeId() == 99 && forMot > 0F
            || te2.getTypeId() == 99 && forMot > 0F){
        if(dura == 1 || dura2 == 1){
            if(speedlevel < maxspeedlevel)
            this.speedlevel+=20.00+bspeed;
        }else if(dura == 3 || dura2 == 3){
            if(speedlevel < maxspeedlevel)
            this.speedlevel+=17.50+bspeed;
        }else if(dura == 7 || dura2 == 7){
            if(speedlevel < maxspeedlevel/2)
            this.speedlevel+=15+bspeed;
        }else if(dura == 9 || dura2 == 9){
            if(speedlevel < maxspeedlevel/2)
            this.speedlevel+=12.50+bspeed;
        }else if(dura == 8 || dura2 == 8){
            this.boostjump+=5;
        }
        pe.playSound(loc, Sound.WATER, 1F, 1F);
        this.spark = true;
    }else if(speedlevel > maxspeed+2){
        spark = true;
    }else{
        spark = false;
    }
this.
    S = 2F;


  
  double posX = motX;
  double posY = motY;
  double posZ = motZ;

  if(posX < -0.20){
      posX = -0.20;
  }else if(posX > 0.20){
      posX = 0.20;
  }
  
  if(posZ < -0.20){
      posZ = -0.20;
  }else if(posZ > 0.20){
      posZ = 0.20;
  }
  
  if(posY < -0.01){
      posY = -0.01;
  }else if(posY > 0.01){
      posY = 0.01;
  }

if(manager != null){
    if(manager.bimap.containsKey(pe)){
        for(int and : manager.bimap.get(pe).keySet()){
            if(and == 0){
                this.forceclip = true;
                if(manager.bimap.get(pe).get(and) - System.currentTimeMillis() < 500){
                    forceclip = false;
                    this.motY = -0.25F;
                }else{
                this.motY = 0.25F;    
                }
               
                this.motZ = 0F;
                this.motX = 0F;
            }
            else if(and == 1){
                multYaw +=16f;
                sideMot = 0f;
                motX = 0F;
                motZ = 0F;
                forMot = 0F;
            }
            else if(and == 2){
                motX = motX/2;
                motZ = motZ/2;
                forMot = forMot/2;
                sideMot = sideMot/2;
                speedlevel-=1;
            }
            if(manager.bimap.get(pe).get(and) < System.currentTimeMillis()){
               forceclip = false;
                manager.bimap.remove(pe);
            }
        }
    }
}





    if(speedlevel < 0F){
        speedlevel = 0F;
    }

    pe.setLevel((int) speedlevel);


Vector vector = this.getVelDir(yaw,pitch);
PacketPlayOutWorldParticles packet2 = null;
PacketPlayOutWorldParticles packet3 = null;
    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(trail,true,(float)loc.getX() + (float)(vector.getX()*-1)*2,(float)loc.getY()+3,(float)loc.getZ() + (float)(vector.getZ()*-1)*2,(float)r.nextGaussian()/100, 0F ,(float)r.nextGaussian()/100,particlequality*5,0);

    if(spark){
        packet2 = new PacketPlayOutWorldParticles(EnumParticle.SPELL_MOB , true , (float)loc.getX()+(float)(vector.getX()*-1)*2 , (float)loc.getY()+3 , (float)loc.getZ()+(float)(vector.getZ()*-1)*2 , (float)r.nextGaussian()/50 , 0F , (float)r.nextGaussian()/50,10,particlequality*5,0);
        packet3 = new PacketPlayOutWorldParticles(trail , true , (float)loc.getX()+(float)(vector.getX()*-1)*2 , (float)loc.getY()+3 , (float)loc.getZ()+(float)(vector.getZ()*-1)*2 , (float)r.nextGaussian()/50 , 0F , (float)r.nextGaussian()/50,10,particlequality*5,0);
  
    }

    
    for(Player pes : Bukkit.getOnlinePlayers()){
    if(pes.getWorld() != pe.getWorld()){
        continue;
    }
    if(pes.getLocation().distanceSquared(pe.getLocation()) < 150){
        EntityPlayer ep = ((CraftPlayer)pes).getHandle();
        ep.playerConnection.sendPacket(packet);
        if(packet2 != null){
        ep.playerConnection.sendPacket(packet2);
        ep.playerConnection.sendPacket(packet3);
        }
    }
}
          
    if(sideMot > 0){
        this.multYaw -= (moreyaw+(this.speedlevel/speedyawset));
    }else if(sideMot < 0){
        multYaw += (moreyaw+(this.speedlevel/speedyawset));
    }
    if(multYaw > 180){
        multYaw = -180+(multYaw-180);
    }
    if(multYaw < -180){
        multYaw = 180-(multYaw+180);
    }

    
    
            this.yaw = (float) (multYaw);
this.lastYaw = (float) multYaw;

this.aI = (float) multYaw;
this.aJ = (float) multYaw;
this.aK = (float) multYaw;
this.aL = (float) multYaw;
    
if(sideMot != 0F){
    stupid++;
}
else if(stupid > 0){
    this.speedlevel+=stupid/(2.5F-drift);

    if(stupid > 25){
    this.getHandle().getWorld().playSound(this.getHandle().getLocation(), Sound.FUSE, 1F, 1F);
    }
    stupid = 0F;
}

if(stupid > 100){
    stupid = 100;
}

float speedtotal = 0.20F;
    if(forMot < 0){
        forMot = -alttraction;
        if(decspeed > 0){
        speedlevel-= (7F-(decspeed/2));
        }else{
        speedlevel-= (7F);            
        }

    }else if(forMot > 0){
        speedtotal = speedbase+(speedlevel/200);
        this.playMotor(pe);

        if(speedlevel < maxspeed){
        if(accspeed > 0F){
        speedlevel+=2.5F+(accspeed*0.8);
        }else{
        speedlevel+=2.5F;    
        }
               
        }else{
        speedlevel-=0.5F;
        }
    }else{
        this.speedlevel-=4F;
    }
            if(forMot > 0 && sideMot != 0F){
                this.speedlevel-=0.5;
            }
    
      if(sideMot < 0){
          if(decspeed > 0){
         this.speedlevel-=(3-(decspeed/2));
          }else{
this.speedlevel-=(3);
          }
        sideMot = -sidetraction;
    }else if(sideMot > 0){
          if(decspeed > 0){
         this.speedlevel-=(3-(decspeed/2));
          }else{
this.speedlevel-=(3);
          }
        sideMot = sidetraction;
    }else{
          if(decspeed > 0){
         this.speedlevel-=(3-(decspeed/2));
          }else{
this.speedlevel-=(1);
          }
    }
      if(!this.isSmall()){
this.setSmall(true);
this.datawatcher.watch(10, (byte)(0xF & 0xFFFFFFFE));
      }

  Vector vec = this.getVelDir(yaw,this.pitch);
boolean collided = this.hasCollided();


       if(sideMot < 0F){
           vec.setZ(ala/200);
           ala-=4;
       }
       else if(sideMot > 0F){
           vec.setZ(ala/200);
           ala+=4;
       }
       else{
           if(ala < 0){
               ala+=8;
               if(forMot == 0F){
                   ala+=4;
               }
           }
           if(ala > 0){
               ala-=8;
               if(forMot == 0F){
                   ala-=4;
               }
           }
         if(ala < 0){
             vec.setZ(ala/200);
         }else{
             vec.setZ(ala/200);
         }
       }

       if(ala < -100F){
           ala = -100F;
       }
       else if(ala > 100F){
           ala = 100F;
       }
       
       
       
              if(forMot < 0F){
           vec.setX(head/200);
           head-=1;
       }
       else if(forMot > 0F){
           vec.setX(head/200);
           head+=1;
       }
       else{
           if(head < 0){
               head+=1;
           }
           if(head > 0){
               head-=1;
           }
         if(head < 0){
             vec.setX(head/200);
         }else{
             vec.setX(head/200);
         }
       }

       if(head < -100F){
           head = -100F;
       }
       else if(head > 100F){
           head = 100F;
       }

 this.getHandle().setHeadPose(new EulerAngle((vec.getX()/2)*-1,vec.getY()/10,vec.getZ()*0.7));
 
 if(flotar < -10){
     flotar = 10;
 }
 if(flotar > 10){
     flotar = -10;
 }
 
 if(!collided){   
if(!loc.clone().getBlock().getType().isSolid()){
    flotar++;
    this.noclip = true;
}else {
    this.noclip = false;
    flotar--;
}}
 if(!loc.getBlock().getType().isSolid() && !loc.clone().subtract(0, 1, 0).getBlock().getType().isSolid()){
     motY = -2F;
 }else{
 motY = flotar/50-0.1F;
 }
        if(collided){
           this.noclip = false;
           motX = 0;
           motY = 0;
           motZ = 0;
        }

if(boostjump > 0){
    boostjump-=1;
}else{
    boostjump=0;
}


if(this.forceclip){
    motZ = 0F;
    motX = 0F;
    motY = 0.25F;
    forMot = 0f;
    sideMot = 0f;
    this.noclip = true;
}

    k(speedtotal*0.8F);
    
this.onGround = true;
if(manager.GetState() == GameState.Live || manager.GetState() == GameState.Prepare){
if(manager.started == false){
    sideMot = 0F;
    forMot = 0F;
    this.motX = 0F;
    this.motZ = 0F;
}    
}




  this.ge(sideMot, forMot);
    

    
    try
    {
       
      Field jump = null;
      jump = EntityLiving.class.getDeclaredField("aY");
      jump.setAccessible(true);
      if ((jump != null) && (onGround)) {
        if (jump.getBoolean(passenger))
        {
            

pe.playSound(new Location(passenger.world.getWorld(),passenger.locX,passenger.locY,passenger.locZ), this.getSound(), 1F, 1F);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
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
if(boostjump > 0){
this.motY = 1.5F;
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
  
  CraftArmorStand stand = new CraftArmorStand((CraftServer) this.getBukkitEntity().getServer(),this);
  public CraftArmorStand getHandle(){
      return stand;
  }

  double maxSpeed = 0.4; 

  
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
  protected void a(BlockPosition blockposition, net.minecraft.server.v1_8_R3.Block block)
  {
    super.a(blockposition, (net.minecraft.server.v1_8_R3.Block) block);
   
    this.a(blockposition.getX(), blockposition.getY(), blockposition.getZ(), block);
  }
  
  protected void a(int i, int j, int k, net.minecraft.server.v1_8_R3.Block block)
  {
    super.a(new BlockPosition(i, j, k), block);
    makeStepSound(i, j, k, block);
  }
  
  protected void makeStepSound(int i, int j, int k, net.minecraft.server.v1_8_R3.Block block)
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
  


