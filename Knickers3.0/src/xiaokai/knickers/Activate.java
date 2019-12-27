package xiaokai.knickers;

import xiaokai.knickers.base.MakeForm;
import xiaokai.knickers.base.PlayerEvent;
import xiaokai.knickers.module.ModuleManage;
import xiaokai.knickers.money.EconomyAPI;
import xiaokai.knickers.money.EconomyManage;

import java.util.LinkedHashMap;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public class Activate {
	private Knickers mis;
	protected Config config, MainMenu, CommandConfig;
	private EconomyManage money;
	protected Message message;
	private static Activate activate;
	public final static String MessageFileName = "Message.yml", ConfigFileName = "Config.yml",
			MainMenuFileName = "Main.yml", CommandFileName = "Command.yml", ItemIDConfigName = "ItemID.yml",
			EconomyListConfigName = "EconomyList.yml", FormIDFileName = "FormID.yml";
	protected static final String[] loadFile = { ConfigFileName, CommandFileName };
	protected static final String[] defaultFile = { ConfigFileName, CommandFileName, MainMenuFileName,
			MessageFileName };
	public final static String[] FormIDs = { /* 0 */"主页0", /* 1 */"主页1", /* 2 */"显示可供创建的按钮列表界面", /* 3 */"创建按钮" };
	protected FormID FormID;
	protected ModuleManage moduleManage;
	private MakeForm makeForm;
	private LinkedHashMap<String, MyPlayer> Players;

	/**
	 * 插件数据的集合类
	 * 
	 * @param kis
	 */
	public Activate(Knickers kis) {
		activate = this;
		mis = kis;
		new ResCheck(this).start();
		money = new EconomyManage();
		money.addEconomyAPI(new EconomyAPI(this));
		FormID = new FormID();
		moduleManage = new ModuleManage(this);
		kis.getServer().getPluginManager().registerEvents(new PlayerEvent(this), kis);
		Players = new LinkedHashMap<>();
	}

	public void removePlayers(Player player) {
		removePlayers(player.getName());
	}

	public void removePlayers(String player) {
		if (Players.containsKey(player))
			Players.remove(player);
	}

	public void setPlayers(Player player, MyPlayer myPlayer) {
		setPlayers(player.getName(), myPlayer);
	}

	public void setPlayers(String player, MyPlayer myPlayer) {
		Players.put(player, myPlayer);
	}

	public LinkedHashMap<String, MyPlayer> getPlayers() {
		return Players;
	}

	public MakeForm getMakeForm() {
		return makeForm;
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
	 * 返回按钮模块管理器</br>
	 * Return to the button module manager
	 * 
	 * @return
	 */
	public ModuleManage getModuleManage() {
		return moduleManage;
	}

	public FormID getFormID() {
		return FormID;
	}

	public Config getMainMenu() {
		return MainMenu;
	}

	public Message getMessage() {
		return message;
	}

	public static Activate getActivate() {
		return activate;
	}

	/**
	 * 返回EconomyAPI货币的名称
	 * 
	 * @return
	 */
	public String getMoneyName() {
		return config.getString("货币名称");
	}

	public Knickers getKnickers() {
		return mis;
	}

	public Config getConfig() {
		return config;
	}
}
