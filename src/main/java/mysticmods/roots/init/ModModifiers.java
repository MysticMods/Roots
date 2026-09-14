// GENERATED FILE - DO NOT EDIT.
// Source: data/modifiers.json  ->  :generateModifiers
package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.SpellType;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.reference.SpellCosts;
import mysticmods.roots.api.reference.SpellModifiers;
import mysticmods.roots.api.registry.GroupId;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.item.TokenItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModModifiers {
  private static final DeferredRegister<SpellModifier> REGISTER = DeferredRegister.create(RootsRegistries.Keys.SPELL_MODIFIERS, RootsAPI.MODID);
  private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RootsAPI.MODID);

  public static final List<GroupId> GROUP_IDS = new ArrayList<>();

  static {
    var x = Cost.class;
    var y = CostInstance.class;
    var z = SpellType.class;
    var a = SpellCosts.class;
  }

  public static final GroupId DANDELION_WINDS_DURATION = group("dandelion_winds/duration", true);
  public static final GroupId DANDELION_WINDS_CHANCE = group("dandelion_winds/chance", true);
  public static final GroupId DANDELION_WINDS_VORTEX_COOLDOWN = group("dandelion_winds/vortex_cooldown", true);
  public static final GroupId DANDELION_WINDS_GUSTS_COOLDOWN = group("dandelion_winds/gusts_cooldown", true);
  public static final GroupId SKY_SOARER_AMPLIFIED = group("sky_soarer/amplified");
  public static final GroupId SKY_SOARER_SPEEDY = group("sky_soarer/speedy");
  public static final GroupId SHATTER_FORTUNE = group("shatter/fortune");
  public static final GroupId SHATTER_HEIGHT = group("shatter/height", true);
  public static final GroupId SHATTER_WIDTH = group("shatter/width", true);
  public static final GroupId SHATTER_DEPTH = group("shatter/depth", true);

  public static final DeferredHolder<SpellModifier, SpellModifier> ACID_CLOUD_FIRE =
      REGISTER.register("acid_cloud/fire", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.ACID_CLOUD_FIRE)
          .source(ModSpells.ACID_CLOUD)
          .condition(SpellType.Condition.SPECIFIED)
          .costs(() -> CostInstance.add(ModHerbs.INFERNO_BULB, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> ACID_CLOUD_PEACEFUL =
      REGISTER.register("acid_cloud/peaceful", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.ACID_CLOUD_PEACEFUL)
          .source(ModSpells.ACID_CLOUD)
          .costs(() -> CostInstance.add(ModHerbs.WILDROOT, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> ACID_CLOUD_KNOCKBACK =
      REGISTER.register("acid_cloud/knockback", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.ACID_CLOUD_KNOCKBACK)
          .source(ModSpells.ACID_CLOUD)
          .costs(() -> CostInstance.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> ACID_CLOUD_SLOWNESS =
      REGISTER.register("acid_cloud/slowness", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.ACID_CLOUD_SLOWNESS)
          .source(ModSpells.ACID_CLOUD)
          .condition(SpellType.Condition.SPECIFIED)
          .costs(() -> CostInstance.add(ModHerbs.DEWGONIA, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> ACID_CLOUD_TEMPORAL_MORASS =
      REGISTER.register("acid_cloud/temporal_morass", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.ACID_CLOUD_TEMPORAL_MORASS)
          .source(ModSpells.ACID_CLOUD)
          .parent(SpellModifiers.ACID_CLOUD_SLOWNESS)
          .condition(SpellType.Condition.SPECIFIED)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_DURATION_1 =
      REGISTER.register("dandelion_winds/duration_i", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_DURATION_1)
          .source(ModSpells.DANDELION_WINDS)
          .group(DANDELION_WINDS_DURATION)
          .costs(() -> CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_DURATION_2 =
      REGISTER.register("dandelion_winds/duration_ii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_DURATION_2)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_DURATION_1)
          .group(DANDELION_WINDS_DURATION)
          .costs(() -> CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_DURATION_3 =
      REGISTER.register("dandelion_winds/duration_iii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_DURATION_3)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_DURATION_2)
          .group(DANDELION_WINDS_DURATION)
          .costs(() -> CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_DURATION_4 =
      REGISTER.register("dandelion_winds/duration_iv", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_DURATION_4)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_DURATION_3)
          .group(DANDELION_WINDS_DURATION)
          .costs(() -> CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_DURATION_5 =
      REGISTER.register("dandelion_winds/duration_v", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_DURATION_5)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_DURATION_4)
          .group(DANDELION_WINDS_DURATION)
          .costs(() -> CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_CHANCE_1 =
      REGISTER.register("dandelion_winds/chance_i", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_CHANCE_1)
          .source(ModSpells.DANDELION_WINDS)
          .group(DANDELION_WINDS_CHANCE)
          .costs(() -> CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_CHANCE_2 =
      REGISTER.register("dandelion_winds/chance_ii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_CHANCE_2)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_CHANCE_1)
          .group(DANDELION_WINDS_CHANCE)
          .costs(() -> CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_CHANCE_3 =
      REGISTER.register("dandelion_winds/chance_iii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_CHANCE_3)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_CHANCE_2)
          .group(DANDELION_WINDS_CHANCE)
          .costs(() -> CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_CHANCE_4 =
      REGISTER.register("dandelion_winds/chance_iv", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_CHANCE_4)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_CHANCE_3)
          .group(DANDELION_WINDS_CHANCE)
          .costs(() -> CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_VORTEX =
      REGISTER.register("dandelion_winds/vortex", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_VORTEX)
          .source(ModSpells.DANDELION_WINDS)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_VORTEX_COOLDOWN_1 =
      REGISTER.register("dandelion_winds/vortex_cooldown_i", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_1)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_VORTEX)
          .group(DANDELION_WINDS_VORTEX_COOLDOWN)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_VORTEX_COOLDOWN_2 =
      REGISTER.register("dandelion_winds/vortex_cooldown_ii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_2)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_1)
          .group(DANDELION_WINDS_VORTEX_COOLDOWN)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_VORTEX_COOLDOWN_3 =
      REGISTER.register("dandelion_winds/vortex_cooldown_iii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_3)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_2)
          .group(DANDELION_WINDS_VORTEX_COOLDOWN)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_VORTEX_COOLDOWN_4 =
      REGISTER.register("dandelion_winds/vortex_cooldown_iv", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_4)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_3)
          .group(DANDELION_WINDS_VORTEX_COOLDOWN)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_VORTEX_COOLDOWN_5 =
      REGISTER.register("dandelion_winds/vortex_cooldown_v", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_5)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_4)
          .group(DANDELION_WINDS_VORTEX_COOLDOWN)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_GUSTS =
      REGISTER.register("dandelion_winds/gusts", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_GUSTS)
          .source(ModSpells.DANDELION_WINDS)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_GUSTS_COOLDOWN_1 =
      REGISTER.register("dandelion_winds/gusts_cooldown_i", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_1)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_GUSTS)
          .group(DANDELION_WINDS_GUSTS_COOLDOWN)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_GUSTS_COOLDOWN_2 =
      REGISTER.register("dandelion_winds/gusts_cooldown_ii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_2)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_1)
          .group(DANDELION_WINDS_GUSTS_COOLDOWN)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_GUSTS_COOLDOWN_3 =
      REGISTER.register("dandelion_winds/gusts_cooldown_iii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_3)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_2)
          .group(DANDELION_WINDS_GUSTS_COOLDOWN)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_GUSTS_COOLDOWN_4 =
      REGISTER.register("dandelion_winds/gusts_cooldown_iv", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_4)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_3)
          .group(DANDELION_WINDS_GUSTS_COOLDOWN)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_GUSTS_COOLDOWN_5 =
      REGISTER.register("dandelion_winds/gusts_cooldown_v", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_5)
          .source(ModSpells.DANDELION_WINDS)
          .parent(SpellModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_4)
          .group(DANDELION_WINDS_GUSTS_COOLDOWN)
          .costs(() -> CostInstance.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_STATUE =
      REGISTER.register("dandelion_winds/statue", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_STATUE)
          .source(ModSpells.DANDELION_WINDS)
          .costs(() -> CostInstance.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> DANDELION_WINDS_INFERNO =
      REGISTER.register("dandelion_winds/inferno", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.DANDELION_WINDS_INFERNO)
          .source(ModSpells.DANDELION_WINDS)
          .costs(() -> CostInstance.add(ModHerbs.INFERNO_BULB, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> GROWTH_INFUSION_TARGETED_GROWTH =
      REGISTER.register("growth_infusion/targeted_growth", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.GROWTH_INFUSION_TARGETED_GROWTH)
          .source(ModSpells.GROWTH_INFUSION)
          .conflicts(SpellModifiers.GROWTH_INFUSION_RAMPANT_GROWTH)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> GROWTH_INFUSION_RAMPANT_GROWTH =
      REGISTER.register("growth_infusion/rampant_growth", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.GROWTH_INFUSION_RAMPANT_GROWTH)
          .source(ModSpells.GROWTH_INFUSION)
          .conflicts(SpellModifiers.GROWTH_INFUSION_TARGETED_GROWTH)
          .costs(() -> CostInstance.of(Cost.negateBase(), Cost.add(ModHerbs.WILDEWHEET, SpellCosts.BASE_0031)))));
  public static final DeferredHolder<SpellModifier, SpellModifier> GROWTH_INFUSION_HYDRATION =
      REGISTER.register("growth_infusion/hydration", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.GROWTH_INFUSION_HYDRATION)
          .source(ModSpells.GROWTH_INFUSION)
          .condition(SpellType.Condition.SPECIFIED)
          .costs(() -> CostInstance.add(ModHerbs.DEWGONIA, SpellCosts.BASE_0063))));
  public static final DeferredHolder<SpellModifier, SpellModifier> GROWTH_INFUSION_FERTILIZER =
      REGISTER.register("growth_infusion/fertilizer", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.GROWTH_INFUSION_FERTILIZER)
          .source(ModSpells.GROWTH_INFUSION)
          .parent(SpellModifiers.GROWTH_INFUSION_TARGETED_GROWTH)
          .condition(SpellType.Condition.SPECIFIED)
          .costs(() -> CostInstance.add(ModHerbs.BAFFLECAP, SpellCosts.BASE_0500))));
  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_FRIENDLY_EARTH =
      REGISTER.register("sky_soarer/friendly_earth", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SKY_SOARER_FRIENDLY_EARTH)
          .source(ModSpells.SKY_SOARER)
          .costs(() -> CostInstance.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_AMPLIFIED_1 =
      REGISTER.register("sky_soarer/amplified_i", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SKY_SOARER_AMPLIFIED_1)
          .source(ModSpells.SKY_SOARER)
          .group(SKY_SOARER_AMPLIFIED)
          .costs(() -> CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_AMPLIFIED_2 =
      REGISTER.register("sky_soarer/amplified_ii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SKY_SOARER_AMPLIFIED_2)
          .source(ModSpells.SKY_SOARER)
          .parent(SpellModifiers.SKY_SOARER_AMPLIFIED_1)
          .group(SKY_SOARER_AMPLIFIED)
          .costs(() -> CostInstance.mult(ModHerbs.CLOUD_BERRY, SpellCosts.MULT_005))));
  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_SPEEDY_1 =
      REGISTER.register("sky_soarer/speedy_i", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SKY_SOARER_SPEEDY_1)
          .source(ModSpells.SKY_SOARER)
          .group(SKY_SOARER_SPEEDY)
          .costs(() -> CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_SPEEDY_2 =
      REGISTER.register("sky_soarer/speedy_ii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SKY_SOARER_SPEEDY_2)
          .source(ModSpells.SKY_SOARER)
          .parent(SpellModifiers.SKY_SOARER_SPEEDY_1)
          .group(SKY_SOARER_SPEEDY)
          .costs(() -> CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_MAGNETISM =
      REGISTER.register("shatter/magnetism", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_MAGNETISM)
          .source(ModSpells.SHATTER)
          .costs(() -> CostInstance.add(ModHerbs.WILDROOT, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_SILK_TOUCH =
      REGISTER.register("shatter/silk_touch", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_SILK_TOUCH)
          .source(ModSpells.SHATTER)
          .conflicts(SpellModifiers.SHATTER_FORTUNE_1, SpellModifiers.SHATTER_FORTUNE_2, SpellModifiers.SHATTER_FORTUNE_3)
          .costs(() -> CostInstance.add(ModHerbs.DEWGONIA, SpellCosts.BASE_0125))));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_FORTUNE_1 =
      REGISTER.register("shatter/fortune_i", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_FORTUNE_1)
          .source(ModSpells.SHATTER)
          .group(SHATTER_FORTUNE)
          .conflicts(SpellModifiers.SHATTER_SILK_TOUCH)
          .costs(() -> CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_FORTUNE_2 =
      REGISTER.register("shatter/fortune_ii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_FORTUNE_2)
          .source(ModSpells.SHATTER)
          .parent(SpellModifiers.SHATTER_FORTUNE_1)
          .group(SHATTER_FORTUNE)
          .conflicts(SpellModifiers.SHATTER_SILK_TOUCH)
          .costs(() -> CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_FORTUNE_3 =
      REGISTER.register("shatter/fortune_iii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_FORTUNE_3)
          .source(ModSpells.SHATTER)
          .parent(SpellModifiers.SHATTER_FORTUNE_2)
          .group(SHATTER_FORTUNE)
          .conflicts(SpellModifiers.SHATTER_SILK_TOUCH)
          .costs(() -> CostInstance.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_SMELTING =
      REGISTER.register("shatter/smelting", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_SMELTING)
          .source(ModSpells.SHATTER)
          .costs(() -> CostInstance.add(ModHerbs.INFERNO_BULB, SpellCosts.BASE_0250))));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_ADJUSTABLE =
      REGISTER.register("shatter/adjustable", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_ADJUSTABLE)
          .source(ModSpells.SHATTER)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_HEIGHT_1 =
      REGISTER.register("shatter/height_i", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_HEIGHT_1)
          .source(ModSpells.SHATTER)
          .parent(SpellModifiers.SHATTER_ADJUSTABLE)
          .group(SHATTER_HEIGHT)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_HEIGHT_2 =
      REGISTER.register("shatter/height_ii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_HEIGHT_2)
          .source(ModSpells.SHATTER)
          .parent(SpellModifiers.SHATTER_HEIGHT_1)
          .group(SHATTER_HEIGHT)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_WIDTH_1 =
      REGISTER.register("shatter/width_i", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_WIDTH_1)
          .source(ModSpells.SHATTER)
          .parent(SpellModifiers.SHATTER_ADJUSTABLE)
          .group(SHATTER_WIDTH)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_WIDTH_2 =
      REGISTER.register("shatter/width_ii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_WIDTH_2)
          .source(ModSpells.SHATTER)
          .parent(SpellModifiers.SHATTER_WIDTH_1)
          .group(SHATTER_WIDTH)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_DEPTH_1 =
      REGISTER.register("shatter/depth_i", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_DEPTH_1)
          .source(ModSpells.SHATTER)
          .parent(SpellModifiers.SHATTER_ADJUSTABLE)
          .group(SHATTER_DEPTH)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SHATTER_DEPTH_2 =
      REGISTER.register("shatter/depth_ii", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SHATTER_DEPTH_2)
          .source(ModSpells.SHATTER)
          .parent(SpellModifiers.SHATTER_DEPTH_1)
          .group(SHATTER_DEPTH)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SYLVAN_LIGHT_COLOR =
      REGISTER.register("sylvan_light/color", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SYLVAN_LIGHT_COLOR)
          .source(ModSpells.SYLVAN_LIGHT)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SYLVAN_LIGHT_WHITE =
      REGISTER.register("sylvan_light/white", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SYLVAN_LIGHT_WHITE)
          .source(ModSpells.SYLVAN_LIGHT)
          .parent(SpellModifiers.SYLVAN_LIGHT_COLOR)
          .conflicts(SpellModifiers.SYLVAN_LIGHT_ORANGE, SpellModifiers.SYLVAN_LIGHT_LIME, SpellModifiers.SYLVAN_LIGHT_PINK, SpellModifiers.SYLVAN_LIGHT_CYAN)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SYLVAN_LIGHT_ORANGE =
      REGISTER.register("sylvan_light/orange", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SYLVAN_LIGHT_ORANGE)
          .source(ModSpells.SYLVAN_LIGHT)
          .parent(SpellModifiers.SYLVAN_LIGHT_COLOR)
          .conflicts(SpellModifiers.SYLVAN_LIGHT_WHITE, SpellModifiers.SYLVAN_LIGHT_LIME, SpellModifiers.SYLVAN_LIGHT_PINK, SpellModifiers.SYLVAN_LIGHT_CYAN)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SYLVAN_LIGHT_LIME =
      REGISTER.register("sylvan_light/lime", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SYLVAN_LIGHT_LIME)
          .source(ModSpells.SYLVAN_LIGHT)
          .parent(SpellModifiers.SYLVAN_LIGHT_COLOR)
          .conflicts(SpellModifiers.SYLVAN_LIGHT_WHITE, SpellModifiers.SYLVAN_LIGHT_ORANGE, SpellModifiers.SYLVAN_LIGHT_PINK, SpellModifiers.SYLVAN_LIGHT_CYAN)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SYLVAN_LIGHT_PINK =
      REGISTER.register("sylvan_light/pink", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SYLVAN_LIGHT_PINK)
          .source(ModSpells.SYLVAN_LIGHT)
          .parent(SpellModifiers.SYLVAN_LIGHT_COLOR)
          .conflicts(SpellModifiers.SYLVAN_LIGHT_WHITE, SpellModifiers.SYLVAN_LIGHT_ORANGE, SpellModifiers.SYLVAN_LIGHT_LIME, SpellModifiers.SYLVAN_LIGHT_CYAN)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SYLVAN_LIGHT_CYAN =
      REGISTER.register("sylvan_light/cyan", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SYLVAN_LIGHT_CYAN)
          .source(ModSpells.SYLVAN_LIGHT)
          .parent(SpellModifiers.SYLVAN_LIGHT_COLOR)
          .conflicts(SpellModifiers.SYLVAN_LIGHT_WHITE, SpellModifiers.SYLVAN_LIGHT_ORANGE, SpellModifiers.SYLVAN_LIGHT_LIME, SpellModifiers.SYLVAN_LIGHT_PINK)
          .costs(() -> CostInstance.empty())));
  public static final DeferredHolder<SpellModifier, SpellModifier> SYLVAN_LIGHT_DECAYING =
      REGISTER.register("sylvan_light/decaying", () -> new SpellModifier(new SpellModifier.Properties(SpellModifiers.SYLVAN_LIGHT_DECAYING)
          .source(ModSpells.SYLVAN_LIGHT)
          .costs(() -> CostInstance.add(ModHerbs.STALICRIPE, SpellCosts.COMPLEX_0625))));

  static {
    REGISTER.addAlias(RootsAPI.rl("sky_soarer/amplified_1"), RootsAPI.rl("sky_soarer/amplified_i"));
    ITEMS.addAlias(RootsAPI.rl("sky_soarer/amplified_1"), RootsAPI.rl("sky_soarer/amplified_i"));
    REGISTER.addAlias(RootsAPI.rl("sky_soarer/amplified_2"), RootsAPI.rl("sky_soarer/amplified_ii"));
    ITEMS.addAlias(RootsAPI.rl("sky_soarer/amplified_2"), RootsAPI.rl("sky_soarer/amplified_ii"));
    REGISTER.addAlias(RootsAPI.rl("sky_soarer/speedy_1"), RootsAPI.rl("sky_soarer/speedy_i"));
    ITEMS.addAlias(RootsAPI.rl("sky_soarer/speedy_1"), RootsAPI.rl("sky_soarer/speedy_i"));
    REGISTER.addAlias(RootsAPI.rl("sky_soarer/speedy_2"), RootsAPI.rl("sky_soarer/speedy_ii"));
    ITEMS.addAlias(RootsAPI.rl("sky_soarer/speedy_2"), RootsAPI.rl("sky_soarer/speedy_ii"));

    modifier(ITEMS, ModModifiers.ACID_CLOUD_FIRE);
    modifier(ITEMS, ModModifiers.ACID_CLOUD_PEACEFUL);
    modifier(ITEMS, ModModifiers.ACID_CLOUD_KNOCKBACK);
    modifier(ITEMS, ModModifiers.ACID_CLOUD_SLOWNESS);
    modifier(ITEMS, ModModifiers.ACID_CLOUD_TEMPORAL_MORASS);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_DURATION_1);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_DURATION_2);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_DURATION_3);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_DURATION_4);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_DURATION_5);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_CHANCE_1);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_CHANCE_2);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_CHANCE_3);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_CHANCE_4);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_VORTEX);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_1);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_2);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_3);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_4);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_5);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_GUSTS);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_1);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_2);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_3);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_4);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_5);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_STATUE);
    modifier(ITEMS, ModModifiers.DANDELION_WINDS_INFERNO);
    modifier(ITEMS, ModModifiers.GROWTH_INFUSION_TARGETED_GROWTH);
    modifier(ITEMS, ModModifiers.GROWTH_INFUSION_RAMPANT_GROWTH);
    modifier(ITEMS, ModModifiers.GROWTH_INFUSION_HYDRATION);
    modifier(ITEMS, ModModifiers.GROWTH_INFUSION_FERTILIZER);
    modifier(ITEMS, ModModifiers.SKY_SOARER_FRIENDLY_EARTH);
    modifier(ITEMS, ModModifiers.SKY_SOARER_AMPLIFIED_1);
    modifier(ITEMS, ModModifiers.SKY_SOARER_AMPLIFIED_2);
    modifier(ITEMS, ModModifiers.SKY_SOARER_SPEEDY_1);
    modifier(ITEMS, ModModifiers.SKY_SOARER_SPEEDY_2);
    modifier(ITEMS, ModModifiers.SHATTER_MAGNETISM);
    modifier(ITEMS, ModModifiers.SHATTER_SILK_TOUCH);
    modifier(ITEMS, ModModifiers.SHATTER_FORTUNE_1);
    modifier(ITEMS, ModModifiers.SHATTER_FORTUNE_2);
    modifier(ITEMS, ModModifiers.SHATTER_FORTUNE_3);
    modifier(ITEMS, ModModifiers.SHATTER_SMELTING);
    modifier(ITEMS, ModModifiers.SHATTER_ADJUSTABLE);
    modifier(ITEMS, ModModifiers.SHATTER_HEIGHT_1);
    modifier(ITEMS, ModModifiers.SHATTER_HEIGHT_2);
    modifier(ITEMS, ModModifiers.SHATTER_WIDTH_1);
    modifier(ITEMS, ModModifiers.SHATTER_WIDTH_2);
    modifier(ITEMS, ModModifiers.SHATTER_DEPTH_1);
    modifier(ITEMS, ModModifiers.SHATTER_DEPTH_2);
    modifier(ITEMS, ModModifiers.SYLVAN_LIGHT_COLOR);
    modifier(ITEMS, ModModifiers.SYLVAN_LIGHT_WHITE);
    modifier(ITEMS, ModModifiers.SYLVAN_LIGHT_ORANGE);
    modifier(ITEMS, ModModifiers.SYLVAN_LIGHT_LIME);
    modifier(ITEMS, ModModifiers.SYLVAN_LIGHT_PINK);
    modifier(ITEMS, ModModifiers.SYLVAN_LIGHT_CYAN);
    modifier(ITEMS, ModModifiers.SYLVAN_LIGHT_DECAYING);
  }

  private static TokenItem.SpellModifierTokenItem modifier(Holder<SpellModifier> modifier) {
    return new TokenItem.SpellModifierTokenItem(modifier.getKey(), new Item.Properties().stacksTo(1));
  }

  private static DeferredHolder<Item, TokenItem.SpellModifierTokenItem> modifier(DeferredRegister.Items reg, Holder<SpellModifier> modifier) {
    return reg.register(modifier.getKey().location().getPath(), () -> modifier(modifier));
  }

  public static GroupId group(String name) {
    return group(name, false);
  }

  public static GroupId group(String name, boolean useGroupDescription) {
    var id = new GroupId(name, useGroupDescription);
    GROUP_IDS.add(id);
    return id;
  }

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
    ITEMS.register(bus);
  }

  public static void noop() {
  }
}