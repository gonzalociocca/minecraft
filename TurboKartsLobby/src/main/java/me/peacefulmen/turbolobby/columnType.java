/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.peacefulmen.turbolobby;


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
  Played("Played");

  private String name = null;

  private columnType(String prefix) {
    name = prefix;
  }

  public String getName(){
      return name;
  }
  


}