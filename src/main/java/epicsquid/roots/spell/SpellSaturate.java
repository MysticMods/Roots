package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageSaturationFX;
import epicsquid.roots.properties.Property;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
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
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildewheet", 0.7));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("terra_moss", 0.5));
  public static Property<Double> PROP_SATURATION_MULTIPLIER = new Property<>("saturation_multiplier", 0.5).setDescription("multiplier for the saturation value each food item gives");
  public static Property<Double> PROP_FOOD_MULTIPLIER = new Property<>("food_multiplier", 0.5).setDescription("multiplier for the food value each food item gives");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_saturate");
  public static SpellSaturate instance = new SpellSaturate(spellName);

  public static Modifier PERESKIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "big_bellied"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier WILDROOT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "root_lover"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier MOONGLOW_LEAF = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "inversion"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier SPIRIT_HERB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "iron_excess"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier BAFFLE_CAP = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "antivenom"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier CLOUD_BERRY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "scattered_goods"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier INFERNAL_BULB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "cooked_goods"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier STALICRIPE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "iron_gut"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier DEWGONIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "bottled_goods"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  private double saturation_multiplier, food_multiplier;
  private boolean suppressSound = false;

  public SpellSaturate(ResourceLocation name) {
    super(name, TextFormatting.GOLD, 225F / 255F, 52F / 255F, 246F / 255F, 232F / 42F, 232F / 255F, 42F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_SATURATION_MULTIPLIER, PROP_FOOD_MULTIPLIER);
    acceptsModifiers(PERESKIA, WILDROOT, MOONGLOW_LEAF, SPIRIT_HERB, BAFFLE_CAP, CLOUD_BERRY, INFERNAL_BULB, STALICRIPE, DEWGONIA);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.MUSHROOM_STEW),
        new ItemStack(Items.MILK_BUCKET),
        new ItemStack(ModItems.cooked_aubergine),
        new ItemStack(Items.PUMPKIN_PIE),
        new ItemStack(epicsquid.roots.init.ModItems.wildewheet)
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
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
    if (foods.isEmpty()) {
      return false;
    }
    List<ItemStack> sortedFoods = foods.keySet().stream().sorted((o1, o2) -> Double.compare(saturation(o2), saturation(o1))).collect(Collectors.toList());

    Object2IntOpenHashMap<ItemStack> usedFoods = new Object2IntOpenHashMap<>();

    for (ItemStack stack : sortedFoods) {
      double thisSaturation = saturation(stack);
      int thisFood = health(stack);
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

    if (newSat <= currentSat && newFood <= currentFood) {
      return false;
    }

    if (usedFoods.isEmpty()) {
      return false;
    }

    if (world.isRemote) {
      return true;
    }

    List<ItemStack> containers = new ArrayList<>();

    suppressSound = true;
    for (Object2IntMap.Entry<ItemStack> entry : usedFoods.object2IntEntrySet()) {
      int used = entry.getIntValue();
      ItemStack stack = entry.getKey();
      int index = foods.getInt(stack);
      ItemStack result = handler.extractItem(index, used, false);
      if (!result.isEmpty()) {
        for (int i = 0; i < result.getCount(); i++) {
          ItemStack container = result.onItemUseFinish(caster.world, caster);
          if (!container.isEmpty() && !ItemUtil.equalWithoutSize(container, result)) {
            containers.add(container);
          }
        }
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

    EntityPlayerMP playerMP = (EntityPlayerMP) caster;
    playerMP.connection.sendPacket(new SPacketUpdateHealth(playerMP.getHealth(), playerMP.getFoodStats().getFoodLevel(), playerMP.getFoodStats().getSaturationLevel()));

    MessageSaturationFX message = new MessageSaturationFX(caster);
    PacketHandler.sendToAllTracking(message, caster);

    return true;
  }

  private double saturation(ItemStack stack) {
    if (!(stack.getItem() instanceof ItemFood)) {
      return 0;
    }
    ItemFood item = (ItemFood) stack.getItem();
    int heal = item.getHealAmount(stack);
    float saturation = item.getSaturationModifier(stack);
    return (heal * saturation * 2f) * saturation_multiplier;
  }

  private int health(ItemStack stack) {
    if (!(stack.getItem() instanceof ItemFood)) {
      return 0;
    }
    ItemFood item = (ItemFood) stack.getItem();
    return (int) Math.floor(item.getHealAmount(stack) * food_multiplier);
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
    this.saturation_multiplier = properties.get(PROP_SATURATION_MULTIPLIER);
    this.food_multiplier = properties.get(PROP_FOOD_MULTIPLIER);
  }
}
