package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.item.CastingItem;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.advancements.critereon.PickedUpItemTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class BlockEventHandler {
  // Needs to be HIGHEST to ensure Architectury's events are lower priority
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
    Player player = event.getEntity();
    InteractionHand hand = event.getHand();
    ItemStack heldItem = player.getItemInHand(hand);
    if (heldItem.is(RootsTags.Items.CASTING_TOOLS)) {
      BlockState block = player.level().getBlockState(event.getPos());
      if (!block.is(RootsTags.Blocks.ALLOW_CASTING_TOOL_RIGHT_CLICK)) {
        event.setUseItem(TriState.TRUE);
        event.setUseBlock(TriState.FALSE);
      }
    }
    // Specific work-around for runic shears & right-click harvest mods
    if (heldItem.is(RootsTags.Items.RUNIC_SHEARS)) {
      BlockState block = player.level().getBlockState(event.getPos());
      if (block.is(BlockTags.CROPS)) {
        UseOnContext context = new UseOnContext(player, hand, event.getHitVec());
        InteractionResult result = heldItem.useOn(context);
        // A pass indicates that there was no recipe
        // Any other result is considered a successful recipe
        if (result != InteractionResult.PASS) {
          event.setCanceled(true);
          event.setCancellationResult(result);
        }
      }
    }
  }

  @SubscribeEvent
  public static void onHoeUse(BlockEvent.BlockToolModificationEvent event) {
    if (event.getItemAbility() == ItemAbilities.HOE_TILL) {
      LevelAccessor world = event.getLevel();
      UseOnContext pContext = event.getContext();
      BlockPos blockpos = pContext.getClickedPos();
      FluidState fluidstate = world.getFluidState(blockpos.above());
      if (pContext.getClickedFace() != Direction.DOWN && fluidstate.is(FluidTags.WATER)) {
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.is(RootsTags.Blocks.UNDERWATER_FARMLAND)) {
          event.setFinalState(Blocks.FARMLAND.defaultBlockState());
        }
      }
    }
  }

  @SubscribeEvent
  public static void magnetiseDrops(BlockDropsEvent event) {
    if (!(event.getBreaker() instanceof ServerPlayer player)) {
      return;
    }

    if (!event.getTool().is(RootsTags.Items.CASTING_TOOLS)) {
      return;
    }

    var level = event.getLevel();
    var tool = event.getTool();

    var modifiers = CastingItem.getEnabledModifiers(level, player, tool);
    if (modifiers.hasTag(RootsTags.SpellModifiers.MAGNETISM)) {
      int xp = event.getDroppedExperience();
      List<ItemStack> items = event.getDrops().stream().map(ItemEntity::getItem).toList();
      event.setDroppedExperience(0);
      event.setCanceled(true);

      // TODO: There's probably a better way to handle this but that won't include the picked-up-drops-event
      for (ItemStack item : items) {
        ItemUtil.Spawn.spawnItem(level, player.getX(), player.getY(), player.getZ(), false, item, 0);
      }
      // TODO: Sound
      if (xp > 0) {
        player.giveExperiencePoints(xp);
      }
    }
  }
}
