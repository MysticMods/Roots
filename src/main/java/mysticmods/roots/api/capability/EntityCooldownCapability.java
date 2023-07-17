package mysticmods.roots.api.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.IntTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EntityCooldownCapability implements ICapabilityProvider, ICapabilitySerializable<IntTag> {
  private int expiresAt = -1;

  public EntityCooldownCapability() {
  }

  public void setExpiresAt (MinecraftServer server, int tickCount) {
    this.expiresAt = server.getTickCount() + tickCount;
  }

  public void setExpiresAt (int totalTickCount) {
    this.expiresAt = totalTickCount;
  }

  public boolean hasExpired(MinecraftServer server) {
    return expiresAt == -1 || server.getTickCount() >= expiresAt;
  }

  @NotNull
  @Override
  public abstract <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side);

  @Override
  public IntTag serializeNBT() {
    return IntTag.valueOf(expiresAt);
  }

  @Override
  public void deserializeNBT(IntTag nbt) {
    this.expiresAt = nbt.getAsInt();
  }

  public static class RunicShearsEntityCooldown extends EntityCooldownCapability {
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      return Capabilities.RUNIC_SHEARS_ENTITY_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
    }
  }

  public static class RunicShearsTokenCooldown extends EntityCooldownCapability {
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      return Capabilities.RUNIC_SHEARS_TOKEN_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
    }
  }

  public static class SquidMilkingCapability extends EntityCooldownCapability {
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      return Capabilities.SQUID_MILKING_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
    }
  }
}
