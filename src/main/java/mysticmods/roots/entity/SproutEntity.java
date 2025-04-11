package mysticmods.roots.entity;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSounds;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;

public class SproutEntity extends Animal {

  public static final EntityDataAccessor<Boolean> hasGift = SynchedEntityData.defineId(SproutEntity.class, EntityDataSerializers.BOOLEAN);

  public SproutEntity(EntityType<? extends SproutEntity> type, Level world) {
    super(type, world);
    this.setPathfindingMalus(PathType.WATER, -1.0f);
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
    goalSelector.addGoal(3, new TemptGoal(this, 1.25D, this::isFood, false));
    goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
    goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
    goalSelector.addGoal(7, new RandomLookAroundGoal(this));
  }

  public static AttributeSupplier.Builder attributes() {
    return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0d).add(Attributes.MOVEMENT_SPEED, 0.2d);
  }

  @Override
  public boolean isFood(ItemStack stack) {
    return stack.is(RootsTags.Items.SPROUT_FOOD);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(hasGift, false);
  }

  @Override
  public void aiStep() {
    if (this.is(RootsTags.Entities.MELODY_SPROUT)) {
      if (this.level().isClientSide && this.random.nextInt(5) == 0) {
        for (int i = 0; i < 2; ++i) {
          this.level()
              .addParticle(ModParticles.SPROUT_PORTAL.get(), this.getRandomX(0.1F), this.getRandomY() - (double) 0.45F, this.getRandomZ(0.1F), (this.random.nextDouble() - (double) 0.5F) * (double) 1.5F, -this.random.nextDouble(), (this.random.nextDouble() - (double) 0.5F) * (double) 1.5F);
        }
      }
    }

    super.aiStep();
  }

  public boolean hasGift() {
    return this.entityData.get(hasGift);
  }

  public void setHasGift(boolean value) {
    this.entityData.set(hasGift, value);
  }

  @Override
  public void spawnChildFromBreeding(ServerLevel level, Animal mate) {
    super.spawnChildFromBreeding(level, mate);
    if (this.getAge() == 6000) {
      setHasGift(true);
    }
    if (mate instanceof SproutEntity sprout && mate.getAge() == 6000) {
      sprout.setHasGift(true);
    }
  }

  protected ItemStack getGift() {
    // NotLikeThis ???
    Registry<Item> itemRegistry = this.level().registryAccess().registry(Registries.ITEM).orElse(null);
    if (itemRegistry == null) {
      return ItemStack.EMPTY;
    }

    HolderSet.Named<Item> items = itemRegistry.getTag(RootsTags.Items.SPROUT_BREEDING_REWARDS).orElse(null);
    if (items == null) {
      return ItemStack.EMPTY;
    }

    SimpleWeightedRandomList.Builder<Item> builder = new SimpleWeightedRandomList.Builder<>();

    int defaultWeight = ConfigManager.SPROUT_BREEDING_REWARDS_DEFAULT_CHANCE.getAsInt();

    for (Holder<Item> item : items) {
      Integer weight = item.getData(DataMaps.SPROUT_BREEDING_ITEM_CHANCE);
      if (weight == null) {
        weight = defaultWeight;
      }
      builder.add(item.value(), weight);
    }

    SimpleWeightedRandomList<Item> result = builder.build();

    return new ItemStack(result.getRandomValue(this.random).orElse(Items.AIR));
  }

  @Override
  public InteractionResult mobInteract(Player player, InteractionHand hand) {
    if (player.getItemInHand(hand).isEmpty() && hasGift()) {
      ItemStack gift = getGift();
      if (!gift.isEmpty()) {
        ItemUtil.Spawn.spawnItem(this.level(), this.getX(), this.getY(), this.getZ(), true, gift, -1);
        setHasGift(false);
        return InteractionResult.SUCCESS;
      }
    }

    return super.mobInteract(player, hand);
  }

  @org.jetbrains.annotations.Nullable
  @Override
  public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
    return (AgeableMob) getType().create(p_146743_);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("hasGift", this.hasGift());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setHasGift(compound.getBoolean("hasGift"));
  }

  @Override
  public float getWalkTargetValue(BlockPos pos, LevelReader level) {
    TagKey<Block> blockTag = null;
    if (this.is(RootsTags.Entities.END_ANIMALS)) {
      blockTag = RootsTags.Blocks.SUPPORTS_MELODY_SPROUT_SPAWN;
    } else if (this.is(RootsTags.Entities.SNOW_ANIMALS)) {
      blockTag = RootsTags.Blocks.SUPPORTS_SNOW_SPROUT_SPAWN;
    } else if (this.is(RootsTags.Entities.HELL_ANIMALS)) {
      blockTag = RootsTags.Blocks.SUPPORTS_HELL_SPROUT_SPAWN;
    }
    if (blockTag == null) {
      return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) ? 10.0F : level.getPathfindingCostFromLightLevels(pos);
    } else {
      return level.getBlockState(pos.below()).is(blockTag) ? 10.0f : level.getPathfindingCostFromLightLevels(pos);
    }
  }

  private boolean is (TagKey<EntityType<?>> tag) {
    return getType().is(tag);
  }

  public static boolean checkMelodySpawnRule (
      EntityType<? extends Animal> animal, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
  ) {
    return level.getBlockState(pos.below()).is(RootsTags.Blocks.SUPPORTS_MELODY_SPROUT_SPAWN);
  }

  public static boolean checkHellSpawnRule (
      EntityType<? extends Animal> animal, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
  ) {
    return level.getBlockState(pos.below()).is(RootsTags.Blocks.SUPPORTS_HELL_SPROUT_SPAWN);
  }

  public static boolean checkSnowSpawnRule (
      EntityType<? extends Animal> animal, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
  ) {
    boolean flag = MobSpawnType.ignoresLightRequirements(spawnType) || isBrightEnoughToSpawn(level, pos);
    return level.getBlockState(pos.below()).is(RootsTags.Blocks.SUPPORTS_SNOW_SPROUT_SPAWN) && flag;
  }
}
