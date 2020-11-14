package epicsquid.roots.entity.layer;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellStormCloud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class AquaBubbleRenderer<T extends EntityLivingBase> implements LayerRenderer<T> {
  private static final ResourceLocation AQUA_TEXTURE = new ResourceLocation(Roots.MODID, "textures/entity/aqua_bubble.png");
  private final RenderLivingBase<T> renderer;
  private final ModelBase model;
  private final boolean player;

  public AquaBubbleRenderer(RenderLivingBase<T> renderer) {
    this.renderer = renderer;
    if (renderer instanceof RenderPlayer) {
      boolean smallArms = ObfuscationReflectionHelper.getPrivateValue(ModelPlayer.class, ((RenderPlayer)renderer).getMainModel(), "field_178735_y");
      this.model = new ModelPlayer(1.3f, smallArms);
      this.player = true;
    } else {
      this.player = false;
      this.model = renderer.getMainModel();
    }
  }

  @Override
  public void doRenderLayer(T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    if (player.getActivePotionEffect(ModPotions.aqua_bubble) != null) {
      boolean flag = player.isInvisible();
      GlStateManager.depthMask(!flag);
      this.renderer.bindTexture(AQUA_TEXTURE);
      GlStateManager.matrixMode(5890);
      GlStateManager.loadIdentity();
      float f = (float) player.ticksExisted + partialTicks;
      GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
      GlStateManager.matrixMode(5888);
      GlStateManager.enableBlend();
      GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
      GlStateManager.disableLighting();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
      this.model.setModelAttributes(this.renderer.getMainModel());
      Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
      if (this.player) {
        this.model.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
      } else {
        this.model.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale * 0.5f);
      }
      Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
      GlStateManager.matrixMode(5890);
      GlStateManager.loadIdentity();
      GlStateManager.matrixMode(5888);
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.depthMask(flag);

      if (Util.rand.nextInt(10) == 0) {
        World world = Minecraft.getMinecraft().world;
        for (float i = 0; i < 360; i += Util.rand.nextInt(40)) {
          float x = (float) player.posX + (1.2f * Util.rand.nextFloat()) * (float) Math.sin(Math.toRadians(i));
          float y = (float) player.posY + (Util.rand.nextFloat() + 0.25f);
          float z = (float) player.posZ + (1.2f * Util.rand.nextFloat()) * (float) Math.cos(Math.toRadians(i));
          float vx = 0.0625f * (float) Math.cos(Math.toRadians(i));
          float vz = 0.025f * (float) Math.sin(Math.toRadians(i));
          if (Util.rand.nextBoolean()) {
            vx *= -1;
            vz *= -1;
          }
          // TODO: Adjust smoke
          if (Util.rand.nextBoolean()) {
            ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, SpellStormCloud.instance.getFirstColours(0.125f), 4f + Util.rand.nextFloat() * 6f, 120, false);
          } else {
            ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, SpellStormCloud.instance.getSecondColours(0.125f), 4f + Util.rand.nextFloat() * 6f, 120, false);
          }
        }
      }
    }
  }

  @Override
  public boolean shouldCombineTextures() {
    return false;
  }
}
