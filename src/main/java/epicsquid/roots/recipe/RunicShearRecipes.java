package epicsquid.roots.recipe;

import epicsquid.mysticalworld.entity.EntityDeer;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import jeresources.util.LootTableHelper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.horse.MuleEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RunicShearRecipes {

  public static void initRecipes() {
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation(Roots.MODID, "wildewheet"), Blocks.WHEAT, null, new ItemStack(ModItems.wildewheet), new ItemStack(Items.WHEAT)));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation(Roots.MODID, "aubergine"), Blocks.CARROTS, null, new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine), new ItemStack(Items.CARROT)));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation(Roots.MODID, "spirit_herb"), Blocks.BEETROOTS, null, new ItemStack(ModItems.spirit_herb), new ItemStack(Items.BEETROOT)));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "cow_fey_leather"), new ItemStack(ModItems.fey_leather, 1), CowEntity.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "deer_fey_leather"), new ItemStack(ModItems.fey_leather, 1), EntityDeer.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "llama_fey_leather"), new ItemStack(ModItems.fey_leather, 1), LlamaEntity.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "horse_fey_leather"), new ItemStack(ModItems.fey_leather, 1), HorseEntity.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "donkey_fey_leather"), new ItemStack(ModItems.fey_leather, 1), DonkeyEntity.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "mule_fey_leather"), new ItemStack(ModItems.fey_leather, 1), MuleEntity.class, 20 * 30));
  }
}
