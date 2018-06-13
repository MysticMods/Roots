package teamroots.roots.tileentity;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityImbuerRenderer extends TileEntitySpecialRenderer {

	@Override
	public void render(TileEntity te, double x, double y, double z,
			float partialTicks, int destroyStage, float alpha) {
		if (te instanceof TileEntityImbuer){
			TileEntityImbuer tei = (TileEntityImbuer)te;
			if (tei.inventory.getStackInSlot(1) != ItemStack.EMPTY){
				GL11.glPushMatrix();
				EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,tei.inventory.getStackInSlot(1));
				item.hoverStart = 0;
				GL11.glTranslated(x+0.5, y+0.3125, z+0.5);
				GL11.glRotated(tei.angle,0,1.0,0);
				Minecraft.getMinecraft().getRenderManager().doRenderEntity(item, 0, 0, 0, 0, 0, true);
				GL11.glPopMatrix();
			}
			if (tei.inventory.getStackInSlot(0) != ItemStack.EMPTY){
				GL11.glPushMatrix();
				EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,tei.inventory.getStackInSlot(0));
				item.hoverStart = 0;
				GL11.glTranslated(x+0.5, y+0.125, z);
				GL11.glRotated(90,1.0,0,0);
				Minecraft.getMinecraft().getRenderManager().doRenderEntity(item, 0, 0, 0, 0, 0, true);
				GL11.glPopMatrix();
			}
		}
	}
	
}
