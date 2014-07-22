package com.kill3rtaco.mineopoly.game.cards.actions;

import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;

public class MoveAction extends MineopolyCardAction {
	
	public MoveAction() {
		super("move", "i");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		player.move((Integer) params[0]);
		return CardResult.MOVE_RELATED;
	}
	
}
