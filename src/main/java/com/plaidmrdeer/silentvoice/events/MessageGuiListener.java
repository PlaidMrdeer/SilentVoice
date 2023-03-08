package com.plaidmrdeer.silentvoice.events;

import com.plaidmrdeer.silentvoice.SilentVoice;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * @author PlaidMrdeer
 */
public class MessageGuiListener implements Listener, InventoryHolder {
    private final SilentVoice instance;
    public MessageGuiListener(final SilentVoice silentVoice) {
        instance = silentVoice;
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) {
            return;
        }
        if (!(inventory.getHolder() instanceof MessageGuiListener)) {
            return;
        }
        if (inventory.getType() != InventoryType.CHEST) {
            return;
        }
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        switch (slot) {
            case 48:
                instance.getMessageGui().previousPage(player);
                break;
            case 50:
                instance.getMessageGui().nextPage(player);
                break;
            default:
                break;
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
