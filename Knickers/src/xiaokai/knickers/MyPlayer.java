package xiaokai.knickers;

/**
*@author Winfxk
*/

import cn.nukkit.Player;

public class MyPlayer {
	private Player player;
	private Activate ac;

	public Player getPlayer() {
		return player;
	}

	/**
	 * 记录存储玩家的一些数据
	 * 
	 * @param player
	 */
	public MyPlayer(Player player) {
		this.player = player;
		ac = Activate.getActivate();
	}

	/**
	 * 获取逗比玩家的金币数量
	 * 
	 * @return
	 */
	public double getMoney() {
		return ac.getEconomy(ac.getConfig().getString("默认货币")).getMoney(player);
	}

	/**
	 * 获取逗比玩家的金币数量
	 * 
	 * @return
	 */
	public static double getMoney(String player) {
		return Activate.getActivate().getEconomy(Activate.getActivate().getConfig().getString("默认货币")).getMoney(player);
	}
}
