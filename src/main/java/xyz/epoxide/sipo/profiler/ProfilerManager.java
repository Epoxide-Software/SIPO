package xyz.epoxide.sipo.profiler;

import xyz.epoxide.sipo.profiler.type.ProfilerBase;
import xyz.epoxide.sipo.profiler.world.ProfilerEntity;
import xyz.epoxide.sipo.profiler.world.ProfilerNetwork;
import xyz.epoxide.sipo.profiler.world.ProfilerTileEntity;
import xyz.epoxide.sipo.profiler.world.ProfilerWorld;

import java.util.ArrayList;
import java.util.List;

public class ProfilerManager {
    public static final ProfilerWorld PROFILER_WORLD = (ProfilerWorld) register(new ProfilerWorld());
    public static final ProfilerTileEntity PROFILER_TILE_ENTITY = (ProfilerTileEntity) register(new ProfilerTileEntity());
    public static final ProfilerEntity PROFILER_ENTITY = (ProfilerEntity) register(new ProfilerEntity());
    public static final ProfilerNetwork PROFILER_NETWORK = (ProfilerNetwork) register(new ProfilerNetwork());

    private static List<ProfilerBase> profilerList;

    public static ProfilerBase register(ProfilerBase profiler) {
        if (profilerList == null)
            profilerList = new ArrayList<>();

        profiler.setID(profilerList.size());
        profilerList.add(profiler);
        return profiler;
    }

    public static List<ProfilerBase> getProfilerList() {
        return profilerList;
    }
}
