package epicsquid.roots.init;

import com.google.common.collect.Lists;
import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import epicsquid.mysticallib.recipe.factories.AccessibleCompoundIngredient;
import epicsquid.mysticallib.recipe.factories.MultiOreIngredient;
import epicsquid.mysticallib.recipe.factories.OreFallbackIngredient;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.MysticalWorld;
import epicsquid.mysticalworld.config.ConfigManager;
import epicsquid.mysticalworld.entity.*;
import epicsquid.mysticalworld.materials.Material;
import epicsquid.mysticalworld.materials.Materials;
import epicsquid.mysticalworld.recipe.Ingredients;
import epicsquid.roots.Roots;
import epicsquid.roots.item.ItemDruidKnife;
import epicsquid.roots.recipe.*;
import epicsquid.roots.recipe.ingredient.GoldOrSilverIngotIngredient;
import epicsquid.roots.recipe.transmutation.*;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.util.IngredientWithStack;
import net.minecraft.block.*;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class ModRecipes {

  private static Map<ResourceLocation, ChrysopoeiaRecipe> chrysopoeiaRecipes = new HashMap<>();

  // TODO: Registries
  private static Map<ResourceLocation, AnimalHarvestRecipe> harvestRecipes = new HashMap<>();
  private static Map<ResourceLocation, AnimalHarvestFishRecipe> fishRecipes = new HashMap<>();
  private static Set<Class<? extends EntityLivingBase>> harvestClasses = null;
  private static Map<ResourceLocation, TransmutationRecipe> transmutationRecipes = new HashMap<>();

  // TODO: REGISTRIES FUCKING REGISTRIES PLEASE OH GOD REGISTRIES
  private static Map<ResourceLocation, SummonCreatureRecipe> summonCreatureRecipes = new HashMap<>();
  private static Map<Class<? extends EntityLivingBase>, SummonCreatureRecipe> summonCreatureClasses = new HashMap<>();

  // TODO: ResourceLocation-based
  private static Map<ResourceLocation, MortarRecipe> mortarRecipes = new HashMap<>();
  private static Map<ResourceLocation, PyreCraftingRecipe> pyreCraftingRecipes = new HashMap<>();

  private static Map<ResourceLocation, FeyCraftingRecipe> feyCraftingRecipes = new HashMap<>();
  private static Map<ResourceLocation, RunicShearRecipe> runicShearRecipes = new HashMap<>();
  private static Map<ResourceLocation, RunicShearEntityRecipe> runicShearEntityRecipes = new HashMap<>();
  private static Map<Class<? extends Entity>, RunicShearEntityRecipe> generatedEntityRecipes = null;

  private static Map<ResourceLocation, PacifistEntry> pacifistEntities = new HashMap<>();
  private static Map<Class<? extends Entity>, PacifistEntry> pacifistClasses = new HashMap<>();
  private static Map<ResourceLocation, BarkRecipe> barkRecipes = new HashMap<>();
  private static Map<ResourceLocation, FlowerRecipe> flowerRecipes = new HashMap<>();

  public static void initChrysopoeiaRecipes() {
    addChrysopoeiaRecipe("gold_from_silver", new IngredientWithStack(new OreIngredient("ingotSilver"), 2), new ItemStack(Items.GOLD_INGOT));
    addChrysopoeiaRecipe("iron_from_copper", new IngredientWithStack(new OreIngredient("ingotCopper"), 2), new ItemStack(Items.IRON_INGOT));
    addChrysopoeiaRecipe("gold_nugget_from_silver", new IngredientWithStack(new OreIngredient("nuggetSilver"), 2), new ItemStack(Items.GOLD_NUGGET));
    addChrysopoeiaRecipe("iron_nugget_from_copper", new IngredientWithStack(new OreIngredient("nuggetCopper"), 2), new ItemStack(Items.IRON_NUGGET));
    addChrysopoeiaRecipe("leather_from_rotten_flesh", new IngredientWithStack(Ingredient.fromItem(Items.ROTTEN_FLESH), 10), new ItemStack(Items.LEATHER));
  }

  public static Collection<ChrysopoeiaRecipe> getChrysopoeiaRecipes() {
    return chrysopoeiaRecipes.values();
  }

  public static ChrysopoeiaRecipe addChrysopoeiaRecipe(String name, IngredientWithStack ingredient, ItemStack output) {
    return addChrysopoeiaRecipe(new ResourceLocation(MysticalWorld.MODID, name), ingredient, output);
  }

  public static ChrysopoeiaRecipe addChrysopoeiaRecipe(ResourceLocation name, IngredientWithStack ingredient, ItemStack output) {
    return addChrysopoeiaRecipe(name, ingredient, output, ItemStack.EMPTY, 0, 0);
  }

  public static ChrysopoeiaRecipe addChrysopoeiaRecipe(ResourceLocation name, IngredientWithStack inputs, ItemStack outputs, ItemStack byproduct, float overload, float byproductChance) {
    if (chrysopoeiaRecipes.containsKey(name)) {
      Roots.logger.error("Invalid key: " + name.toString() + " for Chrysopoeia recipe. Key already exists.");
      return null;
    }
    ChrysopoeiaRecipe recipe = new ChrysopoeiaRecipe(inputs, outputs, byproduct, overload, byproductChance);
    recipe.setRegistryName(name);
    chrysopoeiaRecipes.put(name, recipe);
    return recipe;
  }

  @Nullable
  public static ChrysopoeiaRecipe getChrysopoeiaRecipe(ItemStack stack) {
    for (ChrysopoeiaRecipe recipe : getChrysopoeiaRecipes()) {
      if (recipe.matches(stack)) {
        return recipe;
      }
    }

    return null;
  }

  public static void removeChrysopoeiaRecipe(ResourceLocation name) {
    chrysopoeiaRecipes.remove(name);
  }

  public static void removeChrysopoeiaRecipe(ItemStack stack) {
    ChrysopoeiaRecipe recipe = getChrysopoeiaRecipe(stack);
    if (recipe != null) {
      removeChrysopoeiaRecipe(recipe.getRegistryName());
    }
  }

  public static void initSummonCreatureEntries() {
    addSummonCreatureEntry("owl", EntityOwl.class, new OreIngredient("treeSapling"), new OreIngredient("treeLeaves"));
    addSummonCreatureEntry("deer", EntityDeer.class, Ingredient.fromItem(ModItems.petals), Ingredient.fromItem(ModItems.bark_oak));
    addSummonCreatureEntry("sprout", EntitySprout.class, Ingredient.fromItem(ModItems.bark_birch), Ingredients.AUBERGINE);
    addSummonCreatureEntry("beetle", EntityBeetle.class, new OreIngredient("tallgrass"), Ingredient.fromItem(Item.getItemFromBlock(Blocks.RED_FLOWER)));
    addSummonCreatureEntry("frog", EntityFrog.class, new OreIngredient("tallgrass"), Ingredient.fromItem(Items.CLAY_BALL));
    addSummonCreatureEntry("fox", EntityFox.class, Ingredient.fromItem(ModItems.bark_spruce), new OreIngredient("dustRedstone"));
    addSummonCreatureEntry("wolf", EntityWolf.class, new OreIngredient("bone"), Ingredient.fromItem(Items.FLINT));
    addSummonCreatureEntry("squid", EntitySquid.class, new OreIngredient("sugarcane"), new OreIngredient("paper"));
    addSummonCreatureEntry("sheep", EntitySheep.class, Ingredient.fromItem(Items.WHEAT_SEEDS), new OreIngredient("wool"));
    addSummonCreatureEntry("rabbit", EntityRabbit.class, new OreIngredient("cropCarrot"), new OreIngredient("string"));
    addSummonCreatureEntry("polar_bear", EntityPolarBear.class, new OreIngredient("stone"), Ingredient.fromItem(Items.SNOWBALL));
    addSummonCreatureEntry("pig", EntityPig.class, Ingredient.fromItem(Items.BEETROOT), Ingredient.fromItem(Item.getItemFromBlock(epicsquid.mysticalworld.init.ModBlocks.wet_mud_block)));
    addSummonCreatureEntry("mooshroom", EntityMooshroom.class, new OreIngredient("cropWheat"), Ingredient.fromItem(Item.getItemFromBlock(Blocks.RED_MUSHROOM)));
    addSummonCreatureEntry("cow", EntityCow.class, new OreIngredient("cropWheat"), Ingredient.fromItem(Items.DYE));
    addSummonCreatureEntry("horse", EntityHorse.class, new OreIngredient("cropWheat"), Ingredient.fromItem(Items.APPLE));
    addSummonCreatureEntry("llama", EntityLlama.class, new OreIngredient("cropWheat"), new OreIngredient("wool"));
    addSummonCreatureEntry("bat", EntityBat.class, new OreIngredient("treeSapling"), Ingredient.fromItem(Item.getItemFromBlock(Blocks.WEB)));
    addSummonCreatureEntry("chicken", EntityChicken.class, new OreIngredient("egg"), Ingredient.fromItem(Items.WHEAT_SEEDS));
    addSummonCreatureEntry("donkey", EntityDonkey.class, new OreIngredient("cropCarrot"), new OreIngredient("chestWood"));
    addSummonCreatureEntry("parrot", EntityParrot.class, Ingredient.fromItem(ModItems.bark_jungle), Ingredient.fromItem(Items.BEETROOT_SEEDS));
    addSummonCreatureEntry("ocelot", EntityOcelot.class, new OreIngredient("tallgrass"), Ingredient.fromItem(Items.SUGAR));
    addSummonCreatureEntry("mule", EntityMule.class, new OreIngredient("cropWheat"), Ingredient.fromItem(Items.COAL));

    // Hostiles
    addSummonCreatureEntry("zombie_pigman", EntityPigZombie.class, new OreIngredient("nuggetGold"), Ingredient.fromItem(Items.PORKCHOP), Ingredient.fromItem(Items.FISHING_ROD));
    addSummonCreatureEntry("zombie", EntityZombie.class, Ingredient.fromStacks(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage())), Ingredient.fromItem(Items.ROTTEN_FLESH), new OreIngredient("cropPotato"));
    addSummonCreatureEntry("skeleton", EntitySkeleton.class, Ingredient.fromStacks(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage())), Ingredient.fromItem(Items.FLINT), Ingredient.fromStacks(new ItemStack(Items.BOW, 1, OreDictionary.WILDCARD_VALUE)));
    addSummonCreatureEntry("husk", EntityHusk.class, Ingredient.fromItem(Item.getItemFromBlock(Blocks.CACTUS)), Ingredient.fromItem(Item.getItemFromBlock(Blocks.DEADBUSH)), Ingredient.fromStacks(new ItemStack(Item.getItemFromBlock(Blocks.SANDSTONE), 1, BlockSandStone.EnumType.CHISELED.getMetadata())));
    addSummonCreatureEntry("spider", EntitySpider.class, Ingredient.fromItem(ModItems.glass_eye), new OreIngredient("string"), Ingredient.fromItem(Item.getItemFromBlock(Blocks.LADDER)));
    addSummonCreatureEntry("creeper", EntityCreeper.class, Ingredient.fromItem(Item.getItemFromBlock(Blocks.DIRT)), new OreIngredient("tallgrass"), Ingredient.fromItem(Items.GUNPOWDER));
    addSummonCreatureEntry("witch", EntityWitch.class, new OreIngredient("stickWood"), new OreIngredient("dustRedstone"), new OreIngredient("string"));
    addSummonCreatureEntry("stray", EntityStray.class, Ingredient.fromItem(Items.SNOWBALL), Ingredient.fromItem(Items.FLINT), Ingredient.fromStacks(new ItemStack(Items.BOW, 1, OreDictionary.WILDCARD_VALUE)));
  }

  public static SummonCreatureRecipe addSummonCreatureEntry(String name, Class<? extends EntityLivingBase> clazz, Ingredient... ingredients) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    SummonCreatureRecipe recipe = new SummonCreatureRecipe(rl, clazz, ingredients);
    return addSummonCreatureEntry(recipe);
  }

  public static SummonCreatureRecipe addSummonCreatureEntry(String name, Class<? extends EntityLivingBase> clazz, List<Ingredient> ingredients) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    SummonCreatureRecipe recipe = new SummonCreatureRecipe(rl, clazz, ingredients);
    return addSummonCreatureEntry(recipe);
  }

  public static SummonCreatureRecipe addSummonCreatureEntry(SummonCreatureRecipe recipe) {
    ResourceLocation rl = recipe.getRegistryName();
    if (summonCreatureRecipes.containsKey(rl)) {
      throw new IllegalArgumentException("Resource location " + rl.toString() + " already contained within the Summon Creatures registry.");
    } else if (summonCreatureClasses.containsKey(recipe.getClazz())) {
      throw new IllegalArgumentException("Class " + recipe.getClazz().toString() + " already contained within the Summon Creatures registry.");
    }
    try {
      if (findSummonCreatureEntry(recipe.getIngredients().stream().map(o -> o.getMatchingStacks()[0].copy()).collect(Collectors.toList())) != null) {
        throw new IllegalArgumentException("Combination of ingredients for recipe (" + rl.toString() + "/" + recipe.getClazz().toString() + ") is already in use!");
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid ingredient for recipe " + rl.toString());
    }
    summonCreatureRecipes.put(rl, recipe);
    summonCreatureClasses.put(recipe.getClazz(), recipe);
    return recipe;
  }

  @Nullable
  public static SummonCreatureRecipe getSummonCreatureEntry(String name) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    return getSummonCreatureEntry(rl);
  }

  @Nullable
  public static SummonCreatureRecipe getSummonCreatureEntry(ResourceLocation location) {
    return summonCreatureRecipes.get(location);
  }

  @Nullable
  public static SummonCreatureRecipe getSummonCreatureEntry(Class<? extends Entity> clazz) {
    return summonCreatureClasses.get(clazz);
  }

  public static Collection<SummonCreatureRecipe> getSummonCreatureEntries() {
    return summonCreatureRecipes.values();
  }

  @Nullable
  public static SummonCreatureRecipe findSummonCreatureEntry(List<ItemStack> ingredients) {
    for (SummonCreatureRecipe recipe : getSummonCreatureEntries()) {
      if (recipe.matches(ingredients)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean lifeEssenceCleared = false;
  public static Set<Class<? extends EntityLivingBase>> lifeEssenceAdditions = new HashSet<>();
  public static Set<Class<? extends EntityLivingBase>> lifeEssenceRemovals = new HashSet<>();

  private static Set<Class<? extends EntityLivingBase>> lifeEssenceList = new HashSet<>();

  public static void removeLifeEssence(Class<? extends EntityLivingBase> clzz) {
    lifeEssenceRemovals.add(clzz);
  }

  public static void addLifeEssence(Class<? extends EntityLivingBase> clazz) {
    lifeEssenceAdditions.add(clazz);
  }

  public static void clearLifeEssence() {
    lifeEssenceCleared = true;
  }

  public static void generateLifeEssence() {
    lifeEssenceList.clear();

    if (!lifeEssenceCleared) {
      lifeEssenceList.addAll(getAnimalHarvestClasses());
    }

    lifeEssenceList.addAll(lifeEssenceAdditions);
    lifeEssenceList.removeAll(lifeEssenceRemovals);
  }

  public static Set<Class<? extends EntityLivingBase>> getLifeEssenceList() {
    return lifeEssenceList;
  }

  public static boolean isLifeEssenceAllowed(Class<? extends EntityLivingBase> clzz) {
    return lifeEssenceList.contains(clzz);
  }

  public static boolean isLifeEssenceAllowed(EntityLivingBase entity) {
    return isLifeEssenceAllowed(entity.getClass());
  }

  public static void removeSummonCreatureEntry(Class<? extends EntityLivingBase> clazz) {
    SummonCreatureRecipe recipe = summonCreatureClasses.remove(clazz);
    if (recipe != null) {
      summonCreatureRecipes.remove(recipe.getRegistryName());
    }
  }

  public static void removeSummonCreatureEntry(String name) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    removeSummonCreatureEntry(rl);
  }

  public static void removeSummonCreatureEntry(ResourceLocation location) {
    SummonCreatureRecipe recipe = summonCreatureRecipes.get(location);
    if (recipe == null) {
      return;
    }
    summonCreatureRecipes.remove(location);
    summonCreatureClasses.remove(recipe.getClazz());
  }

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

  public static Map<ResourceLocation, PacifistEntry> getPacifistEntities() {
    return pacifistEntities;
  }

  public static void initPacifistEntities() {
    // Bats are too easily killed.
    //addPacifistEntry("bat", EntityBat.class);
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
    // TODO: Conditional non-hostile
    addPacifistEntry("wolf", EntityWolf.class);

    // Mystical Worlds
    addPacifistEntry("beetle", EntityBeetle.class);
    addPacifistEntry("deer", EntityDeer.class);
    addPacifistEntry("fox", EntityFox.class);
    addPacifistEntry("frog", EntityFrog.class);
    addPacifistEntry("owl", EntityOwl.class);
    addPacifistEntry("sprout", EntitySprout.class);
    addPacifistEntry("silkworm", EntitySilkworm.class);
    // TODO: Conditional non-hostile
    addPacifistEntry("lava_cat", EntityLavaCat.class);
    addPacifistEntry("hellsprout", EntityHellSprout.class);
    addPacifistEntry("clam", EntityClam.class);
  }

  public static void addFlowerRecipe(String name, IBlockState state) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    FlowerRecipe recipe = new FlowerRecipe(rl, state);
    flowerRecipes.put(rl, recipe);
  }

  public static void addFlowerRecipe(String name, Block block, int meta) {
    addFlowerRecipe(name, block, meta, Collections.emptyList());
  }

  public static void addFlowerRecipe(String name, Block block, int meta, List<Ingredient> allowedSoils) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    FlowerRecipe recipe = new FlowerRecipe(rl, meta, block, allowedSoils);
    flowerRecipes.put(rl, recipe);
  }

  public static void removeFlowerRecipe(ResourceLocation name) {
    flowerRecipes.remove(name);
  }

  public static IBlockState getRandomFlowerRecipe(IBlockState soil) {
    if (flowerRecipes.isEmpty()) {
      return Blocks.YELLOW_FLOWER.getStateFromMeta(BlockFlower.EnumFlowerType.DANDELION.getMeta());
    }

    ItemStack soilStack = ItemUtil.stackFromState(soil);
    List<FlowerRecipe> matches = new ArrayList<>();
    for (FlowerRecipe recipe : flowerRecipes.values()) {
      if (recipe.getAllowedSoils().isEmpty()) {
        matches.add(recipe);
      } else {
        for (Ingredient allowedSoil : recipe.getAllowedSoils()) {
          if (allowedSoil.apply(soilStack)) {
            matches.add(recipe);
            break;
          }
        }
      }
    }
    if (matches.isEmpty()) {
      return null;
    }
    return matches.get(Util.rand.nextInt(matches.size())).getFlower();
  }

  public static Map<ResourceLocation, FlowerRecipe> getFlowerRecipes() {
    return flowerRecipes;
  }

  @Nullable
  public static FlowerRecipe getFlowerRecipe(IBlockState state) {
    Block block = state.getBlock();
    int meta = block.getMetaFromState(state);
    for (FlowerRecipe recipe : flowerRecipes.values()) {
      if (recipe.matches(block, meta)) {
        return recipe;
      }
    }
    return null;
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
  public static BarkRecipe getBarkRecipeByName(ResourceLocation name) {
    return barkRecipes.get(name);
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

  public static Map<ResourceLocation, BarkRecipe> getBarkRecipeMap() {
    return barkRecipes;
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

  public static void addTransmutationRecipe(String name, TransmutationRecipe recipe) {
    ResourceLocation n = new ResourceLocation(Roots.MODID, name);
    recipe.setRegistryName(n);
    transmutationRecipes.put(n, recipe);
  }

  public static void addTransmutationRecipe(TransmutationRecipe recipe) {
    ResourceLocation n = recipe.getRegistryName();
    transmutationRecipes.put(n, recipe);
  }

  public static void removeTransmutationRecipe(ResourceLocation name) {
    transmutationRecipes.remove(name);
  }

  public static TransmutationRecipe getTransmutationRecipe(ResourceLocation name) {
    return transmutationRecipes.get(name);
  }

  public static Collection<TransmutationRecipe> getTransmutationRecipes() {
    return transmutationRecipes.values();
  }

  public static List<TransmutationRecipe> getTransmutationRecipesFor(World world, BlockPos pos) {
    IBlockState state = world.getBlockState(pos);
    return getTransmutationRecipesFor(state, world, pos);
  }

  public static List<TransmutationRecipe> getTransmutationRecipesFor(IBlockState state, World world, BlockPos pos) {
    List<TransmutationRecipe> result = new ArrayList<>();
    for (TransmutationRecipe recipe : getTransmutationRecipes()) {
      if (recipe.matches(state, world, pos)) {
        result.add(recipe);
      }
    }
    return result;
  }

  public static void initTransmutationRecipes() {
    BlocksPredicate water = new WaterPredicate();
    BlocksPredicate lava = new LavaPredicate();
    StatePredicate wool = new StatePredicate(Blocks.WOOL.getDefaultState());
    LeavesPredicate leaves = new LeavesPredicate();
    StatePredicate cobblestone = new StatePredicate(Blocks.COBBLESTONE.getDefaultState());
    StatePredicate sand = new StatePredicate(Blocks.SAND.getDefaultState());
    StatePredicate cobblestoneSlab = new StatePredicate(ModBlocks.runestone_slab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM));

    TransmutationRecipe deadbush_cocoa = new TransmutationRecipe(Blocks.DEADBUSH.getDefaultState()).item(new ItemStack(Items.DYE, 3, EnumDyeColor.BROWN.getDyeDamage()));
    addTransmutationRecipe("deadbush_cocoa", deadbush_cocoa);

    TransmutationRecipe birch_jungle = new TransmutationRecipe(new PropertyPredicate(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH), BlockOldLog.VARIANT)).state(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE));
    addTransmutationRecipe("birch_jungle", birch_jungle);

    TransmutationRecipe birch_jungle_leaves = new TransmutationRecipe(new PropertyPredicate(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH), BlockOldLeaf.VARIANT)).state(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE));
    addTransmutationRecipe("birch_jungle_leaves", birch_jungle_leaves);

    TransmutationRecipe pumpkin_to_melon = new TransmutationRecipe(Blocks.PUMPKIN.getDefaultState()).state(Blocks.MELON_BLOCK.getDefaultState()).condition(new BlockStateBelow(water));
    addTransmutationRecipe("pumpkin_to_melon", pumpkin_to_melon);

    TransmutationRecipe pumpkin_to_cactus = new TransmutationRecipe(Blocks.PUMPKIN.getDefaultState()).state(Blocks.CACTUS.getDefaultState()).condition(new BlockStateBelow(sand));
    addTransmutationRecipe("pumpkin_to_cactus", pumpkin_to_cactus);

    TransmutationRecipe cocoa_to_carrot = new TransmutationRecipe(Blocks.COCOA.getDefaultState()).item(new ItemStack(Items.CARROT));
    addTransmutationRecipe("cocoa_to_carrot", cocoa_to_carrot);

    TransmutationRecipe carrot_to_beetroot = new TransmutationRecipe(new PropertyPredicate(Blocks.CARROTS.getDefaultState().withProperty(BlockCrops.AGE, 7), BlockCrops.AGE)).state(Blocks.BEETROOTS.getDefaultState().withProperty(BlockBeetroot.BEETROOT_AGE, 3));
    addTransmutationRecipe("carrot_to_beetroot", carrot_to_beetroot);

    TransmutationRecipe carpet_to_lilypad = new TransmutationRecipe(Blocks.CARPET.getDefaultState()).state(Blocks.WATERLILY.getDefaultState()).condition(new BlockStateBelow(water));
    addTransmutationRecipe("carpet_to_lilypad", carpet_to_lilypad);

    TransmutationRecipe trapdoor_to_cobweb = new TransmutationRecipe(Blocks.TRAPDOOR.getDefaultState()).state(Blocks.WEB.getDefaultState()).condition(new BlockStateBelow(wool));
    addTransmutationRecipe("trapdoor_to_cobweb", trapdoor_to_cobweb);

    TransmutationRecipe redstone_to_vines = new TransmutationRecipe(Blocks.REDSTONE_WIRE.getDefaultState()).item(new ItemStack(Blocks.VINE)).condition(new BlockStateAbove(cobblestone));
    addTransmutationRecipe("redstone_to_vines", redstone_to_vines);

    TransmutationRecipe melon_to_pumpkin = new TransmutationRecipe(Blocks.MELON_BLOCK.getDefaultState()).state(Blocks.PUMPKIN.getDefaultState()).condition(new BlockStateBelow(cobblestone));
    addTransmutationRecipe("melon_to_pumpkin", melon_to_pumpkin);

    TransmutationRecipe redstone_block_to_glowstone = new TransmutationRecipe(Blocks.REDSTONE_BLOCK.getDefaultState()).state(Blocks.GLOWSTONE.getDefaultState()).condition(new BlockStateBelow(lava));
    addTransmutationRecipe("redstone_block_to_glowstone", redstone_block_to_glowstone);

    TransmutationRecipe carpet_diorite = new TransmutationRecipe(Blocks.CARPET.getDefaultState()).state(Blocks.SNOW_LAYER.getDefaultState()).condition(new BlockStateBelow(new MultiStatePropertyPredicate(BlockStone.VARIANT, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE_SMOOTH))));
    addTransmutationRecipe("carpet_to_snow", carpet_diorite);

    TransmutationRecipe lever_brown_mushroom = new TransmutationRecipe(Blocks.LEVER.getDefaultState()).state(Blocks.BROWN_MUSHROOM.getDefaultState()).condition(new BlockStateBelow(new PropertyPredicate(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL), BlockDirt.VARIANT)));
    addTransmutationRecipe("lever_to_brown_mushroom", lever_brown_mushroom);

    TransmutationRecipe redstone_torch_red_mushroom = new TransmutationRecipe(Blocks.REDSTONE_TORCH.getDefaultState()).state(Blocks.RED_MUSHROOM.getDefaultState()).condition(new BlockStateBelow(new PropertyPredicate(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL), BlockDirt.VARIANT)));
    addTransmutationRecipe("redstone_torch_to_red_mushroom", redstone_torch_red_mushroom);
  }

  public static void addAnimalHarvestRecipe(EntityLivingBase entity) {
    addAnimalHarvestRecipe(EntityList.getEntityString(entity), entity);
  }

  public static void addAnimalHarvestRecipe(String name, EntityLivingBase entity) {
    addAnimalHarvestRecipe(name, entity.getClass());
  }

  public static void addAnimalHarvestRecipe(String name, Class<? extends EntityLivingBase> clazz) {
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

  public static Map<ResourceLocation, AnimalHarvestRecipe> getAnimalHarvestRecipes() {
    return harvestRecipes;
  }

  public static Map<ResourceLocation, AnimalHarvestFishRecipe> getAnimalHarvestFishRecipes() {
    return fishRecipes;
  }

  public static AnimalHarvestRecipe getAnimalHarvestRecipe(ResourceLocation location) {
    return harvestRecipes.getOrDefault(location, null);
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

  public static Set<Class<? extends EntityLivingBase>> getAnimalHarvestClasses() {
    if (harvestClasses == null || harvestClasses.size() != harvestRecipes.size()) {
      harvestClasses = new HashSet<>();
      for (AnimalHarvestRecipe recipe : harvestRecipes.values()) {
        harvestClasses.add(recipe.getHarvestClass());
      }
    }
    return harvestClasses;
  }

  public static void addAnimalHarvestFishRecipe(String name, ItemStack item, int weight) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    if (getAnimalHarvestFishRecipe(rl) != null) {
      return;
    }
    if (getAnimalHarvestFishRecipe(item) != null) {
      return;
    }
    AnimalHarvestFishRecipe recipe = new AnimalHarvestFishRecipe(rl, item, weight);
    fishRecipes.put(rl, recipe);
  }

  @Nullable
  public static AnimalHarvestFishRecipe getAnimalHarvestFishRecipe(ItemStack item) {
    for (AnimalHarvestFishRecipe recipe : fishRecipes.values()) {
      if (ItemUtil.equalWithoutSize(recipe.getItemStack(), item)) {
        return recipe;
      }
    }

    return null;
  }

  public static AnimalHarvestFishRecipe getAnimalHarvestFishRecipe(ResourceLocation resource) {
    return fishRecipes.get(resource);
  }

  public static boolean removeAnimalHarvestFishRecipe(ResourceLocation resource) {
    return fishRecipes.remove(resource) != null;
  }

  public static boolean removeAnimalHarvestFishRecipe(ItemStack item) {
    AnimalHarvestFishRecipe recipe = getAnimalHarvestFishRecipe(item);
    if (recipe == null) return false;
    return fishRecipes.remove(recipe.getRegistryName()) != null;
  }

  public static List<AnimalHarvestFishRecipe> getFishRecipes() {
    return Lists.newArrayList(fishRecipes.values());
  }

  public static void clearFishRecipes() {
    fishRecipes.clear();
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
    addAnimalHarvestRecipe("lava_cat", EntityLavaCat.class);
    addAnimalHarvestRecipe("hellsprout", EntityHellSprout.class);
    addAnimalHarvestRecipe("silkworm", EntitySilkworm.class);
    addAnimalHarvestRecipe("clam", EntityClam.class);
    // Fish recipes
    addAnimalHarvestFishRecipe("cod", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.getMetadata()), 60);
    addAnimalHarvestFishRecipe("salmon", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()), 25);
    addAnimalHarvestFishRecipe("clownfish", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()), 2);
    addAnimalHarvestFishRecipe("pufferfish", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()), 13);
  }

  public static void addRunicShearRecipe(RunicShearRecipe recipe) {
    if (recipe instanceof RunicShearEntityRecipe) {
      RunicShearEntityRecipe eRecipe = (RunicShearEntityRecipe) recipe;
      for (RunicShearEntityRecipe entityRecipe : runicShearEntityRecipes.values()) {
        if (entityRecipe.getClazz().equals(eRecipe.getClazz())) {
          Roots.logger.error("RunicShearEntityRecipe duplicate found for class " + eRecipe.getClazz().getSimpleName());
          return;
        }
      }

      runicShearEntityRecipes.put(eRecipe.getRegistryName(), eRecipe);
    } else {
      runicShearRecipes.put(recipe.getRegistryName(), recipe);
    }
  }

  public static RunicShearRecipe getRunicShearRecipe(IBlockState state) {
    for (RunicShearRecipe recipe : runicShearRecipes.values()) {
      if (recipe.matches(state)) {
        return recipe;
      }
    }
    return null;
  }

  public static void clearGeneratedEntityRecipes() {
    generatedEntityRecipes = null;
  }

  private static void generateEntityRecipes() {
    generatedEntityRecipes = new HashMap<>();
    for (RunicShearEntityRecipe recipe : runicShearEntityRecipes.values()) {
      generatedEntityRecipes.put(recipe.getClazz(), recipe);
    }
  }


  public static RunicShearEntityRecipe getRunicShearRecipe(EntityLivingBase entity) {
    if (generatedEntityRecipes == null || generatedEntityRecipes.isEmpty()) {
      generateEntityRecipes();
    }
    return generatedEntityRecipes.get(entity.getClass());
  }

  public static RunicShearRecipe getRunicShearRecipe(String name) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
    return runicShearRecipes.get(rl);
  }

  public static Set<RunicShearRecipe> getRunicShearRecipe(ItemStack stack) {
    Set<RunicShearRecipe> result = new HashSet<>();
    for (RunicShearRecipe recipe : runicShearRecipes.values()) {
      if (recipe.getDropMatch().test(stack)) {
        result.add(recipe);
      }
    }

    return result;
  }

  public static Set<RunicShearEntityRecipe> getRunicShearEntityRecipe(ItemStack stack) {
    Set<RunicShearEntityRecipe> result = new HashSet<>();
    for (RunicShearEntityRecipe recipe : runicShearEntityRecipes.values()) {
      if (recipe.getDropMatch().test(stack)) {
        result.add(recipe);
      }
    }

    return result;
  }

  public static Set<Class<? extends Entity>> getRunicShearEntities() {
    if (generatedEntityRecipes == null) {
      generateEntityRecipes();
    }
    return generatedEntityRecipes.keySet();
  }

  public static void addMortarRecipe(MortarRecipe recipe) {
    mortarRecipes.put(recipe.getRegistryName(), recipe);
  }

  public static MortarRecipe getMortarRecipe(List<ItemStack> items) {
    for (MortarRecipe mortarRecipe : mortarRecipes.values()) {
      if (mortarRecipe.matches(items)) {
        return mortarRecipe;
      }
    }
    return null;
  }

  public static MortarRecipe getMortarRecipe (ResourceLocation name) {
    return mortarRecipes.get(name);
  }

  public static void removeMortarRecipes(ItemStack output) {
    MortarRecipe recipeToRemove = getMortarRecipe(output);
    if (recipeToRemove != null) {
      while (recipeToRemove != null) {
        mortarRecipes.remove(recipeToRemove.getRegistryName());
        recipeToRemove = getMortarRecipe(output);
      }
    }
  }

  public static void removeMortarRecipe(ResourceLocation location) {
    mortarRecipes.remove(location);
  }

  public static void removeMortarRecipe(String name) {
    removeMortarRecipe(new ResourceLocation(Roots.MODID, name));
  }

  public static MortarRecipe getMortarRecipe(ItemStack output) {
    for (MortarRecipe mortarRecipe : mortarRecipes.values()) {
      if (ItemUtil.equalWithoutSize(mortarRecipe.getResult(), output)) {
        return mortarRecipe;
      }
    }
    return null;
  }

  public static List<MortarRecipe> getMortarRecipes (String name, int meta) {
    List<MortarRecipe> recipes = new ArrayList<>();
    for (MortarRecipe recipe : mortarRecipes.values()) {
      if (recipe.getResult().getItem().getRegistryName().toString().equals(name) && (recipe.getResult().getMetadata() == meta || meta == -1)) {
        recipes.add(recipe);
      }
    }
    return recipes;
  }

  public static MortarRecipe getMortarRecipe(String name, int meta) {
    ResourceLocation item = new ResourceLocation(name);
    for (MortarRecipe mortarRecipe : mortarRecipes.values()) {
      ItemStack output = mortarRecipe.getResult();
      if (Objects.equals(output.getItem().getRegistryName(), item) && output.getMetadata() == meta) {
        return mortarRecipe;
      }
    }

    return null;
  }

  private static void addMortarRecipe(String name, ItemStack output, Ingredient input, float red1, float green1, float blue1, float red2, float green2, float blue2) {
    mortarRecipes.putAll(getMortarRecipeList(name, output, input, red1, green1, blue1, red2, green2, blue2));
  }

  public static Map<ResourceLocation, MortarRecipe> getMortarRecipeList(String name, ItemStack output, Ingredient input) {
    return getMortarRecipeList(name, output, input, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
  }

  public static Map<ResourceLocation, MortarRecipe> getMortarRecipeList(String name, ItemStack output, Ingredient input, float red1, float green1, float blue1, float red2, float green2, float blue2) {
    Map<ResourceLocation, MortarRecipe> result = new HashMap<>();
    ItemStack copy;
    List<Ingredient> ingredients = new ArrayList<>();
    int count = output.getCount();
    for (int i = 0; i < 5; i++) {
      ingredients.add(input);
      copy = output.copy();
      copy.setCount((i + 1) * count);
      MortarRecipe recipe = new MortarRecipe(copy, ingredients.toArray(new Ingredient[0]), red1, green1, blue1, red2, green2, blue2);
      recipe.setRegistryName(new ResourceLocation(Roots.MODID, name + "_" + (i + 1)));
      result.put(recipe.getRegistryName(), recipe);
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
    for (PyreCraftingRecipe pyreCraftingRecipe : pyreCraftingRecipes.values()) {
      if (pyreCraftingRecipe.matches(items)) {
        return pyreCraftingRecipe;
      }
    }
    return null;
  }

  public static void addFeyCraftingRecipe(ResourceLocation name, FeyCraftingRecipe recipe) {
    assert !feyCraftingRecipes.containsKey(name);

    feyCraftingRecipes.put(name, recipe);
  }

  public static void addPyreCraftingRecipe(ResourceLocation name, PyreCraftingRecipe recipe) {
    if (pyreCraftingRecipes.containsKey(name)) {
      throw new IllegalStateException("Invalid state: already have recipe registered for name " + name.toString());
    }

    pyreCraftingRecipes.put(name, recipe);
  }

  public static Map<ResourceLocation, FeyCraftingRecipe> getFeyCraftingRecipes() {
    return feyCraftingRecipes;
  }

  @Nullable
  public static FeyCraftingRecipe getFeyCraftingRecipe(ItemStack stack) {
    if (stack.isEmpty()) {
      return null;
    }
    for (FeyCraftingRecipe recipe : feyCraftingRecipes.values()) {
      if (ItemUtil.equalWithoutSize(recipe.getResult(), stack)) {
        return recipe;
      }
    }
    return null;
  }

  @Nullable
  public static FeyCraftingRecipe getFeyCraftingRecipe(List<ItemStack> items) {
    for (FeyCraftingRecipe recipe : feyCraftingRecipes.values()) {
      if (recipe.matches(items)) {
        return recipe;
      }
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
    return getCraftingRecipe(new ResourceLocation(Roots.MODID, recipeName));
  }

  public static PyreCraftingRecipe getCraftingRecipe(ResourceLocation recipeName) {
    return pyreCraftingRecipes.get(recipeName);
  }

  @Nullable
  public static PyreCraftingRecipe getCraftingRecipe(ItemStack output) {
    for (PyreCraftingRecipe recipe : pyreCraftingRecipes.values()) {
      if (ItemStack.areItemStacksEqual(recipe.getResult(), output)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removePyreCraftingRecipe(ResourceLocation name) {
    return pyreCraftingRecipes.remove(name) != null;
  }

  public static boolean removePyreCraftingRecipe(ItemStack output) {
    PyreCraftingRecipe recipe = getCraftingRecipe(output);
    if (recipe == null) {
      return false;
    }
    return removePyreCraftingRecipe(recipe.getRegistryName());
  }

  public static void removeFeyCraftingRecipe(ResourceLocation name) {
    feyCraftingRecipes.remove(name);
  }

  private static void addPyreCraftingRecipe(String recipeName, PyreCraftingRecipe pyreCraftingRecipe) {
    ResourceLocation rl = new ResourceLocation(Roots.MODID, recipeName);
    if (getCraftingRecipe(rl) != null) {
      throw new IllegalStateException("Already have a PyreCraftingRecipe registered for name: " + rl.toString());
    }
    pyreCraftingRecipes.put(rl, pyreCraftingRecipe.setName(recipeName));
  }

  private static void addFeyCraftingRecipe(String recipeName, FeyCraftingRecipe recipe) {
    ResourceLocation recipeId = new ResourceLocation(Roots.MODID, recipeName);
    if (feyCraftingRecipes.containsKey(recipeId)) {
      throw new IllegalStateException("Already have a FeyCraftingRecipe registered for name: " + recipeId.toString());
    }

    feyCraftingRecipes.put(recipeId, recipe.setName(recipeName));
  }

  public static Map<ResourceLocation, RunicShearRecipe> getRunicShearRecipes() {
    return runicShearRecipes;
  }

  public static Map<ResourceLocation, RunicShearEntityRecipe> getRunicShearEntityRecipes() {
    return runicShearEntityRecipes;
  }

  public static Map<Class<? extends Entity>, RunicShearEntityRecipe> getGeneratedEntityRecipes() {
    if (generatedEntityRecipes == null) {
      generateEntityRecipes();
    }

    return generatedEntityRecipes;
  }

  public static Map<ResourceLocation, PyreCraftingRecipe> getPyreCraftingRecipes() {
    return pyreCraftingRecipes;
  }

  public static Map<ResourceLocation, AnimalHarvestRecipe> getHarvestRecipes() {
    return harvestRecipes;
  }

  public static Collection<MortarRecipe> getMortarRecipes() {
    return mortarRecipes.values();
  }

  public static void initMortarRecipes() {
    addMortarRecipe("carapace_blue_dye", new ItemStack(Items.DYE, 1, EnumDyeColor.LIGHT_BLUE.getDyeDamage()), Ingredient.fromItem(epicsquid.mysticalworld.init.ModItems.carapace), 1, 1, 1, 1, 1, 1);
    addMortarRecipe("carrot_orange_dye", new ItemStack(Items.DYE, 1, EnumDyeColor.ORANGE.getDyeDamage()), new OreIngredient("cropCarrot"), 1, 1, 1, 1, 1, 1);
    addMortarRecipe("wheat_flour", new ItemStack(ModItems.flour), new OreIngredient("cropWheat"), 1f, 1f, 0f, 1f, 1f, 0f);
    addMortarRecipe("potato_flour", new ItemStack(ModItems.flour), new OreIngredient("cropPotato"), 1f, 1f, 0, 1f, 1f, 0f);
    addMortarRecipe("bone_to_bone_meal", new ItemStack(Items.DYE, 4, 15), new OreIngredient("bone"), 0f, 0f, 0f, 0f, 0f, 0f);
    addMortarRecipe("sugarcane_to_sugar", new ItemStack(Items.SUGAR, 2), new OreIngredient("sugarcane"), 0f, 0f, 0f, 1f, 1f, 1f);
    addMortarRecipe("rod_to_blaze_powder", new ItemStack(Items.BLAZE_POWDER, 5), Ingredient.fromItem(Items.BLAZE_ROD), 1, 1, 1, 1, 1, 1);
    addMortarRecipe("wool_to_string", new ItemStack(Items.STRING, 4), new OreIngredient("wool"), 1, 1, 1, 1, 1, 1);
    addMortarRecipe("cocoon_to_silk_thread", new ItemStack(epicsquid.mysticalworld.init.ModItems.silk_thread, 5), Ingredient.fromItem(epicsquid.mysticalworld.init.ModItems.silk_cocoon), 0, 0, 0, 0, 0, 0);
    addMortarRecipe("magma_cream_from_magma_blocks", new ItemStack(Items.MAGMA_CREAM, 2), Ingredient.fromItem(Item.getItemFromBlock(Blocks.MAGMA)), 1, 0, 0, 1, 0, 0);
    addMortarRecipe("flint_from_gravel", new ItemStack(Items.FLINT), Ingredient.fromItem(Item.getItemFromBlock(Blocks.GRAVEL)), 1, 1, 1, 1, 1, 1);

    for (Material metal : Materials.getMaterials()) {
      if (!metal.isEnabled()) continue;

      Item metalDust = metal.getDust();
      if (metalDust == null) {
        continue;
      }

      addMortarRecipe(metal.getOredictNameSuffix().toLowerCase() + "_to_dust", new ItemStack(metalDust), new OreIngredient("ingot" + metal.getOredictNameSuffix()), 82f / 255f, 92f / 255f, 114f / 255f, 160f / 255f, 167f / 255f, 183f / 255f);
    }

    if (ConfigManager.gold.enableDusts) {
      addMortarRecipe("gold_to_dust", new ItemStack(epicsquid.mysticalworld.init.ModItems.gold_dust), new OreIngredient("ingotGold"), 82f / 255f, 92f / 255f, 114f / 255f, 160f / 255f, 167f / 255f, 183f / 255f);
    }
    if (ConfigManager.iron.enableDusts) {
      addMortarRecipe("iron_to_dust", new ItemStack(epicsquid.mysticalworld.init.ModItems.iron_dust), new OreIngredient("ingotIron"), 82f / 255f, 92f / 255f, 114f / 255f, 160f / 255f, 167f / 255f, 183f / 255f);
    }

    addMortarRecipe("flowers_to_petals", new ItemStack(ModItems.petals), new OreIngredient("allFlowers"), 1f, 0f, 0f, 0f, 1f, 0f);
    addMortarRecipe("double_flowers_to_petals", new ItemStack(ModItems.petals, 2), new OreIngredient("allTallFlowers"), 1f, 0f, 0f, 0f, 1f, 0f);
    addMortarRecipe("runestone_to_runic_dust", new ItemStack(ModItems.runic_dust), new OreIngredient("runestone"), 0f, 0f, 1f, 60 / 255f, 0f, 1f);
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
    initSummonCreatureEntries();
    initChrysopoeiaRecipes();

    GameRegistry.addSmelting(ModItems.flour, new ItemStack(Items.BREAD), 0.125f);
    /*    GameRegistry.addSmelting(epicsquid.mysticalworld.init.ModItems.seeds, new ItemStack(epicsquid.mysticalworld.init.ModItems.cooked_seeds), 0.05f);*/ // TODO: Move to Mystical World
    GameRegistry.addSmelting(ModItems.pereskia_bulb, new ItemStack(ModItems.cooked_pereskia), 0.125f);

    initCraftingRecipes();
    RunicShearRecipes.initRecipes();

    event.getRegistry().register(new DyeRecipe().setRegistryName(new ResourceLocation(Roots.MODID, "pouch_dye_recipe")));
  }

  private static void initCraftingRecipes() {
    addPyreCraftingRecipe("infernal_bulb",
        new PyreCraftingRecipe(new ItemStack(ModItems.infernal_bulb, 3), 1).addIngredients(
            new OreIngredient("wildroot"),
            new ItemStack(Items.MAGMA_CREAM),
            new OreIngredient("gunpowder"),
            new ItemStack(Items.LAVA_BUCKET),
            new OreIngredient("dustGlowstone")));

    addPyreCraftingRecipe("dewgonia",
        new PyreCraftingRecipe(new ItemStack(ModItems.dewgonia, 3), 1).addIngredients(
            new OreIngredient("tallgrass"),
            new ItemStack(Items.SUGAR),
            new OreIngredient("dyeBlue"),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(Item.getItemFromBlock(Blocks.WATERLILY))));

    addPyreCraftingRecipe("cloud_berry",
        new PyreCraftingRecipe(new ItemStack(ModItems.cloud_berry, 3), 1).addIngredients(
            new OreIngredient("treeLeaves"),
            new OreIngredient("tallgrass"),
            new OreIngredient("wool"),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.terra_moss)));

    addPyreCraftingRecipe("stalicripe",
        new PyreCraftingRecipe(new ItemStack(ModItems.stalicripe, 3), 1).addIngredients(
            new ItemStack(Items.FLINT),
            new OreIngredient("stone"),
            new OreIngredient("ingotIron"),
            new OreIngredient("wildroot"),
            new OreIngredient("dustRedstone")));

    addPyreCraftingRecipe("moonglow_leaf",
        new PyreCraftingRecipe(new ItemStack(ModItems.moonglow_leaf, 3), 1).addIngredients(
            new OreIngredient("treeLeaves"),
            new OreIngredient("blockGlass"),
            new OreIngredient("gemQuartz"),
            new ItemStack(ModItems.bark_birch),
            new ItemStack(ModItems.bark_birch)));

    addPyreCraftingRecipe("pereskia",
        new PyreCraftingRecipe(new ItemStack(ModItems.pereskia, 3), 1).addIngredients(
            new OreIngredient("wildroot"),
            new ItemStack(Items.SPECKLED_MELON),
            new OreIngredient("dustRedstone"),
            new ItemStack(Items.BEETROOT),
            new OreIngredient("sugarcane")));

    addPyreCraftingRecipe("baffle_cap",
        new PyreCraftingRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom), 3), 1).addIngredients(
            new ItemStack(ModItems.terra_moss),
            new ItemStack(Items.ROTTEN_FLESH),
            new ItemStack(Items.WHEAT_SEEDS),
            new ItemStack(Blocks.RED_MUSHROOM),
            new ItemStack(Blocks.BROWN_MUSHROOM)));

    // Cooking!!!
    addPyreCraftingRecipe("cooked_seeds", new PyreCraftingRecipe(new ItemStack(epicsquid.mysticalworld.init.ModItems.cooked_seeds, 5), 1).addIngredients(
        new ItemStack(epicsquid.mysticalworld.init.ModItems.seeds),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.seeds),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.seeds),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.seeds),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.seeds)).setBurnTime(20));

    addPyreCraftingRecipe("dead_bush", new PyreCraftingRecipe(new ItemStack(Blocks.DEADBUSH, 3))
        .addIngredients(
            new OreIngredient("treeSapling"),
            new OreIngredient("treeSapling"),
            new OreIngredient("treeSapling"),
            new OreIngredient("treeSapling"),
            new OreIngredient("treeSapling")));

    addPyreCraftingRecipe("cooked_potato", new PyreCraftingRecipe(new ItemStack(Items.BAKED_POTATO, 5), 1).addIngredients(
        new OreIngredient("cropPotato"),
        new OreIngredient("cropPotato"),
        new OreIngredient("cropPotato"),
        new OreIngredient("cropPotato"),
        new OreIngredient("cropPotato")).setBurnTime(300));

    addPyreCraftingRecipe("cooked_chicken", new PyreCraftingRecipe(new ItemStack(Items.COOKED_CHICKEN, 5), 1).addIngredients(
        new ItemStack(Items.CHICKEN),
        new ItemStack(Items.CHICKEN),
        new ItemStack(Items.CHICKEN),
        new ItemStack(Items.CHICKEN),
        new ItemStack(Items.CHICKEN)).setBurnTime(300));

    Ingredient acceptableFish = Ingredient.fromStacks(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()), new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.getMetadata()));

    addPyreCraftingRecipe("cooked_fish", new PyreCraftingRecipe(new ItemStack(Items.COOKED_FISH, 5), 1).addIngredients(
        acceptableFish,
        acceptableFish,
        acceptableFish,
        acceptableFish,
        acceptableFish).setBurnTime(300));

    addPyreCraftingRecipe("cooked_salmon", new PyreCraftingRecipe(new ItemStack(Items.COOKED_FISH, 5, ItemFishFood.FishType.SALMON.getMetadata()), 1).addIngredients(
        new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()),
        new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()),
        new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()),
        new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()),
        new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata())).setBurnTime(300));

    addPyreCraftingRecipe("steak", new PyreCraftingRecipe(new ItemStack(Items.COOKED_BEEF, 5), 1).addIngredients(
        new ItemStack(Items.BEEF),
        new ItemStack(Items.BEEF),
        new ItemStack(Items.BEEF),
        new ItemStack(Items.BEEF),
        new ItemStack(Items.BEEF)).setBurnTime(300));

    addPyreCraftingRecipe("cooked_mutton", new PyreCraftingRecipe(new ItemStack(Items.COOKED_MUTTON, 5), 1).addIngredients(
        new ItemStack(Items.MUTTON),
        new ItemStack(Items.MUTTON),
        new ItemStack(Items.MUTTON),
        new ItemStack(Items.MUTTON),
        new ItemStack(Items.MUTTON)).setBurnTime(300));

    addPyreCraftingRecipe("cooked_porkchop", new PyreCraftingRecipe(new ItemStack(Items.COOKED_PORKCHOP, 5), 1).addIngredients(
        new ItemStack(Items.PORKCHOP),
        new ItemStack(Items.PORKCHOP),
        new ItemStack(Items.PORKCHOP),
        new ItemStack(Items.PORKCHOP),
        new ItemStack(Items.PORKCHOP)).setBurnTime(300));

    addPyreCraftingRecipe("cooked_rabbit", new PyreCraftingRecipe(new ItemStack(Items.COOKED_RABBIT, 5), 1).addIngredients(
        new ItemStack(Items.RABBIT),
        new ItemStack(Items.RABBIT),
        new ItemStack(Items.RABBIT),
        new ItemStack(Items.RABBIT),
        new ItemStack(Items.RABBIT)).setBurnTime(300));

    addPyreCraftingRecipe("cooked_pereskia", new PyreCraftingRecipe(new ItemStack(ModItems.cooked_pereskia, 5), 1).addIngredients(
        new ItemStack(ModItems.pereskia_bulb),
        new ItemStack(ModItems.pereskia_bulb),
        new ItemStack(ModItems.pereskia_bulb),
        new ItemStack(ModItems.pereskia_bulb),
        new ItemStack(ModItems.pereskia_bulb)).setBurnTime(300));

    addPyreCraftingRecipe("cooked_aubergine", new PyreCraftingRecipe(new ItemStack(epicsquid.mysticalworld.init.ModItems.cooked_aubergine, 5), 1).addIngredients(
        Ingredients.AUBERGINE,
        Ingredients.AUBERGINE,
        Ingredients.AUBERGINE,
        Ingredients.AUBERGINE,
        Ingredients.AUBERGINE).setBurnTime(300));

    addPyreCraftingRecipe("cooked_squid", new PyreCraftingRecipe(new ItemStack(epicsquid.mysticalworld.init.ModItems.cooked_squid, 10), 1).addIngredients(
        new ItemStack(epicsquid.mysticalworld.init.ModItems.raw_squid),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.raw_squid),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.raw_squid),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.raw_squid),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.raw_squid)).setBurnTime(300));

    addPyreCraftingRecipe("cooked_venison", new PyreCraftingRecipe(new ItemStack(epicsquid.mysticalworld.init.ModItems.cooked_venison, 5), 1).addIngredients(
        new ItemStack(epicsquid.mysticalworld.init.ModItems.venison),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.venison),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.venison),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.venison),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.venison)).setBurnTime(300));

    // END OF COOKING

    addFeyCraftingRecipe("unending_bowl",
        new FeyCraftingRecipe(new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.unending_bowl)), 2).addIngredients(
            new ItemStack(Items.WATER_BUCKET),
            new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.mortar)),
            new ItemStack(ModItems.dewgonia),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.dewgonia)));

    addFeyCraftingRecipe("elemental_soil",
        new FeyCraftingRecipe(new ItemStack(ModBlocks.elemental_soil, 4), 4).addIngredients(
            new OreIngredient("dirt"),
            new ItemStack(ModItems.terra_moss),
            new OreIngredient("wildroot"),
            new ItemStack(Blocks.GRAVEL),
            new OreIngredient("dye")));

    addFeyCraftingRecipe("living_pickaxe",
        new FeyCraftingRecipe(new ItemStack(ModItems.living_pickaxe), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_PICKAXE),
            new OreIngredient("wildroot"),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));

    addFeyCraftingRecipe("living_axe",
        new FeyCraftingRecipe(new ItemStack(ModItems.living_axe), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_AXE),
            new OreIngredient("wildroot"),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));

    addFeyCraftingRecipe("living_shovel",
        new FeyCraftingRecipe(new ItemStack(ModItems.living_shovel), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_SHOVEL),
            new OreIngredient("wildroot"),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));

    addFeyCraftingRecipe("living_hoe",
        new FeyCraftingRecipe(new ItemStack(ModItems.living_hoe), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_HOE),
            new OreIngredient("wildroot"),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));

    addFeyCraftingRecipe("living_sword",
        new FeyCraftingRecipe(new ItemStack(ModItems.living_sword), 1).addIngredients(
            new GoldOrSilverIngotIngredient(),
            new ItemStack(Items.WOODEN_SWORD),
            new OreIngredient("wildroot"),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));

    addFeyCraftingRecipe("living_arrow",
        new FeyCraftingRecipe(new ItemStack(ModItems.living_arrow, 6), 1).addIngredients(
            new OreIngredient("treeLeaves"),
            new OreIngredient("treeLeaves"),
            new OreIngredient("rootsBark"),
            new OreIngredient("wildroot"),
            new ItemStack(Items.FLINT)));

    addFeyCraftingRecipe("wildwood_quiver",
        new FeyCraftingRecipe(new ItemStack(ModItems.wildwood_quiver), 2).addIngredients(
            new OreIngredient("chestWood"),
            new ItemStack(ModItems.bark_wildwood),
            new ItemStack(ModItems.bark_wildwood),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.spirit_herb)));

    addFeyCraftingRecipe("wildwood_bow",
        new FeyCraftingRecipe(new ItemStack(ModItems.wildwood_bow), 2).addIngredients(
            new ItemStack(Items.BOW),
            new ItemStack(ModItems.bark_wildwood),
            new ItemStack(ModItems.bark_wildwood),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.spirit_herb)));

    addFeyCraftingRecipe("runic_shears",
        new FeyCraftingRecipe(new ItemStack(ModItems.runic_shears), 1).addIngredients(
            new ItemStack(Items.SHEARS),
            new ItemStack(ModItems.pereskia),
            new ItemStack(ModItems.pereskia),
            new OreIngredient("runestone"),
            new OreIngredient("runestone")));

    addFeyCraftingRecipe("runestone",
        new FeyCraftingRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.runestone), 8), 1).addIngredients(
            new MultiOreIngredient("gemLapis", "dustRedstone", "dustGlowstone", "dustPrismarine", "gemPrismarine", "gemEmerald", "gemDiamond", "gunpowder", "dustBlaze"),
            new OreIngredient("stone"),
            new OreIngredient("stone"),
            new OreIngredient("stone"),
            new OreIngredient("stone")));

    addFeyCraftingRecipe("runed_obsidian",
        new FeyCraftingRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.runed_obsidian), 4), 1).addIngredients(
            new ItemStack(ModItems.runic_dust),
            new OreIngredient("obsidian"),
            new OreIngredient("runestone"),
            new OreIngredient("obsidian"),
            new OreIngredient("runestone")));

    addFeyCraftingRecipe("runic_crafter",
        new FeyCraftingRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.runic_crafter), 1), 1)
            .addIngredients(
                new ItemStack(ModBlocks.fey_crafter),
                new ItemStack(ModBlocks.chiseled_runestone),
                new ItemStack(ModBlocks.chiseled_runestone),
                new ItemStack(ModItems.runic_dust),
                new ItemStack(ModItems.mystic_feather)));

    addFeyCraftingRecipe("wildwood_helmet", new FeyCraftingRecipe(new ItemStack(ModItems.wildwood_helmet), 1).addIngredients(
        new ItemStack(Items.IRON_HELMET),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new OreIngredient("plankWood"),
        new OreIngredient("gemDiamond")));

    addFeyCraftingRecipe("wildwood_chestplate", new FeyCraftingRecipe(new ItemStack(ModItems.wildwood_chestplate), 1).addIngredients(
        new ItemStack(Items.IRON_CHESTPLATE),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new OreIngredient("plankWood"),
        new OreIngredient("gemDiamond")));

    addFeyCraftingRecipe("wildwood_leggings", new FeyCraftingRecipe(new ItemStack(ModItems.wildwood_leggings), 1).addIngredients(
        new ItemStack(Items.IRON_LEGGINGS),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new OreIngredient("plankWood"),
        new OreIngredient("gemDiamond")));

    addFeyCraftingRecipe("wildwood_boots", new FeyCraftingRecipe(new ItemStack(ModItems.wildwood_boots), 1).addIngredients(
        new ItemStack(Items.IRON_BOOTS),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new OreIngredient("plankWood"),
        new OreIngredient("gemDiamond")));

    addFeyCraftingRecipe("component_pouch", new FeyCraftingRecipe(new ItemStack(ModItems.component_pouch), 1).addIngredients(
        new OreIngredient("chestWood"),
        new OreIngredient("wool"),
        new OreIngredient("wool"),
        new OreIngredient("wildroot"),
        new OreIngredient("rootsBark")));

    addFeyCraftingRecipe("apothecary_pouch", new ApothecaryPouchRecipe(new ItemStack(ModItems.apothecary_pouch), 1).addIngredients(
        new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom)),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.bark_wildwood),
        new ItemStack(ModItems.spirit_herb),
        new ItemStack(ModItems.component_pouch)
    ));

    addFeyCraftingRecipe("fey_pouch", new FeyPouchRecipe(new ItemStack(ModItems.fey_pouch), 1).addIngredients(
        new ItemStack(ModItems.herb_pouch),
        new ItemStack(ModItems.fey_leather),
        new ItemStack(ModItems.fey_leather),
        new ItemStack(ModItems.pereskia),
        new ItemStack(ModItems.mystic_feather)
    ));

    addFeyCraftingRecipe("salmon_of_knowledge", new SalmonRecipe(new ItemStack(ModItems.salmon), 1).addIngredients(
        new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()),
        new ItemStack(Items.EXPERIENCE_BOTTLE),
        new OreIngredient("rootsBark"),
        new ItemStack(Items.BOOK),
        new ItemStack(ModItems.terra_moss)
    ));

    addFeyCraftingRecipe("sylvan_helmet", new FeyCraftingRecipe(new ItemStack(ModItems.sylvan_helmet), 1).addIngredients(
        new ItemStack(ModItems.fey_leather),
        new OreIngredient("vine"),
        new ItemStack(ModItems.bark_birch),
        new OreFallbackIngredient("gemAmethyst", "gemDiamond"),
        new ItemStack(Items.IRON_HELMET)));

    addFeyCraftingRecipe("sylvan_chestplate", new FeyCraftingRecipe(new ItemStack(ModItems.sylvan_chestplate), 1).addIngredients(
        new ItemStack(ModItems.fey_leather),
        new OreIngredient("vine"),
        new ItemStack(ModItems.bark_birch),
        new OreFallbackIngredient("gemAmethyst", "gemDiamond"),
        new ItemStack(Items.IRON_CHESTPLATE)));

    addFeyCraftingRecipe("sylvan_leggings", new FeyCraftingRecipe(new ItemStack(ModItems.sylvan_leggings), 1).addIngredients(
        new ItemStack(ModItems.fey_leather),
        new OreIngredient("vine"),
        new ItemStack(ModItems.bark_birch),
        new OreFallbackIngredient("gemAmethyst", "gemDiamond"),
        new ItemStack(Items.IRON_LEGGINGS)));

    addFeyCraftingRecipe("sylvan_boots", new FeyCraftingRecipe(new ItemStack(ModItems.sylvan_boots), 1).addIngredients(
        new ItemStack(ModItems.fey_leather),
        new OreIngredient("vine"),
        new ItemStack(ModItems.bark_birch),
        new OreFallbackIngredient("gemAmethyst", "gemDiamond"),
        new ItemStack(Items.IRON_BOOTS)));


    addFeyCraftingRecipe("terrastone_pickaxe",
        new FeyCraftingRecipe(new ItemStack(ModItems.terrastone_pickaxe), 1).addIngredients(
            new OreIngredient("runestone"),
            new ItemStack(Items.STONE_PICKAXE),
            new ItemStack(ModItems.terra_moss),
            new OreIngredient("gemDiamond"),
            new OreIngredient("mossyCobblestone")));

    addFeyCraftingRecipe("terrastone_axe",
        new FeyCraftingRecipe(new ItemStack(ModItems.terrastone_axe), 1).addIngredients(
            new OreIngredient("runestone"),
            new ItemStack(Items.STONE_AXE),
            new ItemStack(ModItems.terra_moss),
            new OreIngredient("gemDiamond"),
            new OreIngredient("mossyCobblestone")));

    addFeyCraftingRecipe("terrastone_shovel",
        new FeyCraftingRecipe(new ItemStack(ModItems.terrastone_shovel), 1).addIngredients(
            new OreIngredient("runestone"),
            new ItemStack(Items.STONE_SHOVEL),
            new ItemStack(ModItems.terra_moss),
            new OreIngredient("gemDiamond"),
            new OreIngredient("mossyCobblestone")));

    addFeyCraftingRecipe("terrastone_hoe",
        new FeyCraftingRecipe(new ItemStack(ModItems.terrastone_hoe), 1).addIngredients(
            new OreIngredient("runestone"),
            new ItemStack(Items.STONE_HOE),
            new ItemStack(ModItems.terra_moss),
            new OreIngredient("gemDiamond"),
            new OreIngredient("mossyCobblestone")));

    addFeyCraftingRecipe("terrastone_sword",
        new FeyCraftingRecipe(new ItemStack(ModItems.terrastone_sword), 1).addIngredients(
            new OreIngredient("runestone"),
            new ItemStack(Items.STONE_SWORD),
            new ItemStack(ModItems.terra_moss),
            new OreIngredient("gemDiamond"),
            new OreIngredient("mossyCobblestone")));

    addFeyCraftingRecipe("runed_pickaxe",
        new FeyCraftingRecipe(new ItemStack(ModItems.runed_pickaxe), 1).addIngredients(
            new OreIngredient("runedObsidian"),
            new OreIngredient("runedObsidian"),
            new AccessibleCompoundIngredient(Ingredient.fromItem(Items.DIAMOND_PICKAXE), Ingredient.fromItem(Materials.amethyst.getPickaxe())),
            new OreIngredient("feyLeather"),
            new ItemStack(ModItems.stalicripe)));

    addFeyCraftingRecipe("runed_axe",
        new FeyCraftingRecipe(new ItemStack(ModItems.runed_axe), 1).addIngredients(
            new OreIngredient("runedObsidian"),
            new OreIngredient("runedObsidian"),
            new AccessibleCompoundIngredient(Ingredient.fromItem(Items.DIAMOND_AXE), Ingredient.fromItem(Materials.amethyst.getAxe())),
            new OreIngredient("feyLeather"),
            new ItemStack(ModItems.cloud_berry)));

    addFeyCraftingRecipe("runed_shovel",
        new FeyCraftingRecipe(new ItemStack(ModItems.runed_shovel), 1).addIngredients(
            new OreIngredient("runedObsidian"),
            new OreIngredient("runedObsidian"),
            new AccessibleCompoundIngredient(Ingredient.fromItem(Items.DIAMOND_SHOVEL), Ingredient.fromItem(Materials.amethyst.getShovel())),
            new OreIngredient("feyLeather"),
            new ItemStack(ModItems.dewgonia)));

    addFeyCraftingRecipe("runed_hoe",
        new FeyCraftingRecipe(new ItemStack(ModItems.runed_hoe), 1).addIngredients(
            new OreIngredient("runedObsidian"),
            new OreIngredient("runedObsidian"),
            new AccessibleCompoundIngredient(Ingredient.fromItem(Items.DIAMOND_HOE), Ingredient.fromItem(Materials.amethyst.getHoe())),
            new OreIngredient("feyLeather"),
            new ItemStack(ModItems.wildewheet)));

    addFeyCraftingRecipe("runed_sword",
        new FeyCraftingRecipe(new ItemStack(ModItems.runed_sword), 1).addIngredients(
            new OreIngredient("runedObsidian"),
            new OreIngredient("runedObsidian"),
            new AccessibleCompoundIngredient(Ingredient.fromItem(Items.DIAMOND_SWORD), Ingredient.fromItem(Materials.amethyst.getSword())),
            new OreIngredient("feyLeather"),
            new ItemStack(ModItems.infernal_bulb)));

    addFeyCraftingRecipe("runed_dagger",
        new FeyCraftingRecipe(new ItemStack(ModItems.runed_dagger), 1).addIngredients(
            new OreIngredient("runedObsidian"),
            new OreIngredient("runedObsidian"),
            new AccessibleCompoundIngredient(Ingredient.fromItem(ModItems.diamond_knife), Ingredient.fromItem(epicsquid.mysticalworld.init.ModItems.amethyst_knife)),
            new OreIngredient("feyLeather"),
            new ItemStack(ModItems.moonglow_leaf)));

    // END OF ARMOR/etc

    addFeyCraftingRecipe("mycelium", new FeyCraftingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.MYCELIUM), 5), 1).addIngredients(
        new OreIngredient("mushroom"),
        new OreIngredient("dirt"),
        new ItemStack(ModItems.terra_spores),
        new OreIngredient("dirt"),
        new ItemStack(ModItems.stalicripe)
    ));

    addFeyCraftingRecipe("clay", new FeyCraftingRecipe(new ItemStack(Items.CLAY_BALL, 10), 1).addIngredients(
        new OreIngredient("dirt"),
        new ItemStack(ModItems.terra_moss),
        new OreIngredient("sand"),
        new ItemStack(ModItems.stalicripe),
        new ItemStack(Items.WATER_BUCKET)
    ));

    addFeyCraftingRecipe("podzol", new FeyCraftingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.DIRT), 5, BlockDirt.DirtType.PODZOL.getMetadata()), 1).addIngredients(
        new ItemStack(Item.getItemFromBlock(Blocks.DIRT), 1, BlockDirt.DirtType.COARSE_DIRT.getMetadata()),
        new OreIngredient("rootsBark"),
        new OreIngredient("treeLeaves"),
        new ItemStack(ModItems.stalicripe),
        new ItemStack(Item.getItemFromBlock(Blocks.DIRT), 1, BlockDirt.DirtType.COARSE_DIRT.getMetadata())
    ));

    addFeyCraftingRecipe("sand", new FeyCraftingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.SAND), 5, BlockSand.EnumType.SAND.getMetadata()), 1).addIngredients(
        new OreIngredient("gravel"),
        new OreIngredient("gravel"),
        new OreIngredient("gravel"),
        new OreIngredient("gravel"),
        new ItemStack(ModItems.stalicripe)
    ));

    addFeyCraftingRecipe("gravel", new FeyCraftingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.GRAVEL), 5), 1).addIngredients(
        new OreIngredient("cobblestone"),
        new OreIngredient("cobblestone"),
        new OreIngredient("cobblestone"),
        new OreIngredient("cobblestone"),
        new ItemStack(ModItems.stalicripe)
    ));

    addFeyCraftingRecipe("red_sand", new FeyCraftingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.SAND), 2, BlockSand.EnumType.RED_SAND.getMetadata()), 1).addIngredients(
        new OreIngredient("dustRedstone"),
        new OreIngredient("sand"),
        new OreIngredient("dustRedstone"),
        new OreIngredient("sand"),
        new OreIngredient("dyeRed")
    ));

    addFeyCraftingRecipe("gunpowder", new FeyCraftingRecipe(new ItemStack(Items.GUNPOWDER, 5), 1).addIngredients(
        new OreIngredient("netherrack"),
        new ItemStack(Items.COAL, 1, 1), // Charcoal
        new OreIngredient("netherrack"),
        new ItemStack(Items.MAGMA_CREAM, 1),
        new ItemStack(ModItems.infernal_bulb)
    ));
  }

  public static void afterHerbRegisterInit() {

  }

  private static void initDrops() {
  }

}
