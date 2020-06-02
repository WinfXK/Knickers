package cn.winfxk.knickers.module;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;

/**
 * 删除按钮时会显示按钮列表的界面
 * 
 * @Createdate 2020/05/30 19:50:16
 * @author Winfxk
 */
public class DelButton extends AlterButton {
	/**
	 * 删除按钮时显示按钮列表
	 * 
	 * @param player 要准备删除按钮的玩家对象
	 * @param upForm 上一个页面
	 * @param file   要删除按钮的配置文件对象
	 */
	public DelButton(Player player, FormBase upForm, File file) {
		super(player, upForm, file);
	}

	/**
	 * 删除按钮时显示按钮列表
	 * 
	 * @param player 要准备删除按钮的玩家对象
	 * @param upForm 上一个页面
	 * @param config 要删除按钮的配置文件对象
	 */
	public DelButton(Player player, FormBase upForm, Config config) {
		super(player, upForm, config);
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = getSimple(data).getClickedButtonId();
		if (datas.size() > ID) {
			ModuleData data2 = datas.get(ID);
			if (data2.getFunctionBase() == null) {
				player.sendMessage(getString("Buttonoff"));
				return isBack();
			}
			return data2.getFunctionBase().delButton(this, data2);
		}
		return isBack();
	}
}
