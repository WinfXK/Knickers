package cn.winfxk.knickers.rec;

import java.time.Instant;

import cn.nukkit.Player;
import cn.winfxk.knickers.Knickers;
import cn.winfxk.knickers.form.FormBase;

/**
 * @Createdate 2021/07/31 18:18:40
 * @author Winfxk
 */
public class MyPlayer {
	private Player player;
	public FormBase form;
	private static Knickers kis = Knickers.kis;
	public int ID;
	public Instant instant;

	public MyPlayer(Player player) {
		this.player = player;

	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * 返回玩家拥有的钱
	 * 
	 * @return
	 */
	public double getMoney() {
		return kis.getEconomy().getMoney(player);
	}

	/**
	 * 返回玩家的金币数量
	 * 
	 * @param name
	 * @return
	 */
	public static double getMoney(String name) {
		return kis.getEconomy().getMoney(name);
	}
}
