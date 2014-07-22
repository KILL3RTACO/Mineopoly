package com.kill3rtaco.mineopoly.game.config;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.tacoapi.api.TacoConfig;

public class MineopolyHouseRulesConfig extends TacoConfig {

	public MineopolyHouseRulesConfig() {
		super(new File(Mineopoly.plugin.getDataFolder() + "/config/" + Mineopoly.config.getConfigName() + "/house-rules.yml"));
	}

	@Override
	protected void setDefaults() {
		addDefaultValue(MineopolyConstants.HR_BAIL_PRICE, 50);
		addDefaultValue(MineopolyConstants.HR_COLLECT_WHILE_JAILED, false);
		addDefaultValue(MineopolyConstants.HR_HOUSE_SYNCRONIZATION, false);
		addDefaultValue(MineopolyConstants.HR_LAND_ON_GO_BONUS, 0);
		addDefaultValue(MineopolyConstants.HR_MONEY_CAP, 0); //if less than starting money, ignore
		addDefaultValue(MineopolyConstants.HR_IMPROVMENT_REQUIRES_LOCATION, false);
		addDefaultValue(MineopolyConstants.HR_IMPROVMENT_REQUIRES_MONOPOLY, true);
		addDefaultValue(MineopolyConstants.HR_IMPROVE_WHILE_JAILED, false);
		addDefaultValue(MineopolyConstants.HR_PURCHASE_AFTER_GO_PASSES, 0);
		addDefaultValue(MineopolyConstants.HR_STARTING_MONEY, 1500);
		addDefaultValue(MineopolyConstants.HR_TIME_LIMIT, 0); //zero denotes there is no limit
		addDefaultValue(MineopolyConstants.HR_TRADE_ANYTIME, false);
		addDefaultValue(MineopolyConstants.HR_TRADE_WHILE_JAILED, false);
		addDefaultValue(MineopolyConstants.HR_TRAVELING_RAILROADS, false);
		addDefaultValue(MineopolyConstants.HR_VOTING_ALLOWED, true);
	}
	
	public int bailPrice(){
		return getInt(MineopolyConstants.HR_BAIL_PRICE);
	}
	
	public boolean collectWhileJailed(){
		return getBoolean(MineopolyConstants.HR_COLLECT_WHILE_JAILED);
	}
	
	public boolean houseSycronization(){
		return getBoolean(MineopolyConstants.HR_HOUSE_SYNCRONIZATION);
	}
	
	public int landOnGoBonus(){
		return getInt(MineopolyConstants.HR_LAND_ON_GO_BONUS);
	}
	
	public int moneyCap(){
		return getInt(MineopolyConstants.HR_MONEY_CAP);
	}
	
	public boolean improvementRequiresLocation(){
		return getBoolean(MineopolyConstants.HR_IMPROVMENT_REQUIRES_LOCATION);
	}
	
	public boolean improvementRequiresMonopoly(){
		return getBoolean(MineopolyConstants.HR_IMPROVMENT_REQUIRES_MONOPOLY);
	}
	
	public boolean improveWhileJailed(){
		return getBoolean(MineopolyConstants.HR_IMPROVE_WHILE_JAILED);
	}
	
	public int purchaseAfterGoPasses(){
		return getInt(MineopolyConstants.HR_PURCHASE_AFTER_GO_PASSES);
	}
	
	public int startingMoney(){
		return getInt(MineopolyConstants.HR_STARTING_MONEY);
	}
	
	public double timeLimit(){
		return getDouble(MineopolyConstants.HR_TIME_LIMIT);
	}
	
	public boolean tradeAnytime(){
		return getBoolean(MineopolyConstants.HR_TRADE_ANYTIME);
	}
	
	public boolean tradeWhileJailed(){
		return getBoolean(MineopolyConstants.HR_TRADE_WHILE_JAILED);
	}
	
	public boolean travelingRailroads(){
		return getBoolean(MineopolyConstants.HR_TRAVELING_RAILROADS);
	}
	
	public void reload(){
		config = YamlConfiguration.loadConfiguration(new File(Mineopoly.plugin.getDataFolder() + "/config/" + Mineopoly.config.getConfigName() + "/house-rules.yml"));
		setDefaults();
		save();
	}

}
