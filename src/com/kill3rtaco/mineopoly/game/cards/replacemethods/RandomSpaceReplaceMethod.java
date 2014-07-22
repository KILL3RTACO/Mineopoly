package com.kill3rtaco.mineopoly.game.cards.replacemethods;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolySection;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCard;
import com.kill3rtaco.mineopoly.game.cards.VariableReplaceMethod;

public class RandomSpaceReplaceMethod implements VariableReplaceMethod {
	
	@Override
	public String replace(String s, MineopolyCard card) {
		MineopolySection random = Mineopoly.plugin.getGame().getBoard().getRandomSectionNotCurrent();
		return s.replaceAll("%randomSpace", random.getColorfulName());
	}
	
}
