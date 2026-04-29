package mysticmods.roots.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.runtime.IIngredientManager;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class RootsRepairRecipes {
  private record RepairData(Ingredient repairIngredient, List<ItemStack> repairables) {
    public RepairData(Ingredient repairIngredient, ItemStack... repairables) {
      this(repairIngredient, List.of(repairables));
    }
  }

  private static Stream<RepairData> getRepairData() {
    return Stream.of(
        new RepairData(Tiers.WOOD.getRepairIngredient(), new ItemStack(ModItems.WOODEN_KNIFE.get())),
        new RepairData(RootsAPI.COPPER_TIER.getRepairIngredient(),
            new ItemStack(ModItems.COPPER_KNIFE.get()),
            new ItemStack(ModItems.COPPER_SWORD.get()),
            new ItemStack(ModItems.COPPER_PICKAXE.get()),
            new ItemStack(ModItems.COPPER_AXE.get()),
            new ItemStack(ModItems.COPPER_SHOVEL.get()),
            new ItemStack(ModItems.COPPER_HOE.get())),
        new RepairData(ModItems.COPPER_MATERIAL.value().repairIngredient().get(),
            new ItemStack(ModItems.COPPER_HELMET.get()),
            new ItemStack(ModItems.COPPER_CHESTPLATE.get()),
            new ItemStack(ModItems.COPPER_LEGGINGS.get()),
            new ItemStack(ModItems.COPPER_BOOTS.get())),
        new RepairData(ModItems.CARAPACE_MATERIAL.value().repairIngredient().get(),
            new ItemStack(ModItems.BEETLE_HELMET.get()),
            new ItemStack(ModItems.BEETLE_CHESTPLATE.get()),
            new ItemStack(ModItems.BEETLE_LEGGINGS.get()),
            new ItemStack(ModItems.BEETLE_BOOTS.get())),
        new RepairData(ModItems.ANTLER_MATERIAL.value().repairIngredient().get(),
            new ItemStack(ModItems.ANTLER_HAT.get())),
        new RepairData(Tiers.STONE.getRepairIngredient(),
            new ItemStack(ModItems.STONE_KNIFE.get())
        ),
        new RepairData(Tiers.IRON.getRepairIngredient(),
            new ItemStack(ModItems.IRON_KNIFE.get())
        ),
        new RepairData(Tiers.GOLD.getRepairIngredient(),
            new ItemStack(ModItems.GOLDEN_KNIFE.get())
        ),
        new RepairData(Tiers.DIAMOND.getRepairIngredient(),
            new ItemStack(ModItems.DIAMOND_KNIFE.get())
        ),
        new RepairData(Tiers.NETHERITE.getRepairIngredient(),
            new ItemStack(ModItems.NETHERITE_KNIFE.get())
        ),
        new RepairData(RootsAPI.SILVER_TIER.getRepairIngredient(),
            new ItemStack(ModItems.SILVER_KNIFE.get())
        )
    );
  }

  public static List<IJeiAnvilRecipe> getRootsAnvilRepairRecipes(IVanillaRecipeFactory vanillaRecipeFactory, IIngredientManager ingredientManager) {
    IIngredientHelper<ItemStack> ingredientHelper = ingredientManager.getIngredientHelper(VanillaTypes.ITEM_STACK);
    return getRepairData()
        .flatMap(repairData -> getRepairRecipes(repairData, vanillaRecipeFactory, ingredientHelper)).toList();
  }

  private static Stream<IJeiAnvilRecipe> getRepairRecipes(RepairData repairData,
                                                          IVanillaRecipeFactory vanillaRecipeFactory, IIngredientHelper<ItemStack> ingredientHelper) {
    Ingredient repairIngredient = repairData.repairIngredient();
    List<ItemStack> repairables = repairData.repairables();

    List<ItemStack> repairMaterials = List.of(repairIngredient.getItems());

    return repairables.stream()
        .mapMulti((itemStack, consumer) -> {
          String uid = getStringName(itemStack);
          String ingredientIdPath = sanitizePath(uid);
          String itemModId = ingredientHelper.getResourceLocation(itemStack).getNamespace();

          ItemStack damagedThreeQuarters = itemStack.copy();
          damagedThreeQuarters.setDamageValue(damagedThreeQuarters.getMaxDamage() * 3 / 4);
          ItemStack damagedHalf = itemStack.copy();
          damagedHalf.setDamageValue(damagedHalf.getMaxDamage() / 2);

          var damagedThreeQuartersSingletonList = List.of(damagedThreeQuarters);

          IJeiAnvilRecipe repairWithSame = vanillaRecipeFactory.createAnvilRecipe(
              damagedThreeQuartersSingletonList,
              damagedThreeQuartersSingletonList,
              List.of(damagedHalf),
              ResourceLocation.fromNamespaceAndPath(itemModId, "self_repair." + ingredientIdPath)
          );
          consumer.accept(repairWithSame);

          if (!repairMaterials.isEmpty()) {
            ItemStack damagedFully = itemStack.copy();
            damagedFully.setDamageValue(damagedFully.getMaxDamage());
            IJeiAnvilRecipe repairWithMaterial = vanillaRecipeFactory.createAnvilRecipe(
                List.of(damagedFully),
                repairMaterials,
                damagedThreeQuartersSingletonList,
                ResourceLocation.fromNamespaceAndPath(itemModId, "materials_repair." + ingredientIdPath)
            );
            consumer.accept(repairWithMaterial);
          }
        });
  }

  public static String getStringName(ItemStack itemStack) {
    ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(itemStack);
    if (enchantments.isEmpty()) {
      return "";
    }
    List<String> strings = new ArrayList<>();
    for (Holder<Enchantment> e : enchantments.keySet()) {
      ResourceKey<Enchantment> enchantmentResourceKey = e.getKey();
      if (enchantmentResourceKey != null) {
        String s = enchantmentResourceKey.location() + ".lvl" + enchantments.getLevel(e);
        strings.add(s);
      }
    }

    StringJoiner joiner = new StringJoiner(",", "[", "]");
    strings.sort(null);
    for (String s : strings) {
      joiner.add(s);
    }
    return joiner.toString();
  }

  public static String sanitizePath(String path) {
    char[] charArray = path.toCharArray();
    boolean valid = true;
    for (int i = 0; i < charArray.length; i++) {
      char c = charArray[i];
      if (!ResourceLocation.validPathChar(c)) {
        charArray[i] = '.';
        valid = false;
      }
    }
    if (valid) {
      return path;
    }
    return new String(charArray);
  }
}
