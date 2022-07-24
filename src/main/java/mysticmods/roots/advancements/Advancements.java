package mysticmods.roots.advancements;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import noobanidus.libs.noobutil.advancement.GenericTrigger;

public class Advancements {
  public static final ResourceLocation PACIFIST_ID = new ResourceLocation(RootsAPI.MODID, "pacifist");
  public static final ResourceLocation ACTIVATE_ID = new ResourceLocation(RootsAPI.MODID, "grove_activation");
  public static final ResourceLocation CRAFTING_ID = new ResourceLocation(RootsAPI.MODID, "grove_crafting");
  public static GenericTrigger<GroveRecipe> CRAFTING_TRIGGER;
  public static GenericTrigger<LivingDeathEvent> PACIFIST_TRIGGER;
  public static GenericTrigger<Void> ACTIVATE_TRIGGER;

  public static void init() {
    PACIFIST_TRIGGER = CriteriaTriggers.register(new GenericTrigger<>(PACIFIST_ID, new KillPredicate()));
    ACTIVATE_TRIGGER = CriteriaTriggers.register(new GenericTrigger<>(ACTIVATE_ID, new ActivatePredicate()));
    CRAFTING_TRIGGER = CriteriaTriggers.register(new GenericTrigger<>(CRAFTING_ID, new CraftingPredicate()));
  }
}
