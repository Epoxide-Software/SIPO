package xyz.epoxide.sipo;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import xyz.epoxide.sipo.common.ProxyCommon;
import xyz.epoxide.sipo.common.commands.CommandSIPO;
import xyz.epoxide.sipo.common.network.GuiHandler;
import xyz.epoxide.sipo.common.network.packet.PacketOpenGui;
import xyz.epoxide.sipo.common.network.packet.PacketRequestStatistics;
import xyz.epoxide.sipo.common.network.packet.PacketSendStatistics;
import xyz.epoxide.sipo.libs.Constants;

@Mod(modid = Constants.MODID, name = Constants.MOD_NAME, version = Constants.VERSION, acceptableRemoteVersions = "*")
public class SIPO {

    @SidedProxy(serverSide = Constants.SERVER, clientSide = Constants.CLIENT)
    public static ProxyCommon proxy;

    @Instance(Constants.MODID)
    public static SIPO INSTANCE;

    public static SimpleNetworkWrapper network;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);
        network.registerMessage(PacketOpenGui.PacketHandler.class, PacketOpenGui.class, 0, Side.CLIENT);
        network.registerMessage(PacketRequestStatistics.PacketHandler.class, PacketRequestStatistics.class, 1, Side.SERVER);
        network.registerMessage(PacketSendStatistics.PacketHandler.class, PacketSendStatistics.class, 2, Side.CLIENT);

        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.initTabs();
    }


    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSIPO());
    }
}