package xiaokai.knickers.event;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import xiaokai.knickers.form.MakeForm;
import xiaokai.knickers.mtp.Belle;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.MyPlayer;

/**
 * @author Winfxk
 */
public class PlayerEvent implements Listener {
	private Kick kick;

	public PlayerEvent(Kick kick) {
		this.kick = kick;
	}
/**
 * 玩家脑残了点击东西
 * @param e
 */
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if ((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK
				|| e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& Belle.isMaterials(e.getItem()))
			MakeForm.Main(player);
	}

	/**
	 * 玩家滚蛋事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (kick.PlayerDataMap.containsKey(player.getName()))
			kick.PlayerDataMap.remove(player.getName());
	}

	/**
	 * 监听玩家嗝了屁重新破壳的事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerSpawn(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		if (!kick.PlayerDataMap.containsKey(player.getName()))
			kick.PlayerDataMap.put(player.getName(), new MyPlayer(player));
		new Thread() {
			public void run() {
				Belle.exMaterials(player);
			}
		}.start();
	}
}
