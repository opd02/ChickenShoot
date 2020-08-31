package me.opd02.chickenshoot.Game;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import me.opd02.chickenshoot.Main;
import me.opd02.chickenshoot.PlayerData.PlayerManager;

public class GameMechanics implements Listener {
	
	public Main plugin = Main.getPlugin(Main.class);
	public int score;
	public int add;
	public Random rand = new Random();
	
	int x = 19;
	int y = 24;
	int z = 20;
	
	public void spawnFirstWave(Player p){
		for(int i = 0; i<25; i++){
			int addX = rand.nextInt(17);
			int addY = rand.nextInt(6);
			int addZ = rand.nextInt(17);
			
			addX += x;
			addY += y;
			addZ += z;
			
			int score = rand.nextInt(5) + 1;
			
			Location loc = new Location(p.getWorld(), addX, addY, addZ);
			Chicken chick = (Chicken) p.getWorld().spawnEntity(loc, EntityType.CHICKEN);
			plugin.chickens.put(chick, score);
		}
	}
	
	public void spawnChickens(Player p){
			int addX = rand.nextInt(17);
			int addY = rand.nextInt(6);
			int addZ = rand.nextInt(17);
			
			addX += x;
			addY += y;
			addZ += z;
			
			int score = rand.nextInt(5) + 1;
			
			Location loc = new Location(p.getWorld(), addX, addY, addZ);
			Chicken chick = (Chicken) p.getWorld().spawnEntity(loc, EntityType.CHICKEN);
			plugin.chickens.put(chick, score);
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e){
		if(e.getRightClicked().getName().equals("§6§lCHICKEN SHOOT")){
			if(plugin.isCurrentActiveGame==false){
				plugin.playermanager.put(e.getPlayer().getUniqueId(), new PlayerManager(0, true, e.getPlayer().getUniqueId()));
				plugin.gameManager.setupGame(e.getPlayer());
				e.setCancelled(true);	
			}else{
				e.getPlayer().sendMessage(ChatColor.RED + "There is currently a game going on!");
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENDERMAN_HIT, 1, 1);
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDeathEvent e){
			if(plugin.chickens.containsKey(e.getEntity())){
				Player p = (Player) e.getEntity().getKiller();
			
				Firework fw = (Firework) p.getWorld().spawn(e.getEntity().getLocation(), Firework.class);
				FireworkMeta fm = fw.getFireworkMeta();
				fm.setPower(0);
				fm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.RED).with(Type.BALL).build());
				fw.setFireworkMeta(fm);
			//	fw.detonate();
				
				this.score = plugin.playermanager.get(p.getUniqueId()).getScore();
				plugin.playermanager.get(p.getUniqueId()).setScore(score + add);
				
				spawnChickens(p);
				spawnChickens(p);
				
				e.getDrops().clear();
				e.getEntity().remove();

			}	
	}
}
