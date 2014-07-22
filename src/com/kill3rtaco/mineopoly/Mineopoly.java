package com.kill3rtaco.mineopoly;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.kill3rtaco.mineopoly.commands.AdministrationCommands;
import com.kill3rtaco.mineopoly.commands.GameCommands;
import com.kill3rtaco.mineopoly.commands.JailCommands;
import com.kill3rtaco.mineopoly.commands.PropertyCommands;
import com.kill3rtaco.mineopoly.commands.TradeCommands;
import com.kill3rtaco.mineopoly.commands.VoteCommands;
import com.kill3rtaco.mineopoly.game.MineopolyGame;
import com.kill3rtaco.mineopoly.game.MineopolyQueue;
import com.kill3rtaco.mineopoly.game.config.MineopolyBoardConfig;
import com.kill3rtaco.mineopoly.game.config.MineopolyConfig;
import com.kill3rtaco.mineopoly.game.config.MineopolyHouseRulesConfig;
import com.kill3rtaco.mineopoly.game.config.MineopolyNamesConfig;
import com.kill3rtaco.mineopoly.game.config.MineopolyOptionsConfig;
import com.kill3rtaco.mineopoly.gravitydev.Updater;
import com.kill3rtaco.mineopoly.gravitydev.Updater.UpdateResult;
import com.kill3rtaco.mineopoly.gravitydev.Updater.UpdateType;
import com.kill3rtaco.mineopoly.listener.MineopolyListener;
import com.kill3rtaco.mineopoly.saves.MineopolySaveGame;
import com.kill3rtaco.mineopoly.test.MineopolyPluginTester;
import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.api.TacoPlugin;
import com.kill3rtaco.tacoapi.api.ncommands.CommandManager;

public class Mineopoly extends TacoPlugin {
	
	public static MineopolyBoardConfig		boardConfig;
	public static MineopolyConfig			config;
	public static LanguageConfig			lang;
	public static MineopolyOptionsConfig	options;
	public static MineopolyNamesConfig		names;
	public static MineopolyHouseRulesConfig	houseRules;
	public static CommandManager			commands;
	private MineopolyGame					game;
	public static Mineopoly					plugin;
	private File							banned;
	private ArrayList<String>				bannedPlayers;
	private MineopolyQueue					queue;
	private static String					J_ALIAS, M_ALIAS, P_ALIAS, T_ALIAS,
											V_ALIAS;
	
	@Override
	public void onStop() {
		if(game.isRunning()) {
			game.end();
		}
		chat.out("Disabled");
	}
	
	@Override
	public void onStart() {
		plugin = this;
		reloadConfigurationFiles();
		if(options.checkForUpdates())
			checkVersion();
		queue = new MineopolyQueue();
		commands = new CommandManager(this);
		commands.reg(AdministrationCommands.class);
		commands.reg(GameCommands.class);
		commands.reg(JailCommands.class);
		commands.reg(PropertyCommands.class);
		commands.reg(TradeCommands.class);
		commands.reg(VoteCommands.class);
		registerEvents(new MineopolyListener());
		banned = new File(getDataFolder() + "/banned-players.txt");
		bannedPlayers = getBannedPlayers();
		restartGame();
		if(options.useMetrics())
			startMetrics();
	}
	
	public static void reloadConfigurationFiles() {
		config = new MineopolyConfig();
		lang = new LanguageConfig(config.getLanguageFile());
		options = new MineopolyOptionsConfig();
		names = new MineopolyNamesConfig();
		houseRules = new MineopolyHouseRulesConfig();
		boardConfig = new MineopolyBoardConfig();
	}
	
	public static String getMAlias() {
		if(M_ALIAS == null) {
			M_ALIAS = TacoAPI.getServerAPI().getShortestAlias(plugin, "mineopoly");
		}
		return M_ALIAS;
	}
	
	public static String getPAlias() {
		if(P_ALIAS == null) {
			P_ALIAS = TacoAPI.getServerAPI().getShortestAlias(plugin, "mproperty");
		}
		return P_ALIAS;
	}
	
	public static String getJAlias() {
		if(J_ALIAS == null) {
			J_ALIAS = TacoAPI.getServerAPI().getShortestAlias(plugin, "mjail");
		}
		return J_ALIAS;
	}
	
	public static String getVAlias() {
		if(V_ALIAS == null) {
			V_ALIAS = TacoAPI.getServerAPI().getShortestAlias(plugin, "mvote");
		}
		return V_ALIAS;
	}
	
	public static String getTAlias() {
		if(T_ALIAS == null) {
			T_ALIAS = TacoAPI.getServerAPI().getShortestAlias(plugin, "mtrade");
		}
		return T_ALIAS;
	}
	
	public String getAliases(String command) {
		List<String> aliases = getCommand(command).getAliases();
		return "&b" + TacoAPI.getChatUtils().join(aliases.toArray(new String[]{}), "&7, &b");
	}
	
	public MineopolyQueue getQueue() {
		return queue;
	}
	
	public MineopolyGame getGame() {
		return game;
	}
	
	public void restartGame() {
		restartGame(false);
	}
	
	public void restartGame(boolean runTests) {
		long timeStart = System.currentTimeMillis();
		game = new MineopolyGame();
		long timeEnd = System.currentTimeMillis();
		double time = (timeEnd - timeStart) / 1000;
		if(game.isRunning())
			chat.out("[Game] loaded in " + time + "s");
		if(runTests && game.isRunning())
			MineopolyPluginTester.run();
	}
	
	public void resumeGame(MineopolySaveGame save) {
		long timeStart = System.currentTimeMillis();
		game = new MineopolyGame(save);
		long timeEnd = System.currentTimeMillis();
		double time = (timeEnd - timeStart) / 1000;
		if(game.isRunning())
			chat.out("[Game] loaded in " + time + "s");
		game.setData();
	}
	
	public ArrayList<String> getBannedPlayers() {
		if(bannedPlayers != null) {
			return bannedPlayers;
		} else {
			ArrayList<String> bp = new ArrayList<String>();
			try {
				if(!banned.exists()) {
					chat.out("[Bans] Bans file not found, creating...");
					banned.createNewFile();
					chat.out("[Bans] File created");
					return bp;
				} else {
					chat.out("[Bans] Bans file found, reloading...");
					Scanner x = new Scanner(banned);
					while (x.hasNextLine()) {
						String line = x.nextLine();
						if(!line.isEmpty()) {
							String[] parts = line.split("\\s");
							if(parts.length > 0 && !parts[0].isEmpty()) {
								bp.add(parts[0]);
								continue;
							}
						}
					}
					x.close();
					chat.out("[Bans] Found " + bp.size() + " banned players");
					chat.out("[Bans] Done!");
				}
			} catch (IOException e) {
				return bp;
			}
			return bp;
		}
	}
	
	//for later
	private void checkVersion() {
		Updater updater = new Updater(this, 43431, getFile(), UpdateType.NO_DOWNLOAD, false);
		if(updater.getResult() == UpdateResult.FAIL_APIKEY) {
			
		} else if(updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
			
		} else if(updater.getResult() == UpdateResult.NO_UPDATE) {
			
		}
//		try {
//			URL url = new URL("http://www.kill3rtaco.com/assets/mineopoly/version.txt");
//			Scanner x = new Scanner(url.openStream());
//			String version = null, file = null, type = null, date = null;
//			while (x.hasNextLine()) {
//				String line = x.nextLine();
//				String[] parts = line.split("\\t+");
//				version = parts[0];
//				file = parts[1];
//				type = parts[2];
//				date = parts[3];
//			}
//			x.close();
//			if(version == null || file == null || type == null || date == null) {
//				chat.outWarn("Could not verify version");
//				return;
//			}
//			String[] vParts = getDescription().getVersion().split("\\.");
//			String[] fvParts = version.split("\\.");
//			for(int i = 0; i < fvParts.length; i++) {
//				int j = Integer.parseInt(fvParts[i]); //file
//				int k = Integer.parseInt(vParts[i]); //plugin
//				if(j == k)
//					continue;
//				if(j > k) {
//					chat.outWarn("&e[VersionChecker] This version of Mineopoly (" + getDescription().getVersion() + ") is outdated." +
//							" Mineopoly v" + version + " was released on " + date);
//					chat.outWarn("&e[VersionChecker] You can download the latest version at http://dev.bukkit.org/server-mods/files/" + file);
//					return;
//				} else if(j < k) {
//					chat.outSevere("&c[VersionChecker] How are you using a future version? Are you a dev?");
//					return;
//				}
//			}
//			chat.out("&a[VersionChecker] This is the current version of Mineopoly");
//		} catch (MalformedURLException e) {
//			chat.outWarn("[VersionChecker] Could not verify version");
//		} catch (IOException e) {
//			chat.outWarn("[VersionChecker] Could not verify version");
//		} catch (IndexOutOfBoundsException e) {
//			chat.outWarn("[VersionChecker] Could not verify version");
//		}
	}
	
	public void banPlayer(String name) {
		if(!bannedPlayers.contains(name)) {
			bannedPlayers.add(name);
			writeBansToFile();
		}
	}
	
	public void unbanPlayer(String name) {
		if(bannedPlayers.contains(name)) {
			bannedPlayers.remove(name);
			writeBansToFile();
		}
	}
	
	public boolean isBanned(String name) {
		for(String s : bannedPlayers) {
			if(s.equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	
	private void writeBansToFile() {
		if(banned.exists())
			banned.delete();
		try {
			banned.createNewFile();
			FileWriter writer = new FileWriter(banned);
			for(String s : bannedPlayers) {
				writer.append(s + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
