package cn.winfxk.knickers.module.tip;

import java.util.List;

import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeBase;
import cn.winfxk.knickers.tool.CustomForm;

/**
 * 修改Tip按钮时会显示的界面
 * 
 * @Createdate 2020/06/02 19:26:08
 * @author Winfxk
 */
public class AlterTip extends MakeTip {
	private TipData data;

	public AlterTip(FormBase upForm, TipData data) {
		super(upForm.getPlayer(), upForm, data.getConfig(), data.getFunctionBase());
		Key = data.getKey();
		this.data = data;
	}

	@Override
	public boolean MakeMain() {
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getButtonText(), data.getButtonText(), getButtonText());
		form.addInput(getString("InputTitle"), data.getTitle(), getString("InputTitle"));
		form.addInput(getString("InputContent"), data.getContent(), getString("InputContent"));
		form.addInput(getString("InputButton1"), data.getButton1(), getString("InputButton1"));
		form.addInput(getString("InputButton2"), data.getButton2(), getString("InputButton2"));
		form.addStepSlider(getString("SelectTip"), TipButton.TipType, TipButton.TypeKey.indexOf(data.getTipType()));
		form.addInput(getClickCommand(), getClickCommandString(), getClickCommand());
		form.addDropdown(getPlayerBlacklistMode(), getModeList(), getFiltertype(data.getPlayerfilter()));
		form.addInput(getInputBlacklistPlayer(), listtoString(data.getPlayers()), getInputBlacklistPlayer());
		form.addDropdown(getWorldBlacklistMode(), getModeList(), getFiltertype(data.getWorldfilter()));
		form.addInput(getInputBlacklistWorld(), listtoString(data.getWorlds()), getInputBlacklistWorld());
		form.addInput(getMoney(), data.getMoney(), getMoney());
		form.addDropdown(getMoneyEconomy(), ac.getEconomyManage().getEconomy(),
				ac.getEconomyManage().getEconomy().indexOf(data.getEconomy().getEconomyName()));
		form.addDropdown(getPermission(), getPermissions(), getPermissionsType(data.getPermission()));
		form.addInput(getString("Button1Command"), getClickCommandString(data.getCommand1()),
				getString("Button1Command"));
		form.addInput(getString("Button2Command"), getClickCommandString(data.getCommand2()),
				getString("Button2Command"));
		form.addInput(getInputPath(), data.getPath() == null ? "" : data.getPath(), getInputPath());
		form.addDropdown(getSelectPathType(), getPathType(), getPathType(data.getPathType()));
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 返回已经填写的命令
	 * 
	 * @return
	 */
	private String getClickCommandString(List<String> list) {
		String string = "";
		for (String c : list)
			if (c != null && !c.isEmpty())
				string += string.isEmpty() ? "" : MakeBase.ClickCommandSP + c;
		return string;
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
