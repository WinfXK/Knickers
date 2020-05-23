package cn.winfxk.knickers.form;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.winfxk.knickers.MyMap;
import cn.winfxk.knickers.tool.Tool;

/**
 * 功能的基础类
 * 
 * @Createdate 2020/05/22 19:21:32
 * @author Winfxk
 */
public abstract class ButtonBase extends FormBase {
	protected String Key;
	protected File file;
	protected MyMap<String, Object> map;
	protected Config config;

	/**
	 * 创建一个模块界面
	 * 
	 * @param player 打开模块的玩家对象
	 * @param upForm 上个界面
	 * @param file   当前界面的文件对象
	 * @param Key    打开的按钮的Key
	 */
	public ButtonBase(Player player, FormBase upForm, File file, String Key) {
		super(player, upForm);
		this.file = file;
		this.Key = Key;
		config = new Config(file, Config.YAML);
		map = (MyMap<String, Object>) ButtonBase.getButtonMap(config, Key);
	}

	/**
	 * 删除一个按钮
	 * 
	 * @param config 要删除按钮的配置文件对象
	 * @param Key    要上出的按钮的Key
	 * @return
	 */
	public static boolean removeButton(Config config, String Key) {
		Map<String, Object> Buttons = getButtons(config);
		if (!Buttons.containsKey(Key))
			return false;
		Buttons.remove(Key);
		config.set("Button", Buttons);
		return config.save();
	}

	/**
	 * 添加一个按钮
	 * 
	 * @param config 要添加按钮的界面的配置文件对象
	 * @param map    要添加的按钮数据
	 * @param Key    要添加的按钮的Key
	 * @return
	 */
	public static boolean addButtons(Config config, Map<String, Object> map) {
		Map<String, Object> Buttons = getButtons(config);
		String Key = getKey(Buttons);
		map.put("Key", Key);
		Buttons.put(Key, map);
		config.set("Button", Buttons);
		return config.save();
	}

	/**
	 * 获取一个不和当前界面内存在的按钮Key重复的Key
	 * 
	 * @param JJLength
	 * @return
	 */
	private static String getKey(Map<String, Object> Buttons) {
		String string = Tool.getRandString();
		while (Buttons.containsKey(string))
			string += Tool.getRandString();
		return string;
	}

	/**
	 * 返回一个界面的按钮列表
	 * 
	 * @param config 界面的配置文件对象
	 * @return
	 */
	public static Map<String, Object> getButtons(Config config) {
		Object object = config.get("Button");
		return object == null || !(object instanceof Map) ? new MyMap<>() : (Map<String, Object>) object;
	}

	/**
	 * 返回按钮的数据
	 * 
	 * @param Buttons 界面有的按钮总数据
	 * @param Key     要获取数据的按钮Key
	 * @return
	 */
	public static Map<String, Object> getButtonMap(Map<String, Object> Buttons, String Key) {
		if (!Buttons.containsKey(Key))
			return null;
		Object obj = Buttons.get(Key);
		return obj != null && obj instanceof Map ? (HashMap<String, Object>) obj : new HashMap<>();
	}

	/**
	 * 返回按钮的数据
	 * 
	 * @param config 要获取数据的界面配置文件对象
	 * @param Key    要获取数据的按钮Key
	 * @return
	 */
	public static Map<String, Object> getButtonMap(Config config, String Key) {
		return getButtonMap(getButtons(config), Key);
	}

	/**
	 * 返回一个界面的按钮列表
	 * 
	 * @param config 界面的配置文件对象
	 * @return
	 */
	public MyMap<String, Object> getButtons() {
		Object object = config.get("Button");
		return object == null || !(object instanceof Map) ? new MyMap<>() : (MyMap<String, Object>) object;
	}

	/**
	 * 返回当前文件的配置文件对象
	 * 
	 * @return
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * 返回点击的按钮的Key
	 * 
	 * @return
	 */
	public String getKey() {
		return Key;
	}

	/**
	 * 获取按钮数据
	 * 
	 * @return
	 */
	public MyMap<String, Object> getMap() {
		return map;
	}

	/**
	 * 返回当前界面的文件对象
	 * 
	 * @return
	 */
	public File getFile() {
		return file;
	}
}
