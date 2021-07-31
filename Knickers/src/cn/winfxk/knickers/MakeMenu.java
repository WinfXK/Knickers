package cn.winfxk.knickers;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;

/**
 * @Createdate 2021/07/31 22:31:48
 * @author Winfxk
 */
public class MakeMenu extends FormBase {
	private File file;

	public MakeMenu(Player player, FormBase upForm, File file) {
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
