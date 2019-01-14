package epicsquid.roots.block;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.block.BlockStairsBase;
import epicsquid.mysticallib.block.CustomStateMapper;
import epicsquid.mysticallib.block.IBlock;
import epicsquid.mysticallib.model.CustomModelBlock;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.model.block.BakedModelBlock;
import epicsquid.mysticallib.model.block.BakedModelStairs;
import epicsquid.mysticallib.tile.ITile;
import epicsquid.mysticalworld.item.ItemKnife;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockWildrootLog extends BlockLog implements IBlock, IModeledObject, ICustomModeledObject {

    private final @Nonnull
    Item itemBlock;
    private boolean isOpaque = false;
    private boolean hasCustomModel = false;
    private BlockRenderLayer layer = BlockRenderLayer.SOLID;
    public String name;

    public BlockWildrootLog(@Nonnull String name) {
        super();
        this.setCreativeTab(null);
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        itemBlock = new ItemBlock(this).setRegistryName(LibRegistry.getActiveModid(), name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing face, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if(stack.isEmpty()){
            return false;
        }

        if(stack.getItem() instanceof ItemKnife){
            stack.damageItem(1, player);
            world.setBlockState(pos, ModBlocks.wildroot_rune.getDefaultState());
        }
        return false;
    }

    @Nonnull
    public BlockWildrootLog setModelCustom(boolean custom) {
        this.hasCustomModel = custom;
        return this;
    }

    @Nonnull
    public BlockWildrootLog setHarvestReqs(@Nonnull String tool, int level) {
        setHarvestLevel(tool, level);
        return this;
    }

    @Nonnull
    public BlockWildrootLog setOpacity(boolean isOpaque) {
        this.isOpaque = isOpaque;
        return this;
    }

    @Nonnull
    public BlockWildrootLog setLayer(@Nonnull BlockRenderLayer layer) {
        this.layer = layer;
        return this;
    }

    @Override
    public boolean isOpaqueCube(@Nonnull IBlockState state) {
        return isOpaque;
    }

    public boolean hasCustomModel() {
        return this.hasCustomModel;
    }

    @Override
    public boolean isFullCube(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel() {
        if (hasCustomModel) {
            ModelLoader.setCustomStateMapper(this, new CustomStateMapper());
        }
        if (!hasCustomModel) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "handlers"));
        } else {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "handlers"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initCustomModel() {
        if (hasCustomModel) {
            ResourceLocation defaultTex = new ResourceLocation(getRegistryName().getResourceDomain() + ":blocks/" + getRegistryName().getResourcePath());
            if (getParentState() != null) {
                defaultTex = new ResourceLocation(
                        getParentState().getBlock().getRegistryName().getResourceDomain() + ":blocks/" + getParentState().getBlock().getRegistryName().getResourcePath());
            }
            CustomModelLoader.blockmodels.put(new ResourceLocation(getRegistryName().getResourceDomain() + ":models/block/" + name),
                    new CustomModelBlock(getModelClass(), defaultTex, defaultTex));
            CustomModelLoader.itemmodels.put(new ResourceLocation(getRegistryName().getResourceDomain() + ":" + name + "#handlers"),
                    new CustomModelBlock(getModelClass(), defaultTex, defaultTex));
        }
    }

    @Nonnull
    protected Class<? extends BakedModelBlock> getModelClass() {
        return BakedModelStairs.class;
    }


    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return this.layer;
    }

    @Override
    @Nonnull
    public Item getItemBlock() {
        return itemBlock;
    }

    @Nullable
    protected IBlockState getParentState() {
        return null;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState state = this.getDefaultState();

        switch (meta & 0b1100)
        {
            case 0b0000:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;

            case 0b0100:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;

            case 0b1000:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;

            case 0b1100:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
                break;
        }

        return state;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        switch (state.getValue(LOG_AXIS))
        {
            case X: return 0b0100;
            case Y: return 0b0000;
            case Z: return 0b1000;
            case NONE: return 0b1100;
        }
        return 0b1100;
    }
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {LOG_AXIS});
    }
}
