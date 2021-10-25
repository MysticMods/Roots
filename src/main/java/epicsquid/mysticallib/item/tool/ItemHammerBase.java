package epicsquid.mysticallib.item.tool;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import epicsquid.mysticallib.item.ItemPickaxeBase;
import epicsquid.mysticallib.types.OneTimeSupplier;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;
import java.util.function.Supplier;

public abstract class ItemHammerBase extends ItemPickaxeBase implements IItemSizedTool, IBlacklistingTool {
  private static final Set<Material> EFFECTIVE_MATERIALS = ImmutableSet.of(Material.ROCK, Material.IRON, Material.GLASS, Material.ICE, Material.PACKED_ICE, Material.ANVIL, Material.REDSTONE_LIGHT);

  public ItemHammerBase(String name, int maxDamage, ToolMaterial materialIn, Supplier<Ingredient> repair) {
    super(materialIn, name, materialIn.getHarvestLevel(), maxDamage, repair);
    setMaxDamage(maxDamage);
    setHarvestLevel("pickaxe", materialIn.getHarvestLevel());
  }

  @Override
  public Set<Material> getEffectiveMaterials() {
    return EFFECTIVE_MATERIALS;
  }

  @Override
  public int getWidth(ItemStack stack) {
    return 3;
  }

  @Override
  public float getEfficiency() {
    return efficiency;
  }

  @Override
  public float getDestroySpeed(ItemStack stack, IBlockState state) {
    float destroy = getSizedDestroySpeed(stack, state);
    if (destroy != -999) {
      return destroy;
    }
    return super.getDestroySpeed(stack, state);
  }

  @Override
  public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
    return onSizedBlockStartBreak(itemstack, pos, player);
  }
}
