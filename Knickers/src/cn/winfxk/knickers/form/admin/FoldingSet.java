package cn.winfxk.knickers.form.admin;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.admin.add.ButtonType;
import cn.winfxk.knickers.form.admin.alterbutton.AlterButton;
import cn.winfxk.knickers.form.admin.altermenu.AlterMenu;
import cn.winfxk.knickers.form.admin.del.DelButton;
import cn.winfxk.knickers.form.base.SimpleForm;
import cn.winfxk.knickers.tool.Config;

/**
 * @Createdate 2021/08/01 14:46:06
 * @author Winfxk
 */
public class FoldingSet extends FormBase {
	private Config config;
	private List<String> AdminKeys = new ArrayList<>();

	public FoldingSet(Player player, FormBase upForm, Config config, List<String> listKeys) {
		super(player, upForm);
		this.config = config;
		this.listKey = listKeys;
	}

	@Override
	public boolean MakeMain() {
		if (!myPlayer.isAdmin())
			return sendMessage(msg.getMessage("notPermission", this));
		AdminKeys.clear();
		SimpleForm form = new SimpleForm(getID(), msg.getText(config.get("Title"), this), msg.getText(config.get("Content"), this));
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
		if (config.getFile().equals(kis.Main)) {
			AdminKeys.add("ss");
			form.addButton(msg.getSon(t, "Settings", this));
		}
		AdminKeys.add("back");
		form.addButton(getBack());
		form.sendPlayer(player);
		return false;
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = getSimple(data).getClickedButtonId();
		switch (AdminKeys.get(ID)) {
		case "ab":
			return setForm(new ButtonType(player, this, config.getFile())).make();
		case "db":
			return setForm(new DelButton(player, this, config.getFile())).make();
		case "alb":
			return setForm(new AlterButton(player, this, config.getFile())).make();
		case "am":
			return setForm(new AlterMenu(player, this, config.getFile())).make();
		case "ss":
			return setForm(new Setting(player, this)).make();
		default:
			return isBack();
		}
	}
}
