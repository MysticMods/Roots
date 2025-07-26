package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.IPlayerCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.categories.ingredient.RootsEntityType;
import mysticmods.roots.integration.jei.categories.widget.CooldownWidget;
import mysticmods.roots.integration.jei.categories.widget.DurabilityWidget;
import mysticmods.roots.integration.jei.categories.widget.LevelConditionWidget;
import mysticmods.roots.integration.jei.categories.widget.PlayerConditionWidget;
import mysticmods.roots.integration.jei.fake.AnimalHarvestRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import mysticmods.roots.util.LootTableUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.ArrayList;
import java.util.List;

public class AnimalHarvestCategory extends RootsRecipeBaseCategory<AnimalHarvestRecipe> {
  public AnimalHarvestCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.ANIMAL_HARVEST_RECIPE_TYPE, helper, 166, 124, RootsAPI.rl("textures/gui/jei/runic_entity.png"), () -> new ItemStack(ModItems.RUNIC_SHEARS.get()), Component.translatable("roots.jei.runic_entity"));
  }

  private final List<ChanceOutput> chanceOutputs = new ArrayList<>();

  @Override
  public List<ChanceOutput> getChanceOutputs(AnimalHarvestRecipe recipe) {
    return chanceOutputs;
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, AnimalHarvestRecipe recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    HolderLookup.Provider provider = Minecraft.getInstance().level.registryAccess();

    var collector = builder.addInvisibleIngredients(RecipeIngredientRole.INPUT);

      SpawnEggItem inputItem = DeferredSpawnEggItem.byId(recipe.entity());
      collector.addIngredients(Ingredient.of(inputItem));

    List<RootsEntityType> types = List.of(new RootsEntityType(recipe.entity()));

    builder.addSlot(RecipeIngredientRole.INPUT, 12, 26)
        .setCustomRenderer(RootsJEIPlugin.ENTITY_TYPE, RootsJEIPlugin.ENTITY_RENDERER)
        .addIngredients(RootsJEIPlugin.ENTITY_TYPE, types);

    ResourceKey<LootTable> key = recipe.entity().getDefaultLootTable();

    var lootTables = provider.lookupOrThrow(Registries.LOOT_TABLE);

    LootTable mainLootTable = lootTables.get(key).orElseThrow(() -> new IllegalArgumentException("Missing loot table for entity: " + BuiltInRegistries.ENTITY_TYPE.getKey(recipe.entity()))).value();

    chanceOutputs.addAll(LootTableUtil.parseLootTable(mainLootTable, provider));

    for (LootTable extra : recipe.additionalLootTables()) {
      chanceOutputs.addAll(LootTableUtil.parseLootTable(extra, provider));
    }

    int row = 0;
    int column = 0;

    for (int i = 0; i < chanceOutputs.size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, 105 + column * 17, 2 + row * 17)
          .addItemStack(chanceOutputs.get(i).output()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, AnimalHarvestRecipe recipe, IFocusGroup focuses) {
    super.createRecipeExtras(builder, recipe, focuses);
  }
}
