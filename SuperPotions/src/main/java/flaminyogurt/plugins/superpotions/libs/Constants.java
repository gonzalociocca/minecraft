package flaminyogurt.plugins.superpotions.libs;

import java.util.HashMap;
import org.bukkit.potion.PotionEffectType;

public class Constants
{
  public static final String PERMISSION_EFFECT = "superpotions.effect";
  public static final String PERMISSION_EFFECT_BASE = "superpotions.effect.";
  public static final String PERMISSION_EFFECT_OTHERS = "superpotions.effect.others";
  public static final String PERMISSION_EFFECT_OTHERS_BASE = "superpotions.effect.others.";
  public static final String PERMISSION_ALL = "superpotions.all";
  public static final String PERMISSION_ALL_BASE = "superpotions.all.";
  public static final String PERMISSION_REMOVE = "superpotions.removeall";
  public static final String PERMISSION_REMOVE_OTHERS = "superpotions.removeall.others";
  public static final String PERMISSION_CONFIG = "superpotions.config";
  public static final String PERMISSION_UPDATE = "superpotions.update";
  public static final String PERMISSION_CREATE = "superpotions.create";
  public static final String PERMISSION_CREATE_BASE = "superpotions.create.";
  public static final String PERMISSION_CREATE_OTHERS = "superpotions.create.others";
  public static final String PERMISSION_CREATE_OTHERS_BASE = "superpotions.create.others.";
  public static final String PERMISSION_KITS = "superpotions.kits";
  public static final String PERMISSION_KITS_BASE = "superpotions.kits.";
  public static final String PERMISSION_KITS_OTHERS = "superpotions.kits.others";
  public static final String PERMISSION_KITS_OTHERS_BASE = "superpotions.kits.others.";
  public static final String PERMISSION_HELP = "superpotions.help";
  public static final String PERMISSION_OPT = "superpotions.opt";
  public static final int SLUG_ID = 46340;
  public static final HashMap<String, PotionEffectType> potions = new HashMap();
  public static final String CONFIG_MAX_LEVEL = "maxlevel";
  public static final String CONFIG_MAX_DURATION = "maxduration";
  public static final String CONFIG_REPLACE_EFFECTS = "replaceeffects";
  public static final String CONFIG_ECONOMY = "economy";
  public static final String CONFIG_MESSAGES = "messages";
  public static final String CONFIG_AUTO_UPDATE_CHECK = "autoupdate-check";
  public static final String CONFIG_AUTO_UPDATE_DOWNLOAD = "autoupdate-download";
  public static final String CONFIG_AMBIENT = "ambient";
  public static final String CONFIG_EFFECT_PERMISSIONS = "effect-permissions";
  public static final String CONFIG_KIT_PERMISSIONS = "kit-permissions";
  public static final String CONFIG_OPT = "allow-opt";
  public static final String METADATA_OPT = "SuperPotions_opt";
  
  static
  {
    potions.put("blindness", PotionEffectType.BLINDNESS);
    potions.put("confusion", PotionEffectType.CONFUSION);
    potions.put("resistance", PotionEffectType.DAMAGE_RESISTANCE);
    potions.put("haste", PotionEffectType.FAST_DIGGING);
    potions.put("fireresistance", PotionEffectType.FIRE_RESISTANCE);
    potions.put("hunger", PotionEffectType.HUNGER);
    potions.put("strength", PotionEffectType.INCREASE_DAMAGE);
    potions.put("jump", PotionEffectType.JUMP);
    potions.put("poison", PotionEffectType.POISON);
    potions.put("regen", PotionEffectType.REGENERATION);
    potions.put("slow", PotionEffectType.SLOW);
    potions.put("fatigue", PotionEffectType.SLOW_DIGGING);
    potions.put("speed", PotionEffectType.SPEED);
    potions.put("breathing", PotionEffectType.WATER_BREATHING);
    potions.put("weakness", PotionEffectType.WEAKNESS);
    potions.put("heal", PotionEffectType.HEAL);
    potions.put("harm", PotionEffectType.HARM);
    potions.put("invisibility", PotionEffectType.INVISIBILITY);
    potions.put("nightvision", PotionEffectType.NIGHT_VISION);
    potions.put("wither", PotionEffectType.WITHER);
    potions.put("absorption", PotionEffectType.ABSORPTION);
    potions.put("healthboost", PotionEffectType.HEALTH_BOOST);
    potions.put("saturation", PotionEffectType.SATURATION);
  }
}
