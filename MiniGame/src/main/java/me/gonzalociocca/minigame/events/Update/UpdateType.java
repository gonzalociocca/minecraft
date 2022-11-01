package me.gonzalociocca.minigame.events.Update;

import me.gonzalociocca.minigame.misc.Code;

public enum UpdateType
{
    FiveSec(5000l),
    Sec(1000l),
    Fast(250l),
    Slow(500l),
    Tick(50l);

    private final long _time;
    private long _last;
    private long _timeSpent;
    private long _timeCount;

    private UpdateType(long time) {
        _time = time;
        _last = System.currentTimeMillis();
    }

    public boolean Elapsed()
    {
        if (Code.elapsed(_last, _time))
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