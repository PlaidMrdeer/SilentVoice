package com.plaidmrdeer.silentvoice.gui;

import com.plaidmrdeer.silentvoice.SilentVoice;
import com.plaidmrdeer.silentvoice.events.MessageGuiListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author PlaidMrdeer
 */
public class MessageGui {
    private final SilentVoice instance;
    public MessageGui(SilentVoice silentVoice) {
        instance = silentVoice;
    }
    private Inventory inventory;
    private int totalPage = 0;
    private int page = 1;
    private String name;
    private boolean isNothing = false;
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    public void messageGui(Player player, String name) {
        this.name = name;
        inventory = Bukkit.createInventory(new MessageGuiListener(instance), 6 * 9, ChatColor.DARK_RED + name);
        player.openInventory(inventory);

        new BukkitRunnable() {
            @Override
            public void run() {
                Map<Integer, List<String>> message;
                try {
                    if ("sqlite".equals (instance.config.getString("sql.type"))) {
                        message = instance.getSqlite().query(name);
                    } else {
                        message = instance.getMySql().query(name);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                int size = message.size();
                if (size == 0) {
                    isNothing = true;
                    cancel();
                    return;
                }
                int maxNumber = 45;
                totalPage = (int)Math.ceil((double)size / maxNumber);
                inventory = Bukkit.createInventory(new MessageGuiListener(instance), 6 * 9, ChatColor.DARK_RED + name + ChatColor.BLACK + " (" + page + ChatColor.BLACK + "/" + totalPage + ChatColor.BLACK + "/" + ChatColor.YELLOW + size + ChatColor.BLACK + ")");
                Bukkit.getScheduler().runTask(instance, () -> player.openInventory(inventory));

                for (int i = 45; i <= 53; i++) {
                    itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                    itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(" ");
                    itemStack.setItemMeta(itemMeta);
                    inventory.setItem(i, itemStack);
                }

                itemStack = new ItemStack(Material.ARROW);
                itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(instance.setStyle(instance.getLanguageSetting().language.getString("previous_page")));
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(48, itemStack);

                itemMeta.setDisplayName(instance.setStyle(instance.getLanguageSetting().language.getString("next_page")));
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(50, itemStack);

                int startIndex = (page - 1) * maxNumber;
                int endIndex = Math.min(startIndex + maxNumber, size);
                for (int i = startIndex; i < endIndex; i++) {
                    itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                    itemMeta = itemStack.getItemMeta();
                    List<String> messageList = message.get(i + 1);
                    itemMeta.setDisplayName(instance.setStyle(instance.getLanguageSetting().language.getString("anonymous_user")));
                    String primaryMessage = messageList.get(0);
                    String time = messageList.get(1);
                    List<String> lore = new ArrayList<>();
                    lore.add("----------------------");
                    lore.add(ChatColor.AQUA + primaryMessage);
                    lore.add("----------------------");
                    lore.add(ChatColor.AQUA + time);
                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);
                    inventory.setItem(i - startIndex, itemStack);
                }
                cancel();
            }
        }.runTaskAsynchronously(instance);

        if (isNothing) {
            itemStack = new ItemStack(Material.NAME_TAG);
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(instance.setStyle(instance.getLanguageSetting().language.getString("no_message")));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(22, itemStack);
            return;
        }

        ItemStack itemStack;
        ItemMeta itemMeta;
        itemStack = new ItemStack(Material.NAME_TAG);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(instance.setStyle(instance.getLanguageSetting().language.getString("load_message")));
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(22, itemStack);
    }

    public void nextPage(Player player) {
        if (page < totalPage) {
            page++;
            try {
                messageGui(player, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void previousPage(Player player) {
        if (page > 1) {
            page--;
            try {
                messageGui(player, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
