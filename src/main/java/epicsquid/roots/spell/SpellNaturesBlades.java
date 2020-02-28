package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.fx.MessageFallBladesFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
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

public class SpellNaturesBlades extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(160);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST = new Property.PropertyCost(0, new SpellCost("wildroot", 0.05));
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 5).setDescription("horizontal radius of the area in which this spell takes effect");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 3).setDescription("radius on the Y axis of the area in which this spell takes effect");
  public static Property<Integer> PROP_GRASS_CHANCE = new Property<>("grass_chance", 15).setDescription("chance for the spell to generate tall grass in the area (the higher the number is the lower the chance is: 1/x) [default: 1/15]");
  public static Property<Integer> PROP_UPGRADE_CHANCE = new Property<>("upgrade_chance", 30).setDescription("chance for the spell to upgrade tall grass in the area to a double tall grass (the higher the number is the lower the chance is: 1/x) [default: 1/30]");

  public static String spellName = "spell_natures_blades";
  public static SpellNaturesBlades instance = new SpellNaturesBlades(spellName);

  private int radius, radius_y, grass_chance, upgrade_chance;

  public SpellNaturesBlades(String name) {
    super(name, TextFormatting.GREEN, 64 / 255F, 240 / 255F, 24 / 255F, 26 / 255F, 110 / 255F, 13 / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST, PROP_RADIUS, PROP_RADIUS_Y, PROP_GRASS_CHANCE, PROP_UPGRADE_CHANCE);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("wildroot"),
        new OreIngredient("wildroot"),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(Items.GOLDEN_HOE),
        new OreIngredient("grass")
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules, int ticks) {
    World world = caster.world;
    List<BlockPos> blocks = Util.getBlocksWithinRadius(caster.world, caster.getPosition(), radius, radius_y, radius, pos -> (world.getBlockState(pos).getBlock() == Blocks.TALLGRASS && world.getBlockState(pos).getValue(BlockTallGrass.TYPE) != BlockTallGrass.EnumType.DEAD_BUSH && world.isAirBlock(pos.up())) || (world.getBlockState(pos) == Blocks.DIRT && world.getBlockState(pos).getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && world.isAirBlock(pos.up())) || (world.getBlockState(pos).getBlock() == Blocks.GRASS && world.isAirBlock(pos.up())));

    if (blocks.isEmpty()) {
      return false;
    }

    BlockPos pos = blocks.get(Util.rand.nextInt(blocks.size()));
    IBlockState state = world.getBlockState(pos);

    if (state.getBlock() == Blocks.DIRT) {
      if (!world.isRemote) {
        world.setBlockState(pos, Blocks.GRASS.getDefaultState());
      }
    } else if (state.getBlock() == Blocks.GRASS) {
      if (Util.rand.nextInt(grass_chance) == 0) {
        if (!world.isRemote) {
          world.setBlockState(pos.up(), Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS));
        }
      } else {
        return false;
      }
    } else {
      if (Util.rand.nextInt(upgrade_chance) == 0) {
        if (!world.isRemote) {
          BlockDoublePlant.EnumPlantType grass_type;
          if (state.getValue(BlockTallGrass.TYPE) == BlockTallGrass.EnumType.FERN) {
            grass_type = BlockDoublePlant.EnumPlantType.FERN;
          } else {
            grass_type = BlockDoublePlant.EnumPlantType.GRASS;
          }
          IBlockState grass = Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, grass_type).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER);
          world.setBlockState(pos, grass);
          grass = Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, grass_type).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER);
          world.setBlockState(pos.up(), grass);
        }
      } else {
        return false;
      }
    }

    if (!world.isRemote) {
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
    this.grass_chance = properties.get(PROP_GRASS_CHANCE);
    this.upgrade_chance = properties.get(PROP_UPGRADE_CHANCE);
  }
}
