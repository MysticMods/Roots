## 4.0.0.24-alpha

- Wild Grove Stone (previously Primal) now automatically activates when placed, no longer requiring the Grove Supplication ritual.
- Spells can be modified through the library/staff screen by mousing over the staff slot with spell and pressing the Modify Spells keybind. The default keybind for this is 'Insert'.
- Spell modifier screen is currently a work in progress.
- Spells which currently have functional modifiers: Sky Soarer
- Sky Soarer modifiers include: Friendly Earth (prevents fall damage with a Stalicripe cost), Amplified (increases speed of boost), Speedy (increases duration of boost).
- Most spell modifiers are available by default. Some modifiers will require specific unlock methods, which will be shown in JEI.
- Spell tooltips now separately display the cost contributed by all modifiers in addition to the total cost.
- All spells now have short descriptions which are displayed in their tooltips.
- All modifiers now have short descriptions which are displayed in their tooltips.
- Staffs now use a '*' to denote spells which have had modifiers applied to them.
- The Sprouting Grove has been renamed to the Cultivation Grove.
- While the staff or spell modifier screens are open and the casting item they are adjusting is removed from the player's inventory, the screens will now properly close.
- Beam render types have now been properly memoized, fixing #1289 and preventing lag with Iris, etc.
- Elemental Soil now requires Rank 1 with the Elemental Grove to craft.
- Particles created during the Grove Crafter animation will now be properly tinted.

## 4.0.0.25-alpha

- Reputation for growing crops has been reduced slightly.
- Spells and modifiers (only visible through modifier screen) now have tooltip descriptions. 
- Holding Alt/Ctrl/Shift will display an "Extended" tooltip for spells and modifiers that includes exact values (as adjusted by configuration)
- Spells now render with the correct lighting in the spell library of the staff
- The Wildwood Chest now correctly has a tool set for it
- Crafting herbs for the first time by using runic shears on crops now grants reputation
- Added French translation (thanks to programgames)
- Modifiers exist for most spells now
- Removed a number of spells that will be reimplemented (Rampant Growth, Wildfire, Temporal Morass, Storm Cloud)
- The Decay spell has now been implemented but lacks visuals
- Growth Infusion and Rampant Growth have been merged into one spell; the latter is now available as a modifier
- Vastly improved the construction of spells and allow for styling, etc, based on the instances including modifiers
- Spell data adjusted via \ and -, = and \[ and \] have now been removed. The \ key is now used for toggling between modes.
- Acid Cloud's fire modifier now works and correctly changes the visuals
- There is now a configuration option that allows you to use bone meal underwater to rarely grow wild roots that are mossy (which means they will also drop grove spores)
- The Wild Grove stone (the base grove stone)'s inventory visual now includes the coloured texture
- Improved the icons for grove power and grove reputation overlays
- When a spell no longer has an effect, it will no longer swing the arm or drop the staff down
- Modifiers can now negate or cancel out the base cost of a spell
- Pyres should no longer extinguish itself when being right-clicked
- Damage types now have the correct translation keys
- Most instances where potion effects are applied to creates/players include the source
- Runed obsidian still allows you to use iron tools to break it, but it now takes much longer to break
- The message height for insert/delete on the mortar has been adjusted
- Rituals which only have a singular tick (Wildroot Growth, Grove Supplication, etc) have been simplified greatly
- Modifiers are now grouped in tooltips. Instead of displaying "Amplified 1, Amplified 2" when two are enabled, it will instead say "Amplified (2)"
- Grouped modifier tooltips will now show the "lowest" i.e., of 'Amplified 1', 'Amplified 2', the tooltip will only include 'Amplified 2'"
- Costs of combined modifiers are now collated
- The recipe for elemental soil has been complicated and now requires elemental grove rank 1
- All grove crafting recipes now require a grove stone of some description to begin
- Actions with no associated grove reputation entries will no longer be triggered nor will they construct excess contexts
- Using shatter in creative mode no longer destroys the blocks
- Particles spawned by Magnetism move towards the caster much faster
- The bounding box of Bafflecap (mushrooms) have been increased

## 4.0.0.26-alpha

- Removed unused development-only mixin that can cause crashes with Sodium

## 4.0.0.27-alpha

- Animal Harvest ritual now has retries when failing to generate any loot; that value is configurable
- Chances for turtle scute and goat horns to drop has been adjusted to a flat 0.07% chance rather than the stacked 10% chance after a 6.6% chance.
- Modifiers no longer display in an extremely 'stacked' manner when saving and re-entering a world in single player.

## 4.0.0.28-alpha

- Again remove references to mixins that can cause issues with other mods

## 4.0.0.29-alpha

- Again, change how 'dev' mixins are handled to prevent future issues
- Fix how continuous spells, such as Growth Infusion, have their costs calculated and charged to prevent them from over-charging

## 4.0.0.30-alpha

- Improved decay configuration for withers, default maximum health can be reduced 4 times by 20 instead of by a total of 200
- Decay cooldown now scales configurably based on the number of hearts removed from the maximum health
- Decay tooltips now clarify that decay is a permanent effect
- Decay cooldowns are further increased by a configurable modifier (default 2.5) if tagged entity is a "boss"
- Shatter now displays block break particles and plays block break sounds
- Fixed some missing modifier descriptions
- Aqua Bubble and Light Drifter tooltips no longer display cooldowns; these will be automatically added in a future release to all spells
- Ender dragons and withers are bosses with an override to `addEffect` that prevents any potion effect from being applied to them; thus they are ineligible for Geas
- Aliases added for previously removed spells and rituals (#1382) hopefully resolving crash with FTB Quests
- 