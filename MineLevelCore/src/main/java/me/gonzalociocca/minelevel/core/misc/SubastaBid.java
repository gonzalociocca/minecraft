package me.gonzalociocca.minelevel.core.misc;

/**
 * Created by noname on 9/3/2017.
 */
public class SubastaBid {
    String playername;
    int bid;
    int bidcount = 1;
    long latestBid;
    public SubastaBid(String aplayer, int abid){
        playername = aplayer;
        bid = abid;
        latestBid = System.currentTimeMillis()+5000;
    }
    public String getBidderName(){
        return playername;
    }
    public int getMaxBid(){
        return bid;
    }
    public void changeBid(int nextBid){
        bidcount++;
        bid = nextBid;
        latestBid = System.currentTimeMillis()+5000;
    }

    public int getBidCount(){
        return bidcount;
    }
    public boolean canBidOnTime(){
        return System.currentTimeMillis() > latestBid;
    }
}
