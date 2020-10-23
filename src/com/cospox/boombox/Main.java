package com.cospox.boombox;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
/*
1) better colour indication
2) scoress
3) respawns?
4) if u die, delet the concrete
5) replenish wall
6) adjust rates
7) you can epearl out, fix
8) fishing rods
 * Something's broken with the generator chances
 * Flag detection thing show the num flags captured
 * wall regen fuel thing
 * Do the bloody terrian
 * Corner-to-corner teleports
 * Reset 
*/
public class Main extends JavaPlugin implements Listener {

	private Game game;
	public static World w;

    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	w = Bukkit.getWorlds().get(0);

    	Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_TILE_DROPS, false);
    	Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_MOB_SPAWNING, false);
    	Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_FIRE_TICK, false);
    	Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
    	Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_WEATHER_CYCLE, false);

    	//this.setupConfig();
    	this.game = new Game(w, this.getServer(), this);
    	this.setupSchedulers();
    	this.getServer().getPluginManager().registerEvents(this, this);
    }

    public void setupConfig() {
    	//this.getConfig().addDefault("world.lobbyspawn", new double[]{0.5, 4.0, 1000.5});
		//this.getConfig().addDefault("world.hubworld", "world_the_end");
		//this.saveDefaultConfig();
    }

    // Fired when plugin is disabled
    @Override
    public void onDisable() {

    }

    private void setupSchedulers() {
    	BukkitScheduler scheduler = this.getServer().getScheduler();

        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            //@Override
            public void run() {
                game.addResources(w);
                game.doTNTbuttons(w);
                game.doFlags(w);
            }
        }, 0L, 10L);

//        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
//            @Override
//            public void run() {
//                game.handlePlayers();
//            }
//        }, 0L, 10L);

        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            //@Override
            public void run() {
                game.countDown();
            }
        }, 0L, 20L);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	return this.game.onCommand(sender, cmd, label, args);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
    	e.getPlayer().sendMessage("Testing...");
    	this.game.onPlayerJoin(e);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
    	this.game.onPlayerQuit(e);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
    	this.game.onPlayerRespawn(e);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
    	this.game.onPlayerItemConsume(e);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
    	this.game.onBlockPlace(e);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
    	this.game.onBlockBreak(e);
    }

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent e) {
    	this.game.onBlockExplode(e);
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
    	this.game.onPlayerDeath(e);
    }

    //@EventHandler
    //public void onItemDrop(PlayerDropItemEvent e) {
    //	this.game.onItemDrop(e);
    //}
}
