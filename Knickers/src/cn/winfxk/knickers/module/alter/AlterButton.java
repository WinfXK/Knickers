package cn.winfxk.knickers.module.alter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.base.SimpleForm;
import cn.winfxk.knickers.module.BaseButton;
import cn.winfxk.knickers.tool.Config;
import cn.winfxk.knickers.tool.Tool;

/**
 * 用于在修改按钮时显示当前界面的按钮列表
 * 
 * @Createdate 2021/08/01 14:52:38
 * @author Winfxk
 */
public class AlterButton extends FormBase {
	protected File file;
	private Object object;
	protected Config config;
	protected Map<String, Object> Buttons;
	protected boolean MoreButton = kis.config.getBoolean("MoreButton");
	protected List<String> MoreKey = new ArrayList<>();

	public AlterButton(Player player, FormBase upForm, File file) {
		super(player, upForm);
		this.file = file;
		config = new Config(file);
	}

	@Override
	public boolean MakeMain() {
		MoreKey.clear();
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
						MoreKey.add(entry.getKey());
						continue;
					}
			IconPath = Tool.objToString(map.get("IconPath"));
			IconType = Tool.ObjToInt(map.get("IconType"));
			form.addButton(msg.getText(map.get("Text")), IconType == 1, IconType == 0 ? null : IconPath);
		}
		form.addButton(getBack());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		return true;
	}
}
