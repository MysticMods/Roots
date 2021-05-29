### Examples

```zenscript
import mods.roots.Pyre;

// Removes the recipe for stalicripe; note that the quantity is not considered
// when checking if the recipe matches.
Pyre.removeRecipe(<roots:stalicripe>);

// Re-adds the stalicripe using an addition recipe that grants no XP,
// but with considerably greater output
Pyre.addRecipe("stalicripe", <roots:stalicripe>*64, [<minecraft:diamond_block>, <minecraft:flint_and_steel>.anyDamage().transformDamage(1), <minecraft:iron_block>, <minecraft:emerald_block>, <minecraft:deadbush>]);

// As above, but rewarding 30 levels of experience (calculated from level 0)
Pyre.addRecipe("stalicripe2", <roots:stalicripe>*64, [<minecraft:diamond_block>, <minecraft:gold_block>, <minecraft:iron_block>, <minecraft:emerald_block>, <minecraft:deadbush>], 30);
```

### Notes

It's extremely important when replacing recipes to ensure that the recipe name is the same to make certain that Patchouli correctly reports the correct recipe for thaumcraft.crafting base thaumcraft.items.

For all other thaumcraft.items, please use a name descriptive of what your recipe does.