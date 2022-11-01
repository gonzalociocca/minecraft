package server.common;

import com.google.gson.annotations.Expose;

/**
 * Created by noname on 24/3/2017.
 */
public class Rank {

    public enum RankType {
        User(0,"Usuario",Code.Color("&7Usuario"),Code.Color("&7"),Code.Color("&7")),
        VIP(1,"VIP",Code.Color("&e&l[VIP]"),Code.Color("&e&lVIP &e"),Code.Color("&e&l")),
        VIP2(2,"VIP2",Code.Color("&e&l[VIP+]"),Code.Color("&e&lVIP+ &e"),Code.Color("&e&l")),
        MVP(3,"MVP",Code.Color("&b&l[MVP]"),Code.Color("&b&lMVP &b"),Code.Color("&b&l")),
        MVP2(4,"MVP2",Code.Color("&b&l[MVP+]"),Code.Color("&b&lMVP+ &b"),Code.Color("&b&l"));

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

    @Expose
    long init;

    @Expose
    long finish;

    @Expose
    String rank;

    RankType type=null;

    public Rank(long initialTime, long finishTime, String rankType){
        init = initialTime;
        finish = finishTime;
        rank = rankType;
        type = RankType.getByName(rank);
    }

    public RankType getRankType(){
        if(type==null){
            type = RankType.getByName(rank);
        }
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
