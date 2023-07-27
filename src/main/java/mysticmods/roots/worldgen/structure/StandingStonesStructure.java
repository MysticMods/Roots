package mysticmods.roots.worldgen.structure;

import com.mojang.serialization.Codec;
import mysticmods.roots.init.ModFeatures;
import net.minecraft.world.level.levelgen.structure.SinglePieceStructure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class StandingStonesStructure extends SinglePieceStructure {
  public static Codec<StandingStonesStructure> CODEC = simpleCodec(StandingStonesStructure::new);

  protected StandingStonesStructure(StructureSettings pSettings) {
    super(StandingStonePiece::new, 12, 12, pSettings);
  }

  @Override
  public StructureType<?> type() {
    return ModFeatures.STANDING_STONES.get();
  }
}
