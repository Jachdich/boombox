package com.cospox.boombox;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Generator {
	public static void generate(World w) {
		addLootToLocation(w, Loot.normalLootLocations, Loot.normalLoot, Loot.normalLootChances);
		addLootToLocation(w, Loot.middleLootLocations, Loot.middleLoot, Loot.middleLootChances);
		
		addLootToLocation(w, Loot.strongholdLootLocations, Loot.strongholdLoot, Loot.strongholdLootChances);
		addLootToLocation(w, Loot.netherLootLocations, Loot.netherLoot, Loot.netherLootChances);
		addLootToLocation(w, Loot.endLootLocations, Loot.endLoot, Loot.endLootChances);
		addLootToLocation(w, Loot.mansionLootLocations, Loot.mansionLoot, Loot.mansionLootChances);
    }
	
	private static void addLootToLocation(World w, Location[] locs, ItemStack[] loot, Float[] chances) {
		for (Location l: locs) {
			//Location l = locs[new SplittableRandom().nextInt(Loot.normalLootLocations.length)];
			Location loc = new Location(w, l.getX(), l.getY(), l.getZ());
			for (int i = 0; i < loot.length; i++) {
				ItemStack m = loot[i];
				double rand = Math.random();
				if (rand <= chances[i]) {
					Block b = w.getBlockAt(loc);
					BlockState state = b.getState();
	
	                if (state instanceof Container) {
	                    Container cont = (Container) state;
	                    Inventory inv = cont.getInventory();
	                    inv.addItem(m);
	                }
				}
			}
		}
	}
	
	public static void reset(World w) {
		for (Location l: Loot.normalLootLocations) {
			Location loc = new Location(w, l.getX(), l.getY(), l.getZ());
			resetLocation(loc);
		}
		
		for (Location l: Loot.middleLootLocations) {
			Location loc = new Location(w, l.getX(), l.getY(), l.getZ());
			resetLocation(loc);
		}
		
		for (Location l: Loot.endLootLocations) {
			Location loc = new Location(w, l.getX(), l.getY(), l.getZ());
			resetLocation(loc);
		}
		for (Location l: Loot.mansionLootLocations) {
			Location loc = new Location(w, l.getX(), l.getY(), l.getZ());
			resetLocation(loc);
		}
		for (Location l: Loot.strongholdLootLocations) {
			Location loc = new Location(w, l.getX(), l.getY(), l.getZ());
			resetLocation(loc);
		}
		for (Location l: Loot.netherLootLocations) {
			Location loc = new Location(w, l.getX(), l.getY(), l.getZ());
			resetLocation(loc);
		}
		
		
	}
	
	private static void resetLocation(Location l) {
		Block b = l.getWorld().getBlockAt(l);
		BlockState state = b.getState();

        if (state instanceof Container) {
            Container cont = (Container) state;
            Inventory inv = cont.getInventory();
            inv.clear();
        }
	}
	
	/*
	private static void addLootToLocation(World w, Location[] locs, ItemStack[] loot, HashMap<Material, Float> chances) {
		for (Location l: locs) {
			//Location l = locs[new SplittableRandom().nextInt(Loot.normalLootLocations.length)];
			Location loc = new Location(w, l.getX(), l.getY(), l.getZ());
			for (ItemStack m: loot) {
				double rand = new SplittableRandom().nextInt(0, 100) / 100.0;
				if (rand <= chances.get(m)) {
					Block b = w.getBlockAt(loc);
					BlockState state = b.getState();
	
	                if (state instanceof Container) {
	                    Container cont = (Container) state;
	                    Inventory inv = cont.getInventory();
	                    inv.addItem(m);
	                }
				}
			}
		}
	}*/
}
