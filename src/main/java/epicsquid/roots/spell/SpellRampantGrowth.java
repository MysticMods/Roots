package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.mechanics.Growth;
import epicsquid.roots.network.fx.MessageRampantLifeInfusionFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.Random;

public class SpellRampantGrowth extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost("cost_1", new SpellCost("spirit_herb", 0.65));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost("cost_2", new SpellCost("pereskia", 0.45));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15);
  public static Property<Integer> PROP_TICKS = new Property<>("ticks", 3);
  public static Property<Integer> PROP_COUNT = new Property<>("count", 2);
  public static Property<Integer> PROP_ADDITIONAL_COUNT = new Property<>("additional_count", 4);

  public static String spellName = "spell_rampant_growth";
  public static SpellRampantGrowth instance = new SpellRampantGrowth(spellName);

  private int radius_x, radius_y, radius_z, ticks, additionalCount, count;

  public SpellRampantGrowth(String name) {
    super(name, TextFormatting.DARK_AQUA, 224f / 255f, 135f / 255f, 40f / 255f, 46f / 255f, 94f / 255f, 93f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_TICKS, PROP_ADDITIONAL_COUNT, PROP_COUNT);

    addIngredients(
        new ItemStack(Blocks.SAPLING, 1, 5),
        new ItemStack(Items.GOLDEN_APPLE),
        new ItemStack(Blocks.SAPLING, 1, 4),
        new ItemStack(ModItems.spirit_herb),
        new ItemStack(ModItems.pereskia)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    List<BlockPos> positions = Growth.collect(player.world, player.getPosition(), radius_x, radius_y, radius_z);
    if (positions.isEmpty()) return false;
    if (!player.world.isRemote) {
      for (int i = 0; i < count + player.world.rand.nextInt(additionalCount); i++) {
        BlockPos pos = positions.get(player.world.rand.nextInt(positions.size()));
        IBlockState state = player.world.getBlockState(pos);
        for (int j = 0; j < ticks; j++) {
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
  public void finalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);

    SpellCost cost = properties.getProperty(PROP_COST_1);
    addCost(cost.getHerb(), cost.getCost());
    cost = properties.getProperty(PROP_COST_2);
    addCost(cost.getHerb(), cost.getCost());

    this.radius_x = properties.getProperty(PROP_RADIUS_X);
    this.radius_y = properties.getProperty(PROP_RADIUS_Y);
    this.radius_z = properties.getProperty(PROP_RADIUS_Z);
    this.ticks = properties.getProperty(PROP_TICKS);
    this.count = properties.getProperty(PROP_COUNT);
    this.additionalCount = properties.getProperty(PROP_ADDITIONAL_COUNT);
  }
}
