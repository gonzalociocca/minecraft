package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.MessageDigestAlgorithm;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

public class CRAZYCRYPT1 extends UsernameSaltMethod {

    private static final char[] CRYPTCHARS =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private final Charset charset = Charset.forName("UTF-8");

  public static String byteArrayToHexString(byte... args)
  {
    char[] chars = new char[args.length * 2];
    for (int i = 0; i < args.length; i++)
    {
      chars[(i * 2)] = CRYPTCHARS[(args[i] >> 4 & 0xF)];
      chars[(i * 2 + 1)] = CRYPTCHARS[(args[i] & 0xF)];
    }
    return new String(chars);
  }

    @Override
    public HashedPassword computeHash(String password, String name) {
       name = Bukkit.getPlayer(name).getName();
       if(Bukkit.getPlayer(name) != null){
           name = Bukkit.getPlayer(name).getName();
       }else if(Bukkit.getOfflinePlayer(name) != null){
           name = Bukkit.getOfflinePlayer(name).getName();
       }
if(password.contains("|")){
    password = password.replace("|", " ");
}
String text = this.encrypt(name, null, password);

        
        return new HashedPassword(text);
    }
    
  public String encrypt(String name, String salt, String password)
  {
    String text = "ÜÄaeut//&/=I " + password + "7421€547" + name + "__+IÄIH§%NK " + password;
    MessageDigest md = HashUtils.getDigest(MessageDigestAlgorithm.SHA512);
    md.update(text.getBytes(this.charset), 0, text.length());

    return byteArrayToHexString(md.digest());
  }

}