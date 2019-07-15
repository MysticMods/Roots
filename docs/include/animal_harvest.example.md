### Examples

```java
import mods.roots.AnimalHarvest;

// Adds an enderman as a target for the Animal Harvest ritual.
// Generally it would be better to use animals.
AnimalHarvest.addEntity(<entity:minecraft:enderman>);

// Prevents the entity from giving drops during the ritual
AnimalHarvest.removeEntity(<entity:minecraft:cow>);
```