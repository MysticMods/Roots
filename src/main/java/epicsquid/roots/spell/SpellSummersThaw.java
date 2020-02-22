package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageThawFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SpellSummersThaw extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(20);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("infernal_bulb", 0.25));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 5);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 5);

  public static String spellName = "spell_summers_thaw";
  public static SpellSummersThaw instance = new SpellSummersThaw(spellName);

  private int radius_x, radius_y, radius_z;

  public SpellSummersThaw(String name) {
    super(name, TextFormatting.AQUA, 25F/255F, 1F, 235F/255F, 252F/255F, 166F/255F, 37F/255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z);
  }

  @Override
  public void init() {
    addIngredients(
            new ItemStack(ModItems.bark_acacia),
            new ItemStack(Blocks.TORCH),
            new ItemStack(ModItems.infernal_bulb),
            new ItemStack(Items.GUNPOWDER),
            new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta())
    );
  }

  private IBlockState mutate (IBlockState incoming) {
    Block block = incoming.getBlock();
    if (block == Blocks.SNOW_LAYER) {
      return Blocks.AIR.getDefaultState();
    } else if (block == Blocks.ICE) {
      return Blocks.WATER.getDefaultState();
    } else if (block == Blocks.SNOW) {
      return Blocks.WATER.getDefaultState();
    } else if (block == Blocks.PACKED_ICE) {
      return Blocks.ICE.getDefaultState();
    } else {
      return incoming;
    }
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
    BlockPos pos = caster.getPosition();
    World world = caster.world;
    int mX = pos.getX();
    int mY = pos.getY();
    int mZ = pos.getZ();

    List<BlockPos> affectedBlocks = new ArrayList<>();

    for (int x = mX - radius_x; x < mX + radius_x; x++) {
      for (int y = mY - radius_y; y < mY + radius_y; y++) {
        for (int z = mZ - radius_z; z < mZ + radius_z; z++) {
          BlockPos thisPos = new BlockPos(x, y, z);
          IBlockState state = world.getBlockState(thisPos);
          IBlockState mutated = mutate(state);
          if (state == mutated) {
            continue;
          }

          affectedBlocks.add(thisPos);

          if (!world.isRemote) {
            world.setBlockState(thisPos, mutated, 3);
          }


        }
      }
    }

    if (!affectedBlocks.isEmpty()) {
      PacketHandler.sendToAllTracking(new MessageThawFX(affectedBlocks), caster);
    }

    return affectedBlocks.isEmpty();
  }

  public int[] getRadius() {
    return new int[]{radius_x, radius_y, radius_z};
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.radius_x = properties.getProperty(PROP_RADIUS_X);
    this.radius_y = properties.getProperty(PROP_RADIUS_Y);
    this.radius_z = properties.getProperty(PROP_RADIUS_Z);
  }
}
