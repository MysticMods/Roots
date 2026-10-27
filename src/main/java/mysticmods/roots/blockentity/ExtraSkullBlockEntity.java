package mysticmods.roots.blockentity;

import mysticmods.roots.api.block.ExtraSkullTypes;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ExtraSkullBlockEntity extends BlockEntity {
  @Nullable
  private ResourceLocation noteBlockSound;
  private int animationTickCount;
  private boolean isAnimating;
  @Nullable
  private Component customName;

  public ExtraSkullBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModBlockEntities.EXTRA_SKULL.get(), pos, blockState);
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);

    if (this.noteBlockSound != null) {
      tag.putString("note_block_sound", this.noteBlockSound.toString());
    }

    if (this.customName != null) {
      tag.putString("custom_name", Component.Serializer.toJson(this.customName, registries));
    }
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);

    if (tag.contains("note_block_sound", 8)) {
      this.noteBlockSound = ResourceLocation.tryParse(tag.getString("note_block_sound"));
    }

    if (tag.contains("custom_name", 8)) {
      this.customName = parseCustomNameSafe(tag.getString("custom_name"), registries);
    } else {
      this.customName = null;
    }
  }

  public static void animation(Level level, BlockPos pos, BlockState state, ExtraSkullBlockEntity blockEntity) {
    if (state.hasProperty(SkullBlock.POWERED) && state.getValue(SkullBlock.POWERED)) {
      blockEntity.isAnimating = true;
      blockEntity.animationTickCount++;
      if (blockEntity.getBlockState()
          .getBlock() instanceof AbstractSkullBlock abst && abst.getType() == ExtraSkullTypes.ENDERMAN) {
        RandomSource random = level.getRandom();

        double centerX = pos.getX() + 0.5;
        double centerZ = pos.getZ() + 0.5;
        double bottomY = pos.getY();

        if (state.getBlock() instanceof WallSkullBlock) {
          Direction facing = state.getValue(WallSkullBlock.FACING);
          centerX -= facing.getStepX() * 0.25;
          centerZ -= facing.getStepZ() * 0.25;
          bottomY += 0.25;
        }

        level.addParticle(
            ParticleTypes.PORTAL,
            centerX + 0.5 * (2.0 * random.nextDouble() - 1.0) * 0.5,
            bottomY + (2.0 * 5.0 * random.nextDouble()) / 16.0,
            centerZ + 0.5 * (2.0 * random.nextDouble() - 1.0) * 0.5,
            (random.nextDouble() - 0.5) * 2.0,
            -random.nextDouble(),
            (random.nextDouble() - 0.5) * 2.0
        );
      }
    } else {
      blockEntity.isAnimating = false;
    }
  }

  public boolean isAnimating() {
    return this.isAnimating;
  }

  public float getAnimation(float partialTick) {
    return this.isAnimating ? (float) this.animationTickCount + partialTick : 0;
  }

  @Nullable
  public ResourceLocation getNoteBlockSound() {
    return this.noteBlockSound;
  }

  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    return this.saveCustomOnly(registries);
  }

  @Override
  protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
    super.applyImplicitComponents(componentInput);
    this.noteBlockSound = componentInput.get(DataComponents.NOTE_BLOCK_SOUND);
    this.customName = componentInput.get(DataComponents.CUSTOM_NAME);
  }

  @Override
  protected void collectImplicitComponents(DataComponentMap.Builder components) {
    super.collectImplicitComponents(components);
    components.set(DataComponents.NOTE_BLOCK_SOUND, this.noteBlockSound);
    components.set(DataComponents.CUSTOM_NAME, this.customName);
  }

  @Override
  public void removeComponentsFromTag(CompoundTag tag) {
    super.removeComponentsFromTag(tag);
    tag.remove("note_block_sound");
    tag.remove("custom_name");
  }
}
