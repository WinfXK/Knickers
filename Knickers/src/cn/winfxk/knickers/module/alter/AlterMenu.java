package cn.winfxk.knickers.module.alter;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;

/**
 * 修改当前界面时现实的界面
 * 
 * @Createdate 2021/08/01 14:55:08
 * @author Winfxk
 */
public class AlterMenu extends FormBase {
	private File file;

	public AlterMenu(Player player, FormBase upForm, File file) {
		super(player, upForm);
		this.file = file;
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
