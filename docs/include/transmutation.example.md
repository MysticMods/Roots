### Examples

```zenscript
import crafttweaker.block.IBlockState;
import mods.roots.predicates.Predicates;
import mods.roots.predicates.BlockStateBelow;
import mods.roots.predicates.PropertyPredicate;
import mods.roots.Transmutation;

// Creates a transmutation where spruce logs (only spruce logs), will be converted to glowstone, if (and only if) the block below the logs is leaves.
Transmutation.addStateToStateRecipe("spruce_to_glowstone", PropertyPredicate.create(<blockstate:minecraft:log:variant=spruce> as IBlockState, "variant"), <blockstate:minecraft:glowstone>, BlockStateBelow.create(Predicates.Leaves));

// Creates a transmutation where acacia logs (only acacia logs), will be converted into a cobblestone item, if and only if the block below is water.
Transmutation.addStateToItemRecipe("acacia_to_cobblestone", PropertyPredicate.create(<blockstate:minecraft:log2:variant=acacia> as IBlockState, "variant"), <minecraft:cobblestone>, BlockStateBelow.create(Predicates.Water));

// Removes the default pumpkin-over-water-to-melon recipe
Transmutation.removeRecipe("pumpkin_melon");
```

### Notes

See the Predicates examples for more information on building predicates and the different types of predicates and conditions available.

It is possible to find the information about the block and its variants and states by using the F3 debug functionality and targeting it. On the right side of the screen it will display the block's registry name, and then any states below that.

For example, `bone_block` has the following:

```axis: y```

This can be converted into a blockstate by replacing the `:` with `=` like so: `axis=y`, meaning that the final blockstate (for an upwards facing bone block) would be `<blockstate:minecraft:bone_block:axis=y>`.
