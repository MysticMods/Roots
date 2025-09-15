package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.grove.GrovePowerGenerator;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.blockentity.GroveStoneBlockEntity;
import mysticmods.roots.client.ColorHelper;
import mysticmods.roots.client.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroveStoneBlockEntityRenderer extends BoundedBlockEntityRenderer<GroveStoneBlockEntity> {
  public GroveStoneBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public void render(GroveStoneBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    super.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);
    BlockState state = pBlockEntity.getBlockState();
    BlockPos origin = pBlockEntity.getBlockPos();
    if (!state.is(RootsTags.Blocks.GROVE_STONE_PRIMAL) && state.hasProperty(GroveStoneBlock.RANK) && state.hasProperty(GroveStoneBlock.ACTIVE) && state.getValue(GroveStoneBlock.ACTIVE) && state.getValue(GroveStoneBlock.RANK) > 0) {
      if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
        BlockPos pPos = pBlockEntity.getBlockPos();
        List<BlockPos> generatorPositions = pBlockEntity.getValidPositions(pPos);
        List<GrovePowerGenerator.GenerationEntry> entries = pBlockEntity.getGenerationEntries();
        if (generatorPositions.isEmpty() || entries.isEmpty()) {
          return;
        }

        Set<BlockPos> marked = new HashSet<>();

        for (BlockPos pos : generatorPositions) {
          BlockState stateAt = pBlockEntity.getLevel().getBlockState(pos);
          if (stateAt.isAir()) {
            continue;
          }

          if (stateAt.getBlockHolder().getData(DataMaps.GROVE_POWER_GENERATORS) == null) {
            continue;
          }

          if (marked.contains(pos)) {
            continue;
          }

          for (GrovePowerGenerator.GenerationEntry entry : entries) {
            BlockState current = pBlockEntity.getLevel().getBlockState(pos);
            if (!current.is(entry.tag())) {
              continue;
            }
            GrovePowerGenerator.Symmetry sym = entry.symmetry();
            Pair<Boolean, BlockPos> match = sym.matchesWithPair(pBlockEntity.getLevel(), entry.tag(), pos, pPos);
            marked.add(pos);
            BlockPos relativePos = pos.subtract(origin);
            if (match.getFirst()) {
              RenderUtil.renderAABB(pPoseStack, pBufferSource, relativePos, ColorHelper.GREEN, null, null);
            } else {
              RenderUtil.renderAABB(pPoseStack, pBufferSource, relativePos, ColorHelper.RED, null, null);
            }
            if (match.getSecond() != null) {
              marked.add(match.getSecond());
              relativePos = match.getSecond().subtract(origin);
              if (match.getFirst()) {
                RenderUtil.renderAABB(pPoseStack, pBufferSource, relativePos, ColorHelper.GREEN, null, null);
              } else {
                RenderUtil.renderAABB(pPoseStack, pBufferSource, relativePos, ColorHelper.RED, null, null);
              }
            }
          }
        }
      }
    }
  }
}
