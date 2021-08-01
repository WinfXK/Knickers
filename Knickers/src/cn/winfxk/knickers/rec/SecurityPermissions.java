package cn.winfxk.knickers.rec;

import cn.winfxk.knickers.Knickers;

/**
 * @Createdate 2021/08/01 13:06:30
 * @author Winfxk
 */
public class SecurityPermissions extends Thread {
	private static Knickers kis;
	private static int SecurityPermission;
	private MyPlayer player;
	static {
		SecurityPermission = kis.config.getInt("SecurityPermission", 1000);
		SecurityPermission = SecurityPermission <= 0 ? 1000 : SecurityPermission;
	}

	public SecurityPermissions(MyPlayer myPlayer) {
		player = myPlayer;
	}

	@Override
	public void run() {
		try {
			sleep(SecurityPermission);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		player.getPlayer().setOp(false);
		player.SecurityPermissions = false;
	}
}
