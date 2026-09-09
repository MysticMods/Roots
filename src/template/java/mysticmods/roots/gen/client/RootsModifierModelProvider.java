// @HEADER@
package mysticmods.roots.gen.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ModModifiers;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class RootsModifierModelProvider extends ItemModelProvider {
  public RootsModifierModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    for (SpellModifier modifier : RootsRegistries.SPELL_MODIFIERS) {
      if (BuiltInRegistries.ITEM.get(modifier.builtInRegistryHolder().getKey().location()) == Items.AIR) {
        throw new NullPointerException("Modifier " + modifier.builtInRegistryHolder().getKey()
            .location() + " does not have an equivalent item!");
      }
    }

    // @ICONS@
  }

  public ItemModelBuilder modifier(Holder<SpellModifier> itemHolder, String location) {
    if (!location.contains(":")) {
      return modifier(itemHolder, RootsAPI.rl(location));
    } else {
      return modifier(itemHolder, ResourceLocation.parse(location));
    }
  }

  public ItemModelBuilder modifier(Holder<SpellModifier> itemHolder, ResourceLocation location) {
    if (!location.getPath().startsWith("item")) {
      location = location.withPrefix("item/");
    }
    return getBuilder(itemHolder.getKey().location().withPrefix("item/").toString())
        .parent(new ModelFile.UncheckedModelFile("item/generated"))
        .texture("layer0", location);
  }

  public ItemModelBuilder modifier(Holder<SpellModifier> itemHolder, Item icon) {
    ResourceLocation item = itemHolder.getKey().location();

    return getBuilder(item.withPrefix("item/").toString())
        .parent(getExistingFile(icon.builtInRegistryHolder().getKey().location()));
  }

  static {
    ModItems.noop();
    ModModifiers.noop();
  }

  @Override
  public String getName() {
    return "Roots Modifier Model Provider";
  }
}