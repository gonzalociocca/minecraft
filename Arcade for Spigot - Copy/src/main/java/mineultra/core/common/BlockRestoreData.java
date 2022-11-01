package mineultra.core.common;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockRestoreData
{
  protected Block _block;
  protected int _fromID;
  protected byte _fromData;
  protected int _toID;
  protected byte _toData;
  protected long _expireDelay;
  protected long _epoch;
  protected long _meltDelay = 0L;
  protected long _meltLast = 0L;

  public BlockRestoreData(Block block, int toID, byte toData, long expireDelay, long meltDelay)
  {
    _block = block;

    _fromID = block.getTypeId();
    _fromData = block.getData();

    _toID = toID;
    _toData = toData;

    _expireDelay = expireDelay;
    _epoch = System.currentTimeMillis();

    _meltDelay = meltDelay;
    _meltLast = System.currentTimeMillis();

    set();
  }

  public boolean expire()
  {
    if (System.currentTimeMillis() - _epoch < _expireDelay) {
      return false;
    }

    if (melt()) {
      return false;
    }

    restore();
    return true;
  }

  public boolean melt()
  {
    if ((_block.getTypeId() != 78) && (_block.getTypeId() != 80)) {
      return false;
    }
    if ((_block.getRelative(BlockFace.UP).getTypeId() == 78) || (_block.getRelative(BlockFace.UP).getTypeId() == 80))
    {
      _meltLast = System.currentTimeMillis();
      return true;
    }

    if (System.currentTimeMillis() - _meltLast < _meltDelay) {
      return true;
    }

    if (_block.getTypeId() == 80) {
      _block.setTypeIdAndData(78, (byte)7, false);
    }
    byte data = _block.getData();
    if (data <= 0) return false;

    _block.setData((byte)(_block.getData() - 1));
    _meltLast = System.currentTimeMillis();
    return true;
  }

  public void update(int toIDIn, byte toDataIn)
  {
    _toID = toIDIn;
    _toData = toDataIn;

    set();
  }

  public void update(int toID, byte addData, long expireTime)
  {
    if (toID == 78)
    {
      if (_toID == 78) _toData = ((byte)Math.min(7, _toData + addData)); else {
        _toData = addData;
      }
    }
    _toID = toID;

    set();

    _expireDelay = expireTime;
    _epoch = System.currentTimeMillis();
  }

  public void update(int toID, byte addData, long expireTime, long meltDelay)
  {
    if (toID == 78)
    {
      if (_toID == 78) _toData = ((byte)Math.min(7, _toData + addData)); else {
        _toData = addData;
      }
    }
    _toID = toID;

    set();

    _expireDelay = expireTime;
    _epoch = System.currentTimeMillis();

    if (_meltDelay < meltDelay)
      _meltDelay = ((_meltDelay + meltDelay) / 2L);
  }

  public void set()
  {
    if ((_toID == 78) && (_toData == 7))
      _block.setTypeIdAndData(80, (byte)0, true);
    else
      _block.setTypeIdAndData(_toID, _toData, true);
  }

  public void restore()
  {
    _block.setTypeIdAndData(_fromID, _fromData, true);
  }

  public void setFromId(int i)
  {
    _fromID = i;
  }

  public void setFromData(byte i)
  {
    _fromData = i;
  }
}