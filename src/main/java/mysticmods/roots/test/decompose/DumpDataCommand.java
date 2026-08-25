package mysticmods.roots.test.decompose;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DumpDataCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(builder(Commands.literal("dump_data").requires(p -> p.hasPermission(2))));
  }

  private static List<String> entityIds = null;

  private static RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> suggestEntities() {
    if (entityIds == null) {
      entityIds = BuiltInRegistries.ENTITY_TYPE.keySet().stream().map(ResourceLocation::toString).toList();
    }

    return Commands.argument("entity", ResourceLocationArgument.id())
        .suggests((c, build) -> SharedSuggestionProvider.suggest(entityIds, build));
  }

  public static LiteralArgumentBuilder<CommandSourceStack> builder(LiteralArgumentBuilder<CommandSourceStack> builder) {
    builder.then(suggestEntities().executes(c -> {
      ResourceLocation entityID = ResourceLocationArgument.getId(c, "entity");
      EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityID);
      var key = type.getDefaultLootTable();
      List<ILootPoolEntryContainerDecomposer.ItemRecord> result = Decomposers.LOOT_TABLE.perform(c.getSource()
          .getServer().reloadableRegistries().getLootTable(key));

      for (ILootPoolEntryContainerDecomposer.ItemRecord record : result) {
        if (record.item() != null) {
          c.getSource().sendSuccess(() -> record.item().getDescription(), false);
        } else if (record.stack() != null) {
          c.getSource().sendSuccess(() -> record.stack().getDisplayName(), false);
        } else {
          c.getSource().sendSuccess(() -> Component.literal("Tag key: " + record.tag().toString()), false);
        }
      }
      return 1;
    }));
    return builder;
  }

  record BiomeInfo() {
  }

  record EntityInfo() {
  }

  record StructureInfo() {
  }

  record ItemInfo() {
  }

  // Drops self
  // Drops other item based on conditions

  record BlockInfo(ResourceLocation id, Set<ResourceLocation> tags, Set<BreakMode> mode, boolean requiresCorrectTool) {
    public BlockInfo(Holder<Block> block) {
      this(block.getKey().location(),
          block.tags().map(TagKey::location).collect(Collectors.toSet()),
          Set.of(BreakMode.values()).stream().filter(m -> m.test(block)).collect(Collectors.toSet()),
          block.value().defaultBlockState().requiresCorrectToolForDrops());
    }
  }

  enum BreakMode implements Predicate<Holder<Block>> {
    WOODEN_TOOL(o -> !o.is(BlockTags.INCORRECT_FOR_WOODEN_TOOL)),
    STONE_TOOL(o -> !o.is(BlockTags.INCORRECT_FOR_STONE_TOOL) || o.is(BlockTags.NEEDS_STONE_TOOL)),
    IRON_TOOL(o -> !o.is(BlockTags.INCORRECT_FOR_IRON_TOOL) || o.is(BlockTags.NEEDS_IRON_TOOL)),
    GOLD_TOOL(o -> !o.is(BlockTags.INCORRECT_FOR_GOLD_TOOL)),
    DIAMOND_TOOL(o -> !o.is(BlockTags.INCORRECT_FOR_DIAMOND_TOOL) || o.is(BlockTags.NEEDS_DIAMOND_TOOL)),
    NETHERITE_TOOL(o -> !o.is(BlockTags.INCORRECT_FOR_NETHERITE_TOOL));

    private final Predicate<Holder<Block>> predicate;

    BreakMode(Predicate<Holder<Block>> predicate) {
      this.predicate = predicate;
    }

    @Override
    public boolean test(Holder<Block> blockHolder) {
      return predicate.test(blockHolder);
    }
  }
}
