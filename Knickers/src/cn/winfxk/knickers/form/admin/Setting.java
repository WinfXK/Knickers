package cn.winfxk.knickers.form.admin;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.winfxk.knickers.Knickers;
import cn.winfxk.knickers.Period;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.base.CustomForm;
import cn.winfxk.knickers.money.MyEconomy;
import cn.winfxk.knickers.tool.Config;
import cn.winfxk.knickers.tool.Tool;

/**
 * 系统设置
 * 
 * @Createdate 2021/08/01 14:56:52
 * @author Winfxk
 */
public class Setting extends FormBase {
	private static Config config = Knickers.kis.config;
	private List<String> EconomyList;

	public Setting(Player player, FormBase upForm) {
		super(player, upForm);
		Son = "Setting";
	}

	@Override
	public boolean MakeMain() {
		if (!myPlayer.isAdmin())
			return Tip(getNotPermission(), false);
		EconomyList = new ArrayList<>(Knickers.Economys.keySet());
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getString("InputMoneyName"), config.get("MoneyName"), getString("InputMoneyName"));
		form.addDropdown(getString("SelectEconomy"), EconomyList, EconomyList.indexOf(kis.getEconomy().getEconomyName()));
		form.addToggle(getString("DiscardTool"), config.getBoolean("DiscardTool"));
		form.addInput(getString("Tool"), config.get("Tool"), getString("Tool"));
		form.addToggle(getString("EternalTool"), config.getBoolean("EternalTool"));
		form.addInput(getString("EternalToolTime"), config.getInt("EternalToolTime"), getString("EternalToolTime"));
		form.addInput(getString("PalmRejection"), config.getInt("PalmRejection"), getString("PalmRejection"));
		form.addToggle(getString("EventCancelled"), config.getBoolean("EventCancelled"));
		form.addToggle(getString("FoldingSet"), config.getBoolean("FoldingSet"));
		form.addInput(getString("SecurityPermission"), config.getInt("SecurityPermission"), getString("SecurityPermission"));
		form.addInput(getString("AcceptTPAWait"), config.getInt("AcceptTPAWait"), getString("AcceptTPAWait"));
		form.addToggle(getString("MoreButton"), config.getBoolean("MoreButton"));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		FormResponseCustom d = getCustom(data);
		String MoneyName = d.getInputResponse(1);
		if (MoneyName == null || MoneyName.isEmpty())
			return Tip(getString("MoneyNameError"));
		MyEconomy MoneyApi = Knickers.Economys.get(EconomyList.get(d.getDropdownResponse(2).getElementID()));
		boolean DiscardTool = d.getToggleResponse(3);
		String tool = d.getInputResponse(4);
		boolean EternalTool = d.getToggleResponse(5);
		String string = d.getInputResponse(6);
		int EternalToolTime = Tool.ObjToInt(string);
		if (string == null || string.isEmpty() || !Tool.isInteger(string) || EternalToolTime <= 0)
			return Tip(getString("EternalToolTimeError"));
		string = d.getInputResponse(7);
		int PalmRejection = Tool.ObjToInt(string);
		if (string == null || string.isEmpty() || !Tool.isInteger(string) || PalmRejection <= 0)
			return Tip(getString("PalmRejectionError"));
		boolean EventCancelled = d.getToggleResponse(8);
		boolean FoldingSet = d.getToggleResponse(9);
		string = d.getInputResponse(10);
		int SecurityPermission = Tool.ObjToInt(string);
		if (string == null || string.isEmpty() || !Tool.isInteger(string) || SecurityPermission <= 0)
			return Tip(getString("SecurityPermissionError"));
		string = d.getInputResponse(11);
		int AcceptTPAWait = Tool.ObjToInt(string);
		if (string == null || string.isEmpty() || !Tool.isInteger(string) || AcceptTPAWait < 0)
			return Tip(getString("AcceptTPAWaitError"));
		boolean MoreButton = d.getToggleResponse(12);
		config.set("MoneyName", MoneyName);
		config.set("MoneyAPI", MoneyApi);
		config.set("DiscardTool", DiscardTool);
		config.set("Tool", tool);
		config.set("EternalTool", Period.EternalTool = EternalTool);
		config.set("EternalToolTime", Period.EternalToolTime = EternalToolTime);
		config.set("PalmRejection", PalmRejection);
		config.set("EventCancelled", EventCancelled);
		config.set("FoldingSet", FoldingSet);
		config.set("SecurityPermission", SecurityPermission);
		config.set("AcceptTPAWait", AcceptTPAWait);
		config.set("MoreButton", MoreButton);
		config.save();
		sendMessage(getString("Succeed"));
		return isBack();
	}
}
