package xyz.epoxide.sipo.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import xyz.epoxide.sipo.SIPO;
import xyz.epoxide.sipo.libs.Constants;

public class PacketRequestStatistics implements IMessage {
    private boolean outdated;

    @Override
    public void fromBytes(ByteBuf buf) {
        if (buf.readInt() < Constants.PACKET_VERSION) {
            this.outdated = true;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(Constants.PACKET_VERSION);
    }

    public static class PacketHandler implements IMessageHandler<PacketRequestStatistics, IMessage> {

        @Override
        public IMessage onMessage(PacketRequestStatistics packet, MessageContext ctx) {

            if (ctx.side == Side.SERVER) {
                if (packet.outdated) {
                    Minecraft.getMinecraft().player.addChatMessage(new TextComponentString("The client version of SIPO is outdated, please update!"));
                } else {
                    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
                    SIPO.network.sendTo(new PacketSendStatistics(), player);
                }
            }
            return null;
        }
    }
}
