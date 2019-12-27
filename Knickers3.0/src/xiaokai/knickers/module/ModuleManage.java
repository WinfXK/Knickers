package xiaokai.knickers.module;

import xiaokai.knickers.Activate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Winfxk
 */
public class ModuleManage {
	private LinkedHashMap<String, Class<Module>> Modules;

	public ModuleManage(Activate ac) {
		Modules = new LinkedHashMap<>();
	}

	/**
	 * 添加一个新的按钮模块</br>
	 * Add a new button module
	 * 
	 * @param Type        按钮模块类型，这个类型将会存储到配置文件</br>
	 *                    Button module type, which will be stored in the
	 *                    configuration file
	 * @param ModuleClass 模块的Class对象</br>
	 *                    The Class object of the module
	 * @return
	 */
	public boolean addModule(String Type, Class<Module> ModuleClass) {
		if (Modules.containsKey(Type))
			return false;
		Modules.put(Type, ModuleClass);
		return Modules.containsKey(Type);
	}

	/**
	 * 删除一个按钮模块</br>
	 * Remove a button module
	 * 
	 * @param Type 要删除的按钮模块类型</br>
	 *             Type of button module to delete
	 * @return
	 */
	public boolean removeModule(String Type) {
		if (!Modules.containsKey(Type))
			return false;
		Modules.remove(Type);
		return !Modules.containsKey(Type);
	}

	/**
	 * 是否存在某个按钮模块</br>
	 * Whether a button module exists
	 * 
	 * @param Type 按钮模块的类型</br>
	 *             Type of button module
	 * @return
	 */
	public boolean existModule(String Type) {
		return Modules.containsKey(Type);
	}

	/**
	 * 返回已经添加了的按钮模块列表</br>
	 * Returns the list of button modules that have been added
	 * 
	 * @return
	 */
	public List<String> getModules() {
		return new ArrayList<>(Modules.keySet());
	}
}
