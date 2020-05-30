package cn.winfxk.knickers.form;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;

/**
 * 插件设置界面
 * 
 * @Createdate 2020/05/30 18:43:26
 * @author Winfxk
 */
public class Setting extends FormBase {

	public Setting(Player player, FormBase upForm) {
		super(player, upForm);
	}

	@Override
	public boolean MakeMain() {
		return false;
	}

	@Override
	public boolean disMain(FormResponse data) {
		return false;
	}
}
