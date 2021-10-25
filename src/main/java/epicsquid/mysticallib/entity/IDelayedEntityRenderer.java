package epicsquid.mysticallib.entity;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;

public interface IDelayedEntityRenderer {

  void renderLater(@Nonnull Entity entity, double dx, double dy, double dz, float yaw, float pTicks);
}
