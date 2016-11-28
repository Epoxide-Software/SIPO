package xyz.epoxide.sipo.client.gui.tabs;

import xyz.epoxide.sipo.client.gui.GuiStatistics;
import xyz.epoxide.sipo.profiler.ProfilerManager;
import xyz.epoxide.sipo.profiler.world.ProfilerTileEntity;

import java.util.ArrayList;
import java.util.Comparator;

public class GuiTabTileEntity extends GuiTab {
    @Override
    public void render(GuiStatistics parent) {
        ProfilerTileEntity profilerTileEntity = ProfilerManager.PROFILER_TILE_ENTITY;

        ArrayList<ProfilerTileEntity.TileEntityData> dataList = new ArrayList<>(profilerTileEntity.tileEntityDataList);

        dataList.sort(Comparator.comparingLong(o -> o.time));
        int i = 0;
        for (ProfilerTileEntity.TileEntityData data : dataList) {
            i++;
            parent.getFontRenderer().drawString(data.name + "| " + data.time + "| " + data.pos.getX() + "," + data.pos.getY() + "," + data.pos.getZ(), 5, i * 20, 0xFFFFFF);
        }
    }

    @Override
    public String getUnlocalizedName() {
        return "sipo.gui.tileentity";
    }

    @Override
    public int getID() {
        return 1;
    }
}
