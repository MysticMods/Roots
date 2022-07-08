package mysticmods.roots.capability;

import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import mysticmods.roots.api.herbs.Herb;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HerbCapability implements ICapabilityProvider, ICapabilitySerializable<ListTag> {
  private final Object2FloatOpenHashMap<Herb> HERB_MAP = new Object2FloatOpenHashMap<>();

  public HerbCapability() {
  }

  @NotNull
  @Override
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    return Capabilities.HERB_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
  }

  @Override
  public ListTag serializeNBT() {
    ListTag result = new ListTag();
    HERB_MAP.forEach((herb, value) -> {
      CompoundTag tag = new CompoundTag();
      tag.putString("herb", herb.getRegistryName().toString());
      tag.putFloat("value", value);
      result.add(tag);
    });
    return result;
  }

  @Override
  public void deserializeNBT(ListTag nbt) {
    HERB_MAP.clear();
    for (int i = 0; i < nbt.size(); i++) {
      CompoundTag tag = nbt.getCompound(i);
      HERB_MAP.put(ModRegistries.HERB_REGISTRY.get().getValue(new ResourceLocation(tag.getString("herb"))), tag.getFloat("value"));
    }
  }
}
