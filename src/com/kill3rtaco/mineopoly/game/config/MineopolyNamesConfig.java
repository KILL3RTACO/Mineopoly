package com.kill3rtaco.mineopoly.game.config;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.tacoapi.api.TacoConfig;

public class MineopolyNamesConfig extends TacoConfig {
	
	public MineopolyNamesConfig() {
		super(new File(Mineopoly.plugin.getDataFolder() + "/config/" + Mineopoly.config.getConfigName() + "/names.yml"));
	}
	
	@Override
	protected void setDefaults() {
		addDefaultValue(MineopolyConstants.N_PURPLE_1, "Nether Ave");
		addDefaultValue(MineopolyConstants.N_PURPLE_2, "Slimey Street");
		
		addDefaultValue(MineopolyConstants.N_LIGHT_BLUE_1, "Temple Road");
		addDefaultValue(MineopolyConstants.N_LIGHT_BLUE_2, "Stronghold Lane");
		addDefaultValue(MineopolyConstants.N_LIGHT_BLUE_3, "Dungeon Ave");
		
		addDefaultValue(MineopolyConstants.N_MAGENTA_1, "St. Blaze Place");
		addDefaultValue(MineopolyConstants.N_MAGENTA_2, "Zombe Gardens");
		addDefaultValue(MineopolyConstants.N_MAGENTA_3, "Spider Lane");
		
		addDefaultValue(MineopolyConstants.N_ORANGE_1, "Enderman Place");
		addDefaultValue(MineopolyConstants.N_ORANGE_2, "Notch Place");
		addDefaultValue(MineopolyConstants.N_ORANGE_3, "Skeleton Ave");
		
		addDefaultValue(MineopolyConstants.N_RED_1, "Bed Place");
		addDefaultValue(MineopolyConstants.N_RED_2, "Bow and Arrow Place");
		addDefaultValue(MineopolyConstants.N_RED_3, "Wolf Lane");
		
		addDefaultValue(MineopolyConstants.N_YELLOW_1, "Good Street");
		addDefaultValue(MineopolyConstants.N_YELLOW_2, "Bad Street");
		addDefaultValue(MineopolyConstants.N_YELLOW_3, "Ugly Street");
		
		addDefaultValue(MineopolyConstants.N_GREEN_1, "Cookie Drive");
		addDefaultValue(MineopolyConstants.N_GREEN_2, "Watermelon Ave");
		addDefaultValue(MineopolyConstants.N_GREEN_3, "Pumpkin Road");
		
		addDefaultValue(MineopolyConstants.N_BLUE_1, "Harbor Ave");
		addDefaultValue(MineopolyConstants.N_BLUE_2, "Marshmellow Drive");
		
		addDefaultValue(MineopolyConstants.N_RR_1, "Enchanted Railroad");
		addDefaultValue(MineopolyConstants.N_RR_2, "Creeper Railroad");
		addDefaultValue(MineopolyConstants.N_RR_3, "TNT Railroad");
		addDefaultValue(MineopolyConstants.N_RR_4, "The End Railroad");
		
		addDefaultValue(MineopolyConstants.N_UTILITY_ELECTRIC, "Redstone Company");
		addDefaultValue(MineopolyConstants.N_UTILITY_WATER, "Water Company");
		
		addDefaultValue(MineopolyConstants.N_TAX_INCOME, "Income Tax");
		addDefaultValue(MineopolyConstants.N_TAX_LUXURY, "Luxury Tax");
		
		addDefaultValue(MineopolyConstants.N_GO, "Go");
		addDefaultValue(MineopolyConstants.N_JAIL, "Jail/Just Visiting");
		addDefaultValue(MineopolyConstants.N_FREE_PARKING, "Free Parking");
		addDefaultValue(MineopolyConstants.N_GO_TO_JAIL, "Go to Jail");
	}
	
	@Override
	public void reload() {
		config = YamlConfiguration.loadConfiguration(new File(Mineopoly.plugin.getDataFolder() + "/config/" + Mineopoly.config.getConfigName() + "/names.yml"));
		setDefaults();
		save();
	}
	
}
