package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;




import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RootsAtlasGenerator extends SpriteSourceProvider {
  public RootsAtlasGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
    super(output, lookupProvider, RootsAPI.MODID, fileHelper);
  }

  @Override
  protected void gather() {
  }
}
