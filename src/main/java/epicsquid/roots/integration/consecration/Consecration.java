package epicsquid.roots.integration.consecration;

import net.minecraftforge.fml.common.event.FMLInterModComms;

public class Consecration {
	public static void init() {
		FMLInterModComms.sendMessage("consecration", "holydamage", "holy_damage");
	}
}
