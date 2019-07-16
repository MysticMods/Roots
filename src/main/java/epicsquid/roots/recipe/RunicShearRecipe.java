package epicsquid.roots.recipe;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Transmutation recipe for Runic Shears
 */
public class RunicShearRecipe {

  private Block block;
  private Block replacementBlock;
  private ItemStack drop;
  private ItemStack optionalDisplayItem = ItemStack.EMPTY;
  private String name;

  private Entity entity = null;
  private Class<? extends Entity> clazz = null;
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

  public RunicShearRecipe(ItemStack drop, Class<? extends Entity> entity, int cooldown, String name) {
    this.drop = drop;
    this.clazz = entity;
    this.name = name;
    this.cooldown = cooldown;
  }

  public boolean isBlockRecipe() {
    return block != null;
  }

  public boolean isEntityRecipe() {
    return clazz != null;
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

  public Class<? extends Entity> getClazz() {
    return clazz;
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

  @SideOnly(Side.CLIENT)
  @Nullable
  public Entity getEntity () {
    if (entity == null) {
      Minecraft mc = Minecraft.getMinecraft();
      try {
        entity = clazz.getConstructor(World.class).newInstance(mc.world);
      } catch (Exception e) {
        return null;
      }
    }

    return entity;
  }
}
