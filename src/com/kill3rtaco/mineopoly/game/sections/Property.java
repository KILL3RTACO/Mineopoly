package com.kill3rtaco.mineopoly.game.sections;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyColor;
import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.obj.WorldEditObject;

/**
 * Represents a property space on the board. Implements {@link taco.mineopoly.sections.Ownable Ownable}
 * @author Taco
 *
 */
public class Property extends OwnableSection {
	
	protected int				side;
	private int					housePrice	= (getSide() + 1) * 50;
	private int					hotelPrice	= housePrice * 5;
	private int					houses;
	private int					r, h, h2, h3, h4, hotel;
	protected boolean			hasHotel	= false;
	protected MineopolyColor	mColor;
	
	public Property(int id, String pathToName, MineopolyColor color, int side, int price, int[] rent) {
		super(id, pathToName, color.getChar(), price, SectionType.PROPERTY);
		this.mColor = color;
		this.side = side;
		setRent(rent[0], rent[1], rent[2], rent[3], rent[4], rent[5]);
	}
	
	@Override
	public int getRent() {
		if(isOwned()) {
			return getRent(houses, hasHotel);
		} else
			return 0;
	}
	
	public int getRent(int houses) {
		return getRent(houses, false);
	}
	
	public int getRent(int houses, boolean hotel) {
		if(hotel)
			return this.hotel;
		switch(houses) {
			case 0:
				return r;
			case 1:
				return h;
			case 2:
				return h2;
			case 3:
				return h3;
			default:
				return h4;
		}
	}
	
	@Override
	public void reset() {
		this.mortgaged = false;
		this.houses = 0;
		this.owned = false;
		this.hasHotel = false;
	}
	
	public MineopolyColor getColor() {
		return mColor;
	}
	
	protected void setRent(int r, int h, int h2, int h3, int h4, int hotel) {
		this.r = r;
		this.h = h;
		this.h2 = h2;
		this.h3 = h3;
		this.h4 = h4;
		this.hotel = hotel;
	}
	
	@Override
	public void getInfo(Player player) {
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&6---[" + getColorfulName() + "&b(&3" + getId() + "&b)&6]---");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, mColor + "Owner&7:&b " + (isOwned() ? owner.getName() : "none"));
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, mColor + (isOwned() ? "Rent&7: &2" + MineopolyConstants.money(getRent()) : "Price&7: &2" + MineopolyConstants.money(getPrice())));
		if(getHouses() > 0 && !hasHotel())
			Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, mColor + "Houses&7:&b " + getHouses());
		if(hasHotel)
			Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, mColor + "Hotel&7:&b " + hasHotel());
	}
	
	public boolean hasHotel() {
		return this.hasHotel;
	}
	
	public int getHouses() {
		return this.houses;
	}
	
	@Override
	public void setMortgaged(boolean mortgaged) {
		this.mortgaged = mortgaged;
		if(mortgaged) {
			int refund = 0;
			for(Property p : owner.getPropertiesInMonopoly(mColor)) {
				if(!p.getName().equalsIgnoreCase(getName())) {
					if(p.getHouses() == 0 && !p.hasHotel) {
						break;
					} else {
						refund += (p.getHousePrice() * p.getHouses()) / 2;
						refund += ((p.hasHotel() ? 1 : 0) * p.getHotelPrice()) / 2;
						p.removeAllHouses();
						p.removeHotel();
					}
				}
			}
			owner.addMoney(refund);
		}
	}
	
	public int getHousePrice() {
		return this.housePrice;
	}
	
	public int getHotelPrice() {
		return this.hotelPrice;
	}
	
	public boolean canAddHouse() {
		if(owner == null || houses >= 4 || hasHotel)
			return false;
		if(Mineopoly.houseRules.improvementRequiresMonopoly()) {
			if(!owner.hasMonopoly(mColor))
				return false;
			ArrayList<Property> props = owner.getPropertiesInMonopoly(mColor);
			if(Mineopoly.houseRules.houseSycronization()) {
				int[] allHouses = new int[props.size()];
				int index = -1;
				for(int i = 0; i < props.size(); i++) {
					allHouses[i] = props.get(i).getHouses();
					if(props.get(i).getId() == getId())
						index = i;
				}
				int value = allHouses[0];
				boolean same = false;
				for(int i : allHouses) {
					same = i == value;
				}
				if(same) {
					return true;
				} else {
					allHouses[index] += 1;
					for(int i : allHouses) {
						same = i == value;
					}
					return same;
				}
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	public boolean canAddHotel() {
		if(owner == null || houses < 4)
			return false;
		if(Mineopoly.houseRules.improvementRequiresMonopoly()) {
			if(!owner.hasMonopoly(mColor))
				return false;
			if(Mineopoly.houseRules.houseSycronization()) {
				for(Property p : owner.getPropertiesInMonopoly(mColor)) {
					if(!p.hasHotel() && p.getHouses() < 4) {
						return false;
					}
				}
				return true;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	public void addHotel() {
		addHotel(true);
	}
	
	public void addHotel(boolean takeMoney) {
		if(takeMoney)
			owner.takeMoney(hotelPrice);
		this.hasHotel = true;
		this.houses = 0;
		updateVisibleImprovements();
	}
	
	public void addHouse() {
		addHouse(true);
	}
	
	public void addHouse(boolean takeMoney) {
		if(takeMoney)
			owner.takeMoney(housePrice);
		this.houses += 1;
		updateVisibleImprovements();
	}
	
	public void setHouses(int houses) {
		this.houses = houses;
		updateVisibleImprovements();
	}
	
	public void setHasHotel(boolean hasHotel) {
		this.hasHotel = hasHotel;
		updateVisibleImprovements();
	}
	
	public void removeHouse() {
		houses--;
		owner.addMoney(housePrice / 2);
		updateVisibleImprovements();
	}
	
	public void removeAllHouses() {
		owner.addMoney(houses / 2);
		houses = 0;
		updateVisibleImprovements();
	}
	
	public void removeHotel() {
		hasHotel = false;
		houses = 4;
		owner.addMoney(hotelPrice / 2);
		updateVisibleImprovements();
	}
	
	@Override
	public int getSide() {
		return side;
	}
	
	public void clearImprovements() {
		hasHotel = false;
		houses = 0;
		updateVisibleImprovements();
	}
	
	public void updateVisibleImprovements() {
		//just to be safe
		if(!TacoAPI.isWorldEditAPIOnline())
			return;
		clearVisibleImprovements();
		if(Mineopoly.config.ahEdition()) {
			updateTower();
			return;
		}
		if(hasHotel) {
			Location corner = Mineopoly.boardConfig.getHotelMin(this);
			Location corner2 = Mineopoly.boardConfig.getHotelMax(this);
			TacoAPI.getWorldEditAPI().setAreaWithBlock(corner.getWorld().getName(), corner, corner2, "red");
		} else {
			for(int i = 1; i <= houses; i++) {
				Location corner = Mineopoly.boardConfig.getHouseMin(this, i);
				Location corner2 = Mineopoly.boardConfig.getHouseMax(this, i);
				TacoAPI.getWorldEditAPI().setAreaWithBlock(corner.getWorld().getName(), corner, corner2, "green");
			}
		}
		addOwnershipPiece();
	}
	
	public void clearVisibleImprovements() {
		WorldEditObject we = TacoAPI.getWorldEditAPI();
		Location corner, corner2;
		String world = getLocation().getWorld().getName();
		int secLen = Mineopoly.boardConfig.sectionLength();
		int secWidth = Mineopoly.boardConfig.sectionWidth();
		double maxY = Mineopoly.boardConfig.getHotelMax(this).getY();
		if(Mineopoly.config.ahEdition() && maxY < 4)
			maxY = 4;
		corner = getLocationRelative((secWidth / 2) * -1, 0, (secLen / 2) * -1);
		corner2 = getLocationRelative(secWidth / 2, maxY, secLen / 2);
		we.setAreaWithBlock(world, corner, corner2, "air");
	}
	
	//AH Edition
	private void updateTower() {
		int counter = (hasHotel ? 4 : houses);
		if(counter == 0)
			return;
		for(int i = 0; i < counter; i++) {
			Material mat = (i == 0 ? Material.OBSIDIAN : Material.GOLD_BLOCK); //determine material
			Location topLocation = getLocationRelative(Mineopoly.boardConfig.getTOPX(), 0, Mineopoly.boardConfig.getTOPZ());
			topLocation = topLocation.add(0, i, 0);
			String world = topLocation.getWorld().getName();
			TacoAPI.getWorldEditAPI().setAreaWithBlock(world, topLocation, topLocation, mat.name());
			if(i == 3 && hasHotel) {
				topLocation = topLocation.add(0, 1, 0);
				TacoAPI.getWorldEditAPI().setAreaWithBlock(world, topLocation, topLocation, "gold_block");
			}
		}
		addOwnershipPiece();
	}
}
