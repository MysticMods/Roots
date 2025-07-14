package mysticmods.roots.spell;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.growth.HarvestRecord;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.HarvestFXPacket;
import mysticmods.roots.util.FakePlayerUtil;
import mysticmods.roots.util.HarvestUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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

public class HarvestSpell extends TwoRadiusSpell {
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
  }

  @Override
  protected void fillDataKeyMap(Object2IntMap<String> map) {
    super.fillDataKeyMap(map);
    map.put("mode", 0);
    map.put("all", 1);
    map.put("held", 2);
  }

  @Override
  protected void fillDataMaximumValues(Int2IntMap map) {
    super.fillDataMaximumValues(map);
    map.put(0, 2);
  }

  @Override
  public Set<String> getTooltipDataKeys() {
    return Set.of("mode");
  }


  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    boolean held = getDataValue(instance, "mode") == 2;

    ItemStack offHandItem = pPlayer.getOffhandItem();
    Block tempBlock = offHandItem.getItemHolder().getData(DataMaps.SEED_TO_CROP);
    if (tempBlock == null) {
      if (offHandItem.getItem() instanceof BlockItem blockItem) {
        tempBlock = blockItem.getBlock();
      }
    }

    final Block block = tempBlock;

    FakePlayerUtil.buildItems(pLevel, pLevel.getRandom());
    BoundingBox search = getBoundingBox().moved((int) pPlayer.getX(), (int) pPlayer.getY(), (int) pPlayer.getZ());
    List<BlockPos> positions = new ArrayList<>();
    BlockPos.betweenClosedStream(search).forEach(pos -> {
      BlockState state = pLevel.getBlockState(pos);
      if (held && block != null && !state.is(block)) {
        return;
      }
      HarvestRecord record = HarvestUtil.getRecord(pLevel, pos, state, pPlayer);
      if (record != null && record.canHarvest(pLevel, pos, state, pPlayer)) {
        record.harvest(pLevel, pos, state, pPlayer);
        positions.add(pos.immutable());
      }
    });
    if (positions.isEmpty()) {
      costs.noCharge();
      return 0;
    }

    PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new HarvestFXPacket(positions));
    costs.operations(positions.size());
    return cooldown * positions.size();
  }

  @Override
  public CostInstance.ChargeType getChargeType() {
    return CostInstance.ChargeType.OPERATION;
  }
}
