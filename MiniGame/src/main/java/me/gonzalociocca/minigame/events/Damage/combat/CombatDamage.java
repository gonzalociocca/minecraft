package me.gonzalociocca.minigame.events.Damage.combat;


import me.gonzalociocca.minigame.events.Damage.damage.DamageChange;

import java.util.ArrayList;
import java.util.List;

public class CombatDamage
{
	private String _name;
	private double _dmg;
	private long _time;
	private List<DamageChange> _mod = new ArrayList<>();

	public CombatDamage(String name, double dmg, List<DamageChange> mod)
	{
		_name = name;
		_dmg = dmg;
		_time = System.currentTimeMillis();
		_mod = mod;
	}

	public String GetName()
	{
		return _name;
	}

	public double GetDamage()
	{
		return _dmg;
	}
	
	public long GetTime()
	{
		return _time;
	}

	public List<DamageChange> getDamageMod()
	{
		return _mod;
	}
}
