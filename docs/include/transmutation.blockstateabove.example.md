### Examples

```zenscript
import mods.roots.predicates.BlockStateAbove;
import mods.roots.predicates.PropertyPredicate;

BlockStateAbove.create(PropertyPredicate.create(<blockstate:minecraft:log:variant=spruce>, "variant")); // Creates a world predicate that will only match thaumcraft.blocks which have a block above them that is both a log and with the variant of spruce.
```

### Notes

A positional IWorldPredicate that allows for the specification of an IPredicate to check above thaumcraft.blocks for. 
