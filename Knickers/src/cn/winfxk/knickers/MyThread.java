package cn.winfxk.knickers;

import cn.nukkit.Player;
import cn.nukkit.Server;

/**
 * 监控线程
 * 
 * @Createdate 2020/06/02 22:12:56
 * @author Winfxk
 */
public class MyThread extends Thread {
	private Activate ac;
	private transient int MonitorTime;
	private transient boolean isWhile = true;
	private Message msg;
	private Server server;

	public MyThread(Activate ac) {
		this.ac = ac;
		MonitorTime = ac.getConfig().getInt("MonitorTime");
		msg = ac.getMessage();
		server = Server.getInstance();
	}

	/**
	 * 设置是否循环
	 * 
	 * @param isWhile
	 */
	public void setWhile(boolean isWhile) {
		this.isWhile = isWhile;
	}

	@Override
	public void run() {
		while (isWhile)
			try {
				sleep(1000);
				if (MonitorTime-- > 0 || ac.getConfig().getString("Tool") == null
						|| !ac.getConfig().getBoolean("ForceTool"))
					continue;
				MonitorTime = ac.getConfig().getInt("MonitorTime");
				for (Player player : server.getOnlinePlayers().values())
					if (!ac.isToolItem(player)) {
						player.sendMessage(msg.getMessage("GiveTool", player));
						player.getInventory().addItem(ac.getToolItem());
					}
			} catch (InterruptedException e) {
				e.printStackTrace();
				ac.getPluginBase().getLogger().warning(msg.getMessage("MonitorThreadShutdown"));
				break;
			}
		ac.setThread(null);
	}

	/**
	 * 设置监听线程间隔
	 * 
	 * @param monitorTime
	 */
	public void setMonitorTime(int monitorTime) {
		MonitorTime = monitorTime;
	}
}
