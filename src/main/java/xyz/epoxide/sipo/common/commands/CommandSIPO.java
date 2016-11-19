package xyz.epoxide.sipo.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import xyz.epoxide.sipo.SIPO;
import xyz.epoxide.sipo.common.network.packet.PacketOpenGui;

public class CommandSIPO extends CommandBase {

    @Override
    public String getCommandName() {
        return "sipo";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            SIPO.network.sendTo(new PacketOpenGui(), (EntityPlayerMP) sender);
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return (sender instanceof EntityPlayer && ((EntityPlayer) sender).getUniqueID().toString().equals("f0f76db6-0461-4151-8ba7-392d65d62ea3")) || super.checkPermission(server, sender);
    }
}
