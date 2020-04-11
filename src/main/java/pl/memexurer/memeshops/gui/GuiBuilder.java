package pl.memexurer.memeshops.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.EventException;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import pl.memexurer.memeshops.utils.ChatUtil;

import java.util.HashMap;
import java.util.Map;

public class GuiBuilder {
    private String inventoryTitle;
    private InventoryType inventoryType;
    private int inventorySize;
    private HashMap<Integer, GuiItem> inventoryItems = new HashMap<>();

    public GuiBuilder(String inventoryTitle, int rows) {
        this.inventorySize = rows * 9;
        this.inventoryTitle = ChatUtil.fixColor(inventoryTitle);
    }

    public GuiBuilder(InventoryType type) {
        this.inventoryType = type;
    }

    public Inventory build() {
        Inventory inventory;

        if (inventoryType != null) inventory = Bukkit.createInventory(new GuiHolder(this), inventoryType);
        else inventory = Bukkit.createInventory(new GuiHolder(this), inventorySize, inventoryTitle);

        for (Map.Entry<Integer, GuiItem> item : inventoryItems.entrySet()) {
            inventory.setItem(item.getKey(), item.getValue().getItemStack());
        }

        return inventory;
    }

    public void setItem(int rawLocation, GuiItem item) {
        inventoryItems.put(rawLocation, item);
    }

    public void setItem(int y, int x, GuiItem item) {
        inventoryItems.put(y * 9 + x, item);
    }

    public void handleClick(InventoryClickEvent e) {
        if (!inventoryItems.containsKey(e.getRawSlot())) return;
        if (inventoryItems.get(e.getRawSlot()).getExecutor() == null) return;

        try {
            inventoryItems.get(e.getRawSlot()).getExecutor().execute(e);
        } catch (Exception ex) {
            new EventException(ex).printStackTrace();
        }
    }
}
