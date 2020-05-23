package cn.winfxk.knickers.module;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.Message;
import cn.winfxk.knickers.MyPlayer;
import cn.winfxk.knickers.form.ButtonBase;
import cn.winfxk.knickers.form.FormBase;

/**
 * 菜单的功能按钮基本类
 * 
 * @Createdate 2020/05/22 19:27:38
 * @author Winfxk
 */
public abstract class FunctionBase {
	protected Activate ac;
	protected Message msg;
	private String Key, Name;
	protected String t = "Function";
	private boolean Enable;
	private Config config;
	protected FunctionMag mag;

	public FunctionBase(Activate ac, String Key) {
		this.ac = ac;
		msg = ac.getMessage();
		this.Key = Key;
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
	 * 返回格式化文本
	 * 
	 * @param Key
	 * @return
	 */
	public String getString(String Key, String[] K, Object[] D) {
		return msg.getSun(t, this.Key, Key, K, D);
	}

	/**
	 * 返回格式化文本
	 * 
	 * @param Key
	 * @return
	 */
	public String getString(String Key, FormBase form) {
		return msg.getSun(t, this.Key, Key, form, form.getPlayer());
	}

	/**
	 * 返回格式化文本
	 * 
	 * @param Key
	 * @return
	 */
	public String getString(String Key) {
		return msg.getSun(t, this.Key, Key);
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
	protected void setKey(String key) {
		Key = key;
	}

	/**
	 * 返回按钮的类型
	 * 
	 * @return
	 */
	public String getKey() {
		return Key;
	}

	/**
	 * 创建按钮时调用
	 * 
	 * @param player 创建界面的玩家对象
	 * @param config 创建按钮的界面文件对象
	 * @return
	 */
	public boolean makeButton(Player player, Config config, FormBase form) {
		MyPlayer myPlayer = ac.getPlayers(player);
		if (myPlayer == null) {
			player.sendMessage(msg.getSon(t, "NotPlayer", player));
			return false;
		}
		myPlayer.form = getForm(player, config, form);
		return myPlayer.form.MakeMain();
	}

	/**
	 * 返回创建按钮的界面对象
	 * 
	 * @param player 创建按钮的玩家对象
	 * @param config 创建按钮的界面
	 * @param base   上级界面
	 * @return
	 */
	protected abstract FormBase getForm(Player player, Config config, FormBase base);

	/**
	 * 返回按钮文本，创建界面时调用
	 * 
	 * @param form 创建的界面对象
	 * @param file 创建的界面的文件对象
	 * @param Key  当前按钮的Key
	 * @return
	 */
	public String getButtonString(ButtonBase form, Map<String, Object> map) {
		return msg.getText(map.get("ButtonText"), form.getK(), form.getD(), form, form.getPlayer());
	}

	/**
	 * 设置启用方式
	 * 
	 * @param enable
	 */
	public void setEnable(boolean enable) {
		Enable = enable;
		config.set(getKey(), enable);
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
	 * @param form 玩家点击按钮的界面对象
	 * @param Key  玩家点击的按钮的Key
	 * @return
	 */
	public abstract boolean ClickButton(ButtonBase form, String Key);

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
		myPlayer.form = new DeleteFunction(player, null, config, Key);
		return myPlayer.form.MakeMain();
	}

	/**
	 * 删除按钮时调用
	 * 
	 * @param base 要删除按钮的界面对象
	 * @param Key  要删除的按钮Key
	 * @return
	 */
	public boolean delButton(ButtonBase base, String Key) {
		MyPlayer myPlayer = ac.getPlayers(base.getPlayer());
		myPlayer.form = new DeleteFunction(base.getPlayer(), base, base.getConfig(), Key);
		return myPlayer.form.MakeMain();
	}
}
