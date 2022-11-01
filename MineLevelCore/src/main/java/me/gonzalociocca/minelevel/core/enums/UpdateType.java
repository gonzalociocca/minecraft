package me.gonzalociocca.minelevel.core.enums;

import me.gonzalociocca.minelevel.core.misc.Code;

public enum UpdateType
{
    MIN10(600000L),
    MIN1(60000L),
    SEC30(30000L),
    SLOW2(8000L),
    SEC3(3000L),
    SEC(1000L),
    SLOW(500L),
    FAST(150L);

    private final long _time;
    private long _last;
    private long _timeSpent;
    private long _timeCount;

    private UpdateType(long time) { _time = time;
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