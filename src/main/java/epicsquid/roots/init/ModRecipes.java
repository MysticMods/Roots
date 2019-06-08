package epicsquid.roots.init;

import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import epicsquid.mysticalworld.entity.EntityBeetle;
import epicsquid.mysticalworld.entity.EntityDeer;
import epicsquid.mysticalworld.entity.EntityFox;
import epicsquid.mysticalworld.entity.EntityFrog;
import epicsquid.mysticalworld.materials.Metal;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.recipe.*;
import epicsquid.roots.recipe.ingredient.GoldOrSilverIngotIngredient;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.util.StateUtil;
import epicsquid.roots.util.types.WorldPosStatePredicate;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ModRecipes {

  private static Map<ResourceLocation, AnimalHarvestRecipe> harvestRecipes = new HashMap<>();
  private static ObjectOpenHashSet<Class<? extends Entity>> harvestClasses = null;
  private static Map<ResourceLocation, TransmutationRecipe> transmutationRecipes = new HashMap<>();
  private static List<MortarRecipe> mortarRecipes = new ArrayList<>();
  private static Map<String, PyreCraftingRecipe> pyreCraftingRecipes = new HashMap<>();
  private static Map<ResourceLocation, GroveCraftingRecipe> groveCraftingRecipes = new HashMap<>();
  private static Map<String, RunicShearRecipe> runicShearRecipes = new HashMap<>();
  private static Map<Class<? extends EntityLivingBase>, RunicShearRecipe> runicShearEntityRecipes = new HashMap<>();
  private static List<RunicCarvingRecipe> runicCarvingRecipes = new ArrayList<>();
  private static Map<ResourceLocation, PacifistEntry> pacifistEntities = new HashMap<>();
  private static Map<Class<? extends Entity>, PacifistEntry> pacifistClasses = new HashMap<>();
  private static Map<ResourceLocation, BarkRecipe> barkRecipes = new HashMap<>();

  /*
  TODO: DON'T USE THESE >:0

  private static ResourceLocation getRL(@Nonnull String s) {
    return new ResourceLocation(Roots.MODID + ":" + s);
  }

  private static void registerShapeless(@Nonnull IForgeRegistry<IRecipe> registry, @Nonnull String name, @Nonnull ItemStack result, Object... ingredients) {
    registry.register(new ShapelessOreRecipe(getRL(name), result, ingredients).setRegistryName(getRL(name)));
  }

  private static void registerShaped(@Nonnull IForgeRegistry<IRecipe> registry, @Nonnull String name, @Nonnull ItemStack result, Object... ingredients) {
    registry.register(new ShapedOreRecipe(getRL(name), result, ingredients).setRegistryName(getRL(name)));
  }*/

  public static PacifistEntry addPacifistEntry(String name, Class<? extends Entity> clazz) {
    PacifistEntry entry = new PacifistEntry(clazz, name);
    pacifistEntities.put(entry.getRegistryName(), entry);
    pacifistClasses.put(clazz, entry);
    return entry;
  }

  public static PacifistEntry getPacifistEntry(String name) {
    return getPacifistEntry(new ResourceLocation(Roots.MODID, name));
  }

  public static PacifistEntry getPacifistEntry(ResourceLocation name) {
    return pacifistEntities.get(name);
  }

  public static PacifistEntry getPacifistEntry(Entity entity) {
    return pacifistClasses.get(entity.getClass());
  }

  public static void removePacifistEntry(String name) {
    removePacifistEntry(new ResourceLocation(Roots.MODID, name));
  }

  public static void removePacifistEntry(ResourceLocation name) {
    pacifistEntities.remove(name);
  }

  public static void initPacifistEntities() {
    addPacifistEntry("bat", EntityBat.class);
    addPacifistEntry("chicken", EntityChicken.class);
    addPacifistEntry("cow", EntityCow.class);
    addPacifistEntry("donkey", EntityDonkey.class);
    addPacifistEntry("horse", EntityHorse.class);
    addPacifistEntry("llama", EntityLlama.class);
    addPacifistEntry("mooshroom", EntityMooshroom.class);
    addPacifistEntry("mule", EntityMule.class);
    addPacifistEntry("ocelot", EntityOcelot.class);
    addPacifistEntry("parrot", EntityParrot.class);
    addPacifistEntry("pig", EntityPig.class);
    addPacifistEntry("rabbit", EntityRabbit.class);
    addPacifistEntry("sheep", EntitySheep.class);
    addPacifistEntry("squid", EntitySquid.class);
    addPacifistEntry("villager", EntityVillager.class).setCheckTarget(true);
    addPacifistEntry("wolf", EntityWolf.class);

    // Mystical Worlds
    addPacifistEntry("beetle", EntityBeetle.class);
    addPacifistEntry("deer", EntityDeer.class);
    addPacifistEntry("fox", EntityFox.class);
    addPacifistEntry("frog", EntityFrog.class);
  }

  public static void addVanillaBarkRecipe (String name, BlockPlanks.EnumType type, Item item) {
    barkRecipes.put(new ResourceLocation(Roots.MODID, name), new BarkRecipe(type, item));
  }

  public static void initVanillaBarkRecipes() {
    addVanillaBarkRecipe("oak", BlockPlanks.EnumType.OAK, ModItems.bark_oak);
    addVanillaBarkRecipe("spruce", BlockPlanks.EnumType.SPRUCE, ModItems.bark_spruce);
    addVanillaBarkRecipe("birch", BlockPlanks.EnumType.BIRCH, ModItems.bark_birch);
    addVanillaBarkRecipe("jungle", BlockPlanks.EnumType.JUNGLE, ModItems.bark_jungle);
    addVanillaBarkRecipe("acacia", BlockPlanks.EnumType.ACACIA, ModItems.bark_acacia);
    addVanillaBarkRecipe("dark_oak", BlockPlanks.EnumType.DARK_OAK, ModItems.bark_dark_oak);
  }

  public static void initModdedBarkRecipes () {
    addModdedBarkRecipe("wildwood", ModBlocks.wildwood_log, ModItems.bark_wildwood);
  }

  public static void addModdedBarkRecipe (String name, Block block, Item item) {
    barkRecipes.put(new ResourceLocation(Roots.MODID, name), new BarkRecipe(block, item));
  }

  @Nullable
  public static BarkRecipe getModdedBarkRecipe (Block block) {
    for (BarkRecipe recipe : barkRecipes.values()) {
      if (recipe.getBlock() == block) return recipe;
    }
    return null;
  }

  @Nullable
  public static BarkRecipe getVanillaBarkRecipe (BlockPlanks.EnumType type) {
    for (BarkRecipe recipe : barkRecipes.values()) {
      if (recipe.getType() == type) return recipe;
    }
    return null;
  }

  public static Collection<BarkRecipe> getBarkRecipes () {
    return barkRecipes.values();
  }

  public static void addTransmutationRecipe(String name, Block start, IBlockState endState, WorldPosStatePredicate condition) {
    ResourceLocation n = new ResourceLocation(Roots.MODID, name);
    TransmutationRecipe recipe = new TransmutationRecipe(n, start, endState, condition);
    transmutationRecipes.put(n, recipe);
  }

  public static void addTransmutationRecipe(String name, IBlockState start, IBlockState endState) {
    addTransmutationRecipe(name, start, endState, null);
  }

  public static void addTransmutationRecipe(String name, IBlockState start, IBlockState endState, WorldPosStatePredicate condition) {
    ResourceLocation n = new ResourceLocation(Roots.MODID, name);
    TransmutationRecipe recipe = new TransmutationRecipe(n, start, endState, condition);
    transmutationRecipes.put(n, recipe);
  }

  public static void addTransmutationRecipe(String name, IBlockState start, ItemStack endState) {
    addTransmutationRecipe(name, start, endState, null);
  }

  public static void addTransmutationRecipe(String name, IBlockState start, ItemStack endState, WorldPosStatePredicate condition) {
    ResourceLocation n = new ResourceLocation(Roots.MODID, name);
    TransmutationRecipe recipe = new TransmutationRecipe(n, start, endState, condition);
    transmutationRecipes.put(n, recipe);
  }

  public static void addTransmutationRecipe(String name, Block start, ItemStack endState) {
    addTransmutationRecipe(name, start, endState, null);
  }

  public static void addTransmutationRecipe(String name, Block start, ItemStack endState, WorldPosStatePredicate condition) {
    ResourceLocation n = new ResourceLocation(Roots.MODID, name);
    TransmutationRecipe recipe = new TransmutationRecipe(n, start, endState, condition);
    transmutationRecipes.put(n, recipe);
  }

  public static void addTransmutationRecipe(String name, Block start, IBlockState endState) {
    addTransmutationRecipe(name, start, endState, null);
  }

  public static void addTransmutationRecipe(String name, Block start, Block endState) {
    addTransmutationRecipe(name, start, endState.getDefaultState(), null);
  }

  public static void removeTransmutationRecipe(String name) {
    removeTransmutationRecipe(new ResourceLocation(Roots.MODID, name));
  }

  public static void removeTransmutationRecipe(ResourceLocation name) {
    transmutationRecipes.remove(name);
  }

  public static TransmutationRecipe getTransmutationRecipe(String name) {
    return getTransmutationRecipe(new ResourceLocation(Roots.MODID, name));
  }

  public static TransmutationRecipe getTransmutationRecipe(ResourceLocation name) {
    return transmutationRecipes.getOrDefault(name, null);
  }

  public static List<TransmutationRecipe> getTransmutationRecipes(IBlockState startState) {
    List<TransmutationRecipe> result = new ArrayList<>();
    for (TransmutationRecipe recipe : transmutationRecipes.values()) {
      if (recipe.matches(startState)) result.add(recipe);
    }
    return result;
  }

  public static List<TransmutationRecipe> getTransmutationRecipes(Block startState) {
    return getTransmutationRecipes(startState.getDefaultState());
  }

  public static void initTransmutationRecipes() {
    addTransmutationRecipe("deadbush_cocoa", Blocks.DEADBUSH, new ItemStack(Items.DYE, 3, 3));
    addTransmutationRecipe("birch_jungle", Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH), Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE));
    addTransmutationRecipe("birch_jungle_leaves", Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH), Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE));
    addTransmutationRecipe("pumpkin_melon", Blocks.PUMPKIN, Blocks.MELON_BLOCK.getDefaultState(), (t, u, v) -> {
      Block b = t.getBlockState(u.down()).getBlock();
      return b == Blocks.WATER || b == Blocks.FLOWING_WATER;
    });
    addTransmutationRecipe("pumpkin_cactus", Blocks.PUMPKIN, Blocks.CACTUS.getDefaultState(), (t, u, v) -> t.getBlockState(u.down()).getBlock() instanceof BlockSand);
    StateUtil.ignoreState(Blocks.LEAVES, BlockLeaves.CHECK_DECAY);
    StateUtil.ignoreState(Blocks.LEAVES, BlockLeaves.DECAYABLE);
  }

  public static void addAnimalHarvestRecipe(Entity entity) {
    addAnimalHarvestRecipe(EntityList.getEntityString(entity), entity);
  }

  public static void addAnimalHarvestRecipe(String name, Entity entity) {
    addAnimalHarvestRecipe(name, entity.getClass());
  }

  public static void addAnimalHarvestRecipe(String name, Class<? extends Entity> clazz) {
    ResourceLocation n = new ResourceLocation(Roots.MODID, name);
    if (harvestRecipes.containsKey(n)) {
      System.out.println("Animal Harvest recipe name is already registered: " + n.toString());
      return;
    }
    AnimalHarvestRecipe recipe = new AnimalHarvestRecipe(n, clazz);
    harvestRecipes.put(n, recipe);
    harvestClasses = null;
  }

  public static void removeAnimalHarvestRecipe(ResourceLocation location) {
    harvestClasses = null;
    harvestRecipes.remove(location);
  }

  public static void removeAnimalHarvestRecipe(Class<? extends Entity> clz) {
    harvestClasses = null;
    ResourceLocation toRemove = null;
    for (Map.Entry<ResourceLocation, AnimalHarvestRecipe> entry : harvestRecipes.entrySet()) {
      AnimalHarvestRecipe recipe = entry.getValue();
      if (recipe.getHarvestClass().equals(clz)) {
        toRemove = entry.getKey();
      }
    }
    if (toRemove != null) {
      harvestRecipes.remove(toRemove);
    }
  }

  public static void removeAnimalHarvestRecipe(Entity entity) {
    ResourceLocation n = new ResourceLocation(Roots.MODID, entity.getName());
    if (harvestRecipes.containsKey(n)) {
      harvestRecipes.remove(n);
      harvestClasses = null;
    } else {
      n = null;
      for (Map.Entry<ResourceLocation, AnimalHarvestRecipe> entry : harvestRecipes.entrySet()) {
        if (entry.getValue().matches(entity)) {
          n = entry.getKey();
          break;
        }
      }
      if (n != null) {
        harvestRecipes.remove(n);
        harvestClasses = null;
      }
    }
  }

  public static void getAnimalHarvestRecipe(ResourceLocation location) {
    harvestRecipes.getOrDefault(location, null);
  }

  public static AnimalHarvestRecipe getAnimalHarvestRecipe(Entity entity) {
    ResourceLocation n = new ResourceLocation(Roots.MODID, entity.getName());
    if (harvestRecipes.containsKey(n)) {
      return harvestRecipes.get(n);
    } else {
      for (AnimalHarvestRecipe recipe : harvestRecipes.values()) {
        if (recipe.matches(entity)) return recipe;
      }
    }
    return null;
  }

  public static ObjectOpenHashSet<Class<? extends Entity>> getAnimalHarvestClasses() {
    if (harvestClasses == null || harvestClasses.size() != harvestRecipes.size()) {
      harvestClasses = new ObjectOpenHashSet<>();
      for (AnimalHarvestRecipe recipe : harvestRecipes.values()) {
        harvestClasses.add(recipe.getHarvestClass());
      }
    }
    return harvestClasses;
  }


  public static void initAnimalHarvestRecipes() {
    // Vanilla
    addAnimalHarvestRecipe("bat", EntityBat.class);
    addAnimalHarvestRecipe("chicken", EntityChicken.class);
    addAnimalHarvestRecipe("cow", EntityCow.class);
    addAnimalHarvestRecipe("donkey", EntityDonkey.class);
    addAnimalHarvestRecipe("horse", EntityHorse.class);
    addAnimalHarvestRecipe("llama", EntityLlama.class);
    addAnimalHarvestRecipe("mooshroom", EntityMooshroom.class);
    addAnimalHarvestRecipe("mule", EntityMule.class);
    addAnimalHarvestRecipe("ocelot", EntityOcelot.class);
    addAnimalHarvestRecipe("parrot", EntityParrot.class);
    addAnimalHarvestRecipe("pig", EntityPig.class);
    addAnimalHarvestRecipe("rabbit", EntityRabbit.class);
    addAnimalHarvestRecipe("sheep", EntitySheep.class);
    addAnimalHarvestRecipe("squid", EntitySquid.class);
    addAnimalHarvestRecipe("wolf", EntityWolf.class);
    // No villager or skeletal/zombie horses.
    // Mystical World
    addAnimalHarvestRecipe("beetle", EntityBeetle.class);
    addAnimalHarvestRecipe("deer", EntityDeer.class);
    addAnimalHarvestRecipe("fox", EntityFox.class);
    addAnimalHarvestRecipe("frog", EntityFrog.class);
  }

  public static void addRunicCarvingRecipe(RunicCarvingRecipe recipe) {
    for (RunicCarvingRecipe runicCarvingRecipe : runicCarvingRecipes) {
      if (runicCarvingRecipe.matches(recipe)) {
        System.out.println("Recipe is already registered with carving block - " + recipe.getCarvingBlock() + ", rune block - " + recipe.getRuneBlock() + ", herb - " + recipe.getHerb().getItem());
        return;
      }
    }
    runicCarvingRecipes.add(recipe);
  }

  public static RunicCarvingRecipe getRunicCarvingRecipe(IBlockState carvingBlock, Herb herb) {
    if (carvingBlock != null && herb != null) {
      for (RunicCarvingRecipe recipe : runicCarvingRecipes) {
        if (recipe.getHerb().equals(herb) && recipe.getCarvingBlock().equals(carvingBlock)) {
          return recipe;
        }
      }
    }
    return null;
  }

  public static RunicCarvingRecipe getRunicCarvingRecipe(String name) {
    if (name != null) {
      for (RunicCarvingRecipe recipe : runicCarvingRecipes) {
        if (recipe.getName().equals(name)) {
          return recipe;
        }
      }
    }
    return null;
  }

  public static void addRunicShearRecipe(RunicShearRecipe recipe) {
    for (RunicShearRecipe runicShearRecipe : runicShearRecipes.values()) {
      if (recipe.isBlockRecipe() && recipe.getBlock() == runicShearRecipe.getBlock()) {
        System.out.println("Recipe is already registered with block - " + recipe.getBlock().getTranslationKey());
        return;
      } else if (recipe.isEntityRecipe() && recipe.getClazz() == runicShearRecipe.getClazz()) {
        System.out.println("Recipe is already registered with entity - " + recipe.getClazz().getName());
        return;
      }
    }
    if (recipe.isEntityRecipe()) {
      runicShearEntityRecipes.put(recipe.getClazz(), recipe);
    } else {
      runicShearRecipes.put(recipe.getName(), recipe);
    }
  }

  public static RunicShearRecipe getRunicShearRecipe(Block block) {
    for (RunicShearRecipe recipe : runicShearRecipes.values()) {
      if (recipe.isBlockRecipe() && recipe.getBlock() == block) {
        return recipe;
      }
    }
    return null;
  }

  public static RunicShearRecipe getRunicShearRecipe(EntityLivingBase entity) {
    return runicShearEntityRecipes.get(entity.getClass());
  }

  public static RunicShearRecipe getRunicShearRecipe(String name) {
    for (RunicShearRecipe recipe : runicShearRecipes.values()) {
      if (recipe.getName().equals(name)) {
        return recipe;
      }
    }
    return null;
  }

  public static MortarRecipe getMortarRecipe(List<ItemStack> items) {
    for (MortarRecipe mortarRecipe : mortarRecipes) {
      if (mortarRecipe.matches(items)) {
        return mortarRecipe;
      }
    }
    return null;
  }

  public static MortarRecipe getMortarRecipe(ItemStack output) {
    for (MortarRecipe mortarRecipe : mortarRecipes) {
      if (mortarRecipe.getResult().isItemEqual(output)) {
        return mortarRecipe;
      }
    }
    return null;
  }

  public static MortarRecipe getMortarRecipe(String name, int meta) {
    ResourceLocation item = new ResourceLocation(name);
    for (MortarRecipe mortarRecipe : mortarRecipes) {
      ItemStack output = mortarRecipe.getResult();
      if (output.getItem().getRegistryName().equals(item) && output.getMetadata() == meta) {
        return mortarRecipe;
      }
    }

    return null;
  }

  private static void addMortarRecipe(ItemStack output, Ingredient input, float red1, float green1, float blue1, float red2, float green2, float blue2) {
    mortarRecipes.addAll(getMortarRecipeList(output, input, red1, green1, blue1, red2, green2, blue2));
  }

  public static List<MortarRecipe> getMortarRecipeList(ItemStack output, Ingredient input) {
    return getMortarRecipeList(output, input, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
  }

  public static List<MortarRecipe> getMortarRecipeList(ItemStack output, Ingredient input, float red1, float green1, float blue1, float red2, float green2, float blue2) {
    List<MortarRecipe> result = new ArrayList<>();
    ItemStack copy;
    List<Ingredient> ingredients = new ArrayList<>();
    int count = output.getCount();
    for (int i = 0; i < 5; i++) {
      ingredients.add(input);
      copy = output.copy();
      copy.setCount((i + 1) * count);
      MortarRecipe recipe = new MortarRecipe(copy, ingredients.toArray(new Ingredient[0]), red1, green1, blue1, red2, green2, blue2);
      result.add(recipe);
    }
    return result;
  }

  public static SpellBase getSpellRecipe(String name) {
    return SpellRegistry.getSpell(name);
  }

  public static SpellBase getSpellRecipe(List<ItemStack> items) {
    for (SpellBase spell : SpellRegistry.spellRegistry.values()) {
      if (spell.matchesIngredients(items)) {
        return spell;
      }
    }
    return null;
  }

  public static PyreCraftingRecipe getCraftingRecipe(List<ItemStack> items) {
    items.removeIf(ItemStack::isEmpty);
    for (Map.Entry<String, PyreCraftingRecipe> pyreCraftingRecipe : pyreCraftingRecipes.entrySet()) {
      if (pyreCraftingRecipe.getValue().matches(items)) {
        return pyreCraftingRecipe.getValue();
      }
    }
    return null;
  }

  public static Map<ResourceLocation, GroveCraftingRecipe> getGroveCraftingRecipes() {
    return groveCraftingRecipes;
  }

  @Nullable
  public static GroveCraftingRecipe getGroveCraftingRecipe(List<ItemStack> items) {
    for (GroveCraftingRecipe recipe : groveCraftingRecipes.values()) {
      if (recipe.matches(items)) return recipe;
    }

    return null;
  }

  public static GroveCraftingRecipe getGroveCraftingRecipe(String name) {
    return getGroveCraftingRecipe(new ResourceLocation(Roots.MODID, name));
  }

  @Nullable
  public static GroveCraftingRecipe getGroveCraftingRecipe(ResourceLocation name) {
    return groveCraftingRecipes.get(name);
  }

  public static PyreCraftingRecipe getCraftingRecipe(String recipeName) {
    return pyreCraftingRecipes.get(recipeName);
  }

  private static void addCraftingRecipe(String recipeName, PyreCraftingRecipe pyreCraftingRecipe) {
    for (Map.Entry<String, PyreCraftingRecipe> pyreCraftingRecipeEntry : pyreCraftingRecipes.entrySet()) {
      if (pyreCraftingRecipeEntry.getValue().matches(pyreCraftingRecipe.getRecipe())) {
        System.out.println("A Crafting Recipe is already registered");
        return;
      }
    }
    pyreCraftingRecipes.put(recipeName, pyreCraftingRecipe.setName(recipeName));
  }

  private static void addCraftingRecipe(String recipeName, GroveCraftingRecipe recipe) {
    ResourceLocation recipeId = new ResourceLocation(Roots.MODID, recipeName);
    if (groveCraftingRecipes.containsKey(recipeId)) {
      System.out.println("GroveCraftingRecipe already registered with name " + recipeName);
      return;
    }

    groveCraftingRecipes.put(recipeId, recipe.setName(recipeName));
  }

  public static List<RunicCarvingRecipe> getRunicCarvingRecipes() {
    return runicCarvingRecipes;
  }

  public static Map<String, RunicShearRecipe> getRunicShearRecipes() {
    return runicShearRecipes;
  }

  public static Map<String, PyreCraftingRecipe> getPyreCraftingRecipes() {
    return pyreCraftingRecipes;
  }

  public static Map<ResourceLocation, AnimalHarvestRecipe> getHarvestRecipes() {
    return harvestRecipes;
  }

  public static List<MortarRecipe> getMortarRecipes() {
    return mortarRecipes;
  }

  public static void initMortarRecipes() {
    addMortarRecipe(new ItemStack(Items.DYE, 1, 12), Ingredient.fromItem(epicsquid.mysticalworld.init.ModItems.carapace), 1, 1, 1, 1, 1, 1);
    addMortarRecipe(new ItemStack(ModItems.flour), Ingredient.fromItem(Items.WHEAT), 1f, 1f, 0f, 1f, 1f, 0f);
    addMortarRecipe(new ItemStack(ModItems.flour), Ingredient.fromItem(Items.POTATO), 1f, 1f, 0, 1f, 1f, 0f);
    addMortarRecipe(new ItemStack(Items.DYE, 4, 15), Ingredient.fromItem(Items.BONE), 0f, 0f, 0f, 0f, 0f, 0f);
    addMortarRecipe(new ItemStack(Items.SUGAR, 2), new OreIngredient("sugarcane"), 0f, 0f, 0f, 1f, 1f, 1f);

    for (Metal metal : Metal.values()) {
      if (!metal.isEnabled()) continue;

      Item metalDust = metal.getDust();
      if (metalDust == null) {
        continue;
      }

      addMortarRecipe(new ItemStack(metalDust), new OreIngredient("ingot" + metal.getOredictNameSuffix()), 82f / 255f, 92f / 255f, 114f / 255f, 160f / 255f, 167f / 255f, 183f / 255f);
    }

    addMortarRecipe(new ItemStack(epicsquid.mysticalworld.init.ModItems.gold_dust), new OreIngredient("ingotGold"), 82f / 255f, 92f / 255f, 114f / 255f, 160f / 255f, 167f / 255f, 183f / 255f);
    addMortarRecipe(new ItemStack(epicsquid.mysticalworld.init.ModItems.iron_dust), new OreIngredient("ingotIron"), 82f / 255f, 92f / 255f, 114f / 255f, 160f / 255f, 167f / 255f, 183f / 255f);

    addMortarRecipe(new ItemStack(ModItems.petals), new OreIngredient("allFlowers"), 1f, 0f, 0f, 0f, 1f, 0f);
    addMortarRecipe(new ItemStack(ModItems.runic_dust), new OreIngredient("runestone"), 0f, 0f, 1f, 60 / 255f, 0f, 1f);
  }

  /**
   * Register all recipes
   */
  public static void initRecipes(@Nonnull RegisterModRecipesEvent event) {
    initDrops();

    initMortarRecipes();
    initAnimalHarvestRecipes();
    initTransmutationRecipes();
    initPacifistEntities();
    initVanillaBarkRecipes();
    initModdedBarkRecipes();

    GameRegistry.addSmelting(ModItems.flour, new ItemStack(Items.BREAD), 0.125f);
    GameRegistry.addSmelting(epicsquid.mysticalworld.init.ModItems.iron_dust, new ItemStack(Items.IRON_INGOT), 0.125f);
    GameRegistry.addSmelting(epicsquid.mysticalworld.init.ModItems.gold_dust, new ItemStack(Items.GOLD_INGOT), 0.125f);
    GameRegistry.addSmelting(ModItems.aubergine, new ItemStack(ModItems.cooked_aubergine), 0.125f);
    GameRegistry.addSmelting(ModItems.seeds, new ItemStack(ModItems.cooked_seeds), 0.05f);
    GameRegistry.addSmelting(ModItems.pereskia_bulb, new ItemStack(ModItems.cooked_pereskia), 0.125f);

    initCraftingRecipes();
    RunicShearRecipes.initRecipes();

  }

  private static void initCraftingRecipes() {
    addCraftingRecipe("infernal_bulb",
        new PyreCraftingRecipe(new ItemStack(ModItems.infernal_bulb, 3), 1).addIngredients(
            new ItemStack(Items.NETHER_WART),
            new ItemStack(ItemBlock.getItemFromBlock(Blocks.MAGMA)),
            new ItemStack(Items.BLAZE_ROD),
            new ItemStack(ModItems.wildroot),
            new ItemStack(Items.GUNPOWDER)));

    addCraftingRecipe("dewgonia",
        new PyreCraftingRecipe(new ItemStack(ModItems.dewgonia, 3), 1).addIngredients(
            new ItemStack(Item.getItemFromBlock(Blocks.TALLGRASS), 1, 1),
            new ItemStack(Items.SUGAR),
            new ItemStack(Items.DYE, 1, 4),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(Item.getItemFromBlock(Blocks.WATERLILY))));

    addCraftingRecipe("cloud_berry",
        new PyreCraftingRecipe(new ItemStack(ModItems.cloud_berry, 3), 1).addIngredients(
            new ItemStack(ItemBlock.getItemFromBlock(Blocks.LEAVES)),
            new ItemStack(Item.getItemFromBlock(Blocks.TALLGRASS), 1, 1),
            new OreIngredient("blockWool"),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.terra_moss)));

    addCraftingRecipe("stalicripe",
        new PyreCraftingRecipe(new ItemStack(ModItems.stalicripe, 3), 1).addIngredients(
            new ItemStack(Items.FLINT),
            new OreIngredient("stone"),
            new OreIngredient("ingotIron"),
            new ItemStack(ModItems.wildroot),
            new OreIngredient("dustRedstone")));

    addCraftingRecipe("moonglow_leaf",
        new PyreCraftingRecipe(new ItemStack(ModItems.moonglow_leaf, 3), 1).addIngredients(
            new OreIngredient("treeLeaves"),
            new OreIngredient("blockGlass"),
            new OreIngredient("gemQuartz"),
            new ItemStack(ModItems.bark_birch),
            new ItemStack(ModItems.bark_birch)));

    addCraftingRecipe("pereskia",
        new PyreCraftingRecipe(new ItemStack(ModItems.pereskia, 3), 1).addIngredients(
            new ItemStack(ModItems.wildroot),
            new ItemStack(Items.SPECKLED_MELON),
            new OreIngredient("dustRedstone"),
            new ItemStack(Items.BEETROOT),
            new ItemStack(Items.REEDS)));

    addCraftingRecipe("baffle_cap",
        new PyreCraftingRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom), 3), 1).addIngredients(
            new ItemStack(ModItems.terra_moss),
            new ItemStack(Items.FERMENTED_SPIDER_EYE),
            new ItemStack(Items.WHEAT_SEEDS),
            new ItemStack(Blocks.RED_MUSHROOM),
            new ItemStack(Blocks.BROWN_MUSHROOM)));

    addCraftingRecipe("seeds", new PyreCraftingRecipe(new ItemStack(ModItems.cooked_seeds, 5), 1).addIngredients(
        new ItemStack(ModItems.seeds),
        new ItemStack(ModItems.seeds),
        new ItemStack(ModItems.seeds),
        new ItemStack(ModItems.seeds),
        new ItemStack(ModItems.seeds)).setBurnTime(20));

    addCraftingRecipe("unending_bowl",
        new GroveCraftingRecipe(new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.unending_bowl)), 2).addIngredients(
            new ItemStack(Items.WATER_BUCKET),
            new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.mortar)),
            new ItemStack(ModItems.dewgonia),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.dewgonia)));

    addCraftingRecipe("elemental_soil",
        new GroveCraftingRecipe(new ItemStack(ModBlocks.elemental_soil), 1).addIngredients(
            new ItemStack(Blocks.DIRT),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.wildroot),
            new ItemStack(Blocks.GRAVEL),
            new ItemStack(Items.DYE, 1, 15)));

    addCraftingRecipe("living_pickaxe",
        new GroveCraftingRecipe(new ItemStack(ModItems.living_pickaxe), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_PICKAXE),
            new ItemStack(ModItems.wildroot),
            new ItemStack(ModItems.bark_oak),
            new ItemStack(ModItems.bark_oak)));

    addCraftingRecipe("living_axe",
        new GroveCraftingRecipe(new ItemStack(ModItems.living_axe), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_AXE),
            new ItemStack(ModItems.wildroot),
            new ItemStack(ModItems.bark_oak),
            new ItemStack(ModItems.bark_oak)));

    addCraftingRecipe("living_shovel",
        new GroveCraftingRecipe(new ItemStack(ModItems.living_shovel), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_SHOVEL),
            new ItemStack(ModItems.wildroot),
            new ItemStack(ModItems.bark_oak),
            new ItemStack(ModItems.bark_oak)));

    addCraftingRecipe("living_hoe",
        new GroveCraftingRecipe(new ItemStack(ModItems.living_hoe), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_HOE),
            new ItemStack(ModItems.wildroot),
            new ItemStack(ModItems.bark_oak),
            new ItemStack(ModItems.bark_oak)));

    addCraftingRecipe("living_sword",
        new GroveCraftingRecipe(new ItemStack(ModItems.living_sword), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_SWORD),
            new ItemStack(ModItems.wildroot),
            new ItemStack(ModItems.bark_oak),
            new ItemStack(ModItems.bark_oak)));

    addCraftingRecipe("living_arrow",
        new GroveCraftingRecipe(new ItemStack(ModItems.living_arrow, 6), 1).addIngredients(
            new OreIngredient("treeLeaves"),
            new OreIngredient("treeLeaves"),
            new OreIngredient("rootsBark"),
            new ItemStack(ModItems.wildroot),
            new ItemStack(Items.FLINT)));

    addCraftingRecipe("wildwood_quiver",
        new GroveCraftingRecipe(new ItemStack(ModItems.wildwood_quiver), 2).addIngredients(
            new OreIngredient("chestWood"),
            new ItemStack(ModItems.bark_wildwood),
            new ItemStack(ModItems.bark_wildwood),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.spirit_herb)));

    addCraftingRecipe("wildwood_bow",
        new GroveCraftingRecipe(new ItemStack(ModItems.wildwood_bow), 2).addIngredients(
            new ItemStack(Items.BOW),
            new ItemStack(ModItems.bark_wildwood),
            new ItemStack(ModItems.bark_wildwood),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.spirit_herb)));

    addCraftingRecipe("runic_shears",
        new GroveCraftingRecipe(new ItemStack(ModItems.runic_shears), 1).addIngredients(
            new ItemStack(Items.SHEARS),
            new ItemStack(ModItems.pereskia),
            new ItemStack(ModItems.pereskia),
            new ItemStack(ModBlocks.runestone),
            new ItemStack(ModBlocks.runestone)));

    addCraftingRecipe("runestone",
        new GroveCraftingRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.runestone), 8), 1).addIngredients(
            new ItemStack(Items.DYE, 1, 4),
            new OreIngredient("stone"),
            new OreIngredient("stone"),
            new OreIngredient("stone"),
            new OreIngredient("stone")));

    addCraftingRecipe("wildwood_helmet", new GroveCraftingRecipe(new ItemStack(ModItems.wildwood_helmet), 1).addIngredients(
        new ItemStack(Items.IRON_HELMET),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new OreIngredient("plankWood"),
        new OreIngredient("gemDiamond")));

    addCraftingRecipe("wildwood_chestplate", new GroveCraftingRecipe(new ItemStack(ModItems.wildwood_chestplate), 1).addIngredients(
        new ItemStack(Items.IRON_CHESTPLATE),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new OreIngredient("plankWood"),
        new OreIngredient("gemDiamond")));

    addCraftingRecipe("wildwood_leggings", new GroveCraftingRecipe(new ItemStack(ModItems.wildwood_leggings), 1).addIngredients(
        new ItemStack(Items.IRON_LEGGINGS),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new OreIngredient("plankWood"),
        new OreIngredient("gemDiamond")));

    addCraftingRecipe("wildwood_boots", new GroveCraftingRecipe(new ItemStack(ModItems.wildwood_boots), 1).addIngredients(
        new ItemStack(Items.IRON_BOOTS),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new OreIngredient("plankWood"),
        new OreIngredient("gemDiamond")));

    addCraftingRecipe("apothecary_pouch", new ApothecaryPouchRecipe(new ItemStack(ModItems.apothecary_pouch), 1).addIngredients(
        new ItemStack(Blocks.ENDER_CHEST),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.spirit_herb),
        new ItemStack(ModItems.component_pouch)
    ));

    addCraftingRecipe("sylvan_helmet", new GroveCraftingRecipe(new ItemStack(ModItems.sylvan_helmet), 1).addIngredients(
        new ItemStack(ModItems.fey_leather),
        new ItemStack(Blocks.VINE),
        new ItemStack(ModItems.bark_birch),
        new OreIngredient("gemAmethyst"),
        new ItemStack(ModItems.bark_birch)));

    addCraftingRecipe("sylvan_chestplate", new GroveCraftingRecipe(new ItemStack(ModItems.sylvan_chestplate), 1).addIngredients(
        new ItemStack(ModItems.fey_leather),
        new ItemStack(Blocks.VINE),
        new ItemStack(ModItems.bark_birch),
        new OreIngredient("gemAmethyst"),
        new ItemStack(ModItems.bark_birch)));

    addCraftingRecipe("sylvan_leggings", new GroveCraftingRecipe(new ItemStack(ModItems.sylvan_leggings), 1).addIngredients(
        new ItemStack(ModItems.fey_leather),
        new ItemStack(Blocks.VINE),
        new ItemStack(ModItems.bark_birch),
        new OreIngredient("gemAmethyst"),
        new ItemStack(ModItems.bark_birch)));

    addCraftingRecipe("sylvan_boots", new GroveCraftingRecipe(new ItemStack(ModItems.sylvan_boots), 1).addIngredients(
        new ItemStack(ModItems.fey_leather),
        new ItemStack(Blocks.VINE),
        new ItemStack(ModItems.bark_birch),
        new OreIngredient("gemAmethyst"),
        new ItemStack(ModItems.bark_birch)));
  }

  public static void afterHerbRegisterInit() {
  }

  private static void initDrops() {
  }

}
