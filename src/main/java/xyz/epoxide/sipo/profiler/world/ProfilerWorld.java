package xyz.epoxide.sipo.profiler.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import xyz.epoxide.sipo.profiler.type.ProfilerBase;

public class ProfilerWorld extends ProfilerBase {
    public long[] tickTimeArray = new long[100];
    public int chunkCount = 0;
    public int forcedChunkCount;

    @Override
    public void setupData(MinecraftServer server) {
        this.tickTimeArray = server.tickTimeArray;
        for (Integer dimId : DimensionManager.getIDs()) {
            WorldServer worldServer = server.worldServerForDimension(dimId);
            this.chunkCount += worldServer.getChunkProvider().getLoadedChunkCount();
            this.forcedChunkCount += worldServer.getPersistentChunks().size();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (long l : this.tickTimeArray)
            buf.writeLong(l);

        buf.writeInt(this.chunkCount);
        buf.writeInt(this.forcedChunkCount);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        for (int i = 0; i < tickTimeArray.length; i++)
            this.tickTimeArray[i] = (long) (buf.readLong() * 1.0E-6F);

        this.chunkCount = buf.readInt();
        this.forcedChunkCount = buf.readInt();
    }
}
