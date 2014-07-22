package com.kill3rtaco.mineopoly.game.cards;

import java.util.ArrayList;

import com.kill3rtaco.mineopoly.game.cards.actions.EventAction;
import com.kill3rtaco.mineopoly.game.cards.actions.GiveAction;
import com.kill3rtaco.mineopoly.game.cards.actions.GiveAllAction;
import com.kill3rtaco.mineopoly.game.cards.actions.GiveRestAction;
import com.kill3rtaco.mineopoly.game.cards.actions.JailAction;
import com.kill3rtaco.mineopoly.game.cards.actions.LoseTurnAction;
import com.kill3rtaco.mineopoly.game.cards.actions.MoveAction;
import com.kill3rtaco.mineopoly.game.cards.actions.MoveNearestAction;
import com.kill3rtaco.mineopoly.game.cards.actions.MoveToAction;
import com.kill3rtaco.mineopoly.game.cards.actions.PayAllAction;
import com.kill3rtaco.mineopoly.game.cards.actions.PayPlayerAction;
import com.kill3rtaco.mineopoly.game.cards.actions.PayPotAction;
import com.kill3rtaco.mineopoly.game.cards.actions.PayPotAllAction;
import com.kill3rtaco.mineopoly.game.cards.actions.PayPotRestAction;
import com.kill3rtaco.mineopoly.game.cards.actions.RepairsAction;
import com.kill3rtaco.mineopoly.game.cards.actions.TakeAction;
import com.kill3rtaco.mineopoly.game.cards.actions.TakeAllAction;
import com.kill3rtaco.mineopoly.game.cards.actions.TakeRestAction;

public class MineopolyCardActionManager {
	
	private static ArrayList<MineopolyCardAction>	actions;
	
	static {
		actions = new ArrayList<MineopolyCardAction>();
		actions.add(new EventAction());
		actions.add(new GiveAction());
		actions.add(new GiveAllAction());
		actions.add(new GiveRestAction());
		actions.add(new JailAction());
		actions.add(new LoseTurnAction());
		actions.add(new MoveAction());
		actions.add(new MoveNearestAction());
		actions.add(new MoveToAction());
		actions.add(new PayAllAction());
		actions.add(new PayPlayerAction());
		actions.add(new PayPotAction());
		actions.add(new PayPotAllAction());
		actions.add(new PayPotRestAction());
		actions.add(new RepairsAction());
		actions.add(new TakeAction());
		actions.add(new TakeAllAction());
		actions.add(new TakeRestAction());
	}
	
	public static ArrayList<MineopolyCardAction> getActions() {
		return actions;
	}
	
	public static MineopolyCardAction getAction(String name) {
		for(MineopolyCardAction a : actions) {
			if(a.getName().equalsIgnoreCase(name)) {
				return a;
			}
		}
		return null;
	}
	
}
