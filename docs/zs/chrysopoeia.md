
### Class

```zenscript
import mods.roots.Chrysopoeia;
```

#### Methods

```zenscript
void addRecipe(
  string name,            // the name of the recipe being added
  IIngredient ingredient, // a single ingredient (may have variable stack size)
  IItemStack output       // the output produce by Transubstantiation
);
```


---


```zenscript
void removeRecipe(
  IItemStack output // the output of the recipe you wish to remove
);
```


---


### Examples

```zenscript
import mods.roots.Chrysopoeia;

// Add a recipe to transmute 5 gunpowder to 1 ghast tear
Chrysopoeia.addRecipe("ghast_tear", <ore:gunpowder>*5, <minecraft:ghast_tear>);

// Remove the default copper -> iron ingot recipe
Chrysopoeia.removeRecipe(<minecraft:iron_ingot>);
```
