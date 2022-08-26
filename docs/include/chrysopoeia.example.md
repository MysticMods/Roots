### Examples

```zenscript
import mods.roots.Chrysopoeia;

// Add a recipe to transmute 5 gunpowder to 1 ghast tear
Chrysopoeia.addRecipe("ghast_tear", <ore:gunpowder>*5, <minecraft:ghast_tear>);

Chrysopoeia.addRecipe("magma_cream", <minecraft:flint_and_steel>.transformDamage(1), <minecraft:magma_cream>);

// Remove the default copper -> iron ingot recipe
Chrysopoeia.removeRecipe(<minecraft:iron_ingot>);
```
