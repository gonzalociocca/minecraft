package server.user.column.Valuable;

import com.google.gson.annotations.Expose;
import server.user.column.Valuable.DataTypes.GlobalRanks;

public class ValuableData {

     // Ranks, Boosters, Mistery Boxes, Companions(BlockPet buyable from store)


    // Expose Start
    @Expose
    GlobalRanks _globalranks = new GlobalRanks();
    // Expose End

    public static String getColumn() {
        return "ValuableData";
    }

    public GlobalRanks getGlobalRanks() {
        return _globalranks;
    }

    public boolean shouldSave() {
        return _globalranks.isModified();
    }

}