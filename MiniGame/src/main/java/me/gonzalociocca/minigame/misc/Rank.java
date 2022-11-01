package me.gonzalociocca.minigame.misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noname on 24/3/2017.
 */
public class Rank {

    public enum RankType {
        User(0,"Usuario","&7Usuario","&7Usuario","&7"),
        VIP(1,"VIP","&e&l[VIP]","&e&lVIP","&e&l"),
        VIP2(2,"VIP2","&e&l[VIP+]","&e&lVIP+","&e&l"),
        MVP(3,"MVP","&b&l[MVP]","&b&lMVP","&b&l"),
        MVP2(4,"MVP2","&b&l[MVP+]","&b&lMVP+","&b&l");
        String vipname;
        int pos;
        String chatprefix;
        String scoprefix;
        String acolor;
        RankType(int position, String rankName,String achatprefix, String scoreboardprefix, String color) {
            pos = position;
            vipname = rankName;
            chatprefix = achatprefix;
            scoprefix = scoreboardprefix;
            acolor = color;
        }

        public String getColor(){
            return acolor;
        }

        public boolean isAtLeast(RankType compare){
            return getPosition() >= compare.getPosition();
        }

        public String getScoreboardPrefix(){return scoprefix;}

        public String getChatPrefix(){
            return chatprefix;
        }

        public String getName(){
            return vipname;
        }
        public int getPosition(){
            return pos;
        }
        public static RankType getByName(String str){
            for(RankType rk : RankType.values()){
                if(rk.getName().equalsIgnoreCase(str)){
                    return rk;
                }
            }
            return RankType.User;
        }
    }

    long init;
    long finish;
    String rank;
    RankType type;
    public Rank(long initialTime, long finishTime, String rankType){
        init = initialTime;
        finish = finishTime;
        rank = rankType;
        type = RankType.getByName(rank);
    }
    public RankType getRankType(){
        return type;
    }
    public long getInitialTime(){
        return init;
    }
    public long getFinishTime(){
        return finish;
    }
    public String getRankName(){
        return rank;
    }
    public boolean isExpired(){
        return System.currentTimeMillis() > finish;
    }
}
