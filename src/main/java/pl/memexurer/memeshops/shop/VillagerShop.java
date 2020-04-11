package pl.memexurer.memeshops.shop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import pl.memexurer.memeshops.MemeShopsPlugin;
import pl.memexurer.memeshops.utils.ChatUtil;
import pl.memexurer.memeshops.utils.EntityUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class VillagerShop {
    private int shopId;
    private String inventoryName;
    private Location entityLocation;
    private UUID entityUniqueId;
    private Villager villagerEntity;
    private Material shopCurrency;
    private HashMap<Integer, VillagerShopItem> items;

    VillagerShop(ConfigurationSection section) {
        this.shopId = Integer.parseInt(section.getName());
        this.inventoryName = section.getString("inventoryName");
        this.entityLocation = (Location) section.get("entity.location");
        this.entityUniqueId = UUID.fromString(section.getString("entity.uuid"));
        this.villagerEntity = (Villager) findVillagerEntity().orElse(null);
        this.shopCurrency = Material.getMaterial(section.getString("currency"));
        this.items = new HashMap<>();

        ConfigurationSection itemSection = section.getConfigurationSection("items");
        if(itemSection == null) return;
        for(String key: itemSection.getKeys(false)) {
            VillagerShopItem item = new VillagerShopItem(itemSection.getConfigurationSection(key));
            items.put(item.getSlot(), item);
        }
    }

    VillagerShop(int shopId, Location location) {
        this.shopId = shopId;
        this.inventoryName = "Skleps";
        this.shopCurrency = Material.EMERALD;
        this.entityLocation = location;
        spawnEntity(location);
        this.items = new HashMap<>();
    }

    void saveDefault(ConfigurationSection section) {
        section.set("inventoryName", "Skleps");
        section.set("entity.location", entityLocation);
        section.set("currency", "EMERALD");
        section.set("entity.uuid", entityUniqueId.toString());
    }

    private void spawnEntity(Location location) {
        Villager entityVillager = (Villager) findVillagerEntity().orElse(entityLocation.getWorld().spawnEntity(entityLocation, EntityType.VILLAGER));

        entityVillager.setCustomName(ChatUtil.fixColor("Nowy sklepik"));
        entityVillager.setCustomNameVisible(true);
        entityVillager.setAgeLock(true);

        EntityUtils.disableEntityAI(entityVillager);

        this.entityLocation = location;
        this.entityUniqueId = entityVillager.getUniqueId();
        this.villagerEntity = entityVillager;
    }

    public Optional<Entity> findVillagerEntity() {
        for(Entity entity: entityLocation.getWorld().getNearbyEntities(entityLocation, 1, 1, 1)) {
            if(entity.getUniqueId().equals(entityUniqueId)) return Optional.of(entity);
        }
        return Optional.empty();
    }

    public int getShopId() {
        return shopId;
    }

    public HashMap<Integer, VillagerShopItem> getItems() {
        return items;
    }

    public UUID getEntityUniqueId() {
        return entityUniqueId;
    }

    public String getInventoryTitle() {
        return inventoryName;
    }

    public Villager getVillagerEntity() {
        return villagerEntity;
    }

    public Material getShopCurrency() {
        return shopCurrency;
    }

    public void saveItem(VillagerShopItem shopItem) {
        items.put(shopItem.getSlot(), shopItem);
        MemeShopsPlugin.getPluginInstance().getShopData().saveItem(this, shopItem);
    }

    public void removeItem(VillagerShopItem shopItem) {
        items.remove(shopItem.getSlot());
        MemeShopsPlugin.getPluginInstance().getShopData().deleteItem(this, shopItem);
    }

    public void setName(String str) {
        villagerEntity.setCustomName(ChatUtil.fixColor(str));
        villagerEntity.setCustomNameVisible(true);
    }

    public void setInventoryName(String str) {
        this.inventoryName = str;

        MemeShopsPlugin.getPluginInstance().getShopData().setInventoryName(this);
    }

    public void setShopCurrency(Material currency) {
        this.shopCurrency = currency;

        MemeShopsPlugin.getPluginInstance().getShopData().setCurrency(this);
    }
}
