package cn.winfxk.knickers;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

/**
 * @Createdate 2020/05/22 19:03:12
 * @author Winfxk
 */
public class MainCommand extends Command {
	private Activate ac;

	public MainCommand(Activate ac) {
		super(ac.getPluginBase().getName().toLowerCase(),
				Activate.getActivate().message.getSon("MainCommand", "Description"),
				"/" + ac.getPluginBase().getName().toLowerCase() + " help", ac.getCommand("MainCommand"));
		this.ac = ac;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		return false;
	}
}
