package xiaokai.knickers;

import xiaokai.knickers.tool.Tool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Message {
	private Activate ac;
	private static final String[] Key = { "{n}", "{ServerName}", "{PluginName}", "{MoneyName}", "{Time}", "{Date}" };
	private String[] Data;
	private Config Message;

	/**
	 * 文本变量快速插入♂
	 * 
	 * @param ac
	 */
	public Message(Activate ac) {
		this.ac = ac;
		Message = new Config(getFile(), 2);
		load();
	}

	public static File getFile() {
		return new File(Activate.getActivate().getKnickers().getDataFolder(), Activate.MessageFileName);
	}

	/**
	 * 刷新全局变量的数据
	 */
	private void load() {
		Data = new String[] { "\n", ac.getKnickers().getServer().getMotd(), ac.getKnickers().getName(),
				ac.getMoneyName(), Tool.getTime(), Tool.getDate() };
	}

	/**
	 * 从配置文件中获取三级默认文本并插入数据
	 * 
	 * @param t   一级Key
	 * @param Son 二级Key
	 * @param Sun 三级Key
	 * @return
	 */
	public String getSun(String t, String Son, String Sun) {
		return getSun(t, Son, Sun, new String[] {}, new String[] {});
	}

	/**
	 * 从配置文件中获取三级默认文本并插入数据
	 * 
	 * @param t   一级Key
	 * @param Son 二级Key
	 * @param Sun 三级Key
	 * @param k   要插入的对应变量
	 * @param d   要插入的数据
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, String[] k, Object[] d) {
		if (Message.exists(t) && (Message.get(t) instanceof Map)) {
			HashMap<String, Object> map = (HashMap<String, Object>) Message.get(t);
			if (map.containsKey(Son) && (map.get(Son) instanceof Map)) {
				map = (HashMap<String, Object>) map.get(Son);
				if (map.containsKey(Sun))
					return getText(map.get(Sun).toString(), k, d);
			}
		}
		return null;
	}

	/**
	 * 从配置文件中获取三级默认文本并插入数据
	 * 
	 * @param t      一级Key
	 * @param Son    二级Key
	 * @param Sun    三级Key
	 * @param player 默认处理的玩家数据对象
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, Player player) {
		return getSun(t, Son, Sun, new String[] { "{Player}", "{Money}" },
				new Object[] { player.getName(), MyPlayer.getMoney(player.getName()) });
	}

	/**
	 * 从配置文件中获取三级默认文本并插入数据
	 * 
	 * @param t        一级Key
	 * @param Son      二级Key
	 * @param Sun      三级Key
	 * @param myPlayer 默认处理的玩家数据对象
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, MyPlayer myPlayer) {
		return getSun(t, Son, Sun, new String[] { "{Player}", "{Money}" },
				new Object[] { myPlayer.getPlayer().getName(), myPlayer.getMoney() });
	}

	/**
	 * 从配置文件中获取二级默认文本并插入数据
	 * 
	 * @param t   一级Key
	 * @param Son 二级Key
	 * @return
	 */
	public String getSon(String t, String Son) {
		return getSon(t, Son, new String[] {}, new String[] {});
	}

	/**
	 * 从配置文件中获取二级默认文本并插入数据
	 * 
	 * @param t      一级Key
	 * @param Son    二级Key
	 * @param player 默认处理的玩家数据对象
	 * @return
	 */
	public String getSon(String t, String Son, Player player) {
		return getSon(t, Son, new String[] { "{Player}", "{Money}" },
				new Object[] { player.getName(), MyPlayer.getMoney(player.getName()) });
	}

	/**
	 * 从配置文件中获取二级默认文本并插入数据
	 * 
	 * @param t        一级Key
	 * @param Son      二级Key
	 * @param myPlayer 默认处理的玩家数据对象
	 * @return
	 */
	public String getSon(String t, String Son, MyPlayer myPlayer) {
		return getSon(t, Son, new String[] { "{Player}", "{Money}" },
				new Object[] { myPlayer.getPlayer().getName(), myPlayer.getMoney() });
	}

	/**
	 * 从配置文件中获取二级默认文本并插入数据
	 * 
	 * @param t   一级Key
	 * @param Son 二级Key
	 * @param k   要插入的对应变量
	 * @param d   要插入的数据
	 * @return
	 */
	public String getSon(String t, String Son, String[] k, Object[] d) {
		if (Message.exists(t) && (Message.get(t) instanceof Map)) {
			HashMap<String, Object> map = (HashMap<String, Object>) Message.get(t);
			if (map.containsKey(Son))
				return getText(map.get(Son).toString(), k, d);
		}
		return null;
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 * 
	 * @param t      一级Key
	 * @param player 默认处理的玩家数据对象
	 * @return
	 */
	public String getMessage(String t, Player player) {
		return getMessage(t, new String[] { "{Player}", "{Money}" },
				new Object[] { player.getName(), MyPlayer.getMoney(player.getName()) });
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 * 
	 * @param t        一级Key
	 * @param myPlayer 默认处理的玩家数据对象
	 * @return
	 */
	public String getMessage(String t, MyPlayer myPlayer) {
		return getMessage(t, new String[] { "{Player}", "{Money}" },
				new Object[] { myPlayer.getPlayer().getName(), myPlayer.getMoney() });
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 * 
	 * @param t 一级Key
	 * @param k 要插入的对应变量
	 * @param d 要插入的数据
	 * @return
	 */
	public String getMessage(String t, String[] k, Object[] d) {
		if (Message.exists(t))
			return getText(Message.getString(t), k, d);
		return null;
	}

	/**
	 * 将数据插入文本中
	 * 
	 * @param tex 要插入修改的文本
	 * @return
	 */
	public String getText(String text) {
		return getText(text, new String[] {}, new String[] {});
	}

	/**
	 * 将数据插入文本中
	 * 
	 * @param tex 要插入修改的文本
	 * @param k   对应的变量
	 * @param d   对应的数据
	 * @return
	 */
	public String getText(Object tex, String[] k, Object[] d) {
		load();
		String text = String.valueOf(tex);
		for (int i = 0; i < Key.length; i++)
			if (text.contains(Key[i]))
				text = text.replace(Key[i], Data[i]);
		for (int i = 0; (i < k.length && i < d.length); i++)
			if (text.contains(k[i]))
				text = text.replace(k[i], String.valueOf(d[i]));
		if (text.contains("{RandColor}")) {
			String[] strings = text.split("\\{RandColor\\}");
			text = "";
			for (String s : strings)
				text += (text.isEmpty() ? "" : Tool.getRandColor()) + s;
		}
		if (text.contains("{RGBTextStart}") && text.contains("{RGBTextEnd}")) {
			String s = Tool.cutString(text, "{RGBTextStart}", "{RGBTextEnd}");
			if (s != null && !s.isEmpty())
				text = text.replace("{RGBTextStart}" + s + "{RGBTextEnd}", Tool.getColorFont(TextFormat.clean(s)));
		}
		return text;
	}
}
