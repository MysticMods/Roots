### Examples

```zenscript
import mods.roots.Rituals;
import mods.roots.Ritual;

var harvest = Rituals.getRitual("animal_harvest") as Ritual; // If not placed at the beginning, "ritual_" will be automatically added.
harvest.setInteger("count", 10); // Sets the "count" property to 10 from the default of 5, meaning that 10 harvesting operations will be completed every interval
harvest.setInteger("interval", 220); // Sets the "interval" in ticks to double the default of 110, meaning the ritual now fires less often but does double the number of harvests
harvest.setFloat("looting_chance", 1); // Sets the chance of looting being applied to 100% from the default of 0.16 (16%)
harvest.setInteger("radius_x", 30);
harvest.setInteger("radius_z", 30);
harvest.setInteger("radius_y", 30); // Set each radius to 30, doubling the default horizontal radius of 15, and increasing the y radius to 30 from 10.
```

### Notes

You will want to use the `/roots rituals` command in order to get a list of the relevant properties and their default values output to `roots.log` in order to determine which property names you wish to adjust.