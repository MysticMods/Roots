package mysticmods.roots.block.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.function.Supplier;

public class BafflecapBlock extends MushroomBlock {
  public BafflecapBlock(Properties pProperties) {
    super(pProperties, () -> null);
  }

  public boolean growMushroom(ServerLevel pLevel, BlockPos pPos, BlockState pState, RandomSource pRandom) {
    return false;
/*    net.minecraftforge.event.level.SaplingGrowTreeEvent event = net.minecraftforge.event.ForgeEventFactory.blockGrowFeature(pLevel, pRandom, pPos, this.featureSupplier.get());
    if (event.getResult().equals(net.minecraftforge.eventbus.api.Event.Result.DENY)) return false;
    pLevel.removeBlock(pPos, false);
    if (event.getFeature().value().place(pLevel, pLevel.getChunkSource().getGenerator(), pRandom, pPos)) {
      return true;
    } else {
      pLevel.setBlock(pPos, pState, 3);
      return false;
    }*/
  }
}
