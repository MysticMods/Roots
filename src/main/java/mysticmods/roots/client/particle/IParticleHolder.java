package mysticmods.roots.client.particle;

import mysticmods.roots.blockentity.template.BaseBoundedBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public interface IParticleHolder {
  Particle getParticle(ParticleType<?> type);

  Particle getParticle(ParticleType<?> type, IParticleTester tester);

  void setParticle(ParticleType<?> type, Particle particle);

  void setParticle(ParticleType<?> type, Particle particle, IParticleTester tester);

  @Nullable
  static IParticleHolder getHolder(Level level, BlockPos pos) {
    if (!level.isClientSide()) {
      throw new IllegalStateException("Cannot get particle holder on server side");
    }

    BlockEntity blockEntity = level.getBlockEntity(pos);
    if (blockEntity instanceof BaseBoundedBlockEntity base) {
      Object holder = base.getParticleHolder();
      if (holder == null) {
        base.setParticleHolder(holder = new SimpleParticleHolder());
      } else if (!(holder instanceof IParticleHolder)) {
        throw new IllegalStateException("Particle holder is not an instance of IParticleHolder at " + pos);
      }
      return (IParticleHolder) holder;
    } else {
      return null;
    }
  }

  @Nullable
  static IParticleHolder getHolder(Entity entity) {
    if (entity instanceof IParticleHolder holder) {
      return holder;
    }

    return null;
  }

  @Nullable
  static IParticleHolder getHolder(int entityId) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.level.getEntity(entityId);
    return getHolder(entity);
  }
}
