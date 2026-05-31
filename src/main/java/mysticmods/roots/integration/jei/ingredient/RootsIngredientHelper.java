package mysticmods.roots.integration.jei.ingredient;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.ingredient.block.SimpleBlockType;
import mysticmods.roots.integration.jei.ingredient.damage.RootsDamageType;
import mysticmods.roots.integration.jei.ingredient.dimension.RootsDimensionType;
import mysticmods.roots.integration.jei.ingredient.entity.RootsEntityType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class RootsIngredientHelper {
  public static List<Spell> spellIngredient(TagKey<Spell> tag) {
    return RootsRegistries.SPELLS.getTag(tag).map(HolderSet.ListBacked::stream).orElse(Stream.empty())
        .map(Holder::value).toList();
  }

  public static List<Ritual> ritualIngredient(TagKey<Ritual> tag) {
    return RootsRegistries.RITUALS.getTag(tag).map(HolderSet.ListBacked::stream).orElse(Stream.empty())
        .map(Holder::value).toList();
  }

  public static IRecipeSlotBuilder fillSubEntrySlot(IIngredientAcceptor<?> invis, IRecipeSlotBuilder builder, GroveReputationEntry.SubEntryType type, ResourceLocation location) {
    switch (type) {
      case BLOCK, OLD_BLOCK ->
          builder.addIngredients(RootsJEIPlugin.BLOCK_TYPE, SimpleBlockType.fromTag(TagKey.create(Registries.BLOCK, location)))
              .setCustomRenderer(RootsJEIPlugin.BLOCK_TYPE, RootsJEIPlugin.BLOCK_RENDERER);
      case ITEM, OLD_ITEM -> builder.addIngredients(Ingredient.of(TagKey.create(Registries.ITEM, location)));
      case SPELL -> {
        invis.addIngredients(Ingredient.of(TagKey.create(Registries.ITEM, location)));
        builder.addIngredients(RootsJEIPlugin.SPELL_TYPE, spellIngredient(TagKey.create(RootsRegistries.Keys.SPELLS, location)))
            .setCustomRenderer(RootsJEIPlugin.SPELL_TYPE, RootsJEIPlugin.SPELL_RENDERER);
      }
      case RITUAL -> {
        invis.addIngredients(Ingredient.of(TagKey.create(Registries.ITEM, location)));
        builder.addIngredients(RootsJEIPlugin.RITUAL_TYPE, ritualIngredient(TagKey.create(RootsRegistries.Keys.RITUALS, location)))
            .setCustomRenderer(RootsJEIPlugin.RITUAL_TYPE, RootsJEIPlugin.RITUAL_RENDERER);
      }
      case EXACT_RITUAL -> {
        Ritual ritual = RootsRegistries.RITUALS.get(location);
        if (ritual != null) {
          invis.addItemStack(ritual.getIcon());
          builder.addIngredient(RootsJEIPlugin.RITUAL_TYPE, ritual)
              .setCustomRenderer(RootsJEIPlugin.RITUAL_TYPE, RootsJEIPlugin.RITUAL_RENDERER);
        }
      }
      case EXACT_SPELL -> {
        Spell spell = RootsRegistries.SPELLS.get(location);
        if (spell != null) {
          invis.addItemStack(spell.getStaffIcon());
          builder.addIngredient(RootsJEIPlugin.SPELL_TYPE, spell)
              .setCustomRenderer(RootsJEIPlugin.SPELL_TYPE, RootsJEIPlugin.SPELL_RENDERER);
        }
      }
      case EXACT_ITEM -> {
        Item item = BuiltInRegistries.ITEM.get(location);

        //noinspection ConstantValue
        if (item != null && item != Items.AIR) {
          builder.addItemStack(new ItemStack(item));
        }
      }
      case RECIPE -> {
        // Check if there's a recipe with that id
        Optional<RecipeHolder<?>> recipe = Minecraft.getInstance().player.connection.getRecipeManager().byKey(location);
        if (recipe.isPresent()) {
          ItemStack item = recipe.get().value().getResultItem(Minecraft.getInstance().getConnection().registryAccess());

          if (!item.isEmpty()) {
            builder.addItemStack(item);
          }
        } else {
          // Fallback to item
          Item item = BuiltInRegistries.ITEM.get(location);

          //noinspection ConstantValue
          if (item != null && item != Items.AIR) {
            builder.addItemStack(new ItemStack(item));
          }
        }
      }
      case TARGET_ENTITY, SECONDARY_ENTITY, TERTIARY_ENTITY ->
          builder.addIngredients(RootsJEIPlugin.ENTITY_TYPE, RootsEntityType.fromTag(TagKey.create(Registries.ENTITY_TYPE, location)))
              .setCustomRenderer(RootsJEIPlugin.ENTITY_TYPE, RootsJEIPlugin.SMALL_ENTITY_RENDERER);
      case DIMENSION -> {
        RootsDimensionType dim = new RootsDimensionType(location);
        if (!dim.icon().isEmpty()) {
          invis.addItemStack(dim.icon());

          builder.addIngredient(RootsJEIPlugin.DIMENSION_TYPE, dim)
              .setCustomRenderer(RootsJEIPlugin.DIMENSION_TYPE, RootsJEIPlugin.DIMENSION_RENDERER);
        } else {
          RootsAPI.LOG.error("Could not find icon for dimension {}", location);
        }
      }
      case DAMAGE -> {
        var damRegistry = Minecraft.getInstance().player.connection.registryAccess().registry(Registries.DAMAGE_TYPE);
        if (damRegistry.isPresent()) {
          Holder<DamageType> damType = damRegistry.get().getHolder(location).orElse(null);
          if (damType != null) {
            List<RootsDamageType> types = RootsDamageType.fromTag(TagKey.create(Registries.DAMAGE_TYPE, location));
            List<ItemStack> icons = types.stream().map(RootsDamageType::icon).toList();
            invis.addItemStacks(icons);
            builder.addIngredients(RootsJEIPlugin.DAMAGE_TYPE, types)
                .setCustomRenderer(RootsJEIPlugin.DAMAGE_TYPE, RootsJEIPlugin.DAMAGE_RENDERER);
          }
        }

      }
      case SPELL_MODIFIER, RITUAL_MODIFIER -> {
        // These do nothing atm
      }
      case ALWAYS -> {
      }
    }
    return builder;
  }

  public static IRecipeSlotBuilder subEntrySlot(IRecipeLayoutBuilder recipeBuilder, RecipeIngredientRole role, int x, int y, GroveReputationEntry.SubEntry entry) {
    ResourceLocation location = entry.name();
    GroveReputationEntry.SubEntryType type = entry.type();
    IRecipeSlotBuilder builder = recipeBuilder.addSlot(role, x, y);
    var invis = recipeBuilder.addInvisibleIngredients(role);
    return fillSubEntrySlot(invis, builder, type, location);
  }
}
