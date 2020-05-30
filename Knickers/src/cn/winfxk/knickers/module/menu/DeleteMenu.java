package cn.winfxk.knickers.module.menu;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.DeleteFunction;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.tool.SimpleForm;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2020/05/23 20:02:30
 * @author Winfxk
 */
public class DeleteMenu extends DeleteFunction {

	public DeleteMenu(Player player, FormBase upForm, Config config, String Key) {
		super(player, upForm, config, Key);
	}

	@Override
	public boolean MakeMain() {
		setD(player.getName(), myPlayer.getMoney(), map.get("ButtonText"), getButtonData());
		SimpleForm form = new SimpleForm(getID(), getTitle(), getContent());
		form.addButton(getBack());
		form.addButton(getConfirm());
		form.addButton(getString("DeleteMenu"));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		int ID = getSimple(data).getClickedButtonId();
		if (ID == 1 || ID == 2) {
			if (ID == 2) {
				File file = new File(ac.getFunctionMag().getFile(), Tool.objToString(map.get("Menu")));
				if (file.exists())
					file.delete();
			}
			FunctionBase.removeButton(config, Key);
			sendMessage(getString("DeleteSucceed"));
		}
		return isBack();
	}
}
