package epicsquid.roots.recipe;

import com.google.common.collect.Sets;
import epicsquid.mysticalworld.entity.*;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.transmutation.PropertyPredicate;
import net.minecraft.block.BlockBeetroot;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public class RunicShearRecipes {

  public static void initRecipes() {
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation(Roots.MODID, "wildewheet"), new PropertyPredicate(Blocks.WHEAT.getDefaultState().withProperty(BlockCrops.AGE, 7), BlockCrops.AGE), Blocks.WHEAT.getDefaultState().withProperty(BlockCrops.AGE, 0), new ItemStack(ModItems.wildewheet), new ItemStack(Items.WHEAT)));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation(Roots.MODID, "aubergine"), new PropertyPredicate(Blocks.CARROTS.getDefaultState().withProperty(BlockCrops.AGE, 7), BlockCrops.AGE), Blocks.CARROTS.getDefaultState().withProperty(BlockCrops.AGE, 0), new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine), new ItemStack(Items.CARROT)));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation(Roots.MODID, "spirit_herb"), new PropertyPredicate(Blocks.BEETROOTS.getDefaultState().withProperty(BlockBeetroot.BEETROOT_AGE, 3), BlockBeetroot.BEETROOT_AGE), Blocks.BEETROOTS.getDefaultState().withProperty(BlockBeetroot.BEETROOT_AGE, 0), new ItemStack(ModItems.spirit_herb), new ItemStack(Items.BEETROOT)));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "cow_fey_leather"), new ItemStack(ModItems.fey_leather, 1), EntityCow.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "deer_fey_leather"), new ItemStack(ModItems.fey_leather, 1), EntityDeer.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "llama_fey_leather"), new ItemStack(ModItems.fey_leather, 1), EntityLlama.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "horse_fey_leather"), new ItemStack(ModItems.fey_leather, 1), EntityHorse.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "donkey_fey_leather"), new ItemStack(ModItems.fey_leather, 1), EntityDonkey.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "mule_fey_leather"), new ItemStack(ModItems.fey_leather, 1), EntityMule.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "mooshroom_fey_leather"), new ItemStack(ModItems.fey_leather, 1), EntityMooshroom.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "rabbit_fey_leather"), new ItemStack(ModItems.fey_leather, 1), EntityRabbit.class, 20 * 30));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "chicken_fey_feather"), new ItemStack(ModItems.fey_feather, 1), EntityChicken.class, 20 * 45));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "parrot_fey_feather"), new ItemStack(ModItems.fey_feather, 1), EntityParrot.class, 20 * 45));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "owl_fey_feather"), new ItemStack(ModItems.fey_feather, 1), EntityOwl.class, 20 * 45));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "beetle_strange_slime"), new ItemStack(ModItems.strange_slime, 1), EntityBeetle.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "frog_strange_slime"), new ItemStack(ModItems.strange_slime, 1), EntityFrog.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "squid_strange_slime"), new ItemStack(ModItems.strange_slime, 1), EntitySquid.class, 20 * 120));

    ModRecipes.addRunicShearRecipe(new RunicShearConditionalEntityRecipe(new ResourceLocation(Roots.MODID, "sprout_stuff"), entityLivingBase -> {
      EntitySprout sprout = (EntitySprout) entityLivingBase;
      switch (sprout.getDataManager().get(EntitySprout.variant)) {
        default:
        case 0: {
          return new ItemStack(Items.MELON_SEEDS);
        }
        case 1: {
          return new ItemStack(ModItems.terra_spores);
        }
        case 2: {
          return new ItemStack(Items.BEETROOT_SEEDS);
        }
        case 3: {
          return new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine_seed);
        }
      }
    }, Sets.newHashSet(new ItemStack(Items.MELON_SEEDS), new ItemStack(ModItems.terra_spores), new ItemStack(Items.BEETROOT_SEEDS), new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine_seed)), EntitySprout.class, 20 * 180));

//    addPacifistEntry("pig", EntityPig.class);
//    addPacifistEntry("sheep", EntitySheep.class);
//    addPacifistEntry("wolf", EntityWolf.class);
  }
}
