package epicsquid.roots.particle;

import epicsquid.mysticallib.struct.Vec4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

// This code originally derived from the Public Domain code of EnderIO for 1.12.2
// https://github.com/SleepyTrousers/EnderIO
public class RangeParticle<T extends TileEntity & RenderUtil.IRanged> extends Particle {

  private static final int INIT_TIME = 20;
  private static final int AGE_LIMIT = 20 * 20; // 60 seconds * 10; // 10 minutes

  private final T owner;
  private final Vec4f color;
  private int age = 0;

  public RangeParticle(T owner) {
    this(owner, new Vec4f(0.78f, 0.54f, 0.19f, 0.4f));
  }

  public RangeParticle(T owner, Vec4f color) {
    super(owner.getWorld(), owner.getPos().getX(), owner.getPos().getY(), owner.getPos().getZ());
    this.owner = owner;
    this.color = color;
  }

  @Override
  public void onUpdate() {
    if (!isAlive() || isExpired) {
      owner.setShowingRange(false);
    }

    age++;
  }

  @Override
  public boolean isAlive() {
    return age < AGE_LIMIT && owner.hasWorld() && !owner.isInvalid() && owner.isShowingRange() && world.getTileEntity(owner.getPos()) == owner;
  }

  @Override
  public int getFXLayer() {
    return 3;
  }

  @Override
  public void renderParticle(
      @Nonnull BufferBuilder worldRendererIn, @Nonnull Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

    GlStateManager.pushMatrix();
    GlStateManager.enableLighting();
    GlStateManager.disableLighting();
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    GlStateManager.depthMask(false);

    float scale = Math.min((age + partialTicks) / INIT_TIME, 1);

    // Vanilla bug? Particle.interpPosX/Y/Z variables are always one frame behind
    double x = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
    double y = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
    double z = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
    GlStateManager.translate(-x, -y, -z);

    RenderUtil.renderBoundingBox(scale(owner.getPos(), getBounds(), scale).expand(0.01, 0.01, 0.01), color);

    GlStateManager.depthMask(true);
    GlStateManager.disableBlend();
    GlStateManager.enableCull();
    GlStateManager.enableLighting();
    GlStateManager.popMatrix();
  }

  protected @Nonnull
  AxisAlignedBB getBounds() {
    return owner.getBounds();
  }

  private @Nonnull
  AxisAlignedBB scale(final @Nonnull BlockPos source, final @Nonnull AxisAlignedBB bounds, float scale) {
    final double sourceX = source.getX() + .5, sourceY = source.getY() + .5, sourceZ = source.getZ() + .5;
    return new AxisAlignedBB(
        scale(sourceX, bounds.minX, scale),
        scale(sourceY, bounds.minY, scale),
        scale(sourceZ, bounds.minZ, scale),
        scale(sourceX, bounds.maxX, scale),
        scale(sourceY, bounds.maxY, scale),
        scale(sourceZ, bounds.maxZ, scale));
  }

  private double scale(double x0, double x1, double scale) {
    return x0 * (1 - scale) + x1 * scale;
  }
}
