package mysticmods.roots.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.CooldownStorage;
import mysticmods.roots.api.attachment.GrantStorage;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.block.PyreBlock;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.network.client.ClientboundReputationSyncPacket;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
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
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RootsCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(builder(Commands.literal("roots").requires(p -> p.hasPermission(2))));
  }

  private static List<String> spellIds = null;
  private static List<String> ritualIds = null;
  private static List<String> craftingRecipeIds = null;
  private static List<String> groveIds = null;

  private static RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> suggestGroves() {
    if (groveIds == null) {
      groveIds = RootsRegistries.GROVES.keySet().stream().map(ResourceLocation::toString).toList();
    }

    return Commands.argument("grove", ResourceLocationArgument.id())
        .suggests((c, build) -> SharedSuggestionProvider.suggest(groveIds, build));
  }

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

  private static List<String> getCraftingRecipeIds() {
    if (craftingRecipeIds == null) {
      ServerLevel level = ServerLifecycleHooks.getCurrentServer().overworld();
      craftingRecipeIds = ResolvedRecipes.PYRE.getRecipes(level).stream()
          .filter(o -> o.value().getRitual() == null).map(recipe -> recipe.id().toString())
          .collect(Collectors.toList());
    }

    return craftingRecipeIds;
  }

  private static RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> suggestRitualRecipes() {
    return Commands.argument("recipe", ResourceLocationArgument.id())
        .suggests((c, build) -> SharedSuggestionProvider.suggest(getCraftingRecipeIds(), build));
  }

  public static LiteralArgumentBuilder<CommandSourceStack> builder(LiteralArgumentBuilder<CommandSourceStack> builder) {
    builder.executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.usage"), false);
      return 1;
    });
    builder.then(Commands.literal("reset").executes(c -> {
      ServerPlayer player = c.getSource().getPlayerOrException();
      CooldownStorage storage = player.getData(ModAttachments.COOLDOWN_STORAGE);
      storage.reset();
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.reset"), false);
      return 1;
    }));
    builder.then(Commands.literal("library").executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.library.usage"), false);
      return 1;
    }).then(Commands.literal("add").executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.library.add.usage"), false);
      return 1;
    }).then(suggestSpells().executes(c -> {
      if (!c.getSource().isPlayer()) {
        c.getSource().sendFailure(Component.translatable("roots.commands.library.no_player"));
        return 0;
      }
      ServerPlayer player = c.getSource().getPlayerOrException();
      GrantStorage grants = player.getData(ModAttachments.GRANT_STORAGE.get());
      ResourceLocation spellID = ResourceLocationArgument.getId(c, "spell");
      Spell spell = RootsRegistries.SPELLS.get(spellID);
      if (spell == null) {
        c.getSource().sendFailure(Component.translatable("roots.commands.staff.spell_not_found", spellID.toString()));
        return 0;
      }
      if (grants.hasSpell(spell)) {
        c.getSource().sendFailure(Component.translatable("roots.commands.library.add.failure", spell.getStyledName()));
        return 0;
      }
      grants.unlock(player, Unlock.spell(spell));
      c.getSource()
          .sendSuccess(() -> Component.translatable("roots.commands.library.add.success", spell.getStyledName()), false);
      return 1;

    }))).then(Commands.literal("remove").executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.library.remove.usage"), false);
      return 1;
    }).then(suggestSpells().executes(c -> {
      if (!c.getSource().isPlayer()) {
        c.getSource().sendFailure(Component.translatable("roots.commands.library.no_player"));
        return 0;
      }
      ServerPlayer player = c.getSource().getPlayerOrException();
      GrantStorage grants = player.getData(ModAttachments.GRANT_STORAGE.get());
      ResourceLocation spellID = ResourceLocationArgument.getId(c, "spell");
      Spell spell = RootsRegistries.SPELLS.get(spellID);
      if (spell == null) {
        c.getSource().sendFailure(Component.translatable("roots.commands.staff.spell_not_found", spellID.toString()));
        return 0;
      }
      if (!grants.hasSpell(spell)) {
        c.getSource()
            .sendFailure(Component.translatable("roots.commands.library.remove.failure", spell.getStyledName()));
        return 0;
      }
      grants.removeSpell(player, spell);
      c.getSource()
          .sendSuccess(() -> Component.translatable("roots.commands.library.remove.success", spell.getStyledName()), false);
      return 1;
    }))).then(Commands.literal("clear").executes(c -> {
      if (!c.getSource().isPlayer()) {
        c.getSource().sendFailure(Component.translatable("roots.commands.library.no_player"));
        return 0;
      }
      ServerPlayer player = c.getSource().getPlayerOrException();
      GrantStorage grants = player.getData(ModAttachments.GRANT_STORAGE.get());
      if (grants.clearSpells(player)) {
        c.getSource().sendSuccess(() -> Component.translatable("roots.commands.library.clear.success"), false);
      } else {
        c.getSource().sendFailure(Component.translatable("roots.commands.library.clear.failure"));
      }
      return 1;
    })).then(Commands.literal("list").executes(c -> {
      if (!c.getSource().isPlayer()) {
        c.getSource().sendFailure(Component.translatable("roots.commands.library.no_player"));
        return 0;
      }
      ServerPlayer player = c.getSource().getPlayerOrException();
      GrantStorage grants = player.getData(ModAttachments.GRANT_STORAGE.get());
      Set<Spell> spells = grants.getSpells();
      if (spells.isEmpty()) {
        c.getSource().sendFailure(Component.translatable("roots.commands.library.list.empty"));
        return 0;
      }
      for (Spell spell : spells) {
        c.getSource()
            .sendSuccess(() -> Component.translatable("roots.commands.library.list.entry", spell.getStyledName()), false);
      }
      return 1;
    })));
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
          staff.set(ModAttachments.SPELL_STORAGE, storage.setSpell(i, spell));
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
    builder.then(Commands.literal("pyre").executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.pyre.usage"), false);
      return 1;
    }).then(suggestRitualRecipes().executes(c -> {
      ResourceLocation recipeId = ResourceLocationArgument.getId(c, "recipe");
      RecipeHolder<PyreRecipe> recipe = ResolvedRecipes.PYRE.getRecipe(c.getSource().getLevel(), recipeId);
      if (recipe == null) {
        c.getSource()
            .sendFailure(Component.translatable("roots.commands.pyre.recipe_not_found", recipeId.toString()));
        return 1;
      }

      if (c.getSource().getPlayer() == null) {
        c.getSource().sendFailure(Component.translatable("roots.commands.pyre.no_player"));
        return 1;
      }

      // Create a pyre
      BlockPos pos = BlockPos.containing(c.getSource().getPosition());
      Level level = c.getSource().getLevel();

      BlockState state = level.getBlockState(pos);

      if (!state.isAir() && !state.canBeReplaced()) {
        c.getSource().sendFailure(Component.translatable("roots.commands.pyre.no_space"));
        return 1;
      }

      if (level.getFluidState(pos).is(FluidTags.WATER) && level.getFluidState(pos).isSource()) {
        level.setBlock(pos, ModBlocks.PYRE.get().defaultBlockState().setValue(PyreBlock.WATERLOGGED, true), 3);
      } else {
        level.setBlock(pos, ModBlocks.PYRE.get().defaultBlockState(), 3);
      }
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
      List<ILevelCondition> conditions = recipe.value().getLevelConditions();
      for (int i = 0; i < conditions.size(); i++) {
        ILevelCondition condition = conditions.get(i);
        if (!condition.getRepresentation().place(level, pos.relative(Direction.NORTH, i + 1))) {
          c.getSource()
              .sendFailure(Component.translatable("roots.commands.pyre.failed_condition", condition.getNameComponent()));
          return 1;
        }
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
      RecipeHolder<PyreRecipe> recipe = ResolvedRecipes.PYRE.findRecipe(c.getSource().getLevel(), ritual);

      if (recipe == null) {
        c.getSource()
            .sendFailure(Component.translatable("roots.commands.ritual.recipe_not_found", ritualId.toString()));
        return 1;
      }

      // Create a pyre
      BlockPos pos = BlockPos.containing(c.getSource().getPosition());
      Level level = c.getSource().getLevel();

      BlockState state = level.getBlockState(pos);

      if (!state.isAir() && !state.canBeReplaced()) {
        c.getSource().sendFailure(Component.translatable("roots.commands.ritual.no_space"));
        return 1;
      }

      if (level.getFluidState(pos).is(FluidTags.WATER) && level.getFluidState(pos).isSource()) {
        level.setBlock(pos, ModBlocks.PYRE.get().defaultBlockState().setValue(PyreBlock.WATERLOGGED, true), 3);
      } else {
        level.setBlock(pos, ModBlocks.PYRE.get().defaultBlockState(), 3);
      }
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
      List<ILevelCondition> conditions = recipe.value().getLevelConditions();
      for (int i = 0; i < conditions.size(); i++) {
        ILevelCondition condition = conditions.get(i);
        if (!condition.getRepresentation().place(level, pos.relative(Direction.NORTH, i + 1))) {
          c.getSource()
              .sendFailure(Component.translatable("roots.commands.ritual.failed_condition", condition.getNameComponent()));
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
    }).then(Commands.argument("rank", IntegerArgumentType.integer(1)).executes(c -> {
      int rank = IntegerArgumentType.getInteger(c, "rank");
      AABB bounds = new AABB(-15, -15, -15, 15, 15, 15).move(c.getSource().getPosition());
      List<BlockPos> positions = BlockPos.betweenClosedStream(bounds).map(BlockPos::immutable).toList();
      Level level = c.getSource().getLevel();
      for (BlockPos pos : positions) {
        BlockState stateAt = level.getBlockState(pos);
        if (stateAt.is(RootsTags.Blocks.GROVE_STONES) && stateAt.hasProperty(GroveStoneBlock.RANK)) {
          level.setBlock(pos, stateAt.setValue(GroveStoneBlock.RANK, rank).setValue(GroveStoneBlock.ACTIVE, true), 3);
        }
      }
      return 1;
    })));
    builder.then(Commands.literal("alerts").executes(c -> {
      if (c.getSource().isPlayer()) {
        Object2DoubleOpenHashMap<Herb> totals = new Object2DoubleOpenHashMap<>();
        for (Map.Entry<ResourceKey<Herb>, Herb> herb : RootsRegistries.HERBS.entrySet()) {
          totals.put(herb.getValue(), 64.165);
        }
        RootsAPI.getInstance().syncHerbs(c.getSource().getPlayer(), totals);
        c.getSource().sendSuccess(() -> Component.translatable("roots.commands.alerts.synced", c.getSource()
            .getDisplayName()), false);
        return 1;
      } else {
        c.getSource().sendFailure(Component.translatable("roots.commands.alerts.no_player"));
        return 0;
      }
    }));
    builder.then(Commands.literal("horse").executes(c -> {
          if (c.getSource().isPlayer()) {
            ServerPlayer player = c.getSource().getPlayerOrException();
            Horse horse = EntityType.HORSE.create(player.level());
            if (horse != null) {
              horse.setTamed(true);
              horse.setOwnerUUID(player.getUUID());
              horse.setPos(player.getX(), player.getY(), player.getZ());
              horse.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(0.9);
              horse.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.45);
              horse.equipSaddle(new ItemStack(Items.SADDLE), player.getSoundSource());
              horse.setCustomName(Component.literal(player.getName().getString()).append(Component.literal("'s horse")));
              player.level().addFreshEntity(horse);
            }
          }
          return 1;
        }
    ));
    builder.then(Commands.literal("reputation").executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.reputation.usage"), false);
      return 1;
    }).then(Commands.argument("player", EntityArgument.player()).executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.reputation.usage"), false);
      return 1;
    }).then(suggestGroves().executes(c -> {
      ResourceLocation groveId = ResourceLocationArgument.getId(c, "grove");
      Grove grove = RootsRegistries.GROVES.get(groveId);
      ServerPlayer player = EntityArgument.getPlayer(c, "player");
      if (grove == null) {
        c.getSource()
            .sendFailure(Component.translatable("roots.commands.reputation.grove_not_found", groveId.toString()));
        return 1;
      }

      ReputationStorage storage = player.getData(ModAttachments.REPUTATION_STORAGE);
      if (storage == null) {
        c.getSource().sendFailure(Component.translatable("roots.commands.reputation.no_reputation_storage"));
        return 1;
      }

      int reputation = storage.getReputation(grove);
      int rank = storage.getRank(grove);
      c.getSource()
          .sendSuccess(() -> Component.translatable("roots.commands.reputation.current_reputation", grove.getName(), rank, reputation), false);
      return 1;
    }).then(Commands.literal("add").executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.reputation.add.usage"), false);
      return 1;
    }).then(Commands.argument("amount", IntegerArgumentType.integer()).executes(c -> {
      ResourceLocation groveId = ResourceLocationArgument.getId(c, "grove");
      Grove grove = RootsRegistries.GROVES.get(groveId);
      ServerPlayer player = EntityArgument.getPlayer(c, "player");
      if (grove == null) {
        c.getSource()
            .sendFailure(Component.translatable("roots.commands.reputation.grove_not_found", groveId.toString()));
        return 1;
      }
      ReputationStorage storage = player.getData(ModAttachments.REPUTATION_STORAGE);
      if (storage == null) {
        c.getSource().sendFailure(Component.translatable("roots.commands.reputation.no_reputation_storage"));
        return 1;
      }
      int amount = IntegerArgumentType.getInteger(c, "amount");
      storage.increaseReputation(grove, amount);
      PacketDistributor.sendToPlayer(player, new ClientboundReputationSyncPacket(storage));
      c.getSource()
          .sendSuccess(() -> Component.translatable("roots.commands.reputation.add", player.getDisplayName(), grove.getStyledName(), amount, storage.getRank(grove), storage.getReputation(grove)), false);
      return 1;
    }))).then(Commands.literal("remove").executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.reputation.remove.usage"), false);
      return 1;
    }).then(Commands.argument("amount", IntegerArgumentType.integer()).executes(c -> {
      ResourceLocation groveId = ResourceLocationArgument.getId(c, "grove");
      Grove grove = RootsRegistries.GROVES.get(groveId);
      ServerPlayer player = EntityArgument.getPlayer(c, "player");
      if (grove == null) {
        c.getSource()
            .sendFailure(Component.translatable("roots.commands.reputation.grove_not_found", groveId.toString()));
        return 1;
      }
      ReputationStorage storage = player.getData(ModAttachments.REPUTATION_STORAGE);
      if (storage == null) {
        c.getSource().sendFailure(Component.translatable("roots.commands.reputation.no_reputation_storage"));
        return 1;
      }
      int amount = IntegerArgumentType.getInteger(c, "amount");
      storage.decreaseReputation(grove, amount);
      PacketDistributor.sendToPlayer(player, new ClientboundReputationSyncPacket(storage));
      c.getSource()
          .sendSuccess(() -> Component.translatable("roots.commands.reputation.remove", player.getDisplayName(), grove.getStyledName(), amount, storage.getRank(grove), storage.getReputation(grove)), false);
      return 1;
    }))).then(Commands.literal("set").executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("roots.commands.reputation.set.usage"), false);
      return 1;
    }).then(Commands.argument("amount", IntegerArgumentType.integer()).executes(c -> {
      ResourceLocation groveId = ResourceLocationArgument.getId(c, "grove");
      Grove grove = RootsRegistries.GROVES.get(groveId);
      ServerPlayer player = EntityArgument.getPlayer(c, "player");
      if (grove == null) {
        c.getSource()
            .sendFailure(Component.translatable("roots.commands.reputation.grove_not_found", groveId.toString()));
        return 1;
      }
      ReputationStorage storage = player.getData(ModAttachments.REPUTATION_STORAGE);
      if (storage == null) {
        c.getSource().sendFailure(Component.translatable("roots.commands.reputation.no_reputation_storage"));
        return 1;
      }
      int amount = IntegerArgumentType.getInteger(c, "amount");
      storage.setReputation(grove, amount);
      PacketDistributor.sendToPlayer(player, new ClientboundReputationSyncPacket(storage));
      c.getSource()
          .sendSuccess(() -> Component.translatable("roots.commands.reputation.set", player.getDisplayName(), grove.getStyledName(), storage.getRank(grove), amount), false);
      return 1;
    }))))));
    return builder;
  }
}
