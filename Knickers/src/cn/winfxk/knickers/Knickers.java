package cn.winfxk.knickers;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Utils;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.money.MyEconomy;
import cn.winfxk.knickers.rec.Message;
import cn.winfxk.knickers.rec.MyPlayer;
import cn.winfxk.knickers.tool.Config;
import cn.winfxk.knickers.tool.ItemList;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/07/28 22:14:50
 * @author Winfxk
 */
public class Knickers extends PluginBase implements Listener {
	public ItemList itemlist;
	public Instant loadTime;
	public Message message;
	/**
	 * 各名称
	 */
	public static final String ConfigName = "Config.yml", MsgConfigName = "Message.yml",
			MainMenuConfigName = "Main.yml", Menus = "Menus", Language = "Language", ItemList = "ItemList.yml";
	/**
	 * 需要检查本地文件是否存在的文件，不存在及生成默认文件
	 */
	public static final String[] Exist = { ConfigName, MsgConfigName, MainMenuConfigName, ItemList };
	/**
	 * 需要判断文件格式是否错误，错误的话生成默认数据<Exist的下步>
	 */
	public static final String[] Meta = { ConfigName, MsgConfigName };
	public static final String[] Dirs = { Menus, Language };
	public Config config;
	public static Knickers kis;
	/**
	 * 插件支持的经济列表
	 */
	public static final Map<String, MyEconomy> Economys = new HashMap<>();
	/**
	 * 主要经济核心支持
	 */
	public MyEconomy economy;
	/**
	 * 返回个人玩家对象列表
	 */
	public final Map<String, MyPlayer> MyPlayers = new HashMap<>();
	/**
	 * 快捷工具的ID
	 */
	public String FastTool = config.getString("Tool");
	/**
	 * 防误触检测间隔
	 */
	public int ClickInterval = config.getInt("EternalToolTime");
	/**
	 * 主页配置文件的File对象
	 */
	private File Main;

	private void Exist() {
		File file;
		for (String string : Dirs) {
			file = new File(getDataFolder(), string);
			if (!file.exists())
				file.mkdirs();
		}
		for (String string : Exist) {
			file = new File(getDataFolder(), string);
			if (file.exists())
				continue;
			try {
				Utils.writeFile(file, Tool.getResource(string));
			} catch (IOException e) {
				getLogger().error("Unable to obtain resource: " + string);
			}
		}
	}

	@Override
	public void onLoad() {
		getLogger().info(getName() + " start load..");
		kis = this;
	}

	@Override
	public void onEnable() {
		loadTime = Instant.now();
		Check check = new Check(this);
		check.Message();
		Exist();
		check.start();
		config = new Config(new File(getDataFolder(), ConfigName));
		message = new Message(this);
		itemlist = new ItemList();
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info(message.getMessage("插件启动", "{loadTime}",
				(float) Duration.between(loadTime, Instant.now()).toMillis() + "ms"));
	}

	@Override
	public void onDisable() {
		try {
			getLogger().info(message.getMessage("插件关闭"));
		} catch (Exception e) {
		}
	}

	/**
	 * 返回默认货币的名称
	 * 
	 * @return
	 */
	public String getMoneyName() {
		return config.getString("MoneyName");
	}

	/**
	 * 返回当前正在使用的经济支持
	 * 
	 * @return
	 */
	public MyEconomy getEconomy() {
		return economy;
	}

	/**
	 * 根据经济支持名称返回经济支持接口
	 * 
	 * @param EconomyName
	 * @return
	 */
	public MyEconomy getEconomy(String EconomyName) {
		return Economys.get(EconomyName);
	}

	/**
	 * 添加一个经济支持
	 * 
	 * @param economy
	 * @return
	 */
	public static boolean addEcnonmy(MyEconomy economy) {
		if (Economys.containsKey(economy.getEconomyName()))
			return false;
		Economys.put(economy.getEconomyName(), economy);
		return true;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		MyPlayer myPlayer = MyPlayers.get(player.getName());
		if (myPlayer.instant != null && Duration.between(myPlayer.instant, Instant.now()).toMillis() < ClickInterval)
			return;
		myPlayer.instant = Instant.now();
		(myPlayer.form = new MakeMenu(player, null, Main)).MakeMain();
	}

	/**
	 * 玩家退出事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (MyPlayers.containsKey(player.getName()))
			MyPlayers.remove(player.getName());
	}

	/**
	 * 玩家进服事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (!MyPlayers.containsKey(player.getName()) || MyPlayers.get(player.getName()) == null)
			MyPlayers.put(player.getName(), new MyPlayer(player));
		else {
			MyPlayer myPlayer = MyPlayers.get(player.getName());
			myPlayer.setPlayer(player);
		}
	}

	/**
	 * 返回一个快捷工具的Item对象
	 * 
	 * @return
	 */
	public Item getFastTool() {
		String string = FastTool == null || FastTool.isEmpty() ? "347:0" : FastTool;
		Item item = itemlist.getItem(string);
		CompoundTag tag = item.getNamedTag() == null ? new CompoundTag() : item.getNamedTag();
		tag.putString(getName(), getClass().getName());
		item.setNamedTag(tag);
		item.setCustomName(message.getMessage("快捷工具名称"));
		item.setLore(message.getMessage("快捷工具Lore"));
		return item;
	}

	/**
	 * 判断一个物品是否是快捷工具
	 * 
	 * @param item
	 * @return
	 */
	public boolean isFastTool(Item item) {
		String string = FastTool == null || FastTool.isEmpty() ? "347:0" : FastTool;
		if ((item.getId() + ":" + item.getDamage()).equals(string)) {
			CompoundTag tag = item.getNamedTag();
			if (tag == null)
				return false;
			string = tag.getString(getName());
			if (string == null)
				return false;
			return string.equals(getClass().getName());
		}
		return false;
	}

	/**
	 * 判断玩家背包是否拥有快捷工具
	 * 
	 * @param player
	 * @return
	 */
	public boolean isFastTool(Player player) {
		Inventory inventory = player.getInventory();
		Map<Integer, Item> Items = inventory.getContents();
		for (Item item : Items.values())
			if (item.getId() != 0)
				if (isFastTool(item))
					return true;
		return false;
	}

	/**
	 * 表单事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onFormResponded(PlayerFormRespondedEvent e) {
		Player player = e.getPlayer();
		if (player == null)
			return;
		int ID = e.getFormID();
		MyPlayer myPlayer = MyPlayers.get(player.getName());
		if (myPlayer == null)
			return;
		try {
			FormResponse data = e.getResponse();
			if ((ID == FormBase.IDs[0] || ID == FormBase.IDs[1]) && myPlayer.form != null) {
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
			player.sendMessage(message.getMessage("数据处理错误", new String[] { "{Player}", "{Money}", "{Error}" },
					new Object[] { player.getName(), MyPlayer.getMoney(player.getName()), e2.getMessage() }));
		}
	}
}
