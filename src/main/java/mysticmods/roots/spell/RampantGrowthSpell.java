package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.GrowthFXPacket;
import mysticmods.roots.network.client.fx.RampantGrowthFXPacket;
import mysticmods.roots.util.GrowthUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class RampantGrowthSpell extends TwoRadiusSpell {
  private int interval, count;
  private BoundingBox box;

  private static final BiPredicate<Level, BlockPos> GROWABLE_CROP = (level, pos) -> GrowthUtil.growthTicks(level, pos, null, null) > 0;

  public RampantGrowthSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.CONTINUOUS, color, costs, 0x157318, 0x13c3eb);
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
    this.box = new BoundingBox(-getRadiusX(), -getRadiusY(), -getRadiusZ(), getRadiusX() + 1, getRadiusY() + 1, getRadiusZ() + 1);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.RAMPANT_GROWTH_INTERVAL);
    properties.add(ModSpells.RAMPANT_GROWTH_COUNT);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    if (ticks % interval == 0) {
      BoundingBox search = box.moved((int) pPlayer.getX(), (int) pPlayer.getY(), (int) pPlayer.getZ());
      List<BlockPos> positions = new ArrayList<>();
      BlockPos.betweenClosedStream(search).forEach(pos -> {
        if (GrowthUtil.growthTicks(pLevel, pos, null, pPlayer) > 0) {
          positions.add(pos.immutable());
        }
      });
      if (positions.isEmpty()) {
        costs.noCharge();
        return -1;
      }
      boolean hasGrown = false;
      for (int i = 0; i < count; i++) {
        if (positions.isEmpty()) {
          break;
        }
        BlockPos pos = positions.remove(pLevel.random.nextInt(positions.size()));
        int doTicks = GrowthUtil.growthTicks(pLevel, pos, null, pPlayer);
        if (doTicks > 0) {
          if (pLevel.random.nextInt(doTicks) == 0) {
            pLevel.getBlockState(pos).randomTick((ServerLevel) pLevel, pos, pLevel.random);
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new RampantGrowthFXPacket(pos));
            hasGrown = true;
          }
        }
      }
      if (!hasGrown) {
        costs.noCharge();
        return -1;
      }

      return cooldown;
    } else {
      costs.noCharge();
      return -1;
    }
  }
}
