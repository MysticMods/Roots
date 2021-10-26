package epicsquid.roots.recipe;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.transmutation.PropertyPredicate;
import net.minecraft.block.BeetrootBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.horse.*;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RunicShearRecipes {

  public static void initRecipes() {
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation(Roots.MODID, "wildewheet"), new PropertyPredicate(net.minecraft.block.Blocks.WHEAT.getDefaultState().with(CropsBlock.AGE, 7), CropsBlock.AGE), Blocks.WHEAT.getDefaultState().with(CropsBlock.AGE, 0), new ItemStack(ModItems.wildewheet), new ItemStack(Items.WHEAT)));
/*    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation(Roots.MODID, "aubergine"), new PropertyPredicate(Blocks.CARROTS.getDefaultState().with(BlockCrops.AGE, 7), BlockCrops.AGE), Blocks.CARROTS.getDefaultState().with(BlockCrops.AGE, 0), new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine), new ItemStack(Items.CARROT)));*/
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(new ResourceLocation(Roots.MODID, "spirit_herb"), new PropertyPredicate(net.minecraft.block.Blocks.BEETROOTS.getDefaultState().with(BeetrootBlock.BEETROOT_AGE, 3), BeetrootBlock.BEETROOT_AGE), Blocks.BEETROOTS.getDefaultState().with(BeetrootBlock.BEETROOT_AGE, 0), new ItemStack(ModItems.spirit_herb), new ItemStack(Items.BEETROOT)));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "cow_fey_leather"), new ItemStack(ModItems.fey_leather, 1), CowEntity.class, 20 * 30));
/*    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "deer_fey_leather"), new ItemStack(ModItems.fey_leather, 1), EntityDeer.class, 20 * 30));*/
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "llama_fey_leather"), new ItemStack(ModItems.fey_leather, 1), LlamaEntity.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "horse_fey_leather"), new ItemStack(ModItems.fey_leather, 1), HorseEntity.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "donkey_fey_leather"), new ItemStack(ModItems.fey_leather, 1), DonkeyEntity.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "mule_fey_leather"), new ItemStack(ModItems.fey_leather, 1), MuleEntity.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "mooshroom_fey_leather"), new ItemStack(ModItems.fey_leather, 1), MooshroomEntity.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "rabbit_fey_leather"), new ItemStack(ModItems.fey_leather, 1), RabbitEntity.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "bat_fey_leather"), new ItemStack(ModItems.fey_leather, 1), BatEntity.class, 20 * 30));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "pig_fey_leather"), new ItemStack(ModItems.fey_leather, 1), PigEntity.class, 20 * 30));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "chicken_mystic_feather"), new ItemStack(ModItems.mystic_feather, 1), ChickenEntity.class, 20 * 45));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "parrot_mystic_feather"), new ItemStack(ModItems.mystic_feather, 1), ParrotEntity.class, 20 * 45));
/*    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "owl_mystic_feather"), new ItemStack(ModItems.mystic_feather, 1), EntityOwl.class, 20 * 45));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "beetle_strange_ooze"), new ItemStack(ModItems.strange_ooze, 1), EntityBeetle.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "frog_strange_ooze"), new ItemStack(ModItems.strange_ooze, 1), EntityFrog.class, 20 * 120));*/
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "squid_strange_ooze"), new ItemStack(ModItems.strange_ooze, 1), SquidEntity.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "slime_strange_ooze"), new ItemStack(ModItems.strange_ooze, 1), SlimeEntity.class, 20 * 120));

/*    ModRecipes.addRunicShearRecipe(new RunicShearConditionalEntityRecipe(new ResourceLocation(Roots.MODID, "sprout_stuff"), entityLivingBase -> {
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

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "hell_sprout"), new ItemStack(Items.MAGMA_CREAM), EntityHellSprout.class, 20 * 180));*/

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "wolf_bone"), new ItemStack(Items.BONE, 1), WolfEntity.class, 20 * 180));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "ocelot_string"), new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()), OcelotEntity.class, 20 * 180));

/*    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "silverfish_silver"), new ItemStack(Materials.silver.getNugget(), 2), EntitySilverfish.class, 20 * 120));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "clam_pearl"), new ItemStack(epicsquid.mysticalworld.init.ModItems.pearl), EntityClam.class, 20 * 60 * 4));*/

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "villager_spirit_bag"), new ItemStack(ModItems.spirit_bag), VillagerEntity.class, 20 * 60 * 5));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "spider_eyes"), new ItemStack(net.minecraft.item.Items.SPIDER_EYE), SpiderEntity.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "cave_spider_eyes"), new ItemStack(Items.FERMENTED_SPIDER_EYE), CaveSpiderEntity.class, 20 * 120));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "skeletal_horse_bone"), new ItemStack(Items.BONE, 2), SkeletonHorseEntity.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "zombie_horse_flesh"), new ItemStack(Items.ROTTEN_FLESH, 2), ZombieHorseEntity.class, 20 * 120));

    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "polar_bear_ice"), new ItemStack(net.minecraft.block.Blocks.ICE, 6), PolarBearEntity.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "snowman_snowballs"), new ItemStack(Items.SNOWBALL, 8), SnowGolemEntity.class, 20 * 120));
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "iron_golem_books"), new ItemStack(Items.BOOK, 3), IronGolemEntity.class, 20 * 120));

/*    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "lava_cat"), new ItemStack(Blocks.OBSIDIAN), EntityLavaCat.class, 20 * 60 * 4));*/
    ModRecipes.addRunicShearRecipe(new RunicShearEntityRecipe(new ResourceLocation(Roots.MODID, "zombie_pigman_gold"), new ItemStack(Items.GOLD_NUGGET), ZombiePigmanEntity.class, 20 * 60 * 3));
  }
}
