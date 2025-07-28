package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.concurrent.CompletableFuture;

public class RootsAttributeTagsProvider extends IntrinsicHolderTagsProvider<Attribute> {

  public RootsAttributeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @org.jetbrains.annotations.Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
    super(output, Registries.ATTRIBUTE, provider, p_256665_ -> BuiltInRegistries.ATTRIBUTE.getResourceKey(p_256665_)
        .orElseThrow(), RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RootsTags.Atrtibutes.GRAMARY_ATTRIBUTES)
        .add(Attributes.JUMP_STRENGTH.value(), Attributes.MAX_HEALTH.value(), Attributes.MOVEMENT_SPEED.value());
    this.tag(RootsTags.Atrtibutes.AUGMENTABLE)
        .add(Attributes.MAX_HEALTH.value(), Attributes.ATTACK_DAMAGE.value(), Attributes.JUMP_STRENGTH.value(), Attributes.MOVEMENT_SPEED.value());
  }

  @Override
  public String getName() {
    return "Roots Attribute Tags";
  }
}
