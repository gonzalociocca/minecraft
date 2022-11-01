package mineultra.core.updater;

import mineultra.core.common.util.UtilTime;



public enum UpdateType
{
  MIN1(60000L),
  SLOW2(8000L),
  SEC3(3000L),
  SEC(1000L),
  SLOW(750L),
  FAST(150L),
  TICK(49L);

  private final long _time;
  private long _last;
  private long _timeSpent;
  private long _timeCount;

  private UpdateType(long time) { _time = time;
    _last = System.currentTimeMillis();
  }

  public boolean Elapsed()
  {
    if (UtilTime.elapsed(_last, _time))
    {
      _last = System.currentTimeMillis();
      return true;
    }

    return false;
  }

  public void StartTime()
  {
    _timeCount = System.currentTimeMillis();
  }

  public void StopTime()
  {
    _timeSpent += System.currentTimeMillis() - _timeCount;
  }

  public void PrintAndResetTime()
  {
    System.out.println(name() + " in a second: " + _timeSpent);
    _timeSpent = 0L;
  }
}