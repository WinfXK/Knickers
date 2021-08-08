package cn.winfxk.knickers.module.add;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.BaseMake;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/08/07 21:48:24
 * @author Winfxk
 */
public class Tip extends BaseMake {
	public Tip(Player player, File file, FormBase upForm, String Key) {
		super(player, file, upForm, Key);
	}

	public Tip(Player player, File file, FormBase upForm) {
		super(player, file, upForm);
	}

	@Override
	public boolean MakeMain() {
		if (!super.MakeMain())
			return false;
		form.addInput(getString("InputTitle"), Key == null ? msg.config.get("Tip") : map.get("Title"), getString("InputTitle"));
		form.addInput(getString("InputContent"), Key == null ? "" : map.get("Content"), getString("InputContent"));
		form.addToggle(getString("InputType"), Key == null || Tool.ObjToBool(map.get("TipType")));
		form.addToggle(getString("AllowBack"), Key == null || Tool.ObjToBool(map.get("isBack")));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		if (!super.disMain(data))
			return false;
		String Title = d.getInputResponse(location + 1);
		String Content = d.getInputResponse(location + 2);
		boolean Modal = d.getToggleResponse(location + 3);
		boolean AllowBack = d.getToggleResponse(location + 4);
		map.put("Title", Title == null ? "" : Title);
		map.put("Content", Content == null ? "" : Content);
		map.put("TipType", !Modal ? "Simple" : "Modal");
		map.put("isBack", AllowBack);
		save();
		sendMessage(getString("Succeed"));
		return isBack();
	}
}
