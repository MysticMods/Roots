package epicsquid.roots.recipe;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IngredientNBT;

import javax.annotation.Nullable;

public class SummonCreatureIntermediate extends SummonCreatureRecipe {
  private static ResourceLocation empty = new ResourceLocation(Roots.MODID, "generated");

  protected SummonCreatureIntermediate(Class<? extends EntityLivingBase> clazz, Ingredient... ingredients) {
    super(empty, clazz, ingredients);
  }

  @Nullable
  public static SummonCreatureIntermediate create(Class<? extends EntityLivingBase> clazz) {
    ResourceLocation rl = EntityList.getKey(clazz);
    if (rl == null) {
      return null;
    }

    ItemStack stack = new ItemStack(ModItems.life_essence);
    NBTTagCompound tag = stack.getTagCompound();
    if (tag == null) {
      tag = new NBTTagCompound();
      stack.setTagCompound(tag);
    }

    tag.setString("id", rl.toString());

    return new SummonCreatureIntermediate(clazz, IngredientNBT.fromStacks(stack));
  }
}
