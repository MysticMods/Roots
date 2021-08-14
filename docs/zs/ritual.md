
### Class

```zenscript
import mods.roots.Ritual;
```

#### Methods

```zenscript
Ritual setDouble(
  string propertyName, // sets propertyName to the specified double value
  double value         // the value to set propertyName to; if this property is *not* a double, an error will be raised
);
```

Sets a propertyName to a specified value (throwing an exception if this is an invalid type for that property), then returns the ritual, allowing for chained functions.

---


```zenscript
Ritual setFloat(
  string propertyName, // sets propertyName to the specified float value
  float value          // the value to set propertyName to; if this property is *not* a float, an error will be raised
);
```

Sets a propertyName to a specified value (throwing an exception if this is an invalid type for that property), then returns the ritual, allowing for chained functions.

---


```zenscript
Ritual setInteger(
  string propertyName, // sets propertyName to the specified integer value
  int value            // the value to set propertyName to; if this property is *not* a integer, an error will be raised
);
```

Sets a propertyName to a specified value (throwing an exception if this is an invalid type for that property), then returns the ritual, allowing for chained functions.

---


```zenscript
Ritual setDuration(
  int value // the new duration for the ritual
);
```

Changes the duration of the ritual and returns the Ritual object for further modification. Is shorthand for `setInteger("duration", value)`.

---


```zenscript
Ritual setString(
  string propertyName, // sets propertyName to the specified string value
  string value         // the value to set propertyName to; if this property is *not* a string, an error will be raised
);
```

Sets a propertyName to a specified value (throwing an exception if this is an invalid type for that property), then returns the ritual, allowing for chained functions.

---


### Examples

```zenscript
import mods.roots.Rituals;
import mods.roots.Ritual;

var  = Spells.getSpell("harvest") as Spell; // If not placed at the beginning, "spell_" will be automatically added.
harvest.setCooldown(800); // Sets the cooldown of the Harvest spell to 40 seconds.
harvest.setCost(Herbs.wildewheet, 1.25); // Increases the wildewheet cost of Harvest from the default of 0.55 to 1.25
harvest.setModifierCost(Costs.additional_cost, Herbs.wildroot, 0.9); // Increases the cost of the wildroot-related modifier's additional cost from the default of 0.125 to 0.9
harvest.setInteger("radius_x", 20);
harvest.setInteger("radius_z", 20);
harvest.setInteger("radius_y", 20); // Increases the size of the base Harvest radius to 20 blocks in all directions.
```

### Notes

You will want to use the `/roots rituals` command in order to get a list of the relevant properties and their default values output to `roots.log` in order to determine which property names you wish to adjust.
