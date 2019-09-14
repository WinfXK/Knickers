package xiaokai.knickers.tool;

import java.io.File;
import java.util.LinkedHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public class Update {
	private PluginBase mis;
	protected static final String Url = "http://pluginsupdate.epicfx.cn";
	// protected static final String Url = "http://192.168.0.102";
	protected static final int V = 2;
	protected static final String ConfigName = "/Update.yml";

	public Update(PluginBase mis) {
		this.mis = mis;
	}

	public void start() {
		try {
			mis.getLogger().info("§6检查更新各种！若不想使用该功能或出现问题，请关闭该功能.");
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			JsonParser parser = new JsonParser();
			String strByJson = Tool.getHttp(Url, "s=isup&n=" + mis.getName() + "&v=" + V);
			if (strByJson != null && !strByJson.isEmpty()) {
				if (strByJson.contains("<text>") && strByJson.contains("</text>")
						&& Tool.cutString(strByJson, "<text>", "</text>") != null
						&& !Tool.cutString(strByJson, "<text>", "</text>").isEmpty()) {
					strByJson = Tool.cutString(strByJson, "<text>", "</text>");
					JsonElement jsonArray = parser.parse(strByJson).getAsJsonObject();
					AreYouSB userBean = gson.fromJson(jsonArray, AreYouSB.class);
					if (Boolean.valueOf(userBean.Update)) {
						mis.getLogger().info("§6已检测到更新！" + userBean.Msg + " §4相关数据以储存到§5" + ConfigName);
						try {
							String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
							File fsFile = new File(path);
							Tool.DownFile(userBean.Http, fsFile.getName(), mis.getServer().getPluginPath());
							mis.getLogger().info("§6新版文件已下载到§4" + fsFile.getName() + "§6，正在尝试重启服务器");
							mis.getServer().reload();
						} catch (Exception e) {
							mis.getLogger().info("§4自动下载新版文件以失败！请尝试手动下载！错误详情：" + e.getMessage());
						}
						LinkedHashMap<String, Object> map = new LinkedHashMap<>();
						map.put("下载地址", userBean.Http);
						map.put("更新内容", userBean.Msg);
						map.put("更新版本", userBean.V);
						map.put("当前版本", V);
						Config config = new Config(mis.getDataFolder() + ConfigName, Config.YAML);
						config.setAll(map);
						config.save();
					} else
						mis.getLogger().info("§6暂无更新！");
				} else
					mis.getLogger().info("§4更新数据错误！");
			} else
				mis.getLogger().info("§4无法连接到更新服务器！");
		} catch (Exception e) {
			mis.getLogger().info("§4更新出现错误！请检查！§f" + e.getMessage());
		}
	}
}
