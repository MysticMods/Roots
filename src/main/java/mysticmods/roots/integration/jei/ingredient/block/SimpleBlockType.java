package mysticmods.roots.integration.jei.ingredient.block;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public record SimpleBlockType(BlockState state, ItemStack stack) implements IBlockType {
  public SimpleBlockType(Block block) {
    this(block.defaultBlockState(), new ItemStack(block));
  }

  public SimpleBlockType(Holder<Block> block) {
    this(block.value());
  }

  public static List<SimpleBlockType> fromTag(TagKey<Block> tag) {
    List<SimpleBlockType> result = new ArrayList<>();
    BuiltInRegistries.BLOCK.getTagOrEmpty(tag).forEach(o -> result.add(new SimpleBlockType(o)));
    return result;
  }
}
