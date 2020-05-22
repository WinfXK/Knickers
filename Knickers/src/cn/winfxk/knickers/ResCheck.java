package cn.winfxk.knickers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.Utils;
import cn.winfxk.knickers.tool.Tool;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * @author Winfxk
 */
public class ResCheck {
	private Activate ac;
	private PluginBase kis;
	private PluginLogger log;
	public Yaml yaml;

	/**
	 * 数据检查
	 */
	public ResCheck(Activate activate) {
		this.ac = activate;
		kis = activate.getPluginBase();
		log = kis.getLogger();
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		yaml = new Yaml(dumperOptions);
	}

	/**
	 * 检查玩家的配置文件
	 *
	 * @param config
	 * @return
	 */
	public Config Check(MyPlayer player) {
		try {
			player.config.setAll(getMap(player.config.getAll(), yaml.loadAs(
					Utils.readFile(getClass().getResourceAsStream("/resources/player.yml")), LinkedHashMap.class)));
		} catch (IOException e) {
			e.printStackTrace();
			player.getPlayer().kick(ac.message.getMessage("无法加载资源", player));
		}
		return player.config;
	}

	/**
	 * 检查插件数据
	 *
	 * @return
	 */
	protected ResCheck start() {
		File file = Message.getFile();
		String lang = Tool.getLanguage();
		if (!file.exists())
			if (lang != null && getClass().getResource("/language/" + lang + ".yml") != null)
				try {
					log.info("Writing to the default language:" + lang);
					Utils.writeFile(Message.getFile(),
							Utils.readFile(getClass().getResourceAsStream("/language/" + lang + ".yml")));
				} catch (IOException e) {
					e.printStackTrace();
					log.error("§4The default language could not be initialized！");
					try {
						Utils.writeFile(Message.getFile(),
								Utils.readFile(getClass().getResourceAsStream("/resources/Message.yml")));
					} catch (IOException e1) {
						e1.printStackTrace();
						log.error("§4The default language could not be initialized！");
						kis.setEnabled(false);
						return null;
					}
				}
			else
				try {
					Utils.writeFile(Message.getFile(),
							Utils.readFile(getClass().getResourceAsStream("/resources/Message.yml")));
				} catch (IOException e1) {
					e1.printStackTrace();
					log.error("§4The default language could not be initialized！");
					kis.setEnabled(false);
					return null;
				}
		for (String s : Activate.defaultFile) {
			file = new File(kis.getDataFolder(), s);
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			if (!file.exists())
				try {
					Utils.writeFile(file, Utils.readFile(getClass().getResourceAsStream("/resources/" + s)));
				} catch (IOException e) {
					e.printStackTrace();
					log.error("§4Unable to load default file!");
					kis.setEnabled(false);
					return null;
				}
		}
		file = new File(kis.getDataFolder(), Activate.ConfigFileName);
		if (!file.exists())
			try {
				Utils.writeFile(file,
						Utils.readFile(getClass().getResourceAsStream("/resources/" + Activate.ConfigFileName)));
			} catch (IOException e) {
				e.printStackTrace();
				log.error("§4Error initializing default configuration!");
				kis.setEnabled(false);
				return null;
			}
		ac.config = new Config(new File(kis.getDataFolder(), Activate.ConfigFileName), Config.YAML);
		ac.message = new Message(ac, false);
		file = new File(ac.getPluginBase().getDataFolder(), Activate.LanguageDirName);
		if (!file.exists())
			file.mkdirs();
		try {
			JarFile localJarFile = new JarFile(
					new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile()));
			Enumeration<JarEntry> entries = localJarFile.entries();
			File Jf, JFB;
			String JN, JP, JE;
			while (entries.hasMoreElements()) {
				JarEntry jarEntry = entries.nextElement();
				if (jarEntry == null)
					continue;
				JE = jarEntry.getName();
				if (JE == null)
					continue;
				Jf = new File(JE);
				JP = Jf.getParent();
				if (JP == null)
					continue;
				JN = Jf.getName();
				if (JP.equals("language")) {
					JFB = new File(new File(ac.getPluginBase().getDataFolder(), Activate.LanguageDirName), JN);
					if (!JFB.exists())
						try {
							Utils.writeFile(JFB, Utils.readFile(getClass().getResourceAsStream("/language/" + JN)));
						} catch (Exception e) {
							e.printStackTrace();
							ac.getPluginBase().getLogger().warning(ac.getMessage().getMessage("无法获取已支持的语言列表"));
						}
				}
			}
			localJarFile.close();
		} catch (IOException e2) {
			e2.printStackTrace();
			ac.getPluginBase().getLogger().warning(
					ac.getMessage().getMessage("无法加载资源", new String[] { "{Error}" }, new Object[] { e2.getMessage() }));
		}
		Config config;
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
				log.info(ac.message.getMessage("无法初始化配置", new String[] { "{FileName}" },
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
			log.info(ac.message.getMessage("无法效验语言文件"));
		}
		for (String dir : Activate.Mkdir) {
			file = new File(kis.getDataFolder(), dir);
			if (!file.exists())
				file.mkdirs();
		}
		Properties properties = new Properties();
		file = new File(ac.getPluginBase().getDataFolder(), Activate.SystemFileName);
		try {
			if (!file.exists())
				properties.storeToXML(new FileOutputStream(file), null);
			properties.loadFromXML(new FileInputStream(file));
			if (properties.getProperty("Vip") == null || !properties.getProperty("Vip").equals("OK")) {
				for (String string : Activate.ForOnce)
					Utils.writeFile(new File(ac.getPluginBase().getDataFolder(), string),
							Utils.readFile(getClass().getResourceAsStream("/resources/" + string)));
				properties.put("Vip", "OK");
				properties.storeToXML(new FileOutputStream(file), null);
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error(ac.message.getMessage("无法加载系统数据"));
		}
		ac.config = new Config(new File(kis.getDataFolder(), Activate.ConfigFileName), Config.YAML);
		ac.message = new Message(ac);
		ac.CommandConfig = new Config(new File(kis.getDataFolder(), Activate.CommandFileName), Config.YAML);
		ac.FormID.FormIDConfig = new Config(new File(kis.getDataFolder(), Activate.FormIDFileName), Config.YAML);
		ac.FormID.examine();
		return this;
	}

	/**
	 * 返回Jar包所在的位置
	 *
	 * @return
	 */
	public String getPath() {
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		if (System.getProperty("os.name").contains("dows"))
			path = path.substring(1, path.length());
		if (path.contains("jar")) {
			path = path.substring(0, path.lastIndexOf("."));
			return path.substring(0, path.lastIndexOf("/"));
		}
		return path.replace("target/classes/", "");
	}

	/**
	 * 获取完全态的Message文件数据
	 *
	 * @param map
	 * @return
	 */
	public LinkedHashMap<String, Object> FullMessage(LinkedHashMap<String, Object> map) {
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
	 * @param map1 要对比的数据
	 * @param map2 这个是标本，另一个参照这个对比数据
	 * @return
	 */
	public LinkedHashMap<String, Object> getMap(Map<String, Object> map1, Map<String, Object> map2) {
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
				if (obj.getClass().getName().equals(obj2.getClass().getName()) && !(obj instanceof Map))
					continue;
				else if (obj instanceof Map && obj2 instanceof Map) {
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
}
