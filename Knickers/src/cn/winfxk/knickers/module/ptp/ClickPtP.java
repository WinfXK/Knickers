package cn.winfxk.knickers.module.ptp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.MyPlayer;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.tool.SimpleForm;

/**
 * 玩家点击了PtP按钮的处理类
 * 
 * @Createdate 2020/05/31 22:10:52
 * @author Winfxk
 */
public class ClickPtP extends FormBase {
	private PtPData data;
	private static final String[] KK = { "{ByPlayer}", "{ByMoney}" };
	private List<Player> list;
	private Server server;

	public ClickPtP(Player player, FormBase upForm, PtPData data) {
		super(player, upForm);
		this.data = data;
		server = Server.getInstance();
	}

	@Override
	public boolean MakeMain() {
		list = new ArrayList<>();
		Map<UUID, Player> map = server.getOnlinePlayers();
		if (map.size() <= 1) {
			sendMessage(getString("notPlayer"));
			return isBack();
		}
		SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
		for (Player p : map.values()) {
			if (p == null || !p.isOnline() || p.getName().equals(player.getName()))
				continue;
			list.add(p);
			form.addButton(getPlayerItem(p));
		}
		form.addButton(getBack());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = getSimple(data).getClickedButtonId();
		if (list.size() < ID) {
			Player tPlayer = list.get(ID);
			if (this.data.isForceTransfer()) {
				if (tPlayer == null || !tPlayer.isOnline())
					return sendMessage(getString("PlayerOffline"));
				return player.teleport(tPlayer);
			}
			return setForm(new PtPbeg(tPlayer, player, this.data)).make();
		}
		return isBack();
	}

	/**
	 * 返回玩家列表的项目内容
	 * 
	 * @param player
	 * @return
	 */
	private String getPlayerItem(Player player) {
		return msg.getText(data.getPlayerItem(), KK,
				new Object[] { player.getName(), MyPlayer.getMoney(player.getName()) }, this, player);
	}

	@Override
	protected String getContent() {
		return msg.getText(data.getListContent(), this);
	}

	@Override
	protected String getTitle() {
		return msg.getText(data.getListTitle(), this);
	}
}
