package xyz.epoxide.sipo.client.gui.tabs;

import xyz.epoxide.sipo.client.gui.GuiStatistics;

public abstract class GuiTab {

    private int ID;

    public abstract void render(GuiStatistics parent);

    public abstract String getUnlocalisedName();

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
