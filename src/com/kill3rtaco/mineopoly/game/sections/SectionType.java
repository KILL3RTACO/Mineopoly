package com.kill3rtaco.mineopoly.game.sections;

public enum SectionType {
	
	CHANCE, //3
	COMMUNITY_CHEST, //3
	PROPERTY, //22
	SQUARE, //4
	RAILROAD, //4 
	TAX, //2
	UTILITY; //2
	
	@Override
	public String toString() {
		return this.name();
	}
}
