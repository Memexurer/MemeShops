package pl.memexurer.memeshops.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.memexurer.memeshops.utils.ChatUtil;

import java.util.Arrays;
import java.util.List;

public class GuiItem {
    private ItemStack itemStack;
    private GuiItemExecutor executor;

    public GuiItem(GuiItemExecutor executor, ItemStack item) {
        this.itemStack = item;
        this.executor = executor;
    }

    public GuiItem(GuiItemExecutor executor, Material type, String name, String... lore) {
        this.executor = executor;
        this.itemStack = new ItemStack(type, 1);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatUtil.fixColor(name));
        meta.setLore(ChatUtil.fixColor(Arrays.asList(lore)));
        itemStack.setItemMeta(meta);
    }


    public GuiItem(GuiItemExecutor executor, Material type, String name, short mets, List<String> lore) {
        this.executor = executor;
        this.itemStack = new ItemStack(type, 1, mets);

        ItemMeta meta = itemStack.getItemMeta();
        if (name != null)
            meta.setDisplayName(ChatUtil.fixColor(name));
        if (lore != null)
            meta.setLore(ChatUtil.fixColor(lore));

        itemStack.setItemMeta(meta);
    }

    public GuiItem(GuiItemExecutor executor, String name, String skullOwner, String... lore) {
        this.executor = executor;
        this.itemStack = new ItemStack(Material.SKULL_ITEM, 1);

        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setDisplayName(ChatUtil.fixColor(name));
        if (lore.length > 0)
            meta.setLore(ChatUtil.fixColor(Arrays.asList(lore)));
        meta.setOwner(skullOwner);
        itemStack.setItemMeta(meta);
    }

    public GuiItem(GuiItemExecutor executor, Material type, int count, String name, String... lore) {
        this.executor = executor;
        this.itemStack = new ItemStack(type, count);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatUtil.fixColor(name));
        meta.setLore(ChatUtil.fixColor(Arrays.asList(lore)));
        itemStack.setItemMeta(meta);
    }

    public GuiItem(GuiItemExecutor executor, Material type, int count, short data, String name, String... lore) {
        this.executor = executor;
        this.itemStack = new ItemStack(type, count, data);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatUtil.fixColor(name));
        meta.setLore(ChatUtil.fixColor(Arrays.asList(lore)));
        itemStack.setItemMeta(meta);
    }

    public GuiItemExecutor getExecutor() {
        return executor;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
