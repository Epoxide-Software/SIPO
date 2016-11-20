package xyz.epoxide.sipo.profiler.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xyz.epoxide.sipo.profiler.type.ProfilerTimer;

import java.util.*;

public class ProfilerEntity extends ProfilerTimer<Entity> {

    private Map<Entity, Long> keyTimeMap = new WeakHashMap<>();

    private Map<Entity, EntityData> lookupEntityData = Collections.synchronizedMap(new HashMap<>());

    public List<EntityData> entityDataList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void initial(Entity key) {
        this.keyStartTimeMap.put(key, System.currentTimeMillis());
    }

    @Override
    public void end(Entity key) {
        this.keyTimeMap.put(key, System.currentTimeMillis() - this.keyStartTimeMap.get(key));
        this.keyStartTimeMap.clear();
    }

    @Override
    public void setupData(MinecraftServer server) {
        for (Integer dimId : DimensionManager.getIDs())
            for (Entity entity : server.worldServerForDimension(dimId).loadedEntityList)
                this.lookupEntityData.put(entity, new EntityData(entity.getUniqueID(), entity.getPosition(), this.keyTimeMap.get(entity), dimId));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.lookupEntityData.size());
        for (EntityData entityData : this.lookupEntityData.values()) {
            buf.writeLong(entityData.uuid.getMostSignificantBits());
            buf.writeLong(entityData.uuid.getLeastSignificantBits());

            buf.writeInt(entityData.pos.getX());
            buf.writeInt(entityData.pos.getY());
            buf.writeInt(entityData.pos.getZ());

            buf.writeLong(entityData.time);

            buf.writeInt(entityData.dimID);
        }

        this.keyTimeMap.clear();
        this.lookupEntityData.clear();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityDataList.clear();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            this.entityDataList.add(new EntityData(new UUID(buf.readLong(), buf.readLong()), new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()), buf.readLong(), buf.readInt()));
        }
    }

    public static class EntityData {
        public final UUID uuid;
        public final BlockPos pos;
        public final long time;
        public final int dimID;

        public EntityData(UUID uuid, BlockPos pos, Long time, Integer dimID) {
            this.uuid = uuid;
            this.pos = pos;
            this.time = time != null ? time : 0;
            this.dimID = dimID;
        }
    }
}
