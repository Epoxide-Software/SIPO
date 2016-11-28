package xyz.epoxide.sipo.client.gui.tabs;

import xyz.epoxide.sipo.client.gui.GuiStatistics;

public abstract class GuiTab {

    public abstract void render(GuiStatistics parent);

    public abstract String getUnlocalizedName();

    public abstract int getID();
}
