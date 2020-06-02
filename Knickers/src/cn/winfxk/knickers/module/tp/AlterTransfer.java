package cn.winfxk.knickers.module.tp;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeBase;
import cn.winfxk.knickers.tool.CustomForm;

/**
 * 修改传送按钮的数据时创建的界面
 * 
 * @Createdate 2020/05/31 18:46:26
 * @author Winfxk
 */
public class AlterTransfer extends MakeTransfer {
	private TransferData data;

	/**
	 * 修改传送按钮
	 * 
	 * @param player 修改按钮的玩家对象
	 * @param upForm 上个界面
	 * @param data   按钮的数据
	 * @param button 按钮的类型
	 */
	public AlterTransfer(FormBase upForm, TransferData data) {
		super(upForm.getPlayer(), upForm, data.getConfig(), data.getFunctionBase());
		this.Key = data.getKey();
		this.data = data;
	}

	@Override
	public boolean MakeMain() {
		listKey = new ArrayList<>();
		for (Level level : Server.getInstance().getLevels().values())
			listKey.add(level.getFolderName());
		CustomForm form = new CustomForm(getID(), getTitle());
		form.addLabel(getContent());
		form.addInput(getButtonText(), data.getButtonText(), getButtonText());
		form.addInput(getString("InputX"), data.getX(), getString("InputX"));
		form.addInput(getString("InputZ"), data.getZ(), getString("InputZ"));
		form.addInput(getString("InputY"), data.getY(), getString("InputY"));
		form.addDropdown(getString("InputLevel"), listKey, listKey.indexOf(data.getLevelName()));
		form.addInput(getClickCommand(), getClickCommandString(), getClickCommand());
		form.addDropdown(getPlayerBlacklistMode(), getModeList(), getFiltertype(data.getPlayerfilter()));
		form.addInput(getInputBlacklistPlayer(), listtoString(data.getPlayers()), getInputBlacklistPlayer());
		form.addDropdown(getWorldBlacklistMode(), getModeList(), getFiltertype(data.getWorldfilter()));
		form.addInput(getInputBlacklistWorld(), listtoString(data.getWorlds()), getInputBlacklistWorld());
		form.addInput(getMoney(), data.getMoney(), getMoney());
		form.addDropdown(getMoneyEconomy(), ac.getEconomyManage().getEconomy(),
				ac.getEconomyManage().getEconomy().indexOf(data.getEconomy().getEconomyName()));
		form.addDropdown(getPermission(), getPermissions(), getPermissionsType(data.getPermission()));
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
