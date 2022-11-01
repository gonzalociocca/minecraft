package me.gonzalociocca.minelevel.core.user.rank;

import java.util.List;

public class Rank
{
    RankType _rankType;
    long _startTime;
    long _endTime;

    public Rank(RankType rankType, long startTime, long endTime){
        _rankType = rankType;
        _startTime = startTime;
        _endTime = endTime;
    }

    public RankType getType(){
        return _rankType;
    }

    public long getStartTime(){
        return _startTime;
    }

    public long getEndTime(){
        return _endTime;
    }

    public int getDaysLeft(long now){
        return Math.max(0,(int)((_endTime - now)/(1000*60*60*24)));
    }

    public int getHoursLeft(long now){
        return Math.max(0,(int)((_endTime - now)%86400000)/3600000);
    }

    public int getMinutesLeft(long now){
        return Math.max(0,(int)((_endTime - now)%(60000*60))/60000);
    }

    public int getSecondsLeft(long now){
        return Math.max(0,(int)((_endTime - now)%(60000))/1000);
    }


    public static Rank fromString(String rankString){
        Rank rank = null;
        try {
            if (rankString != null && !rankString.isEmpty()) {
                String updated = rankString.replace(":", ","); // Replace ":" to "," due to version update.
                String[] args = updated.split(",");

                RankType rankType = RankType.getByName(args[0]);
                long startTime = Long.parseLong(args[1]);
                long endTime = Long.parseLong(args[2]);
                long now = System.currentTimeMillis();

                if (now < endTime && rankType != RankType.User) {
                    rank = new Rank(rankType, startTime, endTime);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("RankValue: "+rankString);
            rank = new Rank(RankType.User, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        return rank;
    }

    public String toString(){
        return _rankType.getName() + "," + _startTime + "," + _endTime;
    }

    public static Rank getCurrentRank(List<Rank> rankList){
        int rankPower = 0;
        long startTime = Long.MIN_VALUE;
        long endTime = Long.MAX_VALUE;

        RankType highRank = RankType.User;

        if(!rankList.isEmpty()) {
            for(Rank rank : rankList){
                RankType rankType = rank.getType();
                if(rankType.getPosition() > highRank.getPosition() || !highRank.isSpecial() && rankType.isSpecial()){
                    if(!highRank.isSpecial() || highRank.isSpecial() && rankType.isSpecial()) {
                        highRank = rank.getType();
                    }
                }
                rankPower+=rank.getType().getPower();

                if(rank.getStartTime() > startTime){ // get Nearest Start Time to Zero
                    startTime = rank.getStartTime();
                }
                if(rank.getEndTime() < endTime){ // get Nearest End Time to Zero
                    endTime = rank.getEndTime();
                }
            }
        }
        if(!highRank.isSpecial()) {
            highRank = RankType.getByPower(rankPower);
        }

        return new Rank(highRank, startTime, endTime);
    }




}