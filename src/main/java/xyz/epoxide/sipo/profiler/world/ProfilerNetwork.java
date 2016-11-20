package xyz.epoxide.sipo.profiler.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import xyz.epoxide.sipo.profiler.type.ProfilerBase;

import java.util.ArrayList;
import java.util.List;

public class ProfilerNetwork extends ProfilerBase {
    public List<PacketData> packetDataList = new ArrayList<>();

    @Override
    public void setupData(MinecraftServer server) {

    }

    public void addPacket(FMLProxyPacket msg) {
        this.packetDataList.add(new PacketData(msg.channel(), msg.getTarget(), msg.payload().copy().array().length));
    }


    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.packetDataList.size());

        for (PacketData data : this.packetDataList) {
            ByteBufUtils.writeUTF8String(buf, data.channel);
            buf.writeBoolean(data.target == Side.CLIENT);
            buf.writeInt(data.length);
        }
        this.packetDataList.clear();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.packetDataList.clear();

        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            this.packetDataList.add(new PacketData(ByteBufUtils.readUTF8String(buf), buf.readBoolean() ? Side.CLIENT : Side.SERVER, buf.readInt()));
        }
    }


    public class PacketData {

        private final String channel;
        private final Side target;
        private final int length;

        public PacketData(String channel, Side target, int length) {
            this.channel = channel;
            this.target = target;
            this.length = length;
        }
    }
}
