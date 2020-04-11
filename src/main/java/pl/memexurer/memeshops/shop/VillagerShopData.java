package pl.memexurer.memeshops.shop;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class VillagerShopData {
    private FileConfiguration configuration;
    private File dataFile;

    private HashMap<Integer, VillagerShop> shopMap;

    public VillagerShopData(File dataFolder) {
        this.dataFile = new File(dataFolder,"data.yml");
        try {
            if(!dataFolder.exists()) dataFolder.mkdir();

            if (!this.dataFile.exists()) {
                this.dataFile.createNewFile();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        this.shopMap = new HashMap<>();
    }

    public void load() {
        this.configuration = YamlConfiguration.loadConfiguration(dataFile);

        ConfigurationSection shopSection = configuration.getConfigurationSection("shops");
        if(shopSection == null) return;

        for(String key: shopSection.getKeys(false))  {
            VillagerShop shop = new VillagerShop(shopSection.getConfigurationSection(key));
            shopMap.put(shop.getShopId(), shop);
        }
    }

    public Optional<VillagerShop> findShopByEntity(Entity entity) {
        return shopMap.values().stream().filter(shop -> shop.getEntityUniqueId().equals(entity.getUniqueId())).findAny();
    }

    public void deleteShop(VillagerShop shop) {
        shopMap.remove(shop.getShopId());
        configuration.set("shops." + shop.getShopId(), null);
        try {
            configuration.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createShop(Location entityLocation) {
        VillagerShop shop = new VillagerShop(shopMap.size() + 1, entityLocation);
        shopMap.put(shopMap.size() + 1, shop);
        shop.saveDefault(configuration.createSection("shops." + shop.getShopId()));
        try {
            configuration.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void saveItem(VillagerShop villagerShop, VillagerShopItem shopItem) {
        ConfigurationSection itemSection = configuration.getConfigurationSection("shops." + villagerShop.getShopId() + ".items." + shopItem.getSlot());

        shopItem.save(itemSection == null ? configuration.createSection("shops." + villagerShop.getShopId() + ".items." + shopItem.getSlot()) : itemSection);
        try {
            configuration.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void deleteItem(VillagerShop villagerShop, VillagerShopItem shopItem) {
        configuration.set("shops." + villagerShop.getShopId() + ".items." + shopItem.getSlot(), null);
        try {
            configuration.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setInventoryName(VillagerShop villagerShop) {
        configuration.set("shops." + villagerShop.getShopId() + ".inventoryName", villagerShop.getInventoryTitle());
        try {
            configuration.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setCurrency(VillagerShop villagerShop) {
        configuration.set("shops." + villagerShop.getShopId() + ".currency", villagerShop.getShopCurrency().name());
        try {
            configuration.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
