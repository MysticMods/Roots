package mysticmods.roots.mixin;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.data.recipes.RecipeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// TODO: This should only be enabled during recipe generation
@Mixin(RecipeBuilder.class)
public interface MixinRecipeBuilder$DataGenerationModidFix {
  @ModifyArg(method = "save(Lnet/minecraft/data/recipes/RecipeOutput;Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;parse(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"))
  default String saveChangeThing(String id) {
    if (!id.startsWith("roots:")) {
      RootsAPI.LOG.error("Had to fix recipe id: {}", id);
      return "roots:" + id;
    }

    return id;
  }
}
