package com.kill3rtaco.mineopoly.game.config;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.WinMethod;
import com.kill3rtaco.tacoapi.api.TacoConfig;

public class MineopolyOptionsConfig extends TacoConfig{

	public MineopolyOptionsConfig() {
		super(new File(Mineopoly.plugin.getDataFolder() + "/config/" + Mineopoly.config.getConfigName() + "/options.yml"));
	}

	@Override
	public void setDefaults() {
		int minute = 60;
		addDefaultValue(MineopolyConstants.C_AUTO_TURN_END, false);
		addDefaultValue(MineopolyConstants.C_ADD_BANNED, false);
		addDefaultValue(MineopolyConstants.C_CHECK_UPDATES, true);
		addDefaultValue(MineopolyConstants.C_MAX_PLAYERS, 8);
		addDefaultValue(MineopolyConstants.C_METRICS_SEND, true);
		addDefaultValue(MineopolyConstants.C_MIN_PLAYERS, 2);
		addDefaultValue(MineopolyConstants.C_SESSION_TIMEOUT, 2);
		addDefaultValue(MineopolyConstants.C_SHOW_TIPS, true);
		addDefaultValue(MineopolyConstants.C_TIP_INTERVAL, minute * 2);
//		addDefaultValue(MineopolyConstants.C_VOTING_INTERVAL, 0);
		addDefaultValue(MineopolyConstants.C_VOTING_TIME_LIMIT, minute);
		addDefaultValue(MineopolyConstants.C_WIN_METHOD, "MONEY");
		addDefaultValue(MineopolyConstants.C_WIN_REWARD, 0);
		save();
	}
	
	public boolean addEvenWhenBanned(){
		return getBoolean(MineopolyConstants.C_ADD_BANNED);
	}
	
	public boolean allowAutomaticTurnEnding(){
		return getBoolean(MineopolyConstants.C_AUTO_TURN_END);
	}
	
	public boolean checkForUpdates(){
		return getBoolean(MineopolyConstants.C_CHECK_UPDATES);
	}

	public int maxPlayers(){
		return getInt(MineopolyConstants.C_MAX_PLAYERS);
	}

	public int minPlayers(){
		return getInt(MineopolyConstants.C_MIN_PLAYERS);
	}
	
	public double sessionTimeout(){
		return getDouble(MineopolyConstants.C_SESSION_TIMEOUT);
	}
	
	public boolean showTips(){
		return getBoolean(MineopolyConstants.C_SHOW_TIPS);
	}
	
	public int tipInterval(){
		return getInt(MineopolyConstants.C_TIP_INTERVAL);
	}
	
	public boolean useMetrics(){
		return getBoolean(MineopolyConstants.C_METRICS_SEND);
	}
	
//	public int votingInterval(){
//		return getInt(MineopolyConstants.C_VOTING_INTERVAL);
//	}
	
	public int votingTimeLimit(){
		return getInt(MineopolyConstants.C_VOTING_TIME_LIMIT);
	}
	
	public WinMethod winMethod(){
		return WinMethod.getWinMethod(getString(MineopolyConstants.C_WIN_METHOD));
	}
	
	public double winReward(){
		return getDouble(MineopolyConstants.C_WIN_REWARD);
	}
	
	public void reload(){
		config = YamlConfiguration.loadConfiguration(new File(Mineopoly.plugin.getDataFolder() + "/config/" + Mineopoly.config.getConfigName() + "/options.yml"));
		setDefaults();
		save();
	}
	
}
