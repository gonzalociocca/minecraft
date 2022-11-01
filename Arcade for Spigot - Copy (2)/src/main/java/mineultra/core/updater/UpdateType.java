package mineultra.core.updater;

import mineultra.core.common.util.UtilTime;



public enum UpdateType
{
  MIN_64(3840000L), 
  MIN_32(1920000L), 
  MIN_16(960000L), 
  MIN_08(480000L), 
  MIN_04(240000L), 
  MIN_02(120000L), 
  MIN_01(60000L), 
  SLOWEST(32000L), 
  SLOWER(16000L), 
  SLOW(4000L),
  SEC2(2000L),
  SEC(1000L), 
  FAST(500L), 
  FASTER(250L), 
  FASTEST(125L), 
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