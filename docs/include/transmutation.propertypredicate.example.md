### Examples

```zenscript
import mods.roots.predicates.PropertyPredicate;

PropertyPredicate.create(<blockstate:minecraft:log:variant=spruce>, "variant"); // Creates a predicate that matches only other logs where the variant value is also spruce.

PropertyPredicate.create(<blockstate:minecraft:log:variant=spruce>, ["variant"]); // As above, but allowing for multiple property names to be matched. 
```

### Notes

A more complex matching system where a property (or number of properties) is checked in both states to ensure equality, in addition to the block matching.
