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
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Utils;
import cn.winfxk.knickers.cmd.AdminCommand;
import cn.winfxk.knickers.cmd.MainCommand;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.BaseButton;
import cn.winfxk.knickers.money.EconomyAPI;
import cn.winfxk.knickers.money.MyEconomy;
import cn.winfxk.knickers.rec.Message;
import cn.winfxk.knickers.rec.MyPlayer;
import cn.winfxk.knickers.tool.Config;
import cn.winfxk.knickers.tool.ItemList;
import cn.winfxk.knickers.tool.Tool;
import cn.winfxk.knickers.tool.Update;

/**
 * @Createdate 2021/07/28 22:14:50
 * @author Winfxk
 */
public class Knickers extends PluginBase implements Listener {
	public ItemList itemlist;
	public Instant loadTime;
	public Message message;
	private Map<String, BaseButton> Buttons = new HashMap<>();
	/**
	 * 各名称
	 */
	public static final String ConfigName = "Config.yml", MsgConfigName = "Message.yml", MainMenuConfigName = "Main.yml", Menus = "Menus", Language = "Language", ItemList = "ItemList.yml", Command = "Command.yml";
	/**
	 * 需要检查本地文件是否存在的文件，不存在及生成默认文件
	 */
	public static final String[] Exist = { ConfigName, MsgConfigName, MainMenuConfigName, ItemList, Command };
	/**
	 * 需要判断文件格式是否错误，错误的话生成默认数据<Exist的下步>
	 */
	public static final String[] Meta = { ConfigName, MsgConfigName };
	public static final String[] Dirs = { Menus, Language };
	public Config config;
	public static Knickers kis;
	public File LanguageFile;
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
	public String FastTool;;
	/**
	 * 防误触检测间隔
	 */
	public int ClickInterval;
	/**
	 * 主页配置文件的File对象
	 */
	public File Main;
	/**
	 * 是否撤回快捷工具产生的事件
	 */
	private boolean EventCancelled;

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
		LanguageFile = new File(getDataFolder(), Language);
		Check check = new Check(this);
		check.Message();
		Exist();
		check.start();
		config = new Config(new File(getDataFolder(), ConfigName));
		Object obj = config.get("ID1");
		if (obj == null || !Tool.isInteger(obj))
			config.set("ID1", Tool.getRand(0, 147483647));
		obj = config.get("ID2");
		if (obj == null || !Tool.isInteger(obj))
			config.set("ID2", Tool.getRand(0, 147483647));
		config.save();
		FastTool = config.getString("Tool");
		ClickInterval = config.getInt("EternalToolTime");
		EventCancelled = config.getBoolean("EventCancelled");
		addEcnonmy(new EconomyAPI(this));
		economy = getEconomy(config.getString("MoneyAPI", "EconomyAPI"));
		message = new Message(this);
		itemlist = new ItemList();
		Main = new File(getDataFolder(), MainMenuConfigName);
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getCommandMap().register(getName() + " MainCommand", new MainCommand());
		getServer().getCommandMap().register(getName() + " MainCommand", new AdminCommand());
		new Update(config.getBoolean("Update"), config.getBoolean("CycleUpdate"), config.getInt("UpdateTime"), false, this, message).start();
		getLogger().info(message.getMessage("插件启动", "{loadTime}", (float) Duration.between(loadTime, Instant.now()).toMillis() + "ms"));
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!config.getBoolean("PositionLock"))
			return;
		Player player = e.getPlayer();
		Item item = e.getSourceItem();
		if (!isFastTool(item))
			return;
		e.setCancelled();
		int Position = config.getInt("Position");
		PlayerInventory inventory = player.getInventory();
		boolean isOK = false;
		for (Map.Entry<Integer, Item> entry : inventory.getContents().entrySet())
			if (isFastTool(entry.getValue())) {
				inventory.setItem(entry.getKey(), new Item(0));
				isOK = true;
			}
		if (!isOK)
			return;
		Item item2 = inventory.getItem(Position);
		if (item2 != null && item2.getId() != 0) {
			if (inventory.isFull()) {
				player.getLevel().dropItem(player, item2);
				player.sendMessage(message.getMessage("FastToolDropItem", player));
			} else
				inventory.addItem(item2);
		}
		inventory.setItem(Position, getFastTool());
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

	/**
	 * 使用物品事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent e) {
		if (isFastTool(e.getItem())) {
			Player player = e.getPlayer();
			MyPlayer myPlayer = MyPlayers.get(player.getName());
			if (myPlayer.instant != null && Duration.between(myPlayer.instant, Instant.now()).toMillis() < ClickInterval)
				return;
			myPlayer.instant = Instant.now();
			(myPlayer.form = new MakeMenu(player, null, Main)).MakeMain();
			e.setCancelled(EventCancelled);
		}
	}

	/**
	 * 玩家复活事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		if (!MyPlayers.containsKey(player.getName()) || MyPlayers.get(player.getName()) == null)
			MyPlayers.put(player.getName(), new MyPlayer(player));
		else {
			MyPlayer myPlayer = MyPlayers.get(player.getName());
			myPlayer.setPlayer(player);
		}
		if (config.getBoolean("EternalTool"))
			if (!isFastTool(player)) {
				player.getInventory().addItem(getFastTool());
				player.sendMessage(message.getMessage("给工具", player));
			}
	}

	/**
	 * 玩家丢弃快捷工具事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (config.getBoolean("DiscardTool"))
			return;
		if (isFastTool(e.getItem())) {
			Player player = e.getPlayer();
			e.setCancelled();
			player.sendMessage(message.getMessage("拒绝丢弃Tool", player));
		}
	}

	/**
	 * 破坏方块事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (isFastTool(e.getItem())) {
			Player player = e.getPlayer();
			MyPlayer myPlayer = MyPlayers.get(player.getName());
			if (myPlayer.instant != null && Duration.between(myPlayer.instant, Instant.now()).toMillis() < ClickInterval)
				return;
			myPlayer.instant = Instant.now();
			(myPlayer.form = new MakeMenu(player, null, Main)).MakeMain();
			e.setCancelled(EventCancelled);
		}
	}

	/**
	 * 玩家交互事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (isFastTool(e.getItem())) {
			Player player = e.getPlayer();
			MyPlayer myPlayer = MyPlayers.get(player.getName());
			if (myPlayer.instant != null && Duration.between(myPlayer.instant, Instant.now()).toMillis() < ClickInterval)
				return;
			myPlayer.instant = Instant.now();
			(myPlayer.form = new MakeMenu(player, null, Main)).MakeMain();
			e.setCancelled(EventCancelled);
		}
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
		item.addEnchantment(Enchantment.getEnchantment(0));
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
		if (FastTool == null || FastTool.isEmpty())
			return false;
		if ((item.getId() + ":" + item.getDamage()).equals(FastTool)) {
			CompoundTag tag = item.getNamedTag();
			if (tag == null)
				return false;
			String string = tag.getString(getName());
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
				if (data == null || !(data instanceof FormResponseCustom) && !(data instanceof FormResponseSimple) && !(data instanceof FormResponseModal)) {
					myPlayer.form = null;
					return;
				}
				myPlayer.form.disMain(data);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			if (myPlayer.form != null)
				myPlayer.form.onError(e2);
			player.sendMessage(message.getMessage("FormError", new String[] { "{Player}", "{Money}", "{Error}" }, new Object[] { player.getName(), MyPlayer.getMoney(player.getName()), e2.toString() }));
		}
	}

	/**
	 * 添加一个按钮
	 * 
	 * @param button
	 * @return
	 */
	public boolean addButton(BaseButton button) {
		if (Buttons.containsKey(button.getTGA()))
			return false;
		Buttons.put(button.getTGA(), button);
		return true;
	}

	/**
	 * 添加一个按钮
	 * 
	 * @param button
	 * @return 添加是否完全成功
	 */
	public boolean addButton(BaseButton... buttons) {
		boolean isReturn = true;
		for (BaseButton button : buttons) {
			if (Buttons.containsKey(button.getTGA()))
				isReturn = false;
			Buttons.put(button.getTGA(), button);
		}
		return isReturn;
	}

	/**
	 * 返回已经添加了的按钮列表
	 * 
	 * @return
	 */
	public Map<String, String> getButtonName() {
		Map<String, String> map = new HashMap<>();
		for (Map.Entry<String, BaseButton> entry : Buttons.entrySet())
			map.put(entry.getKey(), entry.getValue().getName());
		return map;
	}

	/**
	 * 返回已经支持了的按钮列表
	 * 
	 * @return
	 */
	public Map<String, BaseButton> getButtons() {
		return Buttons;
	}
}
