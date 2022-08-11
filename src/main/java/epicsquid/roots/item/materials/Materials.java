package epicsquid.roots.item.materials;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class Materials {
	public static ToolMaterial LIVING = EnumHelper.addToolMaterial("living", 2, 250, 6.0f, 2.0f, 19);
	public static ToolMaterial TERRASTONE = EnumHelper.addToolMaterial("terrastone", 3, 780, 8.0f, 3.0f, 21);
	public static ToolMaterial RUNIC = EnumHelper.addToolMaterial("runic", 3, 1991, 9.5f, 4.5f, 27);
	
	public static void load() {
	
	}
}
