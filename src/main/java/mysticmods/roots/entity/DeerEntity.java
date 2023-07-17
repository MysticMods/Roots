package mysticmods.roots.entity;

import mysticmods.roots.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DeerEntity extends Animal {

  public static final EntityDataAccessor<Boolean> hasHorns = SynchedEntityData.defineId(DeerEntity.class, EntityDataSerializers.BOOLEAN);

  public DeerEntity(EntityType<? extends DeerEntity> type, Level world) {
    super(type, world);
    this.xpReward = 3;
  }

  @Nullable
  @Override
  public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pEntity) {
    return ModEntities.DEER.get().create(pLevel);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    getEntityData().define(hasHorns, random.nextBoolean());
  }

  @Override
  protected void registerGoals() {
    goalSelector.addGoal(0, new FloatGoal(this));
    goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
    goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.WHEAT), false));
    goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
    goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
    goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
    goalSelector.addGoal(7, new RandomLookAroundGoal(this));
  }

  @Override
  public void tick() {
    super.tick();
    this.setYRot(this.yHeadRot);
  }

  public static AttributeSupplier.Builder attributes() {
    return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 15.0d).add(Attributes.MOVEMENT_SPEED, 0.2d);
  }

  @Override
  public float getStandingEyeHeight(Pose pose, EntityDimensions size) {
    return this.isBaby() ? this.getBbHeight() : 1.3F;
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
/*    if (random.nextInt(15) == 0) {
      return ModSounds.DEER_AMBIENT.get();
    }*/
    return null;
  }

  @Override
  public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    getEntityData().set(hasHorns, compound.getBoolean("hasHorns"));
  }

  @Override
  public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("hasHorns", getEntityData().get(hasHorns));
  }
}