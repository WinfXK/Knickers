package cn.winfxk.knickers.module.ptp;

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
 * 创建点击后传送到玩家附近的按钮时显示的界面
 * 
 * @Createdate 2020/05/31 19:18:38
 * @author Winfxk
 */
public class MakePtP extends MakeBase {

	public MakePtP(Player player, FormBase upForm, Config config, FunctionBase base) {
		super(player, upForm, config, base);
	}

	@Override
	public boolean MakeMain() {
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getButtonText(), "", getButtonText());
		form.addInput(getString("SolicitedTitle"), "", getString("SolicitedTitle"));
		form.addInput(getString("SolicitedMessage"), getString("SolicitedText"), getString("SolicitedMessage"));
		form.addInput(getString("Accept"), "", getString("Accept"));
		form.addInput(getString("Refuse"), "", getString("Refuse"));
		form.addToggle(getString("ForceTransfer"), false);
		form.addInput(getClickCommand(), "", getClickCommand());
		form.addDropdown(getPlayerBlacklistMode(), getModeList());
		form.addInput(getInputBlacklistPlayer(), "", getInputBlacklistPlayer());
		form.addDropdown(getWorldBlacklistMode(), getModeList());
		form.addInput(getInputBlacklistWorld(), "", getInputBlacklistWorld());
		form.addInput(getMoney(), "", getMoney());
		form.addDropdown(getMoneyEconomy(), ac.getEconomyManage().getEconomy());
		form.addDropdown(getPermission(), getPermissions());
		form.addInput(getString("ListTitle"), PtPlayerButton.PtPlayerKey, getString("ListTitle"));
		form.addInput(getString("ListContent"), getString("ListContentText"), getString("ListContent"));
		form.addInput(getString("InputPlayerItem"), getString("PlayerItem"), getString("InputPlayerItem"));
		form.addInput(getInputPath(), "", getInputPath());
		form.addDropdown(getSelectPathType(), getPathType());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse sb) {
		FormResponseCustom data = getCustom(sb);
		String ButtonText = data.getInputResponse(1);
		if (ButtonText == null || ButtonText.isEmpty()) {
			sendMessage(getNotInputButtonText());
			return MakeMain();
		}
		boolean ForceTransfer = data.getToggleResponse(6);
		String SolicitedTitle = data.getInputResponse(2);
		String SolicitedMessage = data.getInputResponse(3);
		String Accept = data.getInputResponse(4);
		String Refuse = data.getInputResponse(5);
		List<String> Command = getCommand(data.getInputResponse(7));
		String PlayerBlacklistMode = getFiltertype(data.getDropdownResponse(8).getElementID());
		List<String> PlayerBlacklist = getList(data.getInputResponse(9));
		String WorldBlacklistMode = getFiltertype(data.getDropdownResponse(10).getElementID());
		List<String> WorldBlacklist = getList(data.getInputResponse(11));
		String MoneyString = data.getInputResponse(12);
		if (MoneyString != null && !MoneyString.isEmpty() && !Tool.isInteger(MoneyString)) {
			sendMessage(getMoneyillegal());
			return MakeMain();
		}
		double Money = Tool.objToDouble(MoneyString);
		Money = Money <= 0 ? 0 : Money;
		MyEconomy economy = ac.getEconomyManage().getEconomy(data.getDropdownResponse(13).getElementContent());
		String Permission = getPermissionsType(data.getDropdownResponse(14).getElementID());
		String ListTitle = data.getInputResponse(15);
		String ListContent = data.getInputResponse(16);
		String PlayerItem = data.getInputResponse(17);
		String Path = data.getInputResponse(18);
		String PathType = getPathType(data.getDropdownResponse(19).getElementID());
		if (!PathType.equals(MakeBase.NotPath)) {
			player.sendMessage(getNotInputPath());
			return MakeMain();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("SolicitedTitle", SolicitedTitle);
		map.put("SolicitedMessage", SolicitedMessage);
		map.put("Accept", Accept);
		map.put("Refuse", Refuse);
		map.put("ForceTransfer", ForceTransfer);
		map.put("ListTitle", ListTitle);
		map.put("ListContent", ListContent);
		map.put("PlayerItem", PlayerItem);
		player.sendMessage(getString(Key == null ? "CreateOK" : "AlterOK"));
		boolean isOK = save(map, ButtonText, Command, PlayerBlacklistMode, PlayerBlacklist, WorldBlacklistMode,
				WorldBlacklist, Money, economy, Permission, Key, Path, PathType);
		return isOK && isBack();
	}
}
