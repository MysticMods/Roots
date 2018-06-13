package teamroots.roots.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import teamroots.roots.model.ModelHolder;

public class RenderDeer extends RenderLiving<EntityDeer> {

	public RenderDeer(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
		super(renderManager, modelBase, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDeer entity) {
		if (entity.getDataManager().get(EntityDeer.hasRednose)){
			return new ResourceLocation("roots:textures/entity/rudolph.png");
		}
		return new ResourceLocation("roots:textures/entity/deer.png");
	}
	
	public static class Factory implements IRenderFactory<EntityDeer> {
		@Override
		public Render<EntityDeer> createRenderFor(RenderManager manager) {
			return new RenderDeer(manager, ModelHolder.entityModels.get("deer"), 0.35f);
		}
	}
}
