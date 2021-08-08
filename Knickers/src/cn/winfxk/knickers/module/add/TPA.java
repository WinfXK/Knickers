package cn.winfxk.knickers.module.add;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.BaseMake;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/08/07 21:48:44
 * @author Winfxk
 */
public class TPA extends BaseMake {
	public TPA(Player player, File file, FormBase upForm, String Key) {
		super(player, file, upForm, Key);
	}

	public TPA(Player player, File file, FormBase upForm) {
		super(player, file, upForm);
	}

	@Override
	public boolean MakeMain() {
		if (!super.MakeMain())
			return false;
		form.addInput(getString("InputToPlayer"), Key == null ? "" : map.get("toPlayer"), getString("InputToPlayer"));
		form.addToggle(getString("isAffirm"), Key == null || Tool.ObjToBool(map.get("isAffirm")));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		if (!super.disMain(data))
			return false;
		String ToPlayer = d.getInputResponse(location + 1);
		boolean isAffirm = d.getToggleResponse(location + 2);
		map.put("toPlayer", ToPlayer.isEmpty() ? null : ToPlayer);
		map.put("isAffirm", isAffirm);
		save();
		sendMessage(getString("Succeed"));
		return isBack();
	}
}
