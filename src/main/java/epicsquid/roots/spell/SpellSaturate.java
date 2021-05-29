package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageSaturationFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.OreDictCache;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class SpellSaturate extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(500);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("wildewheet", 0.425));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(new SpellCost("terra_moss", 0.650));
  public static Property<Double> PROP_SATURATION_MULTIPLIER = new Property<>("saturation_multiplier", 0.5).setDescription("multiplier for the saturation value each food item gives");
  public static Property<Double> PROP_FOOD_MULTIPLIER = new Property<>("food_multiplier", 0.5).setDescription("multiplier for the food value each food item gives");
  public static Property<Double> PROP_ADDITIONAL_SATURATION_MULTIPLIER = new Property<>("increased_saturation_multiplier", 0.3).setDescription("the additional value added to the multiplier");
  public static Property<Double> PROP_ADDITIONAL_FOOD_MULTIPLIER = new Property<>("increased_food_multiplier", 0.3);
  public static Property<String> PROP_UNCOOKED_DICT = new Property<>("uncooked_dictionary", "rootsUncookedVegetables").setDescription("the ore dictionary name for thaumcraft.items that should be considered uncooked vegetables");
  public static Property<Double> PROP_UNCOOKED_ADDITIONAL_SATURATION = new Property<>("uncooked_saturation", 0.4).setDescription("how much additional saturation ratio uncooked food should provide");
  public static Property<Double> PROP_UNCOOKED_ADDITIONAL_FOOD = new Property<>("uncooked_food", 0.4).setDescription("how much additional food ratio uncooked food should provide");
  public static Property<String> PROP_COOKED_DICT = new Property<>("cooked_dictionary", "rootsCookedFoods").setDescription("the ore dictionary name for thaumcraft.items that should be considered cooked");
  public static Property<Double> PROP_COOKED_ADDITIONAL_SATURATION = new Property<>("cooked_saturation", 0.4).setDescription("how much additional saturation ratio cooked food should provide");
  public static Property<Double> PROP_COOKED_ADDITIONAL_FOOD = new Property<>("cooked_food", 0.4).setDescription("how much additional food ratio cooked food should provide");
  public static Property<String> PROP_BOTTLED_DICT = new Property<>("bottled_dictionary", "rootsBottledFood").setDescription("the ore dictionary name for thaumcraft.items that should be considered bottled goods");
  public static Property<Double> PROP_BOTTLED_ADDITIONAL_SATURATION = new Property<>("bottled_saturation", 0.4).setDescription("how much additional saturation ratio bottled food should provide");
  public static Property<Double> PROP_BOTTLED_ADDITIONAL_FOOD = new Property<>("bottled_food", 0.4).setDescription("how much additional food ratio bottled food should provide");
  public static Property<Integer> PROP_RESISTANCE_DURATION = new Property<>("resistance_duration", 10 * 20).setDescription("the duration of the resistance effect, multiplied by how much additional saturation there is");
  public static Property<Integer> PROP_RESISTANCE_AMPLIFIER = new Property<>("resistance_amplifier", 0).setDescription("the amplifier for the resistance effect");
  public static Property<Double> PROP_POISON_ADDITION_FOOD = new Property<>("poison_food", 0.7).setDescription("how much additional percentage of food should be applied when eating poisoned food");
  public static Property<Double> PROP_POISON_ADDITION_SATURATION = new Property<>("poison_saturation", 0.7).setDescription("how much additional percentage of saturation should be applied when eating poisoned food");
  public static Property<Double> PROP_NAUSEA_ADDITIONAL_FOOD = new Property<>("nausea_food", 0.7).setDescription("how much additional percentage of food should be applied when eating sickening food");
  public static Property<Double> PROP_NAUSEA_ADDITIONAL_SATURATION = new Property<>("nausea_saturation", 0.7).setDescription("how much additional percentage of saturation should be applied when eating sickening food");
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 6).setDescription("radius on the X axis of the area the spell has effect on");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5).setDescription("radius on the Y axis of the area the spell has effect on");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 6).setDescription("radius on the Z axis of the area the spell has effect on");
  public static Property<Integer> PROP_INVERSION_SATURATION_BOOST = new Property<>("inversion_saturation_boost", 5).setDescription("the value that the percentage ratio between max foot/saturation and the food/saturation value is added from; set lower to cause inversion to give less food");
  public static Property<Integer> PROP_INVERSION_FOOD_BOOST = new Property<>("inversion_food_boost", 4).setDescription("the value that the percentage ratio between max foot/saturation and the food/saturation value is added from; set lower to cause inversion to give less food (rounded down)");

  public static Modifier RATIO = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "big_bellied"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.6)));
  public static Modifier UNCOOKED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "root_lover"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.345)));
  public static Modifier INVERSION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "inversion"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.345)));
  public static Modifier RESISTANCE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "iron_excess"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.275)));
  public static Modifier POISONED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "antivenom"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.275)));
  public static Modifier ITEMS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "scattered_goods"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.125)));
  public static Modifier COOKED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "cooked_goods"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.275)));
  public static Modifier NAUSEA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "iron_gut"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.275)));
  public static Modifier BOTTLED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "bottled_goods"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.275)));

  static {
    // For sanity, don't cross-polinate
    UNCOOKED.addConflicts(INVERSION, POISONED, COOKED, NAUSEA, BOTTLED);
    INVERSION.addConflicts(POISONED, COOKED, NAUSEA, BOTTLED);
    POISONED.addConflicts(COOKED, NAUSEA, BOTTLED);
    COOKED.addConflicts(NAUSEA, BOTTLED);
    NAUSEA.addConflict(BOTTLED);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_saturate");
  public static SpellSaturate instance = new SpellSaturate(spellName);

  private String uncooked_dictionary, cooked_dictionary, bottled_dictionary;
  private int resistance_amplifier, resistance_duration, radius_x, radius_y, radius_z, inversion_food_boost, inversion_saturation_boost;
  private double saturation_multiplier, food_multiplier, additional_saturation_multiplier, additional_food_multiplier, poison_saturation, poison_food, uncooked_saturation, uncooked_food, cooked_saturation, cooked_food, bottled_saturation, bottled_food, nausea_saturation, nausea_food;
  private boolean suppressSound = false;

  public SpellSaturate(ResourceLocation name) {
    super(name, TextFormatting.GOLD, 225F / 255F, 52F / 255F, 246F / 255F, 232F / 42F, 232F / 255F, 42F / 255F);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_SATURATION_MULTIPLIER, PROP_FOOD_MULTIPLIER, PROP_ADDITIONAL_FOOD_MULTIPLIER, PROP_ADDITIONAL_SATURATION_MULTIPLIER, PROP_UNCOOKED_DICT, PROP_RESISTANCE_AMPLIFIER, PROP_RESISTANCE_DURATION, PROP_POISON_ADDITION_FOOD, PROP_POISON_ADDITION_SATURATION, PROP_COOKED_ADDITIONAL_FOOD, PROP_COOKED_ADDITIONAL_SATURATION, PROP_COOKED_DICT, PROP_UNCOOKED_ADDITIONAL_FOOD, PROP_UNCOOKED_ADDITIONAL_SATURATION, PROP_NAUSEA_ADDITIONAL_FOOD, PROP_NAUSEA_ADDITIONAL_SATURATION, PROP_BOTTLED_ADDITIONAL_FOOD, PROP_BOTTLED_ADDITIONAL_SATURATION, PROP_BOTTLED_DICT, PROP_INVERSION_SATURATION_BOOST, PROP_INVERSION_FOOD_BOOST);
    acceptsModifiers(RATIO, UNCOOKED, INVERSION, RESISTANCE, POISONED, ITEMS, COOKED, NAUSEA, BOTTLED);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.MUSHROOM_STEW),
        new ItemStack(Items.MILK_BUCKET),
        new ItemStack(ModItems.cooked_aubergine),
        new ItemStack(Items.EGG),
        new ItemStack(epicsquid.roots.init.ModItems.wildewheet)
    );
    setCastSound(ModSounds.Spells.SATURATE);
  }

  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList info, int ticks) {
    World world = caster.world;
    FoodStats stats = caster.getFoodStats();
    int currentFood = stats.getFoodLevel();
    double currentSat = stats.getSaturationLevel();
    if (currentSat >= 20 && currentFood >= 20) {
      return false;
    }

    double newSat = currentSat;
    int newFood = currentFood;

    IItemHandler handler = caster.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
    Object2IntOpenHashMap<ItemStack> foods = new Object2IntOpenHashMap<>();
    for (int i = 0; i < Objects.requireNonNull(handler).getSlots(); i++) {
      ItemStack inSlot = handler.getStackInSlot(i);
      if (inSlot.getItem() instanceof ItemFood) {
        foods.put(inSlot, i);
      }
    }
    List<EntityItem> entities = new ArrayList<>();
    if (info.has(ITEMS)) {
      entities.addAll(Util.getEntitiesWithinRadius(caster.world, EntityItem.class, caster.getPosition(), (float) radius_x, (float) radius_y, (float) radius_z));
      entities.removeIf(o -> !(o.getItem().getItem() instanceof ItemFood));
    }
    if (foods.isEmpty() && (entities.isEmpty() || !info.has(ITEMS))) {
      return false;
    }
    entities.sort((o1, o2) -> Double.compare(saturation(o2.getItem(), info), saturation(o1.getItem(), info)));
    List<ItemStack> sortedFoods = foods.keySet().stream().sorted((o1, o2) -> Double.compare(saturation(o2, info), saturation(o1, info))).collect(Collectors.toList());

    Object2IntOpenHashMap<ItemStack> usedFoods = new Object2IntOpenHashMap<>();

    for (ItemStack stack : sortedFoods) {
      double thisSaturation = saturation(stack, info);
      int thisFood = health(stack, info);
      int used = 0;
      for (int i = 0; i < stack.getCount(); i++) {
        newSat += thisSaturation;
        newFood += thisFood;
        used++;
        if (newSat >= 20 && newFood >= 20) {
          break;
        }
      }
      if (used > 0) {
        usedFoods.put(stack, used);
      }
      if (newSat >= 20 && newFood >= 20) {
        break;
      }
    }

    List<EntityItem> usedItems = new ArrayList<>();
    List<ItemStack> consumedStacks = new ArrayList<>();

    if (info.has(ITEMS)) {
      for (EntityItem item : entities) {
        ItemStack stack = item.getItem();
        double thisSaturation = saturation(stack, info);
        int thisFood = health(stack, info);
        int used = 0;
        for (int i = 0; i < stack.getCount(); i++) {
          newSat += thisSaturation;
          newFood += thisFood;
          used++;
          if (newSat >= 20 && newFood >= 20) {
            break;
          }
        }
        if (used > 0) {
          if (!caster.world.isRemote) {
            ItemStack stack2 = stack.copy();
            stack2.setCount(used);
            consumedStacks.add(stack2);
            stack.shrink(used);
          }
          usedItems.add(item);
        }
        if (newSat >= 20 && newFood >= 20) {
          break;
        }
      }

      if (!caster.world.isRemote) {
        for (EntityItem item : usedItems) {
          if (item.getItem().isEmpty()) {
            caster.world.removeEntity(item);
          }
        }
      }
    }

    if (newSat <= currentSat && newFood <= currentFood) {
      return false;
    }

    if (usedFoods.isEmpty() && usedItems.isEmpty()) {
      return false;
    }

    if (world.isRemote) {
      return true;
    }

    List<ItemStack> containers = new ArrayList<>();

    suppressSound = true;
    PotionEffect poison = null;
    PotionEffect hunger = null;
    PotionEffect blindness = null;
    PotionEffect wither = null;
    PotionEffect nausea = null;
    if (info.has(POISONED)) {
      poison = caster.getActivePotionEffect(MobEffects.POISON);
      caster.removeActivePotionEffect(MobEffects.POISON);
    }
    if (info.has(NAUSEA)) {
      nausea = caster.getActivePotionEffect(MobEffects.NAUSEA);
      caster.removeActivePotionEffect(MobEffects.NAUSEA);
      hunger = caster.getActivePotionEffect(MobEffects.HUNGER);
      caster.removeActivePotionEffect(MobEffects.HUNGER);
      blindness = caster.getActivePotionEffect(MobEffects.BLINDNESS);
      caster.removeActivePotionEffect(MobEffects.BLINDNESS);
      wither = caster.getActivePotionEffect(MobEffects.WITHER);
      caster.removeActivePotionEffect(MobEffects.WITHER);
    }
    for (Object2IntMap.Entry<ItemStack> entry : usedFoods.object2IntEntrySet()) {
      int used = entry.getIntValue();
      ItemStack stack = entry.getKey();
      int index = foods.getInt(stack);
      ItemStack result = handler.extractItem(index, used, false);

      if (!result.isEmpty()) {
        containers.addAll(handleContainers(caster, info, result));
      }
    }
    for (ItemStack result : consumedStacks) {
      containers.addAll(handleContainers(caster, info, result));
    }

    if (info.has(POISONED)) {
      caster.removePotionEffect(MobEffects.POISON);
      if (poison != null) {
        caster.addPotionEffect(poison);
      }
    }
    if (info.has(NAUSEA)) {
      caster.removePotionEffect(MobEffects.HUNGER);
      caster.removePotionEffect(MobEffects.NAUSEA);
      caster.removePotionEffect(MobEffects.BLINDNESS);
      caster.removePotionEffect(MobEffects.WITHER);
      if (hunger != null) {
        caster.addPotionEffect(hunger);
      }
      if (wither != null) {
        caster.addPotionEffect(wither);
      }
      if (nausea != null) {
        caster.addPotionEffect(nausea);
      }
      if (blindness != null) {
        caster.addPotionEffect(blindness);
      }
    }
    suppressSound = false;

    if (stats.foodSaturationLevel < newSat) {
      stats.foodSaturationLevel = (float) Math.min(20, newSat);
    }
    if (stats.getFoodLevel() < newFood) {
      stats.setFoodLevel(Math.min(20, newFood));
    }

    if (!containers.isEmpty()) {
      for (ItemStack container : containers) {
        if (!caster.addItemStackToInventory(container)) {
          ItemUtil.spawnItem(caster.world, caster.getPosition(), container);
        }
      }
    }

    if (info.has(RESISTANCE)) {
      double excessSat = newFood - 20;
      int excessFood = newFood - 20;
      if (excessSat > 0 || excessFood > 0) {
        int amount = MathHelper.ceil(excessSat) * Math.min(1, excessFood);
        caster.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, amount * resistance_duration, resistance_amplifier, false, false));
      }
    }

    EntityPlayerMP playerMP = (EntityPlayerMP) caster;
    playerMP.connection.sendPacket(new SPacketUpdateHealth(playerMP.getHealth(), playerMP.getFoodStats().getFoodLevel(), playerMP.getFoodStats().getSaturationLevel()));

    MessageSaturationFX message = new MessageSaturationFX(caster);
    PacketHandler.sendToAllTracking(message, caster);

    return true;
  }

  private List<ItemStack> handleContainers(EntityPlayer caster, StaffModifierInstanceList info, ItemStack result) {
    List<ItemStack> containers = new ArrayList<>();
    for (int i = 0; i < result.getCount(); i++) {
      ItemStack container = result.onItemUseFinish(caster.world, caster);
      if (!container.isEmpty() && !ItemUtil.equalWithoutSize(container, result)) {
        containers.add(container);
      }
    }
    return containers;
  }

  private boolean poisoned(ItemStack stack) {
    if (!(stack.getItem() instanceof ItemFood)) {
      return false;
    }

    ItemFood food = (ItemFood) stack.getItem();
    return food.potionId != null && food.potionId.getPotion() == MobEffects.POISON;

  }

  private boolean nauseating(ItemStack stack) {
    if (!(stack.getItem() instanceof ItemFood)) {
      return false;
    }

    ItemFood food = (ItemFood) stack.getItem();
    if (food.potionId != null) {
      if (food.potionId.getPotion() == MobEffects.NAUSEA) {
        return true;
      }
      if (food.potionId.getPotion() == MobEffects.HUNGER) {
        return true;
      }
      if (food.potionId.getPotion() == MobEffects.BLINDNESS) {
        return true;
      }
      return food.potionId.getPotion() == MobEffects.WITHER;
    }

    return false;
  }

  private boolean cooked(ItemStack stack) {
    return OreDictCache.matches(cooked_dictionary, stack);
  }

  private boolean uncooked(ItemStack stack) {
    return OreDictCache.matches(uncooked_dictionary, stack);
  }

  private boolean bottled(ItemStack stack) {
    return OreDictCache.matches(bottled_dictionary, stack);
  }

  private double inversion(double value, int boost) {
    return value + (boost * ((20d - value) / 20));
  }

  private double saturation(ItemStack stack, StaffModifierInstanceList info) {
    if (!(stack.getItem() instanceof ItemFood)) {
      return 0;
    }
    ItemFood item = (ItemFood) stack.getItem();
    int heal = item.getHealAmount(stack);
    float saturation = item.getSaturationModifier(stack);
    double sat = saturation_multiplier;
    if (info.has(RATIO)) {
      sat += additional_saturation_multiplier;
    }
    if (info.has(POISONED) && poisoned(stack)) {
      sat += poison_saturation;
    }
    if (info.has(NAUSEA) && nauseating(stack)) {
      sat += nausea_saturation;
    }
    if (info.has(COOKED) && cooked(stack)) {
      sat += cooked_saturation;
    }
    if (info.has(UNCOOKED) && uncooked(stack)) {
      sat += uncooked_saturation;
    }
    if (info.has(BOTTLED) && bottled(stack)) {
      sat += bottled_saturation;
    }
    double result = (heal * saturation * 2) * sat;
    if (info.has(INVERSION)) {
      result = inversion(result, inversion_saturation_boost);
    }
    return result;
  }

  private int health(ItemStack stack, StaffModifierInstanceList info) {
    if (!(stack.getItem() instanceof ItemFood)) {
      return 0;
    }
    ItemFood item = (ItemFood) stack.getItem();
    double food = food_multiplier;
    if (info.has(RATIO)) {
      food += additional_food_multiplier;
    }
    if (info.has(POISONED) && poisoned(stack)) {
      food += poison_food;
    }
    if (info.has(NAUSEA) && nauseating(stack)) {
      food += nausea_food;
    }
    if (info.has(COOKED) && cooked(stack)) {
      food += cooked_food;
    }
    if (info.has(UNCOOKED) && uncooked(stack)) {
      food += uncooked_food;
    }
    if (info.has(BOTTLED) && bottled(stack)) {
      food += bottled_food;
    }
    double result = item.getHealAmount(stack) * food;
    if (info.has(INVERSION)) {
      result = inversion(result, inversion_food_boost);
    }
    return MathHelper.floor(result);
  }

  @SubscribeEvent
  public void onSoundPlayed(PlaySoundAtEntityEvent event) {
    if (event.getCategory() == SoundCategory.PLAYERS && event.getSound() == SoundEvents.ENTITY_PLAYER_BURP && suppressSound) {
      event.setCanceled(true);
    }
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
    this.saturation_multiplier = properties.get(PROP_SATURATION_MULTIPLIER);
    this.food_multiplier = properties.get(PROP_FOOD_MULTIPLIER);
    this.additional_food_multiplier = properties.get(PROP_ADDITIONAL_FOOD_MULTIPLIER);
    this.additional_saturation_multiplier = properties.get(PROP_ADDITIONAL_SATURATION_MULTIPLIER);
    this.uncooked_dictionary = properties.get(PROP_UNCOOKED_DICT);
    this.resistance_amplifier = properties.get(PROP_RESISTANCE_AMPLIFIER);
    this.resistance_duration = properties.get(PROP_RESISTANCE_DURATION);
    this.poison_food = properties.get(PROP_POISON_ADDITION_FOOD);
    this.poison_saturation = properties.get(PROP_POISON_ADDITION_SATURATION);
    this.uncooked_saturation = properties.get(PROP_UNCOOKED_ADDITIONAL_SATURATION);
    this.uncooked_food = properties.get(PROP_UNCOOKED_ADDITIONAL_FOOD);
    this.cooked_saturation = properties.get(PROP_COOKED_ADDITIONAL_SATURATION);
    this.cooked_food = properties.get(PROP_COOKED_ADDITIONAL_FOOD);
    this.cooked_dictionary = properties.get(PROP_COOKED_DICT);
    this.bottled_saturation = properties.get(PROP_BOTTLED_ADDITIONAL_SATURATION);
    this.bottled_food = properties.get(PROP_BOTTLED_ADDITIONAL_FOOD);
    this.bottled_dictionary = properties.get(PROP_BOTTLED_DICT);
    this.nausea_saturation = properties.get(PROP_NAUSEA_ADDITIONAL_SATURATION);
    this.nausea_food = properties.get(PROP_NAUSEA_ADDITIONAL_FOOD);
    this.inversion_food_boost = properties.get(PROP_INVERSION_FOOD_BOOST);
    this.inversion_saturation_boost = properties.get(PROP_INVERSION_SATURATION_BOOST);
  }
}
