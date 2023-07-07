package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.reference.Spells;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.summon.SummonCreaturesRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModRecipes {
  private static final DeferredRegister<RecipeType<?>> SERIALIZER = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, RootsAPI.MODID);
  public static RegistryObject<RecipeType<PyreRecipe>> PYRE = SERIALIZER.register("pyre", () -> new RecipeType<>() {
    @Override
    public String toString() {
      return "roots:pyre";
    }
  });
  public static RegistryObject<RecipeType<SummonCreaturesRecipe>> SUMMON_CREATURES = SERIALIZER.register("summon_creatures", () -> new RecipeType<>() {
    @Override
    public String toString() {
      return "roots:summon_creatures";
    }
  });
  public static RegistryObject<RecipeType<MortarRecipe>> MORTAR = SERIALIZER.register("mortar", () -> new RecipeType<>() {
    @Override
    public String toString() {
      return "roots:mortar";
    }
  });
  // TODO: Inline these strings into Reference
  public static RegistryObject<RecipeType<GroveRecipe>> GROVE = SERIALIZER.register("grove", () -> new RecipeType<>() {
    @Override
    public String toString() {
      return "roots:grove";
    }
  });

  static {
    REGISTRATE.addDataGenerator(ProviderType.RECIPE, (p) -> {
      PyreRecipe.builder(ModRituals.TRANSMUTATION.get()).addLevelCondition(ModConditions.RUNE_PILLAR_3_HIGH.get()).addLevelCondition(ModConditions.RUNE_PILLAR_3_HIGH.get()).addIngredient(net.minecraftforge.common.Tags.Items.COBBLESTONE).addIngredient(net.minecraftforge.common.Tags.Items.COBBLESTONE).addIngredient(net.minecraftforge.common.Tags.Items.COBBLESTONE).addIngredient(net.minecraftforge.common.Tags.Items.COBBLESTONE).addIngredient(net.minecraftforge.common.Tags.Items.COBBLESTONE).save(p, new ResourceLocation(RootsAPI.MODID, "ritual/transmutation"));
      PyreRecipe.builder(ModItems.MOONGLOW.get(), 1).addIngredient(ItemTags.LEAVES).addIngredient(net.minecraftforge.common.Tags.Items.GLASS).addIngredient(net.minecraftforge.common.Tags.Items.GEMS_QUARTZ).addIngredient(RootsTags.Items.BIRCH_BARK).addIngredient(RootsTags.Items.BIRCH_BARK).save(p, new ResourceLocation(RootsAPI.MODID, "moonglow"));
      PyreRecipe.builder(ModRituals.ANIMAL_HARVEST.get()).addIngredient(RootsTags.Items.WILDEWHEET_CROP).addIngredient(ItemTags.WOOL).addIngredient(net.minecraftforge.common.Tags.Items.CROPS_CARROT).addIngredient(net.minecraftforge.common.Tags.Items.SLIMEBALLS).addIngredient(RootsTags.Items.WILDROOT_CROP).save(p, new ResourceLocation(RootsAPI.MODID, "ritual/animal_harvest"));
      MortarRecipe.builder(4).addIngredient(net.minecraftforge.common.Tags.Items.SLIMEBALLS).addIngredient(RootsTags.Items.WILDROOT_CROP).addGrant(new Grant(Grant.Type.SPELL, Spells.GROWTH_INFUSION.location())).save(p, new ResourceLocation(RootsAPI.MODID, "spell/growth_infusion"));
      GroveRecipe.builder(new ItemStack(ModItems.GLASS_EYE.get())).addIngredient(Tags.Items.GLASS).addIngredient(Tags.Items.GLASS).addIngredient(Tags.Items.GLASS).addIngredient(Tags.Items.DUSTS_GLOWSTONE).save(p, new ResourceLocation(RootsAPI.MODID, "grove/glass_eye"));
    });
  }

  public static void register(IEventBus bus) {
    SERIALIZER.register(bus);
  }

  public static void load() {
  }
}
