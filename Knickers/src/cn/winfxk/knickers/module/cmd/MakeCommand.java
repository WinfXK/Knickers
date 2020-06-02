package cn.winfxk.knickers.module.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.money.MyEconomy;
import cn.winfxk.knickers.tool.CustomForm;
import cn.winfxk.knickers.tool.Tool;

/**
 * 创建Command按钮时显示的界面
 * 
 * @Createdate 2020/06/02 19:53:45
 * @author Winfxk
 */
public class MakeCommand extends MakeBase {
	public MakeCommand(Player player, FormBase upForm, Config config, FunctionBase base) {
		super(player, upForm, config, base);
	}

	@Override
	public boolean MakeMain() {
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getButtonText(), "", getButtonText());
		form.addInput(getString("ClickCommand"), "", getString("ClickCommand"));
		form.addInput(getString("InputTip"), "", getString("InputTip"));
		form.addInput(getString("InputHint"), "", getString("InputHint"));
		form.addDropdown(getPlayerBlacklistMode(), getModeList());
		form.addInput(getInputBlacklistPlayer(), "", getInputBlacklistPlayer());
		form.addDropdown(getWorldBlacklistMode(), getModeList());
		form.addInput(getInputBlacklistWorld(), "", getInputBlacklistWorld());
		form.addInput(getMoney(), "", getMoney());
		form.addDropdown(getMoneyEconomy(), ac.getEconomyManage().getEconomy());
		form.addDropdown(getPermission(), getPermissions());
		form.addInput(getString("VariableTitle"), "", getString("VariableTitle"));
		form.addInput(getString("VariableContent"), "", getString("VariableContent"));
		form.addToggle(getString("AllowBlank"), false);
		form.addInput(getInputPath(), "", getInputPath());
		form.addDropdown(getSelectPathType(), getPathType());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse d) {
		FormResponseCustom data = getCustom(d);
		String ButtonText = data.getInputResponse(1);
		if (ButtonText == null || ButtonText.isEmpty()) {
			sendMessage(getNotInputButtonText());
			return MakeMain();
		}
		String Command = data.getInputResponse(2);
		if (Command == null || Command.isEmpty()) {
			sendMessage(getString("notInputCommand"));
			return MakeMain();
		}
		String PlayerBlacklistMode = getFiltertype(data.getDropdownResponse(5).getElementID());
		List<String> PlayerBlacklist = getList(data.getInputResponse(6));
		String WorldBlacklistMode = getFiltertype(data.getDropdownResponse(7).getElementID());
		List<String> WorldBlacklist = getList(data.getInputResponse(8));
		String MoneyString = data.getInputResponse(9);
		if (MoneyString != null && !MoneyString.isEmpty() && !Tool.isInteger(MoneyString)) {
			sendMessage(getMoneyillegal());
			return MakeMain();
		}
		double Money = Tool.objToDouble(MoneyString);
		Money = Money <= 0 ? 0 : Money;
		MyEconomy economy = ac.getEconomyManage().getEconomy(data.getDropdownResponse(10).getElementContent());
		String Permission = getPermissionsType(data.getDropdownResponse(11).getElementID());
		List<String> Tip = getHint(data.getInputResponse(3));
		List<String> Hint = getHint(data.getInputResponse(4));
		String VariableTitle = data.getInputResponse(12);
		String VariableContent = data.getInputResponse(13);
		boolean AllowBlank = data.getToggleResponse(14);
		String Path = data.getInputResponse(15);
		String PathType = getPathType(data.getDropdownResponse(16).getElementID());
		if (!PathType.equals(MakeBase.NotPath)) {
			player.sendMessage(getNotInputPath());
			return MakeMain();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("Tip", Tip);
		map.put("Hint", Hint);
		map.put("ClickCommand", Command);
		map.put("VariableTitle", VariableTitle);
		map.put("VariableContent", VariableContent);
		map.put("AllowBlank", AllowBlank);
		player.sendMessage(getString(Key == null ? "CreateOK" : "AlterOK"));
		boolean isOK = save(map, ButtonText, new ArrayList<String>(), PlayerBlacklistMode, PlayerBlacklist,
				WorldBlacklistMode, WorldBlacklist, Money, economy, Permission, Key, Path, PathType);
		return isOK && isBack();
	}

	/**
	 * 将Hint文本转换为list
	 * 
	 * @param s
	 * @return
	 */
	protected List<String> getHint(String s) {
		List<String> list = new ArrayList<>();
		String[] strings = s.split(CommandButton.HintSP);
		for (String sb : strings)
			if (sb != null && !sb.isEmpty())
				list.add(sb);
		return list;
	}
}
