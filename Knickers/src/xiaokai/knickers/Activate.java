package xiaokai.knickers;

import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public class Activate {
	private Knickers mis;
	protected Config config, MainMenu;
	private Money money = new Money();
	protected Message message;
	private static Activate activate;
	public final static String MessageFileName = "Message.yml", ConfigFileName = "Config.yml",
			MainMenuFileName = "Main.yml", CommandFileName = "Command.yml", ItemIDConfigName = "ItemID.yml";
	protected static final String[] loadFile = { ConfigFileName, CommandFileName, MessageFileName };

	/**
	 * 插件数据的集合类
	 * 
	 * @param kis
	 */
	public Activate(Knickers kis) {
		activate = this;
		mis = kis;
		new ResCheck(this).start();
		money.addEconomyAPI(new EconomyAPI(this));
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
	public MyMoney getEconomy(String EconomyName) {
		return money.getEconomy(EconomyName);
	}
}
