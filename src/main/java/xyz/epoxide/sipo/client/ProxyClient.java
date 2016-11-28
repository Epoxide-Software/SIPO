package xyz.epoxide.sipo.client;

import xyz.epoxide.sipo.client.gui.GuiStatistics;
import xyz.epoxide.sipo.client.gui.tabs.GuiTabNetwork;
import xyz.epoxide.sipo.client.gui.tabs.GuiTabOverall;
import xyz.epoxide.sipo.client.gui.tabs.GuiTabTileEntity;
import xyz.epoxide.sipo.common.ProxyCommon;

public class ProxyClient extends ProxyCommon {

    @Override
    public void initTabs() {
        GuiStatistics.registerTab(new GuiTabOverall());
        GuiStatistics.registerTab(new GuiTabTileEntity());
        GuiStatistics.registerTab(new GuiTabNetwork());
    }
}
