package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageShatterBurstFX;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellShatter extends SpellBase {
  public static String spellName = "spell_shatter";
  public static SpellShatter instance = new SpellShatter(spellName);

  public SpellShatter(String name) {
    super(name, TextFormatting.GRAY, 96f / 255f, 96f / 255f, 96f / 255f, 192f / 255f, 192f / 255f, 192f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 20;

    addCost(HerbRegistry.getHerbByName("stalicripe"), 0.0625f);
    addIngredients(
        new ItemStack(Items.FLINT),
        new ItemStack(Items.STONE_PICKAXE),
        new ItemStack(Items.DYE, 1, 15),
        new ItemStack(ModItems.stalicripe),
        new ItemStack(ModItems.stalicripe)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().add(0, player.getEyeHeight(), 0),
          player.getLookVec().scale(8.0f).add(player.getPositionVector().add(0, player.getEyeHeight(), 0)));
      if (result != null) {
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
          BlockPos pos = result.getBlockPos();
          IBlockState state = player.world.getBlockState(pos);
          boolean doParticles = false;
          if (state.getBlockHardness(player.world, pos) > 0) {
            player.world.destroyBlock(pos, true);
            player.world.notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 8);
            doParticles = true;
          }
//          for (int i = 0; i < 4; i++) {
            if (result.sideHit.getAxis() != EnumFacing.Axis.Y)
              pos = result.getBlockPos().down();
            else {
              pos = pos.offset(player.getHorizontalFacing().getOpposite());
            }
            state = player.world.getBlockState(pos);
            if (state.getBlockHardness(player.world, pos) > 0) {
              player.world.destroyBlock(pos, true);
              player.world.notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 8);
           }
//          }
          if (doParticles) {
            float offX = 0.5f * (float) Math.sin(Math.toRadians(-90.0f - player.rotationYaw));
            float offZ = 0.5f * (float) Math.cos(Math.toRadians(-90.0f - player.rotationYaw));
            PacketHandler.sendToAllTracking(new MessageShatterBurstFX(player.posX + offX, player.posY + player.getEyeHeight(), player.posZ + offZ, result.hitVec.x, result.hitVec.y, result.hitVec.z), player);
          }
        } else if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
          if (result.entityHit instanceof EntityLivingBase) {
            result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(player), 5.0f);
            ((EntityLivingBase) result.entityHit).setLastAttackedEntity(player);
            ((EntityLivingBase) result.entityHit).setRevengeTarget(player);
            float offX = 0.5f * (float) Math.sin(Math.toRadians(-90.0f - player.rotationYaw));
            float offZ = 0.5f * (float) Math.cos(Math.toRadians(-90.0f - player.rotationYaw));
            PacketHandler.sendToAllTracking(new MessageShatterBurstFX(player.posX + offX, player.posY + player.getEyeHeight(), player.posZ + offZ, result.hitVec.x, result.hitVec.y, result.hitVec.z), player);
          }
        }
      }
    }
    return true;
  }

}
