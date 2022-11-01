import java.io.File;
import java.util.HashMap;
import org.bukkit.configuration.file.YamlConfiguration;

// $FF: renamed from: c
public class class_120 {

    private static File Ξ;
    private static HashMap<String, Boolean> Ξ;
    private static HashMap<String, Integer> Π;
    private static HashMap<String, Double> HHΞ;

    public static void Ξ() {
        class_120.field_199.clear();
        class_120.field_200.clear();
        class_120.HHΞ.clear();
        class_79.method_444(class_120.field_198, "HitReach.distance_to_check", Double.valueOf(4.0D));
        class_79.method_444(class_120.field_198, "FastPlace.cancel_seconds", Integer.valueOf(1));
        class_79.method_444(class_120.field_198, "FastBreak.cancel_seconds", Integer.valueOf(1));
        class_79.method_444(class_120.field_198, "Clip.check_when_flying", Boolean.valueOf(false));
        class_79.method_444(class_120.field_198, "Phase.check_when_flying", Boolean.valueOf(false));
        class_79.method_444(class_120.field_198, "Speed.check_sneaking", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "Speed.check_walking", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "FastClicks.cps_limit", Integer.valueOf(15));
        class_79.method_444(class_120.field_198, "FastClicks.check_cps", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "FastClicks.check_click_time", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleActions.check_tower", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleActions.check_scaffold", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleActions.check_actions", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleActions.check_cactus", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleActions.check_item_use", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleActions.cancel_seconds", Integer.valueOf(1));
        class_79.method_444(class_120.field_198, "Nuker.max_breaks_per_second", Integer.valueOf(40));
        class_79.method_444(class_120.field_198, "Nuker.cancel_seconds", Integer.valueOf(2));
        class_79.method_444(class_120.field_198, "FastBow.check_bow_force", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "FastBow.check_bow_shots", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleInventory.check_sneaking", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleInventory.check_sprinting", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleInventory.check_sleeping", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleInventory.check_walking", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleInventory.check_dead", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleInventory.check_sprint_jumping", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ImpossibleInventory.check_walk_jumping", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "Sprint.check_food_sprinting", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "Sprint.check_fake_sprinting", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "Sprint.check_omnidirectional_sprinting", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_overall", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_accuracy", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_angle", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_block_raytrace", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_combined", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_direction", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_entity_raytrace", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_hits_per_second", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_hit_time", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_impossible_hits", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_pitch_movement", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_yaw_movement", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_rapid_hits", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_rotations", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_hitbox", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_aim_consistency", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_aim_accuracy", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_comparison", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "KillAura.check_modulo", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "GhostHand.check_block_breaking", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "GhostHand.check_player_interactions", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "NormalMovements.check_step_hacking", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "NormalMovements.check_jump_movement", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "NormalMovements.check_slime_boost", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "NormalMovements.check_block_climbing", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "NormalMovements.check_hop_movement", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "Liquids.check_block_breaking", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "Liquids.check_block_placing", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "Fly.check_fly", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "Fly.check_glide", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ElytraMove.check_speed", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "ElytraMove.check_fly", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "NoFall.check_fall", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "NoFall.check_landing", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "NoFall.check_ground", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "NoSlowdown.check_item_usage", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "NoSlowdown.check_bow_shots", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "NoSlowdown.check_food_eating", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "NoSlowdown.check_cobweb_movement", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "MorePackets.timer.max_moves_per_second", Integer.valueOf(30));
        class_79.method_444(class_120.field_198, "MorePackets.blink.distance_to_check", Double.valueOf(2.5D));
        class_79.method_444(class_120.field_198, "MorePackets.check_timer", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "MorePackets.check_blink", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "MorePackets.check_motion", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "MorePackets.check_overall", Boolean.valueOf(true));
        class_79.method_444(class_120.field_198, "MorePackets.check_difference", Boolean.valueOf(true));
    }

    private static YamlConfiguration Ξ() {
        if (!class_120.field_198.exists()) {
            method_569();
        }

        YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(class_120.field_198);

        return yamlconfiguration == null ? null : yamlconfiguration;
    }

    public static int Ξ(String s) {
        if (s == null) {
            return 0;
        } else if (class_120.field_200.containsKey(s)) {
            return ((Integer) class_120.field_200.get(s)).intValue();
        } else {
            YamlConfiguration yamlconfiguration = method_570();
            int i = yamlconfiguration.getInt(s);

            class_120.field_200.put(s, Integer.valueOf(i));
            return i;
        }
    }

    public static boolean Ξ(String s) {
        if (s == null) {
            return false;
        } else if (class_120.field_199.containsKey(s)) {
            return ((Boolean) class_120.field_199.get(s)).booleanValue();
        } else {
            YamlConfiguration yamlconfiguration = method_570();
            boolean flag = yamlconfiguration.getBoolean(s);

            class_120.field_199.put(s, Boolean.valueOf(flag));
            return flag;
        }
    }

    public static double Ξ(String s) {
        if (s == null) {
            return 0.0D;
        } else if (class_120.HHΞ.containsKey(s)) {
            return ((Double) class_120.HHΞ.get(s)).doubleValue();
        } else {
            YamlConfiguration yamlconfiguration = method_570();
            double d0 = yamlconfiguration.getDouble(s);

            class_120.HHΞ.put(s, Double.valueOf(d0));
            return d0;
        }
    }

    static {
        class_120.field_198 = new File("plugins/Spartan/checks.yml");
        class_120.field_199 = new HashMap();
        class_120.field_200 = new HashMap();
        class_120.HHΞ = new HashMap();
    }
}
