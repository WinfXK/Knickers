package cn.winfxk.knickers.module.alter;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;

/**
 * 用于在修改按钮时显示当前界面的按钮列表
 * 
 * @Createdate 2021/08/01 14:52:38
 * @author Winfxk
 */
public class AlterButton extends FormBase {
	private File file;

	public AlterButton(Player player, FormBase upForm, File file) {
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
