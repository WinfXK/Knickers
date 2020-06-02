package cn.winfxk.knickers.module.cmd;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.MyPlayer;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;
import cn.winfxk.knickers.money.MyEconomy;

/**
 * 命令按钮-点击后执行一个命令
 * 
 * @Createdate 2020/05/22 23:41:48
 * @author Winfxk
 */
public class CommandButton extends FunctionBase {
	public static final String CommandKey = "Command";
	public static final String CommandVarSP = "{msg}";
	protected static final String HintSP = ";";

	public CommandButton(Activate ac) {
		super(ac, CommandKey);
	}

	@Override
	protected FormBase getMakeForm(Player player, Config config, FormBase base) {
		return new MakeCommand(player, base, config, this);
	}

	@Override
	public boolean Click(FormBase form, ModuleData data) {
		Player player = form.getPlayer();
		MyPlayer myPlayer = form.getMyPlayer();
		if (!data.isPermission(player)) {
			player.sendMessage(
					msg.getSon("Function", "notPermission", new String[] { "{Player}", "{Money}", "{Permission}" },
							new Object[] { player.getName(), myPlayer.getMoney(), data.getPermission() }));
			return false;
		}
		if (!data.isPlayerfilter(player)) {
			player.sendMessage(msg.getSon("Function", "BlacklistByPlayer", form));
			return false;
		}
		if (!data.isWorldfilter(player.getLevel())) {
			player.sendMessage(msg.getSon("Function", "BlacklistByWorld", form));
			return false;
		}
		if (data.getFunctionBase() == null || !data.getFunctionBase().isEnable()) {
			player.sendMessage(msg.getSon("Function", "Buttonoff", form));
			return false;
		}
		MyEconomy economy = data.getEconomy();
		if (economy != null && economy.isEnabled() && data.getMoney() > 0)
			if (!economy.allowArrears() && economy.getMoney(player) < data.getMoney()) {
				player.sendMessage(
						msg.getSon("Function", "notMoney", new String[] { "{Player}", "{Money}", "{MoneyN}" },
								new Object[] { player.getName(), economy.getMoney(player), economy.getMoneyName() }));
				return false;
			} else
				economy.reduceMoney(player, data.getMoney());
		return ClickButton(form, data);
	}

	@Override
	public CommandData getModuleData(Config config, String Key) {
		return new CommandData(config, Key);
	}

	@Override
	public boolean ClickButton(FormBase form, ModuleData sb) {
		CommandData data = CommandData.getCommandData(sb);
		if (!data.getClickCommand().equals(CommandVarSP))
			return Server.getInstance().dispatchCommand(form.getPlayer(),
					Activate.getActivate().getMessage().getText(data.getClickCommand(), form));
		return form.getMyPlayer().showForm(new UseVariable(form.getPlayer(), form, data));
	}

	@Override
	protected FormBase getAlterForm(FormBase form, ModuleData data) {
		return new AlterCommand(form, CommandData.getCommandData(data));
	}
}
