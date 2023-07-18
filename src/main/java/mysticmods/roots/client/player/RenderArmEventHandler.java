package mysticmods.mysticalworld.client.player.event;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.mysticalworld.MysticalWorld;
import mysticmods.mysticalworld.client.model.ModelHolder;
import mysticmods.mysticalworld.client.model.armor.BeetleArmorModel;
import mysticmods.mysticalworld.init.ModItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = MysticalWorld.MODID, value = Dist.CLIENT)
public class RenderArmEventHandler {
  public static BeetleArmorModel chestModel;

  public static void init(EntityRendererProvider.Context context) {
    chestModel = new BeetleArmorModel(context.bakeLayer(ModelHolder.BEETLE_ARMOR), EquipmentSlot.CHEST);
    chestModel.attackTime = 0f;
    chestModel.crouching = false;
    chestModel.swimAmount = 0;
  }

  @SubscribeEvent
  public static void renderArm(RenderArmEvent event) {
    AbstractClientPlayer player = event.getPlayer();
    ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
    if (chestStack.getItem() == ModItems.BEETLE_CHESTPLATE.get()) {
      RenderHand renderHand;

      ItemStack inHand = player.getMainHandItem();
      if (inHand.getItem() == Items.FILLED_MAP && player.getOffhandItem().isEmpty()) {
        renderHand = RenderHand.BOTH;
      } else if (event.getArm() == HumanoidArm.RIGHT) {
        renderHand = RenderHand.RIGHT;
      } else {
        renderHand = RenderHand.LEFT;
      }

      VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(event.getMultiBufferSource(), RenderType.armorCutoutNoCull(new ResourceLocation(Objects.requireNonNull(chestStack.getItem().getArmorTexture(chestStack, player, EquipmentSlot.CHEST, null)))), false, chestStack.hasFoil());
      if (event.getArm() == HumanoidArm.RIGHT) {
        chestModel.rightArmPose = HumanoidModel.ArmPose.EMPTY;
      } else {
        chestModel.leftArmPose = HumanoidModel.ArmPose.EMPTY;
      }
      chestModel.setupAnim(player, 0f, 0f, 0f, 0f, 0f);
      if (renderHand.shouldRender(HumanoidArm.RIGHT)) {
        chestModel.rightArm.render(event.getPoseStack(), ivertexbuilder, event.getPackedLight(), OverlayTexture.NO_OVERLAY);
      }
      if (renderHand.shouldRender(HumanoidArm.LEFT)) {
        chestModel.leftArm.render(event.getPoseStack(), ivertexbuilder, event.getPackedLight(), OverlayTexture.NO_OVERLAY);
      }
    }
  }

  public enum RenderHand {
    LEFT,
    RIGHT,
    BOTH;

    public boolean shouldRender(HumanoidArm hand) {
      switch (this) {
        default:
        case BOTH:
          return true;
        case LEFT:
          return hand == HumanoidArm.LEFT;
        case RIGHT:
          return hand == HumanoidArm.RIGHT;
      }
    }
  }
}
