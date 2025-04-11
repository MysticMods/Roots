package mysticmods.roots.spell;

import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.growth.HarvestRecord;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.util.FakePlayerUtil;
import mysticmods.roots.util.HarvestUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class HarvestSpell extends TwoRadiusSpell {
  private BoundingBox box;

  public HarvestSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0x39fd1c, 0xc5e91c);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.HARVEST_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.HARVEST_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.HARVEST_RADIUS_ZX;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    this.box = new BoundingBox(-getRadiusX(), -getRadiusY(), -getRadiusZ(), getRadiusX() + 1, getRadiusY() + 1, getRadiusZ() + 1);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    FakePlayerUtil.buildItems(pLevel, pLevel.getRandom());
    BoundingBox search = box.moved((int) pPlayer.getX(), (int) pPlayer.getY(), (int) pPlayer.getZ());
    List<BlockPos> positions = new ArrayList<>();
    BlockPos.betweenClosedStream(search).forEach(pos -> {
      HarvestRecord record = HarvestUtil.getRecord(pLevel, pos, pLevel.getBlockState(pos), pPlayer);
      if (record != null && record.canHarvest(pLevel, pos, pLevel.getBlockState(pos), pPlayer)) {
        record.harvest(pLevel, pos, pLevel.getBlockState(pos), pPlayer);
        positions.add(pos.immutable());
      }
    });
    if (positions.isEmpty()) {
      costs.noCharge();
      return 0;
    }

    costs.operations(positions.size());
    return cooldown * positions.size();
  }

  @Override
  public CostInstance.ChargeType getChargeType() {
    return CostInstance.ChargeType.OPERATION;
  }
}
