package xiaokai.knickers.money;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Winfxk
 */
public class Money {
	private LinkedHashMap<String, MyMoney> Economy = new LinkedHashMap<>();

	/**
	 * 获取一个Knickers支持的经济插件</br>
	 * Get a Knickers supported Economy plug-in
	 * 
	 * @param EconomyName 经济插件的名称</br>
	 *                    The name of the Economy plug-in
	 * @return
	 */
	public MyMoney getEconomy(String EconomyName) {
		if (!supportEconomy(EconomyName))
			return null;
		return Economy.get(EconomyName);
	}

	/**
	 * 时候已经支持某个经济插件</br>
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
}
