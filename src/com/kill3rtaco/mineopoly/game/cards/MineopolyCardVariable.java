package com.kill3rtaco.mineopoly.game.cards;

import com.kill3rtaco.mineopoly.game.cards.replacemethods.CardDrawerReplaceMethod;
import com.kill3rtaco.mineopoly.game.cards.replacemethods.IdReplaceMethod;
import com.kill3rtaco.mineopoly.game.cards.replacemethods.MoneyReplaceMethod;
import com.kill3rtaco.mineopoly.game.cards.replacemethods.RandomDevReplaceMethod;
import com.kill3rtaco.mineopoly.game.cards.replacemethods.RandomPlayerReplaceMethod;
import com.kill3rtaco.mineopoly.game.cards.replacemethods.RandomSpaceReplaceMethod;

public enum MineopolyCardVariable {
	
	CARD_DRAWER(".*%cardDrawer.*", new CardDrawerReplaceMethod()),
	ID(".*%[0-9]+.*", new IdReplaceMethod()),
	MONEY("%money[0-9]+", new MoneyReplaceMethod()),
	RANDOM_DEV(".*%randomDev.*", new RandomDevReplaceMethod()),
	RANDOM_PLAYER(".*%randomPlayer.*", new RandomPlayerReplaceMethod()),
	RANDOM_SPACE(".*%randomSpace.*", new RandomSpaceReplaceMethod());
	
	private String					regex;
	private VariableReplaceMethod	replaceMethod;
	
	private MineopolyCardVariable(String regex, VariableReplaceMethod replaceMethod) {
		this.regex = regex;
		this.replaceMethod = replaceMethod;
	}
	
	public String getRegex() {
		return this.regex;
	}
	
	public VariableReplaceMethod getReplaceMethod() {
		return this.replaceMethod;
	}
	
}
