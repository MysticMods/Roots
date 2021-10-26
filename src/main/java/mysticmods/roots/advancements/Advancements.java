package mysticmods.roots.advancements;

import mysticmods.roots.Roots;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import noobanidus.libs.noobutil.advancement.GenericTrigger;

public class Advancements {
  public static final ResourceLocation PACIFIST_ID = new ResourceLocation(Roots.MODID, "pacifist");
  public static final ResourceLocation ACTIVATE_ID = new ResourceLocation(Roots.MODID, "grove_activate");
  public static final ResourceLocation CRAFTING_ID = new ResourceLocation(Roots.MODID, "fey_crafting");
  public static final ResourceLocation MODIFIER_ID = new ResourceLocation(Roots.MODID, "modifier");
  /*  public static GenericTrigger<FeyCraftingRecipe> CRAFTING_TRIGGER;*/
  public static GenericTrigger<LivingDeathEvent> PACIFIST_TRIGGER;
  public static GenericTrigger<Void> GROVE_TRIGGER;
  /*  public static GenericTrigger<IModifierCore> MODIFIER_TRIGGER;*/

  public static void init() {
    PACIFIST_TRIGGER = CriteriaTriggers.register(new GenericTrigger<>(PACIFIST_ID, new KillPredicate()));
    GROVE_TRIGGER = CriteriaTriggers.register(new GenericTrigger<>(ACTIVATE_ID, new ActivatePredicate()));
/*    CRAFTING_TRIGGER = CriteriaTriggers.register(new GenericTrigger<>(CRAFTING_ID, new CraftingPredicate()));
    MODIFIER_TRIGGER = CriteriaTriggers.register(new GenericTrigger<>(MODIFIER_ID, new ModifierPredicate()));*/
  }
}
