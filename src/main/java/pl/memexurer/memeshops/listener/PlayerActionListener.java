package pl.memexurer.memeshops.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import pl.memexurer.memeshops.MemeShopsPlugin;
import pl.memexurer.memeshops.gui.GuiHolder;
import pl.memexurer.memeshops.gui.impl.VillagerEditGui;
import pl.memexurer.memeshops.gui.impl.VillagerViewGui;
import pl.memexurer.memeshops.shop.VillagerShop;

import java.util.Optional;

public class PlayerActionListener implements Listener {
    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent e) {
        Optional<VillagerShop> clickedShop = MemeShopsPlugin.getPluginInstance().getShopData().findShopByEntity(e.getRightClicked());
        if(!clickedShop.isPresent()) return;

        if(e.getPlayer().isSneaking() && e.getPlayer().hasPermission("memeshops.edit")) {
            VillagerEditGui.openInventory(e.getPlayer(), clickedShop.get());
            e.setCancelled(true);
        }
         else {
             VillagerViewGui.openInventory(e.getPlayer(), clickedShop.get());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) { // e.setCancelled na powyzsze jmetodzie nie dziala
        if(e.getInventory() != null && e.getInventory().getHolder() instanceof Villager) {
            Optional<VillagerShop> clickedShop = MemeShopsPlugin.getPluginInstance().getShopData().findShopByEntity(((Villager) e.getInventory().getHolder()));
            if(!clickedShop.isPresent()) return;

            e.setCancelled(true);
        }
    }



    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getInventory() == null || !(e.getInventory().getHolder() instanceof GuiHolder)) return;
        e.setCancelled(e.getRawSlot() < e.getInventory().getSize());
        ((GuiHolder) e.getInventory().getHolder()).getBuilder().handleClick(e);
    }
}
