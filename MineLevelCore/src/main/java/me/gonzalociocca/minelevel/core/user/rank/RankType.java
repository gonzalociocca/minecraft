package me.gonzalociocca.minelevel.core.user.rank;

import me.gonzalociocca.minelevel.core.misc.Code;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noname on 9/2/2017.
 */
public enum RankType {
    User(0, "User", "&3", "&3", 0, 0, false),
    OTHER(1, "OTHER", "&3", "&3", 0, 0, false),
    VIP(2, "VIP", "&6&lVIP ", "&e&k||&e[&6&lVIP&e]&e&k||&e", 1, 1, false),
    ELITE(3, "ELITE", "&9&lELITE ", "&2&k||&2[&a&lELITE&2]&2&k||&a&l", 2, 2, false),
    MVP(4, "MVP", "&b&lMVP ", "&3&k||&3[&b&lMVP&3]&3&k||&b&l", 3, 4, false),
    YT(5, "YT", "&f&lYT ", "&a&lYT&a", 3, 0, true),
    DIOS(6, "DIOS", "&f&lDIOS ", "&6&k||&e[&f&lDIOS&e]&6&k||&f&l", 4, 6, false),
    Builder(7, "Constructor", "&2Constructor ", "&a[&2&lCONSTRUCTOR&a]&a&l", 5, 0, true),
    Helper(8, "Ayudante", "&aAyudante ", "&2[&a&lAYUDANTE&2]&e&l", 6, 0, true),
    GM(9, "GM", "&5&lGM ", "&d(&5&lGM&d)&d&l", 7, 0, true),
    ADMIN(10, "Admin", "&c&lADMIN ", "&4[&c&lADMIN&4]&4&l", 8, 0, true),
    SADMIN(11, "Admin", "&c&lS.ADMIN ", "&4[&c&lS.ADMIN&4]&4&l", 9, 0, true),
    OWNER(12, "Owner", "&4&lOWNER ", "&c[&4&lCREADOR&c]&4&l", 9, 0, true);
    int aposition;
    String anameid;
    String ascoreprefix;
    String achatprefix;
    int apermpos;
    int arankpower;
    List<String> permissions;
    List<String> negativepermissions;
    boolean _isSpecial;

    RankType(int position, String nameid, String scoreprefix, String chatprefix, int permpos, int rankpower, boolean isSpecial) {
        _isSpecial = isSpecial;
        permissions = new ArrayList();
        negativepermissions = new ArrayList();
        aposition = position;
        anameid = nameid;
        ascoreprefix = Code.Color(scoreprefix);
        achatprefix = Code.Color(chatprefix);
        apermpos = permpos;
        arankpower = rankpower;

        //Default permissions - Start
        //Negative
        negativepermissions.add("mcmmo.ability.unarmed.disarm");
        negativepermissions.add("plots.deny");
        //Positive
        permissions.add("cr.crate");
        permissions.add("cr.menu");
        permissions.add("cr.info");
        permissions.add("cr.list");
        permissions.add("essentials.balance");
        permissions.add("essentials.pay");
        permissions.add("jobs.command.toggle");
        permissions.add("jobs.command.help");
        permissions.add("jobs.command.info");
        permissions.add("jobs.command.leaveall");
        permissions.add("jobs.command.leave");
        permissions.add("jobs.command.stats");
        permissions.add("jobs.command.browse");
        permissions.add("jobs.use");
        permissions.add("jobs.command.join");
        permissions.add("jobs.join.*");
        permissions.add("jobs.join");
        permissions.add("showitem.chat");
        permissions.add("skinsrestorer.playercmds");
        permissions.add("music.item.use.*");
        permissions.add("music.radio");
        permissions.add("music.shuffle.skip");
        permissions.add("music.shuffle");
        permissions.add("music.random");
        permissions.add("music.list");
        permissions.add("shopchest.worldguard.bypass");
        permissions.add("shopchest.sell");
        permissions.add("shopchest.buy");
        permissions.add("shopchest.create");
        permissions.add("essentials.signs.use.warp");
        permissions.add("preciousstones.limit1");
        permissions.add("preciousstones.benefit.create.forcefield");
        permissions.add("plots.permpack.basic");
        permissions.add("plots.plot.1");
        permissions.add("plots.use");
        permissions.add("shopkeeper.trade");
        permissions.add("mcmmo.item.chimaerawing");
        permissions.add("mcmmo.commands.woodcutting");
        permissions.add("mcmmo.commands.unarmed");
        permissions.add("mcmmo.commands.taming");
        permissions.add("mcmmo.commands.swords");
        permissions.add("mcmmo.commands.smelting");
        permissions.add("mcmmo.commands.repair");
        permissions.add("mcmmo.commands.ptp.toggle");
        permissions.add("mcmmo.commands.ptp.acceptall");
        permissions.add("mcmmo.commands.ptp.accept");
        permissions.add("mcmmo.commands.ptp");
        permissions.add("mcmmo.commands.party.*");
        permissions.add("mcmmo.commands.party");
        permissions.add("mcmmo.commands.mining");
        permissions.add("mcmmo.commands.mctop.*");
        permissions.add("mcmmo.commands.mctop");
        permissions.add("mcmmo.commands.mcstats");
        permissions.add("mcmmo.commands.mcrank");
        permissions.add("mcmmo.commands.mchud");
        permissions.add("mcmmo.commands.mcability");
        permissions.add("mcmmo.commands.herbalism");
        permissions.add("mcmmo.commands.fishing");
        permissions.add("mcmmo.commands.excavation");
        permissions.add("mcmmo.commands.axes");
        permissions.add("mcmmo.commands.archery");
        permissions.add("mcmmo.commands.acrobatics");
        permissions.add("mcmmo.commands.mcmmo.description");
        permissions.add("mcmmo.defaults");
        permissions.add("mcmmo.skills.all");
        permissions.add("mcmmo.commands.mcmmo.help");
        permissions.add("mcmmo.ability.*");
        permissions.add("factionChat.userAssistantChat");
        permissions.add("factionChat.enemyChat");
        permissions.add("factionChat.otherChat");
        permissions.add("factionChat.allyChat");
        permissions.add("factionChat.factionChat");
        permissions.add("factions.kit.fullplayer");
        permissions.add("kits.sign");
        permissions.add("kits.gui");
        permissions.add("essentials.ping");
        permissions.add("essentials.tpdeny");
        permissions.add("essentials.worlds.*");
        permissions.add("essentials.tpahere");
        permissions.add("essentials.tpaccept");
        permissions.add("essentials.tpa");
        permissions.add("preciousstones.whitelist.*");
        permissions.add("preciousstones.benefit.change-owner");
        permissions.add("preciousstones.benefit.repair");
        permissions.add("preciousstones.benefit.potions");
        permissions.add("preciousstones.benefit.feed");
        permissions.add("preciousstones.benefit.locations");
        permissions.add("preciousstones.benefit.counts");
        permissions.add("preciousstones.benefit.onoff");
        permissions.add("preciousstones.benefit.who");
        permissions.add("preciousstones.benefit.visualize");
        permissions.add("preciousstones.benefit.heal");
        permissions.add("preciousstones.benefit.enable");
        permissions.add("preciousstones.benefit.disable");
        permissions.add("preciousstones.benefit.fields");
        permissions.add("runes.use");
        permissions.add("essentials.keepxp");
        permissions.add("essentials.warps.*");
        permissions.add("essentials.warp");
        permissions.add("essentials.warp.list");
        permissions.add("essentials.kits.novato");
        permissions.add("essentials.kits.guias");
        permissions.add("essentials.kit");
        permissions.add("essentials.afk.auto");
        permissions.add("essentials.compass");
        permissions.add("essentials.book");
        permissions.add("essentials.depth");
        permissions.add("essentials.getpos");
        permissions.add("essentials.help");
        permissions.add("essentials.helpop");
        permissions.add("essentials.ignore");
        permissions.add("essentials.info");
        permissions.add("essentials.itemdb");
        permissions.add("essentials.list.hidden");
        permissions.add("essentials.list");
        permissions.add("essentials.mail.send");
        permissions.add("essentials.mail");
        permissions.add("essentials.near");
        permissions.add("essentials.motd");
        permissions.add("essentials.msg.multiple");
        permissions.add("essentials.msg.url");
        permissions.add("essentials.msg");
        permissions.add("essentials.realname");
        permissions.add("essentials.rules");
        permissions.add("essentials.suicide");
        permissions.add("essentials.kick.notify");
        permissions.add("essentials.ban.notify");
        permissions.add("essentials.spawn");
        permissions.add("essentials.back.ondeath");
        permissions.add("essentials.back");
        permissions.add("essentials.delhome");
        permissions.add("essentials.home.bed");
        permissions.add("essentials.home");
        permissions.add("essentials.sethome.multiple.default");
        permissions.add("essentials.sethome.multiple");
        permissions.add("essentials.sethome.bed");
        permissions.add("essentials.sethome");
        permissions.add("essentials.signs.use.heal");
        permissions.add("essentials.signs.use.repair");
        permissions.add("essentials.signs.use.mail");
        permissions.add("essentials.signs.use.kit");
        permissions.add("essentials.msgtoggle");

        permissions.add("MyPet.leash.CaveSpider");
        permissions.add("MyPet.leash.Chicken");
        permissions.add("MyPet.leash.Cow");
        permissions.add("MyPet.leash.Creeper");
        permissions.add("MyPet.leash.Enderman");
        permissions.add("MyPet.leash.Endermite");
        permissions.add("MyPet.leash.IronGolem");
        permissions.add("MyPet.leash.Mooshroom");
        permissions.add("MyPet.leash.Mule");
        permissions.add("MyPet.leash.Ocelot");
        permissions.add("MyPet.leash.Pig");
        permissions.add("MyPet.leash.PigZombie");
        permissions.add("MyPet.leash.Sheep");
        permissions.add("MyPet.leash.Silverfish");
        permissions.add("MyPet.leash.Skeleton");
        permissions.add("MyPet.leash.SkeletonHorse");
        permissions.add("MyPet.leash.Snowman");
        permissions.add("MyPet.leash.Spider");
        permissions.add("MyPet.leash.Squid");
        permissions.add("MyPet.leash.Stray");
        permissions.add("MyPet.leash.Villager");
        permissions.add("MyPet.leash.Witch");
        permissions.add("MyPet.leash.Wolf");
        permissions.add("MyPet.leash.Zombie");
        permissions.add("MyPet.leash.ZombieVillager");
        permissions.add("MyPet.command.info.other");
        permissions.add("MyPet.command.capturehelper");
        permissions.add("MyPet.command.release");
        permissions.add("MyPet.command.respawn");
        permissions.add("MyPet.command.name");
        permissions.add("MyPet.command.switch");
        permissions.add("MyPet.shop.access.all");
        permissions.add("MyPet.shop.access.babies");
        permissions.add("modifyworld.*");
        permissions.add("MyPet.skilltree.combat");
        permissions.add("MyPet.skilltree.utility");
        permissions.add("MyPet.skilltree.pvp");
        permissions.add("MyPet.skilltree.ride");
        permissions.add("MyPet.skilltree.farm");
        permissions.add("MyPet.extended.feed");
        permissions.add("MyPet.extended.beacon");
        permissions.add("MyPet.extended.behavior.friendly");
        permissions.add("MyPet.extended.behavior.aggressive");
        permissions.add("MyPet.extended.behavior.farm");
        permissions.add("MyPet.extended.behavior.raid");
        permissions.add("MyPet.extended.behavior.duel");
        permissions.add("MyPet.extended.inventory");
        permissions.add("MyPet.extended.ride");
        permissions.add("MyPet.extended.control");
        permissions.add("MyPet.extended.pickup");
        permissions.add("MyPet.extended.equip");
        permissions.add("MyPet.extended.nametag");
        permissions.add("MyPet.npc.storage");
        permissions.add("MyPet.npc.shop");

        //Default permissions - End
        if (permpos <= 0) {
            permissions.add("MyPet.petstorage.limit.5");
        }

        if (permpos > 0) {
            permissions.add("MyPet.petstorage.limit.18");
            permissions.add("MyPet.command.name.color");
            permissions.add("MyPet.leash.Blaze");
            permissions.add("MyPet.leash.Donkey");
            permissions.add("MyPet.leash.ElderGuardian");
            permissions.add("MyPet.leash.Ghast");
            permissions.add("MyPet.leash.MagmaCube");
            permissions.add("MyPet.leash.ZombieHorse");
            permissions.add("MyPet.leash.WitherSkeleton");
            permissions.add("MyPet.leash.Llama");
            permissions.add("MyPet.leash.Bat");
            permissions.add("MyPet.leash.Rabbit");
            permissions.add("MyPet.leash.Guardian");
            permissions.add("MyPet.leash.Husk");
            permissions.add("MyPet.leash.Slime");
            permissions.add("essentials.sethome.multiple");
            permissions.add("essentials.enderchest");
            permissions.add("essentials.workbench");
        }

        if (permpos >= 1) {
            permissions.add("essentials.kits.vip");
            if (permpos == 1) {
                permissions.add("essentials.sethome.multiple.vip");
                permissions.add("preciousstones.limit2");
            }
        }

        if (permpos >= 2) {
            permissions.add("essentials.kits.elite");
            if (permpos == 2) {
                permissions.add("essentials.sethome.multiple.elite");
                permissions.add("preciousstones.limit3");
            }
        }
        if (permpos >= 3) {
            permissions.add("MyPet.leash.Wither");
            permissions.add("essentials.kits.mvp");
            if (permpos == 3) {
                permissions.add("essentials.sethome.multiple.mvp");
                permissions.add("preciousstones.limit4");
            }
        }
        if (permpos >= 4) {
            permissions.add("essentials.kits.dios");
            permissions.add("essentials.enderchest.others");
            permissions.add("essentials.invsee");
            if (permpos == 4) {
                permissions.add("essentials.sethome.multiple.dios");
                permissions.add("preciousstones.limit5");
            }
        }
        if (permpos >= 5) {
            permissions.add("essentials.helpop.receive");
            permissions.add("essentials.sethome.multiple.staff");
            permissions.add("preciousstones.limit5");
            permissions.add("minelevel.ban");
            permissions.add("minelevel.unban");
            permissions.add("minelevel.kick");
            permissions.add("minelevel.vanish");
            permissions.add("minelevel.tp");
            if (permpos == 5) {
                permissions.add("essentials.unlimited");
                permissions.add("essentials.unlimited.item-all");
            }
        }
        if (permpos >= 7) {
            permissions.add("nocheatplus.command.notify");
            permissions.add("nocheatplus.notify");
            permissions.add("essentials.jails");
            permissions.add("essentials.jail.exempt");
            permissions.add("essentials.togglejail");
            permissions.add("essentials.togglejail.offline");
        }
        if (permpos >= 8) {
            permissions.add("essentials.tppos");
        }
        if (permpos >= 9) {
            permissions.add("essentials.gamemode");
            permissions.add("essentials.kickall");
            permissions.add("minecraft.command.whitelist");
            permissions.add("bukkit.command.whitelist.enable");
            permissions.add("bukkit.command.whitelist.disable");
            permissions.add("bukkit.command.whitelist.list");
            permissions.add("bukkit.command.whitelist.reload ");
            permissions.add("mcmmo.commands.vampirism.toggle");
            permissions.add("mcmmo.commands.vampirism");
            permissions.add("mcmmo.commands.hardcore.toggle");
            permissions.add("mcmmo.commands.hardcore");
        }
    }

    public static RankType getByName(String str) {
        for (RankType rk : RankType.values()) {
            if (rk.getName().equalsIgnoreCase(str)) {
                return rk;
            }
        }
        return RankType.User;
    }

    public static RankType getByPower(int rankPower) {
        RankType myRankType = RankType.User;

        if (rankPower > 0) {
            for (RankType rankType : RankType.values()) {
                int otherRankPower = rankType.getPower();
                if (otherRankPower > 0 && otherRankPower > myRankType.getPower() && rankPower >= otherRankPower) {
                    myRankType = rankType;
                }
            }
        }

        return myRankType;
    }

    public boolean isAtLeast(RankType compare) {
        return getPosition() >= compare.getPosition();
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getNegativePermissions() {
        return negativepermissions;
    }

    public String getScoreboardPrefix() {
        return ascoreprefix;
    }

    public String getChatPrefix() {
        return achatprefix;
    }

    public int getPower() {
        return arankpower;
    }

    public String getName() {
        return anameid;
    }

    public int getPosition() {
        return aposition;
    }

    public boolean isSpecial() {
        return _isSpecial;
    }
}