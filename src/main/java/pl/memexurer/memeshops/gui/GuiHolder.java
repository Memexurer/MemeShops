package pl.memexurer.memeshops.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GuiHolder implements InventoryHolder {
    private GuiBuilder builder;

    public GuiHolder(GuiBuilder builder) {
        this.builder = builder;
    }

    public GuiBuilder getBuilder() {
        return builder;
    }

    @Override
    public Inventory getInventory() {
        return builder.build();
    }
}
