package mysticmods.roots.gen;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistry;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompatTagsProvider<T> implements DataProvider, RegistrateProvider {
  private final AbstractRegistrate<?> owner;
  private final ProviderType<? extends CompatTagsProvider<T>> type;
  private final String name;
  private static final Logger LOGGER = LogUtils.getLogger();
  protected final DataGenerator.PathProvider pathProvider;
  protected final ForgeRegistry<T> registry;
  protected final Map<ResourceLocation, TagBuilder> builders = Maps.newLinkedHashMap();
  protected final String modId;
  protected final net.minecraftforge.common.data.ExistingFileHelper existingFileHelper;
  private final net.minecraftforge.common.data.ExistingFileHelper.IResourceType resourceType;
  private final net.minecraftforge.common.data.ExistingFileHelper.IResourceType elementResourceType; // FORGE: Resource type for validating required references to datapack registry elements.


  public CompatTagsProvider(AbstractRegistrate<?> owner, ProviderType<? extends CompatTagsProvider<T>> type, String name, DataGenerator pGenerator, ForgeRegistry<T> pRegistry, ExistingFileHelper existingFileHelper) {
    this.owner = owner;
    this.type = type;
    this.name = name;
    this.pathProvider = pGenerator.createPathProvider(DataGenerator.Target.DATA_PACK, TagManager.getTagDir(pRegistry.getRegistryKey()));
    this.registry = pRegistry;
    this.modId = owner.getModid();
    this.existingFileHelper = existingFileHelper;
    this.resourceType = new net.minecraftforge.common.data.ExistingFileHelper.ResourceType(net.minecraft.server.packs.PackType.SERVER_DATA, ".json", TagManager.getTagDir(pRegistry.getRegistryKey()));
    this.elementResourceType = new net.minecraftforge.common.data.ExistingFileHelper.ResourceType(net.minecraft.server.packs.PackType.SERVER_DATA, ".json", net.minecraftforge.common.ForgeHooks.prefixNamespace(pRegistry.getRegistryKey().location()));
  }

  // Forge: Allow customizing the path for a given tag or returning null
  @org.jetbrains.annotations.Nullable
  protected Path getPath(ResourceLocation id) {
    return this.pathProvider.json(id);
  }

  public void run(CachedOutput pOutput) {
    this.builders.clear();
    this.addTags();
    this.builders.forEach((p_236449_, p_236450_) -> {
      List<TagEntry> list = p_236450_.build();
      List<TagEntry> list1 = list.stream().filter((p_236444_) -> !p_236444_.verifyIfPresent(this.registry::containsKey, this.builders::containsKey)).filter(this::missing).toList();
      if (!list1.isEmpty()) {
        throw new IllegalArgumentException(String.format(Locale.ROOT, "Couldn't define tag %s as it is missing following references: %s", p_236449_, list1.stream().map(Objects::toString).collect(Collectors.joining(","))));
      } else {
        JsonElement jsonelement = TagFile.CODEC.encodeStart(JsonOps.INSTANCE, new TagFile(list, false)).getOrThrow(false, LOGGER::error);
        Path path = this.getPath(p_236449_);
        if (path == null)
          return;

        try {
          DataProvider.saveStable(pOutput, jsonelement, path);
        } catch (IOException ioexception) {
          LOGGER.error("Couldn't save tags to {}", path, ioexception);
        }

      }
    });
  }

  private boolean missing(TagEntry reference) {
    // Optional tags should not be validated

    if (reference.isRequired()) {
      return existingFileHelper == null || !existingFileHelper.exists(reference.getId(), reference.isTag() ? resourceType : elementResourceType);
    }
    return false;
  }

  public CompatTagsProvider.TagAppender<T> tag(TagKey<T> pTag) {
    TagBuilder tagbuilder = this.getOrCreateRawBuilder(pTag);
    return new CompatTagsProvider.TagAppender<>(tagbuilder, this.registry, modId);
  }

  protected TagBuilder getOrCreateRawBuilder(TagKey<T> pTag) {
    return this.builders.computeIfAbsent(pTag.location(), (p_236442_) -> {
      existingFileHelper.trackGenerated(p_236442_, resourceType);
      return TagBuilder.create();
    });
  }

  public String getName() {
    return "Tags (" + name + ")";
  }

  protected void addTags() {
    owner.genData(type, this);
  }

  @Override
  public LogicalSide getSide() {
    return LogicalSide.SERVER;
  }

  public static class TagAppender<T> {
    private final TagBuilder builder;
    public final ForgeRegistry<T> registry;
    private final String modId;

    TagAppender(TagBuilder pBuilder, ForgeRegistry<T> pRegistry, String modId) {
      this.builder = pBuilder;
      this.registry = pRegistry;
      this.modId = modId;
    }

    public CompatTagsProvider.TagAppender<T> add(T pItem) {
      this.builder.addElement(this.registry.getKey(pItem));
      return this;
    }

    @SafeVarargs
    public final CompatTagsProvider.TagAppender<T> add(ResourceKey<T>... pToAdd) {
      for (ResourceKey<T> resourcekey : pToAdd) {
        this.builder.addElement(resourcekey.location());
      }

      return this;
    }

    public CompatTagsProvider.TagAppender<T> addOptional(ResourceLocation pLocation) {
      this.builder.addOptionalElement(pLocation);
      return this;
    }

    public CompatTagsProvider.TagAppender<T> addTag(TagKey<T> pTag) {
      this.builder.addTag(pTag.location());
      return this;
    }

    public CompatTagsProvider.TagAppender<T> addOptionalTag(ResourceLocation pLocation) {
      this.builder.addOptionalTag(pLocation);
      return this;
    }

    @SafeVarargs
    public final CompatTagsProvider.TagAppender<T> add(T... pToAdd) {
      Stream.<T>of(pToAdd).map(this.registry::getKey).forEach((p_126587_) -> {
        this.builder.addElement(p_126587_);
      });
      return this;
    }

    public CompatTagsProvider.TagAppender<T> add(TagEntry tag) {
      builder.add(tag);
      return this;
    }

    public TagBuilder getInternalBuilder() {
      return builder;
    }

    public String getModID() {
      return modId;
    }

    private CompatTagsProvider.TagAppender<T> self() {
      return (CompatTagsProvider.TagAppender<T>) this;
    }

    @SuppressWarnings("unchecked")
    public CompatTagsProvider.TagAppender<T> addTags(TagKey<T>... values) {
      CompatTagsProvider.TagAppender<T> builder = self();
      for (TagKey<T> value : values) {
        builder.addTag(value);
      }
      return builder;
    }

    public CompatTagsProvider.TagAppender<T> replace() {
      return replace(true);
    }

    public CompatTagsProvider.TagAppender<T> replace(boolean value) {
      self().getInternalBuilder().replace(value);
      return self();
    }

    /**
     * Adds a registry entry to the tag json's remove list. Callable during datageneration.
     *
     * @param entry The entry to remove
     * @return The builder for chaining
     */
    public CompatTagsProvider.TagAppender<T> remove(final T entry) {
      return remove(this.self().registry.getKey(entry));
    }

    /**
     * Adds multiple registry entries to the tag json's remove list. Callable during datageneration.
     *
     * @param entries The entries to remove
     * @return The builder for chaining
     */
    @SuppressWarnings("unchecked")
    public CompatTagsProvider.TagAppender<T> remove(final T first, final T... entries) {
      this.remove(first);
      for (T entry : entries) {
        this.remove(entry);
      }
      return self();
    }

    /**
     * Adds a single element's ID to the tag json's remove list. Callable during datageneration.
     *
     * @param location The ID of the element to remove
     * @return The builder for chaining
     */
    public CompatTagsProvider.TagAppender<T> remove(final ResourceLocation location) {
      CompatTagsProvider.TagAppender<T> builder = self();
      builder.getInternalBuilder().removeElement(location, builder.getModID());
      return builder;
    }

    /**
     * Adds multiple elements' IDs to the tag json's remove list. Callable during datageneration.
     *
     * @param locations The IDs of the elements to remove
     * @return The builder for chaining
     */
    public CompatTagsProvider.TagAppender<T> remove(final ResourceLocation first, final ResourceLocation... locations) {
      this.remove(first);
      for (ResourceLocation location : locations) {
        this.remove(location);
      }
      return self();
    }

    /**
     * Adds a tag to the tag json's remove list. Callable during datageneration.
     *
     * @param tag The ID of the tag to remove
     * @return The builder for chaining
     */
    public CompatTagsProvider.TagAppender<T> remove(TagKey<T> tag) {
      CompatTagsProvider.TagAppender<T> builder = self();
      builder.getInternalBuilder().removeTag(tag.location(), builder.getModID());
      return builder;
    }

    @SuppressWarnings("unchecked")
    public CompatTagsProvider.TagAppender<T> remove(TagKey<T> first, TagKey<T>... tags) {
      this.remove(first);
      for (TagKey<T> tag : tags) {
        this.remove(tag);
      }
      return self();
    }
  }
}
