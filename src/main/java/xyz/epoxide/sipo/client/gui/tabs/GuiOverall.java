package xyz.epoxide.sipo.client.gui.tabs;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;
import xyz.epoxide.sipo.client.gui.GuiStatistics;
import xyz.epoxide.sipo.profiler.ProfilerManager;
import xyz.epoxide.sipo.profiler.world.ProfilerEntity;
import xyz.epoxide.sipo.profiler.world.ProfilerNetwork;
import xyz.epoxide.sipo.profiler.world.ProfilerTileEntity;
import xyz.epoxide.sipo.profiler.world.ProfilerWorld;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LongSummaryStatistics;

import static com.google.common.math.DoubleMath.mean;

public class GuiOverall extends GuiTab {
    private static final DecimalFormat tFormatter = new DecimalFormat("########0.000");

    @Override
    public void render(GuiStatistics parent) {
        ProfilerWorld world = ProfilerManager.PROFILER_WORLD;
        ProfilerTileEntity te = ProfilerManager.PROFILER_TILE_ENTITY;
        ProfilerEntity entity = ProfilerManager.PROFILER_ENTITY;
        ProfilerNetwork network = ProfilerManager.PROFILER_NETWORK;

        double meanServerTickTime = mean(world.tickTimeArray);
        double meanServerTPS = Math.min(1000.0 / meanServerTickTime, 20);

        String tps = I18n.format("Overall TPS: %s (%sμ)", tFormatter.format(meanServerTPS), tFormatter.format(meanServerTickTime));
        parent.getFontRenderer().drawString(tps, 5, 20, 0xFFFFFFFF);

        parent.getFontRenderer().drawString("Chunk Count: " + world.chunkDataList.stream().filter(chunkData -> !chunkData.persistent).count(), 200, 50, 0xFFFFFFFF);
        parent.getFontRenderer().drawString("Forced Chunk Count: " + world.chunkDataList.stream().filter(chunkData -> chunkData.persistent).count(), 200, 60, 0xFFFFFFFF);


        double totalTime = 0;
        for (ProfilerTileEntity.TileEntityData data : te.tileEntityDataList) {
            totalTime += data.time;
        }

        double meanTETickTime = te.tileEntityDataList.size() > 0 ? mean(totalTime / te.tileEntityDataList.size()) : 0;
        String teCount = I18n.format("Tile Entity: %s count (%sμ)", te.tileEntityDataList.size(), tFormatter.format(meanTETickTime));
        parent.getFontRenderer().drawString(teCount, 5, 50, 0xFFFFFFFF);

        double meanEntityTickTime = entity.entityDataList.size() > 0 ? mean(totalTime / entity.entityDataList.size()) : 0;
        String entityCount = I18n.format("Entity: %s count (%sμ)", entity.entityDataList.size(), tFormatter.format(meanEntityTickTime));
        parent.getFontRenderer().drawString(entityCount, 5, 60, 0xFFFFFFFF);


        parent.getFontRenderer().drawString("Network Packets: " + network.packetDataList.size(), 5, 70, 0xFFFFFFFF);

//        drawGraph(overall.tickTimeArray.clone(), 200, 100, 200, 100);
    }

    public void drawGraph(long[] data, int x, int y, int width, int height) {

        LongSummaryStatistics stat = Arrays.stream(data).summaryStatistics();
        long min = stat.getMin();
        long max = stat.getMax();

        float wStep = ((float) width / (float) data.length);
        float diff = max - min;
        float hStep = 0;
        if (diff != 0) {
            hStep = (float) height / diff;
        }
        Gui.drawRect(x, y, x + width, y + height, 0xFFFFFFFF);
        for (int i = 1; i < data.length; i++) {
            drawLine(x + (i - 1) * wStep, y - 15 + height - data[i - 1] / hStep, x + i * wStep, y - 15 + height - data[i] / hStep);
        }
    }

    private void drawLine(float p1X, float p1Y, float p2X, float p2Y) {
        GL11.glPushMatrix();
        GL11.glTranslatef(p1X, p1Y, 0);
        if ((p2Y - p1Y) != 0) {
            GL11.glRotated(Math.toDegrees(Math.atan2((p2Y - p1Y), (p2X - p1X))), 0, 0, 1);
        }

        float hyp = (float) Math.sqrt(Math.pow((p2Y - p1Y), 2) + Math.pow(p2X - p1X, 2));

        Gui.drawRect(0, 1, (int) Math.ceil(hyp), -1, -1073741824);
        GL11.glPopMatrix();
    }

    @Override
    public String getUnlocalisedName() {
        return "sipo.gui.overall";
    }
}
