package epicsquid.roots.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class SpellDrizzle extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(500);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("dewgonia", 0.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("terra_moss", 0.5));
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(6000);
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 3);

  public int radius, duration;

  public static String spellName = "spell_drizzle";
  public static SpellDrizzle instance = new SpellDrizzle(spellName);

  public SpellDrizzle(String name) {
    super(name, TextFormatting.BLUE, 34 / 255F, 133 / 255F, 245 / 255F, 23 / 255F, 44 / 255F, 89 / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DURATION, PROP_RADIUS);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.WATER_BUCKET),
        new ItemStack(ModItems.dewgonia),
        new ItemStack(ModItems.terra_moss),
        new ItemStack(Item.getItemFromBlock(Blocks.WATERLILY)),
        new ItemStack(Items.CLAY_BALL)
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
    World world = caster.world;
    BlockPos pos = caster.getPosition();
    /*List<BlockPos> blocks = Util.getBlocksWithinRadius(world, pos, radius, 5, radius, ModBlocks.rain_storm);
    if (!blocks.isEmpty()) {
      return false;
    }

    if (!world.isAirBlock(pos.up().up())) {
      return false;
    }

    if (world.isRemote) {
      return true;
    }

    world.setBlockState(pos.up(), ModBlocks.rain_storm.getDefaultState());
    TileEntityRainStorm te = (TileEntityRainStorm) world.getTileEntity(pos.up().up());
    if (te != null) {
      te.setCountdown(duration);
    }*/

    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.duration = properties.getProperty(PROP_DURATION);
    this.radius = properties.getProperty(PROP_RADIUS);
  }
}
