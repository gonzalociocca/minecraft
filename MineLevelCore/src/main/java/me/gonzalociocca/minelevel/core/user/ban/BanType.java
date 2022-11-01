package me.gonzalociocca.minelevel.core.user.ban;

/**
 * Created by noname on 9/2/2017.
 */
public enum BanType {
        Permanente("permanente","Razones superiores.",365),
        Hacks("hacks","Usar hacks",30),
        Grifear("grifear","Grifear",15),
        Bugear("bugear","Bugear",10),
        Insultos("insultos","Hablar mal",1),
        Unknown("Unknown","Sin razon",0);
        String atype;
        String areason;
        int adefaultdays;
        BanType(String bantype, String reason, int defaultdays) {
            atype = bantype;
            areason = reason;
            adefaultdays = defaultdays;
        }
        public String getBanTypeName(){
            return atype;
        }
        public String getReason(){
            return areason;
        }
        public int getDefaultDays(){
            return adefaultdays;
        }

    public static BanType getByName(String str) {
        for (BanType tp : BanType.values()) {
            if (tp.getBanTypeName().equalsIgnoreCase(str)) {
                return tp;
            }
        }
        return BanType.Unknown;
    }
}
