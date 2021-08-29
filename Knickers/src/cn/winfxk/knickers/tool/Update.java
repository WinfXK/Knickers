package cn.winfxk.knickers.tool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginLogger;
import cn.winfxk.knickers.rec.Message;

/**
 * @Createdate 2021/08/29 11:19:23
 * @author Winfxk
 */
public class Update extends Thread {
	private File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
	private static final String[] Key = { "{Name}", "{newName}", "{Version}", "{newVersion}", "{Size}", "{newSize}", "{Download}", "{UpdateMessage}" };
	private static final String MsgKey = "Update";
	private static final String Host = "NukkitPlugin.Winfxk.cn";
	private static final int Port = 1998;
	/**
	 * 是否检查更新
	 */
	private boolean Chenck;
	/**
	 * 是否循环检查更新
	 */
	private boolean Cycle;
	/**
	 * 有更新时是否自动下载
	 */
	@SuppressWarnings("unused")
	private boolean AutoDownload;
	/**
	 * 是否检查过更新
	 */
	private boolean Employ = false;
	/**
	 * 插件主类
	 */
	private PluginBase plugin;
	/**
	 * 循环检查更新的间隔
	 */
	public transient static int CycleTime;
	/**
	 * 剩余的检查间隔
	 */
	private transient int Tik = 0;
	private Message msg;
	private PluginLogger log;

	/**
	 * 插件更新检查类
	 * 
	 * @param Chenck       是否检查更新
	 * @param Cycle        是否循环检查更新
	 * @param CycleTime    循环检查更新的间隔
	 * @param AutoDownload 有更新时是否自动下载
	 * @param plugin       插件主类
	 */
	public Update(boolean Chenck, boolean Cycle, int CycleTime, boolean AutoDownload, PluginBase plugin, Message msg) {
		this.Chenck = Chenck;
		this.Cycle = Cycle;
		Update.CycleTime = CycleTime;
		this.AutoDownload = AutoDownload;
		this.plugin = plugin;
		this.msg = msg;
		log = plugin.getLogger();
	}

	/**
	 * 检查更新
	 */
	private void Conduct() {
		log.info(msg.getSon(MsgKey, "Updateing"));
		Map<String, Object> map = new HashMap<>();
		map.put("Name", plugin.getName());
		map.put("Version", plugin.getDescription().getVersion());
		try {
			Map<String, Object> data = get(map);
			if (data == null) {
				log.error(msg.getSon(MsgKey, "LinkError"));
				return;
			}
			if (data.size() < 1) {
				log.warning(msg.getSon(MsgKey, "NotMessage"));
				return;
			}
			if (Tool.ObjToBool(data.get("Update"))) {
				log.info(msg.getSon(MsgKey, "Update", Key, new Object[] { plugin.getName(), data.get("Name"), plugin.getDescription().getVersion(), data.get("Version"), Tool.getSize(file),
						Tool.getSize(Tool.objToLong(data.get("Size"))), data.get("Download"), data.get("UpdateMessage") }));
				return;
			}
			log.info(msg.getSon(MsgKey, "NotUpdate"));
			return;
		} catch (Exception e) {
			log.error(msg.getSon(MsgKey, "UpdateError", "{Error}", e.toString()));
			return;
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
			}
			if (!Chenck)
				continue;
			if (!Cycle && Employ)
				continue;
			if (Tik-- <= 0) {
				Tik = CycleTime;
				Employ = true;
				Conduct();
			}
		}
	}

	/**
	 * 请求一个数据
	 * 
	 * @param map 数据内容
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> get(Map<String, Object> map) throws Exception {
		Socket socket = new Socket(Host, Port);
		DataInputStream Input = new DataInputStream(socket.getInputStream());
		DataOutputStream Output = new DataOutputStream(socket.getOutputStream());
		Output.writeUTF(Config.yaml.dump(map));
		String string = Input.readUTF();
		try {
			Input.close();
		} catch (Exception e) {
		}
		try {
			Output.close();
		} catch (Exception e) {
		}
		try {
			socket.close();
		} catch (Exception e) {
		}
		return Config.yaml.loadAs(string, Map.class);
	}
}
