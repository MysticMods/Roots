package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class RootsBlockModelProvider extends BlockModelProvider {
  public RootsBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
  }
}
