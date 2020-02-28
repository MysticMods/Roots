package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.mechanics.Growth;
import epicsquid.roots.network.fx.MessageRampantLifeInfusionFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;
import java.util.Random;

public class SpellRampantGrowth extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("spirit_herb", 0.65));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("pereskia", 0.45));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15).setDescription("radius on the X axis of the area in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15).setDescription("radius on the Y axis of the area in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15).setDescription("radius on the Z axis of the area in which the spell takes effect");
  public static Property<Integer> PROP_TICKS = new Property<>("ticks", 3).setDescription("the number of times a random chance to grow the crop is applied every tick");
  public static Property<Integer> PROP_COUNT = new Property<>("count", 2).setDescription("the number of crops selected to be grown each tick");
  public static Property<Integer> PROP_ADDITIONAL_COUNT = new Property<>("additional_count", 4).setDescription("an additional number of crops from zero to the specified value minus 1 added to the default count");

  public static String spellName = "spell_rampant_growth";
  public static SpellRampantGrowth instance = new SpellRampantGrowth(spellName);

  private int radius_x, radius_y, radius_z, ticks, additionalCount, count;

  public SpellRampantGrowth(String name) {
    super(name, TextFormatting.DARK_AQUA, 224f / 255f, 135f / 255f, 40f / 255f, 46f / 255f, 94f / 255f, 93f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_TICKS, PROP_ADDITIONAL_COUNT, PROP_COUNT);
  }

  @Override
  public void init () {
    addIngredients(
        new OreIngredient("treeSapling"),
        new ItemStack(Items.GOLDEN_APPLE),
        new OreIngredient("treeSapling"),
        new ItemStack(ModItems.spirit_herb),
        new ItemStack(ModItems.pereskia)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules, int ticks) {
    List<BlockPos> positions = Growth.collect(player.world, player.getPosition(), radius_x, radius_y, radius_z);
    if (positions.isEmpty()) return false;
    if (!player.world.isRemote) {
      for (int i = 0; i < count + player.world.rand.nextInt(additionalCount); i++) {
        BlockPos pos = positions.get(player.world.rand.nextInt(positions.size()));
        IBlockState state = player.world.getBlockState(pos);
        for (int j = 0; j < this.ticks; j++) {
          state.getBlock().randomTick(player.world, pos, state, new Random());
        }
        if (player.world.rand.nextInt(3) == 0) {
          PacketHandler.sendToAllTracking(new MessageRampantLifeInfusionFX(pos.getX(), pos.getY(), pos.getZ()), player);
        }
      }
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
    this.ticks = properties.get(PROP_TICKS);
    this.count = properties.get(PROP_COUNT);
    this.additionalCount = properties.get(PROP_ADDITIONAL_COUNT);
  }
}
