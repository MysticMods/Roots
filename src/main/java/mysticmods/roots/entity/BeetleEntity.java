package mysticmods.roots.entity;


import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.capability.IPlayerShoulderCapability;
import mysticmods.roots.capability.PlayerShoulderCapability;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.network.Networking;
import mysticmods.roots.network.client.ClientBoundShoulderRidePacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

@SuppressWarnings({"NullableProblems", "Duplicates", "ConstantConditions"})
public class BeetleEntity extends TamableAnimal {

  public BeetleEntity(EntityType<? extends BeetleEntity> type, Level worldIn) {
    super(type, worldIn);
    this.xpReward = 3;
  }

  @Override
  protected void registerGoals() {
    goalSelector.addGoal(0, new FloatGoal(this));
    goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
    goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
    goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.MELON_SLICE), false));
    goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
    goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
    goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
    goalSelector.addGoal(7, new RandomLookAroundGoal(this));
  }

  @Override
  public boolean isFood(@Nonnull ItemStack stack) {
    return stack.getItem() == Items.MELON_SLICE;
  }

  @Override
  public InteractionResult mobInteract(Player player, InteractionHand hand) {
    ItemStack itemstack = player.getItemInHand(hand);
    Item item = itemstack.getItem();
    if (this.level.isClientSide) {
      boolean flag = this.isOwnedBy(player) || this.isTame() || item == Items.MELON_SEEDS && !this.isTame();
      return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
    } else {
      if (this.isTame()) {
        if (this.isOwnedBy(player) && itemstack.isEmpty() && player.isCrouching()) {
          LazyOptional<PlayerShoulderCapability> laycap = player.getCapability(Capabilities.PLAYER_SHOULDER_CAPABILITY);
          if (laycap.isPresent()) {
            IPlayerShoulderCapability cap = laycap.orElseThrow(IllegalStateException::new);
            if (!cap.isShouldered() && player.getShoulderEntityRight().isEmpty()) {
              setOrderedToSit(false);
              this.setShiftKeyDown(false);
              cap.shoulder(this);
              player.swing(InteractionHand.MAIN_HAND);
              try {
                PlayerShoulderCapability.setLeftShoulder.invokeExact(player, cap.generateShoulderNBT());
              } catch (Throwable throwable) {
                RootsAPI.LOG.error("Unable to fake player having a shoulder entity", throwable);
              }

              ClientBoundShoulderRidePacket message = new ClientBoundShoulderRidePacket(player, cap);
              Networking.send(PacketDistributor.ALL.noArg(), message);
              this.discard();
              return InteractionResult.SUCCESS;
            } else {
              player.displayClientMessage(Component.translatable("message.shoulder.occupied").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).withBold(true)), true);
            }
          }
        } else if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
          if (!player.isCreative()) {
            itemstack.shrink(1);
          }

          FoodProperties food = item.getFoodProperties();
          if (food != null) {
            this.heal((float) food.getNutrition());
            return InteractionResult.SUCCESS;
          }
        }

        InteractionResult actionresulttype = super.mobInteract(player, hand);
        if ((!actionresulttype.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) {
          this.setOrderedToSit(!this.isOrderedToSit());
          this.jumping = false;
          this.navigation.stop();
          this.setTarget(null);
          return InteractionResult.SUCCESS;
        }

        return actionresulttype;
      } else if (item == Items.MELON_SEEDS) {
        if (!player.isCreative()) {
          itemstack.shrink(1);
        }

        if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
          this.tame(player);
          this.navigation.stop();
          this.setTarget(null);
          this.setOrderedToSit(true);
          this.level.broadcastEntityEvent(this, (byte) 7);
        } else {
          this.level.broadcastEntityEvent(this, (byte) 6);
        }

        return InteractionResult.SUCCESS;
      }

      return super.mobInteract(player, hand);
    }
  }

  public static AttributeSupplier.Builder attributes() {
    return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0d).add(Attributes.MOVEMENT_SPEED, 0.15d);
  }

  @Nullable
  @Override
  public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
    return ModEntities.BEETLE.get().create(level);
  }
}
