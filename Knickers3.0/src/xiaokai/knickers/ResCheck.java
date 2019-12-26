package xiaokai.knickers;

import xiaokai.knickers.tool.Tool;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.Utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class ResCheck {
	private Activate ac;
	private Knickers kis;

	public ResCheck(Activate activate) {
		this.ac = activate;
		kis = activate.getKnickers();
	}

	public void start() {
		File file = Message.getFile();
		String lang = Tool.getLanguage();
		if (!file.exists()) {
			if (lang != null && getClass().getResource("/language/" + lang + ".yml") != null) {
				try {
					getLogger().info("Writing to the default language:" + lang);
					Utils.writeFile(Message.getFile(),
							Utils.readFile(getClass().getResourceAsStream("/language/" + lang + ".yml")));
				} catch (IOException e) {
					e.printStackTrace();
					getLogger().error("§4The default language could not be initialized！");
					try {
						Utils.writeFile(Message.getFile(),
								Utils.readFile(getClass().getResourceAsStream("/resources/Message.yml")));
					} catch (IOException e1) {
						e1.printStackTrace();
						getLogger().error("§4The default language could not be initialized！");
						kis.setEnabled(false);
						return;
					}
				}
			} else
				try {
					Utils.writeFile(Message.getFile(),
							Utils.readFile(getClass().getResourceAsStream("/resources/Message.yml")));
				} catch (IOException e1) {
					e1.printStackTrace();
					getLogger().error("§4The default language could not be initialized！");
					kis.setEnabled(false);
					return;
				}
		}
		for (String s : Activate.defaultFile) {
			file = new File(kis.getDataFolder(), s);
			if (!file.exists())
				try {
					Utils.writeFile(file, Utils.readFile(getClass().getResourceAsStream("/resources/" + s)));
				} catch (IOException e) {
					e.printStackTrace();
					getLogger().error("§4Unable to load default file!");
					kis.setEnabled(false);
					return;
				}
		}
		file = new File(kis.getDataFolder(), Activate.ConfigFileName);
		if (!file.exists()) {
			try {
				Utils.writeFile(file,
						Utils.readFile(getClass().getResourceAsStream("/resources/" + Activate.ConfigFileName)));
			} catch (IOException e) {
				e.printStackTrace();
				getLogger().error("§4Error initializing default configuration!");
				kis.setEnabled(false);
				return;
			}
		}
		ac.config = new Config(new File(kis.getDataFolder(), Activate.ConfigFileName), 2);
		ac.message = new Message(ac);
		Config config;
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(dumperOptions);
		String content;
		for (String s : Activate.loadFile) {
			file = new File(kis.getDataFolder(), s);
			try {
				content = Utils.readFile(getClass().getResourceAsStream("/resources/" + s));
				if (!file.exists())
					Utils.writeFile(file, content);
				config = new Config(file, Config.YAML);
				config.setAll(getMap(config.getAll(), new ConfigSection(yaml.loadAs(content, LinkedHashMap.class))));
				config.save();
			} catch (IOException e1) {
				e1.printStackTrace();
				getLogger().info(ac.message.getMessage("无法初始化配置", new String[] { "{FileName}" },
						new Object[] { file.getName() }));
			}
		}
		try {
			content = getMessage(lang);
			file = Message.getFile();
			config = new Config(file, Config.YAML);
			config.setAll(
					getMap(config.getAll(), FullMessage(new ConfigSection(yaml.loadAs(content, LinkedHashMap.class)))));
			config.save();
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().info(ac.message.getMessage("无法效验语言文件"));
		}
	}

	/**
	 * 获取完全态的Message文件数据
	 * 
	 * @param map
	 * @return
	 */
	private LinkedHashMap<String, Object> FullMessage(LinkedHashMap<String, Object> map) {
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(dumperOptions);
		String content;
		try {
			content = Utils.readFile(getClass().getResourceAsStream("/resources/" + Activate.MessageFileName));
		} catch (IOException e) {
			e.printStackTrace();
			return map;
		}
		return getMap(map, new ConfigSection(yaml.loadAs(content, LinkedHashMap.class)));
	}

	/**
	 * 获取对应的语言文件
	 * 
	 * @param lang
	 * @return
	 * @throws IOException
	 */
	private String getMessage(String lang) throws IOException {
		if (lang != null && getClass().getResource("/language/" + lang + ".yml") != null)
			return Utils.readFile(getClass().getResourceAsStream("/language/" + lang + ".yml"));
		return Utils.readFile(getClass().getResourceAsStream("/resources/Message.yml"));
	}

	/**
	 * 效验配置文件是否匹配
	 * 
	 * @param map1
	 * @param map2
	 * @return
	 */
	private LinkedHashMap<String, Object> getMap(Map<String, Object> map1, Map<String, Object> map2) {
		if (map1.equals(map2))
			return (LinkedHashMap<String, Object>) map1;
		Map<String, Object> m1, m2;
		for (String Key : map2.keySet()) {
			Object obj = map2.get(Key);
			if (map1.containsKey(Key)) {
				Object obj2 = map1.get(Key);
				if (obj == null)
					continue;
				if (obj2 == null) {
					map1.put(Key, obj);
					continue;
				}
				if (obj.getClass().getName().equals(obj2.getClass().getName()) && !(obj instanceof Map)) {
					continue;
				} else if ((obj instanceof Map) && (obj2 instanceof Map)) {
					m1 = (Map<String, Object>) obj2;
					m2 = (Map<String, Object>) obj;
					if (m1.equals(m2))
						continue;
					map1.put(Key, getMap(m1, m2));
				} else
					map1.put(Key, obj);
			} else
				map1.put(Key, obj);
		}
		return (LinkedHashMap<String, Object>) map1;
	}

	private PluginLogger getLogger() {
		return kis.getLogger();
	}

}
