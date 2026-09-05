package mysticmods.roots.init;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.*;
import mysticmods.roots.api.datacomponent.SpellInstance;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.datamap.AugmentationInfo;
import mysticmods.roots.api.recipe.ComplexEntityType;
import mysticmods.roots.item.GramaryItem;
import mysticmods.roots.item.util.DyeableWithDefault;
import mysticmods.roots.recipe.AnimalHarvestRecipe;
import mysticmods.roots.recipe.pyre.PyrePedestalRecipe;
import mysticmods.roots.spell.mode.AOEGrowthMode;
import mysticmods.roots.spell.mode.HarvestMode;
import mysticmods.roots.util.SpatialMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ModAttachments {
  private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, RootsAPI.MODID);
  private static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RootsAPI.MODID);

  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> HAS_GEAS = ATTACHMENTS.register("has_geas", () -> AttachmentType.builder(() -> false)
      .serialize(Codec.BOOL).build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<GrantStorage>> GRANT_STORAGE = ATTACHMENTS.register("grant_storage", () -> AttachmentType.builder(GrantStorage::new)
      .serialize(GrantStorage.CODEC).copyOnDeath().build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<HerbStorage>> HERB_STORAGE = ATTACHMENTS.register("herb_storage", () -> AttachmentType.builder(() -> new HerbStorage())
      .serialize(HerbStorage.CODEC).copyOnDeath().build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<ReputationStorage>> REPUTATION_STORAGE = ATTACHMENTS.register("reputation_storage", () -> AttachmentType.builder(ReputationStorage::new)
      .serialize(ReputationStorage.CODEC).copyOnDeath().build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<CooldownStorage>> COOLDOWN_STORAGE = ATTACHMENTS.register("cooldown_storage", () -> AttachmentType.builder(CooldownStorage::new)
      .serialize(CooldownStorage.CODEC).copyOnDeath().build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<SnapshotStorage>> SNAPSHOT_STORAGE = ATTACHMENTS.register("snapshot_storage", () -> AttachmentType.builder(() -> new SnapshotStorage())
      .serialize(SnapshotStorage.CODEC).copyOnDeath().build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> RUNIC_SHEARS_ENTITY_COOLDOWN = ATTACHMENTS.register("runic_shears_entity_cooldown", ModAttachments::createIntegerAttachmentType);
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> RUNIC_SHEARS_TOKEN_COOLDOWN = ATTACHMENTS.register("runic_shears_token_cooldown", ModAttachments::createIntegerAttachmentType);
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SQUID_MILKING_COOLDOWN = ATTACHMENTS.register("squid_milking_cooldown", ModAttachments::createIntegerAttachmentType);
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<RitualInformation>> RITUAL_INFORMATION = ATTACHMENTS.register("ritual_information", () -> AttachmentType.builder(RitualInformation::new)
      .serialize(RitualInformation.CODEC).build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<SpatialMap>> GROVE_CONSUMERS = ATTACHMENTS.register("grove_consumers", () -> AttachmentType.builder(SpatialMap::new)
      .build());
  // TODO: Unused?
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<AnimalHarvestRecipe.Cache>> ANIMAL_HARVEST_RECIPE_CACHE = ATTACHMENTS.register("animal_harvest_recipe_cache", () -> AttachmentType.builder(() -> new AnimalHarvestRecipe.Cache(new ArrayList<>()))
      .serialize(AnimalHarvestRecipe.CACHE_CODEC).build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Map<Holder<Attribute>, AugmentationInfo>>> AUGMENTATION_INFO = ATTACHMENTS.register("augmentation_data", () -> AttachmentType.<Map<Holder<Attribute>, AugmentationInfo>>builder(() -> new HashMap<>())
      .serialize(AugmentationInfo.DATA_MAP_CODEC).build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<LightDrifterStorage>> DRIFTER_CLIENT_STORAGE = ATTACHMENTS.register("drifter_client_storage", () -> AttachmentType.builder(LightDrifterStorage::new)
      .build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<LightDrifterStorage>> DRIFTER_SERVER_STORAGE = ATTACHMENTS.register("drifter_server_storage", () -> AttachmentType.builder(LightDrifterStorage::new)
      .serialize(LightDrifterStorage.CODEC).copyOnDeath().build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<PyrePedestalRecipe.PyrePedestalRecipeHolder>> CACHED_PEDESTAL_RECIPE = ATTACHMENTS.register("cached_pyre_pedestal_recipe", () -> AttachmentType.builder(() -> PyrePedestalRecipe.NULL)
      .serialize(PyrePedestalRecipe.CODEC).sync(PyrePedestalRecipe.STREAM_CODEC).build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<ComplexEntityType>> CACHED_PYRE_ENTITY = ATTACHMENTS.register("cached_pyre_entity", () -> AttachmentType.builder(() -> ComplexEntityType.EMPTY)
      .serialize(ComplexEntityType.CODEC).sync(ComplexEntityType.STREAM_CODEC).build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> TARGETED_ENTITY = ATTACHMENTS.register("targeted_entity", () -> AttachmentType.builder(() -> false)
      .build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<GlobalPos>> CONTAINMENT_TETHER = ATTACHMENTS.register("containment_tether", () -> AttachmentType.builder(() -> GlobalPos.of(Level.OVERWORLD, BlockPos.ZERO) /* TODO: ??? Is this a valid default??? */)
      .serialize(GlobalPos.CODEC).sync(GlobalPos.STREAM_CODEC).build());

  private static AttachmentType<Integer> createIntegerAttachmentType() {
    return AttachmentType.builder(() -> -1).serialize(Codec.INT).build();
  }

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<SpellStorage>> SPELL_STORAGE = COMPONENTS.register("spell_storage", () -> new DataComponentType.Builder<SpellStorage>().persistent(SpellStorage.CODEC)
      .networkSynchronized(SpellStorage.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<SpellInstance>> SPELL_SLOT = COMPONENTS.register("spell_slot", () -> new DataComponentType.Builder<SpellInstance>().persistent(SpellInstance.CODEC)
      .networkSynchronized(SpellInstance.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> QUIVER_CONTENTS = COMPONENTS.register("quiver_contents", () -> new DataComponentType.Builder<ItemContainerContents>().persistent(ItemContainerContents.CODEC)
      .networkSynchronized(ItemContainerContents.STREAM_CODEC).build());
  // 9 only herb slots
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> HERB_POUCH_CONTENTS = COMPONENTS.register("herb_pouch_contents", () -> new DataComponentType.Builder<ItemContainerContents>().persistent(ItemContainerContents.CODEC)
      .networkSynchronized(ItemContainerContents.STREAM_CODEC).build());
  // 12 item slots, 6 only herb slots
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> COMPONENT_POUCH_CONTENTS = COMPONENTS.register("component_pouch_contents", () -> new DataComponentType.Builder<ItemContainerContents>().persistent(ItemContainerContents.CODEC)
      .networkSynchronized(ItemContainerContents.STREAM_CODEC).build());
  // 18 item slots, 9 only herb slots
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> APOTHECARY_POUCH_CONTENTS = COMPONENTS.register("apothecary_pouch_contents", () -> new DataComponentType.Builder<ItemContainerContents>().persistent(ItemContainerContents.CODEC)
      .networkSynchronized(ItemContainerContents.STREAM_CODEC).build());
  // 15 item slots, 15 herb slots
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> SYLVAN_POUCH_CONTENTS = COMPONENTS.register("sylvan_pouch_contents", () -> new DataComponentType.Builder<ItemContainerContents>().persistent(ItemContainerContents.CODEC)
      .networkSynchronized(ItemContainerContents.STREAM_CODEC).build());

  static {
    COMPONENTS.addAlias(RootsAPI.rl("fey_pouch_contents"), RootsAPI.rl("sylvan_pouch_contents"));
  }

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FORAGING = COMPONENTS.register("foraging", () -> new DataComponentType.Builder<Integer>().persistent(ExtraCodecs.POSITIVE_INT)
      .networkSynchronized(ByteBufCodecs.VAR_INT).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<DyeableWithDefault>> DYEABLE = COMPONENTS.register("dyeable", () -> new DataComponentType.Builder<DyeableWithDefault>().persistent(DyeableWithDefault.CODEC)
      .networkSynchronized(DyeableWithDefault.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> ITEM_UUID = COMPONENTS.register("uuid", () -> new DataComponentType.Builder<UUID>().persistent(UUIDUtil.CODEC)
      .networkSynchronized(UUIDUtil.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> BOUND_POSITION = COMPONENTS.register("bound_position", () -> new DataComponentType.Builder<BlockPos>().persistent(BlockPos.CODEC)
      .networkSynchronized(BlockPos.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> DELETABLE = COMPONENTS.register("deletable", () -> new DataComponentType.Builder<Unit>().persistent(Unit.CODEC)
      .networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> MODIFIABLE = COMPONENTS.register("modifiable", () -> new DataComponentType.Builder<Unit>().persistent(Unit.CODEC)
      .networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<QuiverRecord>> QUIVER_RECORD = COMPONENTS.register("quiver_record", () -> new DataComponentType.Builder<QuiverRecord>().persistent(QuiverRecord.CODEC)
      .networkSynchronized(QuiverRecord.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CASTING_CURRENT_SPELL = COMPONENTS.register("casting_current_spell", () -> new DataComponentType.Builder<Boolean>().persistent(Codec.BOOL)/*.networkSynchronized(ByteBufCodecs.BOOL)*/.build());

  // Modes
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<GramaryItem.GramaryMode>> GRAMARY_MODE = COMPONENTS.register("gramary_mode", () -> new DataComponentType.Builder<GramaryItem.GramaryMode>().persistent(GramaryItem.GramaryMode.CODEC)
      .networkSynchronized(GramaryItem.GramaryMode.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<AOEGrowthMode>> AOE_GROWTH_MODE = COMPONENTS.register("aoe_growth_mode", () -> new DataComponentType.Builder<AOEGrowthMode>().persistent(AOEGrowthMode.CODEC)
      .networkSynchronized(AOEGrowthMode.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<HarvestMode>> HARVEST_MODE = COMPONENTS.register("harvest_mode", () -> new DataComponentType.Builder<HarvestMode>().persistent(HarvestMode.CODEC)
      .networkSynchronized(HarvestMode.STREAM_CODEC).build());

  public static void register(IEventBus bus) {
    ATTACHMENTS.register(bus);
    COMPONENTS.register(bus);
  }
}
