package cn.winfxk.knickers.form.more.tpa;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.base.ModalForm;
import cn.winfxk.knickers.rec.MyPlayer;

/**
 * @Createdate 2021/08/01 13:59:40
 * @author Winfxk
 */
public class ToPlayerTPA extends FormBase {
	protected Player byPlayer, player;
	protected int Countdown;

	public ToPlayerTPA(Player player, FormBase upForm, Player byPlayer) {
		super(player, upForm);
		this.byPlayer = byPlayer;
		Son = "TPA";
		Countdown = kis.config.getInt("AcceptTPAWait");
		setK("{ByPlayer}", "{ByMoney}", "{Countdown}");
	}

	@Override
	public boolean MakeMain() {
		setD(byPlayer.getName(), MyPlayer.getMoney(byPlayer.getName()), Countdown);
		ModalForm form = new ModalForm(getID(), getTitle(), getContent());
		form.setButton(getString("Accept"), getString("Repulse"));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		if (getModal(data).getClickedButtonId() != 0)
			return Tip(getString("Offline"));
		if (Countdown <= 0) {
			byPlayer.teleport(player);
			return sendMessage(getString("AcceptMsg"));
		}
		new TpaThread(this).start();
		myPlayer.form = null;
		return sendMessage(getString("AcceptMsg"));
	}

	@Override
	protected String getTitle() {
		return getString("Title");
	}

	@Override
	protected String getContent() {
		return getString("TPA请求内容");
	}
}
