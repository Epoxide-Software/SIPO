package xyz.epoxide.sipo.profiler.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xyz.epoxide.sipo.profiler.type.ProfilerTimer;

import java.util.*;

public class ProfilerTileEntity extends ProfilerTimer<TileEntity> {

    private Map<TileEntity, Long> keyTimeMap = new WeakHashMap<>();

    private Map<TileEntity, TileEntityData> lookupTileEntityData = new HashMap<>();

    public List<TileEntityData> tileEntityDataList = new ArrayList<>();

    @Override
    public void initial(TileEntity key) {
        this.keyStartTimeMap.put(key, System.currentTimeMillis());
    }

    @Override
    public void end(TileEntity key) {
        this.keyTimeMap.put(key, System.currentTimeMillis() - this.keyStartTimeMap.get(key));
        this.keyStartTimeMap.clear();
    }

    @Override
    public void setupData(MinecraftServer server) {
        for (Integer dimId : DimensionManager.getIDs())
            for (TileEntity tileEntity : server.worldServerForDimension(dimId).tickableTileEntities)
                this.lookupTileEntityData.put(tileEntity, new TileEntityData(tileEntity.getClass().getName(), tileEntity.getPos(), this.keyTimeMap.get(tileEntity), dimId));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.lookupTileEntityData.size());
        for (TileEntityData tileEntityData : this.lookupTileEntityData.values()) {
            ByteBufUtils.writeUTF8String(buf, tileEntityData.name);

            buf.writeInt(tileEntityData.pos.getX());
            buf.writeInt(tileEntityData.pos.getY());
            buf.writeInt(tileEntityData.pos.getZ());

            buf.writeLong(tileEntityData.time);

            buf.writeInt(tileEntityData.dimID);
        }

        this.keyTimeMap.clear();
        this.lookupTileEntityData.clear();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.tileEntityDataList.clear();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            this.tileEntityDataList.add(new TileEntityData(ByteBufUtils.readUTF8String(buf), new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()), buf.readLong(), buf.readInt()));
        }
    }

    public static class TileEntityData {
        private final String name;
        public final BlockPos pos;
        public final long time;
        public final int dimID;

        public TileEntityData(String name, BlockPos pos, Long time, Integer dimID) {
            this.name = name;
            this.pos = pos;
            this.time = time != null ? time : 0;
            this.dimID = dimID;
        }
    }
}
