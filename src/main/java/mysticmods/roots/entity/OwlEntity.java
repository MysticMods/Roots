package mysticmods.roots.entity;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("NullableProblems")
public class OwlEntity extends TamableAnimal implements FlyingAnimal {
  public float flap;
  public float flapSpeed;
  public float oFlapSpeed;
  public float oFlap;
  public float flapping = 1.0F;
  private float nextFlap = 1.0F;

  public OwlEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
    super(type, worldIn);
    this.moveControl = new FlyingMoveControl(this, 15, false);
  }

  @Override
  protected void registerGoals() {
    goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
    goalSelector.addGoal(0, new FloatGoal(this));
    goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
    goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1D));
  }

  public static AttributeSupplier.Builder attributes() {
    return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0d).add(Attributes.MOVEMENT_SPEED, 0.2d).add(Attributes.FLYING_SPEED, 0.55d);
  }

  @Override
  protected PathNavigation createNavigation(Level worldIn) {
    FlyingPathNavigation pathnavigateflying = new FlyingPathNavigation(this, worldIn);
    pathnavigateflying.setCanOpenDoors(false);
    //pathnavigateflying.setCanFloat(true);
    pathnavigateflying.setCanPassDoors(true);
    return pathnavigateflying;
  }

  @Override
  public float getEyeHeight(Pose pose) {
    return this.getBbHeight() * 0.6F;
  }

  @Override
  public void tick() {
    super.tick();
    this.calculateFlapping();
  }

  private void calculateFlapping() {
    this.oFlap = this.flap;
    this.oFlapSpeed = this.flapSpeed;
    this.flapSpeed = (float) ((double) this.flapSpeed + (double) (this.onGround ? -1 : 4) * 0.3D);
    this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);

    if (!this.onGround && this.flapping < 1.0F) {
      this.flapping = 1.0F;
    }

    this.flapping = (float) ((double) this.flapping * 0.9D);

    Vec3 motion = this.getDeltaMovement();

    if (!this.onGround && motion.y < 0.0D) {
      this.setDeltaMovement(motion.x, motion.y * 0.6D, motion.z);
    }

    this.flap += this.flapping * 2.0F;
  }

  @Override
  public boolean isFood(ItemStack stack) {
    return stack.is(RootsTags.Items.OWL_FOOD);
  }

  public static <T extends OwlEntity> boolean placement(EntityType<T> pAnimal, LevelAccessor worldIn, MobSpawnType reason, BlockPos blockpos, RandomSource pRandom) {
    BlockState down = worldIn.getBlockState(blockpos.below());
    Block block = down.getBlock();
    return block instanceof LeavesBlock || block == Blocks.GRASS || (block instanceof RotatedPillarBlock && down.getMaterial() == Material.WOOD) || block == Blocks.AIR && worldIn.getMaxLocalRawBrightness(blockpos) > 8;
  }

  @Override
  public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
    return false;
  }

  @Override
  protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
  }

  @Override
  @Nonnull
  public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob ageable) {
    return ModEntities.OWL.get().create(world);
  }

  @Override
  public boolean doHurtTarget(Entity entityIn) {
    return entityIn.hurt(DamageSource.mobAttack(this), 3.0F);
  }

  @Override
  @Nullable
  public SoundEvent getAmbientSound() {
    return null; // TODO: Sounds
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
    return null; // TODO: Sounds
  }

  @Override
  protected SoundEvent getDeathSound() {
    return null; // TODO: Sounds
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState blockIn) {
    this.playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
  }

  @Override
  protected void onFlap() {
    this.playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
    this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
  }

  @Override
  protected boolean isFlapping() {
    return this.flyDist > this.nextFlap;
  }

  @Override
  public float getVoicePitch() {
    return getPitch(this.random);
  }

  private static float getPitch(RandomSource random) {
    return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
  }

  @Override
  public SoundSource getSoundSource() {
    return SoundSource.NEUTRAL;
  }

  @Override
  public boolean isPushable() {
    return true;
  }

  @Override
  protected void doPush(Entity entityIn) {
    if (!(entityIn instanceof Player)) {
      super.doPush(entityIn);
    }
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    if (this.isInvulnerableTo(source)) {
      return false;
    } else {
      return super.hurt(source, amount);
    }
  }

  public boolean isFlying() {
    return !this.onGround;
  }
}