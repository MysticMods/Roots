### Examples

```zenscript
import mods.roots.SummonCreatures;

// Clear all automatically generated Life Essences added by
// the Animal Harvest map
SummonCreatures.clearLifeEssence();

// Remove the default recipe for chicken
SummonCreatures.removeEntity(<entity:minecraft:chicken>);

// Add a recipe to summon a Chicken using 3 items
SummonCreatures.addEntity(<entity:minecraft:chicken>, [<minecraft:wheat_seeds>, <minecraft:wheat>, <ore:ingotIron>]);

// Add a recipe to summon a blaze using flint and steel
SummonCreatures.addEntity(<entity:minecraft:blaze>, [<minecraft:flint_and_steel>.anyDamage().transformDamage(1)]);

// Remove the enderman added by the Animal Harvest example
// from the Life Essence list (presuming it hasn't been cleared)
SummonCreatures.removeLifeEssence(<entity:minecraft:enderman>);

// Manually add a life-essence drop capability for an ender dragon
SummonCreatures.addLifeEssence(<entity:minecraft:ender_dragon>);
```