package pl.memexurer.memeshops.listener.chat;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerChatListener implements Listener {
    private static final HashMap<UUID, AwaitedChatMessage> AWAITED_MESSAGES = new HashMap<>();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        AwaitedChatMessage chatMessage = AWAITED_MESSAGES.get(e.getPlayer().getUniqueId());
        if(chatMessage == null) return;

        e.setCancelled(true);
        chatMessage.getMessage(e.getMessage());
        AWAITED_MESSAGES.remove(e.getPlayer().getUniqueId());
    }

    public static void awaitChatMessage(HumanEntity entity, AwaitedChatMessage message) {
        AWAITED_MESSAGES.put(entity.getUniqueId(), message);
    }
}
