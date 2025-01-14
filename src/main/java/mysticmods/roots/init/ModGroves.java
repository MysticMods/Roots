package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.data.GroveData;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.reference.Groves;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.ChatFormatting;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModGroves {
  private static final DeferredRegister<Grove> REGISTER = DeferredRegister.create(RootsRegistries.Keys.GROVES, RootsAPI.MODID);

  public static final DeferredHolder<Grove, Grove> PRIMAL = REGISTER.register("primal", () -> new Grove(ChatFormatting.GOLD));
  public static final DeferredHolder<Grove, Grove> FAIRY = REGISTER.register("fairy", () -> new Grove(ChatFormatting.LIGHT_PURPLE));
  public static final DeferredHolder<Grove, Grove> TWILIGHT = REGISTER.register("twilight", () -> new Grove(ChatFormatting.DARK_PURPLE));
  public static final DeferredHolder<Grove, Grove> FUNGAL = REGISTER.register("fungal", () -> new Grove(ChatFormatting.DARK_AQUA));
  public static final DeferredHolder<Grove, Grove> SPROUT = REGISTER.register("sprout", () -> new Grove(ChatFormatting.GREEN));
  public static final DeferredHolder<Grove, Grove> ELEMENTAL = REGISTER.register("elemental", () -> new Grove(ChatFormatting.DARK_RED));
  public static final DeferredHolder<Grove, Grove> WILD = REGISTER.register("wild", () -> new Grove(ChatFormatting.YELLOW));

  public static final List<GroveData.GroveInitRecord> RECORDS = List.of(
      new GroveData.GroveInitRecord(Groves.PRIMAL, RootsTags.Herbs.PRIMAL, RootsTags.Groves.PRIMAL_ALIGNED, RootsTags.Groves.PRIMAL_OPPOSED, RootsTags.Spells.PRIMAL_ALIGNED, RootsTags.Spells.PRIMAL_OPPOSED, RootsTags.Rituals.PRIMAL_ALIGNED, RootsTags.Rituals.PRIMAL_OPPOSED),
      new GroveData.GroveInitRecord(Groves.FAIRY, RootsTags.Herbs.FAIRY, RootsTags.Groves.FAIRY_ALIGNED, RootsTags.Groves.FAIRY_OPPOSED, RootsTags.Spells.FAIRY_ALIGNED, RootsTags.Spells.FAIRY_OPPOSED, RootsTags.Rituals.FAIRY_ALIGNED, RootsTags.Rituals.FAIRY_OPPOSED),
      new GroveData.GroveInitRecord(Groves.TWILIGHT, RootsTags.Herbs.TWILIGHT, RootsTags.Groves.TWILIGHT_ALIGNED, RootsTags.Groves.TWILIGHT_OPPOSED, RootsTags.Spells.TWILIGHT_ALIGNED, RootsTags.Spells.TWILIGHT_OPPOSED, RootsTags.Rituals.TWILIGHT_ALIGNED, RootsTags.Rituals.TWILIGHT_OPPOSED),
      new GroveData.GroveInitRecord(Groves.FUNGAL, RootsTags.Herbs.FUNGAL, RootsTags.Groves.FUNGAL_ALIGNED, RootsTags.Groves.FUNGAL_OPPOSED, RootsTags.Spells.FUNGAL_ALIGNED, RootsTags.Spells.FUNGAL_OPPOSED, RootsTags.Rituals.FUNGAL_ALIGNED, RootsTags.Rituals.FUNGAL_OPPOSED),
      new GroveData.GroveInitRecord(Groves.SPROUT, RootsTags.Herbs.SPROUT, RootsTags.Groves.SPROUT_ALIGNED, RootsTags.Groves.SPROUT_OPPOSED, RootsTags.Spells.SPROUT_ALIGNED, RootsTags.Spells.SPROUT_OPPOSED, RootsTags.Rituals.SPROUT_ALIGNED, RootsTags.Rituals.SPROUT_OPPOSED),
      new GroveData.GroveInitRecord(Groves.ELEMENTAL, RootsTags.Herbs.ELEMENTAL, RootsTags.Groves.ELEMENTAL_ALIGNED, RootsTags.Groves.ELEMENTAL_OPPOSED, RootsTags.Spells.ELEMENTAL_ALIGNED, RootsTags.Spells.ELEMENTAL_OPPOSED, RootsTags.Rituals.ELEMENTAL_ALIGNED, RootsTags.Rituals.ELEMENTAL_OPPOSED),
      new GroveData.GroveInitRecord(Groves.WILD, RootsTags.Herbs.WILD, RootsTags.Groves.WILD_ALIGNED, RootsTags.Groves.WILD_OPPOSED, RootsTags.Spells.WILD_ALIGNED, RootsTags.Spells.WILD_OPPOSED, RootsTags.Rituals.WILD_ALIGNED, RootsTags.Rituals.WILD_OPPOSED));

  public static void register (IEventBus bus) {
    REGISTER.register(bus);
  }

}
