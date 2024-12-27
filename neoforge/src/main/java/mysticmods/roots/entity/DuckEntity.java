package mysticmods.roots.entity;

import mysticmods.roots.entity.ai.DuckSwimGoal;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;

public class DuckEntity extends Animal {
  public static Ingredient BREEDING_INGREDIENT;

  public float flap;
  public float flapSpeed;
  public float oFlapSpeed;
  public float oFlap;
  public float flapping = 1.0F;

  public DuckEntity(EntityType<? extends Animal> entityType, Level level) {
    super(entityType, level);
    this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
  }

  public static AttributeSupplier.Builder attributes() {
    return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0d).add(Attributes.MOVEMENT_SPEED, 0.2d);
  }

  @Override
  public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
    return false;
  }

  protected Ingredient getBreedingIngredient() {
    if (BREEDING_INGREDIENT == null) {
      BREEDING_INGREDIENT = Ingredient.of(Tags.Items.SEEDS);
    }
    return BREEDING_INGREDIENT;
  }

  @Override
  protected void registerGoals() {
    goalSelector.addGoal(0, new DuckSwimGoal(this));
    goalSelector.addGoal(1, new PanicGoal(this, 1.6d));
    goalSelector.addGoal(2, new BreedGoal(this, 1.0d));
    goalSelector.addGoal(3, new TemptGoal(this, 1.0d, getBreedingIngredient(), false));
    goalSelector.addGoal(4, new FollowParentGoal(this, 1.0d));
    goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
    goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0d, 120));
    goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
    goalSelector.addGoal(7, new RandomLookAroundGoal(this));
  }

  @Override
  public boolean isFood(ItemStack pStack) {
    return getBreedingIngredient().test(pStack);
  }

  @org.jetbrains.annotations.Nullable
  @Override
  public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pEntity) {
    return ModEntities.DUCK.get().create(pLevel);
  }

  @Override
  public void aiStep() {
    super.aiStep();
    this.oFlap = this.flap;
    this.oFlapSpeed = this.flapSpeed;
    this.flapSpeed = (float) ((double) this.flapSpeed + (double) (this.onGround ? -1 : 4) * 0.3D);
    this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
    if (!this.isInWater() && !this.onGround && this.flapping < 1.0F) {
      this.flapping = 1.0F;
    }

    this.flapping = (float) ((double) this.flapping * 0.9D);
    Vec3 vector3d = this.getDeltaMovement();
    if (!this.onGround && vector3d.y < 0.0D) {
      this.setDeltaMovement(vector3d.multiply(1.0D, 0.6D, 1.0D));
    }

    this.flap += this.flapping * 2.0F;
  }

  @Override
  protected void jumpInLiquid(TagKey<Fluid> pFluidTag) {
    this.setDeltaMovement(this.getDeltaMovement().add(0, this.getNavigation().canFloat() ? 0.039 : 0.2, 0.0));
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
/*    if (random.nextInt(9) == 0) {
      return ModSounds.DUCK_AMBIENT.get();
    }*/
    return null;
  }

  @Override
  protected SoundEvent getSwimSound() {
    return ModSounds.DUCK_SWIM.get();
  }
}
