package mysticmods.roots.init;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.*;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.datamap.AugmentationInfo;
import mysticmods.roots.item.Dyeable;
import mysticmods.roots.item.GramaryItem;
import mysticmods.roots.recipe.AnimalHarvestRecipe;
import mysticmods.roots.util.SpatialMap;
import net.minecraft.core.BlockPos;
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
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<AnimalHarvestRecipe.Cache>> ANIMAL_HARVEST_RECIPE_CACHE = ATTACHMENTS.register("animal_harvest_recipe_cache", () -> AttachmentType.builder(() -> new AnimalHarvestRecipe.Cache(new ArrayList<>()))
      .serialize(AnimalHarvestRecipe.CACHE_CODEC).build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Map<Holder<Attribute>, AugmentationInfo>>> AUGMENTATION_INFO = ATTACHMENTS.register("augmentation_data", () -> AttachmentType.<Map<Holder<Attribute>, AugmentationInfo>>builder(() -> new HashMap<>())
      .serialize(AugmentationInfo.DATA_MAP_CODEC).build());

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<SpellStorage>> SPELL_STORAGE = COMPONENTS.register("spell_storage", () -> new DataComponentType.Builder<SpellStorage>().persistent(SpellStorage.CODEC)
      .networkSynchronized(SpellStorage.STREAM_CODEC).build());
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
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Dyeable>> DYEABLE = COMPONENTS.register("dyeable", () -> new DataComponentType.Builder<Dyeable>().persistent(Dyeable.CODEC)
      .networkSynchronized(Dyeable.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> ITEM_UUID = COMPONENTS.register("uuid", () -> new DataComponentType.Builder<UUID>().persistent(UUIDUtil.CODEC)
      .networkSynchronized(UUIDUtil.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<GramaryItem.GramaryMode>> GRAMARY_MODE = COMPONENTS.register("gramary_mode", () -> new DataComponentType.Builder<GramaryItem.GramaryMode>().persistent(GramaryItem.GramaryMode.CODEC)
      .networkSynchronized(GramaryItem.GramaryMode.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> BOUND_POSITION = COMPONENTS.register("bound_position", () -> new DataComponentType.Builder<BlockPos>().persistent(BlockPos.CODEC)
      .networkSynchronized(BlockPos.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> DELETABLE = COMPONENTS.register("deletable", () -> new DataComponentType.Builder<Unit>().persistent(Unit.CODEC)
      .networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build());

  public static final UUID DEFAULT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

  private static AttachmentType<Integer> createIntegerAttachmentType() {
    return AttachmentType.builder(() -> -1).serialize(Codec.INT).build();
  }

  public static void register(IEventBus bus) {
    ATTACHMENTS.register(bus);
    COMPONENTS.register(bus);
  }
}
