package cn.winfxk.knickers.module.tp;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeBase;
import cn.winfxk.knickers.module.FunctionBase;

/**
 * 创建Transfer按钮时会显示的界面
 * 
 * @Createdate 2020/05/30 22:24:07
 * @author Winfxk
 */
public class MakeTransfer extends MakeBase {

	public MakeTransfer(Player player, FormBase upForm, Config config, FunctionBase base) {
		super(player, upForm, config, base);
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
