package mysticmods.roots.item;

import mysticmods.roots.init.ModBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class GroveSporesItem extends Item {
  public GroveSporesItem(Properties pProperties) {
    super(pProperties);
  }

  public static boolean canPlace (Level pLevel, BlockPos pPos, Direction pDirection) {
    if (!pLevel.isEmptyBlock(pPos)) {
      return false;
    }

    BlockState state = pLevel.getBlockState(pPos.below());
    if (!state.isFaceSturdy(pLevel, pPos, pDirection)) {
      return false;
    }

    boolean canPlace = false;
    if (pLevel.getFluidState(pPos).is(FluidTags.WATER)) {
      return false;
    }
    for (Direction dir : Direction.values()) {
      if (dir.getAxis().isVertical()) {
        continue;
      }

      if (pLevel.getBlockState(pPos.relative(dir)).is(ModBlocks.CREEPING_GROVE_MOSS.get())) {
        canPlace = true;
        break;
      }
    }

    if (!canPlace) {
      BlockPos pPos2 = pPos.relative(pDirection.getOpposite());
      for (Direction dir : Direction.values()) {
        FluidState stateAt = pLevel.getFluidState(pPos2.relative(dir));
        if (stateAt.is(FluidTags.WATER)) {
          canPlace = true;
          break;
        }
      }
    }

    return canPlace;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    BlockPlaceContext pContext = new BlockPlaceContext(context);
    if (!pContext.canPlace()) {
      return InteractionResult.FAIL;
    } else {
      BlockPos pPos = pContext.getClickedPos();
      Level pLevel = pContext.getLevel();
      Direction pDirection = pContext.getClickedFace();
      Player player = pContext.getPlayer();

      boolean canPlace = canPlace(pLevel, pPos, pDirection);
      if (!canPlace) {
        return InteractionResult.FAIL;
      }

      BlockState blockstate = ModBlocks.CREEPING_GROVE_MOSS.get().getStateForPlacement(pContext);
      blockstate = (blockstate != null && (blockstate.canSurvive(pLevel, pPos) && pLevel.isUnobstructed(blockstate, pPos, player == null ? CollisionContext.empty() : CollisionContext.of(player)))) ? blockstate : null;
      if (blockstate == null) {
        return InteractionResult.FAIL;
      } else if (!pLevel.setBlock(pPos, blockstate, 11)) {
        return InteractionResult.FAIL;
      } else {
        ItemStack itemstack = pContext.getItemInHand();
        BlockState blockstate1 = pLevel.getBlockState(pPos);
        if (blockstate1.is(blockstate.getBlock())) {
          blockstate1.getBlock().setPlacedBy(pLevel, pPos, blockstate1, player, itemstack);
          if (player instanceof ServerPlayer) {
            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pPos, itemstack);
          }
        }

        pLevel.gameEvent(GameEvent.BLOCK_PLACE, pPos, GameEvent.Context.of(player, blockstate1));
        SoundType soundtype = blockstate1.getSoundType(pLevel, pPos, pContext.getPlayer());
        pLevel.playSound(player, pPos, blockstate1.getSoundType(pLevel, pPos, pContext.getPlayer()).getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        if (player == null || !player.getAbilities().instabuild) {
          itemstack.shrink(1);
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
      }
    }
  }
}
