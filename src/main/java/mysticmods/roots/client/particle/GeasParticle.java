package mysticmods.roots.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.particle.ColorGravityParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;

public class GeasParticle extends TextureSheetParticle {
  protected int entityId;

  protected GeasParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1) {
    super(level, x, y, z, 0, 0, 0);
    this.lifetime = 30;
    this.hasPhysics = false;
    this.roll = this.oRoll = level.getRandom().nextBoolean() ? (float) Math.toRadians(0) : (float) Math.toRadians(-180);
  }

  private Entity getEntity() {
    return this.level.getEntity(this.entityId);
  }

  private void syncWithEntity() {
    Entity entity = this.getEntity();
    if (entity != null) {
      this.setPos(entity.getX(), entity.getY(), entity.getZ());
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void tick() {
    if (this.age++ >= this.lifetime || this.getEntity() == null || this.getEntity().isRemoved()) {
      this.remove();
    }
    this.syncWithEntity();
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    Quaternionf quaternionf = new Quaternionf();
    this.getFacingCameraMode().setRotation(quaternionf, renderInfo, partialTicks);


          poseStack.translate(0f, entity.getBbHeight() + entity.getBbHeight() * 0.2 + bobbingOffset, 0);
          poseStack.mulPose(((AccessorMixinEntityRenderer)renderer).getEntityRenderDispatcher().cameraOrientation());
          poseStack.scale(0.3f * pulse, 0.3f * pulse, 0.3f * pulse);
          poseStack.translate(-0.5f, 0, -0.5f);
          Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
              poseStack.last(),
              buffer.getBuffer(Sheets.translucentItemSheet()),
              null,
              ClientSetup.GEAS_MODEL,
              1,
              1,
              1,
              LightTexture.FULL_SKY,
              OverlayTexture.NO_OVERLAY,
              ModelData.EMPTY,
              Sheets.translucentItemSheet());
          poseStack.popPose();
        }
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<ColorGravityParticleOptions> {
    @Override
    public Particle createParticle(ColorGravityParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new GeasParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
