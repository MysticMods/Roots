package mysticmods.roots.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.math.MatrixUtil;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.mixin.accessor.AccessorMixinItemRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.*;

public class RenderUtil {
  private static final RenderType TRANSLUCENT = RenderType.entityTranslucent(TextureAtlas.LOCATION_BLOCKS);

  public static void renderItemAsIcon(ItemStack stack, PoseStack poseStack, int pX, int pY, int size, boolean transparent) {
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

  public static void renderBlock(GuiGraphics guiGraphics, BlockState block, float x, float y, float z, float rotate, float scale) {
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

/*  public static void renderItemEntity(GuiGraphics graphics, ItemStack stack, @Nullable Level level, float bobOffset) {
    PoseStack posestack = graphics.pose();
    posestack.pushPose();
    posestack.translate(16.0D, 32.0D, 50.0D);
    posestack.scale(50.0F, 50.0F, -50.0F);
    Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
    Quaternionf quaternion1 = Axis.XP.rotationDegrees(20.0F);
    quaternion.mul(quaternion1);
    posestack.mulPose(quaternion);
    posestack.mulPose(Axis.XN.rotationDegrees(35.0F));
    posestack.mulPose(Axis.YN.rotationDegrees(145.0F));
    Lighting.setupForEntityInInventory();
    quaternion1.conjugate();
    ItemEntity item = (ItemEntity) fetchEntity(EntityType.ITEM, level);
    Objects.requireNonNull(item).setItem(stack);
    RenderSystem.runAsFancy(() -> render(item, Minecraft.getInstance().getTimer()
        .getGameTimeDeltaTicks(), posestack, graphics.bufferSource(), bobOffset));
    graphics.flush();
    posestack.popPose();
    Lighting.setupFor3DItems();
  }

  //[VanillaCopy] of ItemEntityRenderer.render. I have to add my own bob offset and ticker since using the vanilla method has issues
  private static void render(ItemEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, float bobOffset) {
    stack.pushPose();
    ItemStack itemstack = entity.getItem();
    BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer()
        .getModel(itemstack, entity.level(), null, entity.getId());
    float f1 = Mth.sin((Objects.requireNonNull(Minecraft.getInstance().level)
        .getGameTime() + partialTicks) / 10.0F + bobOffset) * 0.1F + 0.1F;
    float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
    stack.translate(0.0D, f1 + 0.25F * f2, 0.0D);
    float f3 = getSpin(partialTicks, bobOffset);
    stack.mulPose(Axis.YP.rotation(f3));

    stack.pushPose();
    renderItem(itemstack, ItemDisplayContext.GROUND, false, stack, buffer, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel, Minecraft.getInstance().getItemRenderer());
    stack.popPose();


    stack.popPose();
  }*/

/*  private static float getSpin(float partialTicks, float bobOffset) {
    return (Objects.requireNonNull(Minecraft.getInstance().level).getGameTime() + partialTicks) / 20.0F + bobOffset;
  }*/

  public static List<Component> getMobTooltip(EntityType<?> type) {
    List<Component> components = new ArrayList<>();
    components.add(type.getDescription());
    if (Minecraft.getInstance().options.advancedItemTooltips) {
      components.add(Component.literal(BuiltInRegistries.ENTITY_TYPE.getKey(type).toString())
          .withStyle(ChatFormatting.DARK_GRAY));
    }
    return components;
  }

  // Drop-in replacement for ItemRenderer.render
  public static void renderItemCrumble(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel bakedModel, ItemRenderer renderer, int progress) {
    if (!itemStack.isEmpty()) {
      poseStack.pushPose();

      boolean flag = displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.GROUND || displayContext == ItemDisplayContext.FIXED;
      if (flag) {
        if (itemStack.is(Items.TRIDENT)) {
          bakedModel = ((AccessorMixinItemRenderer) renderer).RootsGetItemModelShaper().getModelManager()
              .getModel(((AccessorMixinItemRenderer) renderer).RootsGetTridentModel());
        } else if (itemStack.is(Items.SPYGLASS)) {
          bakedModel = ((AccessorMixinItemRenderer) renderer).RootsGetItemModelShaper().getModelManager()
              .getModel(((AccessorMixinItemRenderer) renderer).RootsGetSpyglassModel());
        }
      }

      bakedModel = ClientHooks.handleCameraTransforms(poseStack, bakedModel, displayContext, leftHand);
      poseStack.translate(-0.5F, -0.5F, -0.5F);
      if (!bakedModel.isCustomRenderer() && (!itemStack.is(Items.TRIDENT) || flag)) {
        boolean flag1;
        if (displayContext != ItemDisplayContext.GUI && !displayContext.firstPerson() && itemStack.getItem() instanceof BlockItem blockitem) {
          Block block = blockitem.getBlock();
          flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
        } else {
          flag1 = true;
        }

        for (var model : bakedModel.getRenderPasses(itemStack, flag1)) {
          for (var rendertype : model.getRenderTypes(itemStack, flag1)) {
            VertexConsumer vertexconsumer;
            if (hasAnimatedTexture(itemStack) && itemStack.hasFoil()) {
              PoseStack.Pose pose = poseStack.last().copy();
              if (displayContext == ItemDisplayContext.GUI) {
                MatrixUtil.mulComponentWise(pose.pose(), 0.5F);
              } else if (displayContext.firstPerson()) {
                MatrixUtil.mulComponentWise(pose.pose(), 0.75F);
              }

/*              vertexconsumer = ItemRenderer.getCompassFoilBuffer(bufferSource, rendertype, pose);*/
/*            } else if (flag1) {
              vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource, rendertype, true, itemStack.hasFoil());
            } else {
              vertexconsumer = ItemRenderer.getFoilBuffer(bufferSource, rendertype, true, itemStack.hasFoil());*/
            }

            VertexConsumer crumble = new SheetedDecalTextureGenerator(
                Minecraft.getInstance().renderBuffers().crumblingBufferSource().getBuffer(ModelBakery.DESTROY_TYPES.get(progress)), poseStack.last(), 1.0F
            );

            //vertexconsumer = VertexMultiConsumer.create(vertexconsumer, crumble);

            RenderSystem.applyModelViewMatrix();
            renderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, poseStack, crumble);
          }
        }
      } else {
        IClientItemExtensions.of(itemStack).getCustomRenderer()
            .renderByItem(itemStack, displayContext, poseStack, bufferSource, combinedLight, combinedOverlay);
      }

      poseStack.popPose();
    }
  }

  private static boolean hasAnimatedTexture(ItemStack stack) {
    return stack.is(ItemTags.COMPASSES) || stack.is(Items.CLOCK);
  }
}
