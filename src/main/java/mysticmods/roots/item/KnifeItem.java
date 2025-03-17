package mysticmods.roots.item;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.recipe.ConditionResult;
import mysticmods.roots.api.recipe.UnlockResult;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import mysticmods.roots.recipe.knife.KnifeRecipe;
import mysticmods.roots.util.FakePlayerUtil;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.List;

public class KnifeItem extends TieredItem {
  public KnifeItem(Tier tier, Properties props) {
    super(tier, props.component(DataComponents.TOOL, createToolProperties()));
  }

  public static Tool createToolProperties() {
    return new Tool(
        List.of(
            Tool.Rule.overrideSpeed(RootsTags.Blocks.FORAGEABLES, 15.0F)
        ),
        1.0F,
        1
    );
  }

  @Override
  public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
    return RootsAPI.KNIFE_DEFAULTS.contains(itemAbility);
  }

  @Override
  public InteractionResult useOn(UseOnContext pContext) {
    Level level = pContext.getLevel();
    BlockPos blockpos = pContext.getClickedPos();
    Player player = pContext.getPlayer();
    BlockState blockstate = level.getBlockState(blockpos);
    SimpleWorldCrafting crafting = new SimpleWorldCrafting(player, level, blockpos, blockstate, pContext);
    RecipeHolder<KnifeRecipe> recipe = ResolvedRecipes.KNIFE.findRecipe(crafting, level);
    ItemStack itemstack = pContext.getItemInHand();
    if (recipe != null) {
      ConditionResult conditionResult = recipe.value()
          .checkConditions(level, player, PyreBlockEntity.getPyreBoundingBox(), blockpos);
      if (conditionResult.anyFailed()) {
        if (!level.isClientSide()) {
          RootsAPI.LOG.info("Conditions failed.");
          conditionResult.failedLevelConditions().forEach(o -> RootsAPI.LOG.info("Failed: " + o.getDescriptionId()));
          conditionResult.failedPlayerConditions().forEach(o -> RootsAPI.LOG.info("Failed: " + o.getDescriptionId()));
          conditionResult.report(player);
        }
        return InteractionResult.FAIL;
      }

      if (!level.isClientSide()) {
        UnlockResult failedGrants = recipe.value().checkUnlocks(level, (ServerPlayer) player);
        if (failedGrants.anyFailed() && !recipe.value().hasOutput(level.registryAccess())) {
          RootsAPI.LOG.info("Grants failed and recipe has no output");
          // TODO:
          /*        failedUnlocks.failedUnlocks().forEach(o -> RootsAPI.LOG.info("Failed grant of type " + o.type().name() + " with id " + o.id()));*/
          failedGrants.report();
          return InteractionResult.FAIL;
        }
      }

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

  @Override
  public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
    if (!level.isClientSide && !state.is(BlockTags.FIRE)) {
      stack.hurtAndBreak(1, entityLiving, EquipmentSlot.MAINHAND);
    }

    return state.is(RootsTags.Blocks.FORAGEABLES);
  }

  @Override
  public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    return true;
  }

  @Override
  public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
  }

  public static class KnifeDispenseBehaviour extends DefaultDispenseItemBehavior {
    @Override
    protected ItemStack execute(BlockSource blockSource, ItemStack item) {
      BlockState state = blockSource.state();
      if (!state.hasProperty(DispenserBlock.FACING)) {
        return ItemStack.EMPTY;
      }
      Direction facing = state.getValue(DispenserBlock.FACING);
      BlockPos target = blockSource.pos().relative(facing);
      ServerLevel level = blockSource.level();
      state = level.getBlockState(target);
      if (!state.is(ModBlocks.CREEPING_GROVE_MOSS)) {
        return item;
      }

      Player fakePlayer = FakePlayerFactory.get(level, FakePlayerUtil.ROOTS);
      fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, item);
      LootParams.Builder builder = new LootParams.Builder(level)
          .withParameter(LootContextParams.ORIGIN, blockSource.center())
          .withParameter(LootContextParams.BLOCK_STATE, state)
          .withOptionalParameter(LootContextParams.BLOCK_ENTITY, null)
          .withOptionalParameter(LootContextParams.THIS_ENTITY, fakePlayer)
          .withParameter(LootContextParams.TOOL, item);
      List<ItemStack> result = state.getDrops(builder);

      level.setBlock(target, Blocks.AIR.defaultBlockState(), 3);
      BlockPos above = blockSource.pos().above();
      IItemHandler cap = level.getCapability(Capabilities.ItemHandler.BLOCK, above, null);
      if (cap == null) {
        for (ItemStack r : result) {
          ItemUtil.Spawn.spawnItem(level, above, r);
        }
      } else {
        for (ItemStack r : result) {
          ItemStack r2 = ItemHandlerHelper.insertItem(cap, r, false);
          if (!result.isEmpty()) {
            ItemUtil.Spawn.spawnItem(level, above, r2);
          }
        }
      }

      item.hurtAndBreak(1, level, null, o -> {
      });

      return item;
    }
  }
}
