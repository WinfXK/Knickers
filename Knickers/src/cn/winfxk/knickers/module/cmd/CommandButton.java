package cn.winfxk.knickers.module.cmd;

import cn.nukkit.Player;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;

/**
 * 命令按钮-点击后执行一个命令
 * 
 * @Createdate 2020/05/22 23:41:48
 * @author Winfxk
 */
public class CommandButton extends FunctionBase {

	public CommandButton(Activate ac) {
		super(ac, "Command");
	}

	@Override
	protected FormBase getMakeForm(Player player, Config config, FormBase base) {
		return null;
	}

	@Override
	public ModuleData getModuleData(Config config, String Key) {
		return null;
	}

	@Override
	public boolean ClickButton(FormBase form, ModuleData data) {
		return false;
	}

	@Override
	protected FormBase getAlterForm(FormBase form, ModuleData data) {
		return null;
	}
}
