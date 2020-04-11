package pl.memexurer.memeshops.gui.impl;

import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.memexurer.memeshops.gui.GuiBuilder;
import pl.memexurer.memeshops.gui.GuiItem;
import pl.memexurer.memeshops.shop.VillagerShop;
import pl.memexurer.memeshops.shop.VillagerShopItem;

import java.util.Map;

public class VillagerViewGui extends GuiBuilder {
    private VillagerShop shop;

    private VillagerViewGui(VillagerShop shop) {
        super(shop.getInventoryTitle(), 4);

        this.shop = shop;
        for (Map.Entry<Integer, VillagerShopItem> itemEntry : shop.getItems().entrySet()) {
            setItem(itemEntry.getKey(), new GuiItem(e -> buyItem(e.getWhoClicked(), itemEntry.getValue()), itemEntry.getValue().getItem().getType(), itemEntry.getValue().getName(), itemEntry.getValue().getItem().getDurability(), itemEntry.getValue().getDescription()));
        }
    }

    public static void openInventory(Player player, VillagerShop shop) {
        player.openInventory(new VillagerViewGui(shop).build());
    }

    private void buyItem(HumanEntity entity, VillagerShopItem item) {
        if (!entity.getInventory().containsAtLeast(new ItemStack(shop.getShopCurrency()), item.getPrice())) {
            entity.sendMessage(ChatColor.RED + "Nie posiadasz wystarczajacych itemow do zakupu tego przedmiotu.");
            return;
        }

        entity.getInventory().removeItem(new ItemStack(shop.getShopCurrency(), item.getPrice()));
        entity.getInventory().addItem(item.getItem());
        entity.sendMessage(ChatColor.GREEN + "Pomyślnie zakupiłeś item!");
    }
}
