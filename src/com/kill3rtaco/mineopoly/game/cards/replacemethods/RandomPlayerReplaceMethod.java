package com.kill3rtaco.mineopoly.game.cards.replacemethods;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCard;
import com.kill3rtaco.mineopoly.game.cards.VariableReplaceMethod;

public class RandomPlayerReplaceMethod implements VariableReplaceMethod {
	
	@Override
	public String replace(String s, MineopolyCard card) {
		MineopolyPlayer random = Mineopoly.plugin.getGame().getBoard().getRandomPlayerNotCurrent();
		return s.replaceAll("%randomPlayer", random.getName());
	}
	
}
