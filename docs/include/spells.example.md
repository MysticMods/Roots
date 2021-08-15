### Examples

```zenscript
import mods.roots.Spells;
import mods.roots.Spell;

var harvest = Spells.getSpell("harvest") as Spell; // If not placed at the beginning, "spell_" will be automatically added.
```

### Notes

You will want to use the `/roots spells` command in order to get a list of the relevant properties and their default values output to `roots.log` in order to determine which property names you wish to adjust.