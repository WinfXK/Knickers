package cn.winfxk.knickers.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.money.MyEconomy;
import cn.winfxk.knickers.tool.Tool;

/**
 * 创建按钮时会显示的界面基础类
 * 
 * @Createdate 2020/05/23 12:52:00
 * @author Winfxk
 */
public abstract class MakeBase extends FormBase {
	protected Config config;
	protected String Key = null;
	public static final String Whitelist = "Blank", Blacklist = "Black", Notfilter = "Not", Permission_OP = "OP",
			Permission_Player = "Player", Permission_All = "All", Permission_Admin = "Admin", ClickCommandSP = "{nn}",
			FilterSP = ";", LocalPath = "Local", CloudPath = "Cloud", NotPath = "Not";
	public static final String[] BaseKeys = { "{ButtonName}" };

	/**
	 * 创建按钮时创建的界面
	 * 
	 * @param player 创建按钮的玩家对象
	 * @param upForm 上一个页面
	 * @param config 创建界面的文件对象
	 */
	public MakeBase(Player player, FormBase upForm, Config config, FunctionBase base) {
		super(player, upForm);
		this.config = config;
		Son = base.getModuleKey();
		t = "Function";
		setK(BaseKeys);
		setD(Son);
	}

	/**
	 * 获取请输入图标路径的文本
	 * 
	 * @return
	 */
	public String getInputPath() {
		return msg.getSon(t, "InputPaht", this);
	}

	/**
	 * 获取选择图标类型的文本
	 * 
	 * @return
	 */
	public String getSelectPathType() {
		return msg.getSon(t, "SelectPathType", this);
	}

	/**
	 * 返回没有输入图标路径的文本
	 * 
	 * @return
	 */
	public String getNotInputPath() {
		return msg.getSon(t, "NotInputPath", this);
	}

	/**
	 * 返回图标的方式
	 * 
	 * @return
	 */
	public String[] getPathType() {
		return new String[] { getPathType1(), getPathType2(), getPathType3() };
	}

	/**
	 * 根据Form类选择的图标类型获取图标类型
	 * 
	 * @param i
	 * @return
	 */
	public String getPathType(int i) {
		switch (i) {
		case 1:
			return LocalPath;
		case 2:
			return CloudPath;
		default:
			return NotPath;
		}
	}

	/**
	 * 根据图标类型返回位置
	 * 
	 * @param Key
	 * @return
	 */
	public int getPathType(String Key) {
		switch (Key) {
		case LocalPath:
			return 1;
		case CloudPath:
			return 2;
		default:
			return 0;
		}
	}

	/**
	 * 获取选择图标类型1的文本<无>
	 * 
	 * @return
	 */
	public String getPathType1() {
		return msg.getSon(t, "PathType1", this);
	}

	/**
	 * 获取选择图标类型2的文本<自带>
	 * 
	 * @return
	 */
	public String getPathType2() {
		return msg.getSon(t, "PathType2", this);
	}

	/**
	 * 获取选择图标类型3的文本<云端>
	 * 
	 * @return
	 */
	public String getPathType3() {
		return msg.getSon(t, "PathType3", this);
	}

	/**
	 * 返回按钮名称的文本
	 * 
	 * @return
	 */
	protected String getButtonText() {
		return msg.getSon(t, "ButtonText", this);
	}

	@Override
	protected String getContent() {
		return msg.getSon("Function", "Content", this);
	}

	@Override
	protected String getTitle() {
		return msg.getSon(t, "MakeButton", this);
	}

	/**
	 * 返回没有输入按钮文本内容时的文本显示
	 * 
	 * @return
	 */
	protected String getNotInputButtonText() {
		return msg.getSon(t, "NotInputButtonText", this);
	}

	/**
	 * 返回点击将会执行命令的文本
	 * 
	 * @return
	 */
	protected String getClickCommand() {
		return msg.getSon(t, "ClickCommand", this);
	}

	/**
	 * 点击将会扣除金币的货币币种的文本
	 * 
	 * @return
	 */
	protected String getMoneyEconomy() {
		return msg.getSon(t, "ClickMoneyEconomy", this);
	}

	/**
	 * 返回选择玩家过滤模式的文本
	 * 
	 * @return
	 */
	protected String getPlayerBlacklistMode() {
		return msg.getSon(t, "SelectPlayerBlacklistMode", this);
	}

	/**
	 * 返回选择世界过滤模式的文本
	 * 
	 * @return
	 */
	protected String getWorldBlacklistMode() {
		return msg.getSon(t, "SelectWorldBlacklistMode", this);
	}

	/**
	 * 返回输入过滤的玩家名称的文本
	 * 
	 * @return
	 */
	protected String getInputBlacklistPlayer() {
		return msg.getSon(t, "InputBlacklistPlayer", this);
	}

	/**
	 * 返回输入过滤的地图名称的文本
	 * 
	 * @return
	 */
	protected String getInputBlacklistWorld() {
		return msg.getSon(t, "InputBlacklistWorld", this);
	}

	/**
	 * 返回不过滤模式的显示文本
	 */
	private String getNotMode() {
		return msg.getSon(t, "BlacklistMode3", this);
	}

	/**
	 * 返回黑名单模式的显示文本
	 */
	private String getBlackMode() {
		return msg.getSon(t, "BlacklistMode1", this);
	}

	/**
	 * 返回白名单模式的显示文本
	 */
	private String getBlankMode() {
		return msg.getSon(t, "BlacklistMode2", this);
	}

	/**
	 * 返回过滤列表的文本<br>
	 * 无-白-黑
	 * 
	 * @return
	 */
	protected String[] getModeList() {
		return new String[] { getNotMode(), getBlankMode(), getBlackMode() };
	}

	/**
	 * 返回黑白无名单的过滤方式<br>
	 * 无-白-黑
	 * 
	 * @return
	 */
	protected String getFiltertype(int i) {
		switch (i) {
		case 1:
			return Whitelist;
		case 2:
			return Blacklist;
		case 0:
		default:
			return Notfilter;
		}
	}

	/**
	 * 返回黑白无名单的过滤方式<br>
	 * 无-白-黑
	 * 
	 * @return
	 */
	protected int getFiltertype(String i) {
		switch (i) {
		case Whitelist:
			return 1;
		case Blacklist:
			return 2;
		case Notfilter:
		default:
			return 0;
		}
	}

	/**
	 * 返回能使用按钮的权限的文本
	 * 
	 * @return
	 */
	protected String getPermission() {
		return msg.getSon(t, "Permission", this);
	}

	/**
	 * 返回能使用按钮的权限-OP的文本
	 * 
	 * @return
	 */
	private String getPermission_OP() {
		return msg.getSon(t, "Permission-OP", this);
	}

	/**
	 * 返回能使用按钮的权限-Admin的文本
	 * 
	 * @return
	 */
	private String getPermission_Admin() {
		return msg.getSon(t, "Permission-Admin", this);
	}

	/**
	 * 返回能使用按钮的权限-Player的文本
	 * 
	 * @return
	 */
	private String getPermission_Player() {
		return msg.getSon(t, "Permission-Player", this);
	}

	/**
	 * 返回能使用按钮的权限-All的文本
	 * 
	 * @return
	 */
	private String getPermission_All() {
		return msg.getSon(t, "Permission-All", this);
	}

	/**
	 * 返回能使用按钮的权限的列表 <br>
	 * 所有-OP-Player-Admin
	 * 
	 * @return
	 */
	protected String[] getPermissions() {
		return new String[] { getPermission_All(), getPermission_OP(), getPermission_Player(), getPermission_Admin() };
	}

	/**
	 * 返回能使用按钮的权限的模式 <br>
	 * 所有-OP-Player-Admin
	 * 
	 * @return
	 */
	protected String getPermissionsType(int i) {
		switch (i) {
		case 3:
			return Permission_Admin;
		case 2:
			return Permission_Player;
		case 1:
			return Permission_OP;
		case 0:
		default:
			return Permission_All;
		}
	}

	/**
	 * 返回能使用按钮的权限的模式 <br>
	 * 所有-OP-Player-Admin
	 * 
	 * @return
	 */
	protected int getPermissionsType(String Key) {
		switch (Key) {
		case Permission_Admin:
			return 3;
		case Permission_Player:
			return 2;
		case Permission_OP:
			return 1;
		case Permission_All:
		default:
			return 0;
		}
	}

	/**
	 * 点击将会扣除金币的文本
	 * 
	 * @return
	 */
	protected String getMoney() {
		return msg.getSon(t, "ClickMoney", this);
	}

	/**
	 * 返回玩家设置的过滤列表
	 * 
	 * @param string
	 * @return
	 */
	protected List<String> getList(String string) {
		List<String> list = new ArrayList<>();
		if (string == null || string.isEmpty())
			return list;
		String[] strings = string.split(FilterSP);
		for (String s : strings)
			if (s != null && !s.isEmpty())
				list.add(s);
		return list;
	}

	/**
	 * 将设置好的按钮保存
	 * 
	 * @param map                 按钮的其他数据
	 * @param ButtonText          按钮会显示的文本
	 * @param Command             点击按钮将会执行的命令
	 * @param PlayerBlacklistMode 可使用按钮的玩家过滤方式
	 * @param PlayerBlacklist     要过滤的玩家列表
	 * @param WorldBlacklistMode  可以使用按钮的地图的过滤方式
	 * @param WorldBlacklist      要过滤的地图列表
	 * @param Money               点击将会扣除的金币
	 * @param economy             点击扣除金币需要使用的货币
	 * @param Permission          使用这个按钮需要的权限
	 * @return
	 */
	protected boolean save(Map<String, Object> map, String ButtonText, List<String> Command, String PlayerBlacklistMode,
			List<String> PlayerBlacklist, String WorldBlacklistMode, List<String> WorldBlacklist, double Money,
			MyEconomy economy, String Permission, String Path, String PathType) {
		map.put("Player", player.getName());
		map.put("Date", Tool.getDate() + " " + Tool.getTime());
		map.put("Type", Son);
		map.put("Command", Command);
		map.put("Playerfilter", PlayerBlacklistMode);
		map.put("Playerfilterlist", PlayerBlacklist);
		map.put("Worldfilter", WorldBlacklistMode);
		map.put("Worldfilterlist", WorldBlacklist);
		map.put("Money", Money);
		map.put("Economy", economy.getEconomyName());
		map.put("Permission", Permission);
		map.put("ButtonText", ButtonText);
		map.put("Path", Path);
		map.put("PathType", PathType);
		return FunctionBase.addButtons(config, map);
	}

	/**
	 * 将设置好的按钮保存
	 * 
	 * @param map                 按钮的其他数据
	 * @param ButtonText          按钮会显示的文本
	 * @param Command             点击按钮将会执行的命令
	 * @param PlayerBlacklistMode 可使用按钮的玩家过滤方式
	 * @param PlayerBlacklist     要过滤的玩家列表
	 * @param WorldBlacklistMode  可以使用按钮的地图的过滤方式
	 * @param WorldBlacklist      要过滤的地图列表
	 * @param Money               点击将会扣除的金币
	 * @param economy             点击扣除金币需要使用的货币
	 * @param Permission          使用这个按钮需要的权限
	 * @param Key                 这个按钮的Key
	 * @return
	 */
	protected boolean save(Map<String, Object> map, String ButtonText, List<String> Command, String PlayerBlacklistMode,
			List<String> PlayerBlacklist, String WorldBlacklistMode, List<String> WorldBlacklist, double Money,
			MyEconomy economy, String Permission, String Key, String Path, String PathType) {
		map.put("Player", player.getName());
		map.put("Date", Tool.getDate() + " " + Tool.getTime());
		map.put("Type", Son);
		map.put("Command", Command);
		map.put("Playerfilter", PlayerBlacklistMode);
		map.put("Playerfilterlist", PlayerBlacklist);
		map.put("Worldfilter", WorldBlacklistMode);
		map.put("Worldfilterlist", WorldBlacklist);
		map.put("Money", Money);
		map.put("Economy", economy.getEconomyName());
		map.put("Permission", Permission);
		map.put("ButtonText", ButtonText);
		map.put("Path", Path);
		map.put("PathType", PathType);
		return FunctionBase.addButtons(config, map, Key);
	}

	/**
	 * 将命令分割为多个命令
	 * 
	 * @param string
	 * @return
	 */
	protected List<String> getCommand(String string) {
		List<String> list = new ArrayList<>();
		if (string == null || string.isEmpty())
			return list;
		String[] strings = string.split("\\{nn\\}");
		for (String s : strings)
			if (s != null && s.isEmpty())
				list.add(s);
		return list;
	}

	/**
	 * 返回金币数量不合法的文本内容
	 * 
	 * @return
	 */
	protected String getMoneyillegal() {
		return msg.getSon(t, "Moneyillegal", this);
	}
}