package epicsquid.roots.client.particle;

import epicsquid.roots.spell.SpellExtension;
import net.minecraft.client.particle.CloudParticle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

// Based off ParticlePosition from FindMe by Buuz
// https://github.com/Buuz135/FindMe/blob/1.12/src/main/java/com/buuz135/findme/proxy/client/ParticlePosition.java

public class ParticlePosition extends CloudParticle {
  private final SpellExtension.SenseType type;

  public ParticlePosition(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, SpellExtension.SenseType type) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0);
    this.motionX *= 0.25;
    this.motionY *= 0.25;
    this.motionZ *= 0.25;
    this.particleMaxAge = 20 * 5;
    this.type = type;
  }

  @Override
  public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
    Tessellator tessellator = Tessellator.getInstance();
    tessellator.draw();

    GlStateManager.disableDepth();
    switch (type) {
      case CONTAINER:
        GlStateManager.color(224 / 255.0f, 211 / 255.0f, 29 / 255.0f, 0.5f);
        break;
      case LIQUID:
        GlStateManager.color(46 / 255.0f, 133 / 255.0f, 209 / 255.0f, 0.5f);
        break;
      case SPAWNER:
        GlStateManager.color(207 / 255.0f, 66 / 255.0f, 19 / 255.0f, 0.5f);
        break;
      case ORE:
        GlStateManager.color(138 / 255.0f, 114 / 255.0f, 90 / 255.0f, 0.5f);
        break;
    }
    buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    tessellator.draw();

    GlStateManager.enableDepth();
    buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
  }
}
