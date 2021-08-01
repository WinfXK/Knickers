package cn.winfxk.knickers.form.admin;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;

/**
 * 系统设置
 * 
 * @Createdate 2021/08/01 14:56:52
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
