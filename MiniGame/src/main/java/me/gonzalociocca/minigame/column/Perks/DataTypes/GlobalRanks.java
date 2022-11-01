package me.gonzalociocca.minigame.column.Perks.DataTypes;

import me.gonzalociocca.minigame.misc.Rank;

import java.util.ArrayList;
import java.util.List;

public class GlobalRanks extends PerkBaseType {
    /**
     Rank format:
     initialTime,finishTime,RankType-
     **/

    List<Rank> rankList = new ArrayList();

    public GlobalRanks(List<Rank> ranks){
        rankList = ranks;
    }
    public List<Rank> getRanks(){
        return rankList;
    }
    public Rank getRank(){
        Rank last = null;
        for(Rank rank : rankList){
            if(rank.isExpired()){
                continue;
            }
            if(last==null || rank.getRankType().getPosition() > last.getRankType().getPosition()){
                last = rank;
            }
        }
        return last!=null?last:new Rank(1L,9999999999999L,"Usuario");
    }

    public void addRank(long start, long end, String type){
        rankList.add(new Rank(start,end,type));
    }
    public void removeRank(String type){
        Rank toremove = null;
        for(Rank rank : rankList){
            if(rank.getRankName().equalsIgnoreCase(type)){
                toremove = rank;
                break;
            }
        }
        if(toremove!=null) {
            rankList.remove(toremove);
        }
    }
    @Override
    public String getIdentifier(){
        return "GlobalRanks";
    }

    public static String getStaticIdentifier() { return "GlobalRanks";}

    @Override
    public PerkBaseType decode(String content){
        List<Rank> ranks = new ArrayList();

        String[] parts = content.split("-");
        for(String obj : parts){
            String[] parts2 = obj.split(",");
            ranks.add(new Rank(Long.parseLong(parts2[0]),Long.parseLong(parts2[1]),parts2[2]));
        }
        return new GlobalRanks(ranks);
    }

    @Override
    public String encode(){
        String str = "";
        for(Rank rank : getRanks()){
            if(str.isEmpty()){
                str = rank.getInitialTime()+","+rank.getFinishTime()+","+rank.getRankName();
            }else{
                str = str +"-"+rank.getInitialTime()+","+rank.getFinishTime()+","+rank.getRankName();
            }
        }
        return getIdentifier()+"=<"+str+">";
    }

    @Override
    public boolean isEmpty(){
        return rankList.isEmpty();
    }

    @Override
    public PerkBaseType getDefault(){
        return new GlobalRanks(new ArrayList());
    }
}
