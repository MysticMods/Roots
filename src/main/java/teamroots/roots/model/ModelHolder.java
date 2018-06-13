package teamroots.roots.model;

import java.util.HashMap;
import net.minecraft.client.model.ModelBase;

public class ModelHolder {
	public static HashMap<String, ModelBase> entityModels = new HashMap<String, ModelBase>();
	
	public static void init(){
		entityModels.put("deer", new ModelDeer());
		entityModels.put("sprout", new ModelSprout());
		entityModels.put("fairy", new ModelFairy());
	}
}
