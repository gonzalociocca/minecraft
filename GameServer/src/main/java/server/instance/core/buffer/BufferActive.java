package server.instance.core.buffer;

import server.instance.GameServer;

public class BufferActive
{
    private Buffer _buffer;
    
    public BufferActive(GameServer game, final Buffer condition) {
        super();
        this.SetBuffer(game, condition);
    }
    
    public Buffer GetBuffer() {
        return this._buffer;
    }
    
    public void SetBuffer(GameServer game, final Buffer newCon) {
        (this._buffer = newCon).Apply(game);
    }
}
