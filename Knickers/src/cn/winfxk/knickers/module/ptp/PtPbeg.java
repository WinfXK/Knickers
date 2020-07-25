package cn.winfxk.knickers.module.ptp;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.MyPlayer;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.tool.SimpleForm;
import cn.winfxk.knickers.tool.Tool;

/**
 * PtP请求界面
 * 
 * @Createdate 2020/05/31 22:42:31
 * @author Winfxk
 */
public class PtPbeg extends FormBase {
	private Player toPlayer;
	private PtPData data;
	private static final String[] KK = { "{ByPlayer}", "{ByMoney}" };

	public PtPbeg(Player player, Player toPlayer, PtPData data) {
		super(player, null);
		this.toPlayer = toPlayer;
		this.data = data;
	}

	@Override
	public boolean MakeMain() {
		SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
		form.addButton(getText(data.getAccept()));
		form.addButton(getText(data.getRefuse()));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean wasClosed() {
		if (toPlayer != null)
			toPlayer.sendMessage(getString("wasClosed", KK, new Object[] { player.getName(), myPlayer.getMoney() }));
		if (player != null)
			player.sendMessage(getString("towasClosed", KK,
					new Object[] { toPlayer.getName(), MyPlayer.getMoney(toPlayer.getName()) }));
		return super.wasClosed();
	}

	@Override
	public boolean disMain(FormResponse data) {
		if (getSimple(data).getClickedButtonId() == 0) {
			toPlayer.sendMessage(
					getString("AcceptMessage", KK, new Object[] { player.getName(), myPlayer.getMoney() }));
			if (toPlayer == null || !toPlayer.isOnline())
				return false;
			return toPlayer.teleport(player);
		}
		toPlayer.sendMessage(getString("RefuseMessage", KK, new Object[] { player.getName(), myPlayer.getMoney() }));
		return true;
	}

	@Override
	protected String getTitle() {
		return getText(data.getSolicitedTitle());
	}

	@Override
	protected String getContent() {
		return getText(data.getSolicitedMessage());
	}

	private String getText(String Text) {
		return msg.getText(Text, Tool.Arrays(K, KK),
				Tool.Arrays(D, new Object[] { toPlayer.getName(), MyPlayer.getMoney(toPlayer.getName()) }), player);
	}
}
