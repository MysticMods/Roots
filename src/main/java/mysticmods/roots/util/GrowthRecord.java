package mysticmods.roots.util;

import net.minecraft.world.level.block.Block;

public record GrowthRecord (Block cropBlock, String ageProperty, int maximumAge) {
  // Block

  // Age property (if any)

  // Maximum age property value (-1 if no age property)

  /*
    Potatoes: nothing special -> replace with age 0 -> self seed
    Carrots: nope -> replace with age 0 -> self seed
    Nether wart: how does this grow? Nothing special -> replace with age 0 -> self seed
    Berry bush: nope -> replace with age 0 -> self seed but doesn't consume seed to be grown
    Pitcher crop:
      Crop becomes new block
      Grows in multipart
      No seed
      Replace with air
    Torchflower:
      Crop becomes new block
      No seed
      Replace with air
    Beetroot: nothing special -> replace with age 0 -> has own seed item
    Melon:
      Crop becomes new block (attached to facing)
      Spawns nearby block (melon)
      Doesn't actually produce a crop
      When crop is broken reverts to original block
    Pumpkin:
      Same as melon
    Glow lichen:
      Only bone meal
      Doesn't tick
    Vines:
      Ticks and spreads randomly
    Weeping/twisting vines:
      Can be sheared to stop growing:
      age becomes 25, stops growing
      Once grown becomes weeping/twisting vines plant
      Only grow the non-plant block
    Sugar cane/cactus:
      Has a specific height limit (determine limit)
      Extra ticks will cause the top block to grow
      Non-top blocks shouldn't accept ticks
    Bamboo:
      Bamboo sapling
      Becomes bamboo
      Has an age property
      Only the top-most block should grow
      Has a specific height limit
      Non-top blocks shouldn't accept ticks
    Mushrooms:
      Red/brown:
        Can spread
        Can be grown into a big mushroom
        Has light limits
      Warped/crimson:
    Kelp:
    Cocoa:
    Saplings:

    Bushes, grasses:
      No growth ticks




   */
}
