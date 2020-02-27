package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.network.fx.MessageSaturationFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FoodStats;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpellSaturate extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(500);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildewheet", 0.7));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("terra_moss", 0.5));
  public static Property<Double> PROP_MULTIPLIER = new Property<>("multiplier", 0.5);

  public static String spellName = "spell_saturate";
  public static SpellSaturate instance = new SpellSaturate(spellName);

  private double multiplier;

  public SpellSaturate(String name) {
    super(name, TextFormatting.GOLD, 235F / 255F, 183F / 255F, 52F / 255F, 156F / 255F, 100F / 255F, 16F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_MULTIPLIER);
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
  public boolean cast(EntityPlayer caster, List<SpellModule> modules, int ticks) {
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
      if (newSat >= 20) {
        break;
      }
    }

    if (newSat <= currentSat && newFood <= currentFood) {
      return false;
    }

    if (caster.world.isRemote) {
      return true;
    }

    double added = 0.0;
    int addedFood = 0;

    for (Object2IntMap.Entry<ItemStack> entry : usedFoods.object2IntEntrySet()) {
      int used = entry.getIntValue();
      ItemStack stack = entry.getKey();
      int index = foods.getInt(stack);
      ItemStack result = handler.extractItem(index, used, false);
      if (!result.isEmpty()) {
        for (int i = 0; i < result.getCount(); i++) {
          added += saturation(result);
          addedFood += health(result);
        }
      }
    }

    if (added >= 0) {
      stats.foodSaturationLevel = (float) Math.min(20, stats.foodSaturationLevel + added);
    }
    if (addedFood >= 0) {
      stats.setFoodLevel(Math.min(20, currentFood + addedFood));
    }

    EntityPlayerMP playerMP = (EntityPlayerMP) caster;
    playerMP.connection.sendPacket(new SPacketUpdateHealth(playerMP.getHealth(), playerMP.getFoodStats().getFoodLevel(), playerMP.getFoodStats().getSaturationLevel()));

    MessageSaturationFX message = new MessageSaturationFX(caster);
    PacketHandler.sendToAllTracking(message, caster);

    return true;
  }

  private double saturation (ItemStack stack) {
    if (!(stack.getItem() instanceof ItemFood)) {
      return 0;
    }
    ItemFood item = (ItemFood) stack.getItem();
    int heal = item.getHealAmount(stack);
    float saturation = item.getSaturationModifier(stack);
    return (heal * saturation * 2f) * multiplier;
  }

  private int health (ItemStack stack) {
    if (!(stack.getItem() instanceof ItemFood)) {
      return 0;
    }
    ItemFood item = (ItemFood) stack.getItem();
    return (int) Math.floor(item.getHealAmount(stack) * multiplier);
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.multiplier = properties.get(PROP_MULTIPLIER);
  }

}
