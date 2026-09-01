package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.reference.SpellCosts;
import mysticmods.roots.api.registry.GroupId;
import mysticmods.roots.api.registry.RootsRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModModifiers {
  private static final DeferredRegister<SpellModifier> REGISTER = DeferredRegister.create(RootsRegistries.Keys.SPELL_MODIFIERS, RootsAPI.MODID);

  public static final List<GroupId> GROUP_IDS = new ArrayList<>();

  public static GroupId group(String name) {
    return group(name, false);
  }

  public static GroupId group(String name, boolean useGroupDescription) {
    var id = new GroupId(name, useGroupDescription);
    GROUP_IDS.add(id);
    return id;
  }

  public static final DeferredHolder<SpellModifier, SpellModifier> ACID_CLOUD_FIRE = REGISTER.register("acid_cloud/fire", () -> new SpellModifier(CostInstance.add(ModHerbs.INFERNO_BULB, SpellCosts.BASE_0250), ModSpells.ACID_CLOUD.getKey()));
  public static final DeferredHolder<SpellModifier, SpellModifier> ACID_CLOUD_PEACEFUL = REGISTER.register("acid_cloud/peaceful", () -> new SpellModifier(CostInstance.add(ModHerbs.WILDROOT, SpellCosts.BASE_0125), ModSpells.ACID_CLOUD.getKey()));
  public static final DeferredHolder<SpellModifier, SpellModifier> ACID_CLOUD_KNOCKBACK = REGISTER.register("acid_cloud/knockback", () -> new SpellModifier(CostInstance.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0125), ModSpells.ACID_CLOUD.getKey()));

  public static final GroupId DANDELION_WINDS_DURATION = group("dandelion_winds/duration", true);

  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_DURATION_1 = REGISTER.register("dandelion_winds/duration_i", () -> new SpellModifier(CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_DURATION));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_DURATION_2 = REGISTER.register("dandelion_winds/duration_ii", () -> new SpellModifier(CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250), ModModifiers.DANDELION_WINDS_DURATION_1.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_DURATION));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_DURATION_3 = REGISTER.register("dandelion_winds/duration_iii", () -> new SpellModifier(CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250), ModModifiers.DANDELION_WINDS_DURATION_2.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_DURATION));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_DURATION_4 = REGISTER.register("dandelion_winds/duration_iv", () -> new SpellModifier(CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250), ModModifiers.DANDELION_WINDS_DURATION_3.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_DURATION));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_DURATION_5 = REGISTER.register("dandelion_winds/duration_v", () -> new SpellModifier(CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250), ModModifiers.DANDELION_WINDS_DURATION_4.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_DURATION));

  public static final GroupId DANDELION_WINDS_CHANCE = group("dandelion_winds/chance", true);

  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_CHANCE_1 = REGISTER.register("dandelion_winds/chance_i", () -> new SpellModifier(CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_CHANCE));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_CHANCE_2 = REGISTER.register("dandelion_winds/chance_ii", () -> new SpellModifier(CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250), DANDELION_WINDS_CHANCE_1.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_CHANCE));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_CHANCE_3 = REGISTER.register("dandelion_winds/chance_iii", () -> new SpellModifier(CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250), DANDELION_WINDS_CHANCE_2.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_CHANCE));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_CHANCE_4 = REGISTER.register("dandelion_winds/chance_iv", () -> new SpellModifier(CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250), DANDELION_WINDS_CHANCE_3.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_CHANCE));

  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_VORTEX = REGISTER.register("dandelion_winds/vortex", () -> new SpellModifier(CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), ModSpells.DANDELION_WINDS.getKey()));

  public static final GroupId DANDELION_WINDS_VORTEX_COOLDOWN = group("dandelion_winds/vortex_cooldown", true);
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_VORTEX_COOLDOWN_1 = REGISTER.register("dandelion_winds/vortex_cooldown_i", () -> new SpellModifier(CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), ModModifiers.DANDELION_WINDS_VORTEX.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_VORTEX_COOLDOWN));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_VORTEX_COOLDOWN_2 = REGISTER.register("dandelion_winds/vortex_cooldown_ii", () -> new SpellModifier(CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_1.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_VORTEX_COOLDOWN));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_VORTEX_COOLDOWN_3 = REGISTER.register("dandelion_winds/vortex_cooldown_iii", () -> new SpellModifier(CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_2.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_VORTEX_COOLDOWN));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_VORTEX_COOLDOWN_4 = REGISTER.register("dandelion_winds/vortex_cooldown_iv", () -> new SpellModifier(CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_3.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_VORTEX_COOLDOWN));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_VORTEX_COOLDOWN_5 = REGISTER.register("dandelion_winds/vortex_cooldown_v", () -> new SpellModifier(CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_4.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_VORTEX_COOLDOWN));

  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_GUSTS = REGISTER.register("dandelion_winds/gusts", () -> new SpellModifier(CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), ModSpells.DANDELION_WINDS.getKey()));
  public static final GroupId DANDELION_WINDS_GUSTS_COOLDOWN = group("dandelion_winds/gusts_cooldown", true);
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_GUSTS_COOLDOWN_1 = REGISTER.register("dandelion_winds/gusts_cooldown_i", () -> new SpellModifier(CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), ModModifiers.DANDELION_WINDS_GUSTS.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_GUSTS_COOLDOWN));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_GUSTS_COOLDOWN_2 = REGISTER.register("dandelion_winds/gusts_cooldown_ii", () -> new SpellModifier(CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_1.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_GUSTS_COOLDOWN));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_GUSTS_COOLDOWN_3 = REGISTER.register("dandelion_winds/gusts_cooldown_iii", () -> new SpellModifier(CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_2.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_GUSTS_COOLDOWN));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_GUSTS_COOLDOWN_4 = REGISTER.register("dandelion_winds/gusts_cooldown_iv", () -> new SpellModifier(CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_3.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_GUSTS_COOLDOWN));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_GUSTS_COOLDOWN_5 = REGISTER.register("dandelion_winds/gusts_cooldown_v", () -> new SpellModifier(CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_4.getKey(), ModSpells.DANDELION_WINDS.getKey(), DANDELION_WINDS_GUSTS_COOLDOWN));

  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_STATUE = REGISTER.register("dandelion_winds/statue", () -> new SpellModifier(CostInstance.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0250), ModSpells.DANDELION_WINDS.getKey()));

  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_INFERNO = REGISTER.register("dandelion_winds/inferno", () -> new SpellModifier(CostInstance.add(ModHerbs.INFERNO_BULB, SpellCosts.BASE_0250), ModSpells.DANDELION_WINDS.getKey()));

  public static final DeferredHolder<SpellModifier, SpellModifier> TARGETED_GROWTH = REGISTER.register("growth_infusion/targeted_growth", () -> new SpellModifier(CostInstance.empty(), null, ModSpells.GROWTH_INFUSION.getKey(), ModModifiers.RAMPANT_GROWTH.getKey()));
  public static final DeferredHolder<SpellModifier, SpellModifier> RAMPANT_GROWTH = REGISTER.register("growth_infusion/rampant_growth", () -> new SpellModifier(CostInstance.of(Cost.negateBase(), Cost.add(ModHerbs.WILDEWHEET, SpellCosts.BASE_0031)), null, ModSpells.GROWTH_INFUSION.getKey(), ModModifiers.TARGETED_GROWTH.getKey()));

  // Sylvan Light
  // Auto-place to fill dark
  // Cast to remove nearby lights
  // Adjust brightness
  // Adjust colour

  // Geas
  // Increase quantity

  // Harvest
  // Increase radius
  // Magnetism

  // Life Drain
  // Increase damage, decrease healing
  // Increase healing, decrease damage
  // Dot
  // Peaceful

  // Petal Shell
  // Increase shells
  // Do damage/knockback when shield ends

  // Rose Thorns
  // Peaceful
  // Do damage while trapped


  // Increase duration
  // Decrease cooldown
  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_FRIENDLY_EARTH = REGISTER.register("sky_soarer/friendly_earth", () -> new SpellModifier(CostInstance.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0125), ModSpells.SKY_SOARER.getKey()));

  public static final GroupId SKY_SOARER_AMPLIFIED = group("sky_soarer/amplified");

  // TODO: Rename I, etc
  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_AMPLIFIED_1 = REGISTER.register("sky_soarer/amplified_1", () -> new SpellModifier(CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0125), ModSpells.SKY_SOARER.getKey(), SKY_SOARER_AMPLIFIED));

  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_AMPLIFIED_2 = REGISTER.register("sky_soarer/amplified_2", () -> new SpellModifier(CostInstance.mult(ModHerbs.CLOUD_BERRY, SpellCosts.MULT_005), SKY_SOARER_AMPLIFIED_1.getKey(), ModSpells.SKY_SOARER.getKey(), SKY_SOARER_AMPLIFIED));

  public static final GroupId SKY_SOARER_SPEEDY = group("sky_soarer/speedy");

  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_SPEEDY_1 = REGISTER.register("sky_soarer/speedy_1", () -> new SpellModifier(CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0125), ModSpells.SKY_SOARER.getKey(), SKY_SOARER_SPEEDY));

  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_SPEEDY_2 = REGISTER.register("sky_soarer/speedy_2", () -> new SpellModifier(CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0125), SKY_SOARER_SPEEDY_1.getKey(), ModSpells.SKY_SOARER.getKey(), SKY_SOARER_SPEEDY));

  // Shatter:
  //  Silk touch    ->
  //  Fortune I-III  \- Conflict
  //  Adjustable width --|
  //  Adjustable height  |--> Increase maximum number of blocks
  //  Adjustable depth --|    Vein mining
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_MAGNETISM = REGISTER.register("shatter/magnetism", () -> new SpellModifier(CostInstance.add(ModHerbs.WILDROOT, SpellCosts.BASE_0125), ModSpells.SHATTER.getKey()));

  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_SILK_TOUCH = REGISTER.register("shatter/silk_touch", () -> new SpellModifier(CostInstance.add(ModHerbs.DEWGONIA, SpellCosts.BASE_0125), null, ModSpells.SHATTER.getKey(), ModModifiers.SHATTER_FORTUNE_I.getKey(), ModModifiers.SHATTER_FORTUNE_II.getKey(), ModModifiers.SHATTER_FORTUNE_III.getKey()));

  public static final GroupId SHATTER_FORTUNE = group("shatter/fortune");

  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_FORTUNE_I = REGISTER.register("shatter/fortune_i", () -> new SpellModifier(CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250), null, ModSpells.SHATTER.getKey(), SHATTER_FORTUNE, ModModifiers.SHATTER_SILK_TOUCH.getKey()));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_FORTUNE_II = REGISTER.register("shatter/fortune_ii", () -> new SpellModifier(CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250), SHATTER_FORTUNE_I.getKey(), ModSpells.SHATTER.getKey(), SHATTER_FORTUNE, ModModifiers.SHATTER_SILK_TOUCH.getKey()));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_FORTUNE_III = REGISTER.register("shatter/fortune_iii", () -> new SpellModifier(CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250), SHATTER_FORTUNE_II.getKey(), ModSpells.SHATTER.getKey(), SHATTER_FORTUNE, ModModifiers.SHATTER_SILK_TOUCH.getKey()));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_SMELTING = REGISTER.register("shatter/smelting", () -> new SpellModifier(CostInstance.add(ModHerbs.INFERNO_BULB, SpellCosts.BASE_0250), null, ModSpells.SHATTER.getKey()));

  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_ADJUSTABLE = REGISTER.register("shatter/adjustable", () -> new SpellModifier(CostInstance.EMPTY, ModSpells.SHATTER.getKey()));

  public static final GroupId SHATTER_HEIGHT = group("shatter/height", true);
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_HEIGHT_I = REGISTER.register("shatter/height_i", () -> new SpellModifier(CostInstance.empty(), ModModifiers.SHATTER_ADJUSTABLE.getKey(), ModSpells.SHATTER.getKey(), SHATTER_HEIGHT));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_HEIGHT_II = REGISTER.register("shatter/height_ii", () -> new SpellModifier(CostInstance.empty(), ModModifiers.SHATTER_HEIGHT_I.getKey(), ModSpells.SHATTER.getKey(), SHATTER_HEIGHT));
  public static final GroupId SHATTER_WIDTH = group("shatter/width", true);
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_WIDTH_I = REGISTER.register("shatter/width_i", () -> new SpellModifier(CostInstance.empty(), ModModifiers.SHATTER_ADJUSTABLE.getKey(), ModSpells.SHATTER.getKey(), SHATTER_WIDTH));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_WIDTH_II = REGISTER.register("shatter/width_ii", () -> new SpellModifier(CostInstance.empty(), ModModifiers.SHATTER_WIDTH_I.getKey(), ModSpells.SHATTER.getKey(), SHATTER_WIDTH));
  public static final GroupId SHATTER_DEPTH = group("shatter/depth", true);
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_DEPTH_I = REGISTER.register("shatter/depth_i", () -> new SpellModifier(CostInstance.empty(), ModModifiers.SHATTER_ADJUSTABLE.getKey(), ModSpells.SHATTER.getKey(), SHATTER_DEPTH));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_DEPTH_II = REGISTER.register("shatter/depth_ii", () -> new SpellModifier(CostInstance.empty(), ModModifiers.SHATTER_DEPTH_I.getKey(), ModSpells.SHATTER.getKey(), SHATTER_DEPTH));

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
