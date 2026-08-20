package mysticmods.roots.mixin.accessor;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ModConfigSpec.Builder.class)
public interface AccessorMixinModConfigSpec {
  @Accessor("values")
  List<ModConfigSpec.ConfigValue<?>> roots$getValues();
}
