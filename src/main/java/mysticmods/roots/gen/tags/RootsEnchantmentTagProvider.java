package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModEnchantment;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantments;
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
    tag(RootsTags.Enchantments.INCREASES_FORTUNE).add(Enchantments.FORTUNE);
    tag(RootsTags.Enchantments.INCREASES_LOOTING).add(Enchantments.LOOTING);
    tag(RootsTags.Enchantments.SILK_TOUCH).add(Enchantments.SILK_TOUCH);
  }
}
