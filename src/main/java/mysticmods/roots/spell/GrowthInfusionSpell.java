package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.util.GrowthUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class GrowthInfusionSpell extends Spell {
  private int baseTicks;

  public GrowthInfusionSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.CONTINUOUS, color, costs, 0x30ff30, 0xc0ffc0);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.GROWTH_INFUSION_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.DoubleProperty> getReachProperty() {
    return ModSpells.GROWTH_INFUSION_ADDED_REACH;
  }

  @Override
  public void buildProperties (List<PropertyHolder<?>> result) {
    super.buildProperties(result);
    result.add(ModSpells.GROWTH_INFUSION_BASE_TICKS);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.baseTicks = properties.get(ModSpells.GROWTH_INFUSION_BASE_TICKS);
  }

  @Override
  public int cast(Level level, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    BlockHitResult result = pickBlock(pPlayer);
    BlockState at = level.getBlockState(result.getBlockPos());

    int doTicks = GrowthUtil.growthTicks(level, result.getBlockPos(), at, pPlayer);
    if (doTicks > 0) {
      if (level.random.nextInt(doTicks) == 0) {
        at.randomTick((ServerLevel) level, result.getBlockPos(), level.random);
      }
    } else {
      costs.noCharge();
      pPlayer.stopUsingItem();
      return 0;
    }

    return cooldown;
  }
}
