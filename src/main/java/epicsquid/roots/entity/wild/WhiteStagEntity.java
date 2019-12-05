package epicsquid.roots.entity.wild;

import epicsquid.roots.Roots;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WhiteStagEntity extends AnimalEntity {
  public static final ResourceLocation LOOT_TABLE = new ResourceLocation(Roots.MODID, "entity/white_stag");

  protected WhiteStagEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
    super(type, worldIn);
  }

/*  public WhiteStagEntity(@Nonnull World world) {
    super(world);
    setSize(1.5f, 2.2f);
    this.experienceValue = 7;
  }*/

  @Nullable
  @Override
  public AgeableEntity createChild(AgeableEntity ageable) {
    return null;
  }

  // TODO:

/*  @Override
  protected void initEntityAI() {
    this.tasks.addTask(0, new SwimGoal(this));
    this.tasks.addTask(2, new BreedGoal(this, 1.0D));
    this.tasks.addTask(3, new TemptGoal(this, 1.25D, ModItems.wildewheet, false));
    // TODO: Charge
    this.tasks.addTask(4, new FollowParentGoal(this, 1.25D));
    this.tasks.addTask(5, new RandomWalkingGoal(this, 1.0D));
    this.tasks.addTask(7, new LookRandomlyGoal(this));
    this.tasks.addTask(5, new MeleeAttackGoal(this, 1.5D, true));
    this.targetTasks.addTask(3, new HurtByTargetGoal(this, false));
  }

  @Override
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
    this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
  }*/

  @Override
  public void tick() {
    super.tick();
    this.rotationYaw = this.rotationYawHead;
  }

/*  @Override
  public void setScaleForAge(boolean child) {
    this.setScale(child ? 0.5f : 1.0f);
  }

  @Override
  public float getEyeHeight() {
    return this.isChild() ? this.height : 1.3F;
  }*/

  @Override
  public boolean attackEntityAsMob(Entity entityIn) {
    boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) ((int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));

    if (flag) {
      this.applyEnchantments(this, entityIn);
    }

    return flag;
  }

  @Nonnull
  @Override
  public ResourceLocation getLootTable() {
    return LOOT_TABLE;
  }
}
