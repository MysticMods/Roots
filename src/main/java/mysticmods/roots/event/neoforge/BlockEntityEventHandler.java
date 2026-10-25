package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class BlockEntityEventHandler {
  @SubscribeEvent
  public static void onBlockEntityType(BlockEntityTypeAddBlocksEvent event) {
    event.modify(BlockEntityType.SKULL,
        ModBlocks.HUSK_HEAD.get(),
        ModBlocks.DROWNED_HEAD.get(),
        ModBlocks.ZOMBIE_VILLAGER_HEAD.get(),
        ModBlocks.ZOMBIFIED_PIGLIN_HEAD.get(),
        ModBlocks.BOGGED_SKULL.get(),
        ModBlocks.STRAY_SKULL.get(),
        ModBlocks.ENDERMAN_HEAD.get(),
        ModBlocks.WITCH_HEAD.get(),
        ModBlocks.PILLAGER_HEAD.get(),
        ModBlocks.ILLUSIONER_HEAD.get(),
        ModBlocks.PIGLIN_BRUTE_HEAD.get(),
        ModBlocks.VILLAGER_HEAD.get(),
        ModBlocks.WANDERING_TRADER_HEAD.get(),
        ModBlocks.IRON_GOLEM_HEAD.get(),
        ModBlocks.HUSK_WALL_HEAD.get(),
        ModBlocks.DROWNED_WALL_HEAD.get(),
        ModBlocks.ZOMBIE_VILLAGER_WALL_HEAD.get(),
        ModBlocks.ZOMBIFIED_PIGLIN_WALL_HEAD.get(),
        ModBlocks.BOGGED_WALL_SKULL.get(),
        ModBlocks.STRAY_WALL_SKULL.get(),
        ModBlocks.ENDERMAN_WALL_HEAD.get(),
        ModBlocks.WITCH_WALL_HEAD.get(),
        ModBlocks.PILLAGER_WALL_HEAD.get(),
        ModBlocks.ILLUSIONER_WALL_HEAD.get(),
        ModBlocks.PIGLIN_BRUTE_WALL_HEAD.get(),
        ModBlocks.VILLAGER_WALL_HEAD.get(),
        ModBlocks.WANDERING_TRADER_WALL_HEAD.get(),
        ModBlocks.IRON_GOLEM_WALL_HEAD.get());
  }
}
