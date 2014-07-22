package com.kill3rtaco.mineopoly.commands;

import static com.kill3rtaco.mineopoly.MineopolyConstants.*;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.game.MineopolyGame;
import com.kill3rtaco.mineopoly.game.MineopolyPlayer;
import com.kill3rtaco.mineopoly.game.config.MineopolyBoardConfig;
import com.kill3rtaco.mineopoly.saves.MineopolySaveGame;
import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.api.ncommands.Command;
import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.tacoapi.api.ncommands.CommandPermission;
import com.kill3rtaco.tacoapi.api.ncommands.ParentCommand;

public class AdministrationCommands {
	
	@ParentCommand("mineopoly")
	@Command(name = "ban", args = "<player", desc = "Ban a player from playing Mineopoly", onlyPlayer = false)
	@CommandPermission(P_BAN_PLAYER_FROM_GAME)
	public static void ban(CommandContext context) {
		if(context.eq(0)) {
			context.sendMessageToSender(Mineopoly.lang.message(L_SPECIFY_PLAYER_TO_BAN, true));
		} else {
			String name = context.getString(0);
			Player p = context.getPlayer(0);
			if(p != null)
				name = p.getName();
			if(!Mineopoly.plugin.isBanned(name)) {
				Mineopoly.plugin.banPlayer(name);
				if(p != null && p.isOnline() && Mineopoly.plugin.getGame().hasPlayer(p)) {
					MineopolyPlayer mp = Mineopoly.plugin.getGame().getBoard().getPlayer(p);
					Mineopoly.plugin.getGame().kick(mp, "banned by " + context.getPlayerName());
				}
				context.sendMessageToSender(Mineopoly.lang.message(L_PLAYER_BANNED, true, "player", name));
			} else {
				context.sendMessageToSender(Mineopoly.lang.message(L_ALREADY_BANNED, true, "player", name));
			}
		}
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "board", desc = "Set the board to play on", onlyPlayer = false)
	@CommandPermission(P_SWITCH_BOARD)
	public static void board(CommandContext context) {
		if(context.eq(0)) {
			context.sendMessageToSender(Mineopoly.lang.message(L_SPECIFY_BOARD, true));
			return;
		}
		if(Mineopoly.plugin.getGame().isRunning()) {
			context.sendMessageToSender("Cannot switch board while the game is running");
			return;
		}
		String name = context.getString(0);
		File file = new File(Mineopoly.plugin.getDataFolder() + "/boards/" + name + "/board.yml");
		if(!file.exists()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NO_BOARD_YML, true, "name", name));
			return;
		}
		Mineopoly.config.setBoardName(name);
		Mineopoly.reloadConfigurationFiles();
		context.sendMessageToSender(Mineopoly.lang.message(L_BOARD_SWITCHED, true, "name", name));
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "cardset", args = "<card-set>", desc = "Set the cards to use in-game", onlyPlayer = false)
	@CommandPermission(P_SWITCH_CARDS)
	public static void cardset(CommandContext context) {
		if(context.eq(0)) {
			context.sendMessageToSender("&cMust specify cardset");
			return;
		}
		if(Mineopoly.plugin.getGame().isRunning()) {
			context.sendMessageToSender("Cannot switch cardset while the game is running");
			return;
		}
		String name = context.getString(0);
		Mineopoly.config.setCardSet(name);
		Mineopoly.reloadConfigurationFiles();
		context.sendMessageToSender("&aSwitched cards: &e" + name);
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "config", aliases = {"cfg"}, args = "<name>", desc = "Switch between configurations (or create a new one)", onlyPlayer = false)
	@CommandPermission(P_SWITCH_CONFIG)
	public static void config(CommandContext context) {
		if(context.eq(0)) {
			context.sendMessageToSender(Mineopoly.lang.message(L_SPECIFY_CONFIG, true));
			return;
		}
		String name = context.getString(0);
		Mineopoly.config.setConfigName(name);
		context.sendMessageToSender(Mineopoly.lang.message(L_CONFIG_SWITCHED, true, "name", name));
		Mineopoly.reloadConfigurationFiles();
		context.sendMessageToSender(Mineopoly.lang.message(L_CONFIGS_RELOADED, true));
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "end", desc = "End the current Mineopoly game", onlyPlayer = false)
	@CommandPermission(P_END_GAME)
	public static void end(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		game.getChannel().sendMessage("&e" + context.getPlayerName() + " ended the game.");
		game.end();
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "force-add", aliases = {"fa", "add"}, args = "<player>", desc = "Add a player to the Mineopoly game", onlyPlayer = false)
	public static void forceAddPlayer(CommandContext context) {
		if(context.eq(0)) {
			
			return;
		}
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		Player p = context.getPlayer(0);
		if(p == null) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NOT_ONLINE, true, "player", context.getString(0)));
			return;
		}
		if(game.hasPlayer(p)) {
			context.sendMessageToSender(Mineopoly.lang.message(L_PLAYER_ALREADY_PLAYING, true, "player", p.getName()));
			return;
		}
		boolean add = true;
		if(Mineopoly.plugin.isBanned(p.getName())) {
			add = Mineopoly.options.addEvenWhenBanned();
		}
		
		if(add) {
			MineopolyPlayer mp = new MineopolyPlayer(p);
			game.getBoard().addPlayer(mp);
			game.getChannel().addPlayer(mp);
			mp.setCurrentSection(Mineopoly.plugin.getGame().getBoard().getSection(0), false);
			context.sendMessageToSender(Mineopoly.lang.message(L_PLAYER_ADDED_TO_GAME, true, "player", mp.getName()));
		} else {
			context.sendMessageToSender(Mineopoly.lang.message(L_CANNOT_ADD_BANNED, true, "player", p.getName()));
		}
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "kick", args = "<player>", desc = "Kick a player from the Mineopoly game", onlyPlayer = false)
	@CommandPermission(P_KICK_PLAYER_FROM_GAME)
	public static void kick(CommandContext context) {
		if(context.eq(0)) {
			
			return;
		}
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			
			return;
		}
		String start = context.getString(0);
		for(MineopolyPlayer mp : Mineopoly.plugin.getGame().getBoard().getPlayers()) {
			if(mp.getName().equalsIgnoreCase(start) || mp.getName().startsWith(start)) {
				Mineopoly.plugin.getGame().kick(mp, "kicked by " + context.getPlayerName());
				break;
			}
		}
		context.sendMessageToSender(Mineopoly.lang.message(L_PLAYER_NOT_FOUND, true));
		context.sendMessageToSender(Mineopoly.lang.message(L_PLAYER_KICKED, true));
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "queue", aliases = {"q"}, desc = "View who is in the queue", onlyPlayer = false)
	@CommandPermission(P_VIEW_PLAYER_QUEUE)
	public static void queue(CommandContext context) {
		if(Mineopoly.plugin.getQueue().getSize() == 0) {
			context.sendMessageToSender(Mineopoly.lang.message(L_QUEUE_EMPTY, true));
			return;
		}
		context.sendMessageToSender(TacoAPI.getChatUtils().createHeader("&7Mineopoly Queue"));
		String players = "";
		for(Player p : Mineopoly.plugin.getQueue()) {
			if(Mineopoly.plugin.getQueue().getIndexFromPlayer(p) == Mineopoly.plugin.getQueue().getSize() - 1) {
				players = players + "&7" + p.getName();
			} else {
				players = players + "&7" + p.getName() + "&8, ";
			}
		}
		context.sendMessageToSender(players);
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "reload", desc = "Reload the Mineopoly config files", onlyPlayer = false)
	@CommandPermission(P_RELOAD)
	public static void reload(CommandContext context) {
		Mineopoly.reloadConfigurationFiles();
		context.sendMessageToSender(Mineopoly.lang.message(L_CONFIGS_RELOADED, true));
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "resume", aliases = {"res"}, args = "<save-name>", desc = "Resume a Mineopoly game")
	public static void resume(CommandContext context) {
		if(context.eq(0)) {
			context.sendMessageToSender(Mineopoly.lang.message(L_SPECIFY_SAVE_GAME, true));
			return;
		}
		File file = new File(Mineopoly.plugin.getDataFolder() + "/saves/" + context.getString(0) + ".yml");
		if(!file.exists()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_SAVE_NON_EXISTANT, true));
			return;
		}
		MineopolySaveGame save = new MineopolySaveGame(file);
		if(MineopolyGame.canStart(save)) {
			context.sendMessageToSender(Mineopoly.lang.message(L_CANNOT_RESUME_GAME_NOT_ENOUGH_PLAYERS, true));
			return;
		}
		context.sendMessageToSender(Mineopoly.lang.message(L_STARTING_SAVE_GAME, true));
		Mineopoly.plugin.resumeGame(save);
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "save", args = "[save-name]", desc = "Save the current game", onlyPlayer = false)
	@CommandPermission(P_SAVE_GAME)
	public static void save(CommandContext context) {
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!game.isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_GAME_NOT_RUNNING, true));
			return;
		}
		if(context.eq(0) && !game.isLoadedFromSave()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_DONE, true));
			return;
		}
		String name;
		if(context.gt(0))
			name = context.getString(0);
		else
			name = game.getSave().getName();
		context.sendMessageToSender(Mineopoly.lang.message(L_SAVING_GAME, true, "name", name));
		game.save(name);
		context.sendMessageToSender(Mineopoly.lang.message(L_DONE, true));
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "set-paste-location", aliases = {"spl"}, args = "<board-name>", desc = "")
	@CommandPermission(P_SET_PASTE_LOCATION)
	public static void setPasteLocation(CommandContext context) {
		if(context.eq(0)) {
			context.sendMessageToSender(Mineopoly.lang.message(L_SPECIFY_BOARD, true));
			return;
		}
		String name = context.getString(0);
		File file = new File(Mineopoly.plugin.getDataFolder() + "/boards/" + name + "/board.yml");
		if(!file.exists()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NO_BOARD_YML, true, "name", name));
			return;
		}
		MineopolyBoardConfig board = new MineopolyBoardConfig(name);
		Location loc = context.getPlayer().getLocation();
		board.setBoardOrigin(loc);
		context.sendMessageToSender(Mineopoly.lang.message(L_NO_BOARD_YML, true, "name", name,
				"x", loc.getBlockX(), "y", loc.getBlockY(), "z", loc.getBlockZ()));
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "start", desc = "Start the game", onlyPlayer = false)
	@CommandPermission(P_START_GAME)
	public static void start(CommandContext context) {
		if(Mineopoly.plugin.getGame().isRunning()) {
			context.sendMessageToSender(Mineopoly.lang.message(L_ALREADY_GAME_RUNNING, true));
			return;
		}
		int queued = Mineopoly.plugin.getQueue().getSize();
		int min = Mineopoly.options.minPlayers();
		if(queued >= min) {
			if(context.eq(0)) {
				Mineopoly.plugin.restartGame();
			} else {
				//backdoor for testing/debugging, this is done silently
				Mineopoly.plugin.restartGame(Boolean.valueOf(context.getString(0)));
			}
		} else {
			context.sendMessageToSender(Mineopoly.lang.message(L_NOT_ENOUGH_PLAYERS, true, "queued", queued, "min", min));
		}
	}
	
	@ParentCommand("mineopoly")
	@Command(name = "unban", args = "<player>", desc = "Unban a player from playing Mineopoly", onlyPlayer = false)
	@CommandPermission(P_UNBAN_PLAYER_FROM_GAME)
	public static void unban(CommandContext context) {
		if(context.eq(0)) {
			context.sendMessageToSender(Mineopoly.lang.message(L_SPECIFY_PLAYER_TO_UNBAN, true));
			return;
		}
		String name = context.getString(0);
		Player p = context.getPlayer(0);
		if(p != null)
			name = p.getName();
		if(!Mineopoly.plugin.isBanned(name)) {
			context.sendMessageToSender(Mineopoly.lang.message(L_NOT_BANNED, true));
			return;
		}
		Mineopoly.plugin.unbanPlayer(name);
		context.sendMessageToSender(Mineopoly.lang.message(L_PLAYER_UNBANNED, true));
	}
	
}
