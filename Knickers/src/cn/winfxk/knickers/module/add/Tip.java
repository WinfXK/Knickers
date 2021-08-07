package cn.winfxk.knickers.module.add;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.module.BaseMake;

/**
 * @Createdate 2021/08/07 21:48:24
 * @author Winfxk
 */
public class Tip extends BaseMake {
	public Tip(Player player, File file) {
		super(player, file);
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
