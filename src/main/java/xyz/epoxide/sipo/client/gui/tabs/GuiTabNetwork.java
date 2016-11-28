package xyz.epoxide.sipo.client.gui.tabs;

import net.minecraftforge.fml.relauncher.Side;
import xyz.epoxide.sipo.client.gui.GuiStatistics;
import xyz.epoxide.sipo.profiler.ProfilerManager;
import xyz.epoxide.sipo.profiler.world.ProfilerNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiTabNetwork extends GuiTab {
    @Override
    public void render(GuiStatistics parent) {
        ProfilerNetwork profilerNetwork = ProfilerManager.PROFILER_NETWORK;

        List<ProfilerNetwork.PacketData> dataList = new ArrayList<>(profilerNetwork.packetDataList);

        Map<String, Map<Side, Integer>> orderedList = new HashMap<>();

        for (ProfilerNetwork.PacketData data : dataList) {
            if (!orderedList.containsKey(data.channel)) {
                orderedList.put(data.channel, new HashMap<>());
            }

            Map<Side, Integer> dataChannel = orderedList.get(data.channel);
            if (!dataChannel.containsKey(data.target)) {
                dataChannel.put(data.target, data.length);
            } else {
                dataChannel.put(data.target, dataChannel.get(data.target) + data.length);
            }

        }
        int i = 0;
        for (String channel : orderedList.keySet()) {
            for (Side target : orderedList.get(channel).keySet()) {
                i++;
                parent.getFontRenderer().drawString(channel + "| " + target + "| " + orderedList.get(channel).get(target), 5, i * 20, 0xFFFFFF);
            }
        }
    }

    @Override
    public String getUnlocalizedName() {
        return "sipo.gui.network";
    }

    @Override
    public int getID() {
        return 2;
    }
}
