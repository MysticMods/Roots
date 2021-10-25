package epicsquid.mysticallib.block;

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;

public class CustomStateMapper implements IStateMapper {
  protected Map<IBlockState, ModelResourceLocation> mapStateModelLocations = Maps.<IBlockState, ModelResourceLocation>newLinkedHashMap();

  @Override
  @Nonnull
  public Map<IBlockState, ModelResourceLocation> putStateModelLocations(@Nonnull Block block) {
    UnmodifiableIterator unmodifiableiterator = block.getBlockState().getValidStates().iterator();

    while (unmodifiableiterator.hasNext()) {
      IBlockState iblockstate = (IBlockState) unmodifiableiterator.next();
      this.mapStateModelLocations.put(iblockstate, new ModelResourceLocation(iblockstate.getBlock().getRegistryName(), "custom"));
    }

    return this.mapStateModelLocations;
  }

}
