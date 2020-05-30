package cn.winfxk.knickers.module;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.menu.OpenMenu;
import cn.winfxk.knickers.tool.SimpleForm;

/**
 * @Createdate 2020/05/30 19:42:39
 * @author Winfxk
 */
public class AlterButton extends OpenMenu {
	/**
	 * 显示要修改页面的按钮列表
	 * 
	 * @param player 要修改按钮的玩家对象
	 * @param upForm 上一个页面
	 * @param file   要修改按钮的配置文件对象
	 */
	public AlterButton(Player player, FormBase upForm, File file) {
		this(player, upForm, new Config(file, Config.YAML));
	}

	/**
	 * 显示要修改页面的按钮列表
	 * 
	 * @param player 要修改按钮的玩家对象
	 * @param upForm 上一个页面
	 * @param config 要修改按钮的配置文件对象
	 */
	public AlterButton(Player player, FormBase upForm, Config config) {
		super(player, upForm, config);
	}

	@Override
	public boolean MakeMain() {
		if (!myPlayer.isAdmin()) {
			player.sendMessage(getNotPermission());
			return isBack();
		}
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
		form.addButton(getBack());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = getSimple(data).getClickedButtonId();
		if (datas.size() > ID) {
			ModuleData data2 = datas.get(ID);
			if (data2.getFunctionBase() == null) {
				player.sendMessage(getString("Buttonoff"));
				return isBack();
			}
			return data2.getFunctionBase().alterButton(this, data2);
		}
		return isBack();
	}
}
