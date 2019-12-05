package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemKnifeBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.config.MossConfig;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.item.dispenser.DispenseKnife;
import epicsquid.roots.recipe.RunicCarvingRecipe;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemDruidKnife extends ItemKnifeBase {

  public ItemDruidKnife(String name, ToolMaterial material) {
    super(name, material);
    ModItems.knives.add(this);

    DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseKnife.getInstance());
  }

  @Override
  @Nonnull
  @SuppressWarnings("deprecation")
  public ActionResultType onItemUse(@Nonnull PlayerEntity player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Hand hand, @Nonnull Direction facing, float hitX, float hitY, float hitZ) {
    if (hand == Hand.MAIN_HAND) {
      ItemStack offhand = player.getHeldItemOffhand();
      if (!offhand.isEmpty() && HerbRegistry.isHerb(offhand.getItem())) {
        RunicCarvingRecipe recipe = ModRecipes.getRunicCarvingRecipe(world.getBlockState(pos), HerbRegistry.getHerbByItem(offhand.getItem()));
        if (recipe != null) {
          world.setBlockState(pos, recipe.getRuneBlock());

          if (!player.isCreative()) {
            player.getHeldItemMainhand().damageItem(1, player);
          }
        }
      } else {
        if (!MossConfig.getBlacklistDimensions().contains(world.provider.getDimension())) {
          // Used to get terramoss from a block of cobble. This can also be done using runic shears.
          BlockState state = world.getBlockState(pos);
          BlockState result = MossConfig.scrapeResult(state);
          if (result != null) {
            if (!world.isRemote) {
              world.setBlockState(pos, result);
              world.scheduleBlockUpdate(pos, result.getBlock(), 1, result.getBlock().tickRate(world));
              ItemUtil.spawnItem(world, player.getPosition().add(0, 1, 0), new ItemStack(ModItems.terra_moss));
              if (!player.capabilities.isCreativeMode) {
                player.getHeldItem(hand).damageItem(1, player);
              }
            }
            world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
          }
        }
      }
      return ActionResultType.SUCCESS;
    }
    return ActionResultType.PASS;
  }
}