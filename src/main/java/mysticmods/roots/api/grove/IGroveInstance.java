package mysticmods.roots.api.grove;

import mysticmods.roots.api.IProvidesTick;
import mysticmods.roots.api.blockentity.Bounded;
import mysticmods.roots.api.grove.power.distributor.IGrovePowerDistributor;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;

public interface IGroveInstance extends Bounded, IProvidesTick {
  Grove asGrove();

  int getRank();

  int getMaxRank();

  IGrovePowerDistributor getCollector();

  default boolean is(TagKey<Grove> tag) {
    return asGrove().is(tag);
  }

  default BlockPos getGrovePosition() {
    return BlockPos.ZERO;
  }
}
