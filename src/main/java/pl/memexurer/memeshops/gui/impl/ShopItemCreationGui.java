package pl.memexurer.memeshops.gui.impl;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import pl.memexurer.memeshops.gui.GuiBuilder;
import pl.memexurer.memeshops.gui.GuiItem;
import pl.memexurer.memeshops.listener.chat.PlayerChatListener;
import pl.memexurer.memeshops.shop.VillagerShop;
import pl.memexurer.memeshops.shop.VillagerShopItem;

import java.util.Arrays;
import java.util.List;

public class ShopItemCreationGui extends GuiBuilder {
    private VillagerShopItem shopItem;
    private VillagerShop shop;

    public ShopItemCreationGui(VillagerShop shop, VillagerShopItem shopItem) {
        super(InventoryType.HOPPER);

        this.shopItem = shopItem;
        this.shop = shop;

        GuiItem info = new GuiItem(null, Material.PAPER, "&7Informacja", "&7Zamień środkowy slot tego ekwipunku, na item który chcesz ustawić.", "&7Kliknij na środkowy slot z użyciem shifta aby zapisać zmiany.");
        GuiItem setItem = new GuiItem(e -> {
            if(!e.isShiftClick()) {
                shopItem.setItem(e.getInventory().getItem(0));
                e.setCancelled(false);
                return;
            }

            if (e.getInventory().getItem(0) != null) {
                e.setCancelled(true);
                if(shopItem.getName() == null || shopItem.getDescription() == null || shopItem.getPrice() == 0) {
                    e.getWhoClicked().sendMessage(ChatColor.RED + "Nie mozna utworzyc itemu, poniewaz niektore wartosci pozostaly niewypelnione.");
                    return;
                }

                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Zapisano!");
                shopItem.setItem(e.getInventory().getItem(0));
                shop.saveItem(shopItem);
            } else {
                if (shopItem.getItem() != null) {
                    shop.removeItem(shopItem);
                    e.getWhoClicked().sendMessage(ChatColor.RED + "Pomyślnie usunięto item.");
                }
            }

            VillagerViewGui.openInventory((Player) e.getWhoClicked(), shop);
        }, (shopItem != null) ? shopItem.getItem() : new ItemStack(Material.AIR));

        GuiItem changeItemName = new GuiItem(this::changeItemName, Material.WOOL, 1, (shopItem != null && shopItem.getName() != null) ? DyeColor.LIME.getWoolData() : DyeColor.RED.getWoolData(), "&7Zmien nazwe itemu", "&7Kliknij aby zmienic nazwe itemu.");
        GuiItem changeItemDescription = new GuiItem(this::changeItemDescription, Material.WOOL, 1, (shopItem != null && shopItem.getDescription() != null) ? DyeColor.LIME.getWoolData() : DyeColor.RED.getWoolData(), "&7Zmien opis itemu", "&7Kliknij aby zmienic opis itemu.");
        GuiItem changeItemPrice = new GuiItem(this::changeItemPrice, Material.WOOL, 1, (shopItem != null && shopItem.getPrice() != 0) ? DyeColor.LIME.getWoolData() : DyeColor.RED.getWoolData(), "&7Zmien cene itemu", "&7Kliknij aby zmienic cene itemu.");

        setItem(0, setItem);
        setItem(1, changeItemName);
        setItem(2, changeItemDescription);
        setItem(3, changeItemPrice);

        setItem(4, info);
    }

    public static void openInventory(HumanEntity entity, VillagerShop shop, VillagerShopItem item) {
        entity.openInventory(new ShopItemCreationGui(shop, item).build());
    }

    private void changeItemName(InventoryClickEvent e) {
        e.getWhoClicked().sendMessage(ChatColor.GREEN + "Napisz na chatcie nową nazwe itemu; jeżeli chcesz anulować, napisz \"anuluj\"");
        e.getWhoClicked().closeInventory();
        PlayerChatListener.awaitChatMessage(e.getWhoClicked(), str -> {
            if (!str.equalsIgnoreCase("anuluj")) {
                shopItem.setName(str);
                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Ustawiono nową nazwe itemu! " + ChatColor.GRAY + "(" + str + ")");
            } else {
                e.getWhoClicked().sendMessage(ChatColor.RED + "Anulowano.");
            }
            openInventory(e.getWhoClicked(), shop, shopItem);
        });
    }

    private void changeItemDescription(InventoryClickEvent e) {
        e.getWhoClicked().sendMessage(ChatColor.GREEN + "Napisz na chatcie nowy opis itemu, do nowych linu uzywaj znaku &b|&7; jeżeli chcesz anulować, napisz \"anuluj\"");
        e.getWhoClicked().closeInventory();
        PlayerChatListener.awaitChatMessage(e.getWhoClicked(), str -> {
            if (!str.equalsIgnoreCase("anuluj")) {
                List<String> itemDescription = Arrays.asList(str.split("\\|"));
                shopItem.setDescription(itemDescription);
                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Ustawiono nowy opis itemu! " + ChatColor.GRAY + "(" + String.join("\n", itemDescription) + ")");
            } else {
                e.getWhoClicked().sendMessage(ChatColor.RED + "Anulowano.");
            }
            openInventory(e.getWhoClicked(), shop, shopItem);
        });
    }

    private void changeItemPrice(InventoryClickEvent e) {
        e.getWhoClicked().sendMessage(ChatColor.GREEN + "Napisz na chatcie nową cene itemu; jeżeli chcesz anulować, napisz \"anuluj\"");
        e.getWhoClicked().closeInventory();
        PlayerChatListener.awaitChatMessage(e.getWhoClicked(), str -> {
            if (!str.equalsIgnoreCase("anuluj")) {
                if (!isInteger(str)) {
                    e.getWhoClicked().sendMessage(ChatColor.RED + "Podales niepoprawna cene itemu.");
                } else {
                    shopItem.setPrice(Integer.parseInt(str));
                    e.getWhoClicked().sendMessage(ChatColor.GREEN + "Pomyślnie ustawiono nową cene itemu!");
                }
            } else {
                e.getWhoClicked().sendMessage(ChatColor.RED + "Anulowano.");
            }
            openInventory(e.getWhoClicked(), shop, shopItem);
        });
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
