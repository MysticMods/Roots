
### Class

```zenscript
import mods.roots.FlowerGrowth;
```

#### Methods

```zenscript
void removeRecipe(
  string name // The name of the recipe you wish to remove
);
```

Removes a flower growth recipe by name.

---


```zenscript
void addRecipeBlockState(
  string name,      // The name of the recipe that you're adding
  IBlockState state // The state of the block of the flower
);
```

Adds a recipe to create the specific block state during the flower growth ritual.

---


```zenscript
void addRecipeBlock(
  string name,  // The name of the recipe that you're adding
  IBlock block, // The block of the flower to be placed
  int meta      // The meta of the state of the flower block
);
```

Adds a recipe by creating a blockstate from a block along with the meta value from an itemblock to be grown during the flower growth ritual.

---


```zenscript
void addRecipeItem(
  string name,     // The name of the recipe that you're adding
  IItemStack stack // The itemstack describing an itemblock to be placed
);
```

Adds a recipe by creating a blockstate from an itemstack containing an itemblock and metadata to be grown during the Flower Growth ritual.

---


```zenscript
void addRecipeItemOnSoils(
  string name,            // The name of the recipe that you're adding
  IItemStack stack,       // The itemstack describing an itemblock to be placed
  List<IIngredient> soils // The types of "soils" (thaumcraft.blocks) the itemblock can be placed on
);
```

Adds a recipe by creating a blockstate from an itemstack containing an itemblock and metadata to be grown during the Flower Growth ritual, with a whitelist of "soils" (thaumcraft.blocks) that this recipe can grow things on.

The soils can be oredict entries.

---


### Examples

```zenscript
import mods.roots.FlowerGrowth;

// Removes the default recipe for dandelion
FlowerGrowth.removeRecipe("dandelion");

// Adds a Botania white flower using block state
FlowerGrowth.addRecipeBlockState("mystical_white_flower", <blockstate:botania:flower:color=white>);

// Adds a Botania magenta flower using block + meta
FlowerGrowth.addRecipeBlock("mystical_green_flower", <botania:flower>.asBlock(), 2);

// Adds a torch as an item, but only on dirt and brown mulch.
FlowerGrowth.addRecipeItemOnSoils(
  "torch_on_dirt",
  <minecraft:torch>,
  [
    <ore:dirt>,
    <inspirations:mulch:1>
  ]);
```

### Notes

Currently untested with double-tall flowers.

