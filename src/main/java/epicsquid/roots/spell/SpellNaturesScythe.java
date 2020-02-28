package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.fx.MessageFallBladesFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellNaturesScythe extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(160);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST = new Property.PropertyCost(0, new SpellCost("wildroot", 0.05));
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 5).setDescription("horizontal radius of the area in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 3).setDescription("radius on the Y axis of the area in which the spell takes effect");
  public static Property<Integer> PROP_INTERVAL = new Property<>("interval", 10).setDescription("interval in ticks between each harvested block");

  public static String spellName = "spell_natures_scythe";
  public static SpellNaturesScythe instance = new SpellNaturesScythe(spellName);

  private int radius, radius_y, interval;

  public SpellNaturesScythe(String name) {
    super(name, TextFormatting.DARK_GREEN, 64 / 255F, 240 / 255F, 24 / 255F, 26 / 255F, 110 / 255F, 13 / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST, PROP_RADIUS, PROP_RADIUS_Y, PROP_INTERVAL);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("wildroot"),
        new OreIngredient("wildroot"),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(Items.GOLDEN_SWORD),
        new OreIngredient("grass")
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules, int ticks) {
    if (ticks % interval != 0) {
      return false;
    }

    World world = caster.world;
    List<BlockPos> blocks = Util.getBlocksWithinRadius(caster.world, caster.getPosition(), radius, radius_y, radius, pos -> world.getBlockState(pos).getBlock() == Blocks.TALLGRASS || world.getBlockState(pos).getBlock() == Blocks.DOUBLE_PLANT && (world.getBlockState(pos).getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.FERN || world.getBlockState(pos).getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.GRASS));

    if (blocks.isEmpty()) {
      return false;
    }

    BlockPos pos = blocks.get(Util.rand.nextInt(blocks.size()));

    if (!world.isRemote) {
      world.destroyBlock(pos, true);
      world.playSound(null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.5f, 1f);
      PacketHandler.sendToAllTracking(new MessageFallBladesFX(pos.getX(), pos.getY(), pos.getZ(), false), world, pos);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius = properties.get(PROP_RADIUS);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.interval = properties.get(PROP_INTERVAL);
  }
}
