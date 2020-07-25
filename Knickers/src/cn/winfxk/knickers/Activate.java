package cn.winfxk.knickers;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Utils;
import cn.winfxk.knickers.cmd.AdminCommand;
import cn.winfxk.knickers.cmd.MainCommand;
import cn.winfxk.knickers.module.FunctionMag;
import cn.winfxk.knickers.money.EconomyAPI;
import cn.winfxk.knickers.money.EconomyManage;
import cn.winfxk.knickers.money.MyEconomy;
import cn.winfxk.knickers.tool.ItemList;
import cn.winfxk.knickers.tool.Tool;
import cn.winfxk.knickers.tool.Update;

/**
 * 数据集合类
 * 
 * @author Winfxk
 */
public class Activate {
	protected Knickers mis;
	public Player setPlayer;
	public ResCheck resCheck;
	public final static String[] FormIDs = { /* 0 */"主页", /* 1 */"备用主页", /* 2 */ "备用页" };
	public final static String MessageFileName = "Message.yml", ConfigFileName = "Config.yml",
			CommandFileName = "Command.yml", EconomyListConfigName = "EconomyList.yml", FormIDFileName = "FormID.yml",
			PlayerDataDirName = "Players", LanguageDirName = "language", SystemFileName = "System.xml",
			ItemListName = "ItemList.yml", MenuDataDirName = "Menu", MainMenuFileName = "Main.yml",
			FunctionFileName = "Function.yml";
	private Item item;
	private ItemList items;
	private MyThread thread;
	private MyEconomy economy;
	private EconomyManage money;
	private FunctionMag functionMag;
	private static Activate activate;
	private LinkedHashMap<String, MyPlayer> Players;
	protected FormID FormID;
	protected Message message;
	protected Config config, CommandConfig, ItemListConfig, FunctionConfig, MainMenu;
	/**
	 * 默认要加载的配置文件，这些文件将会被用于与插件自带数据匹配
	 */
	protected static final String[] loadFile = { ConfigFileName, CommandFileName };
	/**
	 * 插件基础配置文件
	 */
	protected static final String[] defaultFile = { CommandFileName, MessageFileName, MainMenuFileName, ItemListName };
	/**
	 * 只加载一次的数据
	 */
	protected static final String[] Mkdir = { PlayerDataDirName, MenuDataDirName };

	/**
	 * 插件数据的集合类
	 *
	 * @param kis
	 */
	public Activate(Knickers kis) {
		activate = this;
		mis = kis;
		FormID = new FormID();
		Players = new LinkedHashMap<>();
		if ((resCheck = new ResCheck(this).start()) == null)
			return;
		FunctionConfig = new Config(new File(kis.getDataFolder(), FunctionFileName), Config.YAML);
		money = new EconomyManage();
		ItemListConfig = new Config(new File(mis.getDataFolder(), ItemListName), Config.YAML);
		Plugin plugin = Server.getInstance().getPluginManager().getPlugin(EconomyAPI.Name);
		if (plugin != null)
			money.addEconomyAPI(new EconomyAPI(this));
		economy = money.getEconomy(config.getString("Economy"));
		if (config.getBoolean("检查更新"))
			(new Update(kis)).start();
		items = new ItemList(this);
		functionMag = new FunctionMag(this);
		MainMenu = new Config(new File(kis.getDataFolder(), MainMenuFileName), Config.YAML);
		if (config.getBoolean("ForceTool") && config.getInt("MonitorTime") > 0 && config.getString("Tool") != null
				&& !config.getString("Tool").isEmpty())
			(thread = new MyThread(this)).start();
		kis.getServer().getCommandMap().register(kis.getName(), new MainCommand(this));
		kis.getServer().getCommandMap().register(kis.getName(), new AdminCommand(this));
		kis.getLogger().info(message.getMessage("插件启动", "{loadTime}",
				(float) Duration.between(mis.loadTime, Instant.now()).toMillis() + "ms") + "-Alpha");
	}

	/**
	 * 返回监听线程
	 * 
	 * @return
	 */
	public MyThread getThread() {
		return thread;
	}

	/**
	 * 设置监听线程
	 * 
	 * @param thread
	 */
	public MyThread setThread(MyThread thread) {
		return this.thread = thread;
	}

	/**
	 * 返回主页的配置文件对象
	 * 
	 * @return
	 */
	public Config getMainMenu() {
		return MainMenu;
	}

	/**
	 * 返回模块管理器
	 * 
	 * @return
	 */
	public FunctionMag getFunctionMag() {
		return functionMag;
	}

	/**
	 * 返回功能配置文件
	 * 
	 * @return
	 */
	public Config getFunctionConfig() {
		return FunctionConfig;
	}

	/**
	 * 获取物品列表的管理器
	 * 
	 * @return
	 */
	public ItemList getItems() {
		return items;
	}

	/**
	 * 返回命令列表的配置文件
	 * 
	 * @return
	 */
	public Config getCommandConfig() {
		return CommandConfig;
	}

	/**
	 * 返回物品列表的配置文件
	 * 
	 * @return
	 */
	public Config getItemListConfig() {
		return ItemListConfig;
	}

	/**
	 * 返回一个默认的玩家数据
	 *
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPlayerConfig() throws Exception {
		return resCheck.yaml.loadAs(Utils.readFile(getClass().getResourceAsStream("/resources/player.yml")), Map.class);
	}

	/**
	 * 得到默认经济插件
	 *
	 * @reaturn
	 */
	public MyEconomy getEconomy() {
		return economy;
	}

	/**
	 * 设置默认经济插件
	 *
	 * @param EconomyName
	 */
	public void setEconomy(String EconomyName) {
		if (money.supportEconomy(EconomyName))
			this.economy = money.getEconomy(EconomyName);
	}

	/**
	 * 获取插奸指令
	 * 
	 * @return
	 */
	public String[] getCommand(String string) {
		Object obj = CommandConfig.get(string);
		List<String> list = obj != null && obj instanceof List ? (List<String>) obj
				: obj instanceof Map ? new ArrayList<>(((Map<String, Object>) obj).keySet()) : new ArrayList<>();
		String[] strings = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
			strings[i] = list.get(i);
		return strings;
	}

	/**
	 * 得到插件名称
	 *
	 * @return
	 */
	public String getName() {
		return mis.getName();
	}

	/**
	 * 得到插件主类
	 *
	 * @return
	 */
	public PluginBase getPluginBase() {
		return mis;
	}

	/**
	 * 删除玩家数据
	 *
	 * @param player
	 */
	public void removePlayers(Player player) {
		removePlayers(player.getName());
	}

	/**
	 * 删除玩家数据
	 *
	 * @param player
	 */
	public void removePlayers(String player) {
		if (Players.containsKey(player)) {
			Players.get(player).getConfig().save();
			Players.remove(player);
		}
	}

	/**
	 * 设置玩家数据
	 *
	 * @param player
	 * @return
	 */
	public void setPlayers(Player player, MyPlayer myPlayer) {
		if (!Players.containsKey(player.getName()))
			Players.put(player.getName(), myPlayer);
		myPlayer = Players.get(player.getName());
		myPlayer.setPlayer(player);
	}

	/**
	 * 设置玩家数据
	 *
	 * @param player
	 * @return
	 */
	public MyPlayer getPlayers(Player player) {
		return isPlayers(player.getName()) ? Players.get(player.getName()) : null;
	}

	/**
	 * 设置玩家数据
	 *
	 * @param player
	 * @return
	 */
	public MyPlayer getPlayers(String player) {
		return isPlayers(player) ? Players.get(player) : null;
	}

	/**
	 * 玩家数据是否存在
	 *
	 * @param player
	 * @return
	 */
	public boolean isPlayers(Player player) {
		return Players.containsKey(player.getName());
	}

	/**
	 * 玩家数据是否存在
	 *
	 * @param player
	 * @return
	 */
	public boolean isPlayers(String player) {
		return Players.containsKey(player);
	}

	/**
	 * 得到玩家数据
	 *
	 * @return
	 */
	public LinkedHashMap<String, MyPlayer> getPlayers() {
		return new LinkedHashMap<>(Players);
	}

	/**
	 * 返回经济支持管理器</br>
	 * Return to the economic support manager
	 *
	 * @return
	 */
	public EconomyManage getEconomyManage() {
		return money;
	}

	/**
	 * 得到ID类
	 *
	 * @return
	 */
	public FormID getFormID() {
		return FormID;
	}

	/**
	 * 得到语言类
	 *
	 * @return
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * 对外接口
	 *
	 * @return
	 */
	public static Activate getActivate() {
		return activate;
	}

	/**
	 * 返回EconomyAPI货币的名称
	 *
	 * @return
	 */
	public String getMoneyName() {
		return economy == null ? config.getString("货币名称") : economy.getMoneyName();
	}

	/**
	 * 得到MostBrain主配置文件
	 *
	 * @return
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * 判断玩家是否是管理员
	 * 
	 * @param player
	 * @return
	 */
	public boolean isAdmin(Player player) {
		return isAdmin(player.getName(), false);
	}

	/**
	 * 判断玩家是否是管理员
	 * 
	 * @param player
	 * @return
	 */
	public boolean isAdmin(CommandSender player) {
		return isAdmin(player.getName(), false);
	}

	/**
	 * 判断玩家是否是管理员
	 * 
	 * @param player
	 * @return
	 */
	public boolean isAdmin(CommandSender player, boolean RemoveOP) {
		return isAdmin(player.getName(), RemoveOP);
	}

	/**
	 * 判断玩家是否有快捷工具
	 * 
	 * @param player
	 * @return
	 */
	public boolean isToolItem(Player player) {
		Map<Integer, Item> map = player.getInventory().getContents();
		CompoundTag nbt;
		for (Item item : map.values()) {
			nbt = item.getNamedTag();
			if (nbt == null)
				continue;
			if (nbt.getString(mis.getName()) != null && nbt.getString(mis.getName()).equals("FFFSB"))
				return true;
		}
		return false;
	}

	/**
	 * 判断一个物品是不是快捷工具
	 * 
	 * @param item
	 * @return
	 */
	public boolean isToolItem(Item item) {
		CompoundTag nbt = item.getNamedTag();
		if (nbt == null)
			return false;
		if (nbt.getString(mis.getName()) != null && nbt.getString(mis.getName()).equals("FFFSB"))
			return true;
		return false;
	}

	/**
	 * 返回快捷工具的物品
	 * 
	 * @return
	 */
	public Item getToolItem() {
		if (config.getString("Tool") == null)
			return null;
		if (item == null) {
			String[] string = config.getString("Tool").split(":");
			item = new Item(Tool.ObjToInt(string[0]), Tool.ObjToInt(string[1]));
			CompoundTag nbt = new CompoundTag();
			nbt.putString(mis.getName(), "FFFSB");
			item.setCompoundTag(nbt);
			item.setCustomName(message.getMessage("ToolName"));
			item.setLore(message.getMessage("ToolLore"));
		}
		return item;
	}

	/**
	 * 设置Tool物品
	 * 
	 * @param item
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * 判断玩家是否是管理员
	 * 
	 * @param player
	 * @return
	 */
	public boolean isAdmin(String player, boolean RemoveOP) {
		if (config.getBoolean("OnlyAdmin") || RemoveOP)
			return config.getStringList("Admin").contains(player);
		return config.getStringList("Admin").contains(player) || Server.getInstance().isOp(player);
	}
}
