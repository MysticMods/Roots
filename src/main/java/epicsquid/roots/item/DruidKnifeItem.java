package epicsquid.roots.item;

import epicsquid.mysticallib.item.KnifeItem;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.dispenser.KnifeDispenser;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;

public class DruidKnifeItem extends KnifeItem {

  public DruidKnifeItem(IItemTier tier, float attackDamage, float attackSpeed, Item.Properties props) {
    super(tier, attackDamage, attackSpeed, props);
    ModItems.knives.add(this);

    DispenserBlock.registerDispenseBehavior(this, KnifeDispenser.getInstance());
  }


  @Override
  @Nonnull
  @SuppressWarnings("deprecation")
  public ActionResultType onItemUse(ItemUseContext context) {
/*    @Nonnull PlayerEntity player,
  } @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Hand hand, @Nonnull Direction facing, float hitX, float hitY, float hitZ) {*/
    if (context.getHand() == Hand.MAIN_HAND) {
      ItemStack offhand = context.getPlayer().getHeldItemOffhand();
      if (!offhand.isEmpty() && HerbRegistry.isHerb(offhand.getItem())) {
        /*RunicCarvingRecipe recipe = ModRecipes.getRunicCarvingRecipe(world.getBlockState(pos), HerbRegistry.getHerbByItem(offhand.getItem()));
        if (recipe != null) {
          world.setBlockState(pos, recipe.getRuneBlock());

          if (!player.isCreative()) {
            player.getHeldItemMainhand().damageItem(1, player);
          }
        }*/
      } else {
/*        if (!MossConfig.getBlacklistDimensions().contains(world.provider.getDimension())) {
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
        }*/
      }
      return ActionResultType.SUCCESS;
    }
    return ActionResultType.PASS;
  }
}