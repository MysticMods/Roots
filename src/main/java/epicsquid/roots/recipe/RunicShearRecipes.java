package epicsquid.roots.recipe;

import epicsquid.mysticalworld.entity.EntityDeer;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RunicShearRecipes {

  public static void initRecipes() {
    ModRecipes
        .addRunicShearRecipe(new RunicShearRecipe(Blocks.MOSSY_COBBLESTONE, Blocks.COBBLESTONE, new ItemStack(ModItems.terra_moss), "terra_moss", new ItemStack(Blocks.MOSSY_COBBLESTONE)));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.WHEAT, null, new ItemStack(ModItems.wildewheet), "wildewheet", new ItemStack(Items.WHEAT)));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.CARROTS, null, new ItemStack(ModItems.aubergine), "aubergine", new ItemStack(Items.CARROT)));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.BEETROOTS, null, new ItemStack(ModItems.spirit_herb), "spirit_herb", new ItemStack(Items.BEETROOT)));

    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ItemStack(ModItems.fay_leather, 1), EntityCow.class, 20*60, "cow_fay_leather"));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ItemStack(ModItems.fay_leather, 1), EntityDeer.class, 20*60, "deer_fay_leather"));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ItemStack(ModItems.fay_leather, 1), EntityLlama.class, 20*60, "llama_fay_leather"));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ItemStack(ModItems.fay_leather, 1), EntityHorse.class, 20*60, "horse_fay_leather"));
  }
}
