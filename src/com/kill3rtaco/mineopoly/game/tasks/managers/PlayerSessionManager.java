package com.kill3rtaco.mineopoly.game.tasks.managers;

import java.util.Set;
import java.util.HashMap;

import com.kill3rtaco.mineopoly.Mineopoly;

public class PlayerSessionManager {

	private static HashMap<String, Long> players = new HashMap<String, Long>();
	
	public static void addToList(String name){
		players.put(name, System.currentTimeMillis());
	}
	
	public static boolean isInList(String name){
		for(String s : players.keySet()){
			if(s.equalsIgnoreCase(name)){
				return players.get(s) != null && players.get(s) > 0;
			}
		}
		return false;
	}
	
	public static boolean canReturn(String name){
		double minutes = Mineopoly.options.sessionTimeout();
		double timeNeeded = 1000 * (60 * minutes);
		Long time = players.get(name);
		if(time == null) return true;
		long now = System.currentTimeMillis();
		if(now - time <= timeNeeded){
			return true;
		}else{
			return false;
		}
	}
	
	public static Set<String> getPlayers(){
		return players.keySet();
	}
	
	public static void remove(String name){
		players.put(name, null);
	}

}
