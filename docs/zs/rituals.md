
### Class

```zenscript
import mods.roots.Rituals;
```

#### Methods

```zenscript
void modifyRitual(
  string name,         // the name of the ritual whose ingredients you wish to modify
  IIngredient[] inputs // a list of five ingredients (no more, no less)
);
```


---


### Examples

```zenscript
import mods.roots.Rituals;

// Changes the ingredients required to perform the windwall ritual
Rituals.modifyRitual("ritual_windwall", [<minecraft:feather>, <minecraft:flint_and_steel>.anyDamage().transformDamage(1), <roots:cloud_berry>, <roots:cloud_berry>, <minecraft:web>]);
```
