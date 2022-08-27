package mysticmods.roots.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellStorage;
import mysticmods.roots.init.ModItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class RootsCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(builder(Commands.literal("roots").requires(p -> p.hasPermission(2))));
  }

  private static List<String> spellIds = null;

  private static RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> suggestSpells() {
    if (spellIds == null) {
      spellIds = Registries.SPELL_REGISTRY.get().getValues().stream().map(Spell::getKey).map(ResourceLocation::toString).collect(Collectors.toList());
    }
    return Commands.argument("spell", ResourceLocationArgument.id())
        .suggests((c, build) -> SharedSuggestionProvider.suggest(spellIds, build));
  }

  public static LiteralArgumentBuilder<CommandSourceStack> builder(LiteralArgumentBuilder<CommandSourceStack> builder) {
    builder.executes(c -> {
      c.getSource().sendSuccess(new TranslatableComponent("roots.commands.usage"), false);
      return 1;
    });
    builder.then(Commands.literal("staff").executes(c -> {
      c.getSource().sendSuccess(new TranslatableComponent("roots.commands.staff.usage"), false);
      return 1;
    }).then(suggestSpells().executes(c -> {
      ResourceLocation spellID = ResourceLocationArgument.getId(c, "spell");
      Spell spell = Registries.SPELL_REGISTRY.get().getValue(spellID);
      if (spell == null) {
        c.getSource().sendFailure(new TranslatableComponent("roots.commands.staff.spell_not_found", spellID.toString()));
        return 1;
      }
      ServerPlayer player = c.getSource().getPlayerOrException();
      boolean newStaff = false;
      ItemStack staff;
      if (player.getItemInHand(InteractionHand.MAIN_HAND).is(RootsAPI.Tags.Items.CASTING_TOOLS)) {
        staff = player.getItemInHand(InteractionHand.MAIN_HAND);
      } else {
        newStaff = true;
        staff = new ItemStack(ModItems.STAFF.get());
      }

      SpellStorage storage = SpellStorage.fromItem(staff, true);
      if (storage == null) {
        c.getSource().sendFailure(new TranslatableComponent("roots.commands.staff.no_spell_storage"));
        return 1;
      }
      storage.add(spell, spell.getModifiers());
      storage.save(staff);
      if (newStaff) {
        player.addItem(staff);
      }
      return 1;
    })));
    return builder;
  }


}
