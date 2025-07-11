package mysticmods.roots.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.blockentity.ColorHelper;
import mysticmods.roots.mixin.accessor.AccessorMixinFrustum;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.*;

public class RenderUtil {
  private static final RenderType TRANSLUCENT = RenderType.entityTranslucent(TextureAtlas.LOCATION_BLOCKS);
  private static final int[][] BOX_EDGES = {
      {0, 1}, {1, 3}, {3, 2}, {2, 0}, // bottom face: z = 0
      {4, 5}, {5, 7}, {7, 6}, {6, 4}, // top face: z = 1
      {0, 4}, {1, 5}, {3, 7}, {2, 6}  // vertical edges
  };

  public static void renderAABB(PoseStack pPoseStack, MultiBufferSource bufferSource, AABB bounds) {
    renderAABB(pPoseStack, bufferSource, bounds, null, null, null);
  }

  public static void renderAABB(PoseStack pPoseStack, MultiBufferSource bufferSource, BlockPos position) {
    renderAABB(pPoseStack, bufferSource, position, null, null, null);
  }

  public static void renderAABB(PoseStack pPoseStack, MultiBufferSource bufferSource, AABB bounds, BlockPos position) {
    renderAABB(pPoseStack, bufferSource, bounds, position, null, null);
  }

  // This doesn't require multiple AABB creations every render frame
  public static void renderAABB(PoseStack pPoseStack, MultiBufferSource bufferSource, @NotNull BlockPos position, @Nullable BlockPos colorPos, @Nullable Frustum frustum, @Nullable Camera camera) {
    double minX = position.getX();
    double minY = position.getY();
    double minZ = position.getZ();
    double maxX = minX + 1;
    double maxY = minY + 1;
    double maxZ = minZ + 1;

    if (frustum != null && !((AccessorMixinFrustum) frustum).roots_1_21$cubeInFrustum(minX, minY, minZ, maxX, maxY, maxZ)) {
     return;
    }

    pPoseStack.pushPose();

    if (camera != null) {
      Vec3 camPos = camera.getPosition();
      minX -= camPos.x;
      minY -= camPos.y;
      minZ -= camPos.z;
      maxX -= camPos.x;
      maxY -= camPos.y;
      maxZ -= camPos.z;
    }

    VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
    PoseStack.Pose pose = pPoseStack.last();
    ColorHelper.Color color = ColorHelper.color(colorPos == null ? position : colorPos);

    for (int[] edge : BOX_EDGES) {
      int a = edge[0];
      int b = edge[1];

      float x1 = ((a & 1) == 0 ? (float) minX : (float) maxX);
      float y1 = ((a & 2) == 0 ? (float) minY : (float) maxY);
      float z1 = ((a & 4) == 0 ? (float) minZ : (float) maxZ);
      float x2 = ((b & 1) == 0 ? (float) minX : (float) maxX);
      float y2 = ((b & 2) == 0 ? (float) minY : (float) maxY);
      float z2 = ((b & 4) == 0 ? (float) minZ : (float) maxZ);

      float dx = x2 - x1;
      float dy = y2 - y1;
      float dz = z2 - z1;

      consumer.addVertex(pose.pose(), x1, y1, z1).setColor(color.r(), color.g(), color.b(), color.a())
          .setNormal(pose, dx, dy, dz);
      consumer.addVertex(pose.pose(), x2, y2, z2).setColor(color.r(), color.g(), color.b(), color.a())
          .setNormal(pose, dx, dy, dz);
    }

    pPoseStack.popPose();
  }

  public static void renderAABB(PoseStack pPoseStack, MultiBufferSource bufferSource, AABB bounds, @Nullable BlockPos position, @Nullable Frustum frustum, @Nullable Camera camera) {
    if (frustum != null && !frustum.isVisible(bounds)) {
      return;
    }

    pPoseStack.pushPose();

    if (position == null) {
      position = BlockPos.containing(bounds.getCenter());
    }

    double offsetX = 0, offsetY = 0, offsetZ = 0;
    if (camera != null) {
      Vec3 camPos = camera.getPosition();
      offsetX = -camPos.x;
      offsetY = -camPos.y;
      offsetZ = -camPos.z;
    }

    double minX = bounds.minX + offsetX;
    double minY = bounds.minY + offsetY;
    double minZ = bounds.minZ + offsetZ;
    double maxX = bounds.maxX + offsetX;
    double maxY = bounds.maxY + offsetY;
    double maxZ = bounds.maxZ + offsetZ;

    VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
    PoseStack.Pose pose = pPoseStack.last();
    ColorHelper.Color color = ColorHelper.color(position);

    for (int[] edge : BOX_EDGES) {
      int a = edge[0];
      int b = edge[1];

      float x1 = ((a & 1) == 0 ? (float) minX : (float) maxX);
      float y1 = ((a & 2) == 0 ? (float) minY : (float) maxY);
      float z1 = ((a & 4) == 0 ? (float) minZ : (float) maxZ);
      float x2 = ((b & 1) == 0 ? (float) minX : (float) maxX);
      float y2 = ((b & 2) == 0 ? (float) minY : (float) maxY);
      float z2 = ((b & 4) == 0 ? (float) minZ : (float) maxZ);

      float dx = x2 - x1;
      float dy = y2 - y1;
      float dz = z2 - z1;

      consumer.addVertex(pose.pose(), x1, y1, z1).setColor(color.r(), color.g(), color.b(), color.a())
          .setNormal(pose, dx, dy, dz);
      consumer.addVertex(pose.pose(), x2, y2, z2).setColor(color.r(), color.g(), color.b(), color.a())
          .setNormal(pose, dx, dy, dz);
    }

    pPoseStack.popPose();
  }

  public static void renderItemAsIcon(ItemStack stack, PoseStack poseStack, int pX, int pY, int size,
                                      boolean transparent) {
    if (stack.isEmpty()) {
      RootsAPI.LOG.info("Attempted to render empty item stack {}", stack);
    }
    Minecraft instance = Minecraft.getInstance();
    ItemRenderer itemRenderer = instance.getItemRenderer();
    BakedModel itemBakedModel = itemRenderer.getModel(stack, null, null, 0);
    TextureManager textureManager = instance.getTextureManager();
    textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
    RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
    RenderSystem.enableBlend();
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    poseStack.pushPose();
    poseStack.translate(pX, pY, -1000.0F);
    poseStack.translate(8.0D, 8.0D, 0.0D);
    poseStack.scale(1.0F, -1.0F, 1.0F);
    poseStack.scale(size, size, size);
    //RenderSystem.applyModelViewMatrix();
    MultiBufferSource.BufferSource bufferSource = instance.renderBuffers().bufferSource();
    boolean flag = !itemBakedModel.usesBlockLight();
    if (flag) {
      Lighting.setupForFlatItems();
    }
    if (transparent) {
      itemRenderer.render(stack, ItemDisplayContext.GUI, false, poseStack, transparentBuffer(bufferSource), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, itemBakedModel);
    } else {
      itemRenderer.render(stack, ItemDisplayContext.GUI, false, poseStack, bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, itemBakedModel);
    }
    bufferSource.endBatch();
    if (flag) {
      Lighting.setupFor3DItems();
    }

    if (transparent) {
      RenderSystem.depthMask(true);
      RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }
    poseStack.popPose();
    RenderSystem.applyModelViewMatrix();
    if (transparent) {
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
    }
  }

  private static MultiBufferSource transparentBuffer(MultiBufferSource.BufferSource bufferSource) {
    return (type) -> new TintWrappedVertexConsumer(bufferSource.getBuffer(TRANSLUCENT), 1.0f, 1.0f, 1.0f, 0.25f);
  }

  public static void renderBlock(GuiGraphics guiGraphics, BlockState block, float x, float y, float z, float rotate,
                                 float scale) {
    Minecraft mc = Minecraft.getInstance();
    guiGraphics.pose().pushPose();
    guiGraphics.pose().translate(x, y, z);
    guiGraphics.pose().scale(-scale, -scale, -scale);
    guiGraphics.pose().translate(-0.5F, -0.5F, 0);
    guiGraphics.pose().mulPose(Axis.XP.rotationDegrees(-30F));
    guiGraphics.pose().translate(0.5F, 0, -0.5F);
    guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(rotate));
    guiGraphics.pose().translate(-0.5F, 0, 0.5F);

    guiGraphics.pose().pushPose();
    RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    guiGraphics.pose().translate(0, 0, -1);

    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
    MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
    mc.getBlockRenderer()
        .renderSingleBlock(block, guiGraphics.pose(), bufferSource, 15728880, OverlayTexture.NO_OVERLAY);
    bufferSource.endBatch();
    guiGraphics.pose().popPose();

    guiGraphics.pose().popPose();
  }

  private static final Set<EntityType<?>> IGNORED_ENTITIES = new HashSet<>();
  private static final Map<EntityType<?>, Entity> ENTITY_MAP = new HashMap<>();

  @Nullable
  public static Entity fetchEntity(EntityType<?> type, @Nullable Level level) {
    if (level != null && !IGNORED_ENTITIES.contains(type)) {
      Entity entity;
      if (type == EntityType.PLAYER) {
        entity = Minecraft.getInstance().player;
      } else {
        entity = ENTITY_MAP.computeIfAbsent(type, t -> {
          Entity created = t.create(level);
          if (created != null) {
            created.setYRot(0.0F);
            created.setYHeadRot(0.0F);
            created.setYBodyRot(0.0F);
            created.hasImpulse = false;
            if (created instanceof Mob mob) {
              mob.setNoAi(true);
            }
          }
          return created;
        });
      }
      return entity;
    }
    return null;
  }

  public static void renderEntity(GuiGraphics graphics, EntityType<?> type, int size) {
    Entity entity = fetchEntity(type, Minecraft.getInstance().level);
    if (entity instanceof LivingEntity living) {
      // scale down large mobs, but don't scale up small ones
      int scale = size / 2;
      float height = entity.getBbHeight();
      float width = entity.getBbWidth();
      if (height > 2.25F || width > 2.25F) {
        scale = (int) (20 / Math.max(height, width));
      }
      // catch exceptions drawing the entity to be safe, any caught exceptions blacklist the entity
      try {
        renderTheEntity(graphics, size / 2, size - 2, scale, living);
      } catch (Exception e) {
        RootsAPI.LOG.error("Error drawing entity " + BuiltInRegistries.ENTITY_TYPE.getKey(type), e);
        IGNORED_ENTITIES.add(type);
        ENTITY_MAP.remove(type);
      }
    }
  }

  //[VanillaCopy] of InventoryScreen.renderEntityInInventory, with added rotations and some other modified values
  private static void renderTheEntity(GuiGraphics graphics, int x, int y, int scale, LivingEntity entity) {
    PoseStack posestack = graphics.pose();
    Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
    Quaternionf quaternion1 = Axis.XP.rotationDegrees(20.0F);
    quaternion.mul(quaternion1);
    float f2 = entity.yBodyRot;
    float f3 = entity.getYRot();
    float f4 = entity.getXRot();
    float f5 = entity.yHeadRotO;
    float f6 = entity.yHeadRot;
    entity.yBodyRot = 0.0F;
    entity.setYRot(0.0F);
    entity.setXRot(0.0F);
    entity.yHeadRot = entity.getYRot();
    entity.yHeadRotO = entity.getYRot();
    posestack.pushPose();
    posestack.translate(x, y, 50.0D);
    applyAdditionalTransforms(entity.getType(), posestack);
    posestack.scale((float) scale, (float) scale, (float) -scale);
    posestack.mulPose(quaternion);
    posestack.mulPose(Axis.XN.rotationDegrees(35.0F));
    posestack.mulPose(Axis.YN.rotationDegrees(145.0F));
    Lighting.setupForEntityInInventory();
    EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
    quaternion1.conjugate();
    dispatcher.overrideCameraOrientation(quaternion1);
    boolean hitboxes = dispatcher.shouldRenderHitBoxes();
    dispatcher.setRenderShadow(false);
    dispatcher.setRenderHitBoxes(false);
    RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, posestack, graphics.bufferSource(), 15728880));
    graphics.flush();
    dispatcher.setRenderShadow(true);
    dispatcher.setRenderHitBoxes(hitboxes);
    posestack.popPose();
    Lighting.setupFor3DItems();
    entity.yBodyRot = f2;
    entity.setYRot(f3);
    entity.setXRot(f4);
    entity.yHeadRotO = f5;
    entity.yHeadRot = f6;
  }

  //certain entities are a pain. This exists to fix vanilla cases.
  private static void applyAdditionalTransforms(EntityType<?> entity, PoseStack stack) {
    if (entity == EntityType.GHAST) {
      stack.translate(0.0D, -12.5D, 0.0D);
      stack.scale(0.5F, 0.5F, 0.5F);
    }
    if (entity == EntityType.ENDER_DRAGON) stack.translate(0.0D, -4.0D, 0.0D);
    if (entity == EntityType.WITHER) stack.translate(0.0D, 8.0D, 0.0D);
    if (entity == EntityType.SQUID || entity == EntityType.GLOW_SQUID) stack.translate(0.0D, -19.0D, 0.0D);
    if (entity == EntityType.ELDER_GUARDIAN) stack.scale(0.6F, 0.6F, 0.6F);
  }

  public static List<Component> getMobTooltip(EntityType<?> type) {
    List<Component> components = new ArrayList<>();
    components.add(type.getDescription());
    if (Minecraft.getInstance().options.advancedItemTooltips) {
      components.add(Component.literal(BuiltInRegistries.ENTITY_TYPE.getKey(type).toString())
          .withStyle(ChatFormatting.DARK_GRAY));
    }
    return components;
  }
}
