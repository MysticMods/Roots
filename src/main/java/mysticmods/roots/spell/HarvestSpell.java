package mysticmods.roots.spell;

import mysticmods.roots.api.SpellType;
import mysticmods.roots.api.Cycling;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.*;
import mysticmods.roots.growth.HarvestRecord;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.HarvestFXPacket;
import mysticmods.roots.spell.mode.HarvestMode;
import mysticmods.roots.util.FakePlayerUtil;
import mysticmods.roots.util.HarvestUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
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

public class HarvestSpell extends TwoRadiusSpell {

  public HarvestSpell(Spell.Properties properties) {
    super(properties);
  }

  @Deprecated
  public HarvestSpell(ChatFormatting color, CostInstance costs) {
    super(SpellType.Cast.INSTANT, color, costs, SpellType.Primary.OPERATION, 0x39fd1c, 0xc5e91c);
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
  public CastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    HarvestMode mode = instance.getSpellData(ModAttachments.HARVEST_MODE);

    ItemStack offHandItem = pPlayer.getOffhandItem();
    Block tempBlock = offHandItem.getItemHolder().getData(DataMaps.HARVEST_SEED_TO_CROP);
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
      if (mode == HarvestMode.HELD_IN_OFFHAND && block != null && !state.is(block)) {
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
      return CastResult.nothing();
    }

    PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new HarvestFXPacket(positions));
    costs.operations(positions.size());
    return CastResult.success(positions.size(), cooldown * positions.size());
  }

  @Override
  public DataComponentType<? extends Cycling<?>> getCycleComponent(ISpellInstance iSpellInstance) {
    return ModAttachments.HARVEST_MODE.get();
  }

  @Override
  public Component[] createExtendedDescriptionComponents() {
    return new Component[]{
        Component.literal(String.valueOf(radiusZX)),
        Component.literal(String.valueOf(radiusY))
    };
  }

  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    return new Component[]{};
  }
}
