package com.kill3rtaco.mineopoly.commands;

import static com.kill3rtaco.mineopoly.MineopolyConstants.*;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.MineopolyColor;
import com.kill3rtaco.mineopoly.game.MineopolyGame;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.MineopolySection;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCard;
import com.kill3rtaco.mineopoly.game.chat.MineopolyChannelListener;
import com.kill3rtaco.mineopoly.game.menus.MineopolyGameMenu;
import com.kill3rtaco.mineopoly.game.sections.OwnableSection;
import com.kill3rtaco.mineopoly.game.sections.Railroad;
import com.kill3rtaco.mineopoly.game.sections.SectionType;
import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.api.ncommands.Command;
import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.tacoapi.api.ncommands.CommandPermission;
import com.kill3rtaco.tacoapi.api.ncommands.ParentCommand;

public class GameCommands {
	
	@ParentCommand("mineopoly")
	@Command(name = "", desc = "Mineopoly Commands")
	public static void noArgs(CommandContext context) {
		context.invokeCommand("mineopoly ?");
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "deeds", aliases = {"properties", "props"}, args = "[player]", desc = "View a player's title deeds")
	public static void deeds(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		if(context.eq(0)) {
			if(!game.hasPlayer(context.getPlayer())) {
				context.sendMessageToSender(Mineopoly.lang.message(L_NOT_PLAYING, true));
				return;
			}
			MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer(context.getPlayer());
			if(mp.ownedSections().isEmpty()) {
				mp.sendMessage(Mineopoly.lang.message(L_NO_DEEDS, false));
				return;
			}
			String s = "";
			ArrayList<OwnableSection> props = mp.ownedSections();
			Collections.sort(props);
			for(int i = 0; i < props.size(); i++) {
				if(i == props.size() - 1)
					s = s + props.get(i).getColorfulName();
				else
					s = s + props.get(i).getColorfulName() + "&8, ";
			}
			mp.sendMessage(TacoAPI.getChatUtils().createHeader("&3" + mp.getName() + "&b's Title Deeds"));
			mp.sendMessage(s);
		} else {
			if(!game.hasPlayer(context.getPlayer()) && !context.senderHasPermission(MineopolyConstants.P_VIEW_GAME_STATS)) {
				context.sendMessageToSender("&cYou do not have permission");
				return;
			}
			Player p = context.getPlayer(0);
			if(p == null) {
				context.sendMessageToSender(Mineopoly.lang.message(L_NOT_ONLINE, true, "player", context.getString(0)));
				return;
			}
			if(!game.hasPlayer(p)) {
				context.sendMessageToSender(Mineopoly.lang.message(L_PLAYER_NOT_PLAYING, true, "player", context.getString(0)));
				return;
			}
			MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer(p);
			if(mp.ownedSections().size() == 0) {
				context.sendMessageToSender(Mineopoly.lang.message(L_PLAYER_NO_DEEDS, true, "player", mp.getName()));
				return;
			}
			String s = "";
			ArrayList<OwnableSection> props = mp.ownedSections();
			Collections.sort(props);
			for(int i = 0; i < props.size(); i++) {
				if(i == props.size() - 1)
					s = s + props.get(i).getColorfulName();
				else
					s = s + props.get(i).getColorfulName() + "&8, ";
			}
			context.sendMessageToSender(TacoAPI.getChatUtils().createHeader("&3" + mp.getName() + "&b's Title Deeds"));
			context.sendMessageToSender(s);
		}
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "end-turn", aliases = {"et"}, desc = "End your turn")
	public static void endTurn(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		MineopolyPlayer p = Mineopoly.plugin.getGame().getBoard().getPlayer(context.getPlayer());
		if(p == null) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NOT_PLAYING, true));
			return;
		}
		if(p.isJailed()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_CANNOT_USE_COMMAND_JAILED, true));
			return;
		}
		if(!p.hasTurn()) {
			p.sendMessage(Mineopoly.lang.message(L_INVALID_TURN, false));
			return;
		}
		if(MineopolyCard.eventInProgess) {
			p.sendMessage("&cYou cannot end your turn while there is an event");
			return;
		}
		if(!p.hasRolled()) {
			p.sendMessage(Mineopoly.lang.message(L_ROLL_BEFORE_ENDING_TURN, false));
			return;
		}
		if(p.getBalance() < 0) {
			p.sendMessage(Mineopoly.lang.message(L_NEGATIVE_MONEY, false));
			return;
		}
		if(!Mineopoly.houseRules.tradeAnytime() && p.hasSentRequest()) {
			p.sendMessage(Mineopoly.lang.message(L_TRADE_STILL_ACTIVE, false));
			return;
		}
		p.setTurn(false, false);
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "game-info", aliases = {"gi"}, desc = "View game info")
	public static void info(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		String players = "&b" + TacoAPI.getChatUtils().join(game.getBoard().getPlayerList(), "&7, &b");
		MineopolyPlayer mp = game.getPlayerWithCurrentTurn();
		MineopolySection section = mp.getCurrentSection();
		int properties = game.getBoard().getTotalOwnedProperties();
		int railroads = game.getBoard().getTotalOwnedRailroads();
		int utilities = game.getBoard().getTotalOwnedUtilities();
		String header = TacoAPI.getChatUtils().createHeader("&3MineopolyGameStats");
		context.sendMessageToSender(header);
		context.sendMessageToSender("&3Time running&7: &d" + game.getTimeRunningString()
				+ (Mineopoly.houseRules.timeLimit() < 0 ? " &b(" + game.getTimeLeftString() + " &3 left&b)" : ""));
		context.sendMessageToSender("&3Players&7: " + players);
		context.sendMessageToSender("&3Current Turn&7: &b" + mp.getName() + " &7[" + section.getColorfulName() + "&7]");
		context.sendMessageToSender("&3Properties Owned&7: &d" + properties);
		context.sendMessageToSender("&3Railroads Owned&7: &d" + railroads);
		context.sendMessageToSender("&3Utilities Owned&7: &d" + utilities);
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "join", aliases = {"j"}, desc = "Join the game queue")
	@CommandPermission(P_JOIN_GAME)
	public static void join(CommandContext context) {
		if(Mineopoly.plugin.getQueue().playerIsInQueue(context.getPlayer())) {
			context.sendMessageToSender(Mineopoly.lang.message(L_ALREADY_QUEUED, true));
		} else if(Mineopoly.plugin.getGame().isRunning() && Mineopoly.plugin.getGame().hasPlayer(context.getPlayer())) {
			context.sendMessageToSender(Mineopoly.lang.message(L_ALREADY_PLAYING, true));
		} else {
			if(Mineopoly.plugin.isBanned(context.getPlayerName())) {
				context.sendMessageToSender(Mineopoly.lang.message(L_CANNOT_PLAY_BANNED, true));
			} else {
				Mineopoly.plugin.getQueue().addPlayer(context.getPlayer());
				context.sendMessageToSender(Mineopoly.lang.message(L_ADDED_TO_QUEUE, true));
				Mineopoly.plugin.chat.sendGlobalMessageNoHeader(Mineopoly.lang.message(L_ADDED_TO_QUEUE_SERVER, true, "player", context.getPlayerName()));
			}
		}
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "join-channel", aliases = {"jc"}, desc = "Join the game's output channel")
	@CommandPermission(MineopolyConstants.P_CHANNEL_CHAT)
	public static void joinChannel(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		if(game.getChannel().isListeningToChannel(context.getPlayerName())) {
			context.sendMessageToSender(Mineopoly.lang.message(L_ALREADY_IN_CHANNEL, true));
		} else {
			Mineopoly.plugin.getGame().getChannel().addPlayer(new MineopolyChannelListener(context.getPlayer()));
			context.sendMessageToSender(Mineopoly.lang.message(L_ADDED_TO_CHANNEL, true));
		}
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "leave-channel", aliases = {"lc"}, desc = "Leave the game's output channel")
	@CommandPermission(MineopolyConstants.P_CHANNEL_CHAT)
	public static void leaveChannel(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		if(!game.getChannel().isListeningToChannel(context.getPlayerName())) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NOT_IN_CHANNEL, true));
		} else {
			game.getChannel().removePlayer(context.getPlayerName());
			context.sendMessageToSender(Mineopoly.lang.message(L_REMOVED_FROM_CHANNEL, true));
		}
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "menu", aliases = {"m"}, desc = "Open the in-game menu")
	public static void menu(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		MineopolyPlayer mp = game.getBoard().getPlayer(context.getPlayer());
		if(mp == null) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NOT_PLAYING, true));
			return;
		}
		mp.showMenu(new MineopolyGameMenu(mp));
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "monopolies", aliases = {"monos", "ms"}, args = "[player]", desc = "View a player's monopolies")
	public static void monopolies(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		if(context.eq(0)) {
			MineopolyPlayer mp = game.getBoard().getPlayer(context.getPlayer());
			if(mp == null) {
				context.sendMessageToSender(Mineopoly.lang.message(L_NOT_PLAYING, true));
				return;
			}
			if(mp.ownedSections().size() == 0) {
				context.sendMessageToSender(Mineopoly.lang.message(L_NO_MONOPOLIES, true));
			} else {
				String s = "";
				ArrayList<MineopolyColor> monopolies = mp.getMonopolies();
				for(int i = 0; i < monopolies.size(); i++) {
					if(i == monopolies.size() - 1)
						s = s + monopolies.get(i).getName();
					else
						s = s + monopolies.get(i).getName() + "&8, ";
				}
				context.sendMessageToSender(TacoAPI.getChatUtils().createHeader("&3" + mp.getName() + "&b's Monopolies"));
				context.sendMessageToSender(s);
			}
		} else {
			if(!game.hasPlayer(context.getPlayer()) && !context.senderHasPermission(MineopolyConstants.P_VIEW_GAME_STATS)) {
				context.sendMessageToSender("&cYou don't have permission");
				return;
			}
			Player p = context.getPlayer(0);
			if(p == null) {
				context.sendMessageToSender(Mineopoly.lang.message(L_NOT_ONLINE, true, "player", context.getString(0)));
				return;
			}
			MineopolyPlayer mp = game.getBoard().getPlayer(p);
			if(mp == null) {
				context.sendMessageToSender(Mineopoly.lang.message(L_PLAYER_NOT_PLAYING, true, "player", p.getName()));
				return;
			}
			if(mp.ownedSections().size() == 0) {
				context.sendMessageToSender(Mineopoly.lang.message(L_PLAYER_NO_MONOPOLIES, true, "player", mp.getName()));
				return;
			}
			String s = "";
			ArrayList<MineopolyColor> monopolies = mp.getMonopolies();
			for(int i = 0; i < monopolies.size(); i++) {
				if(i == monopolies.size() - 1)
					s = s + monopolies.get(i).getName();
				else
					s = s + monopolies.get(i).getName() + "&8, ";
			}
			context.sendMessageToSender(TacoAPI.getChatUtils().createHeader("&3" + mp.getName() + "&b's Monopolies"));
			context.sendMessageToSender(s);
		}
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "pot", aliases = {"p"}, desc = "View what is in the pot")
	public static void pot(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		if(game.hasPlayer(context.getPlayer())) {
			game.getBoard().getPot().getInfo(context.getPlayer());
		} else {
			if(context.senderHasPermission(MineopolyConstants.P_VIEW_GAME_STATS)) {
				game.getBoard().getPot().getInfo(context.getPlayer());
			} else {
				context.sendMessageToSender("&cYou don't have permission");
			}
		}
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "quit", aliases = {"forfeit", "leave"}, desc = "Quit the game")
	public static void quit(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		MineopolyPlayer mp = game.getBoard().getPlayer(context.getPlayer());
		if(mp == null) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NOT_PLAYING, true));
		}
		Mineopoly.plugin.getGame().kick(mp, "quit");
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "roll", aliases = {"r"}, desc = "Roll the dice")
	public static void roll(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		MineopolyPlayer p = game.getBoard().getPlayer(context.getPlayer());
		if(p == null) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NOT_PLAYING, true));
			return;
		}
		if(!p.hasTurn()) {
			p.sendMessage(Mineopoly.lang.message(L_INVALID_TURN, false));
			return;
		}
		if(p.isJailed()) {
			p.sendMessage(Mineopoly.lang.message(L_CANNOT_USE_COMMAND_JAILED, false));
			return;
		}
		if(!p.canRoll()) {
			p.sendMessage(Mineopoly.lang.message(L_CANNOT_PERFORM_ACTION, false));
			return;
		}
		p.roll();
//		p.sendMessage(p.getLocation() + "");
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "stats", args = "[player]", desc = "View a player's stats")
	public static void stats(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		if(context.eq(0)) {
			MineopolyPlayer mp = game.getBoard().getPlayer(context.getPlayer());
			if(mp == null) {
				context.sendMessageToSender(Mineopoly.lang.message(L_NOT_PLAYING, true));
				return;
			}
			mp.getInfo(context.getPlayer());
		} else {
			Player p = context.getPlayer(0);
			if(p == null) {
				context.sendMessageToSender(Mineopoly.lang.message(L_NOT_ONLINE, true, "player", context.getString(0)));
				return;
			}
			MineopolyPlayer mp = game.getBoard().getPlayer(p);
			if(mp == null) {
				context.sendMessageToSender(Mineopoly.lang.message(L_PLAYER_NOT_PLAYING, true, "player", p.getName()));
				return;
			}
			if(game.hasPlayer(context.getPlayer()) || context.senderHasPermission(MineopolyConstants.P_VIEW_GAME_STATS)) {
				mp.getInfo(p);
			} else {
				context.sendMessageToSender("&cYou don't have permission");
			}
		}
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "travel", desc = "Travel to the railroad across from you")
	public static void travel(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer(context.getPlayer());
		if(mp == null) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NOT_PLAYING, true));
			return;
		}
		if(!Mineopoly.houseRules.travelingRailroads()) {
			mp.sendMessage(Mineopoly.lang.message(L_HR_TR_NOT_ENABLED, false));
			return;
		}
		if(!mp.hasTurn()) {
			mp.sendMessage(Mineopoly.lang.message(L_INVALID_TURN, false));
			return;
		}
		if(!mp.hasRolled()) {
			mp.sendMessage(Mineopoly.lang.message(L_ROLL_BEFORE_TRAVEL, false));
			return;
		}
		MineopolySection current = mp.getCurrentSection();
		MineopolySection across = Mineopoly.plugin.getGame().getBoard().getSection(current.getId() + 20);
		if(!mp.canTravel()) {
			if(current.getType() != SectionType.RAILROAD) {
				mp.sendMessage("&cYou are not on a railroad space");
			} else {
				Railroad rCurrent = (Railroad) current;
				Railroad rAcross = (Railroad) across;
				if(!rCurrent.isOwned()) {
					mp.sendMessage(Mineopoly.lang.message(L_RR_CURRENT_UNOWNED, false));
				} else if(!rAcross.isOwned()) {
					mp.sendMessage(Mineopoly.lang.message(L_RR_ACROSS_UNOWNED, false));
				} else if(!rCurrent.getOwner().getName().equalsIgnoreCase(rAcross.getName())) {
					mp.sendMessage("&e" + rCurrent.getOwner().getName() + " &cdoes not own " + rAcross.getColorfulName());
				}
			}
			return;
		}
		Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + mp.getName() + " &3traveled from " + current.getColorfulName() + " &3to " + across.getColorfulName(), mp);
		mp.sendMessage("&3You traveled from " + current.getColorfulName() + " &3to " + across.getColorfulName());
		mp.setCurrentSection(across, false, false, false);
		mp.endTurnAutomatically();
	}
	
}
