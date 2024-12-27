package mysticmods.roots.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public interface IPlayerShoulderCapability extends ICapabilitySerializable<CompoundTag> {
  CompoundTag getAnimalSerialized();

  boolean isShouldered();

  ResourceLocation getEntityName();

  @Nullable
  EntityType<?> getEntityType(ResourceLocation registryName);

  @Nullable
  EntityType<?> getEntityType();

  void drop();

  void shoulder(Entity entity);

  CompoundTag generateShoulderNBT();
}
