package com.kill3rtaco.mineopoly.game.cards.replacemethods;

import java.util.Random;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCard;
import com.kill3rtaco.mineopoly.game.cards.VariableReplaceMethod;

public class RandomDevReplaceMethod implements VariableReplaceMethod {
	
	private String[]	devs	= new String[]{"jeb_", "notch", "Dinnerbone",
								"EvilSeph"};
	
	@Override
	public String replace(String s, MineopolyCard card) {
		String exclude = Mineopoly.plugin.getGame().getPlayerWithCurrentTurn().getName();
		String dev = getRandomDev(exclude);
		return s.replaceAll("%randomDev", dev);
	}
	
	public String getRandomDev(String exclude) {
		Random random = new Random();
		String dev = devs[random.nextInt(devs.length)];
		if(dev.equalsIgnoreCase(exclude)) {
			return getRandomDev(exclude);
		}
		return dev;
	}
	
}
