package com.kill3rtaco.mineopoly.game.sections.squares;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.sections.SpecialSquare;

public class JailSquare extends SpecialSquare {
	
	public JailSquare() {
		super(10, Mineopoly.names.getString(MineopolyConstants.N_JAIL), '1');
	}
	
	@Override
	public void provokeAction(MineopolyPlayer player) {
		//nothing to do
	}
	
	@Override
	public void getInfo(Player player) {
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&6---[" + getColorfulName() + "&b(&3" + getId() + "&b)&6]---");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&3Just Visiting&7:&b You are only visiting, you can watch everyone in jail.");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "&3Jail&7:&b You are in jail and must wait until either one of three things happens:");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "   &1*&3You roll doubles");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "   &1*&3You pay bail (&250&b)");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "   &1*&3You use a &1Get Out of Jail Free &3card");
	}
	
	private Location getLocation(boolean jailed) {
		Location offset = Mineopoly.boardConfig.getJailLocationOffset(jailed);
		return getLocationRelative(offset.getX(), 0, offset.getZ());
	}
	
	public Location getJailCellLocation() {
		return getLocation(true);
	}
	
	public Location getJustVisitingLocation() {
		return getLocation(false);
	}
	
}
