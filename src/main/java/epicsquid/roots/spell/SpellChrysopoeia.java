package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageChrysopoeiaFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.recipe.ChrysopoeiaRecipe;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class SpellChrysopoeia extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("infernal_bulb", 0.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(new SpellCost("dewgonia", 0.5));
  public static Property.PropertyCost PROP_COST_3 = new Property.PropertyCost(new SpellCost("spirit_herb", 0.1));
  public static Property<Integer> PROP_INTERVAL = new Property<>("interval", 20).setDescription("interval in ticks between each transmutation");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_chrysopoeia");
  public static SpellChrysopoeia instance = new SpellChrysopoeia(spellName);

  private int interval;

  public SpellChrysopoeia(ResourceLocation name) {
    super(name, TextFormatting.GOLD, 176F / 255F, 169F / 255F, 158F / 255F, 224F / 255F, 174F / 255F, 99F / 255F);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_COST_3, PROP_INTERVAL);
/*    acceptsModifiers(OVER1, BY1, BY4, OVER2, REPETITION, OVER3, OVER4, BY3, BY2);*/
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("ingotIron"),
        new OreIngredient("ingotGold"),
        new OreIngredient("dustRedstone"),
        new ItemStack(Items.MAGMA_CREAM),
        new OreIngredient("gemDiamond")
    );
    setCastSound(ModSounds.Spells.CHRYSOPOEIA);
  }

  private static final Object2LongOpenHashMap<EntityPlayer> soundTimer = new Object2LongOpenHashMap<>();
  private static final long DELAY = 6 * 20;

  static {
    soundTimer.defaultReturnValue(-1);
  }

  public static boolean shouldPlaySound(EntityPlayer player) {
    long val = soundTimer.getLong(player);
    if (val == -1) {
      soundTimer.put(player, player.ticksExisted);
      return true;
    }
    if (player.ticksExisted >= val + DELAY || player.ticksExisted < val) {
      soundTimer.put(player, player.ticksExisted);
      return true;
    }
    return false;
  }

  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList info, int ticks) {
    World world = caster.world;
    ItemStack offHand = caster.getHeldItemOffhand();
    if (offHand.isEmpty()) {
      return false;
    }

    int over = 0;
    int by = 0;
/*    if (info.has(OVER1)) {
      over++;
    }
    if (info.has(OVER2)) {
      over++;
    }
    if (info.has(OVER3)) {
      over++;
    }
    if (info.has(OVER4)) {
      over++;
    }
    if (info.has(BY1)) {
      by++;
    }
    if (info.has(BY2)) {
      by++;
    }
    if (info.has(BY3)) {
      by++;
    }
    if (info.has(BY4)) {
      by++;
    }*/

    if (interval != 0 && ticks % interval != 0) {
      return false;
    }

    ChrysopoeiaRecipe recipe = ModRecipes.getChrysopoeiaRecipe(offHand);
    if (recipe == null) {
      if (!world.isRemote && shouldPlaySound(caster)) {
        world.playSound(null, caster.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.3f, 0.4f);
      }
      return false;
    }

    if (!world.isRemote) {
      if (shouldPlaySound(caster)) {
        world.playSound(null, caster.getPosition(), SoundEvents.BLOCK_METAL_PLACE, SoundCategory.PLAYERS, 0.3f, 0.4f);
      }
      ItemStack result = recipe.getCraftingResult(over);
/*      ItemStack byproduct = recipe.getByproduct(by);*/
      if (!caster.addItemStackToInventory(result)) {
        ItemUtil.spawnItem(world, caster.getPosition(), result);
      }
/*      if (!byproduct.isEmpty()) {
        if (!caster.addItemStackToInventory(byproduct)) {
          ItemUtil.spawnItem(world, caster.getPosition(), byproduct);
        }
      }*/
      ItemStack handResult = recipe.process(caster, offHand, over, by);
      caster.setHeldItem(EnumHand.OFF_HAND, handResult);

      MessageChrysopoeiaFX message = new MessageChrysopoeiaFX(caster);
      PacketHandler.sendToAllTracking(message, caster);
    }

    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.interval = properties.get(PROP_INTERVAL);
  }
}
