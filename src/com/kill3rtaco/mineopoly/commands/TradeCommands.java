package com.kill3rtaco.mineopoly.commands;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.sections.OwnableSection;
import com.kill3rtaco.mineopoly.game.trading.TradeRequest;
import com.kill3rtaco.mineopoly.game.trading.TradeType;
import com.kill3rtaco.tacoapi.api.ncommands.Command;
import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.tacoapi.api.ncommands.ParentCommand;

public class TradeCommands {
	
	@ParentCommand("mtrade")
	@Command(name = "", desc = "MineopolyTrade Commands")
	public static void noArgs(CommandContext context) {
		context.invokeCommand(Mineopoly.getTAlias() + " ?");
	}
	
	@ParentCommand("mtrade")
	@Command(name = "accept", aliases = {"a"}, args = "<player>", desc = "Accept a trade")
	public static void accept(CommandContext context) {
		if(!Mineopoly.plugin.getGame().isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(MineopolyConstants.L_GAME_NOT_RUNNING, true));
			return;
		}
		MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer(context.getPlayer());
		if(mp == null) {
			context.sendMessageToSender(Mineopoly.lang.message(MineopolyConstants.L_NOT_PLAYING, true));
			return;
		}
		if(context.eq(0)) {
			mp.sendMessage("&cMust specify a player");
			return;
		}
		MineopolyPlayer trader = Mineopoly.plugin.getGame().getBoard().getPlayer(context.getString(0));
		if(trader == null) {
			mp.sendMessage("&e" + context.getString(0) + " &cis not playing Mineopoly");
			return;
		}
		
		TradeRequest request = mp.getReceivedRequest(trader.getName());
		if(request == null) {
			mp.sendMessage("&cYou do not have a request from &e" + trader.getName());
			return;
		}
		if(request.getType() == TradeType.SELL) {
			int money = request.getRequestedMoney();
			OwnableSection section = request.getOfferedSection();
			if(!mp.hasMoney(money)) {
				mp.sendMessage("&cYou cannot accept this request because you do not have enough money (&2" + money + "&c)");
				return;
			}
			mp.payPlayer(trader, money);
			trader.removeSection(section);
			trader.sendMessage("&aYou no longer own " + section.getColorfulName());
			section.setOwner(mp);
			mp.sendMessage("&aYou now own " + section.getColorfulName());
			Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3accept &b" + trader.getName() + "&3's trade request&7: " +
					"&2" + money + " &7-> " + section.getColorfulName(), mp, trader);
		} else {
			OwnableSection requested = request.getRequestedSection();
			OwnableSection offered = request.getOfferedSection();
			mp.removeSection(requested);
			mp.sendMessage("&aYou no longer own " + requested.getColorfulName());
			trader.removeSection(offered);
			trader.sendMessage("&aYou no longer own " + offered.getColorfulName());
			offered.setOwner(mp);
			mp.sendMessage("&aYou now own " + offered.getColorfulName());
			requested.setOwner(trader);
			trader.sendMessage("&aYou now own " + requested.getColorfulName());
			Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3accept &b" + trader.getName() + "&3's trade request&7: " +
					requested.getColorfulName() + " &7-> " + offered.getColorfulName(), mp, trader);
		}
		
		mp.clearRequestsWithPlayer(trader.getName());
		trader.clearRequestsWithPlayer(mp.getName());
	}
	
	@ParentCommand("mtrade")
	@Command(name = "cancel", aliases = {"c"}, args = "<player/*>", desc = "Cancel a trade (or all of them)")
	public static void cancel(CommandContext context) {
		if(!Mineopoly.plugin.getGame().isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(MineopolyConstants.L_GAME_NOT_RUNNING, true));
			return;
		}
		MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer(context.getPlayer());
		if(mp == null) {
			context.sendMessageToSender(Mineopoly.lang.message(MineopolyConstants.L_NOT_PLAYING, true));
			return;
		}
		if(context.eq(0)) {
			mp.sendMessage("&cMust specify a player");
			return;
		}
		if(context.eic(0, "*")) {
			for(TradeRequest r : mp.getSentRequests()) {
				MineopolyPlayer other = r.getReceiver();
				mp.clearRequestsWithPlayer(other.getName());
				other.clearRequestsWithPlayer(mp.getName());
			}
			Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3canceled all of their trading requests", mp);
			mp.sendMessage("&aAll trade requests cancelled");
			return;
		}
		MineopolyPlayer trader = Mineopoly.plugin.getGame().getBoard().getPlayer(context.getString(0));
		if(trader == null) {
			mp.sendMessage("&e" + context.getString(0) + " &cis not playing Mineopoly");
			return;
		}
		
		TradeRequest request = mp.getSentRequest(trader.getName());
		if(request == null) {
			mp.sendMessage("&cYou have not sent a request to &e" + trader.getName()); //#remember - requnameest
			return;
		}
		
		mp.clearRequestsWithPlayer(trader.getName());
		trader.clearRequestsWithPlayer(mp.getName());
		
		mp.sendMessage("&aRequest declined");
		trader.sendMessage("&e" + mp.getName() + " &acancelled their request to you");
	}
	
	@ParentCommand("mtrade")
	@Command(name = "decline", aliases = {"d"}, args = "<player>", desc = "Decline a trade")
	public static void decline(CommandContext context) {
		if(!Mineopoly.plugin.getGame().isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(MineopolyConstants.L_GAME_NOT_RUNNING, true));
			return;
		}
		MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer(context.getPlayer());
		if(mp == null) {
			context.sendMessageToSender(Mineopoly.lang.message(MineopolyConstants.L_NOT_PLAYING, true));
			return;
		}
		if(context.eq(0)) {
			mp.sendMessage("&cMust specify a player");
			return;
		}
		MineopolyPlayer trader = Mineopoly.plugin.getGame().getBoard().getPlayer(context.getString(0));
		if(trader == null) {
			mp.sendMessage("&e" + context.getString(0) + " &cis not playing Mineopoly");
			return;
		}
		
		TradeRequest request = mp.getReceivedRequest(trader.getName());
		if(request == null) {
			mp.sendMessage("&cYou do not have a request from &e" + trader.getName());
			return;
		}
		
		mp.clearRequestsWithPlayer(trader.getName());
		trader.clearRequestsWithPlayer(mp.getName());
		
		mp.sendMessage("&aYou declined &e" + trader.getName() + "&a's trade request");
		trader.sendMessage("&e" + mp.getName() + " &adeclined your trade request");
		
		Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3declined &b" + trader.getName() + "&3's trade request", mp, trader);
	}
	
}
