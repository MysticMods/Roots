package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.tileentity.TileEntityPyre;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class ItemFireStarter extends ItemBase {
  public ItemFireStarter(@Nonnull String name) {
    super(name);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    player.setActiveHand(hand);
    return ActionResult.newResult(ActionResultType.SUCCESS, stack);
  }

  @Override
  public int getItemBurnTime(ItemStack itemStack) {
    return 200;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
    if (!world.isRemote && entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      RayTraceResult result = rayTrace(entity.world, player, false);
      //noinspection ConstantConditions
      if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
        Direction facing = result.sideHit;
        BlockPos hit = result.getBlockPos();

        BlockState hitState = world.getBlockState(hit);
        boolean pyre = hitState.getBlock() == ModBlocks.pyre || hitState.getBlock() == ModBlocks.reinforced_pyre;

        hit = hit.offset(facing);
        if (!player.canPlayerEdit(hit, facing, stack)) {
          return stack;
        }

        boolean used = false;

        if (!pyre && world.isAirBlock(hit)) {
          world.playSound(player, hit, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
          world.setBlockState(hit, Blocks.FIRE.getDefaultState(), 11);
          used = true;
        } else {
          TileEntity te = world.getTileEntity(result.getBlockPos());
          if (te instanceof TileEntityPyre) {
            TileEntityPyre pyreTe = (TileEntityPyre) te;
            pyreTe.startRitual(player.getUniqueID(), false);
            pyreTe.setLastPlayerId(player.getUniqueID());
            used = true;
          }
        }


        // TODO: Pyres?
        if (used && !player.isCreative()) {
          stack.shrink(1);
        }
        return stack;
      }
    }

    return stack;
  }

  @Override
  public UseAction getItemUseAction(ItemStack stack) {
    return UseAction.BOW;
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return GeneralConfig.FireStarterTicks;
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void onUsingTick(ItemStack stack, LivingEntity entity, int count) {
    if (entity.world.isRemote && entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      RayTraceResult result = rayTrace(entity.world, player, false);
      if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK && count % 3 == 0) {
        for (int i = 0; i < 2 + player.rand.nextInt(3); i++) {
          player.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, result.hitVec.x, result.hitVec.y, result.hitVec.z, 0, 0.05, 0);
        }
      }
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    tooltip.add("");
    tooltip.add(TextFormatting.YELLOW + I18n.format("roots.fire_starter.tooltip"));
  }
}
