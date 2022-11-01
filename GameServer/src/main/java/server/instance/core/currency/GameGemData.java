package server.instance.core.currency;

public class GameGemData
{
  public double Gems;
  public int Amount;

  public GameGemData(double gems, boolean amount)
  {
    Gems = gems;

    if (amount)
      Amount = 1;
  }

  public void AddGems(double gems)
  {
    Gems += gems;

    if (Amount > 0)
      Amount += 1;
  }
}