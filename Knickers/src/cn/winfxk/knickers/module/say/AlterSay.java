package cn.winfxk.knickers.module.say;

import java.util.List;

import cn.nukkit.Player;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeBase;
import cn.winfxk.knickers.tool.CustomForm;

/**
 * 修改Say按钮数据时会创建的界面F
 * 
 * @Createdate 2020/05/30 21:46:49
 * @author Winfxk
 */
public class AlterSay extends MakeSay {
	private SayData data;

	/**
	 * 修改Say按钮时调用
	 * 
	 * @param player 修改界面的玩家对象
	 * @param upForm 上个界面
	 * @param data   按钮的数据对象
	 * @param button 按钮所述的类型
	 */
	public AlterSay(Player player, FormBase upForm, SayData data, SayButton button) {
		super(player, upForm, data.getConfig(), button);
		this.Key = data.getKey();
		this.data = data;
	}

	@Override
	public boolean MakeMain() {
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getButtonText(), data.getButtonText(), getButtonText());
		form.addInput(getString("InputSayString"), data.getSay(), getString("InputSayString"));
		form.addInput(getClickCommand(), getClickCommandString(), getClickCommand());
		form.addDropdown(getPlayerBlacklistMode(), getModeList(), getFiltertype(data.getPlayerfilter()));
		form.addInput(getInputBlacklistPlayer(), listtoString(data.getPlayers()), getInputBlacklistPlayer());
		form.addDropdown(getWorldBlacklistMode(), getModeList(), getFiltertype(data.getWorldfilter()));
		form.addInput(getInputBlacklistWorld(), listtoString(data.getWorlds()), getInputBlacklistWorld());
		form.addInput(getMoney(), data.getMoney(), getMoney());
		form.addDropdown(getMoneyEconomy(), ac.getEconomyManage().getEconomy(),
				ac.getEconomyManage().getEconomy().indexOf(data.getEconomy().getEconomyName()));
		form.addDropdown(getPermission(), getPermissions(), getPermissionsType(data.getPermission()));
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
