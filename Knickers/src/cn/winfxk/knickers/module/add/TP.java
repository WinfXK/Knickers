package cn.winfxk.knickers.module.add;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.module.BaseMake;

/**
 * 创建一个点击后可以传送到定点的界面时显示的界面
 * 
 * @Createdate 2021/08/07 21:48:54
 * @author Winfxk
 */
public class TP extends BaseMake {

	public TP(Player player, File file) {
		super(player, file);
	}

	@Override
	public boolean MakeMain() {
		if (!isPrmission())
			return false;

		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		return true;
	}
}
