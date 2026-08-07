package mysticmods.roots.mixin.accessor;

import net.minecraft.data.worldgen.biome.OverworldBiomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OverworldBiomes.class)
public interface AccessorMixinOverworldBiomes {
  @Accessor("NORMAL_WATER_COLOR")
  static int roots$GetNormalWaterColor() {
    throw new AssertionError();
  }
}
