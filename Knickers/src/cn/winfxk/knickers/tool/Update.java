package cn.winfxk.knickers.tool;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.winfxk.knickers.Activate;

/**
 * @author Winfxk
 */
public class Update {
	private PluginBase mis;
	protected static final String Url = "http://pluginsupdate.epicfx.cn";
	protected static final int V = 6;
	protected static final String ConfigName = "/Update.yml";
	private Activate activate;

	public Update(PluginBase mis) {
		this.mis = mis;
		activate = Activate.getActivate();
	}

	public void start() {
		try {
			mis.getLogger().info(activate.getMessage().getSon("Update", "Start"));
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
					Map<String, String> map = gson.fromJson(jsonArray, HashMap.class);
					if (Tool.ObjToBool(map.get("Update"), false)) {
						mis.getLogger()
								.info(activate.getMessage().getSon("Update", "Update",
										new String[] { "{Msg}", "{ConfigName}", "{Versions}" },
										new Object[] { map.get("Msg"), ConfigName, map.get("V") }));
						LinkedHashMap<String, Object> map2 = new LinkedHashMap<>();
						map2.put("下载地址", map.get("Http"));
						map2.put("更新内容", map.get("Msg"));
						map2.put("更新版本", map.get("V"));
						map2.put("当前版本", V);
						Config config = new Config(mis.getDataFolder() + ConfigName, Config.YAML);
						config.setAll(map2);
						config.save();
						return;
					}
					mis.getLogger().info(activate.getMessage().getSon("Update", "NotUpdate"));
					return;
				}
				mis.getLogger().info(activate.getMessage().getSon("Update", "UpdateError"));
				return;
			}
			mis.getLogger().info(activate.getMessage().getSon("Update", "ErrorConnect"));
		} catch (Exception e) {
			mis.getLogger().info(activate.getMessage().getSon("Update", "UpdateThreadError", new String[] { "{Error}" },
					new Object[] { e.getMessage() }));
		}
	}
}
