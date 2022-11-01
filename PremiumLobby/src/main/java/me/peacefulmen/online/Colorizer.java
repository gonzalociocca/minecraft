package me.peacefulmen.online;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class Colorizer
{
  private static final HashMap<String, String> varcache = new HashMap();
  private static BufferedImage img = null;
 
  public static BufferedImage getFavicon(String args){
if(img == null){
        ImageIcon icon = new ImageIcon(args);
        Image image = icon.getImage();

        BufferedImage buffImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics g = buffImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        img = buffImage;
        return buffImage;
}
      


        return img;
    
  }
  
  public static String Color(String s)
  {
    if (s == null) {
      return Color("&4&lError");
    }
    return s.replaceAll("(&([a-fk-or0-9]))", "\u00A7$2");
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
  
  
