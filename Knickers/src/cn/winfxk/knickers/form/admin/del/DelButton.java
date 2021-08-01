package cn.winfxk.knickers.form.admin.del;

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
public class DelButton extends FormBase {
	private File file;
	private Object object;

	public DelButton(Player player, FormBase upForm, File file) {
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
