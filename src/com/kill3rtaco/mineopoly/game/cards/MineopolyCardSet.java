package com.kill3rtaco.mineopoly.game.cards;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;

public class MineopolyCardSet implements Iterable<MineopolyCard> {
	
	private final CardType				type;
	private File						cardsDir;
	private String						cardTypeName;
	private ArrayList<MineopolyCard>	cards;
	private int							cardsUsed	= 0;
	
	public MineopolyCardSet(CardType cardTypes, File cardLocation, String cardTypeName) {
		type = cardTypes;
		cardsDir = cardLocation;
		this.cardTypeName = cardTypeName;
		initCards(cardsDir);
		shuffle();
	}
	
	protected void addCard(MineopolyCard card) {
		cards.add(card);
	}
	
	protected void addJailCard() {
		String name = "GetOutOfJailFree";
		String desc = "Get out of Jail Free card. You may use this card to get out of jail";
		cards.add(new MineopolyCard(name, type, desc, "") {
			
			@Override
			public void action(MineopolyPlayer player) {
				if(type == CardType.CHANCE) {
					player.giveChanceJailCard();
				} else {
					player.giveCommunityChestJailCard();
				}
			}
			
			@SuppressWarnings("unused")
			//not used by this class, called in MineopolyCard constructor
			private void validate() {
				valid = true;
			}
			
		});
	}
	
	public MineopolyCard drawCard(MineopolyPlayer player) {
		if(cardsUsed == size()) {
			shuffle();
			cardsUsed = 0;
		}
		MineopolyCard card = cards.get(cardsUsed++);
		card.readDescription(player);
		card.action(player);
		return card;
	}
	
	public ArrayList<MineopolyCard> getCards() {
		return cards;
	}
	
	protected void initCards(File dir) {
		Mineopoly.plugin.chat.out("[Game] [Board] [Cards:" + cardTypeName + "] Loading cards...");
		cards = new ArrayList<MineopolyCard>();
		int count = 0;
		if(dir.isDirectory()) {
			for(File f : dir.listFiles()) {
				if(f.isDirectory())
					continue;
				YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
				String name = f.getName().substring(0, f.getName().lastIndexOf('.'));
				if(c != null) {
					MineopolyCard card = new MineopolyCard(name, type, c.getString("card.description"), c.getString("card.action"));
					if(card != null && card.isValid()) {
						addCard(card);
						count++;
					} else {
						Mineopoly.plugin.chat.out("[Cards:" + cardTypeName + "] Card is invalid: " + f.getName() + " (" + card.getInvalidReason() + "), skipping...");
					}
				}
			}
		}
		addJailCard();
		Mineopoly.plugin.chat.out("[Game] [Board] [Cards:" + cardTypeName + "] " + (count++) + " cards loaded!");
	}
	
	@Override
	public Iterator<MineopolyCard> iterator() {
		return cards.iterator();
	}
	
	public int size() {
		return cards.size();
	}
	
	public void shuffle() { //shuffles the deck
		Collections.shuffle(cards);
	}
	
}
