package epicsquid.roots.recipe;

import com.google.common.collect.Sets;
import epicsquid.mysticalworld.entity.*;
import epicsquid.mysticalworld.materials.Materials;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.transmutation.PropertyPredicate;
import net.minecraft.block.BlockBeetroot;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "bat_fey_leather"), new ItemStack(ModItems.fey_leather, 1), EntityBat.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "pig_fey_leather"), new ItemStack(ModItems.fey_leather, 1), EntityPig.class, 20 * 30));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "chicken_mystic_feather"), new ItemStack(ModItems.mystic_feather, 1), EntityChicken.class, 20 * 45));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "parrot_mystic_feather"), new ItemStack(ModItems.mystic_feather, 1), EntityParrot.class, 20 * 45));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "owl_mystic_feather"), new ItemStack(ModItems.mystic_feather, 1), EntityOwl.class, 20 * 45));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "beetle_strange_ooze"), new ItemStack(ModItems.strange_ooze, 1), EntityBeetle.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "frog_strange_ooze"), new ItemStack(ModItems.strange_ooze, 1), EntityFrog.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "squid_strange_ooze"), new ItemStack(ModItems.strange_ooze, 1), EntitySquid.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "slime_strange_ooze"), new ItemStack(ModItems.strange_ooze, 1), EntitySlime.class, 20 * 120));

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

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "hell_sprout"), new ItemStack(Items.MAGMA_CREAM), EntityHellSprout.class, 20 * 180));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "wolf_bone"), new ItemStack(Items.BONE, 1), EntityWolf.class, 20 * 180));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "ocelot_string"), new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()), EntityOcelot.class, 20 * 180));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "silverfish_silver"), new ItemStack(Materials.silver.getNugget(), 2), EntitySilverfish.class, 20 * 120));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "clam_pearl"), new ItemStack(epicsquid.mysticalworld.init.ModItems.pearl), EntityClam.class, 20 * 60 * 4));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "villager_spirit_bag"), new ItemStack(ModItems.spirit_bag), EntityVillager.class, 20 * 60 * 5));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "spider_eyes"), new ItemStack(Items.SPIDER_EYE), EntitySpider.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "cave_spider_eyes"), new ItemStack(Items.FERMENTED_SPIDER_EYE), EntityCaveSpider.class, 20 * 120));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "skeletal_horse_bone"), new ItemStack(Items.BONE, 2), EntitySkeletonHorse.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "zombie_horse_flesh"), new ItemStack(Items.ROTTEN_FLESH, 2), EntityZombieHorse.class, 20 * 120));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "polar_bear_ice"), new ItemStack(Blocks.ICE, 6), EntityPolarBear.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "snowman_snowballs"), new ItemStack(Items.SNOWBALL, 8), EntitySnowman.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "iron_golem_books"), new ItemStack(Items.BOOK, 3), EntityIronGolem.class, 20 * 120));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "lava_cat"), new ItemStack(Blocks.OBSIDIAN), EntityLavaCat.class, 20 * 60 * 4));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "zombie_pigman_gold"), new ItemStack(Items.GOLD_NUGGET), EntityPigZombie.class, 20 * 60 * 3));
  }
}
