package epicsquid.roots.util;

import epicsquid.roots.api.Herb;
import epicsquid.roots.client.hud.RenderHerbHUD;
import epicsquid.roots.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHerbUtil {
	@SuppressWarnings("ConstantConditions")
	@SideOnly(Side.CLIENT)
	public static boolean isCreativePouch() {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc == null || mc.player == null) {
			return false;
		}
		for (ItemStack stack : CommonHerbUtil.getPouches(mc.player)) {
			if (stack.getItem() == ModItems.creative_pouch) {
				return true;
			}
		}
		return false;
	}
	
	public static double getHerbAmount(Herb herb) {
		if (isCreativePouch()) {
			return 999;
		}
		
		return RenderHerbHUD.INSTANCE.herbAmount(herb);
	}
}
