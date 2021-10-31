package cn.winfxk.knickers.module;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeForm;
import cn.winfxk.knickers.form.base.CustomForm;
import cn.winfxk.knickers.tool.Config;
import cn.winfxk.knickers.tool.Tool;

/**
 * 构建一个基本按钮数据处理类
 * 
 * @Createdate 2021/08/07 21:49:40
 * @author Winfxk
 */
public abstract class BaseMake extends FormBase {
	protected File file;
	protected Config config;
	protected String Key = null;
	protected Map<String, Object> map = new HashMap<>(), Button;
	protected String Command, ButtonName, IconPath;
	protected double Money;
	protected List<String> PlayerFiltered, WorldFiltered, Levellimit;
	protected int PlayerFilteredModel, Permission, LevelFilteredModel, location, IconType;
	protected FormResponseCustom d;
	protected CustomForm form;
	public static final String[] LevellimitKey = { "<", ">", "=" };
	protected boolean Openlevel;

	/**
	 * 修改按钮时调用
	 * 
	 * @param player 修改按钮的玩家对象
	 * @param file   要修改的按钮文件对象
	 * @param upForm 上个界面
	 * @param Key    按钮的Key
	 */
	public BaseMake(Player player, File file, FormBase upForm, String Key) {
		super(player, upForm);
		this.file = file;
		config = new Config(file);
		setK("{Title}", "{ButtonCount}");
		Object obj = config.get("Buttons");
		Button = obj != null && obj instanceof Map ? (HashMap<String, Object>) obj : new HashMap<>();
		obj = Button.get(Key);
		map = obj != null && obj instanceof Map ? (HashMap<String, Object>) obj : new HashMap<>();
		t = "MakeButton";
		setD(msg.getText(config.get("Title")), Button.size());
		map.put("alterPlayer", player.getName());
		this.Key = Key;
	}

	/**
	 * 新增按钮基础类
	 * 
	 * @param player 修改按钮的玩家对象
	 * @param file   要修改的按钮文件对象
	 * @param upForm 上个界面
	 */
	public BaseMake(Player player, File file, FormBase upForm) {
		super(player, upForm);
		this.file = file;
		config = new Config(file);
		setK("{Title}", "{ButtonCount}");
		Object obj = config.get("Buttons");
		Button = obj != null && obj instanceof Map ? (HashMap<String, Object>) obj : new HashMap<>();
		t = "MakeButton";
		setD(msg.getText(config.get("Title")), Button.size());
		map.put("Player", player.getName());
		map.put("Type", getName());
	}

	/**
	 * 粗处理
	 */
	@Override
	public boolean MakeMain() {
		if (!isPrmission()) {
			isBack();
			return false;
		}
		getForm();
		return true;
	}

	/**
	 * 粗处理
	 */
	@Override
	public boolean disMain(FormResponse data) {
		String s;
		d = getCustom(data);
		ButtonName = d.getInputResponse(1);
		if (ButtonName == null || ButtonName.isEmpty())
			return Tip(getWarning("ButtonNameEmpty"), false);
		s = d.getInputResponse(2);
		s = s == null || s.isEmpty() ? "0" : s;
		if (!Tool.isInteger(s))
			return Tip(getWarning("MoneyError"), false);
		Command = d.getInputResponse(3);
		Command = Command == null ? "" : Command;
		Permission = d.getStepSliderResponse(4).getElementID();
		PlayerFilteredModel = d.getStepSliderResponse(5).getElementID();
		PlayerFiltered = getList(d.getInputResponse(6));
		LevelFilteredModel = d.getStepSliderResponse(7).getElementID();
		WorldFiltered = getList(d.getInputResponse(8));
		IconType = d.getStepSliderResponse(9).getElementID();
		IconPath = d.getInputResponse(10);
		IconPath = IconPath.isEmpty() ? null : IconPath;
		Openlevel = d.getToggleResponse(11);
		Levellimit = getLevellimit(d.getInputResponse(12));
		if (IconType != 0 && IconPath == null)
			return Tip(getWarning("IconPathEmpty"), false);
		if (IconType == 1)
			IconPath = itemList.objToPath(IconPath, false, IconPath);
		return true;
	}

	/**
	 * 返回一个创建界面
	 * 
	 * @return
	 */
	protected CustomForm getForm() {
		form = new CustomForm(getID(), Key == null ? getTitle() : getString("AlterTitle"));
		form.addLabel(Key == null ? getContent() : getString("AlterContent"));
		form.addInput(InputName(), Key == null ? "" : map.get("Text"), InputName());
		form.addInput(InputMoney(), Key == null ? 0 : map.get("Money"), InputMoney());
		form.addInput(InputCommand(), Key == null ? "" : map.get("Command"), InputCommand());
		form.addStepSlider(getPermission(), getPermissions(), Key == null ? 0 : Tool.ObjToInt(map.get("Permission")));
		form.addStepSlider(getPlayerFiltra(), getFiltras(), Key == null ? 0 : Tool.ObjToInt(map.get("FilteredModel")));
		form.addInput(getFiltreList(), Key == null ? "" : getFiltreList(map.get("FilteredPlayer")), getFiltreList());
		form.addStepSlider(getWorldFiltras(), getFiltras(), Key == null ? 0 : Tool.ObjToInt(map.get("LevelFilteredModel")));
		form.addInput(getFiltreList(), Key == null ? "" : getFiltreList(map.get("LevelList")), getFiltreList());
		form.addStepSlider(getIconType(), getIconTypes(), Tool.ObjToInt(map.get("IconType")));
		form.addInput(InputIconPath(), map.get("IconPath"), InputIconPath());
		form.addToggle(getOpenlevel(), Key == null ? false : Tool.ObjToBool(map.get("Openlevel")));
		form.addInput(getLevellimitString(), Key == null ? "" : getLevellimit(map.get("Levellimit")), getLevellimitString());
		location = form.getElements().size() - 1;
		return form;
	}

	/**
	 * 将已经存储的等级区间序列化
	 * 
	 * @param obj
	 * @return
	 */
	protected String getLevellimit(Object obj) {
		ArrayList<String> list = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
		String string = "";
		for (String s : list)
			string += (string.isEmpty() ? "" : ";") + s;
		return string;
	}

	/**
	 * 将已经存储的等级区间序列化
	 * 
	 * @param obj
	 * @return
	 */
	protected ArrayList<String> getLevellimit(String string) {
		ArrayList<String> list = new ArrayList<>();
		if (string == null || string.isEmpty())
			return list;
		String[] strings = string.split(";");
		for (String s : strings) {
			if (!Tool.isInteger(s.substring(1)))
				continue;
			list.add(s);
		}
		return list;
	}

	/**
	 * 返回提示等级限制区间的文本
	 * 
	 * @return
	 */
	protected String getLevellimitString() {
		return msg.getSon(t, "Levellimit", this);
	}

	/**
	 * 吧字符数组转换为字符串
	 * 
	 * @param obj
	 * @return
	 */
	protected String getFiltreList(Object obj) {
		List<String> list = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
		String string = "";
		for (String s : list)
			string = string + (string.isEmpty() ? s : ";" + s);
		return string;
	}

	/**
	 * 返回是否开启等级限制的文本
	 * 
	 * @return
	 */
	protected String getOpenlevel() {
		return msg.getSon(t, "Openlevel", this);
	}

	/**
	 * 保存按钮
	 * 
	 * @return
	 */
	protected boolean save() {
		map.put(Key == null ? "creaTime" : "alterTime", Tool.getDate() + " " + Tool.getTime());
		map.put("Key", Key = Key == null ? getKey() : Key);
		map.put("Money", Money);
		map.put("Command", Command);
		map.put("Text", ButtonName);
		map.put("FilteredModel", PlayerFilteredModel);
		map.put("FilteredPlayer", PlayerFiltered);
		map.put("LevelFilteredModel", LevelFilteredModel);
		map.put("LevelList", WorldFiltered);
		map.put("Permission", Permission);
		map.put("IconType", IconType);
		map.put("IconPath", IconPath);
		map.put("Openlevel", Openlevel);
		map.put("Levellimit", Levellimit);
		Button.put(Key, map);
		config.set("Buttons", Button);
		return config.save();
	}

	/**
	 * 将过滤字符串转换为数组
	 * 
	 * @param s
	 * @return
	 */
	protected List<String> getList(String s) {
		List<String> list = new ArrayList<>();
		if (s != null && !s.isEmpty()) {
			String[] strings = s.split(";");
			for (String string : strings)
				if (string != null && !string.isEmpty())
					list.add(string);
		}
		return list;
	}

	/**
	 * 返回当前界面警告词
	 * 
	 * @param Key
	 * @return
	 */
	protected String getWarning(String Key) {
		return msg.getSon(t, Key, this);
	}

	/**
	 * 选择按钮图标类型的字符串
	 * 
	 * @return
	 */
	protected String[] getIconTypes() {
		return new String[] { getWarning("IconEmpty"), getWarning("IconLocal"), getWarning("IconNet") };
	}

	/**
	 * 返回提示输入按钮图标路径的字符串
	 * 
	 * @return
	 */
	protected String InputIconPath() {
		return getWarning("IconPath");
	}

	/**
	 * 选择按钮图标类型的字符串
	 * 
	 * @return
	 */
	protected String getIconType() {
		return getWarning("IconType");
	}

	/**
	 * 返回过滤列表的字符串
	 * 
	 * @return
	 */
	protected String getFiltreList() {
		return getWarning("FiltreList");
	}

	/**
	 * 返回可以选择的权限
	 * 
	 * @return
	 */
	protected String[] getPermissions() {
		return new String[] { msg.getSon("Form", "All"), msg.getSon("Form", "OP"), msg.getSon("Form", "Player") };
	}

	/**
	 * 返回过滤模式的字符串
	 * 
	 * @return
	 */
	protected String[] getFiltras() {
		return new String[] { msg.getMessage("Nonuselist", this), msg.getMessage("Blacklist", this), msg.getMessage("Whitelist", this) };
	}

	/**
	 * 返回玩家过滤模式的字符串
	 * 
	 * @return
	 */
	protected String getPlayerFiltra() {
		return getWarning("PlayerFiltra");
	}

	/**
	 * 返回地图过滤的字符串
	 * 
	 * @return
	 */
	protected String getWorldFiltras() {
		return getWarning("WorldFiltra");
	}

	/**
	 * 返回显示权限字符串
	 * 
	 * @return
	 */
	protected String getPermission() {
		return getWarning("Permission");
	}

	/**
	 * 返回提示输入金币数的字符串
	 * 
	 * @return
	 */
	protected String InputMoney() {
		return getWarning("InputMoney");
	}

	/**
	 * 返回提示输入命令的字符串
	 * 
	 * @return
	 */
	protected String InputCommand() {
		return getWarning("InputCommand");
	}

	/**
	 * 返回提示输入按钮名称的字符
	 * 
	 * @return
	 */
	protected String InputName() {
		return getWarning("InputButtonName");
	}

	/**
	 * 判断是否有权限
	 * 
	 * @return
	 */
	protected boolean isPrmission() {
		if (myPlayer.isAdmin())
			return true;
		return setForm(new MakeForm(player, upForm, msg.getMessage("Tip", this), getNotPermission(), true, true)).make();
	}

	/**
	 * 返回一个Button里面不会重复的Key
	 * 
	 * @return
	 */
	private String getKey() {
		String string = Tool.getRandString();
		while (Button.containsKey(string))
			string += Tool.getRandString();
		return string;
	}
}
