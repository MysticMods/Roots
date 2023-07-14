package mysticmods.roots.item;

import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.bark.BarkCrafting;
import mysticmods.roots.recipe.bark.BarkRecipe;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import noobanidus.libs.noobutil.item.BaseItems;
import noobanidus.libs.noobutil.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;

public class KnifeItem extends BaseItems.KnifeItem {
  public KnifeItem(Tier tier, float attackDamage, float attackSpeed, Properties props) {
    super(tier, attackDamage, attackSpeed, props);
  }

  public InteractionResult useOn(UseOnContext pContext) {
    Level level = pContext.getLevel();
    BlockPos blockpos = pContext.getClickedPos();
    Player player = pContext.getPlayer();
    BlockState blockstate = level.getBlockState(blockpos);
    BarkCrafting crafting = new BarkCrafting(player, level, blockpos, blockstate, pContext);
    BarkRecipe recipe = ResolvedRecipes.BARK.findRecipe(crafting, level);
    ItemStack itemstack = pContext.getItemInHand();
    if (recipe != null) {
      level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);

      if (player instanceof ServerPlayer) {
        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
      }

      if (player != null) {
        itemstack.hurtAndBreak(1, player, (p_150686_) -> {
          p_150686_.broadcastBreakEvent(pContext.getHand());
        });
      }

      if (!level.isClientSide()) {
        List<ItemStack> results = new ArrayList<>();
        // TODO: Item could be empty with only chance outputs
        results.add(recipe.assemble(crafting));
        results.addAll(recipe.assembleChanceOutputs(level.getRandom()));
        for (ItemStack stack : results) {
          ItemUtil.Spawn.spawnItem(level, player == null ? blockpos : player.blockPosition(), stack);
        }
      }

      return InteractionResult.sidedSuccess(level.isClientSide);
    } else {
      return InteractionResult.PASS;
    }
  }
}
