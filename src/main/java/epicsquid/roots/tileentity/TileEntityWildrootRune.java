package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class TileEntityWildrootRune extends TileBase implements ITickableTileEntity {

  private TileEntityIncenseBurner incenseBurner;
  private static final BlockPos[] INCENSE_POSITIONS = new BlockPos[]{new BlockPos(0, -1, 1), new BlockPos(0, -1, -1), new BlockPos(1, -1, 0), new BlockPos(-1, -1, 0)};

  public final Map<Item, EffectInstance> effectItemMap = new HashMap<>();

  public TileEntityWildrootRune(TileEntityType<?> type) {
    super(type);
    this.incenseBurner = null;

    effectItemMap.put(ModItems.moonglow_leaf, new EffectInstance(Effects.SPEED, 2400, 1));
    effectItemMap.put(ModItems.spirit_herb, new EffectInstance(Effects.NIGHT_VISION, 2400, 1));
    effectItemMap.put(ModItems.infernal_bulb, new EffectInstance(Effects.FIRE_RESISTANCE, 2400, 1));
    effectItemMap.put(ModItems.dewgonia, new EffectInstance(Effects.WATER_BREATHING, 3600, 1));
    effectItemMap.put(ModItems.stalicripe, new EffectInstance(Effects.STRENGTH, 1200, 1));
    effectItemMap.put(ModItems.spirit_herb, new EffectInstance(Effects.INVISIBILITY, 2400, 1));
    effectItemMap.put(ModItems.cloud_berry, new EffectInstance(Effects.JUMP_BOOST, 1200, 1));
    effectItemMap.put(ModItems.pereskia, new EffectInstance(Effects.REGENERATION, 200, 1));

  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    return super.write(compound);
  }

  @Override
  public void read(CompoundNBT compound) {
    super.read(compound);
  }

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull Direction side, float hitX, float hitY, float hitZ) {
    ItemStack heldItem = player.getHeldItem(hand);
    if (incenseBurner == null) {
      return true;
    }
    if (heldItem.isEmpty() && incenseBurner.isLit()) {
      if (this.effectItemMap.getOrDefault(incenseBurner.burningItem(), null) != null) {
        player.addPotionEffect(this.effectItemMap.get(incenseBurner.burningItem()));
      }
    }
    return true;
  }

  @Override
  public void tick() {
    if (world.getGameTime() % 20 == 0) {
      if (this.incenseBurner != null) {
        if (!this.incenseBurner.isLit()) {
          this.incenseBurner = null;
        }
      } else {
        for (BlockPos pos : INCENSE_POSITIONS) {
          if (world.getBlockState(this.getPos().add(pos)).getBlock() == ModBlocks.incense_burner) {
            TileEntityIncenseBurner incenseBurner = (TileEntityIncenseBurner) world.getTileEntity(this.getPos().add(pos));
            if (incenseBurner == null) {
              continue;
            }
            if (incenseBurner.isLit()) {
              this.incenseBurner = incenseBurner;
              break;
            }
          }
        }
      }
    }
  }

  public TileEntityIncenseBurner getIncenseBurner() {
    return incenseBurner;
  }


}
