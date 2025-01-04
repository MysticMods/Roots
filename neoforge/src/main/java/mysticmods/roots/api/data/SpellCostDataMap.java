package mysticmods.roots.api.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import java.util.List;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class SpellCostDataMap {
  public static final DataMapType<Spell, SpellCostData> SPELL_COST_DATA = DataMapType.builder(RootsAPI.rl("spell_cost_data"), RootsRegistries.Keys.SPELLS, SpellCostData.CODEC).build();

  public record SpellCostData(List<Cost> costs) {
    public static Codec<SpellCostData> CODEC = RecordCodecBuilder.create(instance -> instance.group(Cost.CODEC.listOf().fieldOf("costs").forGetter(SpellCostData::costs)).apply(instance, SpellCostData::new));
    public static StreamCodec<RegistryFriendlyByteBuf, SpellCostData> STREAM_CODEC = Cost.STREAM_CODEC.apply(ByteBufCodecs.list()).map(SpellCostData::new, SpellCostData::costs);
  }

  public static void registerDataMapTypes (RegisterDataMapTypesEvent event) {
    event.register(SPELL_COST_DATA);
  }
}
