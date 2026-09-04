// GENERATED FILE - DO NOT EDIT.
// Source: data/modifiers.json  ->  :generateModifiers
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

    modifier(ModModifiers.ACID_CLOUD_FIRE, Items.FLINT_AND_STEEL);
    modifier(ModModifiers.ACID_CLOUD_PEACEFUL, Items.TURTLE_EGG);
    modifier(ModModifiers.ACID_CLOUD_KNOCKBACK, Items.IRON_BOOTS);
    modifier(ModModifiers.ACID_CLOUD_SLOWNESS, Items.SOUL_SAND);
    modifier(ModModifiers.ACID_CLOUD_TEMPORAL_MORASS, Items.PURPLE_GLAZED_TERRACOTTA);
    modifier(ModModifiers.DANDELION_WINDS_DURATION_1, Items.IRON_INGOT);
    modifier(ModModifiers.DANDELION_WINDS_DURATION_2, Items.COPPER_INGOT);
    modifier(ModModifiers.DANDELION_WINDS_DURATION_3, Items.GOLD_INGOT);
    modifier(ModModifiers.DANDELION_WINDS_DURATION_4, ModItems.SILVER_INGOT.value());
    modifier(ModModifiers.DANDELION_WINDS_DURATION_5, Items.NETHERITE_INGOT);
    modifier(ModModifiers.DANDELION_WINDS_CHANCE_1, Items.OAK_LOG);
    modifier(ModModifiers.DANDELION_WINDS_CHANCE_2, Items.STONE);
    modifier(ModModifiers.DANDELION_WINDS_CHANCE_3, Items.IRON_BLOCK);
    modifier(ModModifiers.DANDELION_WINDS_CHANCE_4, Items.OBSIDIAN);
    modifier(ModModifiers.DANDELION_WINDS_VORTEX, Items.COMPASS);
    modifier(ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_1, Items.QUARTZ);
    modifier(ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_2, Items.LAPIS_LAZULI);
    modifier(ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_3, Items.AMETHYST_SHARD);
    modifier(ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_4, Items.EMERALD);
    modifier(ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_5, Items.DIAMOND);
    modifier(ModModifiers.DANDELION_WINDS_GUSTS, Items.LIGHTNING_ROD);
    modifier(ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_1, Items.QUARTZ);
    modifier(ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_2, Items.LAPIS_LAZULI);
    modifier(ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_3, Items.AMETHYST_SHARD);
    modifier(ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_4, Items.EMERALD);
    modifier(ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_5, Items.DIAMOND);
    modifier(ModModifiers.DANDELION_WINDS_STATUE, Items.IRON_CHESTPLATE);
    modifier(ModModifiers.DANDELION_WINDS_INFERNO, Items.CAMPFIRE);
    modifier(ModModifiers.GROWTH_INFUSION_TARGETED_GROWTH, Items.TARGET);
    modifier(ModModifiers.GROWTH_INFUSION_RAMPANT_GROWTH, "spells/rampant_growth");
    modifier(ModModifiers.GROWTH_INFUSION_HYDRATION, Items.WATER_BUCKET);
    modifier(ModModifiers.GROWTH_INFUSION_FERTILIZER, Items.BONE_MEAL);
    modifier(ModModifiers.SKY_SOARER_FRIENDLY_EARTH, Items.ARROW);
    modifier(ModModifiers.SKY_SOARER_AMPLIFIED_1, Items.REDSTONE);
    modifier(ModModifiers.SKY_SOARER_AMPLIFIED_2, Items.GLOWSTONE_DUST);
    modifier(ModModifiers.SKY_SOARER_SPEEDY_1, Items.ICE);
    modifier(ModModifiers.SKY_SOARER_SPEEDY_2, Items.PACKED_ICE);
    modifier(ModModifiers.SHATTER_MAGNETISM, Items.COMPASS);
    modifier(ModModifiers.SHATTER_SILK_TOUCH, Items.SLIME_BALL);
    modifier(ModModifiers.SHATTER_FORTUNE_1, Items.IRON_PICKAXE);
    modifier(ModModifiers.SHATTER_FORTUNE_2, Items.GOLDEN_PICKAXE);
    modifier(ModModifiers.SHATTER_FORTUNE_3, Items.DIAMOND_PICKAXE);
    modifier(ModModifiers.SHATTER_SMELTING, Items.FURNACE);
    modifier(ModModifiers.SHATTER_ADJUSTABLE, Items.ANVIL);
    modifier(ModModifiers.SHATTER_HEIGHT_1, "spells/shatter_height");
    modifier(ModModifiers.SHATTER_HEIGHT_2, "spells/shatter_height");
    modifier(ModModifiers.SHATTER_WIDTH_1, "spells/shatter_width");
    modifier(ModModifiers.SHATTER_WIDTH_2, "spells/shatter_width");
    modifier(ModModifiers.SHATTER_DEPTH_1, "spells/shatter_depth");
    modifier(ModModifiers.SHATTER_DEPTH_2, "spells/shatter_depth");
    modifier(ModModifiers.SYLVAN_LIGHT_COLOR, Items.BLACK_DYE);
    modifier(ModModifiers.SYLVAN_LIGHT_WHITE, Items.WHITE_DYE);
    modifier(ModModifiers.SYLVAN_LIGHT_ORANGE, Items.ORANGE_DYE);
    modifier(ModModifiers.SYLVAN_LIGHT_LIME, Items.LIME_DYE);
    modifier(ModModifiers.SYLVAN_LIGHT_PINK, Items.PINK_DYE);
    modifier(ModModifiers.SYLVAN_LIGHT_CYAN, Items.CYAN_DYE);
    modifier(ModModifiers.SYLVAN_LIGHT_DECAYING, Items.CRACKED_STONE_BRICKS);
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

  @Override
  public String getName() {
    return "Roots Modifier Model Provider";
  }
}
