package mysticmods.roots.item;

import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import mysticmods.roots.recipe.bark.BarkRecipe;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class KnifeItem extends DiggerItem {
  public KnifeItem(Tier tier, Properties props) {
    super(tier, BlockTags.LOGS, props);
  }

  @Override
  public InteractionResult useOn(UseOnContext pContext) {
    Level level = pContext.getLevel();
    BlockPos blockpos = pContext.getClickedPos();
    Player player = pContext.getPlayer();
    BlockState blockstate = level.getBlockState(blockpos);
    SimpleWorldCrafting crafting = new SimpleWorldCrafting(player, level, blockpos, blockstate, pContext);
    RecipeHolder<BarkRecipe> recipe = ResolvedRecipes.BARK.findRecipe(crafting, level);
    ItemStack itemstack = pContext.getItemInHand();
    if (recipe != null) {
      level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);

      if (player instanceof ServerPlayer) {
        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
      }

      if (player != null) {
        itemstack.hurtAndBreak(1, player, pContext.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
      }

      if (!level.isClientSide()) {
        List<ItemStack> results = recipe.value()
            .assembleOutputs(crafting, level.getRandom(), level.registryAccess(), null);
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
