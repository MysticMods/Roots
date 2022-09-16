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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class GrowthInfusionSpell extends Spell {
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
  }

  @Override
  public void cast(Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    // TODO: This shouldn't ever be called client-side

    Level level = pPlayer.level;

    BlockHitResult result = pick(pPlayer);
    BlockState at = level.getBlockState(result.getBlockPos());
    if (GrowthUtil.canGrow(level, result.getBlockPos(), at, pPlayer)) {
      RootsAPI.LOG.info("CAN GROW! State at {} at pos {}", at, result.getBlockPos());
    } else {
      RootsAPI.LOG.info("NO GROW! State at {} at pos {}", at, result.getBlockPos());
    }
  }
}
