package cn.winfxk.knickers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2020/07/06 17:16:05
 * @author Winfxk
 */
public class Message {
	private Config config;
	private Map<String, Object> map;
	private SimpleDateFormat time, date;
	private static Activate ac = Activate.getActivate();
	private static final String ColorStart = "{RGBTextStart}", ColorEnd = "{RGBTextEnd}";
	private static File file = new File(ac.getPluginBase().getDataFolder(), Activate.MessageFileName);
	private static final String[] Key = { "{n}", "{ServerName}", "{PluginName}", "{MoneyName}", "{Time}", "{Date}" };

	public Message(Activate ac, boolean bol) {
		time = new SimpleDateFormat("HH:mm:ss");
		date = new SimpleDateFormat("yyyy-MM-dd");
		file = getFile();
		config = new Config(file, Config.YAML);
		map = config.getAll();
		if (bol)
			ac.getPluginBase().getLogger().info("§6Loading language: §e" + map.get("lang"));
	}

	public Message(Activate ac) {
		this(ac, true);
	}

	/**
	 * 获取孙级语言文本
	 * 
	 * @param t    获取语言文本的键
	 * @param Son  获取语言文本的子键
	 * @param Sun  获取语言文本的孙键
	 * @param form 获取语言文本的UI对象
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, FormBase form) {
		Object object = map.get(t);
		Map<String, Object> map = object != null && object instanceof Map ? (Map<String, Object>) object : null;
		if (map == null)
			return null;
		object = map.get(Son);
		if (object != null && object instanceof Map)
			return getText(((Map<String, Object>) object).get(Sun), form.getK(), form.getD(), form.getPlayer());
		return null;
	}

	/**
	 * 获取孙级语言文本
	 * 
	 * @param t   获取语言文本的键
	 * @param Son 获取语言文本的子键
	 * @param Sun 获取语言文本的孙键
	 * @return
	 */
	public String getSun(String t, String Son, String Sun) {
		Object object = map.get(t);
		Map<String, Object> map = object != null && object instanceof Map ? (Map<String, Object>) object : null;
		if (map == null)
			return null;
		object = map.get(Son);
		if (object != null && object instanceof Map)
			return getText(((Map<String, Object>) object).get(Sun), null, null, null);
		return null;
	}

	/**
	 * 获取孙级语言文本
	 * 
	 * @param t      获取语言文本的键
	 * @param Son    获取语言文本的子键
	 * @param Sun    获取语言文本的孙键
	 * @param player 玩家数据对象
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, Player player) {
		Object object = map.get(t);
		Map<String, Object> map = object != null && object instanceof Map ? (Map<String, Object>) object : null;
		if (map == null)
			return null;
		object = map.get(Son);
		if (object != null && object instanceof Map)
			return getText(((Map<String, Object>) object).get(Sun), null, null, player);
		return null;
	}

	/**
	 * 获取孙级语言文本
	 * 
	 * @param t    获取语言文本的键
	 * @param Son  获取语言文本的子键
	 * @param Sun  获取语言文本的孙键
	 * @param Key  想要替换的变量
	 * @param Data 想要被替换成的量
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, String Key, Object Data) {
		Object object = map.get(t);
		Map<String, Object> map = object != null && object instanceof Map ? (Map<String, Object>) object : null;
		if (map == null)
			return null;
		object = map.get(Son);
		if (object != null && object instanceof Map)
			return getText(((Map<String, Object>) object).get(Sun), new String[] { Key }, new Object[] { Data }, null);
		return null;
	}

	/**
	 * 获取孙级语言文本
	 * 
	 * @param t    获取语言文本的键
	 * @param Son  获取语言文本的子键
	 * @param Sun  获取语言文本的孙键
	 * @param Key  想要替换的变量
	 * @param Data 想要被替换成的量
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, String[] Key, Object[] Data) {
		Object object = map.get(t);
		Map<String, Object> map = object != null && object instanceof Map ? (Map<String, Object>) object : null;
		if (map == null)
			return null;
		object = map.get(Son);
		if (object != null && object instanceof Map)
			return getText(((Map<String, Object>) object).get(Sun), Key, Data, null);
		return null;
	}

	/**
	 * 获取孙级语言文本
	 * 
	 * @param t      获取语言文本的键
	 * @param Son    获取语言文本的子键
	 * @param Sun    获取语言文本的孙键
	 * @param Key    想要替换的变量
	 * @param Data   想要被替换成的量
	 * @param player 玩家数据对象
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, String Key, Object Data, Player player) {
		Object object = map.get(t);
		Map<String, Object> map = object != null && object instanceof Map ? (Map<String, Object>) object : null;
		if (map == null)
			return null;
		object = map.get(Son);
		if (object != null && object instanceof Map)
			return getText(((Map<String, Object>) object).get(Sun), new String[] { Key }, new Object[] { Data },
					player);
		return null;
	}

	/**
	 * 获取孙级语言文本
	 * 
	 * @param t      获取语言文本的键
	 * @param Son    获取语言文本的子键
	 * @param Sun    获取语言文本的孙键
	 * @param Key    想要替换的变量
	 * @param Data   想要被替换成的量
	 * @param player 玩家数据对象
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, String[] Key, Object[] Data, Player player) {
		Object object = map.get(t);
		Map<String, Object> map = object != null && object instanceof Map ? (Map<String, Object>) object : null;
		if (map == null)
			return null;
		object = map.get(Son);
		if (object != null && object instanceof Map)
			return getText(((Map<String, Object>) object).get(Sun), Key, Data, player);
		return null;
	}

	/**
	 * 返回子集语言文本
	 * 
	 * @param t    获取语言文本的键
	 * @param Son  获取语言文本的子键
	 * @param form 获取语言文本的UI对象
	 * @return
	 */
	public String getSon(String t, String Son, FormBase form) {
		Object object = map.get(t);
		if (object == null || !(object instanceof Map))
			return null;
		return getText(((Map<String, Object>) object).get(Son), form.getK(), form.getD(), form.getPlayer());
	}

	/**
	 * 返回子集语言文本
	 * 
	 * @param t      获取语言文本的键
	 * @param Son    获取语言文本的子键
	 * @param player 玩家数据对象
	 * @return
	 */
	public String getSon(String t, String Son, Player player) {
		Object object = map.get(t);
		if (object == null || !(object instanceof Map))
			return null;
		return getText(((Map<String, Object>) object).get(Son), null, null, player);
	}

	/**
	 * 返回子集语言文本
	 * 
	 * @param t   获取语言文本的键
	 * @param Son 获取语言文本的子键
	 * @return
	 */
	public String getSon(String t, String Son) {
		Object object = map.get(t);
		if (object == null || !(object instanceof Map))
			return null;
		return getText(((Map<String, Object>) object).get(Son), null, null, null);
	}

	/**
	 * 返回子集语言文本
	 * 
	 * @param t    获取语言文本的键
	 * @param Son  获取语言文本的子键
	 * @param Key  想要替换的变量
	 * @param Data 想要被替换成的量
	 * @return
	 */
	public String getSon(String t, String Son, String Key, Object Data) {
		Object object = map.get(t);
		if (object == null || !(object instanceof Map))
			return null;
		return getText(((Map<String, Object>) object).get(Son), new String[] { Key }, new Object[] { Data }, null);
	}

	/**
	 * 返回子集语言文本
	 * 
	 * @param t    获取语言文本的键
	 * @param Son  获取语言文本的子键
	 * @param Key  想要替换的变量
	 * @param Data 想要被替换成的量
	 * @return
	 */
	public String getSon(String t, String Son, String[] Key, Object[] Data) {
		Object object = map.get(t);
		if (object == null || !(object instanceof Map))
			return null;
		return getText(((Map<String, Object>) object).get(Son), Key, Data, null);
	}

	/**
	 * 返回子集语言文本
	 * 
	 * @param t      获取语言文本的键
	 * @param Son    获取语言文本的子键
	 * @param Key    想要替换的变量
	 * @param Data   想要被替换成的量
	 * @param player 玩家数据对象
	 * @return
	 */
	public String getSon(String t, String Son, String[] Key, Object[] Data, Player player) {
		Object object = map.get(t);
		if (object == null || !(object instanceof Map))
			return null;
		return getText(((Map<String, Object>) object).get(Son), Key, Data, player);
	}

	/**
	 * 返回语言文本
	 * 
	 * @param t      获取语言文本的键
	 * @param player 玩家数据对象
	 * @return
	 */
	public String getMessage(String t, FormBase form) {
		return getText(map.get(t), form.getK(), form.getD(), form.getPlayer());
	}

	/**
	 * 返回语言文本
	 * 
	 * @param t      获取语言文本的键
	 * @param player 玩家数据对象
	 * @return
	 */
	public String getMessage(String t, Player player) {
		return getText(map.get(t), null, null, player);
	}

	/**
	 * 返回语言文本
	 * 
	 * @param t    获取语言文本的键
	 * @param Key  想要替换的变量
	 * @param Data 想要被替换成的量
	 * @return
	 */
	public String getMessage(String t) {
		return getText(map.get(t), null, null, null);
	}

	/**
	 * 返回语言文本
	 * 
	 * @param t    获取语言文本的键
	 * @param Key  想要替换的变量
	 * @param Data 想要被替换成的量
	 * @return
	 */
	public String getMessage(String t, String Key, Object Data) {
		return getText(map.get(t), new String[] { Key }, new Object[] { Data }, null);
	}

	/**
	 * 返回语言文本
	 * 
	 * @param t    获取语言文本的键
	 * @param Key  想要替换的变量
	 * @param Data 想要被替换成的量
	 * @return
	 */
	public String getMessage(String t, String[] Key, Object[] Data) {
		return getText(map.get(t), Key, Data, null);
	}

	/**
	 * 返回语言文本
	 * 
	 * @param t      获取语言文本的键
	 * @param Key    想要替换的变量
	 * @param Data   想要被替换成的量
	 * @param player 玩家数据对象
	 * @return
	 */
	public String getMessage(String t, String[] Key, Object[] Data, Player player) {
		return getText(map.get(t), Key, Data, player);
	}

	/**
	 * 格式化文本替换
	 * 
	 * @param object 想要格式化替换的文本
	 * @return
	 */
	public String getText(Object object) {
		return getText(object, null, null, null);
	}

	/**
	 * 格式化文本替换
	 * 
	 * @param form 要格式化文本的界面对象
	 * @return
	 */
	public String getText(Object object, FormBase form) {
		return getText(object, form.getK(), form.getD(), form.getPlayer());
	}

	/**
	 * 格式化文本替换
	 * 
	 * @param object 想要格式化替换的文本
	 * @param player 玩家数据对象
	 * @return
	 */
	public String getText(Object object, Player player) {
		return getText(object, null, null, player);
	}

	/**
	 * 格式化文本替换
	 * 
	 * @param object 想要格式化替换的文本
	 * @param Key    想要替换的变量
	 * @param Data   想要被替换成的量
	 * @return
	 */
	public String getText(Object object, String[] Key, Object[] Data) {
		return getText(object, Key, Data, null);
	}

	/**
	 * 格式化文本替换
	 * 
	 * @param object 想要格式化替换的文本
	 * @param Key    想要替换的变量
	 * @param Data   想要被替换成的量
	 * @param player 玩家数据对象
	 * @return
	 */
	public String getText(Object object, String[] Key, Object[] Data, Player player) {
		if (object == null)
			return null;
		String text = String.valueOf(object);
		if (Key != null && Data != null)
			for (int i = 0; i < Key.length && i < Data.length; i++)
				text = text.replace(Key[i], String.valueOf(Data[i]));
		String[] strings = getData();
		for (int i = 0; i < Message.Key.length && i < strings.length; i++)
			text = text.replace(Message.Key[i], strings[i]);
		if (player != null) {
			text = text.replace("{Player}", player.getName());
			text = text.replace("{Money}", String.valueOf(ac.getEconomy().getMoney(player)));
		}
		if (text.contains("{RandColor}")) {
			strings = text.split("\\{RandColor\\}");
			text = "";
			for (String s : strings)
				text += Tool.getRandColor() + s;
		}
		int index, lastindex;
		while (text.contains(ColorStart) && text.contains(ColorEnd)
				&& text.indexOf(ColorStart) < text.lastIndexOf(ColorEnd)) {
			index = text.indexOf(ColorStart) + 1;
			lastindex = text.indexOf(ColorEnd, index);
			text = (index > 0 ? text.substring(0, index - 1) : "")
					+ Tool.getColorFont(Tool.cutString(text, ColorStart, ColorEnd)) + text.substring(lastindex + 12);
		}
		return text;
	}

	/**
	 * 返回全局变量的值
	 * 
	 * @return
	 */
	public String[] getData() {
		return new String[] { "\n", Server.getInstance().getName(), ac.getName(), ac.getMoneyName(),
				time.format(new Date()), date.format(new Date()) };
	}

	/**
	 * 返回语言文件的文件对象
	 * 
	 * @return
	 */
	public static File getFile() {
		return file;
	}
}
