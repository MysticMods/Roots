Intro
=====

Greetings! This has been a long time coming, starting around July or August last year. While this is not the final product and doesn't have all of the [planned additions, fixes and changes](https://github.com/MysticMods/Roots/issues?q=is%3Aissue+is%3Aopen+label%3A3.1.0), it's getting fairly close and I'm keen to begin testing.

There are a number of changes, including additional spells and rituals, modified spells and rituals, thaumcraft.items, etc. I've tried to summarise as simply as possible below. Then there are bug fixes and adjustments, not all of which are listed.

Obtaining
---------

You can obtain Roots 3.1-alpha, including all of the dependencies, from the `#testing-admin` channel on [our Discord](https://discord.gg/75aVV7C). These channels are generally locked behind the `tester` role, but the intent of this wide alpha is to get as much feedback as possible.

Depending on how it goes, I'll be posting a simple modpack to `#testing-chat` at some point over the next week or two.

Giving Feedback
---------------

You have two avenues to providing feedback. For simple issues and simple crashes, the `#testing-chat` channel is the **only** place on the Discord that these should be posted; please don't post in any other channel.

For everything else, please [file an issue on our GitHub](https://github.com/MysticMods/Roots/issues/new?assignees=&labels=&template=bug-report.md&title=).

Most importantly, please include:

- `latest.log`, where possible (if not using the Twitch client), `debug.lgo`
- the most recent crash report
- any steps that you can recall that led to this

Changelog
=========

alpha2
------

- Added utility function to MysticalLib to save reusing the same snippet of code over and over.
- Moved Component Pouch and Apothecary Pouch item handlers to server-side only and stored them in world save data.
- This has an impact on casting spells and the HUD updates, but these should've been completely resolved in this release.

Known issues
------------

- Soil exchange is currently broken, returning either the wrong soil or not consuming from your inventory

Bug Fixes
=========

This file will likely be updated, and is currently up to date for release: `alpha` (initial release).

- Component and apothecary pouches should no longer void thaumcraft.items.

Additional Spells
=================

- Autumn's Fall: causes nearby leaves to break
- Chrysopoeia: transmutes (according to a recipe list) thaumcraft.items in your off-hand
- Desaturate: expends saturation & filled hunger in exchange for health (at a lower ratio)
- Disarm: an expensive spell that disarms a nearby creature, with a chance to drop the item
- Nature's Blades: causes tall grass to grow on nearby surfaces, and causes grass to sprout on dirt
- Nature's Scythe: causes nearby grass, tall grass and flowers to break
- Reach: Temporarily grants you extended reach (except when fighting monsters)
- Saturate: instantly consumes edible thaumcraft.items in your inventory to refill your hunger and saturation, at a lower percentage than would be gained from eating them normally
- Scatter: plants seeds in nearby tilled ground
- Spring Storm: causes dark storm clouds to gather above you, granting Resistance & Fire Resistance to players standing within them, melting snow, and striking hostile creatures with lightning
- Summer's Thaw: causes ice, snow, etc, to melt

Removed Spells
==============

- Grove Supplication: is now a ritual, no longer requiring component pouches to activate Grove Stones.

Added Rituals
=============

- Grove Supplication: activates nearby Grove Stones.

Significantly Modified Rituals
==============================

- Fire Storm: The fire spawned by this ritual only harms hostile creatures, not thaumcraft.items, players or passive creatures. Monsters killed by it are considered to have been killed by players with Looting II.
- Summon Creatures: Significantly changed in the 3.0 series, this ritual now accepts multiple thaumcraft.items from multiple offering plates, allowing for more complex recipes.

New Items
=========

- Salmon of Knowledge: can be crafted to share advancements with another player.
- Life Essence: obtained using Runic Shears while sneaking on thaumcraft.entities listed in the Animal Harvest ritual (by default). Used in the Summon Creatures ritual in place of ingredients.
- Creative Component Pouch: provides an infinite amount of herbs whenever a spell is cast.

Modified Items
==============

- Component and Apothecary pouches: can now be dyed, default colour is brown. **All pouches are automatically searched for herbs instead of just the first pouch available**. Hotkey still only brings up the first available pouch.

Integration
===========

JEI
---

Complete support for new systems:

- Summon Creatures ritual
- Chrysopoeia recipes

CraftTweaker
------------

- [Summon Creatures ritual](https://github.com/MysticMods/Roots/blob/release/3.1.0/docs/zs/summoncreatures.md)
- [Chrysopoeia recipes](https://github.com/MysticMods/Roots/blob/release/3.1.0/docs/zs/chrysopoeia.md)

All other documentation can be found on Jared's official CraftTweaker documentation website, [starting here with Animal Harvest](https://docs.blamejared.com/1.12/en/#Mods/Roots_3/animalharvest/), which should also link to the rest of the sections.

***Rituals and Spells are not currently documented.***

Simply put,

```
Rituals.getRitual("animal_harvest").setInt("radius_x", 40);

Spells.getSpell("dandelion_winds").setFloat("distance", 0.95f);
```

To get all properties you can use:

`/roots rituals`

and

`/roots spells`

Both will output to `roots.log`.

Scratch Space
=============

Name changes
------------

- `roots:bonfire` is now `roots:pyre`
- `roots:offertory_plate` is now `roots:offering_plate`
- `roots:assorted_seeds` are now in Mystical World, `mysticalworld:assorted_seeds`
- Poisoned potato crop have been removed from Mystical World

CraftTweaker changes
--------------------

- `mods.roots.Ritual` is now `mods.roots.Rituals`