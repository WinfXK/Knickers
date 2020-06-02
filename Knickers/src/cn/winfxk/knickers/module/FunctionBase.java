package cn.winfxk.knickers.module;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.Message;
import cn.winfxk.knickers.MyMap;
import cn.winfxk.knickers.MyPlayer;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.money.MyEconomy;
import cn.winfxk.knickers.tool.Tool;

/**
 * 菜单的功能按钮基本类
 * 
 * @Createdate 2020/05/22 19:27:38
 * @author Winfxk
 */
public abstract class FunctionBase {
	public static final String FunctionKey = "Function";
	protected Activate ac;
	protected Message msg;
	private String ModuleKey, Name;
	protected String t = FunctionKey;
	private boolean Enable;
	private Config config;
	protected FunctionMag mag;

	public FunctionBase(Activate ac, String Key) {
		this.ac = ac;
		msg = ac.getMessage();
		this.ModuleKey = Key;
		config = ac.getFunctionConfig();
		Map<String, Object> map = config.getAll();
		if (!map.containsKey(Key)) {
			config.set(Key, true);
			config.save();
		}
		Enable = config.getBoolean(Key);
		Name = getString("Name");
		mag = ac.getFunctionMag();
	}

	/**
	 * 返回按钮的数据
	 * 
	 * @param config 按钮所在的配置文件对象
	 * @param Key    按钮所属的Key
	 * @return
	 */
	public abstract ModuleData getModuleData(Config config, String Key);

	/**
	 * 返回按钮的数据
	 * 
	 * @param file 按钮所在的文件对象
	 * @param Key  按钮所属的Key
	 * @return
	 */
	public ModuleData getModuleData(File file, String Key) {
		return getModuleData(new Config(file, Config.YAML), Key);
	}

	/**
	 * 返回格式化文本
	 * 
	 * @param Key
	 * @return
	 */
	public String getString(String Key, String[] K, Object[] D) {
		return msg.getSun(t, this.ModuleKey, Key, K, D);
	}

	/**
	 * 返回格式化文本
	 * 
	 * @param Key
	 * @return
	 */
	public String getString(String Key, FormBase form) {
		return msg.getSun(t, this.ModuleKey, Key, form, form.getPlayer());
	}

	/**
	 * 返回格式化文本
	 * 
	 * @param Key
	 * @return
	 */
	public String getString(String Key) {
		return msg.getSun(t, this.ModuleKey, Key);
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 */
	protected void setName(String name) {
		Name = name;
	}

	/**
	 * 返回名称
	 * 
	 * @return
	 */
	public String getName() {
		return Name;
	}

	/**
	 * 设置当前设置的Key
	 * 
	 * @param key
	 */
	protected void setModuleKey(String key) {
		ModuleKey = key;
	}

	/**
	 * 返回按钮的类型
	 * 
	 * @return
	 */
	public String getModuleKey() {
		return ModuleKey;
	}

	/**
	 * 创建按钮时调用
	 * 
	 * @param player 创建界面的玩家对象
	 * @param config 创建按钮的界面文件对象
	 * @param form   上个界面
	 * @return
	 */
	public boolean makeButton(Player player, Config config, FormBase form) {
		MyPlayer myPlayer = ac.getPlayers(player);
		if (myPlayer == null) {
			player.sendMessage(msg.getSon(t, "NotPlayer", player));
			return false;
		}
		if (!myPlayer.isAdmin()) {
			player.sendMessage(msg.getMessage("权限不足", player));
			return false;
		}
		return myPlayer.showForm(getMakeForm(player, config, form));
	}

	/**
	 * 返回创建按钮的界面对象
	 * 
	 * @param player 创建按钮的玩家对象
	 * @param config 创建按钮的界面
	 * @param base   上级界面
	 * @return
	 */
	protected abstract FormBase getMakeForm(Player player, Config config, FormBase base);

	/**
	 * 返回按钮文本，创建界面时调用
	 * 
	 * @param form      创建的界面对象
	 * @param file      创建的界面的文件对象
	 * @param ModuleKey 当前按钮的Key
	 * @return
	 */
	public String getButtonString(FormBase form, ModuleData data) {
		return msg.getText(data.getButtonText(), form.getK(), form.getD(), form, form.getPlayer());
	}

	/**
	 * 准备修改按钮
	 * 
	 * @param form 按钮所在的页面显示的菜单界面对象
	 * @param data 按钮的数据对象
	 * @return
	 */
	protected abstract FormBase getAlterForm(FormBase form, ModuleData data);

	/**
	 * 修改按钮时调用
	 * 
	 * @param form 按钮所在的界面对象
	 * @param data 按钮的数据
	 * @return
	 */
	public boolean alterButton(FormBase form, ModuleData data) {
		MyPlayer myPlayer = form.getMyPlayer();
		Player player = myPlayer.getPlayer();
		if (!myPlayer.isAdmin()) {
			player.sendMessage(msg.getMessage("权限不足", player));
			return false;
		}
		return myPlayer.showForm(getAlterForm(form, data));
	}

	/**
	 * 设置启用方式
	 * 
	 * @param enable
	 */
	public void setEnable(boolean enable) {
		Enable = enable;
		config.set(getModuleKey(), enable);
		config.save();
	}

	/**
	 * 返回是否启用
	 * 
	 * @return
	 */
	public boolean isEnable() {
		return Enable;
	}

	/**
	 * 玩家点击按钮时调用
	 * 
	 * @param form      玩家点击按钮的界面对象
	 * @param ModuleKey 玩家点击的按钮的Key
	 * @return
	 */
	protected abstract boolean ClickButton(FormBase form, ModuleData data);

	/**
	 * 玩家点击了一个按钮
	 * 
	 * @param form 玩家点击按钮的界面对象
	 * @param data 玩家点击的按钮的数据对象
	 * @return
	 */
	public boolean Click(FormBase form, ModuleData data) {
		Player player = form.getPlayer();
		MyPlayer myPlayer = form.getMyPlayer();
		if (!data.isPermission(player)) {
			player.sendMessage(
					msg.getSon("Function", "notPermission", new String[] { "{Player}", "{Money}", "{Permission}" },
							new Object[] { player.getName(), myPlayer.getMoney(), data.getPermission() }));
			return false;
		}
		if (!data.isPlayerfilter(player)) {
			player.sendMessage(msg.getSon("Function", "BlacklistByPlayer", form));
			return false;
		}
		if (!data.isWorldfilter(player.getLevel())) {
			player.sendMessage(msg.getSon("Function", "BlacklistByWorld", form));
			return false;
		}
		if (data.getFunctionBase() == null || !data.getFunctionBase().isEnable()) {
			player.sendMessage(msg.getSon("Function", "Buttonoff", form));
			return false;
		}
		if (data.getCommand().size() > 0) {
			Server server = Server.getInstance();
			for (String string : data.getCommand())
				server.dispatchCommand(player, msg.getText(string, form));
		}
		MyEconomy economy = data.getEconomy();
		if (economy != null && economy.isEnabled() && data.getMoney() > 0)
			if (!economy.allowArrears() && economy.getMoney(player) < data.getMoney()) {
				player.sendMessage(
						msg.getSon("Function", "notMoney", new String[] { "{Player}", "{Money}", "{MoneyN}" },
								new Object[] { player.getName(), economy.getMoney(player), economy.getMoneyName() }));
				return false;
			} else
				economy.reduceMoney(player, data.getMoney());
		return ClickButton(form, data);
	}

	/**
	 * 删除按钮时调用
	 * 
	 * @param player 要删除按钮的玩家对象
	 * @param config 要删除按钮的配置文件
	 * @param Key    要删除的按钮Key
	 * @return
	 */
	public boolean delButton(Player player, Config config, String Key) {
		MyPlayer myPlayer = ac.getPlayers(player);
		if (!myPlayer.isAdmin()) {
			player.sendMessage(msg.getMessage("权限不足", player));
			return false;
		}
		return myPlayer.showForm(new DeleteFunction(player, null, config, Key));
	}

	/**
	 * 删除按钮时调用
	 * 
	 * @param form 要删除按钮的界面对象
	 * @param data 按钮的数据
	 * @return
	 */
	public boolean delButton(FormBase form, ModuleData data) {
		Player player = form.getPlayer();
		MyPlayer myPlayer = ac.getPlayers(player);
		if (!myPlayer.isAdmin()) {
			player.sendMessage(msg.getMessage("权限不足", player));
			return false;
		}
		return myPlayer.showForm(new DeleteFunction(player, form, data.getConfig(), data.getKey()));
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
	 * 获取一个不和当前界面内存在的按钮Key重复的Key
	 * 
	 * @param JJLength
	 * @return
	 */
	public static String getKey(Map<String, Object> Buttons) {
		String string = Tool.getRandString();
		while (Buttons.containsKey(string))
			string += Tool.getRandString();
		return string;
	}

	/**
	 * 添加一个按钮
	 * 
	 * @param config 要添加按钮的界面的配置文件对象
	 * @param map    要添加的按钮数据
	 * @param Key    要添加的按钮的Key
	 * @return
	 */
	public static boolean addButtons(Config config, Map<String, Object> map, String Key) {
		Map<String, Object> Buttons = FunctionBase.getButtons(config);
		Key = Key == null || Key.isEmpty() ? FunctionBase.getKey(Buttons) : Key;
		map.put("Key", Key);
		Buttons.put(Key, map);
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
		Map<String, Object> Buttons = FunctionBase.getButtons(config);
		String Key = FunctionBase.getKey(Buttons);
		map.put("Key", Key);
		Buttons.put(Key, map);
		config.set("Button", Buttons);
		return config.save();
	}

	/**
	 * 删除一个按钮
	 * 
	 * @param config 要删除按钮的配置文件对象
	 * @param Key    要上出的按钮的Key
	 * @return
	 */
	public static boolean removeButton(Config config, String Key) {
		Map<String, Object> Buttons = FunctionBase.getButtons(config);
		if (!Buttons.containsKey(Key))
			return false;
		Buttons.remove(Key);
		config.set("Button", Buttons);
		return config.save();
	}
}
