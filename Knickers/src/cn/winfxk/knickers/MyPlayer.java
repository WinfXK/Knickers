package cn.winfxk.knickers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.winfxk.knickers.form.FormBase;

/**
 * 玩家数据类
 * 
 * @author Winfxk
 */
public class MyPlayer {
	private static Activate ac = Activate.getActivate();
	public Config config;
	private Player player;
	public FormBase form;
	public int ID = 0;

	/**
	 * 记录存储玩家的一些数据
	 *
	 * @param player
	 */
	public MyPlayer(Player player) {
		this.player = player;
		config = getConfig(getName());
		config = ac.resCheck.Check(this);
		config.set("name", player.getName());
		config.save();
	}

	/**
	 * 显示一个界面
	 * 
	 * @param form
	 * @return
	 */
	public boolean showForm(FormBase form) {
		return (this.form = form).MakeMain();
	}

	/**
	 * 判断玩家是否是管理员
	 * 
	 * @return
	 */
	public boolean isAdmin() {
		return ac.isAdmin(player);
	}

	/**
	 * 返回玩家对象
	 * 
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * 获取逗比玩家的金币数量
	 *
	 * @return
	 */
	public double getMoney() {
		return ac.getEconomy().getMoney(player);
	}

	/**
	 * 获取逗比玩家的金币数量
	 *
	 * @return
	 */
	public static double getMoney(String player) {
		return Activate.getActivate().getEconomy().getMoney(player);
	}

	/**
	 * 返回玩家配置文件
	 * 
	 * @return
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * 得到一个玩家的配置文件对象
	 *
	 * @param player 玩家名称
	 * @return
	 */
	public static Config getConfig(String player) {
		return new Config(getFile(player), Config.YAML);
	}

	/**
	 * 得到一个玩家配置文件的文件对象
	 *
	 * @return
	 */
	public File getFile() {
		return new File(new File(ac.getPluginBase().getDataFolder(), Activate.PlayerDataDirName),
				player.getName() + ".yml");
	}

	/**
	 * 得到一个玩家配置文件的文件对象
	 *
	 * @param player 玩家名称
	 * @return
	 */
	public static File getFile(String player) {
		return new File(new File(Activate.getActivate().getPluginBase().getDataFolder(), Activate.PlayerDataDirName),
				player + ".yml");
	}

	/**
	 * 返回玩家名称
	 * 
	 * @return
	 */
	public String getName() {
		return player.getName();
	}

	/**
	 * 判断玩家的配置文件是否存在
	 *
	 * @param player
	 * @return
	 */
	public static boolean isPlayer(String player) {
		File file = getFile(player);
		return file.exists();
	}

	/**
	 * 将一条信息打包给玩家
	 *
	 * @param player
	 * @param Message
	 * @return 若玩家在线将返回True，否则返回False
	 */
	public static boolean sendMessage(String player, String Message) {
		if (player == null || Message == null || player.isEmpty() || Message.isEmpty() || !isPlayer(player))
			return false;
		if (Activate.getActivate().isPlayers(player)) {
			Activate.getActivate().getPlayers(player).player.sendMessage(Message);
			return true;
		}
		Config config = getConfig(player);
		Object obj = config.get("Message");
		List<Object> list = obj == null || !(obj instanceof List) ? new ArrayList<>() : (ArrayList<Object>) obj;
		list.add(Message);
		config.set("Message", list);
		config.save();
		return false;
	}

	/**
	 * 设置玩家对象
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
}
