package xiaokai.knickers;

import xiaokai.knickers.appliance.EstablishForm;
import xiaokai.knickers.mtp.Kick;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

/**
 * @author Winfxk
 */
public class ToolCommand extends Command {
	private final String Permission = "Knickers.Command.Tool";

	public ToolCommand() {
		super("tool", "§9打开菜单快捷工具主界面", "§b/§6mis §chelp");
		this.commandParameters.clear();
		this.setPermission(Permission);
	}

	@Override
	public boolean execute(CommandSender player, String commandLabel, String[] args) {
		if (!Kick.kick.mis.isEnabled())
			return false;
		if (!player.hasPermission(Permission)) {
			player.sendMessage(Kick.kick.Message.getMessage("权限不足"));
			return true;
		}
		if (!player.isPlayer()) {
			player.sendMessage("§4请在游戏内执行此命令！");
			return true;
		} else
			return EstablishForm.Main((Player) player);
	}

}
