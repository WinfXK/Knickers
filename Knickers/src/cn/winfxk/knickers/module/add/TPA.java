package cn.winfxk.knickers.module.add;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.module.BaseMake;

/**
 * @Createdate 2021/08/07 21:48:44
 * @author Winfxk
 */
public class TPA extends BaseMake {

	public TPA(Player player, File file) {
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
