package cn.winfxk.knickers.module.ptp;

import java.util.List;

import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeBase;
import cn.winfxk.knickers.tool.CustomForm;

/**
 * 修改点击后传送到玩家附近按钮的数据时显示的界面
 * 
 * @Createdate 2020/05/31 20:30:55
 * @author Winfxk
 */
public class AlterPtP extends MakePtP {
	private PtPData data;

	public AlterPtP(FormBase upForm, PtPData data) {
		super(upForm.getPlayer(), upForm, data.getConfig(), data.getFunctionBase());
		Key = data.getKey();
		this.data = data;
	}

	@Override
	public boolean MakeMain() {
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getButtonText(), data.getButtonText(), getButtonText());
		form.addInput(getString("SolicitedTitle"), data.getSolicitedTitle(), getString("SolicitedTitle"));
		form.addInput(getString("SolicitedMessage"), data.getSolicitedMessage(), getString("SolicitedMessage"));
		form.addInput(getString("Accept"), data.getAccept(), getString("Accept"));
		form.addInput(getString("Refuse"), data.getRefuse(), getString("Refuse"));
		form.addToggle(getString("ForceTransfer"), data.isForceTransfer());
		form.addInput(getClickCommand(), getClickCommandString(), getClickCommand());
		form.addDropdown(getPlayerBlacklistMode(), getModeList(), getFiltertype(data.getPlayerfilter()));
		form.addInput(getInputBlacklistPlayer(), listtoString(data.getPlayers()), getInputBlacklistPlayer());
		form.addDropdown(getWorldBlacklistMode(), getModeList(), getFiltertype(data.getWorldfilter()));
		form.addInput(getInputBlacklistWorld(), listtoString(data.getWorlds()), getInputBlacklistWorld());
		form.addInput(getMoney(), data.getMoney(), getMoney());
		form.addDropdown(getMoneyEconomy(), ac.getEconomyManage().getEconomy(),
				ac.getEconomyManage().getEconomy().indexOf(data.getEconomy().getEconomyName()));
		form.addDropdown(getPermission(), getPermissions(), getPermissionsType(data.getPermission()));
		form.addInput(getString("ListTitle"), data.getListTitle(), getString("ListTitle"));
		form.addInput(getString("ListContent"), data.getListContent(), getString("ListContent"));
		form.addInput(getString("InputPlayerItem"), data.getPlayerItem(), getString("InputPlayerItem"));
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
