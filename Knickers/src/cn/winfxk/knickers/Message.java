package cn.winfxk.knickers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.money.MyEconomy;
import cn.winfxk.knickers.tool.Tool;

/**
 * @author Winfxk
 */
public class Message {
	private Activate ac;
	private String[] Data;
	private Map<String, Object> map;
	private Config config;
	private static final String[] Key = { "{n}", "{ServerName}", "{PluginName}", "{MoneyName}", "{Time}", "{Date}" };
	public static final List<String> Dk = new ArrayList<>();
	public static final String EconomyKey = "Economy}";
	static {
		Dk.add("{Player}");
		Dk.add("{Money}");
	}

	public Config getConfig() {
		return config;
	}

	/**
	 * 文本变量快速插入♂
	 *
	 * @param ac
	 */
	public Message(Activate ac) {
		this(ac, true);
	}

	/**
	 * 文本变量快速插入♂
	 *
	 * @param ac
	 */
	public Message(Activate ac, boolean isLog) {
		this.ac = ac;
		config = new Config(getFile(), Config.YAML);
		map = config.getAll();
		if (isLog)
			ac.getPluginBase().getLogger().info("§6Load the language: §e" + config.getString("lang"));
		load();
	}

	/**
	 * 重载语言数据
	 */
	public void reload() {
		config = new Config(getFile(), 2);
		ac.getPluginBase().getLogger().info("§6Load the language: §e" + config.getString("lang"));
		load();
	}

	/**
	 * 得到语言文件的文件对象
	 *
	 * @return
	 */
	public static File getFile() {
		return new File(Activate.getActivate().getPluginBase().getDataFolder(), Activate.MessageFileName);
	}

	/**
	 * 刷新全局变量的数据
	 */
	private void load() {
		Data = new String[] { "\n", ac.getPluginBase().getServer().getMotd(), ac.getPluginBase().getName(),
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
		if (map.containsKey(t) && map.get(t) instanceof Map) {
			HashMap<String, Object> map = (HashMap<String, Object>) this.map.get(t);
			if (map.containsKey(Son) && map.get(Son) instanceof Map) {
				map = (HashMap<String, Object>) map.get(Son);
				if (map.containsKey(Sun))
					return getText(map.get(Sun).toString());
			}
		}
		return null;
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
		return k == null || k.length <= 1 || d == null || d.length <= 1 ? getSon(t, Son)
				: getSun(t, Son, Sun, Arrays.asList(k), Arrays.asList(d));
	}

	/**
	 * 从配置文件中获取三级默认文本并插入数据
	 *
	 * @param t    一级Key
	 * @param Son  二级Key
	 * @param Sun  三级Key
	 * @param form 正在操作的界面
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, FormBase form) {
		return getSun(t, Son, Sun, form.getK(), form.getD());
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
	public String getSun(String t, String Son, String Sun, List<String> k, List<Object> d) {
		if (this.map.containsKey(t) && this.map.get(t) instanceof Map) {
			HashMap<String, Object> map = (HashMap<String, Object>) this.map.get(t);
			if (map.containsKey(Son) && map.get(Son) instanceof Map) {
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
		return player == null ? getSun(t, Son, Sun) : getSun(t, Son, Sun, Dk, getList(player));
	}

	/**
	 * 从配置文件中获取三级默认文本并插入数据
	 *
	 * @param t    一级Key
	 * @param Son  二级Key
	 * @param Sun  三级Key
	 * @param name 要初始化的玩家的名称
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, String player) {
		return player == null ? getSun(t, Son, Sun) : getSun(t, Son, Sun, Dk, getList(player));

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
		return myPlayer == null ? getSun(t, Son, Sun) : getSun(t, Son, Sun, Dk, getList(myPlayer));
	}

	/**
	 * 从配置文件中获取二级默认文本并插入数据
	 *
	 * @param t    一级Key
	 * @param Son  二级Key
	 * @param form 正在显示的界面
	 * @return
	 */
	public String getSon(String t, String Son, FormBase form) {
		return getSon(t, Son, form.getK(), form.getD());
	}

	/**
	 * 从配置文件中获取二级默认文本并插入数据
	 *
	 * @param t   一级Key
	 * @param Son 二级Key
	 * @return
	 */
	public String getSon(String t, String Son) {
		if (this.map.containsKey(t) && this.map.get(t) instanceof Map) {
			HashMap<String, Object> map = (HashMap<String, Object>) this.map.get(t);
			if (map.containsKey(Son))
				return getText(map.get(Son).toString());
		}
		return null;
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
		return getSon(t, Son, Dk, getList(player));
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
		return getSon(t, Son, Dk, getList(myPlayer));
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
		return k == null || k.length <= 1 || d == null || d.length <= 1 ? getSon(t, Son)
				: getSon(t, Son, Arrays.asList(k), Arrays.asList(d));
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
	public String getSon(String t, String Son, List<String> k, List<Object> d) {
		if (this.map.containsKey(t) && this.map.get(t) instanceof Map) {
			HashMap<String, Object> map = (HashMap<String, Object>) this.map.get(t);
			if (map.containsKey(Son))
				return getText(map.get(Son).toString(), k, d);
		}
		return null;
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 *
	 * @param t 一级Key
	 * @return
	 */
	public String getMessage(String t) {
		if (this.map.containsKey(t))
			return getText(this.map.get(t));
		return null;
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 *
	 * @param t      一级Key
	 * @param player 默认处理的玩家数据对象
	 * @return
	 */
	public String getMessage(String t, Player player, FormBase form) {
		return getMessage(t, form.getK(), form.getD());
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 *
	 * @param t      一级Key
	 * @param player 默认处理的玩家数据对象
	 * @return
	 */
	public String getMessage(String t, Player player) {
		return player == null ? getMessage(t) : getMessage(t, Dk, getList(player));
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 *
	 * @param t        一级Key
	 * @param myPlayer 默认处理的玩家数据对象
	 * @return
	 */
	public String getMessage(String t, MyPlayer myPlayer) {
		return getMessage(t, Dk, getList(myPlayer));
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
		return k == null || k.length <= 1 || d == null || d.length <= 1 ? getMessage(t)
				: getMessage(t, Arrays.asList(k), Arrays.asList(d));
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 *
	 * @param t 一级Key
	 * @param k 要插入的对应变量
	 * @param d 要插入的数据
	 * @return
	 */
	public String getMessage(String t, String k, Object d) {
		if (this.map.containsKey(t))
			return getText(this.map.get(t), k, d);
		return null;
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 *
	 * @param t 一级Key
	 * @param k 要插入的对应变量
	 * @param d 要插入的数据
	 * @return
	 */
	public String getMessage(String t, List<String> k, List<Object> d) {
		if (this.map.containsKey(t))
			return getText(this.map.get(t), k, d);
		return null;
	}

	/**
	 * 将数据插入文本中
	 *
	 * @param tex 要插入修改的文本
	 * @return
	 */
	public String getText(Object t, MyPlayer myPlayer) {
		return getText(t, new String[] { "{Player}", "{Money}" },
				new Object[] { myPlayer.getPlayer().getName(), myPlayer.getMoney() });
	}

	/**
	 * 将数据插入文本中
	 *
	 * @param tex 要插入修改的文本
	 * @return
	 */
	public String getText(Object text, Player player) {
		return getText(text, new String[] { "{Player}", "{Money}" },
				new Object[] { player.getName(), MyPlayer.getMoney(player.getName()) });
	}

	/**
	 * 将数据插入文本中
	 *
	 * @param text 要插入修改的文本
	 * @return
	 */
	public String getText(Object text, String k, Object d) {
		if (text == null)
			return null;
		load();
		String string = String.valueOf(text);
		if (string == null || string.isEmpty())
			return null;
		string = string.replace(k, String.valueOf(d));
		for (int i = 0; i < Key.length; i++)
			if (string.contains(Key[i]))
				string = string.replace(Key[i], Data[i]);
		if (string.contains("{RandColor}")) {
			String[] strings = string.split("\\{RandColor\\}");
			string = "";
			for (String s : strings)
				string += Tool.getRandColor() + s;
		}
		if (string.contains("{RGBTextStart}") && string.contains("{RGBTextEnd}")) {
			String rgb = "", rString, gString;
			String[] rgbStrings = string.split("\\{RGBTextEnd\\}");
			for (String rgbString : rgbStrings)
				if (rgbString.contains("{RGBTextStart}")) {
					rString = rgbString + "{RGBTextEnd}";
					gString = Tool.cutString(rString, "{RGBTextStart}", "{RGBTextEnd}");
					if (gString == null || gString.isEmpty())
						gString = "";
					rgb += rString.replace("{RGBTextStart}" + gString + "{RGBTextEnd}", Tool.getColorFont(gString));
				} else
					rgb += rgbString;
			string = rgb;
		}
		return getEconomy(string);
	}

	/**
	 * 将数据插入文本中
	 *
	 * @param text 要插入修改的文本
	 * @return
	 */
	public String getText(Object text) {
		if (text == null)
			return null;
		load();
		String string = String.valueOf(text);
		if (string == null || string.isEmpty())
			return null;
		for (int i = 0; i < Key.length; i++)
			if (string.contains(Key[i]))
				string = string.replace(Key[i], Data[i]);
		if (string.contains("{RandColor}")) {
			String[] strings = string.split("\\{RandColor\\}");
			string = "";
			for (String s : strings)
				string += Tool.getRandColor() + s;
		}
		if (string.contains("{RGBTextStart}") && string.contains("{RGBTextEnd}")) {
			String rgb = "", rString, gString;
			String[] rgbStrings = string.split("\\{RGBTextEnd\\}");
			for (String rgbString : rgbStrings)
				if (rgbString.contains("{RGBTextStart}")) {
					rString = rgbString + "{RGBTextEnd}";
					gString = Tool.cutString(rString, "{RGBTextStart}", "{RGBTextEnd}");
					if (gString == null || gString.isEmpty())
						gString = "";
					rgb += rString.replace("{RGBTextStart}" + gString + "{RGBTextEnd}", Tool.getColorFont(gString));
				} else
					rgb += rgbString;
			string = rgb;
		}
		return getEconomy(string);
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
		return k.length <= 1 || d.length <= 1 ? getText(tex) : getText(tex, Arrays.asList(k), Arrays.asList(d));
	}

	/**
	 * 将数据插入文本中
	 *
	 * @param tex 要插入修改的文本
	 * @param k   对应的变量
	 * @param d   对应的数据
	 * @return
	 */
	public String getText(Object tex, List<String> k, List<Object> d) {
		if (tex == null)
			return null;
		load();
		String text = String.valueOf(tex);
		if (text == null || text.isEmpty())
			return null;
		for (int i = 0; i < Key.length; i++)
			if (text.contains(Key[i]))
				text = text.replace(Key[i], Data[i]);
		for (int i = 0; i < k.size() && i < d.size(); i++)
			if (text.contains(k.get(i)))
				text = text.replace(k.get(i), String.valueOf(d.get(i)));
		if (text.contains("{RandColor}")) {
			String[] strings = text.split("\\{RandColor\\}");
			text = "";
			for (String s : strings)
				text += Tool.getRandColor() + s;
		}
		if (text.contains("{RGBTextStart}") && text.contains("{RGBTextEnd}")) {
			String rgb = "", rString, gString;
			String[] rgbStrings = text.split("\\{RGBTextEnd\\}");
			for (String rgbString : rgbStrings)
				if (rgbString.contains("{RGBTextStart}")) {
					rString = rgbString + "{RGBTextEnd}";
					gString = Tool.cutString(rString, "{RGBTextStart}", "{RGBTextEnd}");
					if (gString == null || gString.isEmpty())
						gString = "";
					rgb += rString.replace("{RGBTextStart}" + gString + "{RGBTextEnd}", Tool.getColorFont(gString));
				} else
					rgb += rgbString;
			text = rgb;
		}
		return getEconomy(text);
	}

	private String getEconomy(String string) {
		if (!string.contains(EconomyKey))
			return string;
		for (MyEconomy economy : ac.getEconomyManage().getEconomys())
			string = string.replace(economy.getEconomyName() + EconomyKey, economy.getMoneyName());
		return string;
	}

	private List<Object> getList(Player player) {
		return getList(player.getName());
	}

	private List<Object> getList(String player) {
		List<Object> list = new ArrayList<>();
		list.add(player);
		list.add(MyPlayer.getMoney(player));
		return list;
	}

	private List<Object> getList(MyPlayer player) {
		if (player == null)
			return new ArrayList<>();
		List<Object> list = new ArrayList<>();
		list.add(player.getPlayer().getName());
		list.add(player.getMoney());
		return list;
	}
}
