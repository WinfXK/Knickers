package cn.winfxk.knickers.module;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.form.MakeBase;
import cn.winfxk.knickers.money.MyEconomy;
import cn.winfxk.knickers.tool.Tool;

/**
 * 存储一个按钮的数据
 * 
 * @Createdate 2020/05/26 18:22:49
 * @author Winfxk
 */
public class ModuleData implements Cloneable {
	protected Date Date;
	protected double Money;
	protected Server server;
	protected MyEconomy Economy;
	protected Map<String, Object> map;
	protected FunctionBase functionBase;
	protected List<String> Players, Worlds, Command;
	protected static Activate ac = Activate.getActivate();
	protected static FunctionMag functionMag = Activate.getActivate().getFunctionMag();
	protected String ButtonText, Type, Player, DateString, Worldfilter, Playerfilter, Permission;

	public ModuleData(Map<String, Object> map) {
		this.map = map;
		server = Server.getInstance();
		DateString = Tool.objToString(map.get("Date"));
		Date = Tool.parseDate(DateString);
		Type = Tool.objToString(map.get("Type"));
		functionBase = ac.getFunctionMag().getFunction(Type);
		Player = Tool.objToString(map.get("Player"));
		Playerfilter = Tool.objToString(map.get("Playerfilter"));
		Worldfilter = Tool.objToString(map.get("Worldfilter"));
		Permission = Tool.objToString(map.get("Permission"));
		ButtonText = Tool.objToString(map.get("ButtonText"));
		Economy = ac.getEconomyManage().getEconomy(Tool.objToString(map.get("Command")));
		Object obj = map.get("Playerfilterlist");
		Players = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
		obj = map.get("Worldfilterlist");
		Worlds = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
		Money = Tool.objToDouble(map.get("Money"));
		obj = map.get("Command");
		Command = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
	}

	/**
	 * 判断一个玩家是否可以使用按钮
	 * 
	 * @param player
	 * @return
	 */
	public boolean isUsable(Player player) {
		return (isPlayerfilter(player) && isWorldfilter(player.getLevel())) || ac.isAdmin(player, true);
	}

	/**
	 * 判断一个玩家是否可以使用按钮
	 * 
	 * @param player
	 * @return
	 */
	public boolean isPlayerfilter(Player player) {
		return isPlayerfilter(player.getName());
	}

	/**
	 * 判断一个玩家是否可以使用按钮
	 * 
	 * @param player
	 * @return
	 */
	public boolean isPlayerfilter(String player) {
		switch (Playerfilter) {
		case MakeBase.Whitelist:
			return Players.contains(player) || ac.isAdmin(player, true);
		case MakeBase.Blacklist:
			return !Players.contains(player) || ac.isAdmin(player, true);
		}
		return true;
	}

	/**
	 * 判断在一个地图是否可以使用这个按钮
	 * 
	 * @param level
	 * @return
	 */
	public boolean isWorldfilter(Level level) {
		return isWorldfilter(level.getFolderName());
	}

	/**
	 * 判断在一个地图是否可以使用这个按钮
	 * 
	 * @param Worlname 地图的名称
	 * @return
	 */
	public boolean isWorldfilter(String Worlname) {
		switch (Worldfilter) {
		case MakeBase.Whitelist:
			return Worlds.contains(Worlname);
		case MakeBase.Blacklist:
			return !Worlds.contains(Worlname);
		}
		return true;
	}

	/**
	 * 判断一个玩家是否是OP
	 * 
	 * @param player
	 * @return
	 */
	public boolean isPermission(Player player) {
		return isPermission(player.getName());
	}

	/**
	 * 判断一个玩家是否是OP
	 * 
	 * @param player
	 * @return
	 */
	public boolean isPermission(String player) {
		switch (Permission) {
		case MakeBase.Permission_Admin:
			return ac.isAdmin(player, true);
		case MakeBase.Permission_Player:
			return ac.isAdmin(player, true) || !server.isOp(player);
		case MakeBase.Permission_OP:
			return ac.isAdmin(player, true) || server.isOp(player);
		}
		return true;
	}

	/**
	 * 返回按钮的文本内容
	 * 
	 * @return
	 */
	public String getButtonText() {
		return ButtonText;
	}

	/**
	 * 返回按钮点击后会执行的命令列表
	 * 
	 * @return
	 */
	public List<String> getCommand() {
		return Command;
	}

	/**
	 * 返回按钮的创建时间
	 * 
	 * @return
	 */
	public Date getDate() {
		return Date;
	}

	/**
	 * 返回按按钮创建的时间（文本）
	 * 
	 * @return
	 */
	public String getDateString() {
		return DateString;
	}

	/**
	 * 若按钮点击后需要扣费，则返回扣费货币
	 * 
	 * @return
	 */
	public MyEconomy getEconomy() {
		return Economy;
	}

	/**
	 * 返回按钮类
	 * 
	 * @return
	 */
	public FunctionBase getFunctionBase() {
		return functionBase;
	}

	/**
	 * 返回点击按钮将会扣除的金币数量
	 * 
	 * @return
	 */
	public double getMoney() {
		return Money;
	}

	/**
	 * 返回创建按钮的玩家名称
	 * 
	 * @return
	 */
	public String getPlayer() {
		return Player;
	}

	/**
	 * 返回玩家过滤名单
	 * 
	 * @return
	 */
	public List<String> getPlayers() {
		return Players;
	}

	/**
	 * 返回按钮类型
	 * 
	 * @return
	 */
	public String getType() {
		return Type;
	}

	/**
	 * 返回地图过滤名单
	 * 
	 * @return
	 */
	public List<String> getWorlds() {
		return Worlds;
	}

	/**
	 * 返回按钮的所有数据
	 * 
	 * @return
	 */
	public Map<String, Object> getMap() {
		return map;
	}

	@Override
	public ModuleData clone() throws CloneNotSupportedException {
		return (ModuleData) super.clone();
	}
}
