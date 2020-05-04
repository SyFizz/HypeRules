package fr.syfizz.hyperules.listeners;

import fr.syfizz.hyperules.HypeRules;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.UUID;

public class HypeRulesListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(HypeRules.getInstance(), new Runnable() {
            public void run() {
                if (!HypeRules.uuids.contains(event.getPlayer().getUniqueId())) {
                    if (HypeRules.hidePlayer) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (!p.getName().equalsIgnoreCase(event.getPlayer().getName())) {
                                p.hidePlayer(HypeRules.getInstance(), event.getPlayer());
                                event.getPlayer().hidePlayer(HypeRules.getInstance(), p);
                            }
                        }
                    }
                    event.getPlayer().openInventory(HypeRules.inv);
                }
            }
        }, 40L);

        ArrayList<String> strings = new ArrayList();
        for (UUID uuid : HypeRules.uuids) {
            strings.add(uuid.toString());
        }
        HypeRules.getInstance().getConfig().set("players", strings);

        HypeRules.getInstance().saveConfig();
        }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(HypeRules.getInstance(), new Runnable() {
            public void run() {
                if (!HypeRules.uuids.contains(event.getPlayer().getUniqueId())) {
                    event.getPlayer().openInventory(HypeRules.inv);
                }
            }
        }, 5L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equalsIgnoreCase(HypeRules.inv.getName())) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

            int slot = event.getRawSlot();
            if (slot == HypeRules.getInstance().getConfig().getInt("gui.agree.slot")) {
                HypeRules.uuids.add(event.getWhoClicked().getUniqueId());
                event.getView().close();
                if (HypeRules.hidePlayer) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.getName().equalsIgnoreCase(event.getWhoClicked().getName())) {
                            p.showPlayer(HypeRules.getInstance(), Bukkit.getPlayer(event.getWhoClicked().getName()));
                            Bukkit.getPlayer(event.getWhoClicked().getName()).showPlayer(HypeRules.getInstance(), p);
                        }
                    }
                }
                for (String command : HypeRules.getInstance().agreecommands){
                    if (command.startsWith("tell %player% ")) {
                        Bukkit.getPlayer(command.replace("%player%", event.getWhoClicked().getName()).split(" ")[1]).sendMessage(
                                ChatColor.translateAlternateColorCodes('&', command.replace("tell %player% ", "")));
                    } else {
                        HypeRules.getInstance().getServer().dispatchCommand(HypeRules.getInstance().getServer().getConsoleSender(),
                                ChatColor.translateAlternateColorCodes('&', command.replace("%player%", event.getWhoClicked().getName())));

                    }
                }
            }
            if (slot == HypeRules.getInstance().getConfig().getInt("gui.disagree.slot")) {
                event.getView().close();
                for (String command : HypeRules.getInstance().disagreecommands) {
                    HypeRules.getInstance().getServer().dispatchCommand(HypeRules.getInstance().getServer().getConsoleSender(),
                            ChatColor.translateAlternateColorCodes('&', command.replace("%player%", event.getWhoClicked().getName())));
                }
            }
        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (((event.getEntity() instanceof Player)) &&
                (!HypeRules.uuids.contains(event.getEntity().getUniqueId()))) {
            event.setCancelled(true);
            event.setDamage(0.0D);
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (((event.getEntity() instanceof Player)) &&
                (!HypeRules.uuids.contains(event.getEntity().getUniqueId()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (((event.getEntity() instanceof Player)) &&
                (!HypeRules.uuids.contains(event.getEntity().getUniqueId()))) {
            event.setCancelled(true);
        }
    }

}
