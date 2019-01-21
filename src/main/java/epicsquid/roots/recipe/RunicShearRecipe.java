package epicsquid.roots.recipe;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;

/**
 * Transmutation recipe for Runic Shears
 */
public class RunicShearRecipe {

  private Block block;
  private Block replacementBlock;
  private Item drop;

  private EntityLiving entity;

  public RunicShearRecipe(Block block, Block replacementBlock, Item drop) {
    this.block = block;
    this.replacementBlock = replacementBlock;
    this.drop = drop;
  }

  public RunicShearRecipe(Item drop, EntityLiving entity) {
    this.drop = drop;
    this.entity = entity;
  }

  public boolean isBlockRecipe() {
    return block != null;
  }

  public boolean isEntityRecipe() {
    return entity != null;
  }

  public Block getBlock() {
    return block;
  }

  public Block getReplacementBlock() {
    return replacementBlock;
  }

  public Item getDrop() {
    return drop;
  }

  public EntityLiving getEntity() {
    return entity;
  }
}
