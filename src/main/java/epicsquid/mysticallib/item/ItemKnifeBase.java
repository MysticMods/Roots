package epicsquid.mysticallib.item;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.google.common.collect.Sets;

import epicsquid.mysticallib.material.MaterialTypes;
import epicsquid.mysticallib.types.OneTimeSupplier;
import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class ItemKnifeBase extends ItemToolBase {
  public static Set<Block> BLOCKS = Sets.newHashSet(Blocks.PLANKS, Blocks.LOG, Blocks.LOG2);

  public ItemKnifeBase(String name, ToolMaterial material, Supplier<Ingredient> repair) {
    super(name, MaterialTypes.stats(material) != null ? (Math.max(1f, MaterialTypes.stats(material).damage - 1.0f)) : 1.0f, MaterialTypes.stats(material) != null ? MaterialTypes.stats(material).speed : -2.0f, material, BLOCKS, repair);
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    stack.damageItem(1, attacker);
    return true;
  }

  @Override
  @Nonnull
  public Set<String> getToolClasses(ItemStack stack) {
    return Collections.singleton("druidKnife");
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
    return !ItemUtil.equalWithoutDamage(oldStack, newStack);
  }

  @Override
  public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
    return !ItemUtil.equalWithoutDamage(oldStack, newStack);
  }
}
