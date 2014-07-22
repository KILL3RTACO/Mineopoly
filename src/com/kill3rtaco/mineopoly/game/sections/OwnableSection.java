package com.kill3rtaco.mineopoly.game.sections;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.tacoapi.TacoAPI;

public abstract class OwnableSection extends CardinalSection {
	
	protected MineopolyPlayer	owner;
	protected boolean			owned, mortgaged;
	protected int				price;
	
	public OwnableSection(int id, String pathToName, char color, int price, SectionType type) {
		super(id, Mineopoly.names.getString(pathToName), color, type);
		this.price = price;
	}
	
	public boolean isOwned() {
		return owned;
	}
	
	public boolean isMortgaged() {
		return mortgaged;
	}
	
	public void setMortgaged(boolean mortgage) {
		this.mortgaged = mortgage;
	}
	
	public MineopolyPlayer getOwner() {
		return owner;
	}
	
	public Location getOwnershipPieceLocation() {
		return getLocationRelative(Mineopoly.boardConfig.getOwnershipPieceX(), 0, Mineopoly.boardConfig.getOwnershipPieceZ());
	}
	
	public void setOwner(MineopolyPlayer player) {
		this.owner = player;
		this.owned = (player != null);
		if(player != null)
			player.addSection(this);
		addOwnershipPiece();
	}
	
	public void addOwnershipPiece() {
		Location opLocation = getOwnershipPieceLocation();
		Block b = Mineopoly.boardConfig.origin().getWorld().getBlockAt(opLocation);
		if(!owned) {
			b.setType(Material.AIR);
			return;
		}
		int rotation = getSide() * 4;
		String blockId = "skull:1|" + getOwner().getName() + "|" + rotation; // skull:1|name|rotation - WE syntax
		String world = Mineopoly.boardConfig.origin().getWorld().getName();
		TacoAPI.getWorldEditAPI().setAreaWithBlock(world, opLocation, opLocation, blockId);
	}
	
	public abstract int getRent();
	
	public int getPrice() {
		return price;
	}
	
	public void reset() {
		setOwner(null);
	}
	
}
