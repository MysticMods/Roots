/*
package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.util.SimpleNoise;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AmplifierBlockEntityRenderer implements BlockEntityRenderer<AmplifierBlockEntity> {
  private static final ResourceLocation TEXTURE = RootsAPI.rl("textures/entity/amplifier.png");

  private static final String AMPLIFIER_CENTER = "amplifier_center";
  private static final String AMPLIFIER_OUTER = "amplifier_outer";
  private final ModelPart amplifierCenter;
  private final ModelPart amplifierOuter;

  public AmplifierBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    ModelPart amplifierModel = context.bakeLayer(ModelHolder.AMPLIFIER_CENTER);
    ModelPart amplifierModel2 = context.bakeLayer(ModelHolder.AMPLIFIER_OUTER);

    this.amplifierCenter = amplifierModel.getChild(AMPLIFIER_CENTER);
    this.amplifierOuter = amplifierModel2.getChild(AMPLIFIER_OUTER);
  }

  public static LayerDefinition createOuterLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition part = mesh.getRoot();
    PartDefinition outer = part.addOrReplaceChild(AMPLIFIER_OUTER, CubeListBuilder.create().texOffs(0, 0)
        .addBox(-2.5F, 0.0F, -2.5f, 5.0f, 5.0f, 5.0f), PartPose.ZERO);
    return LayerDefinition.create(mesh, 32, 32);
  }

  public static LayerDefinition createInnerLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition part = mesh.getRoot();
    PartDefinition inner = part.addOrReplaceChild(AMPLIFIER_CENTER, CubeListBuilder.create().texOffs(0, 10)
        .addBox(-1.5F, 1f, -1.5F, 3.0f, 3.0f, 3.0f), PartPose.ZERO);
    return LayerDefinition.create(mesh, 32, 32);
  }

  @Override
  public void render(AmplifierBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    float ticks = pBlockEntity.ticks + pPartialTick;
    float time = ticks * 0.02f;

    float rotationY = pBlockEntity.rotationAccumulator;

    float swayNoise = SimpleNoise.noise(time * 0.15f);
    float swayAmplitude = swayNoise * 12f;

    float rotationX = (float)(Math.sin(ticks * 0.06) * swayAmplitude);
    float rotationZ = (float)(Math.cos(ticks * 0.06) * swayAmplitude);

    float yOffset = (float)(Math.sin(ticks * 0.015) * 0.05f);

    float pulse = (float)(Math.sin(ticks * 0.02) * 0.05f + 1.1f);

    float coreNoise = SimpleNoise.noise(time * 0.3f + 100f);
    float coreRotation = ticks * 0.1f * (coreNoise * 0.2f + 1.0f);

    pPoseStack.pushPose();
    pPoseStack.translate(0.5, 0.9 + yOffset, 0.5);
    pPoseStack.scale(0.7f * pulse, 0.7f * pulse, 0.7f * pulse);

    // Outer rotation
    pPoseStack.mulPose(Axis.YP.rotationDegrees(rotationY));
    pPoseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
    pPoseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ));

    pPoseStack.pushPose();
    pPoseStack.mulPose(Axis.YP.rotationDegrees(coreRotation));
    VertexConsumer center = pBufferSource.getBuffer(RenderType.entitySolid(TEXTURE));
    this.amplifierCenter.render(pPoseStack, center, pPackedLight, pPackedOverlay);
    pPoseStack.popPose();

    VertexConsumer outer = pBufferSource.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE));
    this.amplifierOuter.render(pPoseStack, outer, LightTexture.pack(15, 15), pPackedOverlay);

    pPoseStack.popPose();
  }
}
*/
