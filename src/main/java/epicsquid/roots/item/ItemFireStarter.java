package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.block.BlockPyre;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.tileentity.TileEntityPyre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
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
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (stack.getItemDamage() < stack.getMaxDamage()) {
      player.setActiveHand(hand);
      return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    return ActionResult.newResult(EnumActionResult.PASS, stack);
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
    if (!world.isRemote && entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entity;
      RayTraceResult result = rayTrace(entity.world, player, false);
      //noinspection ConstantConditions
      if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
        EnumFacing facing = result.sideHit;
        BlockPos hit = result.getBlockPos();

        hit = hit.offset(facing);
        if (!player.canPlayerEdit(hit, facing, stack)) {
          return stack;
        }

        boolean used = false;

        if (world.isAirBlock(hit)) {
          world.playSound(player, hit, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
          world.setBlockState(hit, Blocks.FIRE.getDefaultState(), 11);
          used = true;
        } else {
          TileEntity te = world.getTileEntity(hit);
          if (te instanceof TileEntityPyre) {
            TileEntityPyre pyre = (TileEntityPyre) te;
            pyre.startRitual(player.getUniqueID(), false);
            pyre.setLastPlayerId(player.getUniqueID());
            used = true;
          }
        }


        // TODO: Pyres?
        if (used) {
          stack.shrink(1);
        }
        return stack;
      }
    }

    return stack;
  }

  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BOW;
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return GeneralConfig.FireStarterTicks;
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void onUsingTick(ItemStack stack, EntityLivingBase entity, int count) {
    if (entity.world.isRemote && entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entity;
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
