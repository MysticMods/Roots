package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.concurrent.CompletableFuture;

public final class RootsAttributeTagsProvider extends IntrinsicHolderTagsProvider<Attribute> {

  public RootsAttributeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @org.jetbrains.annotations.Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
    super(output, Registries.ATTRIBUTE, provider, p_256665_ -> BuiltInRegistries.ATTRIBUTE.getResourceKey(p_256665_)
        .orElseThrow(), RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RootsTags.Attributes.GRAMARY_ATTRIBUTES)
        .add(net.minecraft.world.entity.ai.attributes.Attributes.JUMP_STRENGTH.value(), net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE.value(), net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH.value(), net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED.value());
    this.tag(RootsTags.Attributes.AUGMENTABLE)
        .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH.value(), net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE.value(), net.minecraft.world.entity.ai.attributes.Attributes.JUMP_STRENGTH.value(), net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED.value());
  }

  @Override
  public String getName() {
    return "Roots Attribute Tags";
  }
}
