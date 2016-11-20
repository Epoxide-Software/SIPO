package xyz.epoxide.sipo.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xyz.epoxide.sipo.SIPO;
import xyz.epoxide.sipo.client.gui.tabs.GuiTab;
import xyz.epoxide.sipo.common.network.packet.PacketRequestStatistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiStatistics extends GuiScreen {

    private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("sipo", "textures/gui/stats_elements.png");
    private static List<GuiTab> tabList = new ArrayList<>();

    private final EntityPlayer player;
    private final World world;

    private int selectedTabID = 0;

    protected int xSize = 176;
    protected int ySize = 166;
    private int guiLeft;
    private int guiTop;
    private int tick;

    public GuiStatistics(EntityPlayer player, World world) {
        this.player = player;
        this.world = world;
        SIPO.network.sendToServer(new PacketRequestStatistics());
    }

    @Override
    public void initGui() {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.tick++;
        if (this.tick >= 20) {
            SIPO.network.sendToServer(new PacketRequestStatistics());
            this.tick -= 20;
        }

        this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);

        this.drawGradientRect(0, 0, this.width, this.height, -1073741824, -1073741824);

        this.drawGradientRect(0, 13, this.width, this.height, -1073741824, -1073741824);

        for (int i = 0; i < tabList.size(); i++) {
            GuiTab tab = tabList.get(i);
            if (tab.getID() == selectedTabID)
                tab.render(this);

            String tabName = I18n.format(tab.getUnlocalisedName());
            this.fontRendererObj.drawString(tabName, i * (this.width / tabList.size() - this.fontRendererObj.getStringWidth(tabName) / 2) + 5, 3, 0xFFFFFF);
        }
    }

    public static void registerTab(GuiTab tab) {
        tab.setID(tabList.size());
        tabList.add(tab);
    }

    public void drawTab(GuiTab tab) {

    }

    public FontRenderer getFontRenderer() {
        return this.fontRendererObj;
    }
}
