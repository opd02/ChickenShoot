package me.opd02.chickenshoot.Game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.opd02.chickenshoot.Main;

public class GameManager implements Listener {
	
	public Main plugin = Main.getPlugin(Main.class);
	
	private int timeRemaining = 60;
	private int graceTime = 5;
	Location gameSpawn;
	Location gameEndSpawn;
	public static BukkitTask task = null;
	
	public void setupGame(Player p){
		this.gameSpawn = new Location(Bukkit.getWorld(plugin.getConfig().getString("gameSpawn.world")),
				plugin.getConfig().getDouble("gameSpawn.X"),
				plugin.getConfig().getDouble("gameSpawn.Y"),
				plugin.getConfig().getDouble("gameSpawn.Z"),
				(float)plugin.getConfig().getDouble("gameSpawn.YAW"),
				(float)plugin.getConfig().getDouble("gameSpawn.PITCH"));
		
		p.teleport(gameSpawn);
		plugin.isCurrentActiveGame = true;

		//Save inventory
		plugin.inventoryManager.saveInventory(p);
		
		GameManager.task = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run(){
				if(graceTime > 0 && (plugin.isCurrentActiveGame = true)){
					p.sendTitle("§c§l" + graceTime, "");
					p.sendMessage("§l[§6§lGame§r§l] §eChicken shoot will begin in " + graceTime + " seconds.");
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 1);
					graceTime = graceTime -1;
				}else{
					p.resetTitle();
					gameStart(p);
					graceTime = 5;
					task.cancel();
					//stop this task
				}
			}				
		}, 40l, 20l);

		}
	
	public void gameStart(Player p){
		plugin.gameMechanis.spawnFirstWave(p);
		new BukkitRunnable(){
			@Override
			public void run(){
				if(timeRemaining > 0){
					timeRemaining = timeRemaining -1;
					String message = "§l§6Time Remaining: §r§e§l" + timeRemaining;
			        Main.sendActionBar(p, message);
				}else{
					timeRemaining = 60;
					gameStop(p);
					this.cancel();
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0, 20l);
	}
	
	public void gameStop(Player p){
		this.gameEndSpawn = new Location(Bukkit.getWorld(plugin.getConfig().getString("gameEndSpawn.world")),
				plugin.getConfig().getDouble("gameEndSpawn.X"),
				plugin.getConfig().getDouble("gameEndSpawn.Y"),
				plugin.getConfig().getDouble("gameEndSpawn.Z"),
				(float)plugin.getConfig().getDouble("gameEndSpawn.YAW"),
				(float)plugin.getConfig().getDouble("gameEndSpawn.PITCH"));
		for(Entity e : plugin.chickens.keySet()){
			plugin.chickens.remove(e);
			e.remove();
		}
		p.teleport(gameEndSpawn);
		plugin.isCurrentActiveGame = false;
		
		plugin.inventoryManager.loadInventory(p);;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(plugin.playermanager.containsKey(p.getUniqueId())){
			gameStop(p);
			plugin.playermanager.clear();
		}
	}

}
