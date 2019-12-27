package xiaokai.knickers;

import xiaokai.knickers.module.ModuleManage;
import xiaokai.knickers.money.EconomyAPI;
import xiaokai.knickers.money.EconomyManage;
import xiaokai.knickers.money.MyEconomy;

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
	public final static String[] FormIDs = { /* 0 */"主页0", /* 1 */"主页1" };
	protected FormID FormID;
	protected ModuleManage moduleManage;

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
	}

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

	/**
	 * 添加经济插件支持</br>
	 * Add Economy plug-in support
	 * 
	 * @param economy
	 * @return
	 */
	public boolean addEconomyAPI(EconomyAPI economy) {
		return money.addEconomyAPI(economy);
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

	/**
	 * 获取一个Knickers支持的经济插件</br>
	 * Get a Knickers supported Economy plug-in
	 * 
	 * @param EconomyName 经济插件的名称</br>
	 *                    The name of the Economy plug-in
	 * @return
	 */
	public MyEconomy getEconomy(String EconomyName) {
		return money.getEconomy(EconomyName);
	}
}
