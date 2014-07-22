package com.kill3rtaco.mineopoly.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.cards.CardType;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCardSet;
import com.kill3rtaco.mineopoly.game.config.MineopolyBoardConfig;
import com.kill3rtaco.mineopoly.game.sections.CardSection;
import com.kill3rtaco.mineopoly.game.sections.OwnableSection;
import com.kill3rtaco.mineopoly.game.sections.Property;
import com.kill3rtaco.mineopoly.game.sections.Railroad;
import com.kill3rtaco.mineopoly.game.sections.SectionType;
import com.kill3rtaco.mineopoly.game.sections.TaxSection;
import com.kill3rtaco.mineopoly.game.sections.Utility;
import com.kill3rtaco.mineopoly.game.sections.squares.FreeParkingSquare;
import com.kill3rtaco.mineopoly.game.sections.squares.GoSquare;
import com.kill3rtaco.mineopoly.game.sections.squares.GoToJailSquare;
import com.kill3rtaco.mineopoly.game.sections.squares.JailSquare;
import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.obj.WorldEditObject;

public class MineopolyBoard implements Iterable<MineopolySection> {
	
	private ArrayList<MineopolyPlayer>	players;
	private ArrayList<MineopolySection>	sections;
	private MineopolyCardSet			chanceCards;
	private MineopolyCardSet			communityChestCards;
	private MineopolyPot				pot		= new MineopolyPot();
	private MineopolyBoardConfig		config	= Mineopoly.boardConfig;
	
	public MineopolyBoard() {
		Mineopoly.plugin.chat.out("[Game] [Board] Loading game board...");
		Location origin = config.origin();
		if(config.needsPaste()) {
			WorldEditObject we = TacoAPI.getWorldEditAPI();
			Mineopoly.plugin.chat.sendGlobalMessage("&ePasting Mineopoly board... There will be some lag");
			we.pasteSchematic(origin.getWorld().getName(), config.getSchematicLocation(), origin);
			Mineopoly.plugin.chat.sendGlobalMessage("&ePaste Complete");
			config.set(MineopolyConstants.C_BOARD_NEEDS_PASTE, false);
		}
		players = new ArrayList<MineopolyPlayer>();
		sections = new ArrayList<MineopolySection>();
		initSections();
		initCardSets();
		Mineopoly.plugin.chat.out("[Game] [Board] Done loading!");
	}
	
	private void initSections() {
		Mineopoly.plugin.chat.out("[Game] [Board] [Sections] Loading board sections...");
		// property(name, mColor, side, buyPrice, rent[]{site, 1h, 2h, 3h, 4h, hotel})
		sections.add(new GoSquare());
		sections.add(new Property(1, MineopolyConstants.N_PURPLE_1, MineopolyColor.PURPLE, 0, 60, new int[]{
				2, 10, 30, 90, 160, 250}));
		sections.add(new CardSection(2, "Community Chest", 'e', SectionType.COMMUNITY_CHEST, 0));
		sections.add(new Property(3, MineopolyConstants.N_PURPLE_2, MineopolyColor.PURPLE, 0, 60, new int[]{
				4, 20, 60, 180, 320, 450}));
		sections.add(new TaxSection(4, MineopolyConstants.N_TAX_INCOME, '7', 0, 200));
		sections.add(new Railroad(MineopolyConstants.N_RR_1, 0));
		sections.add(new Property(6, MineopolyConstants.N_LIGHT_BLUE_1, MineopolyColor.LIGHT_BLUE, 0, 100, new int[]{
				6, 30, 90, 270, 400, 550}));
		sections.add(new CardSection(7, "Chance", '6', SectionType.CHANCE, 0));
		sections.add(new Property(8, MineopolyConstants.N_LIGHT_BLUE_2, MineopolyColor.LIGHT_BLUE, 0, 100, new int[]{
				6, 30, 90, 270, 400, 550}));
		sections.add(new Property(9, MineopolyConstants.N_LIGHT_BLUE_3, MineopolyColor.LIGHT_BLUE, 0, 120, new int[]{
				8, 40, 100, 300, 450, 600}));
		sections.add(new JailSquare());
		sections.add(new Property(11, MineopolyConstants.N_MAGENTA_1, MineopolyColor.MAGENTA, 1, 140, new int[]{
				10, 50, 150, 450, 625, 750}));
		sections.add(new Utility(12, MineopolyConstants.N_UTILITY_ELECTRIC, '4', 1));
		sections.add(new Property(13, MineopolyConstants.N_MAGENTA_2, MineopolyColor.MAGENTA, 1, 140, new int[]{
				10, 50, 150, 450, 625, 750}));
		sections.add(new Property(14, MineopolyConstants.N_MAGENTA_3, MineopolyColor.MAGENTA, 1, 160, new int[]{
				12, 60, 180, 500, 700, 900}));
		sections.add(new Railroad(MineopolyConstants.N_RR_2, 1));
		sections.add(new Property(16, MineopolyConstants.N_ORANGE_1, MineopolyColor.ORANGE, 1, 180, new int[]{
				14, 70, 200, 550, 750, 950}));
		sections.add(new CardSection(17, "Community Chest", 'e', SectionType.COMMUNITY_CHEST, 1));
		sections.add(new Property(18, MineopolyConstants.N_ORANGE_2, MineopolyColor.ORANGE, 1, 180, new int[]{
				14, 70, 200, 550, 750, 950}));
		sections.add(new Property(19, MineopolyConstants.N_ORANGE_3, MineopolyColor.ORANGE, 1, 200, new int[]{
				16, 80, 220, 600, 800, 1000}));
		sections.add(new FreeParkingSquare());
		sections.add(new Property(21, MineopolyConstants.N_RED_1, MineopolyColor.RED, 2, 220, new int[]{
				18, 90, 250, 700, 875, 1050}));
		sections.add(new CardSection(22, "Chance", '6', SectionType.CHANCE, 2));
		sections.add(new Property(23, MineopolyConstants.N_RED_2, MineopolyColor.RED, 2, 220, new int[]{
				18, 90, 250, 700, 875, 1050}));
		sections.add(new Property(24, MineopolyConstants.N_RED_3, MineopolyColor.RED, 2, 240, new int[]{
				20, 100, 300, 750, 925, 1100}));
		sections.add(new Railroad(MineopolyConstants.N_RR_3, 2));
		sections.add(new Property(26, MineopolyConstants.N_YELLOW_1, MineopolyColor.YELLOW, 2, 260, new int[]{
				22, 110, 330, 800, 975, 1150}));
		sections.add(new Property(27, MineopolyConstants.N_YELLOW_2, MineopolyColor.YELLOW, 2, 260, new int[]{
				22, 110, 330, 800, 975, 1150}));
		sections.add(new Utility(28, MineopolyConstants.N_UTILITY_WATER, '1', 2));
		sections.add(new Property(29, MineopolyConstants.N_YELLOW_3, MineopolyColor.YELLOW, 2, 280, new int[]{
				22, 120, 360, 850, 1025, 1200}));
		sections.add(new GoToJailSquare());
		sections.add(new Property(31, MineopolyConstants.N_GREEN_1, MineopolyColor.GREEN, 3, 300, new int[]{
				26, 130, 390, 900, 110, 1275}));
		sections.add(new Property(32, MineopolyConstants.N_GREEN_2, MineopolyColor.GREEN, 3, 300, new int[]{
				26, 130, 390, 900, 110, 1275}));
		sections.add(new CardSection(33, "Community Chest", 'e', SectionType.COMMUNITY_CHEST, 3));
		sections.add(new Property(34, MineopolyConstants.N_GREEN_3, MineopolyColor.GREEN, 3, 320, new int[]{
				28, 150, 450, 1000, 1200, 1400}));
		sections.add(new Railroad(MineopolyConstants.N_RR_4, 3));
		sections.add(new CardSection(36, "Chance", '6', SectionType.CHANCE, 3));
		sections.add(new Property(37, MineopolyConstants.N_BLUE_1, MineopolyColor.BLUE, 3, 350, new int[]{
				35, 175, 500, 1100, 1300, 1500}));
		sections.add(new TaxSection(38, "Luxury Tax", '7', 3, 75));
		sections.add(new Property(39, MineopolyConstants.N_BLUE_2, MineopolyColor.BLUE, 3, 400, new int[]{
				50, 200, 600, 1400, 1700, 2000}));
		Mineopoly.plugin.chat.out("[Game] [Board] [Sections] Done!");
	}
	
	public void initCardSets() {
		chanceCards = new MineopolyCardSet(CardType.CHANCE, Mineopoly.config.getCardSetLocation("chance"), "Chance");
		communityChestCards = new MineopolyCardSet(CardType.COMMUNITY_CHEST, Mineopoly.config.getCardSetLocation("communitychest"), "Community Chest");
	}
	
	public MineopolyCardSet getChanceCards() {
		return chanceCards;
	}
	
	public MineopolyCardSet getCommunityChestCards() {
		return communityChestCards;
	}
	
	public Location getLocationFor(int id) {
		return getSection(id).getLocation();
	}
	
	public Location getJailLocation() {
		return ((JailSquare) getSection(10)).getJailCellLocation();
	}
	
	public Location getJustVisitingLocation() {
		return ((JailSquare) getSection(10)).getJustVisitingLocation();
	}
	
	public MineopolySection getSection(String name) {
		for(MineopolySection ms : this) {
			if(ms.getName().replace(" ", "_").equalsIgnoreCase(name)) {
				return ms;
			}
		}
		return null;
	}
	
	public Location getLocationRelative(double x, double y, double z) {
		Location origin = config.origin();
		return origin.add(x, y, z);
	}
	
	public Location getLocationRelativeToCenter(double x, double y, double z) {
		Location center = getCenter();
		return center.add(x, y, z);
	}
	
	public Location getCenter() {
		return config.origin().add(config.size() / 2, 0, config.size() / 2);
	}
	
	public MineopolySection getNext(MineopolyPlayer player, SectionType type) {
		int start = normalizeId(player.getCurrentSection().getId() + 1);
		for(int i = start; i < 40; i++) {
			MineopolySection section = getSection(i);
			if(section.getType() == type)
				return section;
			if(i == 39)
				i = -1; //0 on next pass
		}
		return null;
	}
	
	public int normalizeId(int id) {
		if(id >= 40)
			return id - (40 * (id / 40)); //not using % cause an id of 40 should result in 0 not 1
		else if(id < 0)
			return 0;
		else
			return id;
	}
	
	public MineopolySection getSection(int id) {
		id = normalizeId(id);
		return sections.get(id);
	}
	
	public ArrayList<MineopolySection> getAllSections() {
		return sections;
	}
	
	public MineopolySection getRandomSection() {
		return getRandomSection("");
	}
	
	public MineopolySection getRandomSection(String exclude) {
		Random random = new Random();
		int index = random.nextInt(sections.size());
		MineopolySection potential = sections.get(index);
		if(potential.getName().equalsIgnoreCase(exclude)) {
			return getRandomSection(exclude);
		}
		return potential;
	}
	
	public MineopolySection getRandomSectionNotCurrent() {
		MineopolySection current = Mineopoly.plugin.getGame().getPlayerWithCurrentTurn().getCurrentSection();
		return getRandomSection(current.getName());
	}
	
	public ArrayList<OwnableSection> getOwnableSections() {
		ArrayList<OwnableSection> properties = new ArrayList<OwnableSection>();
		for(MineopolySection section : sections) {
			if(section instanceof OwnableSection) {
				properties.add((OwnableSection) section);
			}
		}
		return properties;
	}
	
	public ArrayList<Property> getProperties() {
		ArrayList<Property> properties = new ArrayList<Property>();
		for(MineopolySection section : sections) {
			if(section instanceof Property) {
				properties.add((Property) section);
			}
		}
		return properties;
	}
	
	public ArrayList<Railroad> getRailroads() {
		ArrayList<Railroad> railroads = new ArrayList<Railroad>();
		for(MineopolySection section : sections) {
			if(section.getType() == SectionType.RAILROAD) {
				railroads.add((Railroad) section);
			}
		}
		return railroads;
	}
	
	public ArrayList<Utility> getUtilities() {
		ArrayList<Utility> utilities = new ArrayList<Utility>();
		utilities.add((Utility) this.sections.get(12));
		utilities.add((Utility) this.sections.get(28));
		return utilities;
	}
	
	public MineopolyPlayer getRandomPlayer(String exclude) {
		Random random = new Random();
		if(players.size() == 1)
			exclude = "";
		int index = random.nextInt(players.size());
		MineopolyPlayer potential = players.get(index);
		if(potential.getName().equalsIgnoreCase(exclude)) {
			return getRandomPlayer(exclude);
		}
		return potential;
	}
	
	public MineopolyPlayer getRandomPlayer() {
		return getRandomPlayer("");
	}
	
	public MineopolyPlayer getRandomPlayerNotCurrent() {
		return getRandomPlayer(Mineopoly.plugin.getGame().getPlayerWithCurrentTurn().getName());
	}
	
	public ArrayList<MineopolyPlayer> getPlayers() {
		return this.players;
	}
	
	public String[] getPlayerList() {
		String[] players = new String[this.players.size()];
		for(int i = 0; i < players.length; i++) {
			players[i] = this.players.get(i).getName();
		}
		return players;
	}
	
	public MineopolyPlayer getPlayer(Player p) {
		return getPlayer(p.getName());
	}
	
	public MineopolyPlayer getPlayer(String name) {
		for(MineopolyPlayer mp : players) {
			if(mp.getName().equalsIgnoreCase(name)) {
				return mp;
			}
		}
		return null;
	}
	
	public void addPlayer(MineopolyPlayer player) {
		this.players.add(player);
		player.getPlayer().setGameMode(GameMode.ADVENTURE);
		player.getPlayer().setAllowFlight(true);
	}
	
	public void removePlayer(MineopolyPlayer player) {
		String name = player.getName();
		for(MineopolyPlayer p : players) {
			if(p.getName().equalsIgnoreCase(player.getName())) {
				p.getPlayer().setAllowFlight(false);
				players.remove(p);
				break;
			}
		}
		for(MineopolyPlayer p : players) {
			p.clearRequestsWithPlayer(name);
		}
		if(Mineopoly.plugin.getGame().getBoard().getPlayers().size() <= 1) {
			Mineopoly.plugin.getGame().end();
		}
	}
	
	public void removeAllPlayers() {
		for(MineopolyPlayer p : players) {
			Player player = p.getPlayer();
			if(player == null)
				continue;
			player.setAllowFlight(false);
			TacoAPI.getPlayerAPI().setToLastGameMode(player);
			Location location = Mineopoly.plugin.getGame().getLastKnownLocation(p.getName());
			if(location != null)
				TacoAPI.getPlayerAPI().teleport(player, location);
			Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "Game mode reverted");
			Mineopoly.plugin.chat.sendPlayerMessageNoHeader(player, "Teleported to last known location");
		}
		players.clear();
	}
	
	public MineopolyPot getPot() {
		return this.pot;
	}
	
	public Location getOrigin() {
		return config.origin();
	}
	
	public int getTotalOwnedProperties() {
		return getTotalOwnedSections(SectionType.PROPERTY);
	}
	
	public int getTotalOwnedUtilities() {
		return getTotalOwnedSections(SectionType.UTILITY);
	}
	
	public int getTotalOwnedRailroads() {
		return getTotalOwnedSections(SectionType.RAILROAD);
	}
	
	public ArrayList<Property> getPropertiesWithColor(MineopolyColor color) {
		ArrayList<Property> properties = new ArrayList<Property>();
		for(OwnableSection o : getOwnableSections()) {
			if(o instanceof Property && ((Property) o).getColor() == color) {
				properties.add((Property) o);
			}
		}
		return properties;
	}
	
	public Property getProperty(MineopolyColor color, int index) {
		ArrayList<Property> properties = getPropertiesWithColor(color);
		if(index < 0 || index > properties.size() - 1)
			return null;
		return properties.get(index);
	}
	
	public Utility getUtility(int index) {
		ArrayList<Utility> utils = getUtilities();
		if(index < 0 || index > utils.size() - 1)
			return null;
		return utils.get(index);
	}
	
	public Railroad getRailroad(int index) {
		ArrayList<Railroad> utils = getRailroads();
		if(index < 0 || index > utils.size() - 1)
			return null;
		return utils.get(index);
	}
	
	private int getTotalOwnedSections(SectionType type) {
		int total = 0;
		for(MineopolyPlayer mp : players) {
			for(OwnableSection s : mp.ownedSections()) {
				if(s.getType() == type) {
					total++;
				}
			}
		}
		return total;
	}
	
	public ArrayList<MineopolyPlayer> getJailedPlayers() {
		ArrayList<MineopolyPlayer> jailed = new ArrayList<MineopolyPlayer>();
		for(MineopolyPlayer mp : players) {
			if(mp.isJailed()) {
				jailed.add(mp);
			}
		}
		return jailed;
	}
	
	@Override
	public Iterator<MineopolySection> iterator() {
		return sections.iterator();
	}
}
