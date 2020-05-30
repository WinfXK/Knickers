package cn.winfxk.knickers.module.say;

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
 * 创建点击说话的按钮
 * 
 * @Createdate 2020/05/30 20:02:51
 * @author Winfxk
 */
public class MakeSay extends MakeBase {
	protected String Key;

	public MakeSay(Player player, FormBase upForm, Config config, FunctionBase base) {
		super(player, upForm, config, base);
	}

	@Override
	public boolean MakeMain() {
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getButtonText(), "", getButtonText());
		form.addInput(getString("InputSayString"), "", getString("InputSayString"));
		form.addInput(getClickCommand(), "", getClickCommand());
		form.addDropdown(getPlayerBlacklistMode(), getModeList());
		form.addInput(getInputBlacklistPlayer(), "", getInputBlacklistPlayer());
		form.addDropdown(getWorldBlacklistMode(), getModeList());
		form.addInput(getInputBlacklistWorld(), "", getInputBlacklistWorld());
		form.addInput(getMoney(), "", getMoney());
		form.addDropdown(getMoneyEconomy(), ac.getEconomyManage().getEconomy());
		form.addDropdown(getPermission(), getPermissions());
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
		String SayText = data.getInputResponse(2);
		if (SayText == null || SayText.isEmpty()) {
			sendMessage(getString("InputSayString"));
			return MakeMain();
		}
		List<String> Command = getCommand(data.getInputResponse(3));
		String PlayerBlacklistMode = getFiltertype(data.getDropdownResponse(4).getElementID());
		List<String> PlayerBlacklist = getList(data.getInputResponse(5));
		String WorldBlacklistMode = getFiltertype(data.getDropdownResponse(6).getElementID());
		List<String> WorldBlacklist = getList(data.getInputResponse(7));
		String MoneyString = data.getInputResponse(8);
		if (MoneyString != null && !MoneyString.isEmpty() && !Tool.isInteger(MoneyString)) {
			sendMessage(getMoneyillegal());
			return MakeMain();
		}
		double Money = Tool.objToDouble(MoneyString);
		Money = Money <= 0 ? 0 : Money;
		MyEconomy economy = ac.getEconomyManage().getEconomy(data.getDropdownResponse(9).getElementContent());
		String Permission = getPermissionsType(data.getDropdownResponse(10).getElementID());
		Map<String, Object> map = new HashMap<>();
		map.put("Say", SayText);
		player.sendMessage(getString(Key == null ? "CreateOK" : "AlterOK"));
		boolean isOK = save(map, ButtonText, Command, PlayerBlacklistMode, PlayerBlacklist, WorldBlacklistMode,
				WorldBlacklist, Money, economy, Permission, Key);
		return isOK && isBack();
	}
}
