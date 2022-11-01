package me.gonzalociocca.versionupdate;

import java.util.HashMap;

public class Colorizer
{
  private static final HashMap<String, String> varcache = new HashMap();
  
  public static String Color(String s)
  {
    if (s == null) {
      return Color("&4&lError");
    }
    return s.replaceAll("(&([a-fk-or0-9]))", "§$2");
  }
  
  public static String replaceVars(String msg, String[] vars)
  {
    for (String str : vars)
    {
      String[] s = str.split("-");
      varcache.put(s[0], s[1]);
    }
    for (String str : varcache.keySet()) {
      msg = msg.replace("{$" + str + "}", (CharSequence)varcache.get(str));
    }
    return msg;
  }
}
