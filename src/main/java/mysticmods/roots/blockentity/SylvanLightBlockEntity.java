package mysticmods.roots.blockentity;

import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModModifiers;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class SylvanLightBlockEntity extends BaseBlockEntity implements ClientTickBlockEntity, ServerTickBlockEntity {
  private final RandomSource random;

  private static final Map<SylvanLightColor, int[][]> COLORS = new EnumMap<>(SylvanLightColor.class);
  private int ticks;
  private int decay = -1;
  private SylvanLightColor color = SylvanLightColor.ORIGINAL;

  private enum SylvanLightColor implements StringRepresentable {
    WHITE,
    ORANGE,
    LIME,
    PINK,
    CYAN,
    ORIGINAL;

    @Override
    public String getSerializedName() {
      return name().toLowerCase(Locale.ROOT);
    }

    private static SylvanLightColor fromOrdinal(int i) {
      return values()[i];
    }
  }

  public SylvanLightBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModBlockEntities.SYLVAN_LIGHT.get(), pos, blockState);

    if (COLORS.isEmpty()) {
      COLORS.put(SylvanLightColor.ORIGINAL, new int[][]{
          {0xffe383, 0xffbd83}, {0xffb4eb, 0x9da2ff}, {0x9dfff9, 0xadff9d}, {0xe7ff9d, 0x9db9ff}, {0xffb69d, 0xff9dc4}, {0x9dffa6, 0xc1ddff}
      });
      COLORS.put(SylvanLightColor.WHITE, new int[][]{{0xffffff}, {0xffffff}});
      COLORS.put(SylvanLightColor.ORANGE, new int[][]{{0xfdf3d7, 0xfcd6be}, {0xf3edc7, 0xfcd396}});
      COLORS.put(SylvanLightColor.LIME, new int[][]{
          {0xf2ffe8, 0xc4fcb6}, {0xabfcc6, 0xeafcab}, {0xd0fec0, 0xe8f4ca}, {0xf2eecc, 0xcafdc1}});
      COLORS.put(SylvanLightColor.PINK, new int[][]{
          {0xfcc2c2, 0xfee1d6}, {0xfed6f5, 0xfec5d5}, {0xf8b7ff, 0xffebe8},
      });
      COLORS.put(SylvanLightColor.CYAN, new int[][]{
          {0xc0feec, 0xc0dcfe}, {0xc4cbfb, 0xc5faf8}, {0xc6f9e4, 0xc5cffa}
      });
    }

    this.random = RandomSource.create();
  }

  public void setDecaying (int decayTicks) {
    this.decay = decayTicks;
    setChanged();
    updateViaState();
  }

  public void setColor(SpellModifier modifier) {
    var curColor = this.color;

    if (modifier.is(ModModifiers.SYLVAN_LIGHT_CYAN)) {
      this.color = SylvanLightColor.CYAN;
    } else if (modifier.is(ModModifiers.SYLVAN_LIGHT_LIME)) {
      this.color = SylvanLightColor.LIME;
    } else if (modifier.is(ModModifiers.SYLVAN_LIGHT_ORANGE)) {
      this.color = SylvanLightColor.ORANGE;
    } else if (modifier.is(ModModifiers.SYLVAN_LIGHT_PINK)) {
      this.color = SylvanLightColor.PINK;
    } else if (modifier.is(ModModifiers.SYLVAN_LIGHT_WHITE)) {
      this.color = SylvanLightColor.WHITE;
    }

    if (color != curColor) {
      setChanged();
      updateViaState();
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.putInt("decay", decay);
    tag.putInt("color", color.ordinal());
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    this.decay = tag.contains("decay") ? tag.getInt("decay") : -1;
    this.color = tag.contains("color") ? SylvanLightColor.fromOrdinal(tag.getInt("color")) : SylvanLightColor.ORIGINAL;
  }

  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    this.ticks++;

    if (ticks % 2 == 0) {
      if (random.nextInt(3) == 0) {
        return;
      }
      int[][] colorSet = COLORS.get(this.color);

      int[] color = colorSet[random.nextInt(colorSet.length)];
      Vec3 spot = Vec3.atCenterOf(pPos)
          .add((random.nextDouble() - 0.5) * 0.058, (random.nextDouble() - 0.5) * 0.02, (random.nextDouble() - 0.5) * 0.058);

      RootsParticleOptions.builder(ModParticles.LIGHT).color(color).velocity(0, random.nextFloat() * 0.003, 0)
          .start(spot).spawn(pLevel);
    }
  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    if (decay > 0) {
      decay--;
      if (decay > 0) {
        updateViaState();
      }
    }

    if (decay == 0) {
      // We decayed!
      pLevel.destroyBlock(pPos, true);
    }
  }
}
