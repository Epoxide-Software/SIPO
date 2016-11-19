package xyz.epoxide.sipo.client;

import xyz.epoxide.sipo.client.gui.GuiStatistics;
import xyz.epoxide.sipo.client.gui.tabs.GuiOverall;
import xyz.epoxide.sipo.common.ProxyCommon;

public class ProxyClient extends ProxyCommon {

    @Override
    public void initTabs() {
        GuiStatistics.registerTab(new GuiOverall());
    }
}
