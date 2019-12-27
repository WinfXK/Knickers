package xiaokai.knickers.base;

import xiaokai.knickers.Activate;
import xiaokai.knickers.MyPlayer;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;

/**
 * @author Winfxk
 */
public class PlayerEvent implements Listener {
	private Activate ac;

	public PlayerEvent(Activate activate) {
		this.ac = activate;
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		ac.setPlayers(player, new MyPlayer(player));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		ac.removePlayers(e.getPlayer());
	}
}
