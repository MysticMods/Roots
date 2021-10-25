package epicsquid.mysticallib.util;


import epicsquid.mysticallib.item.tool.IBlacklistingTool;
import epicsquid.mysticallib.item.tool.ISizedTool;
import epicsquid.mysticallib.item.tool.ItemPloughBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// Concept based partially on ToolFunctions.java by astradamus from MIT Licensed Practical Tools
// https://github.com/astradamus/PracticalTools/blob/master/src/main/java/com/alexanderstrada/practicaltools/ToolFunctions.java
public class BreakUtil {
  public static Set<BlockPos> nearbyBlocks(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
    final World world = player.world;
    final BlockState state = world.getBlockState(pos);
    final Item tool = itemstack.getItem();
    final Block block = state.getBlock();
    final String harvestTool = block.getHarvestTool(state);
    final Set<String> toolClasses = tool.getToolClasses(itemstack);
    final boolean plough = tool instanceof ItemPloughBase;
    if (!(tool instanceof ISizedTool)) {
      return Collections.emptySet();
    }
    if (state.getBlockHardness(world, pos) == 0.0f) {
      return Collections.emptySet();
    }
    if (harvestTool != null && !toolClasses.contains(harvestTool)) {
      return Collections.emptySet();
    }
    if (player.isSneaking()) {
      return Collections.emptySet();
    }

    IBlacklistingTool blacklist = (IBlacklistingTool) tool;

    final ISizedTool sized = (ISizedTool) tool;

    float hardness = state.getPlayerRelativeBlockHardness(player, world, pos);
    if (hardness == 0) {
      return Collections.emptySet();
    }

    RayTraceResult ray = BreakUtil.rayTrace(world, player);
    if (ray == null || ray.sideHit == null) {
      return Collections.emptySet();
    }

    int width = sized.getWidth(itemstack);
    if (width % 2 == 0) {
      width /= 2;
    } else {
      width = (width - 1) / 2;
    }

    Direction facing = ray.sideHit;
    Set<BlockPos> result = new HashSet<>();

    for (int x = -width; x < width + 1; x++) {
      for (int z = -width; z < width + 1; z++) {
        if (x == 0 && z == 0) {
          continue;
        }

        BlockPos potential;

        switch (facing.getAxis()) {
          case X:
            potential = pos.add(0, x, z);
            break;
          case Y:
            potential = pos.add(x, 0, z);
            break;
          case Z:
            potential = pos.add(x, z, 0);
            break;
          default:
            continue;
        }

        final BlockState potentialState = world.getBlockState(potential);
        final Material material = potentialState.getMaterial();
        final Block potentialBlock = potentialState.getBlock();
        if (blacklist.isBlacklisted(potentialBlock)) {
          continue;
        }
        if (plough && ItemPloughBase.EFFECTIVE_BLOCKS.contains(potentialBlock)) {
          result.add(potential);
          continue;
        }
        if (!ForgeHooks.canToolHarvestBlock(world, potential, itemstack) || !ForgeHooks.canHarvestBlock(potentialState.getBlock(), player, world, potential)) {
          continue;
        }
        final float destroySpeed = tool.getDestroySpeed(itemstack, potentialState);
        if (destroySpeed == 1.0f) {
          continue;
        }

        result.add(potential);
      }
    }

    return result;
  }

  public static boolean harvestBlock(World world, BlockPos pos, PlayerEntity player) {
    if (world.isAirBlock(pos)) {
      return false;
    }

    final ItemStack stack = player.getHeldItemMainhand();
    final BlockState state = world.getBlockState(pos);
    final Block block = state.getBlock();
    if (!ForgeHooks.canToolHarvestBlock(world, pos, stack) || !ForgeHooks.canHarvestBlock(state.getBlock(), player, world, pos)) {
      return false;
    }

    final ServerPlayerEntity playerMP = (player instanceof ServerPlayerEntity) ? (ServerPlayerEntity) player : null;
    final boolean creative = player.isCreative();
    final GameType type = (playerMP == null) ? null : playerMP.interactionManager.getGameType();

    int xp = 0;
    if (playerMP != null) {
      xp = ForgeHooks.onBlockBreakEvent(world, type, playerMP, pos);
      if (xp == -1) {
        return false;
      }
    }
    if (!world.isRemote) {
      if (playerMP == null) {
        return false;
      }
      final TileEntity te = world.getTileEntity(pos);
      if (block.removedByPlayer(state, world, pos, player, !creative)) {
        block.onPlayerDestroy(world, pos, state);
        if (!creative) {
          block.harvestBlock(world, player, pos, state, te, stack);
          if (xp > 0) {
            block.dropXpOnBlockBreak(world, pos, xp);
          }
        }
      }
      playerMP.connection.sendPacket(new SChangeBlockPacket(world, pos));
    } else {
      clientSideRemoval(state, world, pos, player);
    }
    return true;
  }

  @SideOnly(Side.CLIENT)
  private static void clientSideRemoval(BlockState state, World world, BlockPos pos, PlayerEntity player) {
    final Block block = state.getBlock();
    if (block.removedByPlayer(state, world, pos, player, !player.isCreative())) {
      block.onPlayerDestroy(world, pos, state);
    }
    final Minecraft mc = Minecraft.getMinecraft();
    final Direction side = mc.objectMouseOver.sideHit;
    final CPlayerDiggingPacket packet = new CPlayerDiggingPacket(Action.START_DESTROY_BLOCK, pos, side);
    final ClientPlayNetHandler connection = mc.getConnection();
    if (connection != null) {
      connection.sendPacket(packet);
    }
  }

  public static RayTraceResult rayTrace(World world, PlayerEntity player) {
    Vec3d eyes = player.getPositionEyes(1f);
    float yawCos = MathHelper.cos(-player.rotationYaw * (float) (Math.PI / 180F) - (float) Math.PI);
    float yawSin = MathHelper.sin(-player.rotationYaw * (float) (Math.PI / 180F) - (float) Math.PI);
    float pitchCos = -MathHelper.cos(-player.rotationPitch * (float) (Math.PI / 180F));
    float pitchSin = MathHelper.sin(-player.rotationPitch * (float) (Math.PI / 180F));

    float f1 = yawSin * pitchCos;
    float f2 = yawCos * pitchCos;

    double reach = player.getEntityAttribute(PlayerEntity.REACH_DISTANCE).getAttributeValue();
    Vec3d reachVec = eyes.add((double) f1 * reach, (double) pitchSin * reach, (double) f2 * reach);
    return world.rayTraceBlocks(eyes, reachVec, false, true, true);
  }
}

