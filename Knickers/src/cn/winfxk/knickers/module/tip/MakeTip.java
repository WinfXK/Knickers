package cn.winfxk.knickers.module.tip;

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
 * 创建Tip按钮时显示的内容
 * 
 * @Createdate 2020/05/31 23:11:25
 * @author Winfxk
 */
public class MakeTip extends MakeBase {

	public MakeTip(Player player, FormBase upForm, Config config, FunctionBase base) {
		super(player, upForm, config, base);
	}

	@Override
	public boolean MakeMain() {
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getButtonText(), "", getButtonText());
		form.addInput(getString("InputTitle"), "", getString("InputTitle"));
		form.addInput(getString("InputContent"), "", getString("InputContent"));
		form.addInput(getString("InputButton1"), "", getString("InputButton1"));
		form.addInput(getString("InputButton2"), "", getString("InputButton2"));
		form.addStepSlider(getString("SelectTip"), TipButton.TipType, 1);
		form.addInput(getClickCommand(), "", getClickCommand());
		form.addDropdown(getPlayerBlacklistMode(), getModeList());
		form.addInput(getInputBlacklistPlayer(), "", getInputBlacklistPlayer());
		form.addDropdown(getWorldBlacklistMode(), getModeList());
		form.addInput(getInputBlacklistWorld(), "", getInputBlacklistWorld());
		form.addInput(getMoney(), "", getMoney());
		form.addDropdown(getMoneyEconomy(), ac.getEconomyManage().getEconomy());
		form.addDropdown(getPermission(), getPermissions());
		form.addInput(getString("Button1Command"), "", getString("Button1Command"));
		form.addInput(getString("Button2Command"), "", getString("Button2Command"));
		form.addInput(getInputPath(), "", getInputPath());
		form.addDropdown(getSelectPathType(), getPathType());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		FormResponseCustom d = getCustom(data);
		String ButtonText = d.getInputResponse(1);
		if (ButtonText == null || ButtonText.isEmpty()) {
			sendMessage(getNotInputButtonText());
			return MakeMain();
		}
		String Title = d.getInputResponse(2);
		String Content = d.getInputResponse(3);
		String Button1 = d.getInputResponse(4);
		String Button2 = d.getInputResponse(5);
		String TipType = TipButton.TypeKey.get(d.getStepSliderResponse(6).getElementID());
		List<String> Command = getCommand(d.getInputResponse(7));
		String PlayerBlacklistMode = getFiltertype(d.getDropdownResponse(8).getElementID());
		List<String> PlayerBlacklist = getList(d.getInputResponse(9));
		String WorldBlacklistMode = getFiltertype(d.getDropdownResponse(10).getElementID());
		List<String> WorldBlacklist = getList(d.getInputResponse(11));
		String MoneyString = d.getInputResponse(12);
		if (MoneyString != null && !MoneyString.isEmpty() && !Tool.isInteger(MoneyString)) {
			sendMessage(getMoneyillegal());
			return MakeMain();
		}
		double Money = Tool.objToDouble(MoneyString);
		Money = Money <= 0 ? 0 : Money;
		MyEconomy economy = ac.getEconomyManage().getEconomy(d.getDropdownResponse(13).getElementContent());
		String Permission = getPermissionsType(d.getDropdownResponse(14).getElementID());
		List<String> Command1 = getCommand(d.getInputResponse(15));
		List<String> Command2 = getCommand(d.getInputResponse(16));
		String Path = d.getInputResponse(17);
		String PathType = getPathType(d.getDropdownResponse(18).getElementID());
		if (!PathType.equals(MakeBase.NotPath)) {
			player.sendMessage(getNotInputPath());
			return MakeMain();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("Title", Title);
		map.put("Content", Content);
		map.put("Button1", Button1);
		map.put("Button2", Button2);
		map.put("TipType", TipType);
		map.put("Command1", Command1);
		map.put("Command2", Command2);
		player.sendMessage(getString(Key == null ? "CreateOK" : "AlterOK"));
		boolean isOK = save(map, ButtonText, Command, PlayerBlacklistMode, PlayerBlacklist, WorldBlacklistMode,
				WorldBlacklist, Money, economy, Permission, Key, Path, PathType);
		return isOK && isBack();
	}
}
