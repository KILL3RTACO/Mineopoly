package com.kill3rtaco.mineopoly.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.config.MineopolyBoardConfig;
import com.kill3rtaco.mineopoly.game.sections.CardinalSection;
import com.kill3rtaco.mineopoly.game.sections.SectionType;
import com.kill3rtaco.mineopoly.game.sections.SpecialSquare;
import com.kill3rtaco.mineopoly.game.sections.squares.FreeParkingSquare;
import com.kill3rtaco.mineopoly.game.sections.squares.GoSquare;
import com.kill3rtaco.mineopoly.game.sections.squares.GoToJailSquare;
import com.kill3rtaco.mineopoly.game.sections.squares.JailSquare;

/**
 * Represents any section on the board
 * @author Taco
 *
 */
public abstract class MineopolySection implements Comparable<MineopolySection> {
	
	private int					id;
	protected String			name;
	protected char				color;
	private SectionType			type;
	protected MineopolyBoard	board	= Mineopoly.plugin.getGame().getBoard();
	
	public MineopolySection(int id, String name, char color, SectionType type) {
		this.name = name;
		this.color = color;
		this.id = id;
		this.type = type;
	}
	
	public abstract void getInfo(Player player);
	
	public String getColorfulName() {
		return "&" + color + name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public char getDisplayColor() {
		return this.color;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public int getId() {
		return id;
	}
	
	//return new location each time to prevent modification
	public Location getLocation() {
		MineopolyBoardConfig board = Mineopoly.boardConfig;
		Location location = board.origin();
//		Location location = origin; //unneeded because origin() supplies a new Location anyway
		location.setX(location.getX() + 1);
		location.setZ(location.getZ() + 1); //get lower right corner of go square, not including border
		setPitchYaw(location);
		int sqLen = board.squareLength();
		int secLen = board.sectionLength();
		int secWid = board.sectionWidth();
		//any addition of 1 is to compensate for the borders between sections
		if(this instanceof CardinalSection) {
			CardinalSection section = (CardinalSection) this;
			if(section.getSide() == 0) {
				location.setX(location.getX() + sqLen + 1 + (secWid / 2D) + ((id - 1) * (secWid + 1)));
				location.setZ(location.getZ() + (secLen / 2D));
			} else if(section.getSide() == 1) {
				location.setX(location.getX() + (sqLen * 1.5) + 1 + (9 * (secWid + 1)));
				location.setZ(location.getZ() + sqLen + 1 + (secWid / 2D) + ((id - 11) * (secWid + 1)));
			} else if(section.getSide() == 2) {
				location.setX(location.getX() + sqLen + 1 + (secWid / 2D) + ((29 - id) * (secWid + 1)));
				location.setZ(location.getZ() + (sqLen * 1.5) + 1 + (9 * (secWid + 1)));
			} else {
				location.setX(location.getX() + (secLen / 2D));
				location.setZ(location.getZ() + sqLen + 1 + (secWid / 2D) + ((39 - id) * (secWid + 1)));
			}
		} else if(this instanceof SpecialSquare) {
			if(this instanceof GoSquare) {
				location.setX(location.getX() + (sqLen / 2D));
				location.setZ(location.getZ() + (sqLen / 2D));
			} else if(this instanceof JailSquare) {
				location.setX(location.getX() + (sqLen * 1.5) + 1 + (9 * (secWid + 1)));
				location.setZ(location.getZ() + (sqLen / 2D));
			} else if(this instanceof FreeParkingSquare) {
				location.setX(location.getX() + (sqLen * 1.5) + 1 + (9 * (secWid + 1)));
				location.setZ(location.getZ() + (sqLen * 1.5) + 1 + (9 * (secWid + 1)));
			} else if(this instanceof GoToJailSquare) {
				location.setX(location.getX() + (sqLen / 2D));
				location.setZ(location.getZ() + (sqLen * 1.5) + 1 + (9 * (secWid + 1)));
			}
		}
		return location;
	}
	
	public double[] outside(MineopolyPlayer player) {
		return outside(player.getLocation(), player.isJailed());
	}
	
	public double[] outside(Location loc) {
		return outside(loc, false);
	}
	
	//return null if inside, or new int[]{x, z} if outside. {x, z} is relative to the Location, not the section
	public double[] outside(Location loc, boolean jailed) {
		int secLen = Mineopoly.boardConfig.sectionLength();
		int secWidth = Mineopoly.boardConfig.sectionWidth();
		int sqLen = Mineopoly.boardConfig.squareLength();
		Location location = getLocation();
		if(this instanceof CardinalSection) {
			CardinalSection cs = (CardinalSection) this;
			if(cs.isTopBottom()) {
				double[] diffs = getDiffs(loc, location, secLen, secWidth);
				if(diffs[0] < 0 || diffs[1] < 0) {
					return diffs;
				}
			} else {
				double[] diffs = getDiffs(loc, location, secWidth, secLen);
				if(diffs[0] < 0 || diffs[1] < 0) {
					return diffs;
				}
			}
		} else {
			if(this instanceof JailSquare && jailed) {
				JailSquare jail = (JailSquare) this;
				double[] diffs = getDiffs(loc, jail.getJailCellLocation(), Mineopoly.boardConfig.getJailLength(), Mineopoly.boardConfig.getJailWidth());
				if(diffs[0] < 0 || diffs[1] < 0) {
					return diffs;
				}
			}
			double[] diffs = getDiffs(loc, location, sqLen, sqLen);
			if(diffs[0] < 0 || diffs[1] < 0) {
				return diffs;
			}
		}
		return null; //inside section
	}
	
	//pos - current location
	//loc - relative to
	//length/width - area allowed to be in
	//returns {x,z} difference, relative to the location 'loc'.
	//difference < 0 means that are out of permitted space
	private double[] getDiffs(Location pos, Location loc, int length, int width) {
		double[] diffs = new double[2];
		diffs[0] = (width / 2D) - (Math.abs(pos.getX() - loc.getX()));
		diffs[1] = (length / 2D) - (Math.abs(pos.getZ() - loc.getZ()));
		return diffs;
	}
	
	public Location getLocationRelative(double x, double y, double z) {
		return getLocationRelative(getLocation(), x, y, z);
	}
	
	//get a location relative to this section
	public Location getLocationRelative(Location loc, double x, double y, double z) {
		Location location = loc.clone();
		if(this instanceof CardinalSection) {
			CardinalSection c = (CardinalSection) this;
			int side = c.getSide();
			if(side == 0) {
				location.setX(location.getX() - x);
				location.setZ(location.getZ() + z);
			} else if(side == 1) {
				location.setX(location.getX() - z);
				location.setZ(location.getZ() - x);
			} else if(side == 2) {
				location.setX(location.getX() + x);
				location.setZ(location.getZ() - z);
			} else {
				location.setX(location.getX() + z);
				location.setZ(location.getZ() + x);
			}
		} else {
			location.setX(location.getX() - x);
			location.setZ(location.getZ() + z);
		}
		location.setY(location.getY() + y);
		return location;
	}
	
	private void setPitchYaw(Location location) {
		location.setPitch(0);
		if(this instanceof CardinalSection) {
			CardinalSection section = (CardinalSection) this;
			location.setYaw(270 + (section.getSide() * 90));
		} else if(this instanceof SpecialSquare) {
			SpecialSquare square = (SpecialSquare) this;
			if(square instanceof GoSquare) {
				location.setYaw(270);
			} else if(square instanceof JailSquare) {
				location.setYaw(0);
			} else if(square instanceof FreeParkingSquare) {
				location.setYaw(90);
			} else if(square instanceof GoToJailSquare) {
				location.setYaw(180);
			}
		}
	}
	
	public SectionType getType() {
		return type;
	}
	
	@Override
	public int compareTo(MineopolySection section) {
		return id - section.getId();
	}
}
