package com.kill3rtaco.mineopoly;

import static com.kill3rtaco.mineopoly.MineopolyConstants.*;

import java.io.File;

import com.kill3rtaco.tacoapi.api.TacoConfig;

public class LanguageConfig extends TacoConfig {
	
	public static String	h	= "&7[&9Mineopoly&7] &f";
	
	public LanguageConfig(File file) {
		super(file);
	}
	
	@Override
	protected void setDefaults() {
		addDefaultValue(L_CANNOT_USE_COMMAND_JAILED, "&cYou cannot use this command because you are jailed");
		addDefaultValue(L_GAME_NOT_RUNNING, "&cThere is no &6Mineopoly &cgame in progress");
		addDefaultValue(L_INVALID_TURN, "&cIt is not your turn");
		addDefaultValue(L_NO_DEEDS, "&cYou do not own any title deeds");
		addDefaultValue(L_NOT_ONLINE, "&e%player &cis not online");
		addDefaultValue(L_PLAYER_NO_DEEDS, "&e%player &cdoes not own any title deeds");
		addDefaultValue(L_PLAYER_NOT_PLAYING, "&e%player &cis not playing &6Mineopoly");
		addDefaultValue(L_ROLL_BEFORE_ENDING_TURN, "&cYou must roll before ending your turn");
		addDefaultValue(L_NEGATIVE_MONEY, "&cYou are in debt (negative money) you must gain money before ending your turn. Or you can quit with &3/mgame quit");
		addDefaultValue(L_TRADE_STILL_ACTIVE, "&cYou cannot end your turn until players that you have sent a trade request" +
				" either accept or decline it, or you can cancel it");
		addDefaultValue(L_CANNOT_PLAY_BANNED, "&cYou cannot play, you are banned");
		addDefaultValue(L_ALREADY_PLAYING, "&cYou are already playing &6Mineopoly");
		addDefaultValue(L_ALREADY_QUEUED, "&cYou are already in the &6Mineopoly &cqueue");
		addDefaultValue(L_ADDED_TO_QUEUE, "You've been added to the game queue, please wait until the next game is over or until more players join");
		addDefaultValue(L_ADDED_TO_QUEUE_SERVER, "&b%player &3just joined the &9Mineopoly &3game queue! &b/mineopoly join");
		addDefaultValue(L_ALREADY_IN_CHANNEL, "&cYou are already in the channel");
		addDefaultValue(L_ADDED_TO_CHANNEL, "&aYou are now listening to the Mineopoly channel");
		addDefaultValue(L_CANNOT_LEAVE_CHANNEL_PLAYING, "&cYou cannot leave the channel if you are playing");
		addDefaultValue(L_REMOVED_FROM_CHANNEL, "&aYou are no longer listening to the channel");
		addDefaultValue(L_NOT_IN_CHANNEL, "&cYou are you are not in the Mineopoly channel");
		addDefaultValue(L_NO_MONOPOLIES, "&cYou do not have any monopolies");
		addDefaultValue(L_PLAYER_NO_MONOPOLIES, "&e%player &cdoes not have any monopolies");
		addDefaultValue(L_CANNOT_PERFORM_ACTION, "&cYou cannot do that right now");
		addDefaultValue(L_HR_TR_NOT_ENABLED, "&cThe HouseRule &eTraveling Railroads &cis not enabled");
		addDefaultValue(L_RR_ACROSS_UNOWNED, "&cThe railroad across from this one is unowned");
		addDefaultValue(L_RR_CURRENT_UNOWNED, "&cThis railroad is unowned");
		addDefaultValue(L_ROLL_BEFORE_TRAVEL, "&cYou must roll before traveling across the board");
		addDefaultValue(L_QUEUE_EMPTY, "&cThere is no one in the game queue");
		addDefaultValue(L_SPECIFY_PLAYER_TO_BAN, "&cMust specify a player to ban");
		addDefaultValue(L_ALREADY_BANNED, "&e%player &cis already banned");
		addDefaultValue(L_PLAYER_BANNED, "&aPlayer banned");
		addDefaultValue(L_SPECIFY_PLAYER_TO_UNBAN, "&cMust specify a player to unban");
		addDefaultValue(L_NOT_BANNED, "&e%player &cis not banned");
		addDefaultValue(L_PLAYER_UNBANNED, "&aPlayer unbanned");
		addDefaultValue(L_SPECIFY_BOARD, "&cYou must specify a board name");
		addDefaultValue(L_NO_BOARD_YML, "&cNo &6board.yml &cfound for the board &e%name");
		addDefaultValue(L_BOARD_SWITCHED, "&aSwitched board: &e%name");
		addDefaultValue(L_SPECIFY_CONFIG, "&cYou must specify a configuration name");
		addDefaultValue(L_CONFIG_SWITCHED, "&aSwitched config: &e%name");
		addDefaultValue(L_CONFIGS_RELOADED, "&aConfig files reloaded");
		addDefaultValue(L_SPECIFY_PLAYER_TO_ADD, "&cMust specify player to add");
		addDefaultValue(L_PLAYER_ADDED_TO_GAME, "&e%player was added to the game");
		addDefaultValue(L_CANNOT_ADD_BANNED, "&e%player &cis banned from playing &6Mineopoly");
		addDefaultValue(L_PLAYER_ALREADY_PLAYING, "&e%player &cis already playing &6Mineopoly");
		addDefaultValue(L_PLAYER_NOT_FOUND, "&cPlayer not found: &e%player");
		addDefaultValue(L_PLAYER_KICKED, "&2%player &akicked");
		addDefaultValue(L_SPECIFY_SAVE_GAME, "&cMust specify a save game");
		addDefaultValue(L_STARTING_SAVE_GAME, "&aStarting game &e%name&a...");
		addDefaultValue(L_CANNOT_RESUME_GAME_NOT_ENOUGH_PLAYERS, "&cCannot resume that game, not enough players are online");
		addDefaultValue(L_SAVE_NON_EXISTANT, "&cThe save &e%name &cdoes not exist");
		addDefaultValue(L_NOT_LOADED_FROM_SAVE, "&cThis game was not loaded from a save," +
				" please speicify a name to save this game");
		addDefaultValue(L_SAVING_GAME, "&aSaving game as &e%name&a...");
		addDefaultValue(L_DONE, "&aDone!");
		addDefaultValue(L_PASTE_LOCATION_SAVED, "&aPaste location saved for &e %name&a(&e%x&a, &e%y&a, &e%z&a)");
		addDefaultValue(L_ALREADY_GAME_RUNNING, "&cThere is already a game in progress");
		addDefaultValue(L_NOT_ENOUGH_PLAYERS, "&e%queued &cplayers in queue with minimum of &e%min &crequired");
		addDefaultValue(L_NOT_JAILED, "&cYou are not in &1Jail");
		addDefaultValue(L_CANNOT_SWITCH_BOARD_PLAYING, "&cYou cannot switch the board while a game is running");
	}
	
	public String message(String key, boolean header, Object... pairs) {
		if(pairs.length % 2 > 0) {
			throw new IllegalArgumentException("pairs.length must be divisible by two");
		}
		String msg = getString(key);
		if(header)
			msg = h + msg;
		for(int i = 0; i < pairs.length; i++) {
			msg = msg.replaceAll("%" + pairs[i], pairs[++i].toString());
		}
		return msg;
	}
	
}
