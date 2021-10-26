package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockTEBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockTrample extends BlockTEBase {
  public static int SAFE_RANGE_X = 30;
  public static int SAFE_RANGE_Y = 5;
  public static int SAFE_RANGE_Z = 30;

  public BlockTrample(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "handlers"));
  }
}
