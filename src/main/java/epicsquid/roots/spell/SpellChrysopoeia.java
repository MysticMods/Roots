package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.network.fx.MessageChrysopoeiaFX;
import epicsquid.roots.recipe.ChrysopoeiaRecipe;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellChrysopoeia extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("dewgonia", 0.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("stalicripe", 0.5));
  public static Property<Integer> PROP_INTERVAL = new Property<>("interval", 20);

  public static String spellName = "spell_chrysopoeia";
  public static SpellChrysopoeia instance = new SpellChrysopoeia(spellName);

  private int interval;

  public SpellChrysopoeia(String name) {
    super(name, TextFormatting.GOLD, 176F / 255F, 169F / 255F, 158F / 255F, 224F / 255F, 174F / 255F, 99F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_INTERVAL);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("ingotIron"),
        new OreIngredient("ingotGold"),
        new ItemStack(epicsquid.roots.init.ModItems.pestle),
        new ItemStack(Items.MAGMA_CREAM),
        new OreIngredient("gemDiamond")
    );
  }

  private static final Object2LongOpenHashMap<EntityPlayer> soundTimer = new Object2LongOpenHashMap<>();
  private static final long DELAY = 6 * 20;
  static {
    soundTimer.defaultReturnValue(-1);
  }

  public static boolean shouldPlaySound (EntityPlayer player) {
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
  public boolean cast(EntityPlayer caster, List<SpellModule> modules, int ticks) {
    World world = caster.world;
    ItemStack offHand = caster.getHeldItemOffhand();
    if (offHand.isEmpty()) {
      return false;
    }

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
      ItemStack result = recipe.getOutput().copy();
      if (!caster.addItemStackToInventory(result)) {
        ItemUtil.spawnItem(world, caster.getPosition(), result);
      }
      ItemStack handResult = recipe.process(caster, offHand);
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
