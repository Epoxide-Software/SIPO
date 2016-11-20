package xyz.epoxide.sipo.profiler.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import xyz.epoxide.sipo.profiler.type.ProfilerBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfilerWorld extends ProfilerBase {
    public long[] tickTimeArray = new long[100];

    public List<ChunkData> chunkDataList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void setupData(MinecraftServer server) {
        this.tickTimeArray = server.tickTimeArray;

        this.chunkDataList.clear();

        for (Integer dimId : DimensionManager.getIDs()) {
            WorldServer worldServer = server.worldServerForDimension(dimId);
            for (Chunk chunk : worldServer.getChunkProvider().getLoadedChunks()) {
                ChunkPos chunkPos = new ChunkPos(chunk.xPosition, chunk.zPosition);
                this.chunkDataList.add(new ChunkData(chunk.xPosition, chunk.zPosition, dimId, worldServer.getPersistentChunks().containsKey(chunkPos)));
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (long l : this.tickTimeArray)
            buf.writeLong(l);

        buf.writeInt(this.chunkDataList.size());
        for (ChunkData chunkData : this.chunkDataList) {
            buf.writeInt(chunkData.xPos);
            buf.writeInt(chunkData.zPos);
            buf.writeInt(chunkData.dimID);
            buf.writeBoolean(chunkData.persistent);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.chunkDataList.clear();

        for (int i = 0; i < tickTimeArray.length; i++)
            this.tickTimeArray[i] = (long) (buf.readLong() * 1.0E-6F);

        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            this.chunkDataList.add(new ChunkData(buf.readInt(), buf.readInt(), buf.readInt(), buf.readBoolean()));
        }
    }


    public static class ChunkData {
        public final int xPos;
        public final int zPos;
        public final int dimID;
        public final boolean persistent;

        public ChunkData(int xPos, int zPos, Integer dimId, boolean persistent) {
            this.xPos = xPos;
            this.zPos = zPos;
            this.dimID = dimId;
            this.persistent = persistent;
        }
    }
}
