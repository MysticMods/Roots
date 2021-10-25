package epicsquid.roots.recipe;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IngredientNBT;

import javax.annotation.Nullable;

public class SummonCreatureIntermediate extends SummonCreatureRecipe {
  private static ResourceLocation empty = new ResourceLocation(Roots.MODID, "generated");

  private final ItemStack essenceStack;

  private SummonCreatureIntermediate(Class<? extends LivingEntity> clazz, ItemStack essenceStack, Ingredient... ingredients) {
    super(empty, clazz, ingredients);
    this.essenceStack = essenceStack;
  }

  public ItemStack getEssenceStack() {
    return essenceStack;
  }

  @Nullable
  public static SummonCreatureIntermediate create(Class<? extends LivingEntity> clazz) {
    ResourceLocation rl = EntityList.getKey(clazz);
    if (rl == null) {
      return null;
    }

    ItemStack stack = new ItemStack(ModItems.life_essence);
    CompoundNBT tag = stack.getTagCompound();
    if (tag == null) {
      tag = new CompoundNBT();
      stack.setTagCompound(tag);
    }

    tag.setString("id", rl.toString());

    return new SummonCreatureIntermediate(clazz, stack, IngredientNBT.fromStacks(stack));
  }
}
