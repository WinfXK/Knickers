package cn.winfxk.knickers.form;

import java.util.Map;

import cn.nukkit.Player;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.module.FunctionBase;

/**
 * 点击事件基础类
 * 
 * @Createdate 2020/05/23 20:57:08
 * @author Winfxk
 */
public abstract class ClickBase extends FormBase {
	protected Config config;
	protected String Key;
	protected Map<String, Object> map, Buttons;

	/**
	 * 点击事件基础类
	 * 
	 * @param player 点击界面的玩家对象
	 * @param upForm 上一个界面
	 * @param config 当前的界面的配置文件对象
	 * @param Key    点击的按钮的Key
	 */
	public ClickBase(Player player, FormBase upForm, Config config, String Key) {
		super(player, upForm);
		this.config = config;
		this.Key = Key;
		Buttons = FunctionBase.getButtons(config);
		map = FunctionBase.getButtonMap(Buttons, Key);
	}
}
