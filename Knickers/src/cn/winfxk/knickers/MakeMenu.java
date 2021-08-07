package cn.winfxk.knickers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeForm;
import cn.winfxk.knickers.form.admin.ButtonType;
import cn.winfxk.knickers.form.admin.FoldingSet;
import cn.winfxk.knickers.form.admin.Setting;
import cn.winfxk.knickers.form.base.SimpleForm;
import cn.winfxk.knickers.form.more.Command;
import cn.winfxk.knickers.form.more.tpa.TPA;
import cn.winfxk.knickers.form.more.tpa.ToPlayerTPA;
import cn.winfxk.knickers.module.BaseButton;
import cn.winfxk.knickers.module.alter.AlterButton;
import cn.winfxk.knickers.module.alter.AlterMenu;
import cn.winfxk.knickers.module.alter.DelButton;
import cn.winfxk.knickers.rec.SecurityPermissions;
import cn.winfxk.knickers.tool.Config;
import cn.winfxk.knickers.tool.Tool;

/**
 * 显示菜单页面
 * 
 * @Createdate 2021/07/31 22:31:48
 * @author Winfxk
 */
public class MakeMenu extends FormBase {
	public Player player;
	public File file;
	public Config config;
	/**
	 * 过滤模式
	 */
	public int FilteredModel, LevelFilteredModel,
			/**
			 * 使用该菜单的权限
			 */
			Permission;
	public List<String> FilteredPlayer, AdminKeys = new ArrayList<>(), LevelList;
	public String Whitelist, Blacklist, Nonuselist, OP, Player, All;
	public boolean FoldingSet;
	public Map<String, Object> Buttons;
	public static final File MenuFile;
	static {
		MenuFile = new File(Knickers.kis.getDataFolder(), Knickers.Menus);
	}
	private boolean MoreButton = kis.config.getBoolean("MoreButton");

	public MakeMenu(Player player, FormBase upForm, File file) {
		super(player, upForm);
		this.file = file;
		setK("{Player}", "{Money}", "{FilteredModel}", "{Permission}");
		config = new Config(file);
		FilteredModel = config.getInt("FilteredModel");
		Object obj = config.get("FilteredPlayer");
		FilteredPlayer = obj == null || !(obj instanceof List) ? new ArrayList<>() : (ArrayList<String>) obj;
		Whitelist = msg.getMessage("Whitelist", player);
		Blacklist = msg.getMessage("Blacklist", player);
		Nonuselist = msg.getMessage("Nonuselist", player);
		OP = msg.getSon(t, "OP", player);
		Player = msg.getSon(t, "Player", player);
		All = msg.getSon(t, "All", player);
		Permission = config.getInt("Permission");
		FoldingSet = kis.config.getBoolean("FoldingSet");
		LevelFilteredModel = Tool.ObjToInt(config.get("LevelFilteredModel"));
		obj = config.get("LevelList");
		LevelList = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
	}

	@Override
	public boolean MakeMain() {
		listKey.clear();
		AdminKeys.clear();
		setD(player.getName(), myPlayer.getMoney(), FilteredModel == 0 ? Nonuselist : FilteredModel == 1 ? Blacklist : Whitelist, Permission == 0 ? All : Permission == 1 ? OP : Player);
		if (!myPlayer.isAdmin()) {
			if ((FilteredModel == 1 && FilteredPlayer.contains(player.getName())) || (FilteredModel == 2 && !FilteredPlayer.contains(player.getName())))
				return sendMessage(msg.getMessage("FilteredModel", this));
			if ((Permission == 1 && !player.isOp()) || (Permission == 2 && player.isOp()))
				return sendMessage(msg.getSon(t, "notPermission", this));
			if ((LevelFilteredModel == 1 && LevelList.contains(player.getLevel().getFolderName())) || (LevelFilteredModel == 2 && !LevelList.contains(player.getLevel().getFolderName())))
				return sendMessage(msg.getSon(t, "WorldFiltered", this));
			double Money = Tool.objToDouble(config.get("Money"));
			if (Money != 0)
				if (myPlayer.getMoney() < Money)
					return sendMessage(msg.getMessage("notMoney", this));
			kis.getEconomy().reduceMoney(player, Money);
		}
		SimpleForm form = new SimpleForm(getID(), msg.getText(config.get("Title"), this), msg.getText(config.get("Content"), this));
		Object obj = config.get("Buttons");
		Buttons = new LinkedHashMap<>(obj == null || !(obj instanceof Map) ? new HashMap<>() : (Map<String, Object>) obj);
		Map<String, Object> map;
		String IconPath;
		int IconType;
		for (Map.Entry<String, Object> entry : Buttons.entrySet()) {
			map = entry.getValue() != null && entry.getValue() instanceof Map ? (HashMap<String, Object>) entry.getValue() : new HashMap<>();
			if (map == null || map.size() <= 0) {
				log.error(msg.getMessage("ButtonError", new String[] { "{Key}", "{Error}" }, new Object[] { entry.getKey(), "Data is empty" }, player));
				continue;
			}
			listKey.add(entry.getKey());
			if (MoreButton && kis.getButtons().size() > 0)
				for (BaseButton button : kis.getButtons().values())
					if (button.getKeys().contains(entry.getKey())) {
						form.addButton(button.getText(this, map));
						continue;
					}
			IconPath = Tool.objToString(map.get("IconPath"));
			IconType = Tool.ObjToInt(map.get("IconType"));
			form.addButton(msg.getText(map.get("Text")), IconType == 1, IconType == 0 ? null : IconPath);
		}
		if (myPlayer.isAdmin())
			if (FoldingSet) {
				AdminKeys.add("fa");
				form.addButton(msg.getSon(t, "FoldingSet", this));
			} else {
				AdminKeys.add("ab");
				form.addButton(msg.getSon(t, "AddButton", this));
				if (listKey.size() > 0) {
					AdminKeys.add("db");
					form.addButton(msg.getSon(t, "DelButton", this));
					AdminKeys.add("alb");
					form.addButton(msg.getSon(t, "AlterButton", this));
				}
				AdminKeys.add("am");
				form.addButton(msg.getSon(t, "AlterMenu", this));
				if (file.equals(kis.Main)) {
					AdminKeys.add("ss");
					form.addButton(msg.getSon(t, "Settings", this));
				}
			}
		AdminKeys.add("back");
		form.addButton(getBack());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		FormResponseSimple d = getSimple(data);
		int ID = d.getClickedButtonId();
		Object obj;
		String command, string;
		Level level;
		double x, y, z;
		if (ID >= listKey.size()) {
			switch (AdminKeys.get(ID - listKey.size())) {
			case "fa":
				return setForm(new FoldingSet(player, this, config, listKey)).make();
			case "ab":
				return setForm(new ButtonType(player, this, file)).make();
			case "db":
				return setForm(new DelButton(player, this, file)).make();
			case "alb":
				return setForm(new AlterButton(player, this, file)).make();
			case "am":
				return setForm(new AlterMenu(player, this, file)).make();
			case "ss":
				return setForm(new Setting(player, this)).make();
			default:
				return isBack();
			}
		} else {
			Map<String, Object> map = (Map<String, Object>) Buttons.get(listKey.get(ID));
			if (!myPlayer.isAdmin()) {
				int FilteredModel = Tool.ObjToInt(map.get("FilteredModel"));
				obj = map.get("FilteredPlayer");
				List<String> FilteredPlayer = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
				if ((FilteredModel == 1 && FilteredPlayer.contains(player.getName())) || (FilteredModel == 2 && !FilteredPlayer.contains(player.getName())))
					return sendMessage(msg.getMessage("FilteredModel", this));
				int Permission = Tool.ObjToInt(map.get("Permission"));
				if ((Permission == 1 && !player.isOp()) || (Permission == 2 && player.isOp()))
					return sendMessage(msg.getSon(t, "notPermission", this));
				int LevelFilteredModel = Tool.ObjToInt(map.get("LevelFilteredModel"));
				obj = map.get("LevelList");
				List<String> LevelList = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
				if ((LevelFilteredModel == 1 && LevelList.contains(player.getLevel().getFolderName())) || (LevelFilteredModel == 2 && !LevelList.contains(player.getLevel().getFolderName())))
					return sendMessage(msg.getSon(t, "WorldFiltered", this));
				double Money = Tool.objToDouble(map.get("Money"));
				if (Money != 0)
					if (myPlayer.getMoney() < Money)
						return sendMessage(msg.getMessage("notMoney", this));
				kis.getEconomy().reduceMoney(player, Money);
			}
			command = msg.getText(Tool.objToString(map.get("Command")), this);
			String Key = Tool.objToString(map.get("Type"), "Tip").toLowerCase();
			switch (Key) {
			case "tip":
			case "提示":
			case "弹窗":
				onCommand(command);
				return setForm(new MakeForm(player, this, map)).make();
			case "tp":
			case "传送":
				x = Tool.objToDouble(map.get("X"));
				z = Tool.objToDouble(map.get("Z"));
				y = Tool.objToDouble(map.get("Y"));
				string = Tool.objToString(map.get("World"));
				level = server.getLevelByName(string);
				if (level == null)
					return sendMessage(msg.getSon(t, "LevelError", this));
				player.teleport(new Location(x, y, z, level));
				return onCommand(command);
			case "open":
			case "menu":
			case "菜单":
			case "打开":
			case "ui":
				string = Tool.objToString("Config");
				if (string == null || string.isEmpty())
					return sendMessage(msg.getSun(t, "Menu", "菜单不存在", this));
				File file = new File(MenuFile, string);
				if (!file.exists() || file.isDirectory())
					return sendMessage(msg.getSun(t, "Menu", "菜单不存在", this));
				onCommand(command);
				return setForm(new MakeMenu(player, this, file)).make();
			case "tpa":
			case "传送玩家":
			case "传送到玩家":
				string = Tool.objToString(map.get("toPlayer"));
				boolean isAffirm = Tool.ObjToBool(map.get("isAffirm"));
				if (string == null)
					return onCommand(command) && setForm(new TPA(player, this, isAffirm)).make();
				Player toPlayer = server.getPlayer(string);
				if (toPlayer == null || !toPlayer.isOnline())
					return sendMessage(msg.getSun(t, "TPA", "Offline", Tool.Arrays(new String[] { "{ToPlayer}" }, getK()), Tool.Arrays(new Object[] { string }, getD())));
				if (isAffirm)
					return player.teleport(toPlayer) && onCommand(command);
				onCommand(command);
				return setForm(new ToPlayerTPA(toPlayer, this, player)).make();
			case "command":
			case "cnd":
			case "命令":
				if (myPlayer.SecurityPermissions)
					return sendMessage(msg.getSun(t, "Command", "SecurityPermission", this));
				if (command == null || command.isEmpty())
					return sendMessage(msg.getSun(t, "Command", "Error", this));
				if (command.contains("{msg}"))
					return setForm(new Command(player, this, map)).make();
				string = Tool.objToString(map.get("Commander"), "Player");
				switch (string.toLowerCase()) {
				case "console":
				case "c":
				case "控制台":
					return server.dispatchCommand(new ConsoleCommandSender(), command);
				case "op":
				case "admin":
				case "管理员":
					if (player.isOp())
						return server.dispatchCommand(player, command);
					myPlayer.SecurityPermissions = true;
					player.setOp(true);
					try {
						server.dispatchCommand(player, command);
					} catch (Exception e) {
						return sendMessage(msg.getSun(t, "Command", "Error", this));
					} finally {
						new SecurityPermissions(myPlayer).start();
					}
					return true;
				default:
					server.dispatchCommand(player, command);
					return true;
				}
			default:
				if (!MoreButton || kis.getButtons().size() <= 0)
					return Tip(getString("ButtonClone"));
				Map<String, BaseButton> buttons = kis.getButtons();
				for (BaseButton button : buttons.values())
					if (button.getKeys().contains(Key))
						return button.onClick(this, map);
				return Tip(getString("ButtonClone"));
			}
		}
	}

	/**
	 * 返回或关闭当前页面
	 * 
	 * @return
	 */
	@Override
	protected boolean isBack() {
		return upForm == null || !Tool.ObjToBool(config.get("isBack")) ? (myPlayer.form = null) == null : setForm(upForm).make();
	}

	private boolean onCommand(String Command) {
		if (Command == null || Command.isEmpty())
			return false;
		return server.dispatchCommand(player, Command);
	}
}
