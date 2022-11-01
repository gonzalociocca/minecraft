package mineultra.minecraft.game.core.Buffer;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Buffer
{
  protected BufferManager Manager;
  protected long _time;
  protected String _reason;
  protected String _informOn;
  protected String _informOff;
  protected LivingEntity _ent;
  protected LivingEntity _source;
  protected BufferType _type;
  protected int _mult;
  protected int _ticks;
  protected int _ticksTotal;
  protected boolean _ambient;
  protected Material _indicatorType;
  protected byte _indicatorData;
  protected boolean _add = false;
  protected boolean _live = false;

  protected boolean _showIndicator = true;

  public Buffer(BufferManager manager, String reason, LivingEntity ent, LivingEntity source, BufferType type, int mult, int ticks, boolean add, Material visualType, byte visualData, boolean showIndicator, boolean ambient)
  {
    Manager = manager;
    _time = System.currentTimeMillis();

    _reason = reason;

    _ent = ent;
    _source = source;

    _type = type;
    _mult = mult;
    _ticks = ticks;
    _ticksTotal = ticks;
    _ambient = ambient;

    _indicatorType = visualType;
    _indicatorData = visualData;
    _showIndicator = showIndicator;

    _add = add;

    _live = (!add);
  }

  public boolean Tick()
  {
    if ((_live) && (_ticks > 0)) {
      _ticks -= 1;
    }
    return IsExpired();
  }

  public void OnBufferAdd()
  {
  }

  public void Apply()
  {
    _live = true;

    Add();
  }

  public void Add()
  {
    try
    {
      PotionEffectType type = PotionEffectType.getByName(_type.toString());

      _ent.removePotionEffect(type);

      if (_ticks == -1)
        new PotionEffect(type, 72000, _mult, _ambient).apply(_ent);
      else
        new PotionEffect(type, _ticks, _mult, _ambient).apply(_ent);
    }
    catch (Exception localException)
    {
    }
  }

  public void Remove()
  {
    try
    {
      PotionEffectType type = PotionEffectType.getByName(_type.toString());
      _ent.removePotionEffect(type);
    }
    catch (Exception localException)
    {
    }
  }

  public Material GetIndicatorMaterial()
  {
    return _indicatorType;
  }

  public byte GetIndicatorData()
  {
    return _indicatorData;
  }

  public LivingEntity GetEnt()
  {
    return _ent;
  }

  public LivingEntity GetSource()
  {
    return _source;
  }

  public boolean IsAdd()
  {
    return _add;
  }

  public BufferType GetType()
  {
    return _type;
  }

  public int GetMult()
  {
    return _mult;
  }

  public void SetLive(boolean live)
  {
    _live = live;
  }

  public int GetTicks()
  {
    return _ticks;
  }

  public int GetTicksTotal()
  {
    return _ticksTotal;
  }

  public String GetReason()
  {
    return _reason;
  }

  public long GetTime()
  {
    return _time;
  }

  public void Expire()
  {
    _ticks = 0;

    Remove();
  }

  public void Restart()
  {
    _ticks = _ticksTotal;
  }

  public boolean IsBetterOrEqual(Buffer other, boolean additive)
  {
    if (GetMult() > other.GetMult()) {
      return true;
    }
    if (GetMult() < other.GetMult()) {
      return false;
    }
    if (additive) {
      return true;
    }
    if (GetTicks() >= other.GetTicks()) {
      return true;
    }
    return false;
  }

  public boolean IsVisible()
  {
    return _showIndicator;
  }

  public boolean IsExpired()
  {
    if (_ticks == -1) {
      return false;
    }
    return _ticks <= 0;
  }

  public BufferManager GetManager()
  {
    return Manager;
  }

  public String GetInformOn()
  {
    return _informOn;
  }

  public String GetInformOff()
  {
    return _informOff;
  }

  public void ModifyTicks(int amount)
  {
    _ticks += amount;
    _ticksTotal += amount;
  }

  public void ModifyMult(int i)
  {
    _mult = Math.max(0, _mult + i);
  }

  public static enum BufferType
  {
    CLOAK, 
    SHOCK, 
    SILENCE, 
    BURNING, 
    FALLING, 
    LIGHTNING, 
    INVULNERABLE, 
    EXPLOSION, 
    FIRE_ITEM_IMMUNITY, 

    CUSTOM, 

    ABSORBTION, 
    BLINDNESS, 
    CONFUSION, 
    DAMAGE_RESISTANCE, 
    FAST_DIGGING, 
    FIRE_RESISTANCE, 
    HARM, 
    HEAL, 
    HEALTH_BOOST, 
    HUNGER, 
    INCREASE_DAMAGE, 
    INVISIBILITY, 
    JUMP, 
    NIGHT_VISION, 
    POISON, 
    REGENERATION, 
    SLOW, 
    SLOW_DIGGING, 
    SPEED, 
    WATER_BREATHING, 
    WEAKNESS, 
    WITHER;
  }
}