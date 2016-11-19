package xyz.epoxide.sipo.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.server.FMLServerHandler;
import xyz.epoxide.sipo.profiler.ProfilerManager;
import xyz.epoxide.sipo.profiler.type.ProfilerBase;

public class PacketSendStatistics implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            int id = buf.readInt();
            for (ProfilerBase profiler : ProfilerManager.getProfilerList()) {
                if (id == profiler.getID()) {
                    profiler.fromBytes(buf);
                    break;
                }
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        MinecraftServer server = FMLServerHandler.instance().getServer();

        buf.writeInt(ProfilerManager.getProfilerList().size());
        for (ProfilerBase profiler : ProfilerManager.getProfilerList()) {
            buf.writeInt(profiler.getID());
            profiler.setupData(server);
            profiler.toBytes(buf);
        }
    }

    public static class PacketHandler implements IMessageHandler<PacketSendStatistics, IMessage> {

        @Override
        public IMessage onMessage(PacketSendStatistics packet, MessageContext ctx) {


            return null;
        }
    }
}
