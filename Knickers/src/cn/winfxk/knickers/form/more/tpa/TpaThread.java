package cn.winfxk.knickers.form.more.tpa;

/**
 * @Createdate 2021/08/02 21:47:03
 * @author Winfxk
 */
public class TpaThread extends Thread {
	private ToPlayerTPA tpa;

	public TpaThread(ToPlayerTPA tpa) {
		this.tpa = tpa;
	}

	@Override
	public void run() {
		try {
			sleep(tpa.Countdown * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tpa.byPlayer.teleport(tpa.player);
	}
}
