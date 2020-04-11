package pl.memexurer.memeshops.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.memexurer.memeshops.MemeShopsPlugin;

public class VillagerCreateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("memeshops.edit")) {
            sender.sendMessage(ChatColor.RED + "Nie posiadasz wystarczajacych permisji do uzycia tej komendy!");
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage("Ta komenda jest dostepna tylko dla graczy.");
            return true;
        }

        MemeShopsPlugin.getPluginInstance().getShopData().createShop(((Player) sender).getLocation());
        sender.sendMessage(ChatColor.GREEN + "Pomyślnie utworzono sklepik!");

        return true;
    }
}
