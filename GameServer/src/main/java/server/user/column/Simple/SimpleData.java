package server.user.column.Simple;

import com.google.gson.annotations.Expose;
import server.user.column.Simple.Type.*;

// Store statistics, kits, money, etc.

public class SimpleData {

    // Expose Start

    @Expose
    BlockBreak _blockBreak = new BlockBreak();

    @Expose
    BlockPlace _blockPlace = new BlockPlace();

    @Expose
    BowHit _bowHit = new BowHit();

    @Expose
    Cage _cage = new Cage();

    @Expose
    Death _death = new Death();

    @Expose
    Kill _kill = new Kill();

    @Expose
    Kits _kits = new Kits();

    @Expose
    Money _money = new Money();

    // Expose End

    public SimpleData() {}

    public static String getColumn() {
        return "SimpleData";
    }

    public BlockBreak getBlockBreak() {
        return _blockBreak;
    }

    public BlockPlace getBlockPlace() {
        return _blockPlace;
    }

    public BowHit getBowHit() {
        return _bowHit;
    }

    public Cage getCage() {
        return _cage;
    }

    public Death getDeath() {
        return _death;
    }

    public Kill getKill() {
        return _kill;
    }

    public Kits getKits() {
        return _kits;
    }

    public Money getMoney() {
        return _money;
    }

    public boolean shouldSave() {
        return getBlockBreak().isModified() || getBlockPlace().isModified() || getBowHit().isModified() || getCage().isModified() || getDeath().isModified() || getKill().isModified() || getKits().isModified() || getMoney().isModified();
    }

}
