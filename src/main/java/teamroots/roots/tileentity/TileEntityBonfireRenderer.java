package teamroots.roots.tileentity;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBonfireRenderer extends TileEntitySpecialRenderer {
	@Override
	public void render(TileEntity te, double x, double y, double z,
			float partialTicks, int destroyStage, float alpha) {
		if (te instanceof TileEntityBonfire){
			TileEntityBonfire tem = (TileEntityBonfire)te;
			ArrayList<ItemStack> renderItems = new ArrayList<ItemStack>();
			
			for (int i = 0; i < tem.inventory.getSlots(); i ++){
				if (tem.inventory.getStackInSlot(i) != ItemStack.EMPTY){
					renderItems.add(tem.inventory.getStackInSlot(i));
				}
			}
			
			for (int i = 0; i < renderItems.size(); i ++){
				GL11.glPushMatrix();
				EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,renderItems.get(i));
				item.hoverStart = 0;
				double shifted = tem.ticker + partialTicks + i * (360.0/renderItems.size());
				Random random = new Random();
				random.setSeed(item.getItem().hashCode());
				GL11.glTranslated(x+0.5, y+0.5+0.1*Math.sin(Math.toRadians((shifted*4.0))), z+0.5);
				GL11.glRotated(shifted, 0, 1, 0);
				GL11.glTranslated(-0.5,0,0);
				GL11.glRotated(shifted, 0, 1, 0);
				Minecraft.getMinecraft().getRenderManager().doRenderEntity(item, 0, 0, 0, 0, 0, true);
				GL11.glPopMatrix();
			}
		}
	}
	
}
