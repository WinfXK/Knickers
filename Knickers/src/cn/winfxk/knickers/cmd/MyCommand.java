package cn.winfxk.knickers.cmd;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.winfxk.knickers.Knickers;
import cn.winfxk.knickers.rec.Message;
import cn.winfxk.knickers.rec.MyPlayer;
import cn.winfxk.knickers.tool.Config;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/08/27 21:54:44
 * @author Winfxk
 */
public abstract class MyCommand extends Command implements FilenameFilter {
	protected static Knickers kis = Knickers.kis;
	protected static Message msg = kis.message;
	protected String CommandKey;
	protected static File file = new File(kis.getDataFolder(), Knickers.Command);
	protected static Config commandConfig = new Config(file);
	protected static final String CommandMsgKey = "Command";

	public MyCommand(String Key) {
		super(kis.getName().toLowerCase(), msg.getSon(Key, "Description"), "/" + kis.getName().toLowerCase() + " help", getCommandKey(Key));
	}

	public static String[] getCommandKey(String Key) {
		Object obj = commandConfig.get(Key);
		List<Object> list = obj != null && obj instanceof List ? (ArrayList<Object>) obj : new ArrayList<>();
		List<String> l = new ArrayList<>();
		for (Object string : list)
			if (string != null && !Tool.objToString(string, "").isEmpty())
				l.add(Tool.objToString(string));
		return l.toArray(new String[] {});
	}

	protected String getNotPlayer() {
		return msg.getSon(CommandMsgKey, "NotPlayer");
	}

	protected String getString(String Key) {
		return msg.getSun(CommandMsgKey, CommandKey, Key);
	}

	protected String getString(String Key, Player player) {
		return msg.getSun(CommandMsgKey, CommandKey, Key, player);
	}

	protected String getString(String Key, String[] Keys, Object[] Data) {
		return msg.getSun(CommandMsgKey, CommandKey, Key, Keys, Data);
	}

	protected String getString(String Key, String player) {
		return msg.getSun(CommandMsgKey, CommandKey, Key, new String[] { "{Player}", "{Money}" }, new Object[] { player, MyPlayer.getMoney(player) });
	}

	@Override
	public boolean accept(File dir, String file) {
		return new File(dir, file).isFile();
	}
}
