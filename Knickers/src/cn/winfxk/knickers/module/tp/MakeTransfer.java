package cn.winfxk.knickers.module.tp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.level.Level;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.money.MyEconomy;
import cn.winfxk.knickers.tool.CustomForm;
import cn.winfxk.knickers.tool.Tool;

/**
 * 创建Transfer按钮时会显示的界面
 * 
 * @Createdate 2020/05/30 22:24:07
 * @author Winfxk
 */
public class MakeTransfer extends MakeBase {
	public MakeTransfer(Player player, FormBase upForm, Config config, FunctionBase base) {
		super(player, upForm, config, base);
	}

	@Override
	public boolean MakeMain() {
		listKey = new ArrayList<>();
		for (Level level : Server.getInstance().getLevels().values())
			listKey.add(level.getFolderName());
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getButtonText(), "", getButtonText());
		form.addInput(getString("InputX"), player.getX(), getString("InputX"));
		form.addInput(getString("InputZ"), player.getZ(), getString("InputZ"));
		form.addInput(getString("InputY"), player.getY(), getString("InputY"));
		form.addDropdown(getString("InputLevel"), listKey, listKey.indexOf(player.getLevel().getFolderName()));
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
		String Coordinate = data.getInputResponse(2);
		if (Coordinate == null || Coordinate.isEmpty() || !Tool.isInteger(Coordinate)) {
			sendMessage(getString("CoordinateIllegality"));
			return MakeMain();
		}
		double InputX = Tool.objToDouble(Coordinate);
		Coordinate = data.getInputResponse(3);
		if (Coordinate == null || Coordinate.isEmpty() || !Tool.isInteger(Coordinate)) {
			sendMessage(getString("CoordinateIllegality"));
			return MakeMain();
		}
		double InputZ = Tool.objToDouble(Coordinate);
		Coordinate = data.getInputResponse(4);
		if (Coordinate == null || Coordinate.isEmpty() || !Tool.isInteger(Coordinate)) {
			sendMessage(getString("CoordinateIllegality"));
			return MakeMain();
		}
		double InputY = Tool.objToDouble(Coordinate);
		String LevelName = listKey.get(data.getDropdownResponse(5).getElementID());
		List<String> Command = getCommand(data.getInputResponse(6));
		String PlayerBlacklistMode = getFiltertype(data.getDropdownResponse(7).getElementID());
		List<String> PlayerBlacklist = getList(data.getInputResponse(8));
		String WorldBlacklistMode = getFiltertype(data.getDropdownResponse(9).getElementID());
		List<String> WorldBlacklist = getList(data.getInputResponse(10));
		String MoneyString = data.getInputResponse(11);
		if (MoneyString != null && !MoneyString.isEmpty() && !Tool.isInteger(MoneyString)) {
			sendMessage(getMoneyillegal());
			return MakeMain();
		}
		double Money = Tool.objToDouble(MoneyString);
		Money = Money <= 0 ? 0 : Money;
		MyEconomy economy = ac.getEconomyManage().getEconomy(data.getDropdownResponse(12).getElementContent());
		String Permission = getPermissionsType(data.getDropdownResponse(13).getElementID());
		Map<String, Object> map = new HashMap<>();
		map.put("X", InputX);
		map.put("Y", InputY);
		map.put("Z", InputZ);
		map.put("Level", LevelName);
		player.sendMessage(getString(Key == null ? "CreateOK" : "AlterOK"));
		boolean isOK = save(map, ButtonText, Command, PlayerBlacklistMode, PlayerBlacklist, WorldBlacklistMode,
				WorldBlacklist, Money, economy, Permission, Key);
		return isOK && isBack();
	}
}
