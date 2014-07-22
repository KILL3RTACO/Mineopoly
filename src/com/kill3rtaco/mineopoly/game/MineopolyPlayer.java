package com.kill3rtaco.mineopoly.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.kill3rtaco.mineopoly.Mineopoly;
import com.kill3rtaco.mineopoly.MineopolyConstants;
import com.kill3rtaco.mineopoly.game.cards.CardResult;
import com.kill3rtaco.mineopoly.game.cards.MineopolyCard;
import com.kill3rtaco.mineopoly.game.chat.MineopolyChannelListener;
import com.kill3rtaco.mineopoly.game.menus.MineopolyMenu;
import com.kill3rtaco.mineopoly.game.sections.ActionProvoker;
import com.kill3rtaco.mineopoly.game.sections.CardSection;
import com.kill3rtaco.mineopoly.game.sections.OwnableSection;
import com.kill3rtaco.mineopoly.game.sections.Property;
import com.kill3rtaco.mineopoly.game.sections.SectionType;
import com.kill3rtaco.mineopoly.game.sections.SpecialSquare;
import com.kill3rtaco.mineopoly.game.sections.squares.FreeParkingSquare;
import com.kill3rtaco.mineopoly.game.sections.squares.JailSquare;
import com.kill3rtaco.mineopoly.game.trading.TradeRequest;
import com.kill3rtaco.tacoapi.TacoAPI;

public class MineopolyPlayer extends MineopolyChannelListener {
	
	private boolean						jailed				= false;
	private boolean						canRoll;
	private boolean						hasRolled			= false;
	private boolean						hasTurn				= false;
	private boolean						needsGoMoney		= false;
	private boolean						payRent				= true;
	private boolean						endTurnAuto			= false;
	private int							money				= Mineopoly.houseRules.startingMoney();
	private int							roll1;
	private int							roll2;
	private int							totalRolls			= 0;
	private int							doubleRolls			= 0;
	private int							jailRolls			= 0;
	private int							goPasses			= 0;
	private int							turnSkips			= 0;
	private boolean						chanceJailCard;
	private boolean						ccJailCard;
	private MineopolySection			sectionOn;
	private ArrayList<OwnableSection>	ownedSections		= new ArrayList<OwnableSection>();
	private ArrayList<TradeRequest>		sentRequests		= new ArrayList<TradeRequest>();
	private ArrayList<TradeRequest>		receivedRequests	= new ArrayList<TradeRequest>();
	private ArrayList<MineopolyColor>	monopolies			= new ArrayList<MineopolyColor>();
	
	public MineopolyPlayer(Player player) {
		super(player);
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setBalance(int amount) {
		this.money = amount;
		sendBalanceMessage();
	}
	
	public void setTurn(boolean turn, boolean supressDoubles) {
		this.hasTurn = turn;
		this.canRoll = turn;
		this.hasRolled = !turn;
		MineopolyGame game = Mineopoly.plugin.getGame();
		if(!turn) {
			if(roll1 == roll2 && !supressDoubles && !jailed) {
				this.hasTurn = true;
				this.canRoll = true;
				game.getChannel().sendMessage("&b" + getName() + " &3has ended their turn but rolled doubles, rolling again...");
				roll();
			} else {
				game.nextPlayer();
			}
		} else { //test if turn is being skipped
			if(turnSkips > 0) {
				game.getChannel().sendMessage("&b" + getName() + " &3 has &b" + turnSkips-- + " &3turns left to be skipped");
				game.nextPlayer();
			}
		}
	}
	
	public void setTurnSkips(int turns) {
		turnSkips = turns;
		if(turnSkips < 0)
			turnSkips = 0; //fix if needed
	}
	
	public int getTurnSkips() {
		return turnSkips;
	}
	
	public int modifyTurnSkips(int amount) {
		turnSkips += amount; //if amount is negative, the number will go down
		if(turnSkips < 0)
			turnSkips = 0; //fix if needed
		return turnSkips;
	}
	
	/**
	 * Gets the amount of money that this player has. Note that this money is separate from any money
	 * earned in the server economy, as the game uses a separate economy to play the game.
	 * @return The amount of money this player has
	 */
	public int getBalance() {
		return this.money;
	}
	
	public boolean hasRolled() {
		return this.hasRolled;
	}
	
	public boolean hasTurn() {
		return this.hasTurn;
	}
	
	private void payRent() {
		if(sectionOn instanceof OwnableSection) {
			OwnableSection section = (OwnableSection) sectionOn;
			if(section.isOwned()) {
				if(section.getOwner().isJailed() && !Mineopoly.houseRules.collectWhileJailed()) {
					Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + section.getOwner() + " &3cannot collect rent from &b" + getName() +
							" &3for landing on " + sectionOn.getColorfulName() + " &3because they are &1jailed", section.getOwner());
					section.getOwner().sendMessage("&3You cannot collect rent from &b" + getName() + " &3for landing on " + sectionOn.getColorfulName() +
							" &3because you are &1jailed");
				} else {
					if(!section.getOwner().getName().equalsIgnoreCase(getName())) {
						Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + getName() + " &3paid &b" + section.getOwner().getName() +
								" &2" + section.getRent() + " &3for landing on " + sectionOn.getColorfulName(), this);
						sendMessage("&3You paid &b" + section.getOwner() + " &2" + section.getRent() + " &3for landing on " + sectionOn.getColorfulName());
						takeMoney(section.getRent());
						section.getOwner().addMoney(section.getRent());
					}
				}
			}
		}
	}
	
	public boolean canBuy(OwnableSection section) {
		return getBalance() >= section.getPrice();
	}
	
	public void getInfo(Player p) {
		int cards = 0;
		if(hasChanceJailCard())
			cards++;
		if(hasCommunityChestJailCard())
			cards++;
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(p, TacoAPI.getChatUtils().createHeader("&3Mineopoly&7: &b" + getName()));
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(p, "&3Money&7: &2" + getBalance());
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(p, "&3Rolls&7: &2" + getTotalRolls() + " &3On space: " + getCurrentSection().getColorfulName() + " &3(&b" + getCurrentSection().getId() + "&3)" +
				(getCurrentSection().getId() == 10 ? (isJailed() ? "&7[&cJail&7]" : "&7[&aJust Visiting&7])") : ""));
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(p, "&3Properties&7: &b" + ownedPropertiesSize());
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(p, "&3Monopolies&7: &b" + monopolySize());
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(p, "&3Get Out of Jail Free cards&7: &2" + (cards > 0 ? cards : "none"));
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(p, "");
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(p, "&8My properties: &7/mineopoly deeds " + getName());
		Mineopoly.plugin.chat.sendPlayerMessageNoHeader(p, "&8My monopolies: &7/mineopoly monopolies " + getName());
	}
	
	public boolean hasMonopoly(MineopolyColor color) {
		if(monopolies.contains(color))
			return true;
		else
			return false;
	}
	
	public void addMoney(int amount) {
		addMoney(amount, true);
		int cap = Mineopoly.houseRules.moneyCap();
		if(cap > 0 && cap > Mineopoly.houseRules.startingMoney()) {
			Mineopoly.plugin.getGame().end(this);
		}
	}
	
	public void addMoney(int amount, boolean sendMessage) {
		this.money += amount;
		if(sendMessage)
			sendBalanceMessage();
	}
	
	public void takeMoney(int amount) {
		takeMoney(amount, true);
	}
	
	public void takeMoney(int amount, boolean sendMessage) {
		this.money -= amount;
		if(sendMessage)
			sendBalanceMessage();
	}
	
	public void sendBalanceMessage() {
		sendMessage("&3Your balance is now &2" + MineopolyConstants.money(getBalance()));
	}
	
	public void payPot(int amount) {
		payPot(amount, true);
	}
	
	public void payPot(int amount, boolean sendMessage) {
		this.money -= amount;
		Mineopoly.plugin.getGame().getBoard().getPot().addMoney(amount);
		if(sendMessage)
			sendBalanceMessage();
	}
	
	public boolean hasMoney(int amount) {
		return getBalance() >= amount;
	}
	
	public void payPlayer(MineopolyPlayer player, int amount) {
		payPlayer(player, amount, true, true);
	}
	
	public void payPlayer(MineopolyPlayer player, int amount, boolean sendMessageSender, boolean sendMessageReceiver) {
		takeMoney(amount, sendMessageSender);
		player.addMoney(amount, sendMessageReceiver);
	}
	
	public void roll() {
		this.totalRolls++;
		this.canRoll = false;
		this.hasRolled = true;
		Random random = new Random();
		this.roll1 = random.nextInt(6) + 1;
		this.roll2 = random.nextInt(6) + 1;
		int sum = roll1 + roll2 + getCurrentSection().getId();
		if(sum > 39) {
			sum -= 40;
		}
		
		Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + getName() + " &3rolled a &b" + roll1 + "&3 and a &b" + roll2, this);
		sendMessage("&3You rolled a &b" + roll1 + "&3 and a &b" + roll2);
		
		if(roll1 == roll2) {
			doubleRolls++;
			if(doubleRolls == 3) {
				doubleRolls = 0;
				setJailed(true, true);
				Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + getName() + "&3 was jailed because they rolled doubles 3 times in a row", this);
				sendMessage("&3You were jailed because you rolled doubles 3 times in a row");
			} else {
				moveTo(sum);
			}
		} else {
			doubleRolls = 0;
			moveTo(sum);
		}
	}
	
	public int getHotels() {
		int hotels = 0;
		for(MineopolySection s : ownedSections()) {
			if(s instanceof Property) {
				if(((Property) s).hasHotel())
					hotels++;
			}
		}
		return hotels;
	}
	
	public int getHouses() {
		int houses = 0;
		for(MineopolySection s : ownedSections()) {
			if(s instanceof Property) {
				houses += ((Property) s).getHouses();
			}
		}
		return houses;
	}
	
//	private void moveForward(int forward) { //name didn't make sense
	private void moveTo(int id) {
		MineopolySection next = Mineopoly.plugin.getGame().getBoard().getSection(id);
		if(!(getCurrentSection() instanceof FreeParkingSquare)) {
			Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + getName() + " &3landed on " + next.getColorfulName(), this);
			sendMessage("&3You landed on " + next.getColorfulName());
		}
		setCurrentSection(next);
	}
	
	public void moveToNearestRailroad() {
//		int id = getCurrentSection().getId();
//		if(id > 35 || id < 5)
//			setCurrentSection(Mineopoly.plugin.getGame().getBoard().getSection(5));
//		else if(id > 5 && id < 15)
//			setCurrentSection(Mineopoly.plugin.getGame().getBoard().getSection(15));
//		else if(id > 15 && id < 25)
//			setCurrentSection(Mineopoly.plugin.getGame().getBoard().getSection(25));
//		else if(id > 25 && id < 35)
//			setCurrentSection(Mineopoly.plugin.getGame().getBoard().getSection(35));
		setCurrentSection(Mineopoly.plugin.getGame().getBoard().getNext(this, SectionType.RAILROAD));
	}
	
	public void moveToNearestUtility() {
//		int id = getCurrentSection().getId();
//		if(id < 12 || id > 24)
//			setCurrentSection(Mineopoly.plugin.getGame().getBoard().getSection(12));
//		else
//			setCurrentSection(Mineopoly.plugin.getGame().getBoard().getSection(24));
		setCurrentSection(Mineopoly.plugin.getGame().getBoard().getNext(this, SectionType.UTILITY));
	}
	
	public void setJailRolls(int rolls) {
		jailRolls = rolls;
	}
	
	public int getJailRolls() {
		return jailRolls;
	}
	
	public int getTotalRolls() {
		return totalRolls;
	}
	
	public void setTotalRolls(int rolls) {
		this.totalRolls = rolls;
	}
	
	public boolean canRoll() {
		return this.canRoll;
	}
	
	public boolean canEndTurnAutomatically() {
		return this.endTurnAuto;
	}
	
	public void setCanEndTurnAutomatically(boolean endTurn) {
		this.endTurnAuto = endTurn;
	}
	
	public void moveWithoutRent(MineopolySection section) {
		payRent = false;
		setCurrentSection(section);
	}
	
	public void setCurrentSection(MineopolySection section) {
		setCurrentSection(section, true);
	}
	
	public void setCurrentSection(MineopolySection section, boolean goMoney) {
		setCurrentSection(section, goMoney, true);
	}
	
	public void setCurrentSection(MineopolySection section, boolean goMoney, boolean process) {
		setCurrentSection(section, goMoney, process, true);
	}
	
	public void setCurrentSection(MineopolySection section, boolean goMoney, boolean process, boolean endAuto) {
//		if(totalRolls > 0 && !jailed)
//			section = Mineopoly.plugin.getGame().getBoard().getNext(this, new SectionType[]{
//					SectionType.CHANCE, SectionType.COMMUNITY_CHEST}[new Random().nextInt(2)]); //testing purposes
		if(process) {
			int lastId = 0;
			if(this.sectionOn != null)
				lastId = this.sectionOn.getId();
			this.sectionOn = section;
			
			if(section.getId() < lastId && section.getId() != 0) {
				if(section instanceof JailSquare) {
					if(isJailed()) {
						needsGoMoney = false;
					}
				}
				needsGoMoney = true;
			} else if(lastId == 0 && totalRolls > 1) {
				needsGoMoney = true;
			} else if(goMoney && totalRolls > 0 && section.getId() == 0 && Mineopoly.houseRules.landOnGoBonus() > 0) {
				int bonus = Mineopoly.houseRules.landOnGoBonus();
				Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + getName() + " &3landed on &6Go &3and was given &2" + MineopolyConstants.money(bonus) + " &3as a bonus", this);
				sendMessage("&3You landed on &6Go &3and were given &2" + bonus + " as a bonus");
				addMoney(bonus);
				needsGoMoney = false;
			}
			
			if(!goMoney)
				needsGoMoney = false;
			
			if(needsGoMoney) {
				goPasses++;
				Mineopoly.plugin.getGame().getChannel().sendMessage("&b" + getName() + " &3passed &6Go &3and was given &2" + MineopolyConstants.money(200) + "", this);
				sendMessage("&3You passed &6Go &3and were given &2" + MineopolyConstants.money(200));
				addMoney(200);
				needsGoMoney = false;
			}
			
			MineopolyCard cardDrawn = null;
			if(section instanceof SpecialSquare) {
				if(section instanceof JailSquare) {
					JailSquare js = (JailSquare) section;
					if(isJailed()) {
						TacoAPI.getPlayerAPI().teleport(getPlayer(), js.getJailCellLocation());
					} else {
						TacoAPI.getPlayerAPI().teleport(getPlayer(), js.getJustVisitingLocation());
						endTurnAuto = true;
					}
				} else {
					TacoAPI.getPlayerAPI().teleport(getPlayer(), section.getLocation());
					SpecialSquare square = (SpecialSquare) section;
					square.provokeAction(this);
				}
			} else if(section instanceof ActionProvoker) {
				TacoAPI.getPlayerAPI().teleport(getPlayer(), section.getLocation());
				ActionProvoker ss = (ActionProvoker) section;
				ss.provokeAction(this);
				if(section instanceof CardSection) {
					CardSection css = (CardSection) section;
					cardDrawn = css.getLastCard();
					if(cardDrawn.getResult() == CardResult.MONEY_RELATED)
						endTurnAuto = true;
				}
			} else if(section instanceof OwnableSection) {
				TacoAPI.getPlayerAPI().teleport(getPlayer(), section.getLocation());
				if(this.payRent && !((OwnableSection) section).isMortgaged())
					payRent();
				this.payRent = true;
				OwnableSection ownable = (OwnableSection) section;
				if(!ownable.isOwned()) {
					String type;
					if(ownable.getType() == SectionType.PROPERTY)
						type = "property";
					else if(ownable.getType() == SectionType.RAILROAD)
						type = "railroad";
					else if(ownable.getType() == SectionType.UTILITY)
						type = "utility";
					else
						type = "space";
					if(canBuy(ownable)) {
						sendMessage("&3You can buy this &b" + type + " &3for &2" + MineopolyConstants.money(ownable.getPrice()) + " &3with &b/" + Mineopoly.getPAlias() + " buy");
					} else {
						sendMessage("&3You do not have enough money to buy this &b" + type + " (&2" + MineopolyConstants.money(ownable.getPrice()) + "&b)");
						endTurnAuto = true;
					}
				} else {
					if(canTravel()) {
						sendMessage("&3You can travel &bacross the board &3from this railroad with &b/" + Mineopoly.getMAlias() + " travel");
					} else {
						endTurnAuto = true;
					}
				}
			}
			if(cardDrawn == null || cardDrawn.getResult() != CardResult.MOVE_RELATED)
				sendMessage("&3Type &b/" + Mineopoly.getPAlias() + " info &3to view information for this space");
			if(endTurnAuto && endAuto) {
				endTurnAutomatically();
			} else if(endAuto && !isJailed()) {
				if(cardDrawn != null && cardDrawn.getResult() != CardResult.MOVE_RELATED)
					if(totalRolls > 0 && !MineopolyCard.eventInProgess)
						sendMessage("&3End your turn with &b/" + Mineopoly.getMAlias() + " et");
			}
		} else {
			this.sectionOn = section;
			TacoAPI.getPlayerAPI().teleport(getPlayer(), section.getLocation());
		}
	}
	
	public void endTurnAutomatically() {
		boolean ate = Mineopoly.options.allowAutomaticTurnEnding();
		if(ate) {
			sendMessage("&aTurn ended automatically");
			getPlayer().chat("/mineopoly end-turn");
		} else {
			sendMessage("&3End your turn with &b/" + Mineopoly.getMAlias() + " et");
		}
		endTurnAuto = false;
	}
	
	public void move(int amount) {
		boolean goMoney = true;
		if(amount < 0)
			goMoney = false;
		int lastId = getCurrentSection().getId();
		MineopolySection next = Mineopoly.plugin.getGame().getBoard().getSection(lastId + amount);
		setCurrentSection(next, goMoney);
	}
	
	public boolean canTravel() {
		if(sectionOn.getType() == SectionType.RAILROAD && Mineopoly.houseRules.travelingRailroads()) {
			int across = sectionOn.getId() + 20;
			OwnableSection rr = (OwnableSection) Mineopoly.plugin.getGame().getBoard().getSection(across);
			if(rr.isOwned() && rr.getOwner().getName().equalsIgnoreCase(this.getName())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public int getOwnedPropertiesWithColor(MineopolyColor color) {
		int amount = 0;
		for(MineopolySection s : ownedSections()) {
			if(s instanceof Property) {
				Property p = (Property) s;
				if(p.getColor() == color)
					amount++;
			}
		}
		return amount;
	}
	
	public boolean ownsCurrentSection() {
		if(getCurrentSection() instanceof OwnableSection) {
			OwnableSection section = (OwnableSection) getCurrentSection();
			return ownsSection(section);
		} else {
			return false;
		}
	}
	
	public int ownedProperties() {
		return ownedSections(SectionType.PROPERTY).size();
	}
	
	public int ownedRailRoads() {
		return ownedSections(SectionType.RAILROAD).size();
	}
	
	public int ownedUtilities() {
		return ownedSections(SectionType.UTILITY).size();
	}
	
	public void setJailed(boolean jailed, boolean ignoreDoubles) {
		this.jailRolls = 0;
		this.jailed = jailed;
		setCurrentSection(Mineopoly.plugin.getGame().getBoard().getSection(10), false, true, false);
		setTurn(false, ignoreDoubles);
	}
	
	public boolean isJailed() {
		return this.jailed;
	}
	
	public void giveChanceJailCard() {
		chanceJailCard = true;
	}
	
	public void takeChanceJailCard() {
		chanceJailCard = false;
	}
	
	public boolean hasChanceJailCard() {
		return chanceJailCard;
	}
	
	public void giveCommunityChestJailCard() {
		ccJailCard = true;
	}
	
	public void takeCommunityChestJailCard() {
		ccJailCard = false;
	}
	
	public boolean hasCommunityChestJailCard() {
		return ccJailCard;
	}
	
	public MineopolySection getCurrentSection() {
		return this.sectionOn;
	}
	
	public boolean ownsSection(int id) {
		MineopolySection section = Mineopoly.plugin.getGame().getBoard().getSection(id);
		if(section instanceof OwnableSection) {
			return ownsSection((OwnableSection) section);
		}
		return false;
	}
	
	public boolean ownsSection(OwnableSection section) {
		return ownedSections.contains(section);
	}
	
	public void addSection(OwnableSection section) {
		ownedSections.add(section);
		if(section instanceof Property) {
			Property p = (Property) section;
			if(p.getColor() == MineopolyColor.BLUE || p.getColor() == MineopolyColor.PURPLE) {
				if(getOwnedPropertiesWithColor(p.getColor()) == 2)
					monopolies.add(p.getColor());
			} else {
				if(getOwnedPropertiesWithColor(p.getColor()) == 3)
					monopolies.add(p.getColor());
			}
		}
	}
	
	public ArrayList<OwnableSection> ownedSections() {
		return ownedSections;
	}
	
	public ArrayList<OwnableSection> ownedSections(SectionType type) {
		if(type != SectionType.PROPERTY && type != SectionType.RAILROAD && type != SectionType.UTILITY) {
			throw new IllegalArgumentException("type must be PROPERTY, RAILROAD, or UTILITY");
		}
		ArrayList<OwnableSection> result = new ArrayList<OwnableSection>();
		if(ownedSections.size() == 0)
			return result;
		for(OwnableSection o : ownedSections) {
			if(o.getType() == type) {
				result.add(o);
			}
		}
		return result;
	}
	
	public int ownedPropertiesSize() {
		return ownedSections.size();
	}
	
	public int valueOfOwnedSections() {
		int amount = 0;
		for(OwnableSection o : ownedSections()) {
			amount += o.getPrice();
			if(o instanceof Property) {
				Property p = (Property) o;
				if(p.hasHotel()) {
					amount += p.getHotelPrice();
				} else if(p.getHouses() > 0) {
					amount += p.getHousePrice() * p.getHouses();
				}
			}
		}
		return amount;
	}
	
	public ArrayList<Property> getPropertiesInMonopoly(MineopolyColor color) {
		return getPropertiesLessHousing(color, 5);
	}
	
	public ArrayList<Property> getPropertiesLessHousing(MineopolyColor color, int houses) {
		ArrayList<Property> result = new ArrayList<Property>();
		for(OwnableSection s : ownedSections) {
			if(s.getType() == SectionType.PROPERTY) {
				Property p = (Property) s;
				if(p.getColor() == color && p.getHouses() < houses)
					result.add(p);
			}
		}
		return result;
	}
	
	public ArrayList<Property> getPropertiesNeedingHousing(Property parent) {
		ArrayList<Property> properties = getPropertiesInMonopoly(parent.getColor());
		int max = 0;
		for(Property p : properties) {
			if(p.getHouses() > max) {
				max = p.getHouses();
			}
		}
		ArrayList<Property> result = new ArrayList<Property>();
		for(Property p : properties) {
			if(p.getHouses() < max && p.getId() != parent.getId()) {
				result.add(p);
			}
		}
		return result;
	}
	
	public ArrayList<Property> getPropertiesCanHaveHouses() {
		ArrayList<Property> properties = new ArrayList<Property>();
		for(OwnableSection s : ownedSections) {
			if(s.getType() != SectionType.PROPERTY)
				continue;
			Property p = (Property) s;
			if(p.canAddHouse())
				properties.add(p);
		}
		return properties;
	}
	
	public ArrayList<Property> getPropertiesCanHaveHotel() {
		ArrayList<Property> properties = new ArrayList<Property>();
		for(OwnableSection s : ownedSections) {
			if(s.getType() != SectionType.PROPERTY)
				continue;
			Property p = (Property) s;
			if(p.canAddHotel())
				properties.add(p);
		}
		return properties;
	}
	
	public int monopolySize() {
		return monopolies.size();
	}
	
	public ArrayList<MineopolyColor> getMonopolies() {
		Collections.sort(monopolies);
		return monopolies;
	}
	
	public OwnableSection getOwnableSection(int id) {
		for(OwnableSection o : ownedSections) {
			if(o.getId() == id) {
				return o;
			}
		}
		return null;
	}
	
	public int getGoPasses() {
		return goPasses;
	}
	
	public void setGoPasses(int passes) {
		this.goPasses = passes;
	}
	
	//this player was sent a request
	public boolean sendRequest(TradeRequest request) {
		if(getReceivedRequest(request.getReceiver().getName()) == null) {
			receivedRequests.add(request);
			return true;
		}
		return false;
	}
	
	//this player sent a request
	public boolean addRequest(TradeRequest request) {
		if(getSentRequest(request.getReceiver().getName()) == null) {
			sentRequests.add(request);
			return true;
		}
		return false;
	}
	
	public TradeRequest getReceivedRequest(String sender) {
		for(TradeRequest r : receivedRequests) {
			if(!r.isCancelled() && r.getSender().getName().equalsIgnoreCase(sender)) {
				return r;
			}
		}
		return null;
	}
	
	public TradeRequest getSentRequest(String receiver) {
		for(TradeRequest r : receivedRequests) {
			if(!r.isCancelled() && r.getReceiver().getName().equalsIgnoreCase(receiver)) {
				return r;
			}
		}
		return null;
	}
	
	public boolean hasRequest() {
		return receivedRequests.size() > 0;
	}
	
	public boolean hasSentRequest() {
		return sentRequests.size() > 0;
	}
	
	public void clearRequestsWithPlayer(String name) {
		ArrayList<TradeRequest> sent = new ArrayList<TradeRequest>();
		ArrayList<TradeRequest> received = new ArrayList<TradeRequest>();
		for(TradeRequest r : sentRequests) {
			if(!r.getReceiver().getName().equalsIgnoreCase(name)) {
				sent.add(r);
			}
		}
		for(TradeRequest r : receivedRequests) {
			if(!r.getSender().getName().equalsIgnoreCase(name)) {
				received.add(r);
			}
		}
		sentRequests = sent;
		receivedRequests = received;
	}
	
	public void showMenu(MineopolyMenu menu) {
		player.closeInventory();
		if(menu != null)
			player.openInventory(menu.getInventory());
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public boolean canAddHouse() {
		if(ownedSections.size() == 0)
			return false;
		for(OwnableSection s : ownedSections) {
			if(s.getType() != SectionType.PROPERTY)
				continue;
			Property p = (Property) s;
			if(p.canAddHouse())
				return true;
		}
		return false;
	}
	
	public boolean canAddHotel() {
		if(ownedSections.size() == 0)
			return false;
		for(OwnableSection s : ownedSections) {
			if(s.getType() != SectionType.PROPERTY)
				continue;
			Property p = (Property) s;
			if(p.canAddHotel())
				return true;
		}
		return false;
	}
	
	public void removeSection(OwnableSection section) {
		for(OwnableSection s : ownedSections) {
			if(s.getId() == section.getId()) {
				ownedSections.remove(s);
				break;
			}
		}
	}
	
	public ArrayList<TradeRequest> getSentRequests() {
		return sentRequests;
	}
	
	public ArrayList<TradeRequest> getRequests() {
		return receivedRequests;
	}
	
	public Location getLocation() {
		return player.getLocation();
	}
	
}
