package cn.winfxk.knickers.module.cmd;

import java.util.List;

import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeBase;
import cn.winfxk.knickers.tool.CustomForm;

/**
 * 修改命令按钮时显示原来按钮数据的界面
 * 
 * @Createdate 2020/06/02 20:34:32
 * @author Winfxk
 */
public class AlterCommand extends MakeCommand {
	private CommandData data;

	public AlterCommand(FormBase upForm, CommandData data) {
		super(upForm.getPlayer(), upForm, data.getConfig(), data.getFunctionBase());
		this.data = data;
		Key = data.getKey();
	}

	@Override
	public boolean MakeMain() {
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getButtonText(), data.getButtonText(), getButtonText());
		form.addInput(getString("ClickCommand"), data.getClickCommand(), getString("ClickCommand"));
		form.addInput(getString("InputTip"), getTip(data.getTip()), getString("InputTip"));
		form.addInput(getString("InputHint"), getTip(data.getHint()), getString("InputHint"));
		form.addDropdown(getPlayerBlacklistMode(), getModeList(), getFiltertype(data.getPlayerfilter()));
		form.addInput(getInputBlacklistPlayer(), listtoString(data.getPlayers()), getInputBlacklistPlayer());
		form.addDropdown(getWorldBlacklistMode(), getModeList(), getFiltertype(data.getWorldfilter()));
		form.addInput(getInputBlacklistWorld(), listtoString(data.getWorlds()), getInputBlacklistWorld());
		form.addInput(getMoney(), data.getMoney(), getMoney());
		form.addDropdown(getMoneyEconomy(), ac.getEconomyManage().getEconomy(),
				ac.getEconomyManage().getEconomy().indexOf(data.getEconomy().getEconomyName()));
		form.addDropdown(getPermission(), getPermissions(), getPermissionsType(data.getPermission()));
		form.addInput(getString("VariableTitle"), "", getString("VariableTitle"));
		form.addInput(getString("VariableContent"), "", getString("VariableContent"));
		form.addToggle(getString("AllowBlank"), false);
		form.addInput(getInputPath(), data.getPath() == null ? "" : data.getPath(), getInputPath());
		form.addDropdown(getSelectPathType(), getPathType(), getPathType(data.getPathType()));
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 将List的Hint数组转换为String
	 * 
	 * @param list
	 * @return
	 */
	private String getTip(List<String> list) {
		String string = "";
		for (String c : list)
			if (c != null && !c.isEmpty())
				string += string.isEmpty() ? "" : CommandButton.HintSP + c;
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
