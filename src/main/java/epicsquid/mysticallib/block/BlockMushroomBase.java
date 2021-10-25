package epicsquid.mysticallib.block;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.world.StructureData;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMushroomBase extends BlockMushroom implements IBlock, IModeledObject {

  private Item itemBlock;
  private StructureData hugeMushroom;

  public BlockMushroomBase(@Nonnull String name, @Nonnull StructureData hugeMushroom) {
    super();
    this.hugeMushroom = hugeMushroom;
    setSoundType(SoundType.PLANT);
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
  }

  public BlockMushroomBase setItemBlock(@Nonnull Item itemBlock) {
    this.itemBlock = itemBlock;
    return this;
  }

  @Override
  public boolean generateBigMushroom(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
    world.setBlockToAir(pos);
    hugeMushroom.generateIn(world, pos.getX(), pos.getY(), pos.getZ(), Rotation.NONE, Mirror.NONE, false, false);
    return true;
  }

  @Nullable
  @Override
  public Item getItemBlock() {
    return itemBlock;
  }

  @Override
  public ItemBlock setItemBlock(ItemBlock block) {
    this.itemBlock = block;
    return block;
  }

  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName().toString(), "inventory"));
  }
}
