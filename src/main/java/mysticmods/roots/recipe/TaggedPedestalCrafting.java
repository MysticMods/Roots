package mysticmods.roots.recipe;

import com.mojang.datafixers.util.Pair;
import mysticmods.roots.api.recipe.crafting.RootsTileCrafting;
import mysticmods.roots.blockentity.PedestalBlockEntity;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class TaggedPedestalCrafting<T extends BaseBlockEntity> extends RootsTileCrafting<PedestalInventoryWrapper, T> {
  private final TagKey<Block> includeTag;
  private final TagKey<Block> excludeTag;

  public TaggedPedestalCrafting(TagKey<Block> includeTag, TagKey<Block> excludeTag, T blockEntity, @Nullable Player player) {
    super(new PedestalInventoryWrapper(blockEntity.pedestals(includeTag, excludeTag)), blockEntity, player);
    this.includeTag = includeTag;
    this.excludeTag = excludeTag;
  }

  public TagKey<Block> getIncludeTag() {
    return includeTag;
  }
  public TagKey<Block> getExcludeTag() {
    return excludeTag;
  }

  public List<ItemStack> popItems() {
    List<ItemStack> result = new ArrayList<>();
    if (getBlockEntity() == null) {
      return result;
    }
    for (Pair<BlockPos, PedestalBlockEntity> entry : getBlockEntity().pedestals(getIncludeTag(), getExcludeTag())) {
      result.add(entry.getSecond().popOne());
    }
    return result;
  }
}
