package com.kill3rtaco.mineopoly;

public class MineopolyConstants {
	
	//Configuration Constants
	public static final String	C_AUTO_TURN_END							= "mineopoly.game.automatic-turn-ending";
	public static final String	C_ADD_BANNED							= "mineopoly.game.add-even-when-banned";
	public static final String	C_BOARD_NAME							= "board";
	public static final String	C_CARDSET								= "cardset";
	public static final String	C_CHECK_UPDATES							= "mineopoly.check-for-updates";
	public static final String	C_MAX_PLAYERS							= "mineopoly.game.max-players";
	public static final String	C_MIN_PLAYERS							= "mineopoly.game.min-players";
	public static final String	C_MONEY_SIGN							= "mineopoly.game.money.sign";
	public static final String	C_MONEY_NAME							= "mineopoly.game.money.name.singular";
	public static final String	C_MONEY_NAME_PLURAL						= "mineopoly.game.money.name.plural";
	public static final String	C_SESSION_TIMEOUT						= "mineopoly.session-timeout-minutes";
	public static final String	C_SHOW_TIPS								= "mineopoly.tips.show";
	public static final String	C_TIP_INTERVAL							= "mineopoly.tips.interval";
	public static final String	C_USE_MONEY_SIGN						= "mineopoly.game.money.use-sign";
	public static final String	C_WIN_METHOD							= "mineopoly.game.win-method";
	public static final String	C_WIN_REWARD							= "mineopoly.game.reward";
	public static final String	C_METRICS_SEND							= "mineopoly.metrics.send_data";
//	public static final String C_VOTING_INTERVAL = "mineopoly.voting.interval";
	public static final String	C_VOTING_TIME_LIMIT						= "mineopoly.voting.time-limit";
	
	public static final String	C_BOARD_SECTION_LENGTH					= "space-length";
	public static final String	C_BOARD_SECTION_WIDTH					= "space-width";
	public static final String	C_BOARD_HOUSE_MIN_X						= "house-%house.min.x";
	public static final String	C_BOARD_HOUSE_MIN_Y						= "house-%house.min.y";
	public static final String	C_BOARD_HOUSE_MIN_Z						= "house-%house.min.z";
	public static final String	C_BOARD_HOUSE_MAX_X						= "house-%house.max.x";
	public static final String	C_BOARD_HOUSE_MAX_Y						= "house-%house.max.y";
	public static final String	C_BOARD_HOUSE_MAX_Z						= "house-%house.max.z";
	public static final String	C_BOARD_HOTEL_MIN_X						= "hotel.min.x";
	public static final String	C_BOARD_HOTEL_MIN_Y						= "hotel.min.y";
	public static final String	C_BOARD_HOTEL_MIN_Z						= "hotel.min.z";
	public static final String	C_BOARD_HOTEL_MAX_X						= "hotel.max.x";
	public static final String	C_BOARD_HOTEL_MAX_Y						= "hotel.max.y";
	public static final String	C_BOARD_HOTEL_MAX_Z						= "hotel.max.z";
	public static final String	C_BOARD_NEEDS_PASTE						= "needs-paste";
	public static final String	C_BOARD_JAIL_SPAWN_X					= "jail-spawn.x";
	public static final String	C_BOARD_JAIL_SPAWN_Z					= "jail-spawn.z";
	public static final String	C_BOARD_VISITING_SPAWN_X				= "visiting-spawn.x";
	public static final String	C_BOARD_VISITING_SPAWN_Z				= "visiting-spawn.z";
	public static final String	C_JAIL_LENGTH							= "jail-length";
	public static final String	C_JAIL_WIDTH							= "jail-width";
	public static final String	C_CONFIG_NAME							= "config";
	public static final String	C_SCHEMA_WORLD							= "world";
	public static final String	C_SCHEMA_NEEDS_PASTE					= "needs_paste";
	public static final String	C_SCHEMA_ORIGIN_X						= "origin-x";
	public static final String	C_SCHEMA_ORIGIN_Y						= "origin-y";
	public static final String	C_SCHEMA_ORIGIN_Z						= "origin-z";
	public static final String	C_OWNERSHIP_PIECE_X						= "ownership-piece.x";
	public static final String	C_OWNERSHIP_PIECE_Z						= "ownership-piece.z";
	
	//House Rule Constants
	public static final String	HR_BAIL_PRICE							= "bail-price";											//done
//	public static final String HR_BANK_LOAN_AMOUNT = "bank-loan-amount";
//	public static final String HR_BANK_LOAN_DEFINABLE = "bank-loan-definable";
//	public static final String HR_BANK_LOANS = "bank-loans";
	public static final String	HR_COLLECT_WHILE_JAILED					= "collect-while-jailed";									//done
	public static final String	HR_HOUSE_SYNCRONIZATION					= "house-sycronization";									//done
	public static final String	HR_LAND_ON_GO_BONUS						= "land-on-go-bonus";										//done
	public static final String	HR_MONEY_CAP							= "money-cap";												//done
	public static final String	HR_IMPROVMENT_REQUIRES_LOCATION			= "improvement-requires-location";							//done
	public static final String	HR_IMPROVMENT_REQUIRES_MONOPOLY			= "improvement-requires-monopoly";							//done
	public static final String	HR_IMPROVE_WHILE_JAILED					= "improve-while-jailed";									//done
	public static final String	HR_PURCHASE_AFTER_GO_PASSES				= "purchase-after-go-passes";								//done
	public static final String	HR_STARTING_MONEY						= "starting-money";										//done
	public static final String	HR_TIME_LIMIT							= "time-limit-minutes";									//done
	public static final String	HR_TRADE_ANYTIME						= "trade-anytime";											//done
	public static final String	HR_TRADE_WHILE_JAILED					= "trade-while-jailed";									//done
	public static final String	HR_TRAVELING_RAILROADS					= "traveling-railroads";									//done
	public static final String	HR_VOTING_ALLOWED						= "voting-allowed";
	
	//believe it or not this section of code was in MineopolyHelper first
	public static String		N_GO									= "square.go";
	public static String		N_PURPLE_1								= "property.purple_1";
	public static String		N_PURPLE_2								= "property.purple_2";
	public static String		N_TAX_INCOME							= "tax.income";
	public static String		N_RR_1									= "railroad_1";
	public static String		N_LIGHT_BLUE_1							= "property.lightBlue_1";
	public static String		N_LIGHT_BLUE_2							= "property.lightBlue_2";
	public static String		N_LIGHT_BLUE_3							= "property.lightBlue_3";
	public static String		N_JAIL									= "square.jail";
	public static String		N_MAGENTA_1								= "property.magenta_1";
	public static String		N_UTILITY_ELECTRIC						= "utility.electric";
	public static String		N_MAGENTA_2								= "property.magenta_2";
	public static String		N_MAGENTA_3								= "property.magenta_3";
	public static String		N_RR_2									= "railroad_2";
	public static String		N_ORANGE_1								= "property.orange_1";
	public static String		N_ORANGE_2								= "property.orange_2";
	public static String		N_ORANGE_3								= "property.orange_3";
	public static String		N_FREE_PARKING							= "square.freeParking";
	public static String		N_RED_1									= "property.red_1";
	public static String		N_RED_2									= "property.red_2";
	public static String		N_RED_3									= "property.red_3";
	public static String		N_RR_3									= "railroad_3";
	public static String		N_YELLOW_1								= "property.yellow_1";
	public static String		N_UTILITY_WATER							= "utility.water";
	public static String		N_YELLOW_2								= "property.yellow_2";
	public static String		N_YELLOW_3								= "property.yellow_3";
	public static String		N_GO_TO_JAIL							= "square.goToJail";
	public static String		N_GREEN_1								= "property.green_1";
	public static String		N_GREEN_2								= "property.green_2";
	public static String		N_GREEN_3								= "property.green_3";
	public static String		N_RR_4									= "railroad_4";
	public static String		N_BLUE_1								= "property.blue_1";
	public static String		N_TAX_LUXURY							= "tax.luxury";
	public static String		N_BLUE_2								= "property.blue_2";
	
	//Permission Constants
	public static final String	P_BAN_PLAYER_FROM_GAME					= "Mineopoly.admin.ban";
	public static final String	P_CHANNEL_CHAT							= "Mineopoly.general.chat";
	public static final String	P_END_GAME								= "Mineopoly.admin.end";
	public static final String	P_FORCE_ADD_PLAYER						= "Mineopoly.admin.force-add";
	public static final String	P_JOIN_GAME								= "Mineopoly.general.game";
	public static final String	P_KICK_PLAYER_FROM_GAME					= "Mineopoly.admin.kick";
	public static final String	P_RELOAD								= "Mineopoly.admin.reload";
	public static final String	P_RESUME_GAME							= "Mineopoly.admin.resume";
	public static final String	P_SAVE_GAME								= "Mineopoly.admin.save";
	public static final String	P_SET_PASTE_LOCATION					= "Mineopoly.admin.set-paste-location";
	public static final String	P_START_GAME							= "Mineopoly.admin.start";
	public static final String	P_SWITCH_BOARD							= "Mineopoly.admin.switch-board";
	public static final String	P_SWITCH_CARDS							= "Mineopoly.admin.switch-cards";
	public static final String	P_SWITCH_CONFIG							= "Mineopoly.admin.switch-config";
	public static final String	P_UNBAN_PLAYER_FROM_GAME				= "Mineopoly.admin.unban";
	public static final String	P_VIEW_PLAYER_QUEUE						= "Mineopoly.admin.queue";
	public static final String	P_VIEW_GAME_STATS						= "Mineopoly.general.stats";
	
	public static final String	L_CANNOT_USE_COMMAND_JAILED				= "error.player.invalid_command.jailed";
	public static final String	L_GAME_NOT_RUNNING						= "error.game.not_running";
	public static final String	L_INVALID_TURN							= "error.player.invalid_turn";
	public static final String	L_NOT_PLAYING							= "error.player.not_playing.self";
	public static final String	L_NO_DEEDS								= "error.player.no_deeds.self";
	public static final String	L_NOT_ONLINE							= "error.player.not_online";
	public static final String	L_PLAYER_NO_DEEDS						= "error.player.no_deeds.player";
	public static final String	L_PLAYER_NOT_PLAYING					= "error.player.not_playing.player";
	public static final String	L_ROLL_BEFORE_ENDING_TURN				= "error.player.roll_before.ending_turn";
	public static final String	L_NEGATIVE_MONEY						= "error.player.money.negative";
	public static final String	L_TRADE_STILL_ACTIVE					= "error.trade.still_active";
	public static final String	L_CANNOT_PLAY_BANNED					= "error.player.cannot_play.banned";
	public static final String	L_ALREADY_PLAYING						= "error.player.already_playing.self";
	public static final String	L_ALREADY_QUEUED						= "error.queue.already_queued.self";
	public static final String	L_ADDED_TO_QUEUE						= "success.queue.added_to_queue.self";
	public static final String	L_ADDED_TO_QUEUE_SERVER					= "success.queue.added_to_queue.server";
	public static final String	L_ALREADY_IN_CHANNEL					= "error.channel.already_listening.self";
	public static final String	L_ADDED_TO_CHANNEL						= "success.channel.added_to_channel.self";
	public static final String	L_CANNOT_LEAVE_CHANNEL_PLAYING			= "error.channel.cannot_leave.playing.self";
	public static final String	L_REMOVED_FROM_CHANNEL					= "error.channel.removed_from_channel.self";
	public static final String	L_NOT_IN_CHANNEL						= "error.channel.not_in_channel.self";
	public static final String	L_NO_MONOPOLIES							= "error.player.no_monopolies.self";
	public static final String	L_PLAYER_NO_MONOPOLIES					= "error.player.no_monopolies.player";
	public static final String	L_CANNOT_PERFORM_ACTION					= "error.player.cannot_perform_action";
	public static final String	L_HR_TR_NOT_ENABLED						= "error.game.house_rule.not_enabled.traveling_railroads";
	public static final String	L_RR_ACROSS_UNOWNED						= "error.player.hr_tr.rr_across_unowned";
	public static final String	L_RR_CURRENT_UNOWNED					= "error.player.hr_tr.rr_current_unowned";
	public static final String	L_ROLL_BEFORE_TRAVEL					= "error.player.roll_before.travel";
	public static final String	L_QUEUE_EMPTY							= "error.queue.empty";
	public static final String	L_SPECIFY_PLAYER_TO_BAN					= "error.admin.ban.specify_player";
	public static final String	L_ALREADY_BANNED						= "error.admin.ban.already_banned";
	public static final String	L_PLAYER_BANNED							= "success.admin.ban.player_banned";
	public static final String	L_SPECIFY_PLAYER_TO_UNBAN				= "error.admin.unban.specify_player";
	public static final String	L_NOT_BANNED							= "error.admin.unban.not_banned";
	public static final String	L_PLAYER_UNBANNED						= "success.admin.unban.player_unbanned";
	public static final String	L_SPECIFY_BOARD							= "error.admin.board.specify_board";
	public static final String	L_NO_BOARD_YML							= "error.admin.board.no_yml";
	public static final String	L_BOARD_SWITCHED						= "success.admin.switch_board.board_switched";
	public static final String	L_SPECIFY_CONFIG						= "error.admin.switch_config.specify_config";
	public static final String	L_CONFIG_SWITCHED						= "success.admin.switch_config.config_switched";
	public static final String	L_CONFIGS_RELOADED						= "success.admin.relaod_config";
	public static final String	L_SPECIFY_PLAYER_TO_ADD					= "error.admin.force_add.specify_player";
	public static final String	L_PLAYER_ADDED_TO_GAME					= "success.admin.force_add.player_added";
	public static final String	L_CANNOT_ADD_BANNED						= "error.admin.force_add.cannot_add.banned";
	public static final String	L_PLAYER_ALREADY_PLAYING				= "error.admin.force_add.cannot_add.already_playing";
	public static final String	L_PLAYER_NOT_FOUND						= "error.player.not_found";
	public static final String	L_PLAYER_KICKED							= "success.admin.kick.player_kicked";
	public static final String	L_SPECIFY_SAVE_GAME						= "error.admin.resume.specify_save";
	public static final String	L_STARTING_SAVE_GAME					= "success.admin.resume.starting_game";
	public static final String	L_CANNOT_RESUME_GAME_NOT_ENOUGH_PLAYERS	= "error.admin.resume.cannot_start.not_enough_players";
	public static final String	L_SAVE_NON_EXISTANT						= "error.admin.resume.save_non_existant";
	public static final String	L_NOT_LOADED_FROM_SAVE					= "error.admin.save.not_loaded_from_save";
	public static final String	L_SAVING_GAME							= "success.admin.save.saving_game";
	public static final String	L_DONE									= "success.general.done";
	public static final String	L_PASTE_LOCATION_SAVED					= "success.admin.set_paste_location.location_saved";
	public static final String	L_ALREADY_GAME_RUNNING					= "error.admin.start.already_game_running";
	public static final String	L_NOT_ENOUGH_PLAYERS					= "error.admin.start.not_enough_players";
	public static final String	L_NOT_JAILED							= "error.player.jail.not_jailed";
	public static final String	L_INSUFFICIENT_FUNDS					= "error.player.money.insufficient";
	public static final String	L_CANNOT_SWITCH_BOARD_PLAYING			= "error.admin.switch_board.cannot_switch.game_running";
	public static final String	L_										= "";
	
	public static String money(int amount) {
		return money(amount, true);
	}
	
	public static String money(int amount, boolean color) {
		if(Mineopoly.config.getBoolean(C_USE_MONEY_SIGN))
			return (color ? "&2" : "") + Mineopoly.config.getString(C_MONEY_SIGN) + amount;
		if(amount == 1)
			return (color ? "&2" : "") + amount + " " + Mineopoly.config.getString(C_MONEY_NAME);
		else
			return (color ? "&2" : "") + amount + " " + Mineopoly.config.getString(C_MONEY_NAME_PLURAL);
	}
	
}
