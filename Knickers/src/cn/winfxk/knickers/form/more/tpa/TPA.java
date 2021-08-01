package cn.winfxk.knickers.form.more.tpa;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;

/**
 * @Createdate 2021/08/01 14:01:35
 * @author Winfxk
 */
public class TPA extends FormBase {
	private boolean isAffirm;

	public TPA(Player player, FormBase upForm, boolean isAffirm) {
		super(player, upForm);
		this.isAffirm = isAffirm;
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
