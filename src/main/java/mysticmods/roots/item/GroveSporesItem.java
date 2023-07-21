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

  @Override
  public InteractionResult useOn(UseOnContext context) {
    BlockPlaceContext pContext = new BlockPlaceContext(context);
    if (!pContext.canPlace()) {
      return InteractionResult.FAIL;
    } else {
      BlockPos pPos = pContext.getClickedPos();

      if (pContext.getLevel().getFluidState(pPos).is(FluidTags.WATER)) {
        return InteractionResult.FAIL;
      }

      boolean canPlace = false;
      for (Direction dir : Direction.values()) {
        if (dir.getAxis().isVertical()) {
          continue;
        }

        if (pContext.getLevel().getBlockState(pPos.relative(dir)).is(ModBlocks.CREEPING_GROVE_MOSS.get())) {
          canPlace = true;
          break;
        }
      }

      if (!canPlace) {
        BlockPos pPos2 = pPos.relative(pContext.getClickedFace().getOpposite());
        for (Direction dir : Direction.values()) {
          FluidState stateAt = pContext.getLevel().getFluidState(pPos2.relative(dir));
          if (stateAt.is(FluidTags.WATER)) {
            canPlace = true;
            break;
          }
        }
      }

      if (!canPlace) {
        return InteractionResult.FAIL;
      }

      BlockState blockstate = ModBlocks.CREEPING_GROVE_MOSS.get().getStateForPlacement(pContext);
      blockstate = (blockstate != null && (blockstate.canSurvive(pContext.getLevel(), pContext.getClickedPos()) && pContext.getLevel().isUnobstructed(blockstate, pContext.getClickedPos(), pContext.getPlayer() == null ? CollisionContext.empty() : CollisionContext.of(pContext.getPlayer())))) ? blockstate : null;
      if (blockstate == null) {
        return InteractionResult.FAIL;
      } else if (!pContext.getLevel().setBlock(pContext.getClickedPos(), blockstate, 11)) {
        return InteractionResult.FAIL;
      } else {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();
        ItemStack itemstack = pContext.getItemInHand();
        BlockState blockstate1 = level.getBlockState(blockpos);
        if (blockstate1.is(blockstate.getBlock())) {
          blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
          if (player instanceof ServerPlayer) {
            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
          }
        }

        level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
        SoundType soundtype = blockstate1.getSoundType(level, blockpos, pContext.getPlayer());
        level.playSound(player, blockpos, blockstate1.getSoundType(level, blockpos, pContext.getPlayer()).getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        if (player == null || !player.getAbilities().instabuild) {
          itemstack.shrink(1);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
      }
    }
  }
}
