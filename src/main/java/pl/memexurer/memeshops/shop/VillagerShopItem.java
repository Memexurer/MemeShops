package pl.memexurer.memeshops.shop;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VillagerShopItem {
    private int slot;
    private int price;
    private String name;
    private List<String> description;
    private ItemStack item;

    VillagerShopItem(ConfigurationSection section) {
        this.slot = Integer.parseInt(section.getName());
        this.price = section.getInt("price");
        this.item = section.getItemStack("item");
        this.description = section.getStringList("description");
        this.name = section.getString("name");
    }

    public VillagerShopItem(int slot) {
        this.slot = slot;
    }

    public List<String> getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getSlot() {
        return slot;
    }

    public int getPrice() {
        return price;
    }

    public ItemStack getItem() {
        return item;
    }

    void save(ConfigurationSection configurationSection) {
        configurationSection.set("price", price);
        configurationSection.set("description", description);
        configurationSection.set("name", name);
        configurationSection.set("item", item);
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setDescription(List<String> itemDescription) {
        this.description = itemDescription;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setPrice(int parseInt) {
        this.price = parseInt;
    }
}
