package fr.syfizz.hyperules;

import fr.syfizz.hyperules.commands.HypeRulesCommand;
import fr.syfizz.hyperules.listeners.HypeRulesListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class HypeRules extends JavaPlugin {
    public static List<UUID> uuids = new ArrayList();
    public static Inventory inv;
    public List<String> agreecommands = new ArrayList();
    public List<String> disagreecommands = new ArrayList();
    static HypeRules instance;
    public static boolean hidePlayer;
    public static HypeRules getInstance(){
        return instance;
    }

    public void onEnable(){

        saveDefaultConfig();
        System.out.println("HypeRules enabled !");
        instance = this;
        for (String s : getConfig().getStringList("players")) {
            uuids.add(UUID.fromString(s));
        }
        hidePlayer = getConfig().getBoolean("hidePlayer");
        inv = Bukkit.createInventory(
                null,
                getConfig().getInt("gui.rows") * 9,
                ChatColor.translateAlternateColorCodes(
                        '&',
                        getConfig().getString("gui.name").substring(0,
                                getConfig().getString("gui.name").length() >= 32 ? 31 : getConfig().getString("gui.name").length())));
        for (int i = 0; i < inv.getSize(); i++) {
            try {
                ItemStack item = new ItemStack(Material.getMaterial(getConfig().getString("gui." + i + ".itemid")), getConfig().getInt("gui." + i + ".amount"));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(getConfig().getString("gui." + i + ".itemname").replaceAll("&","§"));
                List<String> lores = new ArrayList();
                for (String lore : getConfig().getStringList("gui." + i + ".lores")) {
                    lores.add(lore.replaceAll("&","§"));
                }
                meta.setLore(lores);
                item.setItemMeta(meta);
                try {
                    Iterator<String> it = getConfig().getStringList("gui." + i + ".enchantments").iterator();
                    while (it.hasNext()) {
                        String data = (String) it.next();
                        item.addEnchantment(Enchantment.getByName(data.split(":")[0]), Integer.parseInt(data.split(":")[1]));
                    }
                } catch (Exception localException) {}
                inv.setItem(getConfig().getInt("gui." + i + ".slot"), item);
            } catch (Exception localException4) {}
        }


        ItemStack agree = new ItemStack(Material.getMaterial(getConfig().getString("gui.agree.itemid")), getConfig().getInt("gui.agree.amount"));
        ItemMeta agreemeta = agree.getItemMeta();
        agreemeta.setDisplayName(getConfig().getString("gui.agree.itemname").replaceAll("&","§"));
        List<String> lores = new ArrayList();
        for (String lore : getConfig().getStringList("gui.agree.lores")) {
            lores.add(lore.replaceAll("&","§"));
        }
        agreemeta.setLore(lores);
        agree.setItemMeta(agreemeta);
        try {
            for (String enchantmentData : getConfig().getStringList("gui.agree.enchantments")) {
                agree.addEnchantment(Enchantment.getByName(enchantmentData.split(":")[0]), Integer.parseInt(enchantmentData.split(":")[1]));
            }
        } catch (Exception localException2) {
        }
        inv.setItem(getConfig().getInt("gui.agree.slot"), agree);
        agreecommands = ((ArrayList) getConfig().getStringList("agreecommands"));

        ItemStack disagree = new ItemStack(Material.getMaterial(getConfig().getString("gui.disagree.itemid")), getConfig().getInt("gui.disagree.itemamount"));
        ItemMeta disagreemeta = agree.getItemMeta();
        disagreemeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString("gui.disagree.itemname")));
        List<String> lores2 = new ArrayList();
        for (String lore : getConfig().getStringList("gui.disagree.lores")) {
            lores2.add(lore.replaceAll("&","§"));
        }
        disagreemeta.setLore(lores2);
        disagree.setItemMeta(disagreemeta);
        try {
            Iterator<String> s = getConfig().getStringList("gui.disagree.enchantments").iterator();
            while (s.hasNext()) {
                String enchantmentData = (String) s.next();

                disagree.addEnchantment(Enchantment.getByName(enchantmentData.split(":")[0]), Integer.parseInt(enchantmentData.split(":")[1]));
            }
        } catch (Exception localException3) {
        }
        inv.setItem(getConfig().getInt("gui.disagree.slot"), disagree);
        this.disagreecommands = ((ArrayList) getConfig().getStringList("disagreecommands"));

        Bukkit.getPluginManager().registerEvents(new HypeRulesListener(), this);
        getCommand("hyperules").setExecutor(new HypeRulesCommand());
    }
    public void onDisable(){
        System.out.println("HypeRules disabled !");
    }
}
