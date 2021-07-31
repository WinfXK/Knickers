package cn.winfxk.knickers.money;

import cn.winfxk.knickers.Knickers;

/**
 * @author Winfxk
 */
public class EconomyAPI extends MyEconomy {
	private me.onebone.economyapi.EconomyAPI eApi;
	public final static String Name = "EconomyAPI";

	/**
	 * 增加对EconomyAPI的支持</br>
	 * Added support for EconomyAPI
	 *
	 * @param ac
	 */
	public EconomyAPI(Knickers ac) {
		super(Name, getMoneyEconomyAPIName());
		eApi = me.onebone.economyapi.EconomyAPI.getInstance();
	}

	public static String getMoneyEconomyAPIName() {
		String string = kis.config.getString("EconomyAPI-MoneyName");
		return string != null && !string.isEmpty() ? string : kis.config.getString("MoneyName");
	}

	@Override
	public double getMoney(String player) {
		return eApi.myMoney(player);
	}

	@Override
	public double addMoney(String player, double Money) {
		eApi.addMoney(player, Money);
		return getMoney(player);
	}

	@Override
	public double reduceMoney(String player, double Money) {
		eApi.reduceMoney(player, Money);
		return getMoney(player);
	}

	@Override
	public double setMoney(String player, double Money) {
		eApi.setMoney(player, Money);
		return getMoney(player);
	}

	@Override
	public boolean allowArrears() {
		return false;
	}
}
