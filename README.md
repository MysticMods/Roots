# Roots

[![Discord](https://img.shields.io/discord/455383608773836801.svg?style=for-the-badge&logo=discord)](https://discord.gg/75aVV7C)
[![](https://img.shields.io/github/contributors/MysticMods/Roots.svg?style=for-the-badge&logo=github)](https://github.com/MysticMods/Roots/graphs/contributors)
[![](https://img.shields.io/github/issues/MysticMods/Roots.svg?style=for-the-badge&logo=github)](https://github.com/MysticMods/Roots/issues)
[![](https://img.shields.io/github/issues-pr/MysticMods/MysticalWorld.svg?style=for-the-badge&logo=github)](https://github.com/MysticMods/Roots/pulls)
[![](https://img.shields.io/github/forks/MysticMods/Roots.svg?style=for-the-badge&logo=github)](https://github.com/MysticMods/Roots/network/members)
[![](https://img.shields.io/github/stars/MysticMods/Roots.svg?style=for-the-badge&logo=github)](https://github.com/MysticMods/Roots/stargazers)
[![](https://img.shields.io/github/license/MysticMods/Roots.svg?logo=github&style=for-the-badge)](https://github.com/MysticMods/Roots/blob/master/LICENSE)
[![](https://img.shields.io/endpoint.svg?style=for-the-badge&url=https%3A%2F%2Fshieldsio-patreon.herokuapp.com%2Fepicsquid315)](https://patreon.com/epicsquid315)

Roots is a magic mod about exploring the world, finding the sources of natural magic that exist within it, and using it to do cool things. 

You can download the latest files here on CurseForge. https://minecraft.curseforge.com/projects/roots

You can also contact us on the [Mystic Modding Discord]( https://discord.gg/75aVV7C).

### CraftTweaker, Configuration and Pack Development

Roots was designed with pack developers in mind, in order to be as configurable as possible. The following is possible through configuration:

- Enabling or disabling individual spells
- Enabling or disabling individual rituals
- Specifying mod IDs to exclude from Growth spells & rituals
- Specifying individual blocks to exclude from Growth spells & rituals 
- Access to a list of "growables" through the `/roots growables` command.
- Specifying mod IDs to exclude from Harvest spells & rituals.
- Specifying individual blocks to exclude from Harvest spells & rituals.
- Configure the minimum Y level required for Elemental Soil to convert into Caelic Soil.
- Configure the minimum Y level required for Elemental Soil to convert into Terran Soil.
- Configure the delay (in ticks) required for Elemental Soils (excluding Magmatic) to transform.
- Enabling or disabling loot injection 
- Configure the minimum and maximum number of pulls to make from loot pools, if loot injection is enabled.
- Configure the radius of the Runic Shears' sheep-shearing AoE.
- Specify saplings to be deliberately excluded from the Spreading Forest ritual.
- Specify saplings that must be planted in a 2x2 for the Spreading Forest ritual.
- Specify (and potentially remove or disable) via item (mod:item:meta (meta optional)) which mossy blocks can be scraped and what block they turn into. (This works primarily with blocks that have no directional or other information in their state.)

Some of the configuration options are too complex or ill-suited for a configuration file. These are instead performed via CraftTweaker integration, which is fully documented under the "Mods -> Roots 3" section of [the Official CraftTweaker Documentation](https://docs.blamejared.com/en/). Covered by those include:

- Creatures can be added and removed to those eligible for the [Animal Harvest ritual](https://docs.blamejared.com/en/#Mods/Roots_3/animalharvest/).
- Logs (or other breakable objects) can be specified for the creation of [Bark](https://docs.blamejared.com/en/#Mods/Roots_3/bark/).
- Recipes can be added or removed to [the Fey Crafter](https://docs.blamejared.com/en/#Mods/Roots_3/fey/).
- Additional flower types can be specified for the [Ritual of Flower Growth](https://docs.blamejared.com/en/#Mods/Roots_3/flowergrowth/).
- Recipes can be added or removed from the [Mortar and Pestle](https://docs.blamejared.com/en/#Mods/Roots_3/mortar/).
- Spell recipes made in a [Mortar and Pestle](https://docs.blamejared.com/en/#Mods/Roots_3/mortar/) can also be modified.
- Creatures considered animals that grant the ["Untrue Pacifist" advancement can be specified](https://docs.blamejared.com/en/#Mods/Roots_3/pacifist/).
- Crafting recipes performed on a [Pyre](https://docs.blamejared.com/en/#Mods/Roots_3/pyre/) can be modified.
- The ingredients for [Pyre Rituals](https://docs.blamejared.com/en/#Mods/Roots_3/ritual/) can be modified.
- The results of using [Runic Shears on thaumcraft.entities or blocks](https://docs.blamejared.com/en/#Mods/Roots_3/runicshears/) can be specified.
- Recipes for the [Ritual of Transmutation](https://docs.blamejared.com/en/#Mods/Roots_3/transmutation/) can be removed or added.

If there's anything that you'd like to modify that isn't currently available, you can open an issue or ask on Discord.