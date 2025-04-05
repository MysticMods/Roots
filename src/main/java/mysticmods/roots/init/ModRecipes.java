package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.knife.KnifeRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.pyre.SummonCreaturesRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
  // TODO: Do we need ones for dynamic bark, off-hand knife?
  private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, RootsAPI.MODID);
  public static DeferredHolder<RecipeType<?>, RecipeType<PyreRecipe>> PYRE = TYPES.register("pyre", () -> RecipeType.simple(RootsAPI.rl("pyre")));
  public static DeferredHolder<RecipeType<?>, RecipeType<SummonCreaturesRecipe>> SUMMON_CREATURES = TYPES.register("summon_creatures", () -> RecipeType.simple(RootsAPI.rl("summon_creatures")));
  public static DeferredHolder<RecipeType<?>, RecipeType<MortarRecipe>> MORTAR = TYPES.register("mortar", () -> RecipeType.simple(RootsAPI.rl("mortar")));
  public static DeferredHolder<RecipeType<?>, RecipeType<GroveRecipe>> GROVE = TYPES.register("grove", () -> RecipeType.simple(RootsAPI.rl("grove")));
  public static DeferredHolder<RecipeType<?>, RecipeType<KnifeRecipe>> KNIFE = TYPES.register("knife", () -> RecipeType.simple(RootsAPI.rl("knife")));
  public static DeferredHolder<RecipeType<?>, RecipeType<RunicBlockRecipe>> RUNIC_BLOCK = TYPES.register("runic_block", () -> RecipeType.simple(RootsAPI.rl("runic_block")));
  public static DeferredHolder<RecipeType<?>, RecipeType<RunicEntityRecipe>> RUNIC_ENTITY = TYPES.register("runic_entity", () -> RecipeType.simple(RootsAPI.rl("runic_entity")));

  public static void register(IEventBus bus) {
    TYPES.register(bus);
  }
}
