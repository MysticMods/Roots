package mysticmods.roots.gen.advancement;

import mysticmods.roots.advancements.PacifistTrigger;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public final class RootsAdvancementProvider implements AdvancementProvider.AdvancementGenerator {
  @Override
  public void generate(HolderLookup.Provider arg, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
    AdvancementHolder root = Advancement.Builder.advancement()
        .display(ModItems.WILDROOT.get(), Component.translatable("roots.advancements.root.title"), Component.translatable("roots.advancements.root.description"), RootsAPI.rl("textures/block/runestone.png"), AdvancementType.TASK, false, false, false)
        .addCriterion("always_true", PlayerTrigger.TriggerInstance.tick())
        .save(consumer, RootsAPI.rl("root"), existingFileHelper);

    AdvancementHolder pacifist = Advancement.Builder.advancement()
        .display(Items.COOKED_BEEF, Component.translatable("roots.advancements.pacifist.title"), Component.translatable("roots.advancements.pacifist.description"), null, AdvancementType.TASK, true, true, true)
        .addCriterion("pacifist", PacifistTrigger.pacifist()).parent(root)
        .save(consumer, RootsAPI.rl("pacifist"), existingFileHelper);
  }
}
