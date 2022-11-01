package server.util;


import org.bukkit.ChatColor;

public class F
{
  public static String main(String module, String body)
  {
    return UtilMsg.WhiteBold + module + " > " + C.mBody + body;
  }

  public static String game(String elem)
  {
    return C.mGame + elem + C.mBody;
  }

  public static String elem(String elem)
  {
    return C.mElem + elem + ChatColor.RESET + C.mBody;
  }

  public static String name(String elem)
  {
    return C.mElem + elem + C.mBody;
  }

  public static String item(String elem)
  {
    return C.mItem + elem + C.mBody;
  }

  public static String skill(String elem)
  {
    return C.mSkill + elem + C.mBody;
  }

  public static String time(String elem)
  {
    return C.mTime + elem + C.mBody;
  }

  public static String desc(String head, String body)
  {
    return C.descHead + head + ": " + C.descBody + body;
  }

  public static String value(String variable, String value)
  {
    return value(0, variable, value);
  }

  public static String value(int a, String variable, String value)
  {
    String indent = "";
    while (indent.length() < a) {
      indent = indent + ChatColor.GRAY + ">";
    }
    return indent + C.listTitle + variable + ": " + C.listValue + value;
  }

  public static String value(String variable, String value, boolean on)
  {
    return value(0, variable, value, on);
  }

  public static String value(int a, String variable, String value, boolean on)
  {
    String indent = "";
    while (indent.length() < a) {
      indent = indent + ChatColor.GRAY + ">";
    }
    if (on) return indent + C.listTitle + variable + ": " + C.listValueOn + value;
    return indent + C.listTitle + variable + ": " + C.listValueOff + value;
  }

}