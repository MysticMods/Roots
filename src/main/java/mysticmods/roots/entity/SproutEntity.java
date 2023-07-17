package mysticmods.roots.entity;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class SproutEntity extends Animal {
  public static final EntityDataAccessor<Integer> variant = SynchedEntityData.defineId(SproutEntity.class, EntityDataSerializers.INT);

  public SproutEntity(EntityType<? extends SproutEntity> type, Level world) {
    super(type, world);
    this.xpReward = 3;
  }

  @Override
  protected float getSoundVolume() {
    return 0.2f;
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    if (random.nextInt(45) == 0) {
      return ModSounds.SPROUT_AMBIENT.get();
    }

    return null;
  }

  @Override
  protected void registerGoals() {
    goalSelector.addGoal(0, new FloatGoal(this));
    goalSelector.addGoal(1, new PanicGoal(this, 1.5));
    goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(RootsTags.Items.AUBERGINE_CROP), false));
    goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
    goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
    goalSelector.addGoal(7, new RandomLookAroundGoal(this));
  }

  public static AttributeSupplier.Builder attributes() {
    return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0d).add(Attributes.MOVEMENT_SPEED, 0.2d);
  }

  @Override
  public boolean isFood(ItemStack stack) {
    return stack.is(RootsTags.Items.AUBERGINE_CROP);
  }


  @Override
  public float getStandingEyeHeight(Pose pose, EntityDimensions size) {
    return isBaby() ? getBbHeight() : 1.3F;
  }

  @org.jetbrains.annotations.Nullable
  @Override
  public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
    return (AgeableMob) getType().create(p_146743_);
  }
}
