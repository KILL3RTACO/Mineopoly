package com.kill3rtaco.mineopoly.game.config;

import java.io.File;
import java.io.IOException;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.tacoapi.api.TacoConfig;

public class MineopolyConfig extends TacoConfig {
	
	public MineopolyConfig() {
		super(new File(Mineopoly.plugin.getDataFolder() + "/config.yml"));
	}
	
	@Override
	protected void setDefaults() {
		addDefaultValue(MineopolyConstants.C_CONFIG_NAME, "default");
		addDefaultValue(MineopolyConstants.C_BOARD_NAME, "default");
		addDefaultValue(MineopolyConstants.C_CARDSET, "default");
		addDefaultValue(MineopolyConstants.C_MONEY_SIGN, "$");
		addDefaultValue(MineopolyConstants.C_MONEY_NAME, "dollar");
		addDefaultValue(MineopolyConstants.C_MONEY_NAME_PLURAL, "dollars");
		addDefaultValue(MineopolyConstants.C_USE_MONEY_SIGN, true);
		addDefaultValue("language", "english");
	}
	
	public String getConfigName() {
		return getString(MineopolyConstants.C_CONFIG_NAME);
	}
	
	public void setConfigName(String name) {
		set(MineopolyConstants.C_CONFIG_NAME, name);
		reload();
	}
	
	public String getBoardName() {
		return getString(MineopolyConstants.C_BOARD_NAME);
	}
	
	public void setBoardName(String name) {
		set(MineopolyConstants.C_BOARD_NAME, name);
	}
	
	public File getCardSetLocation(String type) {
		File cardsDir = new File(Mineopoly.plugin.getDataFolder(), "cards");
		File cardset = new File(cardsDir, getString(MineopolyConstants.C_CARDSET));
		return new File(cardset, type);
		
	}
	
	public void setCardSet(String set) {
		set(MineopolyConstants.C_CARDSET, set);
	}
	
	public File getLanguageFile() {
		String lang = getString("language");
		File f = new File(Mineopoly.plugin.getDataFolder() + "/languages/" + lang + ".yml");
		f.getParentFile().mkdirs();
		try {
			if(!f.exists())
				f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return f;
	}
	
	public boolean ahEdition() {
		if(config.isSet("ah_edition")) {
			return config.getBoolean("ah_edition", false);
		}
		return false;
	}
	
}
