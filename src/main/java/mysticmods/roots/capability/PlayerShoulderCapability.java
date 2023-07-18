package mysticmods.roots.capability;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.capability.IPlayerShoulderCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class PlayerShoulderCapability implements IPlayerShoulderCapability, ICapabilitySerializable<CompoundTag>, ICapabilityProvider {
  public static MethodHandle setRightShoulder = null;
  public static MethodHandle setLeftShoulder = null;

  static {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    Method setLeft = ObfuscationReflectionHelper.findMethod(Player.class, "m_36362_", CompoundTag.class);

    setLeft.setAccessible(true);
    Method setRight = ObfuscationReflectionHelper.findMethod(Player.class, "m_36364_", CompoundTag.class);
    try {
      setLeftShoulder = lookup.unreflect(setLeft);
      setRightShoulder = lookup.unreflect(setRight);
    } catch (IllegalAccessException e) {
      RootsAPI.LOG.error("Unable to unprotect setRightShoulder", e);
    }
  }

  private CompoundTag animalSerialized = new CompoundTag();
  private boolean shouldered = false;
  private ResourceLocation registryName = null;

  public PlayerShoulderCapability() {
  }

  @Override
  public CompoundTag getAnimalSerialized() {
    return animalSerialized;
  }

  @Override
  public boolean isShouldered() {
    return shouldered;
  }

  @Override
  public ResourceLocation getEntityName() {
    return registryName;
  }

  @Override
  @Nullable
  public EntityType<?> getEntityType(ResourceLocation registryName) {
    return ForgeRegistries.ENTITY_TYPES.getValue(registryName);
  }

  @Override
  @Nullable
  public EntityType<?> getEntityType() {
    return getEntityType(getEntityName());
  }

  @Override
  public void drop() {
    this.animalSerialized = new CompoundTag();
    this.shouldered = false;
    this.registryName = null;
  }

  @Override
  public void shoulder(Entity entity) {
    this.animalSerialized = new CompoundTag();
    entity.save(this.animalSerialized);
    this.shouldered = true;
    this.registryName = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
  }

  @Override
  public CompoundTag generateShoulderNBT() {
    CompoundTag result = new CompoundTag();
    result.putBoolean("Silent", true);
    result.putString("id", registryName == null ? "minecraft:pig" : registryName.toString());
    return result;
  }

  @NotNull
  @Override
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
    return Capabilities.PLAYER_SHOULDER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag result = new CompoundTag();
    result.put("animalSerialized", animalSerialized);
    result.putBoolean("shouldered", shouldered);
    result.putString("registryName", registryName == null ? "" : registryName.toString());
    return result;
  }

  @Override
  public void deserializeNBT(CompoundTag incoming) {
    if (incoming.contains("animalSerialized")) {
      this.animalSerialized = incoming.getCompound("animalSerialized");
    }
    if (incoming.contains("shouldered")) {
      this.shouldered = incoming.getBoolean("shouldered");
    }
    if (incoming.contains("registryName")) {
      this.registryName = incoming.getString("registryName").isEmpty() ? null : new ResourceLocation(incoming.getString("registryName"));
    }
  }
}
