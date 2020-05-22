package cn.winfxk.knickers;

import java.time.Instant;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.plugin.PluginBase;

/**
 * @Createdate 2020/05/22 18:56:27
 * @author Winfxk
 */
public class Knickers extends PluginBase implements Listener {
	public Instant loadTime;
	private static Activate ac;

	@Override
	public void onEnable() {
		loadTime = Instant.now();
		ac = new Activate(this);
		getServer().getPluginManager().registerEvents(this, this);
		super.onEnable();
	}

	@Override
	public void onLoad() {
		getLogger().info(getName() + " start load..");
		if (!getDataFolder().exists())
			getDataFolder().mkdirs();
	}

	@Override
	public void onDisable() {
		try {
			getLogger().info(ac.getMessage().getMessage("插件关闭"));
		} catch (Exception e) {
		}
		super.onDisable();
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (ac.isPlayers(player))
			ac.removePlayers(player);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (!ac.isPlayers(player))
			ac.setPlayers(player, new MyPlayer(player));
	}

	/**
	 * 表单接受事件
	 *
	 * @param e
	 */
	@EventHandler
	public void onFormResponded(PlayerFormRespondedEvent e) {
		Player player = e.getPlayer();
		if (player == null)
			return;
		int ID = e.getFormID();
		FormID f = ac.getFormID();
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		if (myPlayer == null)
			return;
		try {
			FormResponse data = e.getResponse();
			if ((ID == f.getID(0) || ID == f.getID(1) || ID == f.getID(2)) && myPlayer.form != null) {
				if (e.wasClosed()) {
					myPlayer.form.wasClosed();
					return;
				}
				if (data == null || !(data instanceof FormResponseCustom) && !(data instanceof FormResponseSimple)
						&& !(data instanceof FormResponseModal)) {
					myPlayer.form = null;
					return;
				}
				myPlayer.form.disMain(data);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			if (myPlayer.form != null)
				myPlayer.form.onError(e2);
			player.sendMessage(ac.getMessage().getMessage("数据处理错误", new String[] { "{Player}", "{Money}", "{Error}" },
					new Object[] { player.getName(), MyPlayer.getMoney(player.getName()), e2.getMessage() }));
		}
	}

	/**
	 * 返回插件数据中心</br>
	 * <b>PS: </b> 我不喜欢把太多数据放到插件主类，有意见你就去屎吧
	 *
	 * @return
	 */
	public static Activate getInstance() {
		return ac;
	}
}
