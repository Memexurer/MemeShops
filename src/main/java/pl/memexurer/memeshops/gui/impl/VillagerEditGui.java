package pl.memexurer.memeshops.gui.impl;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.memexurer.memeshops.MemeShopsPlugin;
import pl.memexurer.memeshops.gui.GuiBuilder;
import pl.memexurer.memeshops.gui.GuiItem;
import pl.memexurer.memeshops.listener.chat.PlayerChatListener;
import pl.memexurer.memeshops.shop.VillagerShop;
import pl.memexurer.memeshops.shop.VillagerShopItem;

import java.util.HashMap;

public class VillagerEditGui extends GuiBuilder {
    private static final HashMap<Villager.Profession, DyeColor> PROFESSION_ITEMS = new HashMap<>();

    static {
        PROFESSION_ITEMS.put(Villager.Profession.BLACKSMITH, DyeColor.BLACK);
        PROFESSION_ITEMS.put(Villager.Profession.BUTCHER, DyeColor.GRAY);
        PROFESSION_ITEMS.put(Villager.Profession.FARMER, DyeColor.BROWN);
        PROFESSION_ITEMS.put(Villager.Profession.LIBRARIAN, DyeColor.WHITE);
        PROFESSION_ITEMS.put(Villager.Profession.PRIEST, DyeColor.PINK);
    }

    private VillagerShop shop;

    private VillagerEditGui(VillagerShop shop) {
        super(shop.getInventoryTitle(), 5);
        this.shop = shop;

        for (int i = 0; i < 36; i++) {
            VillagerShopItem item = shop.getItems().get(i);
            if (item == null) item = new VillagerShopItem(i);

            Material material = item.getItem() != null ? item.getItem().getType() : Material.AIR;
            short durability = item.getItem() != null ? item.getItem().getDurability() : 0;

            VillagerShopItem finalItem = item;
            setItem(i, new GuiItem(ex -> openItemEdit(ex.getWhoClicked(), finalItem), material, item.getName(), durability, item.getDescription()));
        }

        GuiItem villagerAge = new GuiItem(this::changeVillagerAge, Material.ANVIL, 1, "&7Zmien wiek villagera");
        GuiItem villagerProfession = new GuiItem(this::changeProfession, Material.WOOL, 1, PROFESSION_ITEMS.get(shop.getVillagerEntity().getProfession()).getWoolData(),"&7Zmien typ villagera");
        GuiItem villagerDelete = new GuiItem(this::delete, Material.BARRIER, 1, "&cUsun villagera");
        GuiItem villagerName = new GuiItem(this::changeVillagerName, Material.NAME_TAG, 1, "&7Zmien nazwe villagera");
        GuiItem villagerTitle = new GuiItem(this::changeVillagerTitle, Material.NAME_TAG, 1, "&7Zmien nazwe villagera w gui");
        GuiItem villagerCurrency = new GuiItem(this::changeVillagerCurrency, shop.getShopCurrency(), 1, "&7Zmien walute sklepu");

        setItem(4, 0, villagerDelete);
        setItem(4, 1, villagerCurrency);
        setItem(4, 3, villagerProfession);
        setItem(4, 4, villagerAge);
        setItem(4, 7, villagerName);
        setItem(4, 8, villagerTitle);
    }

    public static void openInventory(Player player, VillagerShop villagerShop) {
        player.openInventory(new VillagerEditGui(villagerShop).build());
    }

    private void changeVillagerCurrency(InventoryClickEvent e) {
        e.getWhoClicked().sendMessage(ChatColor.GREEN + "Napisz na chatcie nową walute sklepiku; jeżeli chcesz anulować, napisz \"anuluj\"");
        e.getWhoClicked().closeInventory();

        PlayerChatListener.awaitChatMessage(e.getWhoClicked(), str -> {
            if (!str.equalsIgnoreCase("anuluj")) {
                Material material;
                if((material = Material.getMaterial(str)) == null) {
                    e.getWhoClicked().sendMessage(ChatColor.RED + "Nie znaleziono takiego itemu!");
                    return;
                }

                shop.setShopCurrency(material);
                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Ustawiono nową walute sklepiku! " + ChatColor.GRAY + "(" + material.name().toLowerCase() + ")");
            } else {
                e.getWhoClicked().sendMessage(ChatColor.RED + "Anulowano.");
            }
        });
    }

    private void changeVillagerName(InventoryClickEvent e) {
        e.getWhoClicked().sendMessage(ChatColor.GREEN + "Napisz na chatcie nową nazwe sklepiku; jeżeli chcesz anulować, napisz \"anuluj\"");
        e.getWhoClicked().closeInventory();

        PlayerChatListener.awaitChatMessage(e.getWhoClicked(), str -> {
            if (!str.equalsIgnoreCase("anuluj")) {
                shop.setName(str);
                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Ustawiono nową nazwe sklepiku! " + ChatColor.GRAY + "(" + str + ")");
            } else {
                e.getWhoClicked().sendMessage(ChatColor.RED + "Anulowano.");
            }
        });
    }

    private void changeVillagerTitle(InventoryClickEvent e) {
        e.getWhoClicked().sendMessage(ChatColor.GREEN + "Napisz na chatcie nową nazwe sklepiku; jeżeli chcesz anulować, napisz \"anuluj\"");
        e.getWhoClicked().closeInventory();

        PlayerChatListener.awaitChatMessage(e.getWhoClicked(), str -> {
            if (!str.equalsIgnoreCase("anuluj")) {
                if (str.length() > 32) {
                    e.getWhoClicked().sendMessage(ChatColor.RED + "Nazwa ekwipunku nie moze dluzsza niz 32 znaki.");
                    return;
                }

                shop.setInventoryName(str);
                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Ustawiono nową nazwe sklepiku! " + ChatColor.GRAY + "(" + str + ")");
            } else {
                e.getWhoClicked().sendMessage(ChatColor.RED + "Anulowano.");
            }
        });
    }

    private void openItemEdit(HumanEntity entity, VillagerShopItem item) {
        entity.openInventory(new ShopItemCreationGui(shop, item).build());
    }

    private void changeVillagerAge(InventoryClickEvent inventoryClickEvent) {
        if (shop.getVillagerEntity().isAdult()) shop.getVillagerEntity().setBaby();
        else shop.getVillagerEntity().setAdult();

        inventoryClickEvent.getWhoClicked().sendMessage(ChatColor.GREEN + "Zmieniono wielkosc villagera!");
        inventoryClickEvent.getWhoClicked().closeInventory();
    }

    private void changeProfession(InventoryClickEvent inventoryClickEvent) {
        int professionId = shop.getVillagerEntity().getProfession().getId();
        if (professionId == 4) professionId = 0;
        else professionId++;
        shop.getVillagerEntity().setProfession(Villager.Profession.getProfession(professionId));

        inventoryClickEvent.getWhoClicked().sendMessage(ChatColor.GREEN + "Zmieniono typ villagera na " + shop.getVillagerEntity().getProfession().name().toLowerCase() + "!");
        inventoryClickEvent.getWhoClicked().closeInventory();
    }

    private void delete(InventoryClickEvent inventoryClickEvent) {
        shop.getVillagerEntity().getWorld().strikeLightningEffect(shop.getVillagerEntity().getLocation());
        shop.getVillagerEntity().damage(shop.getVillagerEntity().getHealth() + 1);
        MemeShopsPlugin.getPluginInstance().getShopData().deleteShop(shop);

        inventoryClickEvent.getWhoClicked().sendMessage(ChatColor.RED + "Pomyślnie usunięto sklepik!");
        inventoryClickEvent.getWhoClicked().closeInventory();
    }
}
