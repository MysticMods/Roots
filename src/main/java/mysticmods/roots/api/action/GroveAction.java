package mysticmods.roots.api.action;

import java.util.function.Consumer;

// Server level
// Player
// List<Triplet<BlockPos, BlockState, BlockState>> old state -> new state
// Block entity
// Direct entity
// Indirect entity


// Actions:
// - Growing crops (from one stage to another, not just random ticks)
// -> level, player, <blockpos, blockstate, blockstate>, spell instance
// - Successfully breeding (producing offspring) two animals
// -> level, player, parent1, parent2, offspring, spell instance
// - Aging up a baby animal
// -> level, player, baby, spellinstance, itemstack
// - Breaking blocks with shatter -> pickaxe?
// -> level, player, list<blockpos, blockstate>, spell instance
// - Flying with sky soarer
// -> level, player, spell instance
// - Successfully trading with a villager giving them experience
// -> level, player, villager, spell instance
// - Killing a peaceful animal
// -> level, player, indirect entity, spell instance, hand, damage source
// - Curing a zombie villager

// - Taming an animal
// -> level, player, animal, spell instance, itemstack
// - Eating specific food
// -> level, player, spell instance, itemstack
// - Crafting specific items
// -> level, player, recipe, itemstack, container?
// - Brush a block
// -> level, player, blockstate, block entity, itemstack
// - Trade with a piglin
// -> level, player, piglin, itemstack, spell instance
// - Harvest a bee hive
// -> level, player, blockpos, blockstate, block entity, itemstack
// - Defeat an undead creature
// -> level, player, entity, indirect entity, itemstack, spell instance
// - Defeat a wither
// -> level, player, entity, indirect entity, itemstack, spell instance
// - Defeat the ender dragon
// -> level, player, entity, indirect entity, itemstack, spell instance
// - Milking a cow
// -> level, player, entity, itemstack, spell instance
// - Completing a trial
// - Successfully composting
// - Spreading mushrooms
// - Growing a big mushroom
// - Successfully casting a geas
// - Successfully draining an enemy
// -

// Milestones
// - Visit the end for the first time
// - Visit the nether for the first time

public interface GroveAction extends Consumer<GroveContext>, GroveContextUser {

}