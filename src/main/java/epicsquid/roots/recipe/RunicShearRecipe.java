package epicsquid.roots.recipe;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Transmutation recipe for Runic Shears
 */
public class RunicShearRecipe {

  private Block block;
  private Block replacementBlock;
  private ItemStack drop;
  private ItemStack optionalDisplayItem = ItemStack.EMPTY;
  private String name;

  private Class<? extends EntityLivingBase> entity = null;
  private int cooldown = 0;

  public RunicShearRecipe(Block block, Block replacementBlock, ItemStack drop, String name, ItemStack optionalDisplayItem) {
    this.block = block;
    this.replacementBlock = replacementBlock;
    this.drop = drop;
    this.name = name;
    this.optionalDisplayItem = optionalDisplayItem;
  }

  public RunicShearRecipe(Block block, Block replacementBlock, ItemStack drop, String name) {
    this(block, replacementBlock, drop, name, null);
  }

  public RunicShearRecipe(ItemStack drop, Class<? extends EntityLiving> entity, int cooldown, String name) {
    this.drop = drop;
    this.entity = entity;
    this.name = name;
    this.cooldown = cooldown;
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

  public ItemStack getDrop() {
    return drop;
  }

  public Class<? extends EntityLivingBase> getEntity() {
    return entity;
  }

  public String getName() {
    return name;
  }

  public int getCooldown() {
    return cooldown;
  }

  public ItemStack getOptionalDisplayItem() {
    return optionalDisplayItem;
  }
}
