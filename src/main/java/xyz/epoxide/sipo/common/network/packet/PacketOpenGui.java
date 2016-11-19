package xyz.epoxide.sipo.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import xyz.epoxide.sipo.SIPO;

public class PacketOpenGui implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class PacketHandler implements IMessageHandler<PacketOpenGui, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenGui packet, MessageContext ctx) {

            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().player.openGui(SIPO.INSTANCE, 0, Minecraft.getMinecraft().player.getEntityWorld(), 0, 0, 0);
            }
            return null;
        }
    }
}
