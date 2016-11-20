package xyz.epoxide.sipo.profiler.type;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ProfilerBase {

    protected int ID;

    @SideOnly(Side.SERVER)
    public abstract void setupData(MinecraftServer server);

    @SideOnly(Side.SERVER)
    public abstract void toBytes(ByteBuf buf);

    public abstract void fromBytes(ByteBuf buf);

    public final int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

}
