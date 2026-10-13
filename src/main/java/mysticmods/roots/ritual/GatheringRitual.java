package mysticmods.roots.ritual;

import mysticmods.roots.api.ritual.IRitualInstance;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.util.MagnetismUtil;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class GatheringRitual extends Ritual {
  public GatheringRitual(Properties properties) {
    super(properties);
  }

  @Override
  protected void functionalTick(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (duration % instance.getInterval() == 0) {
      List<Vec3> positions = MagnetismUtil.store(pLevel, pPos, blockEntity, getRadiusXZ(instance), getRadiusY(instance), getRadiusXZ(instance));
      // TODO: Visuals for the yoink
    }
  }

  @Override
  protected void initialize(Holder<Ritual> holder) {

  }
}
