package cn.winfxk.knickers.money;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;

/**
 * @author Winfxk
 */
public abstract class MyEconomy {
	private String EconomyName, MoneyName;
	protected EconomyManage em;
	private boolean isEnabled = false;

	/**
	 * 经济支持管理 </br>
	 * Economy support management
	 *
	 * @param EconomyName 想要支持的经济插件名称（这应该是唯一的，否则可能会有不可预知的错误）</br>
	 *                    The economic plug-in name you want to support (this should
	 *                    be unique, otherwise there may be unpredictable errors)
	 * @param MoneyName   货币单位的名称，如：金币</br>
	 *                    The name of a unit of money, such as a gold coin
	 */
	public MyEconomy(String EconomyName, String MoneyName) {
		this.EconomyName = EconomyName;
		this.MoneyName = MoneyName;
		em = EconomyManage.em;
	}

	/**
	 * 设置插件启用状态
	 * 
	 * @param isEnabled
	 */
	protected MyEconomy setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		return this;
	}

	/**
	 * 返回货币单位的名称，如：金币 </br>
	 * Returns the name of the Economy unit.Such as: gold COINS
	 *
	 * @return
	 */
	public String getMoneyName() {
		return MoneyName;
	}

	/**
	 * 返回货币的名称 </br>
	 * Returns the name of the Economy
	 *
	 * @return
	 */
	public String getEconomyName() {
		return EconomyName;
	}

	/**
	 * 返回一个玩家拥有的金钱数量</br>
	 * Returns the amount of money a player has
	 *
	 * @param player 要查询金钱数量的玩家名</br>
	 *               The player name to query for the amount of money
	 * @return 当前玩家的金钱数量</br>
	 *         The amount of money a player currently has
	 */
	public double getMoney(CommandSender player) {
		return getMoney(player.getName());
	}

	/**
	 * 返回一个玩家拥有的金钱数量</br>
	 * Returns the amount of money a player has
	 *
	 * @param player 要查询金钱数量的玩家名</br>
	 *               The player name to query for the amount of money
	 * @return 当前玩家的金钱数量</br>
	 *         The amount of money a player currently has
	 */
	public double getMoney(Player player) {
		return getMoney(player.getName());
	}

	/**
	 * 返回一个玩家拥有的金钱数量</br>
	 * Returns the amount of money a player has
	 *
	 * @param player 要查询金钱数量的玩家名</br>
	 *               The player name to query for the amount of money
	 * @return 当前玩家的金钱数量</br>
	 *         The amount of money a player currently has
	 */
	public abstract double getMoney(String player);

	/**
	 * 增加一个玩家的金钱数量</br>
	 * Increases the amount of money a player has
	 *
	 * @param player 要增加金钱的玩家名称</br>
	 *               Player names to add money to
	 * @param Money  要增加金钱的数量</br>
	 *               Increase the amount of money
	 * @return 当前玩家的金钱数量</br>
	 *         The amount of money a player currently has
	 */
	public double addMoney(CommandSender player, double Money) {
		return addMoney(player.getName(), Money);
	}

	/**
	 * 增加一个玩家的金钱数量</br>
	 * Increases the amount of money a player has
	 *
	 * @param player 要增加金钱的玩家名称</br>
	 *               Player names to add money to
	 * @param Money  要增加金钱的数量</br>
	 *               Increase the amount of money
	 * @return 当前玩家的金钱数量</br>
	 *         The amount of money a player currently has
	 */
	public double addMoney(Player player, double Money) {
		return addMoney(player.getName(), Money);
	}

	/**
	 * 增加一个玩家的金钱数量</br>
	 * Increases the amount of money a player has
	 *
	 * @param player 要增加金钱的玩家名称</br>
	 *               Player names to add money to
	 * @param Money  要增加金钱的数量</br>
	 *               Increase the amount of money
	 * @return 当前玩家的金钱数量</br>
	 *         The amount of money a player currently has
	 */
	public abstract double addMoney(String player, double Money);

	/**
	 * 减少一个玩家的金钱数量</br>
	 * Reduces the amount of money a player has
	 *
	 * @param player 要减少金钱的玩家名称</br>
	 *               Player names to reduce money
	 * @param Money  要减少的金钱数量</br>
	 *               The amount of money to be reduced
	 * @return 当前玩家的金钱数量</br>
	 *         The amount of money a player currently has
	 */
	public double reduceMoney(CommandSender player, double Money) {
		return reduceMoney(player.getName(), Money);
	}

	/**
	 * 减少一个玩家的金钱数量</br>
	 * Reduces the amount of money a player has
	 *
	 * @param player 要减少金钱的玩家名称</br>
	 *               Player names to reduce money
	 * @param Money  要减少的金钱数量</br>
	 *               The amount of money to be reduced
	 * @return 当前玩家的金钱数量</br>
	 *         The amount of money a player currently has
	 */
	public double reduceMoney(Player player, double Money) {
		return reduceMoney(player.getName(), Money);
	}

	/**
	 * 减少一个玩家的金钱数量</br>
	 * Reduces the amount of money a player has
	 *
	 * @param player 要减少金钱的玩家名称</br>
	 *               Player names to reduce money
	 * @param Money  要减少的金钱数量</br>
	 *               The amount of money to be reduced
	 * @return 当前玩家的金钱数量</br>
	 *         The amount of money a player currently has
	 */
	public abstract double reduceMoney(String player, double Money);

	/**
	 * 设置一个玩家的金钱数量为某值</br>
	 * Sets the amount of money a player has to a certain value
	 *
	 * @param player 要设置金钱数量的玩家名称</br>
	 *               The player name to set the amount of money
	 * @param Money  要设置成的金钱数量</br>
	 *               The amount of money to set
	 * @return 当前玩家的金钱数量</br>
	 *         The amount of money a player currently has
	 */
	public double setMoney(Player player, double Money) {
		return setMoney(player.getName(), Money);
	}

	/**
	 * 设置一个玩家的金钱数量为某值</br>
	 * Sets the amount of money a player has to a certain value
	 *
	 * @param player 要设置金钱数量的玩家名称</br>
	 *               The player name to set the amount of money
	 * @param Money  要设置成的金钱数量</br>
	 *               The amount of money to set
	 * @return 当前玩家的金钱数量</br>
	 *         The amount of money a player currently has
	 */
	public double setMoney(CommandSender player, double Money) {
		return setMoney(player.getName(), Money);
	}

	/**
	 * 设置一个玩家的金钱数量为某值</br>
	 * Sets the amount of money a player has to a certain value
	 *
	 * @param player 要设置金钱数量的玩家名称</br>
	 *               The player name to set the amount of money
	 * @param Money  要设置成的金钱数量</br>
	 *               The amount of money to set
	 * @return 当前玩家的金钱数量</br>
	 *         The amount of money a player currently has
	 */
	public abstract double setMoney(String player, double Money);

	/**
	 * 该经济支持是否允许欠款
	 * 
	 * @return
	 */
	public abstract boolean allowArrears();

	/**
	 * 判断一个经济支持是否启用
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		return em.supportEconomy(getEconomyName()) && isEnabled;
	}

	/**
	 * 设置货币名称
	 * 
	 * @param economyName
	 */
	public void setEconomyName(String economyName) {
		EconomyName = economyName;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ">>{EconomyName: " + EconomyName + ", MoneyName: " + MoneyName + "}";
	}
}
