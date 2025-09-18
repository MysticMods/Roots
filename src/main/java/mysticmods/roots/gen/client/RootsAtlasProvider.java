package mysticmods.roots.gen.client;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RootsAtlasProvider extends SpriteSourceProvider {
  public RootsAtlasProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
    super(output, lookupProvider, RootsAPI.MODID, fileHelper);
  }

  @Override
  protected void gather() {
    this.atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(RootsAPI.rl("gui/grove_action_symbol"), Optional.empty()));
  }
}
