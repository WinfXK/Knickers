package cn.winfxk.knickers.module.tip;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeBase;
import cn.winfxk.knickers.module.FunctionBase;

/**
 * 创建Tip按钮时显示的内容
 * 
 * @Createdate 2020/05/31 23:11:25
 * @author Winfxk
 */
public class MakeTip extends MakeBase {

	public MakeTip(Player player, FormBase upForm, Config config, FunctionBase base) {
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
