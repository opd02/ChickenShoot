package me.opd02.chickenshoot;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.opd02.chickenshoot.Game.GameManager;
import me.opd02.chickenshoot.Game.GameMechanics;
import me.opd02.chickenshoot.PlayerData.InventoryManager;
import me.opd02.chickenshoot.PlayerData.PlayerManager;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class Main extends JavaPlugin implements Listener {
	
	public HashMap<UUID, PlayerManager> playermanager = new HashMap<UUID, PlayerManager>();
	public HashMap<UUID, ItemStack[]> savedInventory = new HashMap<UUID, ItemStack[]>();
	public HashMap<UUID, ItemStack[]> savedArmor = new HashMap<UUID, ItemStack[]>();
	public HashMap<Chicken, Integer> chickens = new HashMap<Chicken, Integer>();
	public boolean isCurrentActiveGame;
	public GameManager gameManager;
	public InventoryManager inventoryManager;
	public GameMechanics gameMechanis;
	
	public void onEnable(){
		gameManager = new GameManager();
		inventoryManager = new InventoryManager();
		gameMechanis = new GameMechanics();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(new GameManager(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new GameMechanics(), this);
		loadConfig();
		this.isCurrentActiveGame = false;
	}
	
	public void onDisable(){
		for(UUID uuid : playermanager.keySet()){
			Player p = Bukkit.getPlayer(uuid);
			gameManager.gameStop(p);
		}
		playermanager.clear();
	}
	
	public void loadConfig(){
		this.getConfig().options().copyDefaults();
		this.saveConfig();
	}
	
    public static void sendActionBar( Player player, String message ){
        CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent cbc = ChatSerializer.a( "{\"text\": \"" + message + "\"}" );
        PacketPlayOutChat ppoc = new PacketPlayOutChat( cbc, (byte) 2 );
        p.getHandle().playerConnection.sendPacket( ppoc );
    }
    
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]){
		
		if(cmd.getName().equalsIgnoreCase("border")){
			
		}
		
		/*
		if(cmd.getName().equalsIgnoreCase("fw")){
			Player p = (Player) sender;
			Firework fw = (Firework) p.getWorld().spawn(p.getLocation(), Firework.class);
			FireworkMeta fm = fw.getFireworkMeta();
			fm.setPower(1);
			fm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.RED).with(Type.BALL).build());
			fw.setFireworkMeta(fm);
			fw.detonate();
		}
		*/
		if(cmd.getName().equalsIgnoreCase("npccreate")){
			if(!(sender instanceof Player)){
				return true;
			}
			Player p = (Player) sender;
			if(!(p.hasPermission("chickenshoot.npccreate"))){
				p.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
				return true;
			}
			Villager v = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
			v.setProfession(Profession.LIBRARIAN);
			v.setCustomNameVisible(true);
			v.setCustomName("§6§lCHICKEN SHOOT");
			p.playSound(p.getLocation(), Sound.FIREWORK_BLAST, 1, 1);
			p.sendMessage(ChatColor.GREEN + "NPC summoned!");
			
		}
		
		if(cmd.getName().equalsIgnoreCase("setgamespawn")){
			if(!(sender instanceof Player)){
				return true;
			}
			Player p = (Player) sender;
			if(!(p.hasPermission("chickenshoot.setlocations"))){
				p.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
				return true;
			}
			this.getConfig().set("gameSpawn.world", p.getLocation().getWorld().getName());
			this.getConfig().set("gameSpawn.X", p.getLocation().getBlockX());
			this.getConfig().set("gameSpawn.Y", p.getLocation().getBlockY());
			this.getConfig().set("gameSpawn.Z", p.getLocation().getBlockZ());
			this.getConfig().set("gameSpawn.PITCH", (float) p.getLocation().getPitch());
			this.getConfig().set("gameSpawn.YAW", (float) p.getLocation().getYaw());
			this.saveConfig();
			p.sendMessage(ChatColor.GREEN + "Game spawn set!");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("setgameendspawn")){
			if(!(sender instanceof Player)){
				return true;
			}
			Player p = (Player) sender;
			if(!(p.hasPermission("chickenshoot.setlocations"))){
				p.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
				return true;
			}
			this.getConfig().set("gameEndSpawn.world", p.getLocation().getWorld().getName());
			this.getConfig().set("gameEndSpawn.X", p.getLocation().getBlockX());
			this.getConfig().set("gameEndSpawn.Y", p.getLocation().getBlockY());
			this.getConfig().set("gameEndSpawn.Z", p.getLocation().getBlockZ());
			this.getConfig().set("gameEndSpawn.PITCH", (float) p.getLocation().getPitch());
			this.getConfig().set("gameEndSpawn.YAW", (float) p.getLocation().getYaw());
			this.saveConfig();
			p.sendMessage(ChatColor.GREEN + "Game end spawn set!");
		}
		return true;
	}
}
