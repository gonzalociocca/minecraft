package me.gonzalociocca.minelevel.core.user.economy;

import me.gonzalociocca.minelevel.core.Main;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;

/**
 * Created by ciocca on 22/11/16.
 */
public class MLEconomy implements Economy {
    Main main=null;
    public MLEconomy(Main mn){
        main=mn;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "Diamantes";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return ""+v;
    }

    @Override
    public String currencyNamePlural() {
        return "Diamantes";
    }

    @Override
    public String currencyNameSingular() {
        return "Diamante";
    }

    @Override
    public boolean hasAccount(String s) {
        return main.getDB().hasPlayerData(s);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return main.getDB().hasPlayerData(offlinePlayer.getName());
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return main.getDB().hasPlayerData(s);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return main.getDB().hasPlayerData(offlinePlayer.getName());
    }

    @Override
    public double getBalance(String s) {
        return main.getDB().getPlayerData(s).getDiamonds();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return main.getDB().getPlayerData(offlinePlayer.getName()).getDiamonds();
    }

    @Override
    public double getBalance(String s, String s1) {
        return main.getDB().getPlayerData(s).getDiamonds();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return main.getDB().getPlayerData(offlinePlayer.getName()).getDiamonds();
    }

    @Override
    public boolean has(String s, double v) {
        return main.getDB().getPlayerData(s).hasDiamonds(v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return main.getDB().getPlayerData(offlinePlayer.getName()).hasDiamonds(v);
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return main.getDB().getPlayerData(s).hasDiamonds(v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return main.getDB().getPlayerData(offlinePlayer.getName()).hasDiamonds(v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        return main.getDB().getPlayerData(s).withdrawDiamonds(v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        return main.getDB().getPlayerData(offlinePlayer.getName()).withdrawDiamonds(v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return main.getDB().getPlayerData(s).withdrawDiamonds(v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return main.getDB().getPlayerData(offlinePlayer.getName()).withdrawDiamonds(v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        return main.getDB().getPlayerData(s).depositDiamonds(v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        return main.getDB().getPlayerData(offlinePlayer.getName()).depositDiamonds(v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return main.getDB().getPlayerData(s).depositDiamonds(v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return main.getDB().getPlayerData(offlinePlayer.getName()).depositDiamonds(v);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return main.getDB().getPlayerData(s)!=null;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return main.getDB().getPlayerData(offlinePlayer.getName())!=null;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return main.getDB().getPlayerData(s) != null;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return main.getDB().getPlayerData(offlinePlayer.getName()) != null;
    }
}
