package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModEnchantment;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RootsEnchantmentTagProvider extends EnchantmentTagsProvider {
  public RootsEnchantmentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(EnchantmentTags.NON_TREASURE).add(ModEnchantment.COLLECTING);
  }
}
