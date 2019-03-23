package epicsquid.roots.item;

import java.util.Random;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRunicShears extends ItemBase {

  private Random random;

  public ItemRunicShears(@Nonnull String name) {
    super(name);
    setMaxDamage(80);
    setMaxStackSize(1);
    setHasSubtypes(false);
    random = new Random();
  }

  @Override
  @Nonnull
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    Block block = world.getBlockState(pos).getBlock();

    RunicShearRecipe recipe = ModRecipes.getRunicShearRecipe(block);

    if (recipe != null) {
      if (!world.isRemote) {

        if (block instanceof BlockCrops) {
          if (((BlockCrops) block).isMaxAge(world.getBlockState(pos))) {
            world.setBlockState(pos, ((BlockCrops) block).withAge(0));
          } else {
            return EnumActionResult.SUCCESS;
          }
        } else {
          world.setBlockState(pos, recipe.getReplacementBlock().getDefaultState());
        }
        world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), recipe.getDrop()));
        player.getHeldItem(hand).damageItem(1, player);
      } else {
        for (int i = 0; i < 50; i++) {
          ClientProxy.particleRenderer.spawnParticle(world, Util.getLowercaseClassName(ParticleGlitter.class), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1),
              random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), 120, 0.855 + random.nextDouble() * 0.05, 0.710, 0.943 - random.nextDouble() * 0.05, 1, random.nextDouble() + 0.5, random.nextDouble() * 2);
        }
      }
      world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
    }
    return EnumActionResult.SUCCESS;
  }
}
