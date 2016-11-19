package xyz.epoxide.sipo.profiler.type;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class ProfilerTimer<T> extends ProfilerBase {
    protected Map<T, Long> keyStartTimeMap = new WeakHashMap<>();

    public abstract void initial(T key);

    public abstract void end(T key);

}
