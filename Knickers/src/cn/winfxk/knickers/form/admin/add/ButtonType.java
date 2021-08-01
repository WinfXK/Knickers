package cn.winfxk.knickers.form.admin.add;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;

/**
 * 显示按钮类型的界面
 * 
 * @Createdate 2021/08/01 14:49:30
 * @author Winfxk
 */
public class ButtonType extends FormBase {
	private File file;

	public ButtonType(Player player, FormBase upForm, File file) {
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
