package mysticmods.roots.spell;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.util.GrowthUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class GrowthInfusionSpell extends Spell {
  private int base_ticks;

  public GrowthInfusionSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.CONTINUOUS, color, costs);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.GROWTH_INFUSION_COOLDOWN.get();
  }

  @Override
  public SpellProperty<Double> getReachProperty() {
    return ModSpells.GROWTH_INFUSION_ADDED_REACH.get();
  }

  @Override
  public void initialize() {
    this.base_ticks = ModSpells.GROWTH_INFUSION_BASE_TICKS.get().getValue();
  }

  @Override
  public void cast(Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    // TODO: This shouldn't ever be called client-side

    ServerLevel level = (ServerLevel) pPlayer.level;

    BlockHitResult result = pick(pPlayer);
    BlockState at = level.getBlockState(result.getBlockPos());

    int doTicks = GrowthUtil.growthTicks(level, result.getBlockPos(), at, pPlayer);
    if (doTicks > 0 && level.random.nextInt(doTicks) == 0) {
      RootsAPI.LOG.info("RANDOM TICK: {}", ticks);

      at.randomTick(level, result.getBlockPos(), level.random);
    } else {
      RootsAPI.LOG.info("NO GROW! {}", ticks);
    }
  }
}
