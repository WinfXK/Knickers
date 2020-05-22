package cn.winfxk.knickers.module;

import java.io.File;

import cn.nukkit.Player;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.Message;
import cn.winfxk.knickers.form.ButtonBase;

/**
 * @Createdate 2020/05/22 19:27:38
 * @author Winfxk
 */
public abstract class FunctionBase {
	protected Activate ac;
	protected Message msg;
	private String Key;

	public FunctionBase(Activate ac, String Key) {
		this.ac = ac;
		msg = ac.getMessage();
		this.Key = Key;
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
	 * @param file   创建按钮的界面文件对象
	 * @return
	 */
	public abstract boolean makeButton(Player player, File file);

	/**
	 * 返回按钮文本，创建界面时调用
	 * 
	 * @param form 创建的界面对象
	 * @param file 创建的界面的文件对象
	 * @param Key  当前按钮的Key
	 * @return
	 */
	public abstract String getButtonString(ButtonBase form, String Key);

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
	 * @param form
	 * @param Key
	 * @return
	 */
	public boolean delButton(ButtonBase form, String Key) {
		return true;
	}
}
