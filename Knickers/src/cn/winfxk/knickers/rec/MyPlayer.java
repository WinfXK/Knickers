package cn.winfxk.knickers.rec;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
	private static boolean Whitelist;
	private static List<String> Whitelists;
	public transient boolean SecurityPermissions;
	static {
		Whitelist = Knickers.kis.config.getBoolean("Whitelist");
		Object obj = Knickers.kis.config.get("Whitelists");
		Whitelists = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
	}

	public MyPlayer(Player player) {
		this.player = player;

	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * 显示一个界面
	 * 
	 * @param form
	 * @return
	 */
	public boolean show(FormBase form) {
		this.form = form;
		return this.form.MakeMain();
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

	/**
	 * 判断玩家是否是管理员
	 * 
	 * @return
	 */
	public boolean isAdmin() {
		if (Whitelist)
			return Whitelists.contains(player.getName());
		return player.isOp();
	}

	public Player getPlayer() {
		return player;
	}

	/**
	 * 判断玩家是否是管理员
	 * 
	 * @return
	 */
	public static boolean isAdmin(Player player) {
		if (Whitelist)
			return Whitelists.contains(player.getName());
		return player.isOp();
	}
}
