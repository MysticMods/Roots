package mysticmods.roots.client.particle.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.world.item.ItemStack;

public abstract class RootsItemParticle extends RootsParticle {
  protected final float uo, vo;

  protected RootsItemParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ItemStack item) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    if (item == null) {
      throw new IllegalArgumentException("ItemStack cannot be null for particle");
    }
    var model = Minecraft.getInstance().getItemRenderer().getModel(item, level, null, 0);
    this.setSprite(model.getOverrides().resolve(model, item, level, null, 0)
        .getParticleIcon(net.neoforged.neoforge.client.model.data.ModelData.EMPTY));

    this.uo = this.random.nextFloat() * 3.0F;
    this.vo = this.random.nextFloat() * 3.0F;

  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.TERRAIN_SHEET;
  }

  @Override
  protected float getU0() {
    return this.sprite.getU((this.uo + 1.0F) / 4.0F);
  }

  @Override
  protected float getU1() {
    return this.sprite.getU(this.uo / 4.0F);
  }

  @Override
  protected float getV0() {
    return this.sprite.getV(this.vo / 4.0F);
  }

  @Override
  protected float getV1() {
    return this.sprite.getV((this.vo + 1.0F) / 4.0F);
  }
}
