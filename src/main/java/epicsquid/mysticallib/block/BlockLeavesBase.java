package epicsquid.mysticallib.block;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.item.ItemBlockLeaves;
import epicsquid.mysticallib.model.IModeledObject;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class BlockLeavesBase extends LeavesBlock implements IBlock, IModeledObject, INoCullBlock {
  private @Nonnull
  Item itemBlock;
  private List<ItemStack> drops;
  private boolean hasItems = true;
  private boolean noCull = true;
  private boolean isFlammable = false;
  private Supplier<ItemStack> sapling;
  private AxisAlignedBB box = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
  private int saplingChance;
  public @Nonnull
  String name;

  public BlockLeavesBase(float hardness, @Nonnull String name, Supplier<ItemStack> sapling, int saplingChance) {
    super();
    this.name = name;
    this.sapling = sapling;
    this.saplingChance = saplingChance;
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
    setHardness(hardness);
    itemBlock = new ItemBlockLeaves(this).setRegistryName(LibRegistry.getActiveModid(), name);
    this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
  }

  @Nonnull
  public BlockLeavesBase setFlammable(boolean flammable) {
    this.isFlammable = flammable;
    return this;
  }

  @Override
  @Nonnull
  public BlockLeavesBase setResistance(float resistance) {
    super.setResistance(resistance);
    return this;
  }

  @Nonnull
  public BlockLeavesBase setDrops(@Nonnull List<ItemStack> drops) {
    this.drops = drops;
    return this;
  }

  @Override
  protected int getSaplingDropChance(BlockState state) {
    return saplingChance;
  }

  @Override
  public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, BlockState state, int fortune) {
    Random rand = world instanceof World ? ((World) world).rand : new Random();
    int chance = getSaplingDropChance(state);

    if (fortune > 0) {
      chance -= 2 << fortune;
      if (chance < 10)
        chance = 10;
    }

    if (rand.nextInt(chance) == 0) {
      if (sapling != null) {
        ItemStack sap = sapling.get();
        if (!sap.isEmpty()) {
          drops.add(sap);
        }
      }
    }
  }

  @Override
  @Nonnull
  public AxisAlignedBB getBoundingBox(@Nonnull BlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return box;
  }

  @Nonnull
  public BlockLeavesBase setHarvestReqs(@Nonnull String tool, int level) {
    setHarvestLevel(tool, level);
    return this;
  }

  @SideOnly(Side.CLIENT)
  public BlockLeavesBase setLayer(@Nonnull BlockRenderLayer layer) {
    return this;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull BlockState state) {
    return Blocks.LEAVES.isOpaqueCube(state);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getRenderLayer() {
    return net.minecraft.block.Blocks.LEAVES.getRenderLayer();
  }

  @Override
  public boolean isFullCube(@Nonnull BlockState state) {
    return true;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "handlers"));
    ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(CHECK_DECAY).ignore(DECAYABLE).build());
  }

  @Override
  public void getSubBlocks(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> items) {
    if (hasItems) {
      super.getSubBlocks(tab, items);
    }
  }

  @Override
  public BlockPlanks.EnumType getWoodType(int meta) {
    return null;
  }

  @Override
  public Item getItemBlock() {
    return itemBlock;
  }

  @Override
  public BlockItem setItemBlock(BlockItem block) {
    this.itemBlock = block;
    return block;
  }

  @Override
  public Item getItemDropped(BlockState state, Random rand, int fortune) {
    if (drops != null && drops.size() > 0) {
      return drops.get(0).getItem();
    }
    return super.getItemDropped(state, rand, fortune);
  }

  @Override
  public boolean isFlammable(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull Direction face) {
    return isFlammable || super.isFlammable(world, pos, face);
  }

  @Override
  public int getFlammability(IBlockAccess world, BlockPos pos, Direction face) {
    return isFlammable ? 100 : super.getFlammability(world, pos, face);
  }

  @Nonnull
  @Override
  public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
    return NonNullList.withSize(1, new ItemStack(this, 1));
  }

  @Override
  public BlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(DECAYABLE, (meta & 4) == 0).withProperty(CHECK_DECAY, (meta & 8) > 0);
  }

  @Override
  public int getMetaFromState(BlockState state) {
    int i = 0;

    if (!state.getValue(DECAYABLE)) {
      i |= 4;
    }

    if (state.getValue(CHECK_DECAY)) {
      i |= 8;
    }

    return i;
  }

  @Override
  public boolean noCull() {
    return false;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side) {
    return net.minecraft.block.Blocks.LEAVES.shouldSideBeRendered(blockState, blockAccess, pos, side);
  }
}
