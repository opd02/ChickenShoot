package me.opd02.chickenshoot.PlayerData;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.opd02.chickenshoot.Main;

public class InventoryManager implements Listener {
	
	public Main plugin = Main.getPlugin(Main.class);
	public float exp;
	public int level;
	
	public void saveInventory(Player p){
		
			plugin.savedInventory.put(p.getUniqueId(), p.getInventory().getContents());
			plugin.savedArmor.put(p.getUniqueId(), p.getInventory().getArmorContents());

		p.getInventory().clear();
		p.getInventory().setBoots(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setHelmet(null);
		
		ItemStack bow = new ItemStack(Material.BOW, 1);
		ItemMeta bowmeta = bow.getItemMeta();
		bowmeta.spigot().setUnbreakable(true);
		bowmeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
		bowmeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		bow.setItemMeta(bowmeta);
		
		p.getInventory().addItem(bow);
		p.getInventory().setItem(17, new ItemStack(Material.ARROW));
		
		this.exp = p.getExp();
		this.level = p.getLevel();
		p.setExp(0);
		p.setLevel(0);
		
	}
	
	public void loadInventory(Player p){
		p.getInventory().clear();
		
		ItemStack[] inv = plugin.savedInventory.get(p.getUniqueId());
		ItemStack[] armor = plugin.savedArmor.get(p.getUniqueId());
		
		p.getInventory().setContents(inv);
		p.getInventory().setArmorContents(armor);;
		
		plugin.savedInventory.clear();
		plugin.savedArmor.clear();
		
		p.setExp(exp);
		p.setLevel(level);
	}
}
