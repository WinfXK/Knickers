package cn.winfxk.knickers;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.winfxk.knickers.rec.MyPlayer;
import cn.winfxk.knickers.tool.Config;

/**
 * @Createdate 2021/08/11 19:58:20
 * @author Winfxk
 */
public class Period extends Thread {
	private Knickers kis;
	public static transient int EternalToolTime;
	public static transient boolean EternalTool;
	private static Config config = Knickers.kis.config;
	public int AlterEternalToolTime = 0;
	private Server server = Server.getInstance();

	public Period(Knickers kis) {
		this.kis = kis;
		EternalToolTime = config.getInt("EternalToolTime");
		EternalTool = config.getBoolean("EternalTool");
	}

	@Override
	public void run() {
		while (true) {
			if (EternalTool && AlterEternalToolTime++ >= EternalToolTime) {
				AlterEternalToolTime = 0;
				for (Player p : server.getOnlinePlayers().values()) {
					if (!kis.MyPlayers.containsKey(p.getName()))
						kis.MyPlayers.put(p.getName(), new MyPlayer(p));
					if (!kis.isFastTool(p)) {
						p.getInventory().addItem(kis.getFastTool());
						p.sendMessage(kis.message.getMessage("给工具", p));
					}
				}
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
