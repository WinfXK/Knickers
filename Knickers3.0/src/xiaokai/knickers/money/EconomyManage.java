package xiaokai.knickers.money;

import xiaokai.knickers.Activate;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public class EconomyManage {
	private LinkedHashMap<String, MyEconomy> Economy;
	private Config config;

	public EconomyManage() {
		Economy = new LinkedHashMap<>();
		config = new Config(
				new File(Activate.getActivate().getKnickers().getDataFolder(), Activate.EconomyListConfigName),
				Config.YAML);
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
		if (!supportEconomy(EconomyName))
			return null;
		return Economy.get(EconomyName);
	}

	/**
	 * 是否已经支持某个经济插件</br>
	 * An Economy plug-in is already supported
	 * 
	 * @param EconomyName
	 * @return
	 */
	public boolean supportEconomy(String EconomyName) {
		return Economy.containsKey(EconomyName);
	}

	/**
	 * 添加经济插件支持</br>
	 * Add Economy plug-in support
	 * 
	 * @param economy
	 * @return
	 */
	public boolean addEconomyAPI(EconomyAPI economy) {
		if (Economy.containsKey(economy.getEconomyName()))
			return false;
		Economy.put(economy.getEconomyName(), economy);
		Write();
		return true;
	}

	/**
	 * 取消Knickers对某个经济插件的支持</br>
	 * Cancel Knickers support for an Economy plug-in
	 * 
	 * @param EconomyName 要取消支持的经济插件名称</br>
	 *                    The Economy plug-in name to unsupport
	 * @return
	 */
	public boolean cancelEconomyAPI(String EconomyName) {
		if (!Economy.containsKey(EconomyName))
			return true;
		Economy.remove(EconomyName);
		Write();
		return !Economy.containsKey(EconomyName);
	}

	/**
	 * 返回已经兼容添加的经济插件对应列表
	 * 
	 * @return
	 */
	public List<String> getEconomy() {
		return new ArrayList<>(Economy.keySet());
	}

	/**
	 * 写入已经支持了的经济插件
	 */
	public void Write() {
		config.set("Economys", new ArrayList<>(Economy.keySet()));
		config.save();
	}
}
