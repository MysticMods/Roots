
### Class

```zenscript
import mods.roots.RunicShears;
```

#### Methods

```zenscript
void addRecipe(
  string name,                  // the name of the recipe being created
  IItemStack outputDrop,        // the item output obtained by performing the shearing
  IBlockState replacementState, // the replacement blockstate described as a block state
  IPredicate inputState,        // a predicate describing the input state (see Predicates)
  IItemStack displayItem        // the item that should be displayed in integration for this recipe
);
```

Creates a recipe with the defined name that creats the specified itemstack whenever runic shears are used on the specified input state, as well as the state that will replace the input state. Additionally, an optional item that can be displayed in integration.

---


```zenscript
void addRecipeViaItem(
  string name,                 // the name of the recipe being created
  IItemStack outputDrop,       // the item output obtained by performing the shearing
  IItemStack replacementBlock, // the block (as an itemstack) that replaces the block being interacted with upon shearing
  IItemStack inputBlock,       // the block that is to be sheared
  IItemStack displayItem       // the item that should be displayed in integration for this recipe
);
```

Creates a recipe with the defined name that creats the specified itemstack whenever runic shears are used on the specified input state (derived from the itemstack), as well as the state that will replace the input state (derived from an itemstack). Additionally, an optional item that can be displayed in integration. ItemStacks for blockstates must be itemblocks.

---


```zenscript
void addEntityRecipe(
  string name,              // the name of the recipe for the shearing
  IItemStack outputDrop,    // the item that is dropped upon shearing the specified entity
  IEntityDefinition entity, // the entity that is to be sheared to obtain the drop
  int cooldown              // the number of ticks (seconds multiplied by 20) it takes until the entity can be sheared again
);
```

Create a Runic Shears recipe that provides the outputDrop whenever the specified entity is interacted with using runic shears. The drop will only be created once every specified cooldown period. The entity specified must derive from EntityLivingBase.

---


```zenscript
void removeRecipe(
  IItemStack output // the itemstack output that you wish to remove
);
```

Removes any/all recipes that have the output item specified.

---


### Examples

```zenscript
import mods.roots.RunicShears;

// Creates a recipe that obtains nether wart from red nether bricks
// and then converts the bricks into normal nether bricks
RunicShears.addRecipe("nether_wart_block", <minecraft:nether_wart>*2, <minecraft:nether_brick>, <minecraft:red_nether_brick>, <minecraft:red_nether_brick>);

// Creates a recipe that obtains eggs from chickens with a 2 minute cooldown
RunicShears.addEntityRecipe("egg_from_chicken", <minecraft:egg>*2, <entity:minecraft:chicken>, 120*20);

// Removes all recipes (both entity & block) that give fey leather
RunicShears.removeRecipe(<roots:fey_leather>);
```

### Notes

Note that the `removeRecipe` function will attempt to remove any recipe (both runic shearing of thaumcraft.blocks and of thaumcraft.entities) that matches the desired output.
