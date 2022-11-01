//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gmail.nossr50;

import com.gmail.nossr50.config.AdvancedConfig;
import com.gmail.nossr50.config.Config;
import com.gmail.nossr50.config.HiddenConfig;
import com.gmail.nossr50.config.experience.ExperienceConfig;
import com.gmail.nossr50.config.mods.ArmorConfigManager;
import com.gmail.nossr50.config.mods.BlockConfigManager;
import com.gmail.nossr50.config.mods.EntityConfigManager;
import com.gmail.nossr50.config.mods.ToolConfigManager;
import com.gmail.nossr50.config.skills.alchemy.PotionConfig;
import com.gmail.nossr50.config.skills.repair.RepairConfigManager;
import com.gmail.nossr50.config.skills.salvage.SalvageConfigManager;
import com.gmail.nossr50.config.treasure.TreasureConfig;
import com.gmail.nossr50.database.DatabaseManager;
import com.gmail.nossr50.database.DatabaseManagerFactory;
import com.gmail.nossr50.listeners.BlockListener;
import com.gmail.nossr50.listeners.EntityListener;
import com.gmail.nossr50.listeners.InventoryListener;
import com.gmail.nossr50.listeners.PlayerListener;
import com.gmail.nossr50.listeners.SelfListener;
import com.gmail.nossr50.listeners.WorldListener;
import com.gmail.nossr50.party.PartyManager;
import com.gmail.nossr50.runnables.CheckDateTask;
import com.gmail.nossr50.runnables.SaveTimerTask;
import com.gmail.nossr50.runnables.UpdaterResultAsyncTask;
import com.gmail.nossr50.runnables.backups.CleanBackupsTask;
import com.gmail.nossr50.runnables.database.UserPurgeTask;
import com.gmail.nossr50.runnables.party.PartyAutoKickTask;
import com.gmail.nossr50.runnables.player.ClearRegisteredXPGainTask;
import com.gmail.nossr50.runnables.player.PlayerProfileLoadingTask;
import com.gmail.nossr50.runnables.player.PowerLevelUpdatingTask;
import com.gmail.nossr50.runnables.skills.BleedTimerTask;
import com.gmail.nossr50.skills.alchemy.Alchemy;
import com.gmail.nossr50.skills.child.ChildConfig;
import com.gmail.nossr50.skills.repair.repairables.RepairableManager;
import com.gmail.nossr50.skills.repair.repairables.SimpleRepairableManager;
import com.gmail.nossr50.skills.salvage.salvageables.SalvageableManager;
import com.gmail.nossr50.skills.salvage.salvageables.SimpleSalvageableManager;
import com.gmail.nossr50.skills.smelting.SmeltingManager;
import com.gmail.nossr50.util.ChimaeraWing;
import com.gmail.nossr50.util.HolidayManager;
import com.gmail.nossr50.util.LogFilter;
import com.gmail.nossr50.util.ModManager;
import com.gmail.nossr50.util.Permissions;
import com.gmail.nossr50.util.blockmeta.chunkmeta.ChunkManager;
import com.gmail.nossr50.util.blockmeta.chunkmeta.ChunkManagerFactory;
import com.gmail.nossr50.util.commands.CommandRegistrationManager;
import com.gmail.nossr50.util.experience.FormulaManager;
import com.gmail.nossr50.util.player.UserManager;
import com.gmail.nossr50.util.scoreboards.ScoreboardManager;
import com.gmail.nossr50.util.upgrade.UpgradeManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.shatteredlands.shatt.backup.ZipLibrary;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class mcMMO extends JavaPlugin {
    private static ChunkManager placeStore;
    private static RepairableManager repairableManager;
    private static SalvageableManager salvageableManager;
    private static ModManager modManager;
    private static DatabaseManager databaseManager;
    private static FormulaManager formulaManager;
    private static HolidayManager holidayManager;
    private static UpgradeManager upgradeManager;
    private static String mainDirectory;
    private static String flatFileDirectory;
    private static String usersFile;
    private static String modDirectory;
    public static mcMMO p;
    public static File mcmmo;
    private boolean updateAvailable;
    private static boolean healthBarPluginEnabled;
    public boolean noErrorsInConfigFiles = true;
    private boolean xpEventEnabled;
    public static final String entityMetadataKey = "mcMMO: Spawned Entity";
    public static final String blockMetadataKey = "mcMMO: Piston Tracking";
    public static final String furnaceMetadataKey = "mcMMO: Tracked Furnace";
    public static final String tntMetadataKey = "mcMMO: Tracked TNT";
    public static final String tntsafeMetadataKey = "mcMMO: Safe TNT";
    public static final String customNameKey = "mcMMO: Custom Name";
    public static final String customVisibleKey = "mcMMO: Name Visibility";
    public static final String droppedItemKey = "mcMMO: Tracked Item";
    public static final String infiniteArrowKey = "mcMMO: Infinite Arrow";
    public static final String bowForceKey = "mcMMO: Bow Force";
    public static final String arrowDistanceKey = "mcMMO: Arrow Distance";
    public static final String customDamageKey = "mcMMO: Custom Damage";
    public static final String disarmedItemKey = "mcMMO: Disarmed Item";
    public static final String playerDataKey = "mcMMO: Player Data";
    public static final String greenThumbDataKey = "mcMMO: Green Thumb";
    public static final String databaseCommandKey = "mcMMO: Processing Database Command";
    public static final String bredMetadataKey = "mcMMO: Bred Animal";
    public static FixedMetadataValue metadataValue;

    public mcMMO() {
    }

    public void onEnable() {
        try {
            p = this;
            this.getLogger().setFilter(new LogFilter(this));
            metadataValue = new FixedMetadataValue(this, Boolean.valueOf(true));
            PluginManager t = this.getServer().getPluginManager();
            healthBarPluginEnabled = t.getPlugin("HealthBar") != null;
            upgradeManager = new UpgradeManager();
            this.setupFilePaths();
            modManager = new ModManager();
            this.loadConfigFiles();
            if(!this.noErrorsInConfigFiles) {
                return;
            }

            if(this.getServer().getName().equals("Cauldron") || this.getServer().getName().equals("MCPC+")) {
                this.checkModConfigs();
            }

            if(healthBarPluginEnabled) {
                this.getLogger().info("HealthBar plugin found, mcMMO\'s healthbars are automatically disabled.");
            }

            if(t.getPlugin("NoCheatPlus") != null && t.getPlugin("CompatNoCheatPlus") == null) {
                this.getLogger().warning("NoCheatPlus plugin found, but CompatNoCheatPlus was not found!");
                this.getLogger().warning("mcMMO will not work properly alongside NoCheatPlus without CompatNoCheatPlus");
            }

            databaseManager = DatabaseManagerFactory.getDatabaseManager();
            this.registerEvents();
            this.registerCustomRecipes();
            PartyManager.loadParties();
            formulaManager = new FormulaManager();
            holidayManager = new HolidayManager();
            Iterator var2 = this.getServer().getOnlinePlayers().iterator();

            while(var2.hasNext()) {
                Player player = (Player)var2.next();
                (new PlayerProfileLoadingTask(player)).runTaskLaterAsynchronously(p, 1L);
            }

            this.debug("Version " + this.getDescription().getVersion() + " is enabled!");
            this.scheduleTasks();
            CommandRegistrationManager.registerCommands();
            placeStore = ChunkManagerFactory.getChunkManager();
            this.checkForUpdates();
            if(Config.getInstance().getPTPCommandWorldPermissions()) {
                Permissions.generateWorldTeleportPermissions();
            }
        } catch (Throwable var4) {
            this.getLogger().severe("There was an error while enabling mcMMO!");
            if(!(var4 instanceof ExceptionInInitializerError)) {
                var4.printStackTrace();
            } else {
                this.getLogger().info("Please do not replace the mcMMO jar while the server is running.");
            }

            this.getServer().getPluginManager().disablePlugin(this);
        }

    }

    public void onDisable() {
        try {
            Alchemy.finishAllBrews();
            UserManager.saveAll();
            UserManager.clearAll();
            PartyManager.saveParties();
            ScoreboardManager.teardownAll();
            formulaManager.saveFormula();
            holidayManager.saveAnniversaryFiles();
            placeStore.saveAll();
            placeStore.cleanUp();
        } catch (NullPointerException var2) {
            ;
        }

        this.debug("Canceling all tasks...");
        this.getServer().getScheduler().cancelTasks(this);
        this.debug("Unregister all events...");
        HandlerList.unregisterAll(this);
        if(Config.getInstance().getBackupsEnabled()) {
            try {
                ZipLibrary.mcMMOBackup();
            } catch (IOException var3) {
                this.getLogger().severe(var3.toString());
            } catch (Throwable var4) {
                if(var4 instanceof NoClassDefFoundError) {
                    this.getLogger().severe("Backup class not found!");
                    this.getLogger().info("Please do not replace the mcMMO jar while the server is running.");
                } else {
                    this.getLogger().severe(var4.toString());
                }
            }
        }

        databaseManager.onDisable();
        this.debug("Was disabled.");
    }

    public static String getMainDirectory() {
        return mainDirectory;
    }

    public static String getFlatFileDirectory() {
        return flatFileDirectory;
    }

    public static String getUsersFilePath() {
        return usersFile;
    }

    public static String getModDirectory() {
        return modDirectory;
    }

    public boolean isUpdateAvailable() {
        return this.updateAvailable;
    }

    public void setUpdateAvailable(boolean available) {
        this.updateAvailable = available;
    }

    public boolean isXPEventEnabled() {
        return this.xpEventEnabled;
    }

    public void setXPEventEnabled(boolean enabled) {
        this.xpEventEnabled = enabled;
    }

    public void toggleXpEventEnabled() {
        this.xpEventEnabled = !this.xpEventEnabled;
    }

    public void debug(String message) {
        this.getLogger().info("[Debug] " + message);
    }

    public static FormulaManager getFormulaManager() {
        return formulaManager;
    }

    public static HolidayManager getHolidayManager() {
        return holidayManager;
    }

    public static ChunkManager getPlaceStore() {
        return placeStore;
    }

    public static RepairableManager getRepairableManager() {
        return repairableManager;
    }

    public static SalvageableManager getSalvageableManager() {
        return salvageableManager;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static ModManager getModManager() {
        return modManager;
    }

    public static UpgradeManager getUpgradeManager() {
        return upgradeManager;
    }

    /** @deprecated */
    @Deprecated
    public static void setDatabaseManager(DatabaseManager databaseManager) {
        mcMMO.databaseManager = databaseManager;
    }

    public static boolean isHealthBarPluginEnabled() {
        return healthBarPluginEnabled;
    }

    private void setupFilePaths() {
        mcmmo = this.getFile();
        mainDirectory = this.getDataFolder().getPath() + File.separator;
        flatFileDirectory = mainDirectory + "flatfile" + File.separator;
        usersFile = flatFileDirectory + "mcmmo.users";
        modDirectory = mainDirectory + "mods" + File.separator;
        this.fixFilePaths();
    }

    private void fixFilePaths() {
        File oldFlatfilePath = new File(mainDirectory + "FlatFileStuff" + File.separator);
        File oldModPath = new File(mainDirectory + "ModConfigs" + File.separator);
        if(oldFlatfilePath.exists() && !oldFlatfilePath.renameTo(new File(flatFileDirectory))) {
            this.getLogger().warning("Failed to rename FlatFileStuff to flatfile!");
        }

        if(oldModPath.exists() && !oldModPath.renameTo(new File(modDirectory))) {
            this.getLogger().warning("Failed to rename ModConfigs to mods!");
        }

        File oldArmorFile = new File(modDirectory + "armor.yml");
        File oldBlocksFile = new File(modDirectory + "blocks.yml");
        File oldEntitiesFile = new File(modDirectory + "entities.yml");
        File oldToolsFile = new File(modDirectory + "tools.yml");
        if(oldArmorFile.exists() && !oldArmorFile.renameTo(new File(modDirectory + "armor.default.yml"))) {
            this.getLogger().warning("Failed to rename armor.yml to armor.default.yml!");
        }

        if(oldBlocksFile.exists() && !oldBlocksFile.renameTo(new File(modDirectory + "blocks.default.yml"))) {
            this.getLogger().warning("Failed to rename blocks.yml to blocks.default.yml!");
        }

        if(oldEntitiesFile.exists() && !oldEntitiesFile.renameTo(new File(modDirectory + "entities.default.yml"))) {
            this.getLogger().warning("Failed to rename entities.yml to entities.default.yml!");
        }

        if(oldToolsFile.exists() && !oldToolsFile.renameTo(new File(modDirectory + "tools.default.yml"))) {
            this.getLogger().warning("Failed to rename tools.yml to tools.default.yml!");
        }

        File currentFlatfilePath = new File(flatFileDirectory);
        currentFlatfilePath.mkdirs();
    }

    private void checkForUpdates() {
        if(Config.getInstance().getUpdateCheckEnabled()) {
            (new UpdaterResultAsyncTask(this)).runTaskAsynchronously(p);
        }
    }

    private void loadConfigFiles() {
        TreasureConfig.getInstance();
        HiddenConfig.getInstance();
        AdvancedConfig.getInstance();
        PotionConfig.getInstance();
        new ChildConfig();
        ArrayList repairables = new ArrayList();
        ArrayList salvageables = new ArrayList();
        if(Config.getInstance().getToolModsEnabled()) {
            new ToolConfigManager(this);
        }

        if(Config.getInstance().getArmorModsEnabled()) {
            new ArmorConfigManager(this);
        }

        if(Config.getInstance().getBlockModsEnabled()) {
            new BlockConfigManager(this);
        }

        if(Config.getInstance().getEntityModsEnabled()) {
            new EntityConfigManager(this);
        }

        repairables.addAll((new RepairConfigManager(this)).getLoadedRepairables());
        repairables.addAll(modManager.getLoadedRepairables());
        repairableManager = new SimpleRepairableManager(repairables.size());
        repairableManager.registerRepairables(repairables);
        SalvageConfigManager sManager = new SalvageConfigManager(this);
        salvageables.addAll(sManager.getLoadedSalvageables());
        salvageableManager = new SimpleSalvageableManager(salvageables.size());
        salvageableManager.registerSalvageables(salvageables);
    }

    private void registerEvents() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new BlockListener(this), this);
        pluginManager.registerEvents(new EntityListener(this), this);
        pluginManager.registerEvents(new InventoryListener(this), this);
        pluginManager.registerEvents(new SelfListener(), this);
        pluginManager.registerEvents(new WorldListener(this), this);
    }

    private void registerCustomRecipes() {
        if(Config.getInstance().getChimaeraEnabled()) {
            this.getServer().addRecipe(ChimaeraWing.getChimaeraWingRecipe());
        }

        if(Config.getInstance().getFluxPickaxeEnabled()) {
            this.getServer().addRecipe(SmeltingManager.getFluxPickaxeRecipe(Material.DIAMOND_PICKAXE));
            this.getServer().addRecipe(SmeltingManager.getFluxPickaxeRecipe(Material.GOLD_PICKAXE));
            this.getServer().addRecipe(SmeltingManager.getFluxPickaxeRecipe(Material.IRON_PICKAXE));
            this.getServer().addRecipe(SmeltingManager.getFluxPickaxeRecipe(Material.STONE_PICKAXE));
            this.getServer().addRecipe(SmeltingManager.getFluxPickaxeRecipe(Material.WOOD_PICKAXE));
        }

    }

    private void scheduleTasks() {
        long saveIntervalTicks = (long)(Config.getInstance().getSaveInterval() * 1200);
        (new SaveTimerTask()).runTaskTimer(this, saveIntervalTicks, saveIntervalTicks);
        (new CleanBackupsTask()).runTaskAsynchronously(p);
        (new BleedTimerTask()).runTaskTimer(this, 40L, 40L);
        long purgeIntervalTicks = (long)Config.getInstance().getPurgeInterval() * 60L * 60L * 20L;
        if(purgeIntervalTicks == 0L) {
            (new UserPurgeTask()).runTaskLaterAsynchronously(this, 40L);
        } else if(purgeIntervalTicks > 0L) {
            (new UserPurgeTask()).runTaskTimerAsynchronously(this, purgeIntervalTicks, purgeIntervalTicks);
        }

        long kickIntervalTicks = (long)Config.getInstance().getAutoPartyKickInterval() * 60L * 60L * 20L;
        if(kickIntervalTicks == 0L) {
            (new PartyAutoKickTask()).runTaskLater(this, 40L);
        } else if(kickIntervalTicks > 0L) {
            (new PartyAutoKickTask()).runTaskTimer(this, kickIntervalTicks, kickIntervalTicks);
        }

        (new PowerLevelUpdatingTask()).runTaskTimer(this, 40L, 40L);
        if(getHolidayManager().nearingAprilFirst()) {
            (new CheckDateTask()).runTaskTimer(this, 200L, 72000L);
        }

        if(ExperienceConfig.getInstance().getDiminishedReturnsEnabled()) {
            (new ClearRegisteredXPGainTask()).runTaskTimer(this, 60L, 60L);
        }

    }

    private void checkModConfigs() {
        if(!Config.getInstance().getToolModsEnabled()) {
            this.getLogger().warning("Cauldron implementation found, but the custom tool config for mcMMO is disabled!");
            this.getLogger().info("To enable, set Mods.Tool_Mods_Enabled to TRUE in config.yml.");
        }

        if(!Config.getInstance().getArmorModsEnabled()) {
            this.getLogger().warning("Cauldron implementation found, but the custom armor config for mcMMO is disabled!");
            this.getLogger().info("To enable, set Mods.Armor_Mods_Enabled to TRUE in config.yml.");
        }

        if(!Config.getInstance().getBlockModsEnabled()) {
            this.getLogger().warning("Cauldron implementation found, but the custom block config for mcMMO is disabled!");
            this.getLogger().info("To enable, set Mods.Block_Mods_Enabled to TRUE in config.yml.");
        }

        if(!Config.getInstance().getEntityModsEnabled()) {
            this.getLogger().warning("Cauldron implementation found, but the custom entity config for mcMMO is disabled!");
            this.getLogger().info("To enable, set Mods.Entity_Mods_Enabled to TRUE in config.yml.");
        }

    }
}
