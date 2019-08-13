package xiaokai.knickers.event;

import java.time.Duration;
import java.time.Instant;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.event.player.PlayerItemHeldEvent;
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

	/**
	 * 玩家点击快捷物品栏物品事件事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onClicks(PlayerItemHeldEvent e) {
		if (!kick.config.getBoolean("快捷工具监听快捷栏双击"))
			return;
		Player player = e.getPlayer();
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
		Item item = player.getInventory().getItemInHand();
		if (Belle.isMaterials(item)) {
			if (myPlayer.ClickHeldTime == null || Duration.between(myPlayer.ClickHeldTime, Instant.now())
					.toMillis() > kick.config.getInt("快捷工具快捷栏双击间隔")) {
				myPlayer.ClickHeldTime = Instant.now();
				kick.PlayerDataMap.put(player.getName(), myPlayer);
				return;
			}
			MakeForm.Main(player);
			if (kick.config.getBoolean("打开撤销"))
				e.setCancelled();
			return;
		} else if (Appliance.isGirl(item)) {
			if (myPlayer.ClickHeldTime == null || Duration.between(myPlayer.ClickHeldTime, Instant.now())
					.toMillis() > kick.config.getInt("快捷工具快捷栏双击间隔")) {
				myPlayer.ClickHeldTime = Instant.now();
				kick.PlayerDataMap.put(player.getName(), myPlayer);
				return;
			}
			kick.App.start(player, item);
			if (kick.config.getBoolean("打开撤销"))
				e.setCancelled();
			return;
		}
	}

	/**
	 * 玩家丢弃事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e) {
		Item item = e.getItem();
		if (!kick.config.getBoolean("是否允许玩家丢弃快捷工具") && Belle.isMaterials(item)) {
			e.setCancelled();
			Player player = e.getPlayer();
			player.sendMessage(
					kick.Message.getMessage("撤销丢掉快捷工具的提示", new String[] { "{Player}", "{ItemName}", "{ItemID}" },
							new Object[] { player.getName(), ItemIDSunName.getIDByName(item.getId(), item.getDamage()),
									item.getId() + ":" + item.getDamage() }));
		} else if (!Appliance.isDrop(item)) {
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
		if (!kick.config.getBoolean("快捷工具监听破坏"))
			return;
		Player player = e.getPlayer();
		Item item = e.getItem();
		if (Belle.isMaterials(item)) {
			MakeForm.Main(player);
			if (kick.config.getBoolean("打开撤销"))
				e.setCancelled();
		} else if (Appliance.isGirl(item)) {
			kick.App.start(player, item);
			if (kick.config.getBoolean("打开撤销"))
				e.setCancelled();
		}
	}

	/**
	 * 玩家脑残了点击东西
	 * 
	 * @param e
	 */
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (!kick.config.getBoolean("快捷工具监听点击"))
			return;
		Player player = e.getPlayer();
		Action ac = e.getAction();
		Item item = e.getItem();
		if (!ac.equals(Action.PHYSICAL)) {
			if (Belle.isMaterials(item)) {
				MakeForm.Main(player);
				if (kick.config.getBoolean("打开撤销"))
					e.setCancelled();
			} else if (Appliance.isGirl(item)) {
				kick.App.start(player, item);
				if (kick.config.getBoolean("打开撤销"))
					e.setCancelled();

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
