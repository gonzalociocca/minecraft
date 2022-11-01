package net.md_5.bungee.query;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import java.beans.ConstructorProperties;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class QueryHandler
  extends SimpleChannelInboundHandler<DatagramPacket>
{
  private final ProxyServer bungee;
  private final ListenerInfo listener;
  
  @ConstructorProperties({"bungee", "listener"})
  public QueryHandler(ProxyServer bungee, ListenerInfo listener)
  {
    this.bungee = bungee;this.listener = listener;
  }
  
  private final Random random = new Random();
  private final Map<Integer, Long> sessions = new HashMap();
  
  private void writeShort(ByteBuf buf, int s)
  {
    buf.order(ByteOrder.LITTLE_ENDIAN).writeShort(s);
  }
  
  private void writeNumber(ByteBuf buf, int i)
  {
    writeString(buf, Integer.toString(i));
  }
  
  private void writeString(ByteBuf buf, String s)
  {
    for (char c : s.toCharArray()) {
      buf.writeByte(c);
    }
    buf.writeByte(0);
  }
  Random r = new Random();
  protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg)
    throws Exception
  {
    ByteBuf in = (ByteBuf)msg.content();
    if ((in.readUnsignedByte() != 254) || (in.readUnsignedByte() != 253))
    {
      bungee.getLogger().log(Level.WARNING, "Query - Incorrect magic!: {0}", msg.sender());
      return;
    }
    ByteBuf out = ctx.alloc().buffer();
    AddressedEnvelope response = new DatagramPacket(out, (InetSocketAddress)msg.sender());
    
    byte type = in.readByte();
    int sessionId = in.readInt();
    if (type == 9)
    {
      out.writeByte(9);
      out.writeInt(sessionId);
      
      int challengeToken = random.nextInt();
      sessions.put(Integer.valueOf(challengeToken), Long.valueOf(System.currentTimeMillis()));
      
      writeNumber(out, challengeToken);
    }
    if (type == 0)
    {
      int challengeToken = in.readInt();
      Long session = (Long)sessions.get(Integer.valueOf(challengeToken));
      if ((session == null) || (System.currentTimeMillis() - session.longValue() > TimeUnit.SECONDS.toMillis(30L))) {
        throw new IllegalStateException("No session!");
      }
      out.writeByte(0);
      out.writeInt(sessionId);
      if (in.readableBytes() == 0)
      {
        writeString(out, listener.getMotd());
        writeString(out, "SMP");
        writeString(out, "BungeeCord_Proxy");
        writeNumber(out, 1000+r.nextInt(400));
        writeNumber(out, listener.getMaxPlayers());
        writeShort(out, listener.getHost().getPort());
        writeString(out, listener.getHost().getHostString());
      }
      else if (in.readableBytes() == 4)
      {
        out.writeBytes(new byte[] { 115, 112, 108, 105, 116, 110, 117, 109, 0, -128, 0 });
        


        Map<String, String> data = new LinkedHashMap();
        
        data.put("hostname", listener.getMotd());
        data.put("gametype", "SMP");
        
        data.put("game_id", "MINECRAFT");
        data.put("version", bungee.getGameVersion());
        data.put("plugins", "");
        
        data.put("map", "BungeeCord_Proxy");
        data.put("numplayers", Integer.toString(1000+r.nextInt(400)));
        data.put("maxplayers", Integer.toString(listener.getMaxPlayers()));
        data.put("hostport", Integer.toString(listener.getHost().getPort()));
        data.put("hostip", listener.getHost().getHostString());
        for (Map.Entry<String, String> entry : data.entrySet())
        {
          writeString(out, (String)entry.getKey());
          writeString(out, (String)entry.getValue());
        }
        out.writeByte(0);
        

        writeString(out, "");
        for (ProxiedPlayer p : bungee.getPlayers()) {
          writeString(out, p.getName());
        }
        out.writeByte(0);
      }
      else
      {
        throw new IllegalStateException("Invalid data request packet");
      }
    }
    ctx.writeAndFlush(response);
  }
}
