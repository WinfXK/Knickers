package cn.winfxk.knickers.form.more.tpa;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;

/**
 * @Createdate 2021/08/01 13:59:40
 * @author Winfxk
 */
public class ToPlayerTPA extends FormBase {
	private Player byPlayer;

	public ToPlayerTPA(Player player, FormBase upForm, Player byPlayer) {
		super(player, upForm);
		this.byPlayer = byPlayer;
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
