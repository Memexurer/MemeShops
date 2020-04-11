package pl.memexurer.memeshops;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.memexurer.memeshops.commands.VillagerCreateCommand;
import pl.memexurer.memeshops.listener.PlayerActionListener;
import pl.memexurer.memeshops.listener.chat.PlayerChatListener;
import pl.memexurer.memeshops.shop.VillagerShopData;

public final class MemeShopsPlugin extends JavaPlugin {
    private static MemeShopsPlugin PLUGIN_INSTANCE;

    private final VillagerShopData shopData = new VillagerShopData(this.getDataFolder());

    public static MemeShopsPlugin getPluginInstance() {
        return MemeShopsPlugin.PLUGIN_INSTANCE;
    }

    @Override
    public void onEnable() {
        MemeShopsPlugin.PLUGIN_INSTANCE = this;

        Bukkit.getPluginManager().registerEvents(new PlayerActionListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
        this.shopData.load();

        getCommand("villagercreate").setExecutor(new VillagerCreateCommand());
    }

    public VillagerShopData getShopData() {
        return shopData;
    }
}
