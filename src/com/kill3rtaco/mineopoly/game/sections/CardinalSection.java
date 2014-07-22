package com.kill3rtaco.mineopoly.game.sections;

import com.kill3rtaco.mineopoly.game.MineopolySection;

public abstract class CardinalSection extends MineopolySection {
	
	public CardinalSection(int id, String name, char color, SectionType type) {
		super(id, name, color, type);
	}
	
	public abstract int getSide();
	
	public boolean isTopBottom() {
		return getSide() % 2 == 0;
	}
	
	public boolean isLeftRight() {
		return !isTopBottom();
	}
	
}
