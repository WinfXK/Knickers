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
	private long lastModified;
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
	boolean isload = false;
	public List<String> FilteredPlayer, AdminKeys = new ArrayList<>(), LevelList;
	public String Whitelist, Blacklist, Nonuselist, OP, Player, All;
	public boolean FoldingSet;
	public Map<String, Object> Buttons;
	public static final File MenuFile = new File(Knickers.kis.getDataFolder(), Knickers.Menus);
	private boolean MoreButton = kis.config.getBoolean("MoreButton");

	/**
	 * 根据菜单的文件对象创建一个菜单页面
	 * 
	 * @param player 需要显示菜单的玩家
	 * @param upForm 上一个界面
	 * @param file   菜单的配置文件
	 */
	public MakeMenu(Player player, FormBase upForm, File file) {
		super(player, upForm);
		this.player = player;
		this.file = file;
	}

	/**
	 * 加载菜单数据
	 */
	private void Loader() {
		if (isload && file.lastModified() == lastModified)
			return;
		isload = true;
		lastModified = file.lastModified();
		setK("{FilteredModel}", "{Permission}");
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
		setD(FilteredModel == 0 ? Nonuselist : FilteredModel == 1 ? Blacklist : Whitelist, Permission == 0 ? All : Permission == 1 ? OP : Player);

	}

	/**
	 * 构建菜单界面
	 */
	@Override
	public boolean MakeMain() {
		Loader();
		listKey.clear();
		AdminKeys.clear();
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
						button.getText(this, map, form);
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

	/**
	 * 处理菜单响应的结果
	 */
	@Override
	public boolean disMain(FormResponse data) {
		FormResponseSimple d = getSimple(data);
		int ID = d.getClickedButtonId();
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
		} else
			return onClick((Map<String, Object>) Buttons.get(listKey.get(ID)));
	}

	/**
	 * 检查玩家是否在等级限制范围内
	 * 
	 * @param map 按钮的数据
	 * @return
	 */
	public boolean isLevellimit(Map<String, Object> map) {
		Object obj = map.get("Levellimit");
		return isLevellimit(obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>(), Tool.objToString(map.get("Key")));
	}

	/**
	 * 检查玩家是否在等级限制范围内
	 * 
	 * @param list 等级限制范围
	 * @return
	 */
	public boolean isLevellimit(List<String> list, String Key) {
		if (list == null || list.size() <= 0) {
			log.warning(msg.getSon(t, "LevellimitWarning", new String[] { "{File}", "{Key}" }, new Object[] { file.getName(), Key }, player));
			return true;
		}
		double Exp;
		for (String s : list) {
			Exp = Tool.objToDouble(s.substring(1));
			switch (s.substring(0, 1)) {
			case "<":
				if (Exp > player.getExperienceLevel())
					return true;
				break;
			case ">":
				if (Exp < player.getExperienceLevel())
					return true;
				break;
			case "=":
			default:
				if (Exp == player.getExperienceLevel())
					return true;
				break;
			}
		}
		return false;
	}

	/**
	 * 将等级限制序列变成显示给万家康的文本
	 * 
	 * @param Msg  回复的消息(已解析)
	 * @param list 等级列表
	 * @return
	 */
	protected String getLevellimit(String Msg, List<String> list) {
		String string = "";
		String ss;
		for (String s : list) {
			ss = s.substring(0, 1);
			string += (string.isEmpty() ? "" : "\n") + (ss.equals("<") ? "小于" : ss.equals(">") ? "大于" : "等于") + Tool.objToDouble(s.substring(1));
		}
		return Msg.replace("{Levellimit}", string);
	}

	/**
	 * 处理按钮被点击的事件
	 * 
	 * @param map
	 * @return
	 */
	public boolean onClick(Map<String, Object> map) {
		Object obj;
		String command, string;
		Level level;
		double x, y, z;
		String[] cmds;
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
			boolean Openlevel = Tool.ObjToBool(map.get("Openlevel"));
			obj = map.get("Levellimit");
			List<String> Levellimit = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
			if (Openlevel && !isLevellimit(map))
				return sendMessage(getLevellimit(msg.getSon(t, "NoLevellimit", this), Levellimit));
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
			return setForm(new MakeForm(player, this, msg.getText(map.get("Title"), this), msg.getText(map.get("Content"), this), Tool.ObjToBool(map.get("isBack")),
					!Tool.objToString(map.get("TipType")).toLowerCase().equals("simple"))).make();
		case "tp":
		case "传送":
			x = Tool.objToDouble(map.get("X"));
			z = Tool.objToDouble(map.get("Z"));
			y = Tool.objToDouble(map.get("Y"));
			string = Tool.objToString(map.get("World"));
			level = string == null || string.isEmpty() ? player.getLevel() : server.getLevelByName(string);
			if (level == null)
				return sendMessage(msg.getSon(t, "LevelError", this));
			player.teleport(new Location(x, y, z, level));
			return onCommand(command);
		case "open":
		case "menu":
		case "菜单":
		case "打开":
		case "ui":
			string = Tool.objToString(map.get("Config"));
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
		case "cmd":
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
				cmds = command.split("\\{m\\}");
				for (String s : cmds)
					if (s != null && !s.isEmpty())
						try {
							server.dispatchCommand(new ConsoleCommandSender(), s);
						} catch (Exception e) {
						}
				return true;
			case "op":
			case "admin":
			case "管理员":
				if (player.isOp())
					return onCommand(command);
				myPlayer.SecurityPermissions = true;
				player.setOp(true);
				try {
					cmds = command.split("\\{m\\}");
					for (String s : cmds)
						if (s != null && !s.isEmpty())
							server.dispatchCommand(player, s);
				} catch (Exception e) {
					return sendMessage(msg.getSun(t, "Command", "Error", this));
				} finally {
					new SecurityPermissions(myPlayer).start();
				}
				return true;
			default:
				cmds = command.split("\\{m\\}");
				for (String s : cmds)
					if (s != null && !s.isEmpty())
						try {
							server.dispatchCommand(player, s);
						} catch (Exception e) {
						}
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

	/**
	 * 返回或关闭当前页面
	 * 
	 * @return
	 */
	@Override
	protected boolean isBack() {
		return upForm == null || Tool.ObjToBool(config.get("isBack")) ? (myPlayer.form = null) == null : setForm(upForm).make();
	}

	/**
	 * 返回按钮的内容
	 * 
	 * @return
	 */
	@Override
	protected String getBack() {
		return msg.getMessage(upForm != null && !Tool.ObjToBool(config.get("isBack")) ? "Back" : "Close", this);
	}

	/**
	 * 命令执行接口
	 * 
	 * @param Command
	 * @return
	 */
	private boolean onCommand(String Command) {
		if (Command == null || Command.isEmpty())
			return false;
		String[] cmds = Command.split("\\{m\\}");
		for (String string : cmds)
			if (string != null && !string.isEmpty())
				try {
					server.dispatchCommand(player, string);
				} catch (Exception e) {
				}
		return true;
	}
}
