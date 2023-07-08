package mysticmods.roots.api;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class Materials {
  public static Tier LIVING = new ForgeTier(2, 250, 6.0f, 2.0f, 19, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(RootsTags.Items.BARKS));
}
