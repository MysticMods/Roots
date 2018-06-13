package teamroots.roots.util;

import java.lang.reflect.Field;

import net.minecraft.client.renderer.OpenGlHelper;

public class Fields {
	public static Field OpenGlHelper_arbShaders;
	
	public static void init() throws NoSuchFieldException, SecurityException{
		OpenGlHelper_arbShaders = OpenGlHelper.class.getField("arbShaders");
	}
}
