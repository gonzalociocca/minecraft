/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.game.center.kit.perks.data;

import java.util.HashSet;
import org.bukkit.entity.Player;

public class ReboundData
{
    public Player Shooter;
    public HashSet<Player> Ignore;
    public int Bounces;
    
    public ReboundData(final Player shooter, final int bounces, final HashSet<Player> previousIgnore) {
        super();
        this.Ignore = new HashSet<Player>();
        this.Shooter = shooter;
        this.Bounces = bounces;
        if (previousIgnore != null) {
            this.Ignore = previousIgnore;
        }
        this.Ignore.add(shooter);
    }
}
