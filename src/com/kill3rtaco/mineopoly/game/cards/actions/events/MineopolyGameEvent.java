package com.kill3rtaco.mineopoly.game.cards.actions.events;

import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCard;

/*
 * An event is like a mini-game. Chance or Community Chest cards can trigger them.
 */
public abstract class MineopolyGameEvent {
	
	protected int				_prize;
	protected MineopolyPlayer	_starter;
	
	public MineopolyGameEvent(MineopolyPlayer starter, int prize) {
		_prize = prize;
		_starter = starter;
	}
	
	public abstract void start();
	
	public void end() {
		MineopolyCard.eventInProgess = false;
		_starter.endTurnAutomatically();
	}
	
}
