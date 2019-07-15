### Examples

```java
import mods.roots.FlowerGrowth;

// Removes the default recipe for dandelion
FlowerGrowth.removeRecipe("dandelion");

// Adds a Botania white flower using block state
FlowerGrowth.addRecipeBlockState("mystical_white_flower", <blockstate:botania:flower:color=white>);

// Adds a Botania magenta flower using block + meta
FlowerGrowth.addRecipeBlock("mystical_green_flower", <botania:flower>.asBlock(), 2);
```

### Notes

Currently untested with double-tall flowers.

