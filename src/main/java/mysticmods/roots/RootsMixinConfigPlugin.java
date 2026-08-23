package mysticmods.roots;

import mysticmods.roots.mixin.MixinComponentSerialization$IngredientContents;
import mysticmods.roots.mixin.MixinRecipeBuilder$DataGenerationModidFix;
import mysticmods.roots.mixin.accessor.AccessorMixinModConfigSpec;
import net.neoforged.fml.loading.FMLEnvironment;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class RootsMixinConfigPlugin implements IMixinConfigPlugin {
  @SuppressWarnings("ReferenceToMixin")
  private static final Set<String> DEV_ONLY_CLASSES = Set.of(AccessorMixinModConfigSpec.class.getName(), MixinRecipeBuilder$DataGenerationModidFix.class.getName(), MixinComponentSerialization$IngredientContents.class.getName());

  @Override
  public void onLoad(String mixinPackage) {

  }

  @Override
  public String getRefMapperConfig() {
    return null;
  }

  @Override
  public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
    if (FMLEnvironment.production && DEV_ONLY_CLASSES.contains(mixinClassName)) {
      return false;
    }

    return true;
  }

  @Override
  public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

  }

  @Override
  public List<String> getMixins() {
    return null;
  }

  @Override
  public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

  }

  @Override
  public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

  }
}
