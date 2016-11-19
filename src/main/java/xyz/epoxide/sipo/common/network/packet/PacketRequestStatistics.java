package xyz.epoxide.sipo.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import xyz.epoxide.sipo.SIPO;

public class PacketRequestStatistics implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class PacketHandler implements IMessageHandler<PacketRequestStatistics, IMessage> {

        @Override
        public IMessage onMessage(PacketRequestStatistics packet, MessageContext ctx) {

            if (ctx.side == Side.SERVER) {
                EntityPlayerMP player = ctx.getServerHandler().playerEntity;
                SIPO.network.sendTo(new PacketSendStatistics(), player);
            }
            return null;
        }
    }
}
