package com.cospox.boombox;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerTeam {
	public  Location spawn;
	public ArrayList<Player> players = new ArrayList<Player>();
	private boolean flagOut = false;
	private int score = 0;

	public PlayerTeam(String name) {
	}
	
	public boolean isFlagOut() {
		return this.flagOut;
	}
	
	public void setFlagOut(boolean x) {
		this.flagOut = x;
	}
	
	public void incScore() {
		this.score += 1;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void addPlayer(Player p) {
		this.players.add(p);
        if (p.hasPermission("nocollide.nocollide")) {
            Scoreboard scoreboard = p.getScoreboard();
            if(scoreboard == null) {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            }
            
            Team team = scoreboard.getTeam("noCollide");
            if (team == null) {
            	team = scoreboard.registerNewTeam("noCollide");
            }
            
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            
            if (!team.hasEntry(p.getName())) {
                team.addEntry(p.getName());
            }
            p.setScoreboard(scoreboard);
        }
	}
	
	public int getSize() {
		return this.players.size();
	}
	
	public void removePlayer(Player p) {
		if (this.players.contains(p)) {
			this.players.remove(p);
		}
	}
	
	public void setSpawnPoints(Location spawn) {
		this.spawn = spawn;
	}
	
	public void respawnPlayer(Player p) {
		p.teleport(this.spawn);
	}
	
	public boolean isInTeam(Player p) {
		return this.players.contains(p);
	}
	
	public void teleportToSpawn() {
		for (Player p: this.players) {
			p.teleport(this.spawn);
		}
	}
	
	public void broadcast(String message) {
		for (Player p: this.players) {
			p.sendMessage(message);
		}
	}
}
