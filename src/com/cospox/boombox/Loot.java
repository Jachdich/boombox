package com.cospox.boombox;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class Loot {
	private static float stoneChance = 0.0003f;
	public static ItemStack[] normalLoot     = {new ItemStack(Material.TNT, 1),
											    new ItemStack(Material.CARROT, 1)};
	
	public static ItemStack[] endLoot        = {new ItemStack(Material.ENDER_PEARL, 1),
			 								    new ItemStack(Material.CHORUS_FRUIT, 1),
			 								    new ItemStack(Material.STONE, 1)};
	
	public static ItemStack[] mansionLoot    = {new ItemStack(Material.TOTEM_OF_UNDYING, 1),
			 								    new ItemStack(Material.CROSSBOW, 1),
			 								    genFirework(),
			 								    new ItemStack(Material.STONE, 1)};
	
	public static ItemStack[] strongholdLoot = {genEnchItem(Material.IRON_BOOTS, Enchantment.DEPTH_STRIDER, 1),
												new ItemStack(Material.STONE, 1)};
	
	public static ItemStack[] netherLoot     = {genPotion(PotionType.SPEED, 2, false, false),
											    genPotion(PotionType.INVISIBILITY, 1, false, false),
											    new ItemStack(Material.STONE, 1)};
	
	public static ItemStack[] middleLoot = normalLoot;
	
	public static Float[] middleLootChances =     {0.07f, 0.04f};
	public static Float[] normalLootChances =     {0.03f, 0.008f};
	
	public static Float[] mansionLootChances =    {0.001f, 0.003f, 0.04f, stoneChance};
	public static Float[] netherLootChances =     {0.003f, 0.003f,        stoneChance};
	public static Float[] strongholdLootChances = {0.002f,                stoneChance};
	public static Float[] endLootChances =        {0.02f, 0.02f,          stoneChance};
	
	public static Location[] middleLootLocations = {new Location(null, -21, 8, 8)};
	public static Location[] normalLootLocations = {new Location(null, -11, 9, -6),
			new Location(null, -16, 9, -7),
			new Location(null, -26, 9, -7),
			new Location(null, -31, 9, -10),
			new Location(null, -31, 9, -5),
			new Location(null, -26, 9, -2),
			new Location(null, -16, 9, -2),
			new Location(null, -11, 9, -1),
			new Location(null, -11, 9, 3),
			new Location(null, -13, 9, 3),
			new Location(null, -29, 9, 3),
			new Location(null, -31, 9, 3),
			new Location(null, -11, 9, 13),
			new Location(null, -13, 9, 13),
			new Location(null, -29, 9, 13),
			new Location(null, -31, 9, 13),
			new Location(null, -31, 9, 19),
			new Location(null, -26, 9, 18),
			new Location(null, -16, 9, 18),
			new Location(null, -11, 9, 18),
			new Location(null, -11, 9, 26),
			new Location(null, -16, 9, 23),
			new Location(null, -26, 9, 23),
			new Location(null, -31, 9, 25),
			new Location(null, -34, 9, 25)};
			public static Location[] netherLootLocations = {new Location(null, -3, 10, -2),
			new Location(null, -3, 10, -8),
			new Location(null, -6, 10, -14),
			new Location(null, -12, 10, -14)};
			public static Location[] strongholdLootLocations = {new Location(null, -3, 10, 19),
			new Location(null, -3, 10, 26),
			new Location(null, -8, 10, 30),
			new Location(null, -12, 10, 30)};
			public static Location[] mansionLootLocations = {new Location(null, -31, 10, 30),
			new Location(null, -36, 10, 30),
			new Location(null, -39, 10, 25),
			new Location(null, -39, 10, 19)};
			public static Location[] endLootLocations = {new Location(null, -33, 10, -15),
			new Location(null, -37, 10, -15),
			new Location(null, -39, 10, -11),
			new Location(null, -39, 10, -4)};
	
	public static ItemStack genPotion(PotionType type, int level, boolean extend, boolean upgraded){
        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionData(new PotionData(type, extend, upgraded));
        potion.setItemMeta(meta);
        return potion;
    }
	
	public static ItemStack genEnchItem(Material item, Enchantment x, int level) {
		ItemStack enchItem = new ItemStack(item, 1);
		ItemMeta meta = enchItem.getItemMeta();
		meta.addEnchant(x, level, true);
		enchItem.setItemMeta(meta);
		return enchItem;
	}
	
	public static ItemStack genFirework() {
		ItemStack i = new ItemStack(Material.FIREWORK_ROCKET, 1);
        FireworkMeta fm = (FireworkMeta) i.getItemMeta();
        List<Color> c = new ArrayList<Color>();
            c.add(Color.BLUE);
            c.add(Color.LIME);
        FireworkEffect e = FireworkEffect.builder().flicker(true).withColor(c).withFade(c).with(Type.BALL_LARGE).trail(true).build();
        fm.addEffect(e);
        fm.setPower(3);
        i.setItemMeta(fm);
        return i;
	}
}
