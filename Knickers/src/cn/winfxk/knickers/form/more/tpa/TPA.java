package cn.winfxk.knickers.form.more.tpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeForm;
import cn.winfxk.knickers.form.base.SimpleForm;
import cn.winfxk.knickers.rec.MyPlayer;

/**
 * @Createdate 2021/08/01 14:01:35
 * @author Winfxk
 */
public class TPA extends FormBase {
	private boolean isAffirm;
	private List<Player> list;
	private static final String[] PK = { "{ByPlayer}", "{ByMoney}" };

	public TPA(Player player, FormBase upForm, boolean isAffirm) {
		super(player, upForm);
		this.isAffirm = isAffirm;
		Son = "TPA";
	}

	@Override
	public boolean MakeMain() {
		list = new ArrayList<>();
		Collection<Player> players = server.getOnlinePlayers().values();
		if (players.size() <= 1)
			return setForm(new MakeForm(player, upForm, getString("Tip"), getString("OfflinePlayer"))).make();
		SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
		for (Player p : players) {
			if (p == null || !p.isOnline() || p.getName().equals(player.getName()))
				continue;
			list.add(p);
			form.addButton(getString("ItemStyle", PK, new Object[] { p.getName(), MyPlayer.getMoney(p.getName()) }));
		}
		if (list.size() <= 0)
			return setForm(new MakeForm(player, upForm, getString("Tip"), getString("OfflinePlayer"))).make();
		form.addButton(getBack());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = getSimple(data).getClickedButtonId();
		if (ID >= list.size())
			return isBack();
		Player p = list.get(ID);
		Object[] dd = { p == null ? getString("NullPlayer") : p.getName(), p == null ? getString("NullPlayer") : MyPlayer.getMoney(p.getName()) };
		if (p == null || !p.isOnline())
			return setForm(new MakeForm(player, upForm, getString("Tip"), getString("Offline", PK, dd))).make();
		if (p.getName().equals(player.getName()))
			return setForm(new MakeForm(player, upForm, getString("Tip"), getString("ToMe"))).make();
		if (isAffirm)
			return player.teleport(p);
		sendMessage(getString("TPAToPlayer", PK, dd));
		setForm(new ToPlayerTPA(p, this, player)).make();
		return true;
	}

	@Override
	protected String getTitle() {
		return getString("Title");
	}

	@Override
	protected String getContent() {
		return getString("Content");
	}
}
