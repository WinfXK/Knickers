package cn.winfxk.knickers.module.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.Setting;
import cn.winfxk.knickers.module.AlterButton;
import cn.winfxk.knickers.module.DelButton;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.IncreaseButton;
import cn.winfxk.knickers.module.ModuleData;
import cn.winfxk.knickers.tool.SimpleForm;

/**
 * 打开一个菜单
 * 
 * @Createdate 2020/05/23 21:07:11
 * @author Winfxk
 */
public class OpenMenu extends FormBase {
	protected Config config;
	protected Map<String, Object> Buttons;
	protected List<ModuleData> datas = new ArrayList<>();

	/**
	 * 打开一个菜单
	 * 
	 * @param player 要打开菜单的玩家对象
	 * @param upForm 上个界面
	 * @param file   要打开的菜单的文件对象
	 */
	public OpenMenu(Player player, FormBase upForm, File file) {
		this(player, upForm, new Config(file, Config.YAML));
	}

	/**
	 * 打开一个菜单
	 * 
	 * @param player 要打开菜单的玩家对象
	 * @param upForm 上个界面
	 * @param config 要打开的菜单的配置文件对象
	 */
	public OpenMenu(Player player, FormBase upForm, Config config) {
		super(player, upForm);
		this.config = config;
		t = FunctionBase.FunctionKey;
		Son = MenuButton.MenuKey;
	}

	@Override
	public boolean MakeMain() {
		Buttons = FunctionBase.getButtons(config);
		SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
		ModuleData data;
		Map<String, Object> map;
		for (Object obj : Buttons.values()) {
			map = obj == null || !(obj instanceof Map) ? new HashMap<>() : (HashMap<String, Object>) obj;
			if (map.size() <= 0)
				continue;
			data = new ModuleData(config, map);
			if ((data.getFunctionBase() == null || !data.getFunctionBase().isEnable())
					&& ((!myPlayer.isAdmin() && ac.getConfig().getBoolean("DisablesDisplay"))
							|| (myPlayer.isAdmin() && ac.getConfig().getBoolean("AdminDisablesDisplay"))))
				continue;
			form.addButton(data.getFunctionBase() != null
					? data.getFunctionBase().isEnable() ? data.getFunctionBase().getButtonString(this, data)
							: data.getKey() + getString("DisablesString")
					: data.getKey() + getString("DisablesString"));
			datas.add(data);
		}
		if (myPlayer.isAdmin()) {
			if (form.getButtonSize() > 0) {
				listKey.add("db");
				form.addButton(getString("DeleteButton"));
				listKey.add("ab");
				form.addButton(getString("AlterButton"));
			}
			listKey.add("cb");
			form.addButton(getString("CreateButton"));
			if (upForm == null) {
				listKey.add("sb");
				form.addButton(getString("SettingButton"));
			}
		}
		listKey.add("bb");
		form.addButton(getBack());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = getSimple(data).getClickedButtonId();
		if (datas.size() > ID) {
			ModuleData data2 = datas.get(ID);
			if (data2.getFunctionBase() == null || !data2.getFunctionBase().isEnable()) {
				player.sendMessage(getString("Buttonoff"));
				return isBack();
			}
			return data2.getFunctionBase().Click(this, data2);
		}
		switch (listKey.get(ID - datas.size())) {
		case "db":
			setForm(new DelButton(player, upForm, config));
			break;
		case "ab":
			setForm(new AlterButton(player, upForm, config));
			break;
		case "cb":
			setForm(new IncreaseButton(player, upForm, config));
			break;
		case "sb":
			setForm(new Setting(player, upForm));
			break;
		default:
			return isBack();
		}
		return make();
	}

	@Override
	protected String getContent() {
		return msg.getText(config.get("Content"), this);
	}

	@Override
	protected String getTitle() {
		return msg.getText(config.get("Name"), this);
	}
}
