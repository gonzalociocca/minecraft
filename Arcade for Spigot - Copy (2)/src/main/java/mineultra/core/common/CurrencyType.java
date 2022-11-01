package mineultra.core.common;

import org.bukkit.Material;

public enum CurrencyType
{
  Gems("Coins", Material.DIAMOND);

  private final String _prefix;
  private final Material _displayMaterial;

  private CurrencyType(String prefix, Material displayMaterial) {
    _prefix = prefix;
    _displayMaterial = displayMaterial;
  }

  public String Prefix()
  {
    return _prefix;
  }

  public Material GetDisplayMaterial()
  {
    return _displayMaterial;
  }
}