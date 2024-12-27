package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FeyLightSpell extends Spell {
  protected double maxDistance = 0;

  public FeyLightSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0xf7f6d2, 0xe351f4);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.FEY_LIGHT_COOLDOWN.get();
  }

  @Override
  public void initialize() {
    this.maxDistance = ModSpells.FEY_LIGHT_MAX_DISTANCE.get().getValue();
  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    Vec3 look = pPlayer.getLookAngle().scale(1.5);
    BlockPos potentialPos = new BlockPos(pPlayer.position().add(look.x, 1, look.z));
    boolean doPlace = pLevel.isEmptyBlock(potentialPos);
    if (!doPlace) {
      BlockPlaceContext context = new BlockPlaceContext(pLevel, null, InteractionHand.MAIN_HAND, ItemStack.EMPTY, new BlockHitResult(Vec3.ZERO, Direction.UP, potentialPos, false));
      BlockState stateAt = pLevel.getBlockState(potentialPos);
      if (stateAt.canBeReplaced(context)) {
        doPlace = true;
      }
    }

    if (doPlace) {
      pLevel.setBlock(potentialPos, ModBlocks.FEY_LIGHT.getDefaultState(), 3);
    } else {
      costs.noCharge();
    }
  }
}
