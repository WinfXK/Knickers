package cn.winfxk.knickers.module;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.base.CustomForm;
import cn.winfxk.knickers.tool.Config;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/08/07 21:49:40
 * @author Winfxk
 */
public abstract class BaseMake extends FormBase {
	protected File file;
	protected Config config;
	protected String Key = null;
	protected Map<String, Object> map = new HashMap<>(), Button;
	protected String Command, ButtonName;
	protected double Money;
	protected List<String> FilteredPlayer, WorldFiltered;
	protected int FilteredModel, Permission, LevelFilteredModel;
	protected FormResponseCustom d;
	protected CustomForm form;

	public BaseMake(Player player, File file) {
		super(player, null);
		this.file = file;
		config = new Config(file);
		setK("{Title}", "{ButtonCount}");
		Object obj = config.get("Buttons");
		Button = obj != null && obj instanceof Map ? (HashMap<String, Object>) obj : new HashMap<>();
		t = "MakeButton";
		setD(msg.getText(config.get("Title")), Button.size());
		map.put("Player", player.getName());
	}

	@Override
	public boolean disMain(FormResponse data) {
		String s;
		d = getCustom(data);
		ButtonName = d.getInputResponse(1);
		if (ButtonName == null || ButtonName.isEmpty())
			return Tip(getWarning("ButtonNameEmpty"));
		s = d.getInputResponse(2);
		if (!Tool.isInteger(s))
			return Tip(getWarning("MoneyError"));
		return true;
	}

	/**
	 * 返回一个创建界面
	 * 
	 * @return
	 */
	protected CustomForm getForm() {
		form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(InputName(), "", InputName());
		form.addInput(InputMoney(), "", InputMoney());
		form.addInput(InputCommand(), "", InputCommand());
		form.addStepSlider(getPermission(), getPermissions());

		return form;
	}

	/**
	 * 返回当前界面警告词
	 * 
	 * @param Key
	 * @return
	 */
	public String getWarning(String Key) {
		return msg.getSon(t, Key, this);
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
	protected String getWorldFiltra() {
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
		Tip(getNotPermission());
		return false;
	}

	/**
	 * 保存按钮
	 * 
	 * @return
	 */
	protected boolean save() {
		map.put("creaTime", Tool.getDate() + " " + Tool.getTime());
		map.put("Key", Key = Key == null ? getKey() : Key);
		map.put("Money", Money);
		map.put("Command", Command);
		map.put("Text", ButtonName);
		Button.put(Key, map);
		config.set("Buttons", Button);
		return config.save();
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
