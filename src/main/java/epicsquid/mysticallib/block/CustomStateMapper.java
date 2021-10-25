package epicsquid.mysticallib.block;

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;

public class CustomStateMapper implements IStateMapper {
  protected Map<BlockState, net.minecraft.client.renderer.model.ModelResourceLocation> mapStateModelLocations = Maps.<BlockState, ModelResourceLocation>newLinkedHashMap();

  @Override
  @Nonnull
  public Map<BlockState, net.minecraft.client.renderer.model.ModelResourceLocation> putStateModelLocations(@Nonnull Block block) {
    UnmodifiableIterator unmodifiableiterator = block.getBlockState().getValidStates().iterator();

    while (unmodifiableiterator.hasNext()) {
      BlockState iblockstate = (BlockState) unmodifiableiterator.next();
      this.mapStateModelLocations.put(iblockstate, new ModelResourceLocation(iblockstate.getBlock().getRegistryName(), "custom"));
    }

    return this.mapStateModelLocations;
  }

}
