package com.cospox.boombox;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/*
1) better colour indication
4) if u die, delet the concrete
5) replenish wall
6) adjust rates
7) you can epearl out, fix
8) fishing rods
9) Something's broken with the generator chances
10) Flag detection thing show the num flags captured
11) wall regen fuel thing
12) Do the bloody terrian
13) Corner-to-corner teleports
14) Reset 
*/

public class Game {
	private  boolean  gameIsRunning = false;
	private  int      countingDown  = 60;
	
	private Location[] buttonLocations;
	private Location[] tntLocations;
	
	public static Location CORNER;
	private Location lobbySpawn;
	private Location redSpawn;
	private Location blueSpawn;
	
	private Location RED_FLAG_BUTTON;
	private Location BLUE_FLAG_BUTTON;
	private Location RED_FLAG_DROP;
	private Location BLUE_FLAG_DROP;
	
	private Location RED_FLAG_BARREL;
	private Location BLUE_FLAG_BARREL;
	
	private World w;
	private Server s;
	public static Main plugin;
	
	private PlayerTeam red = new PlayerTeam("red");
	private PlayerTeam blue = new PlayerTeam("blue");
	
	private Objective objective;
	
	public Game(World w, Server s, Main plugin) {
		Game.plugin = plugin;
		this.w = w;
		this.s = s;
    	
    	CORNER = new Location(this.w, -34, 9, -9);
    	buttonLocations = new Location[]{new Location(w, -17, 8, 4 ),
    			                         new Location(w, -17, 8, 12),
    			                         new Location(w, -25, 8, 12),
    			                         new Location(w, -25, 8, 4)};
    	
    	tntLocations = new Location[]   {new Location(w, -19, 10, 10),
    									 new Location(w, -23, 10, 10),
    									 new Location(w, -23, 10, 6),
    									 new Location(w, -19, 10, 6)};
    	this.lobbySpawn = new Location(w, 200, 20, 200);
    	this.redSpawn   = new Location(w, -20, 14, -13);
    	this.blueSpawn  = new Location(w, -20, 14, 30);
    	
    	this.RED_FLAG_BUTTON = new Location(w, -14, 11, -25);
    	this.BLUE_FLAG_BUTTON = new Location(w, -14, 11, 41);
    	
    	this.RED_FLAG_DROP = new Location(w, -16, 12, -25);
    	this.BLUE_FLAG_DROP = new Location(w,-16, 12, 41);
    	
    	this.RED_FLAG_BARREL = new Location(w, -21, 9, 28);
    	this.BLUE_FLAG_BARREL = new Location(w, -21, 9, -12);
	}
	
	public void start() {
		this.reset();
		this.gameIsRunning = true;
    	this.countingDown = -1;
		this.assignTeams();
		for (Player p: this.w.getPlayers()) {
			p.sendTitle("GO!", "", 10, 50, 10);
			p.getInventory().clear();
			p.setHealth(20);
			p.setFoodLevel(20);
			p.getInventory().setArmorContents(null);
			for(PotionEffect effect: p.getActivePotionEffects()){
				p.removePotionEffect(effect.getType());
			}
		}
	}
	
	public void checkWon() {
		if (this.red.getScore() > 3) {
			this.won("RED");
		}
		
		if (this.blue.getScore() > 3) {
			this.won("BLUE");
		}
	}
	
	public void won(String team) {
		for (Player p: this.w.getPlayers()) {
			p.sendTitle(team + " WON!", "", 10, 50, 10);
			p.teleport(this.lobbySpawn);
		}
		this.gameIsRunning = false;
		this.reset();
	}
	
	public void doFlags(World w) {
		if (w.getBlockAt(RED_FLAG_BUTTON).isBlockPowered() && !this.red.isFlagOut()) {
			this.red.setFlagOut(true);
			
			w.dropItemNaturally(RED_FLAG_DROP, new ItemStack(Material.RED_BANNER, 1));
		}
		
		if (w.getBlockAt(BLUE_FLAG_BUTTON).isBlockPowered() && !this.blue.isFlagOut()) {
			this.blue.setFlagOut(true);
			w.dropItemNaturally(BLUE_FLAG_DROP, new ItemStack(Material.BLUE_BANNER, 1));
		}
		
		Block b = w.getBlockAt(RED_FLAG_BARREL);
		BlockState bs = b.getState();
		Container c = (Container) bs;
		Inventory i = c.getInventory();
		if (i.contains(new ItemStack(Material.RED_BANNER))) {
			i.clear();
			this.blue.incScore();
			objective.getScore(ChatColor.BLUE + "BLUE").setScore(this.blue.getScore());
			this.red.setFlagOut(false);
			this.checkWon();
		}
		if (i.contains(new ItemStack(Material.STONE))) {
			i.clear();
			resetWalls(false, true);
			this.blue.broadcast("Your wall has been reset!");
		}
		
		b = w.getBlockAt(BLUE_FLAG_BARREL);
		bs = b.getState();
		c = (Container) bs;
		i = c.getInventory();
		if (i.contains(new ItemStack(Material.BLUE_BANNER))) {
			i.clear();
			this.red.incScore();
			objective.getScore(ChatColor.RED  + "RED").setScore(this.red.getScore());
			this.blue.setFlagOut(false);
			this.checkWon();
		}
		if (i.contains(new ItemStack(Material.STONE))) {
			i.clear();
			resetWalls(true, false);
			this.red.broadcast("Your wall has been reset!");
		}
	}
	
	public void resetWalls(boolean red, boolean blue) {
		int x = -19;
		for (int y = 11; y <= 13; y++) {
			if (red) {
				for (int z = -24; z <= -20; z++) {
					w.getBlockAt(new Location(w, x, y, z)).setType(Material.COBBLESTONE);
				}
			}
			
			if (blue) {
				for (int z = 36; z <= 40; z++) {
					w.getBlockAt(new Location(w, x, y, z)).setType(Material.COBBLESTONE);
				}
			}
		}
	}
	
	public void reset() {
		this.red = new PlayerTeam("red");
		this.blue = new PlayerTeam("blue");
		this.countingDown = 60;
		Generator.reset(this.w);
		this.resetWalls(true, true);
		//this.s.dispatchCommand(this.s.getConsoleSender(), "fill -19 11 -24   -19 13 -20 minecraft:cobblestone");
		//this.s.dispatchCommand(this.s.getConsoleSender(), "fill -19 11 36 -19 13 40 minecraft:cobblestone");
    }
	
	public void countDown() { //HELP
		if (this.gameIsRunning) { return; }
		
    	if (this.countingDown < 0) { return; }
    	
    	if (this.s.getOnlinePlayers().size() > 0) {
    		if (this.w.getPlayers().size() < 4) {
    			countingDown = 60;
    		} else {
    			countingDown -= 1;
    		}
    		if (countingDown < 0) {
    			this.start();
    		}
    	} else {
    		this.countingDown = 60;
    	}
    	
    	if (this.s.getOnlinePlayers().size() == 6) { this.countingDown = 5; }
    	for (Player player: this.s.getOnlinePlayers()) {
    		player.setLevel(this.countingDown);
    	}
    }
	
	public void handlePlayers() {
		
	}
	
	public void addResources(World w) {
		if (!this.gameIsRunning) { return; }
		Generator.generate(w);
	}
	
	public void assignTeams() {
		for (Player p: this.w.getPlayers()) {
			if (!this.red.isInTeam(p) && !this.blue.isInTeam(p)) {
				if (this.red.getSize() < this.blue.getSize()) {
					this.red.addPlayer(p);
				} else {
					this.blue.addPlayer(p);
				}
			}
		}
		this.blue.setSpawnPoints(this.blueSpawn);
		this.red.setSpawnPoints(this.redSpawn);
		this.red.teleportToSpawn();
		this.blue.teleportToSpawn();
		this.setupScoreboard();
    }
	
	public void setupScoreboard() {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		
	    objective = sb.registerNewObjective("teams", "dummy", "Teams");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.getScore(ChatColor.RED  + "RED").setScore(0);
		objective.getScore(ChatColor.BLUE + "BLUE").setScore(0);
		for (Player p : this.w.getPlayers()) {
			if (red.isInTeam(p)) {
				p.setPlayerListName(ChatColor.RED + p.getName());
			} else if (blue.isInTeam(p)) {
				p.setPlayerListName(ChatColor.BLUE + p.getName());
			}
			p.setScoreboard(sb);
		}
	}
	
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.getPlayer().teleport(this.lobbySpawn);
	}
	
	public void onPlayerQuit(PlayerQuitEvent e) {
		
	}
	
	 public void onBlockPlace(BlockPlaceEvent e) {
		 if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
		 Location l = e.getBlock().getLocation();
		 Location below = l.subtract(0, 1, 0);
		 if (this.w.getBlockAt(below).getType() != Material.REDSTONE_BLOCK &&
				 this.w.getBlockAt(below).getType() != Material.OBSIDIAN) {
			 e.setCancelled(true);
		 }
		 if (e.getBlock().getType() != Material.TNT) {
			 e.setCancelled(true);
		 }
	 }
	
	//public void onItemDrop(PlayerDropItemEvent e) {
	//}
	
	public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
		if (e.getItem().getType() == Material.CHORUS_FRUIT) {
    		e.setCancelled(true);
    		ItemStack a = e.getPlayer().getInventory().getItemInMainHand();
    		//if (a.getType() != Material.CHORUS_FRUIT) {
    		//	throw new RuntimeException("Bad things happened: Item eaten wasn't chorus fruit even if it was (this message should NEVER occur!)");
    		//}
    		a.setAmount(a.getAmount() - 1);
    		int x, y, z;
    		for (int i = 0; i < 5; i++) {
	    		x = ThreadLocalRandom.current().nextInt(-8, 9);
	    		y = 0;
	    		z = ThreadLocalRandom.current().nextInt(-8, 9);
	    		Location playerLoc = e.getPlayer().getLocation();
	    		x += playerLoc.getX();
	    		y += playerLoc.getY();
	    		z += playerLoc.getZ();
	    		
	    		if ((this.w.getBlockAt(x, y - 1, z).getType() == Material.REDSTONE_BLOCK &&
    				 this.w.getBlockAt(x, y, z).getType() == Material.WATER)) {
	    			e.getPlayer().teleport(new Location(this.w, x + 0.5, y, z + 0.5));
	    			break;
	    		}
    		}
    	}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("pausegame")) { 
        	this.gameIsRunning = false; 
        	sender.sendMessage("Game has been paused!");
        }
    	 
    	if (cmd.getName().equals("startgame")) { 
        	this.start();
        	sender.sendMessage("Game is now running!");
        }
    	
    	if (cmd.getName().equals("stopgame")) { 
        	gameIsRunning = false;
        	this.reset();
        	sender.sendMessage("Game has been stopped & reset!");
    	}
    	
    	if (cmd.getName().equals("onteam")) {
    		if (args[0].toLowerCase() == "red") {
    			this.red.addPlayer((Player)sender);
    			sender.sendMessage("You have joined the RED team!");
    		} else if (args[0].toLowerCase() == "blue") {
    			this.blue.addPlayer((Player)sender);
    			sender.sendMessage("You have joined the BLUE team!");
    		} else {
    			sender.sendMessage("Invalid argument: Usage: /onteam (red or blue)");
    		}
    	}
        return true;
	}

	public void doTNTbuttons(World w) {
		for (Location l: buttonLocations) {
			if (w.getBlockAt(l).isBlockPowered()) {
				for (Location l2: tntLocations) {
					w.spawnEntity(l2, EntityType.PRIMED_TNT);
				}
				return;
			}
		}
	}

	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if (red.isInTeam(e.getPlayer())) {
			e.setRespawnLocation(redSpawn);
		}
		
		if (blue.isInTeam(e.getPlayer())) {
			e.setRespawnLocation(blueSpawn);
		}
	}
	
	public void onBlockBreak(BlockBreakEvent e) {
		if (!(e.getBlock().getType() == Material.TNT) && e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			e.setCancelled(true);
		}
	}

	public void onBlockExplode(EntityExplodeEvent e) {
    	for (Block b: e.blockList()) {
    		if (!(b.getType() == Material.COBBLESTONE)) {
    			e.setCancelled(true);
    		} else {
    			e.setYield(0);
    		}
    	}
		
	}

	public void onPlayerDeath(PlayerDeathEvent e) {
    	List<ItemStack> list = e.getDrops();
    	Iterator<ItemStack> i = list.iterator();
    	while(i.hasNext()) {
    	    ItemStack item = i.next();
    	    if (item.getType().equals(Material.RED_BANNER))
    	    	i.remove();
    	    	red.setFlagOut(false);
    	    if (item.getType().equals(Material.BLUE_BANNER))
    	        i.remove();
    	    	blue.setFlagOut(false);
    	}
		
	}
}
