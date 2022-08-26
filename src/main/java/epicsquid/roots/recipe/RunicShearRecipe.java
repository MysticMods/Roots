package epicsquid.roots.recipe;

import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

/**
 * Transmutation recipe for Runic Shears
 */
public class RunicShearRecipe extends RegistryItem {

  private Block block;
  private Block replacementBlock;
  private ItemStack drop;
  private ItemStack optionalDisplayItem;

  public RunicShearRecipe(ResourceLocation name, Block block, Block replacementBlock, ItemStack drop, ItemStack optionalDisplayItem) {
    setRegistryName(name);
    this.block = block;
    this.replacementBlock = replacementBlock;
    this.drop = drop;
    this.optionalDisplayItem = optionalDisplayItem;
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

  public ItemStack getOptionalDisplayItem() {
    return optionalDisplayItem;
  }
}
