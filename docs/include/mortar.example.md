### Examples

```zenscript
import mods.roots.Mortar;

// Adds a recipe that makes gunpowder out of flint
// As the recipe only has one input, it will automatically
// generate 5 recipes, increasing the number of inputs and
// the quantity of the output respectively.
Mortar.addRecipe("gunpowder_from_flint", <minecraft:gunpowder>, [<minecraft:flint>]);

// This recipe uses five ingredients to create one bed.
Mortar.addRecipe("bed_from_wool_planks", <minecraft:bed>, [<minecraft:wool>, <minecraft:wool>, <minecraft:planks>, <minecraft:planks>, <minecraft:planks>]);

Mortar.addRecipe("charred_planks_from_transformed_flint_and_steel", <mysticalworld:charred_planks>*4, [<minecraft:flint_and_steel>.anyDamage().transformDamage(1), <ore:plankWood>, <ore:plankWood>, <ore:plankWood>, <ore:plankWood>]);

// This will remove all recipes that have Root's flour as an output
// including any multi-ingredient recipes
Mortar.removeRecipe(<roots:flour>);

// This will change the recipe for the Harvest to
// simply require five pieces of sugar.
Mortar.changeSpell("spell_harvest", [<minecraft:flint_and_steel>.anyDamage().transformDamage(1), <minecraft:sugar>, <minecraft:sugar>, <minecraft:sugar>, <minecraft:sugar>]);
```