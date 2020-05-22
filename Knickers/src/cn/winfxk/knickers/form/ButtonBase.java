package cn.winfxk.knickers.form;

import java.io.File;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.utils.Config;
import cn.winfxk.knickers.MyMap;

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
		Object object = config.get("Button");
		object = object == null || !(object instanceof Map) ? new MyMap<>() : ((Map<String, Object>) object).get(Key);
		map = object == null || !(object instanceof Map) ? new MyMap<>() : (MyMap<String, Object>) object;
	}

	@Override
	public boolean MakeMain() {
		return false;
	}

	@Override
	public boolean disMain(FormResponse data) {
		return false;
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
	 * 返回当前界面的文件对象
	 * 
	 * @return
	 */
	public File getFile() {
		return file;
	}
}
