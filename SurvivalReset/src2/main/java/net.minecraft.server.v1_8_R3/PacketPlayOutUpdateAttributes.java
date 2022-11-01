package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PacketPlayOutUpdateAttributes
        implements Packet<PacketListenerPlayOut>
{
    private int a;
    private final List<AttributeSnapshot> b = Lists.newArrayList();

    public PacketPlayOutUpdateAttributes() {}

    public PacketPlayOutUpdateAttributes(int paramInt, Collection<AttributeInstance> paramCollection)
    {
        this.a = paramInt;
        for (AttributeInstance attributeinstance : paramCollection) {
            this.b.add(new PacketPlayOutUpdateAttributes.AttributeSnapshot(attributeinstance.getAttribute().getName(), attributeinstance.b(), attributeinstance.c()));
        }
    }

    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.e();
        int i = packetdataserializer.readInt();

        for (int j = 0; j < i; ++j) {
            String s = packetdataserializer.c(64);
            double d0 = packetdataserializer.readDouble();
            ArrayList arraylist = Lists.newArrayList();
            int k = packetdataserializer.e();

            for (int l = 0; l < k; ++l) {
                UUID uuid = packetdataserializer.g();

                arraylist.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", packetdataserializer.readDouble(), packetdataserializer.readByte()));
            }

            this.b.add(new PacketPlayOutUpdateAttributes.AttributeSnapshot(s, d0, arraylist));
        }

    }

    public void b(PacketDataSerializer paramPacketDataSerializer)
            throws IOException
    {
        paramPacketDataSerializer.b(this.a);
        paramPacketDataSerializer.writeInt(this.b.size());
        for (AttributeSnapshot localAttributeSnapshot : this.b)
        {
            paramPacketDataSerializer.a(localAttributeSnapshot.a());
            paramPacketDataSerializer.writeDouble(localAttributeSnapshot.b());
            paramPacketDataSerializer.b(localAttributeSnapshot.c().size());
            for (AttributeModifier localAttributeModifier : localAttributeSnapshot.c())
            {
                paramPacketDataSerializer.a(localAttributeModifier.a());
                paramPacketDataSerializer.writeDouble(localAttributeModifier.d());
                paramPacketDataSerializer.writeByte(localAttributeModifier.c());
            }
        }
    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

    public class AttributeSnapshot {

        private final String b;
        private final double c;
        private final Collection<AttributeModifier> d;

        public AttributeSnapshot(String s, double d0, Collection collection) {
            this.b = s;
            this.c = d0;
            this.d = collection;
        }

        public String a() {
            return this.b;
        }

        public double b() {
            return this.c;
        }

        public Collection<AttributeModifier> c() {
            return this.d;
        }
    }
}
