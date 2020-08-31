package me.opd02.chickenshoot.PlayerData;

import java.util.UUID;

import org.bukkit.event.Listener;

public class PlayerManager implements Listener {
	
	private int score;
	private boolean isingame;
	private UUID uuid;
	
public PlayerManager(int score, boolean isingame, UUID uuid){
		this.setScore(score);
		this.setIsingame(isingame);
		this.setUuid(uuid);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isIsingame() {
		return isingame;
	}

	public void setIsingame(boolean isingame) {
		this.isingame = isingame;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}
