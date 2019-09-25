package epicsquid.roots.init;

import com.google.common.collect.Lists;
import com.sun.swing.internal.plaf.metal.resources.metal;
import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import epicsquid.mysticallib.recipe.factories.OreFallbackIngredient;
import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.config.ConfigManager;
import epicsquid.mysticalworld.entity.*;
import epicsquid.mysticalworld.materials.Material;
import epicsquid.mysticalworld.materials.Materials;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.item.ItemDruidKnife;
import epicsquid.roots.recipe.*;
import epicsquid.roots.recipe.ingredient.GoldOrSilverIngotIngredient;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.util.StateUtil;
import epicsquid.roots.util.types.WorldPosStatePredicate;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.*;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class ModRecipes {

  private static Map<ResourceLocation, AnimalHarvestRecipe> harvestRecipes = new HashMap<>();
  private static ObjectOpenHashSet<Class<? extends Entity>> harvestClasses = null;
  private static Map<ResourceLocation, TransmutationRecipe> transmutationRecipes = new HashMap<>();
  private static List<MortarRecipe> mortarRecipes = new ArrayList<>();
  private static Map<String, PyreCraftingRecipe> pyreCraftingRecipes = new HashMap<>();
  private static Map<ResourceLocation, FeyCraftingRecipe> feyCraftingRecipes = new HashMap<>();
  private static Map<String, RunicShearRecipe> runicShearRecipes = new HashMap<>();
  private static Map<Class<? extends Entity>, RunicShearRecipe> runicShearEntityRecipes = new HashMap<>();
  private static List<RunicCarvingRecipe> runicCarvingRecipes = new ArrayList<>();
  private static Map<ResourceLocation, PacifistEntry> pacifistEntities = new HashMap<>();
  private static Map<Class<? extends Entity>, PacifistEntry> pacifistClasses = new HashMap<>();
  private static Map<ResourceLocation, BarkRecipe> barkRecipes = new HashMap<>();
  private static Map<ResourceLocation, FlowerRecipe> flowerRecipes = new HashMap<>();

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
    PacifistEntry entry = getPacifistEntry(name);
    if (entry != null) {
      pacifistEntities.remove(name);
      pacifistClasses.remove(entry.getEntityClass());
    }
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
    addPacifistEntry("owl", EntityOwl.class);
    addPacifistEntry("sprout", EntitySprout.class);
  }

  public static void addFlowerRecipe(String name, IBlockState state) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    FlowerRecipe recipe = new FlowerRecipe(rl, state);
    flowerRecipes.put(rl, recipe);
  }

  public static void addFlowerRecipe(String name, Block block, int meta) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    FlowerRecipe recipe = new FlowerRecipe(rl, meta, block);
    flowerRecipes.put(rl, recipe);
  }

  public static void removeFlowerRecipe(ResourceLocation name) {
    flowerRecipes.remove(name);
  }

  @Nullable
  public static FlowerRecipe getRandomFlowerRecipe() {
    if (flowerRecipes.isEmpty()) return null;

    return Lists.newArrayList(flowerRecipes.values()).get(Util.rand.nextInt(Math.max(1, flowerRecipes.size())));
  }

  public static void initFlowerRecipes() {
    addFlowerRecipe("dandelion", Blocks.YELLOW_FLOWER.getStateFromMeta(EnumFlowerType.DANDELION.getMeta()));
    addFlowerRecipe("poppy", Blocks.RED_FLOWER.getStateFromMeta(EnumFlowerType.POPPY.getMeta()));
    addFlowerRecipe("blue_orchid", Blocks.RED_FLOWER.getStateFromMeta(EnumFlowerType.BLUE_ORCHID.getMeta()));
    addFlowerRecipe("allium", Blocks.RED_FLOWER.getStateFromMeta(EnumFlowerType.ALLIUM.getMeta()));
    addFlowerRecipe("houstonia", Blocks.RED_FLOWER.getStateFromMeta(EnumFlowerType.HOUSTONIA.getMeta()));
    addFlowerRecipe("red_tulip", Blocks.RED_FLOWER.getStateFromMeta(EnumFlowerType.RED_TULIP.getMeta()));
    addFlowerRecipe("orange_tulip", Blocks.RED_FLOWER.getStateFromMeta(EnumFlowerType.ORANGE_TULIP.getMeta()));
    addFlowerRecipe("white_tulip", Blocks.RED_FLOWER.getStateFromMeta(EnumFlowerType.WHITE_TULIP.getMeta()));
    addFlowerRecipe("pink_tulip", Blocks.RED_FLOWER.getStateFromMeta(EnumFlowerType.PINK_TULIP.getMeta()));
    addFlowerRecipe("oxeye_daisy", Blocks.RED_FLOWER.getStateFromMeta(EnumFlowerType.OXEYE_DAISY.getMeta()));
  }

  public static void addVanillaBarkRecipe(String name, BlockPlanks.EnumType type, ItemStack item) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    barkRecipes.put(rl, new BarkRecipe(rl, item, type));
  }

  public static void initVanillaBarkRecipes() {
    addVanillaBarkRecipe("oak", BlockPlanks.EnumType.OAK, new ItemStack(ModItems.bark_oak, 4));
    addVanillaBarkRecipe("spruce", BlockPlanks.EnumType.SPRUCE, new ItemStack(ModItems.bark_spruce, 4));
    addVanillaBarkRecipe("birch", BlockPlanks.EnumType.BIRCH, new ItemStack(ModItems.bark_birch, 4));
    addVanillaBarkRecipe("jungle", BlockPlanks.EnumType.JUNGLE, new ItemStack(ModItems.bark_jungle, 4));
    addVanillaBarkRecipe("acacia", BlockPlanks.EnumType.ACACIA, new ItemStack(ModItems.bark_acacia, 4));
    addVanillaBarkRecipe("dark_oak", BlockPlanks.EnumType.DARK_OAK, new ItemStack(ModItems.bark_dark_oak, 4));
  }

  public static void initModdedBarkRecipes() {
    addModdedBarkRecipe("wildwood", new ItemStack(ModItems.bark_wildwood, 4), new ItemStack(ModBlocks.wildwood_log));
  }

  public static void addModdedBarkRecipe(String name, ItemStack item, ItemStack blockStack) {
    if (blockStack.getItem() instanceof ItemBlock) {
      for (Item knife : ModItems.knives) {
        ((ItemDruidKnife) knife).addEffectiveBlock(((ItemBlock) blockStack.getItem()).getBlock());
      }
    }
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    barkRecipes.put(rl, new BarkRecipe(rl, item, blockStack));
  }

  @Nullable
  public static BarkRecipe getModdedBarkRecipe(IBlockState block) {
    ItemStack stack = new ItemStack(block.getBlock(), 1, block.getBlock().damageDropped(block));
    for (BarkRecipe recipe : barkRecipes.values()) {
      if (ItemUtil.equalWithoutSize(recipe.getBlockStack(), stack)) {
        return recipe;
      }
    }
    return null;
  }

  @Nullable
  public static BarkRecipe getVanillaBarkRecipe(BlockPlanks.EnumType type) {
    for (BarkRecipe recipe : barkRecipes.values()) {
      if (recipe.getType() == type) return recipe;
    }
    return null;
  }

  public static Collection<BarkRecipe> getBarkRecipes() {
    return barkRecipes.values();
  }

  public static boolean removeBarkRecipe(ItemStack stack) {
    List<BarkRecipe> toRemove = new ArrayList<>();
    for (BarkRecipe recipe : barkRecipes.values()) {
      if (ItemUtil.equalWithoutSize(stack, recipe.getItem())) {
        toRemove.add(recipe);
        break;
      }
    }

    if (!toRemove.isEmpty()) {
      for (BarkRecipe removing : toRemove) {
        barkRecipes.remove(removing.getName());
      }
      return true;
    }

    return false;
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

  public static List<TransmutationRecipe> getTransmutationRecipes() {
    return new ArrayList<>(transmutationRecipes.values());
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
    addAnimalHarvestRecipe("polar_bear", EntityPolarBear.class);
    // No villager or skeletal/zombie horses.
    // Mystical World
    addAnimalHarvestRecipe("beetle", EntityBeetle.class);
    addAnimalHarvestRecipe("deer", EntityDeer.class);
    addAnimalHarvestRecipe("fox", EntityFox.class);
    addAnimalHarvestRecipe("frog", EntityFrog.class);
    addAnimalHarvestRecipe("owl", EntityOwl.class);
    addAnimalHarvestRecipe("sprout", EntitySprout.class);
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

  public static RunicShearRecipe getRunicShearRecipe(ItemStack stack) {
    for (RunicShearRecipe recipe : runicShearRecipes.values()) {
      if (ItemStack.areItemStacksEqual(recipe.getDrop(), stack)) {
        return recipe;
      }
    }

    return null;
  }

  public static RunicShearRecipe getRunicShearEntityRecipe(ItemStack stack) {
    for (RunicShearRecipe recipe : runicShearEntityRecipes.values()) {
      if (ItemStack.areItemStacksEqual(recipe.getDrop(), stack)) {
        return recipe;
      }
    }

    return null;
  }

  public static Set<Class<? extends Entity>> getRunicShearEntities() {
    return runicShearEntityRecipes.keySet();
  }

  public static void addMortarRecipe(MortarRecipe recipe) {
    mortarRecipes.add(recipe);
  }

  public static MortarRecipe getMortarRecipe(List<ItemStack> items) {
    for (MortarRecipe mortarRecipe : mortarRecipes) {
      if (mortarRecipe.matches(items)) {
        return mortarRecipe;
      }
    }
    return null;
  }

  public static void removeMortarRecipes(ItemStack output) {
    mortarRecipes.removeIf(recipe -> ItemUtil.equalWithoutSize(recipe.getResult(), output));
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
      if (Objects.equals(output.getItem().getRegistryName(), item) && output.getMetadata() == meta) {
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

  public static void addFeyCraftingRecipe(ResourceLocation name, FeyCraftingRecipe recipe) {
    assert !feyCraftingRecipes.containsKey(name);

    feyCraftingRecipes.put(name, recipe);
  }

  public static void addPyreCraftingRecipe(ResourceLocation name, PyreCraftingRecipe recipe) {
    assert !pyreCraftingRecipes.containsKey(name.getPath());

    pyreCraftingRecipes.put(name.getPath(), recipe);
  }

  public static Map<ResourceLocation, FeyCraftingRecipe> getFeyCraftingRecipes() {
    return feyCraftingRecipes;
  }

  @Nullable
  public static FeyCraftingRecipe getFeyCraftingRecipe(List<ItemStack> items) {
    for (FeyCraftingRecipe recipe : feyCraftingRecipes.values()) {
      if (recipe.matches(items)) return recipe;
    }

    return null;
  }

  public static FeyCraftingRecipe getFeyCraftingRecipe(String name) {
    return getFeyCraftingRecipe(new ResourceLocation(Roots.MODID, name));
  }

  @Nullable
  public static FeyCraftingRecipe getFeyCraftingRecipe(ResourceLocation name) {
    return feyCraftingRecipes.get(name);
  }

  public static PyreCraftingRecipe getCraftingRecipe(String recipeName) {
    return pyreCraftingRecipes.get(recipeName);
  }

  public static void removePyreCraftingRecipe(ResourceLocation name) {
    pyreCraftingRecipes.remove(name.getPath());
  }

  public static void removePyreCraftingRecipe(ItemStack output) {
    pyreCraftingRecipes.entrySet().removeIf((recipe) -> ItemUtil.equalWithoutSize(recipe.getValue().getResult(), output));
  }

  public static void removeFeyCraftingRecipe(ResourceLocation name) {
    feyCraftingRecipes.remove(name);
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

  private static void addCraftingRecipe(String recipeName, FeyCraftingRecipe recipe) {
    ResourceLocation recipeId = new ResourceLocation(Roots.MODID, recipeName);
    if (feyCraftingRecipes.containsKey(recipeId)) {
      System.out.println("GroveCraftingRecipe already registered with name " + recipeName);
      return;
    }

    feyCraftingRecipes.put(recipeId, recipe.setName(recipeName));
  }

  public static List<RunicCarvingRecipe> getRunicCarvingRecipes() {
    return runicCarvingRecipes;
  }

  public static Map<String, RunicShearRecipe> getRunicShearRecipes() {
    return runicShearRecipes;
  }

  public static Map<Class<? extends Entity>, RunicShearRecipe> getRunicShearEntityRecipes() {
    return runicShearEntityRecipes;
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
    addMortarRecipe(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getMetadata()), Ingredient.fromItem(epicsquid.mysticalworld.init.ModItems.carapace), 1, 1, 1, 1, 1, 1);
    addMortarRecipe(new ItemStack(Items.DYE, 1, EnumDyeColor.ORANGE.getMetadata()), new OreIngredient("cropCarrot"), 1, 1, 1, 1, 1, 1);
    addMortarRecipe(new ItemStack(ModItems.flour), new OreIngredient("cropWheat"), 1f, 1f, 0f, 1f, 1f, 0f);
    addMortarRecipe(new ItemStack(ModItems.flour), new OreIngredient("cropPotato"), 1f, 1f, 0, 1f, 1f, 0f);
    addMortarRecipe(new ItemStack(Items.DYE, 4, 15), new OreIngredient("bone"), 0f, 0f, 0f, 0f, 0f, 0f);
    addMortarRecipe(new ItemStack(Items.SUGAR, 2), new OreIngredient("sugarcane"), 0f, 0f, 0f, 1f, 1f, 1f);
    addMortarRecipe(new ItemStack(Items.BLAZE_POWDER, 5), Ingredient.fromItem(Items.BLAZE_ROD), 1, 1, 1, 1, 1, 1);
    addMortarRecipe(new ItemStack(Items.STRING, 3), new OreIngredient("wool"), 1, 1, 1, 1, 1, 1);
    addMortarRecipe(new ItemStack(Items.STRING, 5), Ingredient.fromItem(epicsquid.mysticalworld.init.ModItems.silk_cocoon), 0, 0, 0, 0, 0, 0);
    addMortarRecipe(new ItemStack(Items.MAGMA_CREAM, 2), Ingredient.fromItem(Item.getItemFromBlock(Blocks.MAGMA)), 1, 0, 0, 1, 0, 0);

    for (Material metal : Materials.getMaterials()) {
      if (!metal.isEnabled()) continue;

      Item metalDust = metal.getDust();
      if (metalDust == null) {
        continue;
      }

      addMortarRecipe(new ItemStack(metalDust), new OreIngredient("ingot" + metal.getOredictNameSuffix()), 82f / 255f, 92f / 255f, 114f / 255f, 160f / 255f, 167f / 255f, 183f / 255f);
    }

    if (ConfigManager.gold.enableDusts) {
      addMortarRecipe(new ItemStack(epicsquid.mysticalworld.init.ModItems.gold_dust), new OreIngredient("ingotGold"), 82f / 255f, 92f / 255f, 114f / 255f, 160f / 255f, 167f / 255f, 183f / 255f);
    }
    if (ConfigManager.iron.enableDusts) {
      addMortarRecipe(new ItemStack(epicsquid.mysticalworld.init.ModItems.iron_dust), new OreIngredient("ingotIron"), 82f / 255f, 92f / 255f, 114f / 255f, 160f / 255f, 167f / 255f, 183f / 255f);
    }

    addMortarRecipe(new ItemStack(ModItems.petals), new OreIngredient("allFlowers"), 1f, 0f, 0f, 0f, 1f, 0f);
    addMortarRecipe(new ItemStack(ModItems.petals, 2), new OreIngredient("allTallFlowers"), 1f, 0f, 0f, 0f, 1f, 0f);
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
    initFlowerRecipes();

    GameRegistry.addSmelting(ModItems.flour, new ItemStack(Items.BREAD), 0.125f);
    GameRegistry.addSmelting(ModItems.seeds, new ItemStack(ModItems.cooked_seeds), 0.05f);
    GameRegistry.addSmelting(ModItems.pereskia_bulb, new ItemStack(ModItems.cooked_pereskia), 0.125f);

    initCraftingRecipes();
    RunicShearRecipes.initRecipes();

  }

  private static void initCraftingRecipes() {
    addCraftingRecipe("infernal_bulb",
        new PyreCraftingRecipe(new ItemStack(ModItems.infernal_bulb, 3), 1).addIngredients(
            new OreIngredient("wildroot"),
            new ItemStack(Items.MAGMA_CREAM),
            new OreIngredient("gunpowder"),
            new ItemStack(Items.LAVA_BUCKET),
            new OreIngredient("dustGlowstone")));

    addCraftingRecipe("dewgonia",
        new PyreCraftingRecipe(new ItemStack(ModItems.dewgonia, 3), 1).addIngredients(
            new ItemStack(Item.getItemFromBlock(Blocks.TALLGRASS), 1, 1),
            new ItemStack(Items.SUGAR),
            new ItemStack(Items.DYE, 1, 4),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(Item.getItemFromBlock(Blocks.WATERLILY))));

    addCraftingRecipe("cloud_berry",
        new PyreCraftingRecipe(new ItemStack(ModItems.cloud_berry, 3), 1).addIngredients(
            new OreIngredient("treeLeaves"),
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
        new FeyCraftingRecipe(new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.unending_bowl)), 2).addIngredients(
            new ItemStack(Items.WATER_BUCKET),
            new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.mortar)),
            new ItemStack(ModItems.dewgonia),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.dewgonia)));

    addCraftingRecipe("elemental_soil",
        new FeyCraftingRecipe(new ItemStack(ModBlocks.elemental_soil), 4).addIngredients(
            new ItemStack(Blocks.DIRT),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.wildroot),
            new ItemStack(Blocks.GRAVEL),
            new ItemStack(Items.DYE, 1, 15)));

    addCraftingRecipe("living_pickaxe",
        new FeyCraftingRecipe(new ItemStack(ModItems.living_pickaxe), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_PICKAXE),
            new ItemStack(ModItems.wildroot),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));

    addCraftingRecipe("living_axe",
        new FeyCraftingRecipe(new ItemStack(ModItems.living_axe), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_AXE),
            new ItemStack(ModItems.wildroot),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));

    addCraftingRecipe("living_shovel",
        new FeyCraftingRecipe(new ItemStack(ModItems.living_shovel), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_SHOVEL),
            new ItemStack(ModItems.wildroot),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));

    addCraftingRecipe("living_hoe",
        new FeyCraftingRecipe(new ItemStack(ModItems.living_hoe), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_HOE),
            new ItemStack(ModItems.wildroot),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));

    addCraftingRecipe("living_sword",
        new FeyCraftingRecipe(new ItemStack(ModItems.living_sword), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_SWORD),
            new ItemStack(ModItems.wildroot),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));

    addCraftingRecipe("living_arrow",
        new FeyCraftingRecipe(new ItemStack(ModItems.living_arrow, 6), 1).addIngredients(
            new OreIngredient("treeLeaves"),
            new OreIngredient("treeLeaves"),
            new OreIngredient("rootsBark"),
            new ItemStack(ModItems.wildroot),
            new ItemStack(Items.FLINT)));

    addCraftingRecipe("wildwood_quiver",
        new FeyCraftingRecipe(new ItemStack(ModItems.wildwood_quiver), 2).addIngredients(
            new OreIngredient("chestWood"),
            new ItemStack(ModItems.bark_wildwood),
            new ItemStack(ModItems.bark_wildwood),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.spirit_herb)));

    addCraftingRecipe("wildwood_bow",
        new FeyCraftingRecipe(new ItemStack(ModItems.wildwood_bow), 2).addIngredients(
            new ItemStack(Items.BOW),
            new ItemStack(ModItems.bark_wildwood),
            new ItemStack(ModItems.bark_wildwood),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.spirit_herb)));

    addCraftingRecipe("runic_shears",
        new FeyCraftingRecipe(new ItemStack(ModItems.runic_shears), 1).addIngredients(
            new ItemStack(Items.SHEARS),
            new ItemStack(ModItems.pereskia),
            new ItemStack(ModItems.pereskia),
            new ItemStack(ModBlocks.runestone),
            new ItemStack(ModBlocks.runestone)));

    addCraftingRecipe("runestone",
        new FeyCraftingRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.runestone), 8), 1).addIngredients(
            new ItemStack(Items.DYE, 1, 4),
            new OreIngredient("stone"),
            new OreIngredient("stone"),
            new OreIngredient("stone"),
            new OreIngredient("stone")));

    addCraftingRecipe("wildwood_helmet", new FeyCraftingRecipe(new ItemStack(ModItems.wildwood_helmet), 1).addIngredients(
        new ItemStack(Items.IRON_HELMET),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new OreIngredient("plankWood"),
        new OreIngredient("gemDiamond")));

    addCraftingRecipe("wildwood_chestplate", new FeyCraftingRecipe(new ItemStack(ModItems.wildwood_chestplate), 1).addIngredients(
        new ItemStack(Items.IRON_CHESTPLATE),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new OreIngredient("plankWood"),
        new OreIngredient("gemDiamond")));

    addCraftingRecipe("wildwood_leggings", new FeyCraftingRecipe(new ItemStack(ModItems.wildwood_leggings), 1).addIngredients(
        new ItemStack(Items.IRON_LEGGINGS),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new OreIngredient("plankWood"),
        new OreIngredient("gemDiamond")));

    addCraftingRecipe("wildwood_boots", new FeyCraftingRecipe(new ItemStack(ModItems.wildwood_boots), 1).addIngredients(
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

    addCraftingRecipe("sylvan_helmet", new FeyCraftingRecipe(new ItemStack(ModItems.sylvan_helmet), 1).addIngredients(
        new ItemStack(ModItems.fey_leather),
        new ItemStack(Blocks.VINE),
        new ItemStack(ModItems.bark_birch),
        new OreFallbackIngredient("gemAmethyst", "gemDiamond"),
        new ItemStack(Items.IRON_HELMET)));

    addCraftingRecipe("sylvan_chestplate", new FeyCraftingRecipe(new ItemStack(ModItems.sylvan_chestplate), 1).addIngredients(
        new ItemStack(ModItems.fey_leather),
        new ItemStack(Blocks.VINE),
        new ItemStack(ModItems.bark_birch),
        new OreFallbackIngredient("gemAmethyst", "gemDiamond"),
        new ItemStack(Items.IRON_CHESTPLATE)));

    addCraftingRecipe("sylvan_leggings", new FeyCraftingRecipe(new ItemStack(ModItems.sylvan_leggings), 1).addIngredients(
        new ItemStack(ModItems.fey_leather),
        new ItemStack(Blocks.VINE),
        new ItemStack(ModItems.bark_birch),
        new OreFallbackIngredient("gemAmethyst", "gemDiamond"),
        new ItemStack(Items.IRON_LEGGINGS)));

    addCraftingRecipe("sylvan_boots", new FeyCraftingRecipe(new ItemStack(ModItems.sylvan_boots), 1).addIngredients(
        new ItemStack(ModItems.fey_leather),
        new ItemStack(Blocks.VINE),
        new ItemStack(ModItems.bark_birch),
        new OreFallbackIngredient("gemAmethyst", "gemDiamond"),
        new ItemStack(Items.IRON_BOOTS)));
  }

  public static void afterHerbRegisterInit() {
  }

  private static void initDrops() {
  }

}
