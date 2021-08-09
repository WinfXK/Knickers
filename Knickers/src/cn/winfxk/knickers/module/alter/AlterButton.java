package cn.winfxk.knickers.module.alter;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.base.SimpleForm;
import cn.winfxk.knickers.module.BaseButton;
import cn.winfxk.knickers.module.add.Command;
import cn.winfxk.knickers.module.add.Menu;
import cn.winfxk.knickers.module.add.TP;
import cn.winfxk.knickers.module.add.TPA;
import cn.winfxk.knickers.module.add.Tip;
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
	protected Config config;
	protected Map<String, Object> Buttons;
	protected boolean MoreButton = kis.config.getBoolean("MoreButton");
	protected Map<String, String> MoreKey = new HashMap<>();

	public AlterButton(Player player, FormBase upForm, File file) {
		super(player, upForm);
		this.file = file;
		config = new Config(file);
	}

	@Override
	public boolean MakeMain() {
		if (!myPlayer.isAdmin())
			return sendMessage(getNotPermission(), false);
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
						MoreKey.put(entry.getKey(), button.getTGA());
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
		int ID = getSimple(data).getClickedButtonId();
		if (ID >= listKey.size())
			return isBack();
		String Key = listKey.get(ID);
		Map<String, Object> map = (Map<String, Object>) Buttons.get(Key);
		if (MoreKey.containsKey(Key))
			return kis.getButtons().get(MoreKey.get(Key)).onAlter(upForm, map);
		switch (Tool.objToString(map.get("Type")).toLowerCase()) {
		case "tip":
		case "提示":
		case "弹窗":
			return setForm(new Tip(player, file, upForm, Key)).make();
		case "tp":
		case "传送":
			return setForm(new TP(player, file, upForm, Key)).make();
		case "open":
		case "menu":
		case "菜单":
		case "打开":
		case "ui":
			return setForm(new Menu(player, file, upForm, Key)).make();
		case "tpa":
		case "传送玩家":
		case "传送到玩家":
			return setForm(new TPA(player, file, upForm, Key)).make();
		case "command":
		case "cmd":
		case "命令":
			return setForm(new Command(player, file, upForm, Key)).make();
		}
		return sendMessage(getString("AlterNotButton"), false);
	}
}
