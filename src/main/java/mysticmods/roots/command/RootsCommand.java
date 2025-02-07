package mysticmods.roots.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;

import java.util.List;
import java.util.stream.Collectors;

public class RootsCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(builder(Commands.literal("roots").requires(p -> p.hasPermission(2))));
  }

  private static List<String> spellIds = null;
  private static List<String> ritualIds = null;

  private static RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> suggestSpells() {
    if (spellIds == null) {
      spellIds = RootsRegistries.SPELLS.keySet().stream().map(ResourceLocation::toString).collect(Collectors.toList());
    }
    return Commands.argument("spell", ResourceLocationArgument.id())
        .suggests((c, build) -> SharedSuggestionProvider.suggest(spellIds, build));
  }

  private static RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> suggestRituals() {
    if (ritualIds == null) {
      ritualIds = RootsRegistries.RITUALS.keySet().stream().map(ResourceLocation::toString)
          .collect(Collectors.toList());
    }

    return Commands.argument("ritual", ResourceLocationArgument.id())
        .suggests((c, build) -> SharedSuggestionProvider.suggest(ritualIds, build));
  }

  public static LiteralArgumentBuilder<CommandSourceStack> builder(LiteralArgumentBuilder<CommandSourceStack> builder) {
    builder.executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.usage"), false);
      return 1;
    });
    builder.then(Commands.literal("staff").executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.staff.usage"), false);
      return 1;
    }).then(suggestSpells().executes(c -> {
      ResourceLocation spellID = ResourceLocationArgument.getId(c, "spell");
      Spell spell = RootsRegistries.SPELLS.get(spellID);
      if (spell == null) {
        c.getSource().sendFailure(Component.translatable("roots.commands.staff.spell_not_found", spellID.toString()));
        return 1;
      }
      ServerPlayer player = c.getSource().getPlayerOrException();
      boolean newStaff = false;
      ItemStack staff;
      if (player.getItemInHand(InteractionHand.MAIN_HAND).is(RootsTags.Items.CASTING_TOOLS)) {
        staff = player.getItemInHand(InteractionHand.MAIN_HAND);
      } else {
        newStaff = true;
        staff = new ItemStack(ModItems.STAFF.get());
      }

      SpellStorage storage = staff.get(ModAttachments.SPELL_STORAGE);
      if (storage == null) {
        c.getSource().sendFailure(Component.translatable("roots.commands.staff.no_spell_storage"));
        return 1;
      }
      for (int i = 0; i < storage.maxSlot(); i++) {
        if (storage.getSpell(i) == null) {
          staff.set(ModAttachments.SPELL_STORAGE, storage.setSpell(i, spell, spell.getModifiers()));
          if (newStaff) {
            player.addItem(staff);
          } else {
            player.setItemInHand(InteractionHand.MAIN_HAND, staff);
          }
          return 1;
        }
      }
      if (newStaff) {
        player.addItem(staff);
      } else {
        player.setItemInHand(InteractionHand.MAIN_HAND, staff);
      }
      return 1;
    })));
    builder.then(Commands.literal("ritual").executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.ritual.usage"), false);
      return 1;
    }).then(suggestRituals().executes(c -> {
      ResourceLocation ritualId = ResourceLocationArgument.getId(c, "ritual");
      Ritual ritual = RootsRegistries.RITUALS.get(ritualId);
      if (ritual == null) {
        c.getSource()
            .sendFailure(Component.translatable("roots.commands.ritual.ritual_not_found", ritualId.toString()));
        return 1;
      }

      if (c.getSource().getPlayer() == null) {
        c.getSource().sendFailure(Component.translatable("roots.commands.ritual.no_player"));
        return 1;
      }

      // Get the ritual recipe
      RecipeHolder<PyreRecipe> recipe = ResolvedRecipes.PYRE.findRecipe(ritual);

      if (recipe == null) {
        c.getSource()
            .sendFailure(Component.translatable("roots.commands.ritual.recipe_not_found", ritualId.toString()));
        return 1;
      }

      // Create a pyre
      BlockPos pos = BlockPos.containing(c.getSource().getPosition());
      Level level = c.getSource().getLevel();

      if (!level.getBlockState(pos).isAir()) {
        c.getSource().sendFailure(Component.translatable("roots.commands.ritual.no_space"));
        return 1;
      }

      level.setBlock(pos, ModBlocks.PYRE.get().defaultBlockState(), 3);
      level.setBlock(pos.below(), Blocks.CHEST.defaultBlockState(), 3);

      // Place a chest below the pyre
      InvWrapper chest = new InvWrapper((ChestBlockEntity) level.getBlockEntity(pos.below()));
      PyreBlockEntity pyre = (PyreBlockEntity) level.getBlockEntity(pos);
      // Fill the pyre with the ritual recipe

      for (Ingredient ingredient : recipe.value().getIngredients()) {
        pyre.getInventory().insert(ingredient.getItems()[0].copy());
        ItemStack stack = ingredient.getItems()[0].copy();
        stack.setCount(stack.getMaxStackSize());
        for (int i = 0; i < 5; i++) {
          // Fill the chest with ingredients
          ItemHandlerHelper.insertItemStacked(chest, stack.copy(), false);
        }
      }

      PlayerMainInvWrapper playerInv = new PlayerMainInvWrapper(c.getSource().getPlayer().getInventory());
      // Give the player a flint and steel
      ItemHandlerHelper.insertItemStacked(playerInv, new ItemStack(Items.FLINT_AND_STEEL), false);

      // Iterate over world conditions and create them using /place
      List<LevelCondition> conditions = recipe.value().getLevelConditions();
      for (int i = 0; i < conditions.size(); i++) {
        LevelCondition condition = conditions.get(i);
        if (!condition.getRepresentation().place(level, pos.relative(Direction.NORTH, i + 1))) {
          c.getSource()
              .sendFailure(Component.translatable("roots.commands.ritual.failed_condition", condition.builtInRegistryHolder()
                  .getKey()));
          return 1;
        }
      }

      return 1;
    })));
    builder.then(Commands.literal("activate").executes(c -> {
      AABB bounds = new AABB(-15, -15, -15, 15, 15, 15).move(c.getSource().getPosition());
      List<BlockPos> positions = BlockPos.betweenClosedStream(bounds).map(BlockPos::immutable).toList();
      Level level = c.getSource().getLevel();
      for (BlockPos pos : positions) {
        BlockState stateAt = level.getBlockState(pos);
        if (stateAt.is(RootsTags.Blocks.GROVE_STONES) && stateAt.hasProperty(GroveStoneBlock.ACTIVE)) {
          level.setBlock(pos, stateAt.setValue(GroveStoneBlock.ACTIVE, true), 3);
        }
      }
      return 1;
    }));
    return builder;
  }
}
