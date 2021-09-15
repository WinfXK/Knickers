package cn.winfxk.knickers.form.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.base.SimpleForm;
import cn.winfxk.knickers.module.BaseButton;
import cn.winfxk.knickers.module.add.Command;
import cn.winfxk.knickers.module.add.Menu;
import cn.winfxk.knickers.module.add.TPA;
import cn.winfxk.knickers.module.add.Tip;
import cn.winfxk.knickers.tool.Config;

/**
 * 显示按钮类型的界面
 * 
 * @Createdate 2021/08/01 14:49:30
 * @author Winfxk
 */
public class ButtonType extends FormBase {
	private File file;
	private Config config;
	private List<BaseButton> base = new ArrayList<>();

	public ButtonType(Player player, FormBase upForm, File file) {
		super(player, upForm);
		this.file = file;
		config = new Config(file);
		setK("{Title}");
		Son = "SelectButton";
	}

	@Override
	public boolean MakeMain() {
		base.clear();
		setD(msg.getText(config.get("Title")));
		if (!myPlayer.isAdmin())
			return Tip(getNotPermission());
		SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
		listKey.add("tip");
		form.addButton(getString("MakeTip"));
		listKey.add("menu");
		form.addButton(getString("newMenu"));
		listKey.add("cmd");
		form.addButton(getString("MakeCmd"));
		listKey.add("tp");
		form.addButton(getString("Transfer"));
		listKey.add("tpa");
		form.addButton(getString("Leap"));
		if (kis.config.getBoolean("MoreButton") && kis.getButtons().size() > 0)
			for (BaseButton button : kis.getButtons().values()) {
				base.add(button);
				form.addButton(button.getName());
			}
		listKey.add("back");
		form.addButton(getBack());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = getSimple(data).getClickedButtonId();
		if (ID < listKey.size() || !kis.config.getBoolean("MoreButton") || kis.getButtons().size() <= 0)
			switch (listKey.get(ID)) {
			case "menu":
				return setForm(new Menu(player, file, upForm)).make();
			case "tp":
				return setForm(new Tip(player, file, upForm)).make();
			case "tpa":
				return setForm(new TPA(player, file, upForm)).make();
			case "cmd":
				return setForm(new Command(player, file, upForm)).make();
			case "tip":
				return setForm(new Tip(player, file, upForm)).make();
			default:
				return isBack();
			}
		return base.get(ID).onAdd(player, file, upForm);
	}
}
