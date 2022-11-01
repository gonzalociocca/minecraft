package mineultra.minecraft.game.core.Buffer;

public class BufferActive
{
    private Buffer _buffer;
    
    public BufferActive(final Buffer condition) {
        super();
        this.SetBuffer(condition);
    }
    
    public Buffer GetBuffer() {
        return this._buffer;
    }
    
    public void SetBuffer(final Buffer newCon) {
        (this._buffer = newCon).Apply();
    }
}
