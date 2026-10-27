package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import mysticmods.roots.api.block.ExtraSkullTypes;
import mysticmods.roots.block.ShapedWallSkullBlock;
import mysticmods.roots.blockentity.ExtraSkullBlockEntity;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.mixin.accessor.AccessorMixinSkullBlockRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class ExtraSkullBlockRenderer implements BlockEntityRenderer<ExtraSkullBlockEntity> {
  private static SkullBlockRenderer skullBlockRenderer = null;
  private static final RenderType ENDERMAN_EYES = RenderType.create("eyes",
      DefaultVertexFormat.NEW_ENTITY,
      VertexFormat.Mode.QUADS,
      1536,
      false,
      true,
      RenderType.CompositeState.builder()
          .setShaderState(RenderType.RENDERTYPE_EYES_SHADER)
          .setTextureState(new RenderStateShard.TextureStateShard(ResourceLocation.withDefaultNamespace("textures/entity/enderman/enderman_eyes.png"), false, false))
          .setTransparencyState(RenderType.ADDITIVE_TRANSPARENCY)
          .setWriteMaskState(RenderType.COLOR_WRITE)
          .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
          .createCompositeState(false)
  );
  private static final ResourceLocation STRAY_CLOTHES_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/skeleton/stray_overlay.png");
  private static final ResourceLocation BOGGED_CLOTHES_LAYER_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/skeleton/bogged_overlay.png");
  private static final ResourceLocation DROWNED_OUTER_LAYER_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/zombie/drowned_outer_layer.png");
  private static SkullModelBase strayOuterLay;
  private static SkullModelBase boggedOuterLayer;
  private static SkullModelBase drownedOuterLayer;

  public ExtraSkullBlockRenderer(BlockEntityRendererProvider.Context context) {
    strayOuterLay = new SkullModel(context.bakeLayer(ModelHolder.STRAY_HEAD_OUTER));
    boggedOuterLayer = new SkullModel(context.bakeLayer(ModelHolder.BOGGED_HEAD_OUTER));
    drownedOuterLayer = new SkullModel(context.bakeLayer(ModelHolder.DROWNED_HEAD_OUTER));
  }

  private static SkullBlockRenderer getSkullBlockRenderer() {
    if (skullBlockRenderer == null) {
      skullBlockRenderer = (SkullBlockRenderer) Minecraft.getInstance().getBlockEntityRenderDispatcher()
          .getRenderer(new SkullBlockEntity(BlockPos.ZERO, Blocks.SKELETON_SKULL.defaultBlockState()));
    }

    return skullBlockRenderer;
  }

  public static SkullModelBase getModel(SkullBlock.Type type) {
    return ((AccessorMixinSkullBlockRenderer) getSkullBlockRenderer()).roots$getModelByType()
        .get(type);
  }

  public static SkullModelBase getExtraModel(SkullBlock.Type type) {
    return switch (type) {
      case ExtraSkullTypes.STRAY -> strayOuterLay;
      case ExtraSkullTypes.DROWNED -> drownedOuterLayer;
      case ExtraSkullTypes.BOGGED -> boggedOuterLayer;
      default -> null;
    };
  }

  public void render(ExtraSkullBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    float f = blockEntity.getAnimation(partialTick);
    BlockState blockstate = blockEntity.getBlockState();
    boolean flag = blockstate.getBlock() instanceof ShapedWallSkullBlock;
    Direction direction = flag ? blockstate.getValue(WallSkullBlock.FACING) : null;
    int i = flag ? RotationSegment.convertToSegment(direction.getOpposite()) : blockstate.getValue(SkullBlock.ROTATION);
    float f1 = RotationSegment.convertToDegrees(i);
    SkullBlock.Type skullblock$type = ((AbstractSkullBlock) blockstate.getBlock()).getType();
    SkullModelBase skullmodelbase = getModel(skullblock$type);
    RenderType rendertype = SkullBlockRenderer.getRenderType(skullblock$type, null);
    if (skullblock$type == ExtraSkullTypes.ENDERMAN && blockEntity.isAnimating()) {
      RandomSource random = blockEntity.getLevel().getRandom();
      double jitterX = random.nextGaussian() * 0.02;
      double jitterZ = random.nextGaussian() * 0.02;

      if (direction != null) {
        if (direction.getAxis() == Direction.Axis.X) {
          jitterX = Math.abs(jitterX) * direction.getStepX();
        } else {
          jitterZ = Math.abs(jitterZ) * direction.getStepZ();
        }
      }

      poseStack.pushPose();
      poseStack.translate(jitterX, 0.0, jitterZ);
    }

    renderSkull(direction, f1, f, poseStack, bufferSource, packedLight, skullblock$type, skullmodelbase, getExtraModel(skullblock$type), rendertype);
    if (skullblock$type == ExtraSkullTypes.ENDERMAN && blockEntity.isAnimating()) {
      poseStack.popPose();
    }
  }

  public static void renderSkull(
      @Nullable Direction direction,
      float yRot,
      float mouthAnimation,
      PoseStack poseStack,
      MultiBufferSource bufferSource,
      int packedLight,
      SkullBlock.Type type,
      SkullModelBase model,
      SkullModelBase extra,
      RenderType renderType
  ) {
    poseStack.pushPose();
    if (direction == null) {
      poseStack.translate(0.5F, 0.0F, 0.5F);
    } else {
      poseStack.translate(0.5F - (float) direction.getStepX() * 0.25F, 0.25F, 0.5F - (float) direction.getStepZ() * 0.25F);
    }

    poseStack.scale(-1.0F, -1.0F, 1.0F);

    VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
    model.setupAnim(mouthAnimation, yRot, 0.0F);
    model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
    if (type == ExtraSkullTypes.ENDERMAN) {
      model.renderToBuffer(poseStack, bufferSource.getBuffer(ENDERMAN_EYES), 15728640, OverlayTexture.NO_OVERLAY);
    } else if (type == ExtraSkullTypes.STRAY) {
      extra.setupAnim(mouthAnimation, yRot, 0.0f);
      extra.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCullZOffset(STRAY_CLOTHES_LOCATION)), packedLight, OverlayTexture.NO_OVERLAY);
    } else if (type == ExtraSkullTypes.BOGGED) {
      extra.setupAnim(mouthAnimation, yRot, 0.0f);
      extra.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCullZOffset(BOGGED_CLOTHES_LAYER_LOCATION)), packedLight, OverlayTexture.NO_OVERLAY);
    } else if (type == ExtraSkullTypes.DROWNED) {
      extra.setupAnim(mouthAnimation, yRot, 0.0f);
      extra.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCullZOffset(DROWNED_OUTER_LAYER_LOCATION)), packedLight, OverlayTexture.NO_OVERLAY);
    }

    poseStack.popPose();
  }

  @Override
  public AABB getRenderBoundingBox(ExtraSkullBlockEntity blockEntity) {
    SkullBlock.Type type = ((AbstractSkullBlock) blockEntity.getBlockState().getBlock()).getType();
    if (type == SkullBlock.Types.DRAGON) {
      net.minecraft.core.BlockPos pos = blockEntity.getBlockPos();
      return new net.minecraft.world.phys.AABB(pos.getX() - .75, pos.getY() - .35, pos.getZ() - .75, pos.getX() + 1.75, pos.getY() + 1.0, pos.getZ() + 1.75);
    } else {
      return new AABB(blockEntity.getBlockPos());
    }
  }
}
