package cn.winfxk.knickers.module.cmd;

import java.io.File;

import cn.nukkit.Player;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.form.ButtonBase;
import cn.winfxk.knickers.module.FunctionBase;

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
	public boolean makeButton(Player player, File file) {
		return false;
	}

	@Override
	public String getButtonString(ButtonBase form, String Key) {
		return null;
	}

	@Override
	public boolean ClickButton(ButtonBase form, String Key) {
		return false;
	}

	@Override
	public boolean delButton(ButtonBase form, String Key) {
		return false;
	}
}
