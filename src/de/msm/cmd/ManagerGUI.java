package de.msm.cmd;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ManagerGUI implements Listener, CommandExecutor {
	
	File file = new File("plugins//EntitySpawnHandler//EntityList.yml");
	YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String args[]) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("�cYou must be a player to use this command!");
			return true;
		}
		
		if(!sender.hasPermission("esm.perm.gui")) {
			sender.sendMessage("�cYou don't have the permissions to use this command!");
			return true;
		}
		
		setGui((Player) sender);
		
		return false;
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getPlayer().getOpenInventory().getTitle().equalsIgnoreCase("�cEntitySpawnManager - GUI")) {
			e.getPlayer().sendMessage("�4To update the config you must reload or restart the server!");
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("�cEntitySpawnManager - GUI")) {
			e.setCancelled(true);
			if(e.getCurrentItem().getType().equals(Material.LIME_WOOL)) {
					ItemStack item = new ItemStack(Material.RED_WOOL);
					ItemMeta itemmeta = item.getItemMeta();
					itemmeta.setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName());
					itemmeta.setLore(Arrays.asList("�7Status: �cDISABLED"));
					item.setItemMeta(itemmeta);
					e.setCurrentItem(item);
					yaml.set(e.getCurrentItem().getItemMeta().getDisplayName(), false);
					try { yaml.save(file); } catch(IOException e1) {
				}
			}else if(e.getCurrentItem().getType().equals(Material.RED_WOOL)) {
					ItemStack item = new ItemStack(Material.LIME_WOOL);
					ItemMeta itemmeta = item.getItemMeta();
					itemmeta.setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName());
					itemmeta.setLore(Arrays.asList("�7Status: �aENABLED"));
					item.setItemMeta(itemmeta);
					e.setCurrentItem(item);
					yaml.set(e.getCurrentItem().getItemMeta().getDisplayName(), true);
					try { yaml.save(file); } catch(IOException e1) {
			}
		}
	}
}
	
	public void setGui(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*6, "�cEntitySpawnManager - GUI");
		
		int slot = -1;
		for(String entry : yaml.getKeys(false)) {
			slot++;
			
			if(yaml.getBoolean(entry)) {
				ItemStack item = new ItemStack(Material.LIME_WOOL);
				ItemMeta itemmeta = item.getItemMeta();
				itemmeta.setDisplayName(entry);
				itemmeta.setLore(Arrays.asList("�7Status: �aENABLED"));
				item.setItemMeta(itemmeta);
				item.setAmount(1);
				inv.setItem(slot, item);
			}else {
				ItemStack item = new ItemStack(Material.RED_WOOL);
				ItemMeta itemmeta = item.getItemMeta();
				itemmeta.setDisplayName(entry);
				itemmeta.setLore(Arrays.asList("�7Status: �cDISABLED"));
				item.setItemMeta(itemmeta);
				item.setAmount(1);
				inv.setItem(slot, item);
			}
		}
		p.openInventory(inv);
	}

}
