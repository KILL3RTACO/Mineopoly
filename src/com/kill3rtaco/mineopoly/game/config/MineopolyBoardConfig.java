package com.kill3rtaco.mineopoly.game.config;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.World;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.sections.Property;
import com.kill3rtaco.tacoapi.api.TacoConfig;

public class MineopolyBoardConfig extends TacoConfig {
	
	private String	name;
	
	public MineopolyBoardConfig() {
		this(Mineopoly.config.getBoardName());
	}
	
	public MineopolyBoardConfig(String name) {
		super(new File(Mineopoly.plugin.getDataFolder() + "/boards/" + name + "/board.yml"));
		this.name = name;
	}
	
	@Override
	protected void setDefaults() {
		addDefaultValue(MineopolyConstants.C_SCHEMA_WORLD, "world");
		addDefaultValue(MineopolyConstants.C_SCHEMA_NEEDS_PASTE, true);
		addDefaultValue(MineopolyConstants.C_SCHEMA_ORIGIN_X, 0);
		addDefaultValue(MineopolyConstants.C_SCHEMA_ORIGIN_Y, 0);
		addDefaultValue(MineopolyConstants.C_SCHEMA_ORIGIN_Z, 0);
		addDefaultValue(MineopolyConstants.C_BOARD_SECTION_LENGTH, 23);
		addDefaultValue(MineopolyConstants.C_BOARD_SECTION_WIDTH, 13);
		addDefaultValue(MineopolyConstants.C_JAIL_LENGTH, 16);
		addDefaultValue(MineopolyConstants.C_JAIL_WIDTH, 16);
		addDefaultValue(MineopolyConstants.C_OWNERSHIP_PIECE_X, 0);
		addDefaultValue(MineopolyConstants.C_OWNERSHIP_PIECE_Z, -1);
	}
	
	//return new location each time to prevent modification
	public Location origin() {
		World mWorld = Mineopoly.plugin.getServer().getWorld(getString(MineopolyConstants.C_SCHEMA_WORLD));
		return new Location(mWorld, schematicX(), schematicY(), schematicZ());
	}
	
	public int schematicX() {
		return getInt(MineopolyConstants.C_SCHEMA_ORIGIN_X);
	}
	
	public int schematicY() {
		return getInt(MineopolyConstants.C_SCHEMA_ORIGIN_Y);
	}
	
	public int schematicZ() {
		return getInt(MineopolyConstants.C_SCHEMA_ORIGIN_Z);
	}
	
	public void setBoardOrigin(Location location) {
		set(MineopolyConstants.C_SCHEMA_WORLD, location.getWorld().getName());
		set(MineopolyConstants.C_SCHEMA_ORIGIN_X, location.getBlockX());
		set(MineopolyConstants.C_SCHEMA_ORIGIN_Y, location.getBlockY());
		set(MineopolyConstants.C_SCHEMA_ORIGIN_Z, location.getBlockZ());
		save();
		reload();
	}
	
	public int getOwnershipPieceX() {
		return getInt(MineopolyConstants.C_OWNERSHIP_PIECE_X);
	}
	
	public int getOwnershipPieceZ() {
		return getInt(MineopolyConstants.C_OWNERSHIP_PIECE_Z);
		
	}
	
	public int getTOPX() {
		return getInt("top.x");
	}
	
	public int getTOPZ() {
		return getInt("top.z");
	}
	
	public boolean needsPaste() {
		return getBoolean(MineopolyConstants.C_BOARD_NEEDS_PASTE);
	}
	
	public int sectionLength() {
		return getInt(MineopolyConstants.C_BOARD_SECTION_LENGTH);
	}
	
	public int sectionWidth() {
		return getInt(MineopolyConstants.C_BOARD_SECTION_WIDTH);
	}
	
	public int squareLength() {
		return sectionLength();
	}
	
	public Location getHouseMin(Property prop, int house) {
		int minX = getInt(MineopolyConstants.C_BOARD_HOUSE_MIN_X.replace("%house", house + ""));
		int minY = getInt(MineopolyConstants.C_BOARD_HOUSE_MIN_Y.replace("%house", house + ""));
		int minZ = getInt(MineopolyConstants.C_BOARD_HOUSE_MIN_Z.replace("%house", house + ""));
		return prop.getLocationRelative(minX, minY, minZ);
	}
	
	public Location getHouseMax(Property prop, int house) {
		int maxX = getInt(MineopolyConstants.C_BOARD_HOUSE_MAX_X.replace("%house", house + ""));
		int maxY = getInt(MineopolyConstants.C_BOARD_HOUSE_MAX_Y.replace("%house", house + ""));
		int maxZ = getInt(MineopolyConstants.C_BOARD_HOUSE_MAX_Z.replace("%house", house + ""));
		return prop.getLocationRelative(maxX, maxY, maxZ);
	}
	
	public Location getHotelMin(Property prop) {
		int minX = getInt(MineopolyConstants.C_BOARD_HOTEL_MIN_X);
		int minY = getInt(MineopolyConstants.C_BOARD_HOTEL_MIN_Y);
		int minZ = getInt(MineopolyConstants.C_BOARD_HOTEL_MIN_Z);
		return prop.getLocationRelative(minX, minY, minZ);
	}
	
	public Location getHotelMax(Property prop) {
		int maxX = getInt(MineopolyConstants.C_BOARD_HOTEL_MAX_X);
		int maxY = getInt(MineopolyConstants.C_BOARD_HOTEL_MAX_Y);
		int maxZ = getInt(MineopolyConstants.C_BOARD_HOTEL_MAX_Z);
		return prop.getLocationRelative(maxX, maxY, maxZ);
	}
	
	public int getJailLength() {
		return getInt(MineopolyConstants.C_JAIL_LENGTH);
	}
	
	public int getJailWidth() {
		return getInt(MineopolyConstants.C_JAIL_WIDTH);
	}
	
	public Location getJailLocationOffset(boolean jailed) {
		Location location = new Location(origin().getWorld(), 0, 0, 0);
		if(jailed) {
			location.setX(getDouble(MineopolyConstants.C_BOARD_JAIL_SPAWN_X));
			location.setZ(getDouble(MineopolyConstants.C_BOARD_JAIL_SPAWN_Z));
		} else {
			location.setX(getDouble(MineopolyConstants.C_BOARD_VISITING_SPAWN_X));
			location.setZ(getDouble(MineopolyConstants.C_BOARD_VISITING_SPAWN_Z));
		}
		return location;
	}
	
	public int size() {
		int size = 1;
		size += 2 * (squareLength() + 1); //2 squares on each side
		size += 9 * (sectionWidth() + 1); //9 sections on each side
		return size;
	}
	
	public String getSchematicLocation() {
		return Mineopoly.plugin.getDataFolder() + "/boards/" + name + "/board.schematic";
	}
	
}
