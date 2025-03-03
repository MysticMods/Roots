package mysticmods.roots.spell;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShatterSpell extends Spell {
  private int maxWidth, maxDepth, maxHeight;

  public ShatterSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0x606060, 0xc0c0c0);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.SHATTER_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.maxWidth = properties.get(ModSpells.SHATTER_MAXIMUM_WIDTH);
    this.maxDepth = properties.get(ModSpells.SHATTER_MAXIMUM_DEPTH);
    this.maxHeight = properties.get(ModSpells.SHATTER_MAXIMUM_HEIGHT);
  }

  @Override
  protected void fillDataKeyMap(Object2IntMap<String> map) {
    super.fillDataKeyMap(map);
    map.put("mode", 0);
    map.put("width", 1);
    map.put("height", 2);
    map.put("depth", 3);
  }

  @Override
  protected void fillDataMaximumValues(Int2IntMap map) {
    super.fillDataMaximumValues(map);
    map.put(0, 3);
    map.put(1, maxWidth);
    map.put(2, maxHeight);
    map.put(3, maxDepth);
  }

  @Override
  public Map<BlockPos, BlockState> getAffectedBlocks(Level level, Player player, ISpellInstance spell, ItemStack stack, BlockPos pos, BlockState blockState, BlockHitResult rayTraceResult) {
    Map<BlockPos, BlockState> result = new HashMap<>();

    float yaw = player.getViewYRot(1f);
    Direction playerFacing = Direction.fromYRot(yaw);
    Direction sideDir = rayTraceResult.getDirection();
    Direction widthDir = Direction.fromYRot(playerFacing.toYRot() + 90);
    Direction heightDir = sideDir == Direction.DOWN ? playerFacing : sideDir.getAxis() == Direction.Axis.Y ? playerFacing.getOpposite() : Direction.DOWN;
    Direction depthDir = sideDir.getOpposite();

    int width = getDataValue(spell, "width");
    int height = getDataValue(spell, "height");
    int depth = getDataValue(spell, "depth");

    BlockPos start = pos;
    BlockPos stop = pos;

    if (width == 0 && height == 0 && depth == 0) {
      stop = stop.relative(heightDir);
    } else {
      if (width > 0) {
        start = start.relative(widthDir, -width);
        stop = stop.relative(widthDir, width);
      }
      if (height > 0) {
        start = start.relative(heightDir, -height);
        stop = stop.relative(heightDir, height);
      }
      if (depth > 0) {
        stop = stop.relative(depthDir, depth);
      }
    }

    for (BlockPos blockPos : BlockPos.betweenClosed(start, stop)) {
      BlockState state = level.getBlockState(blockPos);
      if (state.isAir()) {
        continue;
      }
      result.put(blockPos.immutable(), state);
    }

    return result;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.SHATTER_MAXIMUM_DEPTH);
    properties.add(ModSpells.SHATTER_MAXIMUM_HEIGHT);
    properties.add(ModSpells.SHATTER_MAXIMUM_WIDTH);
  }

  @Override
  public boolean hasBlockTarget(Player pPlayer) {
    return true;
  }

  @Override
  public @Nullable Vec3 getBlockTarget(Player pPlayer) {
    return pickBlock(pPlayer).getLocation();
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    BlockHitResult rayTraceResult = pickBlock(pPlayer);
    Map<BlockPos, BlockState> toBreak = getAffectedBlocks(pLevel, pPlayer, instance, pStack, rayTraceResult.getBlockPos(), pLevel.getBlockState(rayTraceResult.getBlockPos()), rayTraceResult);
    for (Map.Entry<BlockPos, BlockState> entry : toBreak.entrySet()) {
      BlockPos pos = entry.getKey();
      pLevel.destroyBlock(pos, true, pPlayer);
    }

    return cooldown;
  }
}
