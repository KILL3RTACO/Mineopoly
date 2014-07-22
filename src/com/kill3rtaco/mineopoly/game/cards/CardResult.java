package com.kill3rtaco.mineopoly.game.cards;

public enum CardResult {
	
	CUSTOM, //multiple things done, currently not used
	EVENT_RELATED,
	LOSE_TURN,
	MONEY_RELATED,
	MOVE_RELATED;
	
	@Override
	public String toString() {
		return name();
	}
	
}
