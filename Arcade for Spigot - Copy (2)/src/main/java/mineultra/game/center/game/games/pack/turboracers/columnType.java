
package mineultra.game.center.game.games.pack.turboracers;

import org.bukkit.Material;

public enum columnType
{
  Helmets("Helmets"),
  Horns("Horns"),
  KartSkin("KartSkin"),
  RacingSuits("RacingSuits"),
  ParticleTrail("ParticleTrail"),
  ParticleQuality("ParticleQuality"),
  Turbocharger("Turbocharger"),
  Engine("Engine"),
  Frame("Frame"),
  FirstPlace("FirstPlace"),
  SecondPlace("SecondPlace"),
  ThirdPlace("ThirdPlace"),
  Laps("Laps"),
  Played("Played"),
  Items("Items");

  private String name = null;

  private columnType(String prefix) {
    name = prefix;
  }

  public String getName(){
      return name;
  }
  


}