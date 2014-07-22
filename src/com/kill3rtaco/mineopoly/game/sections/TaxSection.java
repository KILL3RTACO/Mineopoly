package com.kill3rtaco.mineopoly.game.sections;

import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;

public class TaxSection extends CardinalSection implements ActionProvoker {
	
	private int	tax, side;
	
	public TaxSection(int id, String name, char color, int side, int tax) {
		super(id, name, color, SectionType.TAX);
		this.side = side;
		this.tax = tax;
	}
	
	@Override
	public void provokeAction(MineopolyPlayer player) {
		Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + player.getName() + "&3 paid &2" + tax + " &3to the bank", player);
		player.sendMessage("&3You paid &2" + tax + " &3to the bank");
		player.payPot(tax);
		player.setCanEndTurnAutomatically(true);
	}
	
	@Override
	public void getInfo(Player player) {
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&6---[" + getColorfulName() + "&b(&3" + getId() + "&b)&6]---");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&bLand on this space and you must pay &2" + tax + " &bto the bank");
	}
	
	public int getTax() {
		return tax;
	}
	
	@Override
	public int getSide() {
		return side;
	}
	
}
