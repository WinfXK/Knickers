package cn.winfxk.knickers.module.alter;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;

/**
 * 删除按钮前显示按钮列表的界面
 * 
 * @Createdate 2021/08/01 14:51:02
 * @author Winfxk
 */
public class DelButton extends AlterButton {

	public DelButton(Player player, FormBase upForm, File file) {
		super(player, upForm, file);
	}

	@Override
	public boolean MakeMain() {

		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		return true;
	}
}
