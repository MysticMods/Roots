package mysticmods.roots.spell;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mysticmods.roots.action.CropGrowthAction;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.ParentChargeType;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.RampantGrowthFXPacket;
import mysticmods.roots.util.GrowthUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RampantGrowthSpell extends TwoRadiusSpell {
  private int interval, count;

  public RampantGrowthSpell(ChatFormatting color, CostInstance costs) {
    super(Type.CONTINUOUS, color, costs, ParentChargeType.OPERATION, 0x157318, 0x13c3eb);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.RAMPANT_GROWTH_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.RAMPANT_GROWTH_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.RAMPANT_GROWTH_RADIUS_ZX;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.interval = properties.get(ModSpells.RAMPANT_GROWTH_INTERVAL);
    this.count = properties.get(ModSpells.RAMPANT_GROWTH_COUNT);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.RAMPANT_GROWTH_INTERVAL);
    properties.add(ModSpells.RAMPANT_GROWTH_COUNT);
  }

  @Override
  protected void fillDataKeyMap(Object2IntMap<String> map) {
    super.fillDataKeyMap(map);
    map.put("mode", 0);
    map.put("untagged", 1);
    map.put("tagged", 2);
    map.put("held", 3);
  }

  @Override
  protected void fillDataMaximumValues(Int2IntMap map) {
    super.fillDataMaximumValues(map);
    map.put(0, 3);
  }

  @Override
  public Set<String> getTooltipDataKeys() {
    return Set.of("mode");
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    if (ticks % interval == 0) {
      boolean checkTag = getDataValue(instance, "mode") == 2;
      ItemStack offHandItem = pPlayer.getOffhandItem();
      Block tempBlock = offHandItem.getItemHolder().getData(DataMaps.GROWTH_SEED_TO_CROP);
      if (tempBlock == null) {
        if (offHandItem.getItem() instanceof BlockItem blockItem) {
          tempBlock = blockItem.getBlock();
        }
      }

      if (getBoundingBox() == null) {
        costs.noCharge();
        return -1;
      }

      final Block block = tempBlock;

      boolean offHand = getDataValue(instance, "mode") == 3 && !offHandItem.isEmpty() && tempBlock != null;

      BoundingBox search = getBoundingBox().moved((int) pPlayer.getX(), (int) pPlayer.getY(), (int) pPlayer.getZ());
      List<BlockPos> positions = new ArrayList<>();
      BlockPos.betweenClosedStream(search).forEach(pos -> {
        BlockState state = pLevel.getBlockState(pos);
        if (GrowthUtil.growthTicks(pLevel, pos, state, pPlayer) > 0) {
          if (checkTag && state.is(RootsTags.Blocks.RAMPANT_GROWTH_EXCLUDE_MODE)) {
            return;
          }
          if (offHand && !state.is(block)) {
            return;
          }
          positions.add(pos.immutable());
        }
      });
      if (positions.isEmpty()) {
        costs.noCharge();
        return -1;
      }
      int growCount = 0;
      for (int i = 0; i < count; i++) {
        if (positions.isEmpty()) {
          break;
        }
        BlockPos pos = positions.get(pLevel.random.nextInt(positions.size()));
        int doTicks = GrowthUtil.growthTicks(pLevel, pos, null, pPlayer);
        if (doTicks > 0) {
          if (pLevel.random.nextInt(doTicks) == 0) {
            BlockState oldState = pLevel.getBlockState(pos);
            oldState.randomTick((ServerLevel) pLevel, pos, pLevel.random);
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new RampantGrowthFXPacket(pos));
            BlockState newState = pLevel.getBlockState(pos);
            CropGrowthAction.Context context = new CropGrowthAction.Context((ServerLevel) pLevel, (ServerPlayer) pPlayer, pos, newState, oldState, pHand, pStack, instance);
            ModActions.CROP_GROWTH.get().accept(context);
            growCount++;
          }
        }
      }
      if (growCount == 0) {
        costs.noCharge();
        return -1;
      } else {
        costs.operations(growCount);
      }

      return cooldown;
    } else {
      costs.noCharge();
      return -1;
    }
  }

  @Override
  public ParentChargeType getChargeType() {
    return ParentChargeType.OPERATION;
  }

  @Override
  public int getMaximumOperations() {
    return count;
  }

  @Override
  public int getCostChargeRate() {
    return interval;
  }
}
