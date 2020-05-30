package cn.winfxk.knickers.module.menu;

import java.util.List;

import cn.nukkit.Player;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeBase;
import cn.winfxk.knickers.module.FunctionMag;
import cn.winfxk.knickers.tool.CustomForm;

/**
 * 修改菜单按钮会调用
 * 
 * @Createdate 2020/05/30 17:42:37
 * @author Winfxk
 */
public class AlterMenu extends MakeMenu {
	private MenuData data;

	public AlterMenu(Player player, FormBase upForm, MenuData data) {
		super(player, upForm, data.getConfig(), FunctionMag.getMag().getFunction(MenuButton.MenuKey));
		this.data = data;
		Key = data.getKey();
	}

	@Override
	public boolean MakeMain() {
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getButtonText(), data.getButtonText(), getButtonText());
		form.addInput(getString("MenuContent"), data.getMenuContent(), getString("MenuContent"));
		form.addDropdown(getString("SelectMenu"), listKey, listKey.indexOf(data.getMenufilename()));
		form.addInput(getClickCommand(), getClickCommandString(), getClickCommand());
		form.addDropdown(getPlayerBlacklistMode(), getModeList(), getFiltertype(data.getPlayerfilter()));
		form.addInput(getInputBlacklistPlayer(), listtoString(data.getPlayers()), getInputBlacklistPlayer());
		form.addDropdown(getWorldBlacklistMode(), getModeList(), getFiltertype(data.getWorldfilter()));
		form.addInput(getInputBlacklistWorld(), listtoString(data.getWorlds()), getInputBlacklistWorld());
		form.addInput(getMoney(), data.getMoney(), getMoney());
		form.addDropdown(getMoneyEconomy(), ac.getEconomyManage().getEconomy(),
				ac.getEconomyManage().getEconomy().indexOf(data.getEconomy().getEconomyName()));
		form.addDropdown(getPermission(), getPermissions(), getPermissionsType(data.getPermission()));
		form.addInput(getString("InputTitle"), data.getMenuTitle(), getString("InputTitle"));
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 返回已经填写的命令
	 * 
	 * @return
	 */
	private String getClickCommandString() {
		String string = "";
		for (String c : data.getCommand())
			if (c != null && !c.isEmpty())
				string += string.isEmpty() ? "" : MakeBase.ClickCommandSP + c;
		return string;
	}

	/**
	 * 将过滤列表转换为文本
	 * 
	 * @param list
	 * @return
	 */
	private String listtoString(List<String> list) {
		String string = "";
		for (String c : list)
			if (c != null && !c.isEmpty())
				string += string.isEmpty() ? "" : MakeBase.FilterSP + c;
		return string;
	}
}
