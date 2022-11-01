package server.user.column.Valuable.DataTypes;

import com.google.gson.annotations.Expose;
import server.common.Rank;

import java.util.ArrayList;
import java.util.List;

public class GlobalRanks {
    /**
     Rank format:
     initialTime,finishTime,RankType-
     **/

    boolean modified = false;
    @Expose List<Rank> rankList = new ArrayList<Rank>();

    public GlobalRanks(){

    }

    public List<Rank> getRankList(){
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


    public boolean isModified(){
        return modified;
    }

    public void setModified(boolean value){
        modified = value;
    }
}
