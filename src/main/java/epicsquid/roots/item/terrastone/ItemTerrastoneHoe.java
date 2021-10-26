package epicsquid.roots.item.terrastone;

import epicsquid.mysticallib.item.ItemHoeBase;
import epicsquid.roots.config.ToolConfig;
import epicsquid.roots.item.ILivingRepair;
import epicsquid.roots.recipe.ingredient.RootsIngredients;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTerrastoneHoe extends ItemHoeBase implements ILivingRepair {
  public ItemTerrastoneHoe(ToolMaterial material, String name) {
    super(material, name, 3, 565, () -> Ingredient.EMPTY);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected, 20);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public ActionResultType onItemUse(PlayerEntity player, World worldIn, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
    ItemStack itemstack = player.getHeldItem(hand);

    if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
      return ActionResultType.FAIL;
    } else {
      int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
      if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;

      BlockState iblockstate = worldIn.getBlockState(pos);
      Block block = iblockstate.getBlock();
      BlockState farmland;
      if (ToolConfig.HoeMoisturises) {
        farmland = net.minecraft.block.Blocks.FARMLAND.getDefaultState().withProperty(FarmlandBlock.MOISTURE, 7);
      } else {
        farmland = Blocks.FARMLAND.getDefaultState();
      }

      if (facing != Direction.DOWN && worldIn.isAirBlock(pos.up())) {
        if (block == net.minecraft.block.Blocks.GRASS || block == net.minecraft.block.Blocks.GRASS_PATH) {
          this.setBlock(itemstack, player, worldIn, pos, farmland);
          return ActionResultType.SUCCESS;
        }

        if (block == net.minecraft.block.Blocks.DIRT) {
          switch (iblockstate.get(BlockDirt.VARIANT)) {
            case DIRT:
              this.setBlock(itemstack, player, worldIn, pos, farmland);
              return ActionResultType.SUCCESS;
            case COARSE_DIRT:
              this.setBlock(itemstack, player, worldIn, pos, net.minecraft.block.Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
              return ActionResultType.SUCCESS;
          }
        }
      }

      return ActionResultType.PASS;
    }
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
    if (!worldIn.isRemote) {
      stack.damageItem(1, entityLiving);
    }

    final Block block = state.getBlock();
    final Material material = state.getMaterial();
    if (block instanceof net.minecraftforge.common.IShearable) return true;
    return material == Material.LEAVES || material == Material.VINE || super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
  }

  @Override
  public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
    if (player.world.isRemote || player.capabilities.isCreativeMode) {
      return false;
    }
    if (!ToolConfig.HoeSilkTouch) {
      return super.onBlockStartBreak(itemstack, pos, player);
    }
    final BlockState state = player.world.getBlockState(pos);
    final Block block = state.getBlock();
    final Material material = state.getMaterial();
    if (block instanceof net.minecraftforge.common.IShearable && (material == Material.VINE || material == Material.LEAVES)) {
      net.minecraftforge.common.IShearable target = (net.minecraftforge.common.IShearable) block;
      if (target.isShearable(itemstack, player.world, pos)) {
        java.util.List<ItemStack> drops = target.onSheared(itemstack, player.world, pos,
            net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack));
        java.util.Random rand = new java.util.Random();

        for (ItemStack stack : drops) {
          float f = 0.7F;
          double d = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          double d1 = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          double d2 = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          ItemEntity entityitem = new ItemEntity(player.world, (double) pos.getX() + d, (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
          entityitem.setDefaultPickupDelay();
          player.world.addEntity(entityitem);
        }

        itemstack.damageItem(1, player);
        player.addStat(Stats.getBlockStats(block));
        player.world.setBlockState(pos, net.minecraft.block.Blocks.AIR.getDefaultState(), 11); //TODO: Move to IShearable implementors in 1.12+
        return true;
      }
    }
    return false;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add("");
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && RootsIngredients.MOSSY_COBBLESTONE.test(repair);
  }
}
