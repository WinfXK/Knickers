package cn.winfxk.knickers.module.alter;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.MakeMenu;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.base.SimpleForm;
import cn.winfxk.knickers.module.alter.DelButton.Data;
import cn.winfxk.knickers.tool.Format;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/08/09 18:28:04
 * @author Winfxk
 */
public class ConfirmDeletion extends FormBase {
	private Data data;

	public ConfirmDeletion(Player player, FormBase upForm, Data data) {
		super(player, upForm);
		setD(new Format<>(data.map).getString());
		setK("{ContentData}");
	}

	@Override
	public boolean MakeMain() {
		if (!myPlayer.isAdmin())
			return sendMessage(getNotPermission(), false);
		SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
		form.addButton(msg.getMessage("Confirm", this));
		form.addButton(getBack());
		switch (Tool.objToString(data.map.get("Type")).toLowerCase()) {
		case "open":
		case "menu":
		case "菜单":
		case "打开":
		case "ui":
			form.addButton(msg.getSon("MakeButton", "Confirm", this));
		}
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = getSimple(data).getClickedButtonId();
		if (ID == 0 || ID == 2) {
			if (ID == 2) {
				File file = new File(MakeMenu.MenuFile, Tool.objToString(this.data.map.get("Config")));
				file.delete();
			}
			this.data.Buttons.remove(this.data.Key);
			this.data.config.set("Buttons", this.data.Buttons);
			this.data.config.save();
			sendMessage(msg.getSon("MakeButton", "DelSucceed"));
		}
		return isBack();
	}

	@Override
	protected String getTitle() {
		return msg.getSon("MakeButton", "DelTitle", this);
	}

	@Override
	protected String getContent() {
		return msg.getSon("MakeButton", "DelContent", this);
	}
}
