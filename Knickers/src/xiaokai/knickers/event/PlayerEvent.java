package xiaokai.knickers.event;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.item.Item;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import xiaokai.knickers.appliance.Appliance;
import xiaokai.knickers.form.MakeForm;
import xiaokai.knickers.mtp.Belle;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.tool.ItemIDSunName;

/**
 * @author Winfxk
 */
public class PlayerEvent implements Listener {
	private Kick kick;

	public PlayerEvent(Kick kick) {
		this.kick = kick;
	}

//测试中..
	public void onIMI(InventoryMoveItemEvent e) {
		Server.getInstance().broadcastMessage("asdasd");
		Item item = e.getItem();
		if (e.getAction().equals(cn.nukkit.event.inventory.InventoryMoveItemEvent.Action.DROP)
				&& Belle.isMaterials(item) && !kick.config.getBoolean("是否允许玩家将快捷工具装箱"))
			e.setCancelled();
	}

	/**
	 * 玩家丢弃事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e) {
		Item item = e.getItem();
		boolean isC = false;
		if (!kick.config.getBoolean("是否允许玩家丢弃快捷工具") && Belle.isMaterials(item)) {
			if (!isC)
				e.setCancelled();
			Player player = e.getPlayer();
			player.sendMessage(
					kick.Message.getMessage("撤销丢掉快捷工具的提示", new String[] { "{Player}", "{ItemName}", "{ItemID}" },
							new Object[] { player.getName(), ItemIDSunName.getIDByName(item.getId(), item.getDamage()),
									item.getId() + ":" + item.getDamage() }));
		}
		if (!Appliance.isDrop(item)) {
			if (!isC)
				e.setCancelled();
			Player player = e.getPlayer();
			player.sendMessage(
					kick.Message.getMessage("撤销丢掉自定义快捷工具的提示", new String[] { "{Player}", "{ItemName}", "{ItemID}" },
							new Object[] { player.getName(), ItemIDSunName.getIDByName(item.getId(), item.getDamage()),
									item.getId() + ":" + item.getDamage() }));
		}
	}

	/**
	 * 玩家脑残了砸碎点击东西
	 * 
	 * @param e
	 */
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		boolean isC = false;
		Item item = e.getItem();
		if (Belle.isMaterials(item)) {
			MakeForm.Main(player);
			if (!isC && kick.config.getBoolean("打开撤销")) {
				isC = true;
				e.setCancelled();
			}
		}
		if (Appliance.isGirl(item)) {
			kick.App.start(player, item);
			if (!isC && kick.config.getBoolean("打开撤销")) {
				e.setCancelled();
				isC = true;
			}
		}
	}

	/**
	 * 玩家脑残了点击东西
	 * 
	 * @param e
	 */
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Action ac = e.getAction();
		Item item = e.getItem();
		boolean isC = false;
		if (!ac.equals(Action.PHYSICAL)) {
			if (Belle.isMaterials(item)) {
				MakeForm.Main(player);
				if (!isC && kick.config.getBoolean("打开撤销")) {
					isC = true;
					e.setCancelled();
				}
			}
			if (Appliance.isGirl(item)) {
				kick.App.start(player, item);
				if (!isC && kick.config.getBoolean("打开撤销")) {
					e.setCancelled();
					isC = true;
				}
			}
		}
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
