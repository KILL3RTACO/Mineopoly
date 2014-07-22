package com.kill3rtaco.mineopoly.game.cards.actions;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardAction;

public class MoveToAction extends MineopolyCardAction {
	
	public MoveToAction() {
		super("moveto", "i");
	}
	
	@Override
	public CardResult doAction(MineopolyPlayer player, Object... params) {
		player.setCurrentSection(Mineopoly.plugin.getGame().getBoard().getSection((Integer) params[0]));
		return CardResult.MOVE_RELATED;
	}
	
}
