package mysticmods.roots.api.test.world;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.*;
import java.util.function.Predicate;

// Derived from https://github.com/thiakil/MCCodecStuff/blob/main/src/main/java/com/thiakil/codecs/blockstate/PartialBlockState.java
public record PartialBlockState(Block block,
                                Map<String, Property.Value<?>> propertyMap) implements Predicate<BlockState> {
  public static Codec<PartialBlockState> CODEC = BuiltInRegistries.BLOCK.byNameCodec()
      .dispatch("block",//dispatch based on the value of "block" (resource location)
          pred -> pred.block,//get if from the block field
          blockType -> blockPropertiesOf(blockType)//generate a property map codec from the block
              .xmap(propMap -> new PartialBlockState(blockType, propMap), pred -> pred.propertyMap)//turn the map and the block type into a predicate
              .optionalFieldOf("properties") //wrap it in a MapCodec, so we can set the value's key
              .xmap(//decide if it's empty or not
                  op -> op.orElseGet(() -> new PartialBlockState(blockType, Collections.emptyMap())),//unwrap optional, constructing a bare predicate when empty
                  pred -> pred.propertyMap.isEmpty() ? Optional.empty() : Optional.of(pred) //wrap in an optional, handling empty props
              )//package it back up in a regular codec
      );
  public static StreamCodec<RegistryFriendlyByteBuf, PartialBlockState> STREAM_CODEC = StreamCodec.of(PartialBlockState::toNetwork, PartialBlockState::fromNetwork);

  public PartialBlockState(Block block, String... properties) {
    this(block.defaultBlockState(), Arrays.asList(properties));
  }

  public PartialBlockState(BlockState template, String... properties) {
    this(template, Arrays.asList(properties));
  }

  public Block getBlock() {
    return block();
  }

  public PartialBlockState(BlockState template, List<String> properties) {
    this(template.getBlock(), new HashMap<>());
    Map<String, Property<?>> propMap = new HashMap<>();
    for (Property<?> property : template.getProperties()) {
      propMap.put(property.getName(), property);
    }
    for (String property : properties) {
      this.propertyMap.put(property, propMap.get(property).value(template));
    }
  }

  public PartialBlockState(BlockState template, Property<?>... properties) {
    this(template.getBlock(), new HashMap<>());
    for (Property<?> property : properties) {
      this.propertyMap.put(property.getName(), property.value(template));
    }
  }

  @Override
  public boolean test(BlockState blockState) {
    if (blockState.getBlock() != this.block) {
      return false;
    }

    for (Map.Entry<String, Property.Value<?>> entry : this.propertyMap.entrySet()) {
      Property.Value<?> value = entry.getValue();
      if (!Objects.equals(value.value(), value.property().value(blockState).value())) {
        return false;
      }
    }

    return true;
  }

  public BlockState build() {
    BlockState state = block.defaultBlockState();
    for (Map.Entry<String, Property.Value<?>> entry : propertyMap.entrySet()) {
      state = uncheckedSet(entry.getValue().property(), entry.getValue().value(), state);
    }
    return state;
  }

  public PartialBlockState copy() {
    return new PartialBlockState(block, new HashMap<>(propertyMap));
  }

  public BlockState copyState(BlockState oldState) {
    return copyState(oldState, Collections.emptyList());
  }

  public BlockState copyState(BlockState oldState, List<String> toSkip) {
    BlockState state = block.defaultBlockState();
    Set<Property<?>> completed = new HashSet<>();
    for (Map.Entry<String, Property.Value<?>> entry : propertyMap.entrySet()) {
      state = uncheckedSet(entry.getValue().property(), entry.getValue().value(), state);
      completed.add(entry.getValue().property());
    }
    for (Property<?> property : oldState.getProperties()) {
      if (completed.contains(property)) {
        continue;
      }

      completed.add(property);

      if (toSkip.contains(property.getName())) {
        continue;
      }

      if (!state.hasProperty(property)) {
        continue;
      }

      state = uncheckedSet(property, oldState.getValue(property), state);
    }

    return state;
  }

  public static PartialBlockState fromNetwork(RegistryFriendlyByteBuf buffer) {
    Block incomingBlock = ByteBufCodecs.registry(Registries.BLOCK).decode(buffer);
    BlockState templateState = incomingBlock.defaultBlockState();
    Map<String, Property<?>> properties = new HashMap<>();
    for (Property<?> property : templateState.getProperties()) {
      properties.put(property.getName(), property);
    }
    int pairCount = buffer.readVarInt();
    List<String> keys = new ArrayList<>();
    for (int i = 0; i < pairCount; i++) {
      String key = buffer.readUtf();
      String value = buffer.readUtf();
      Property<?> prop = properties.get(key);
      if (prop == null) {
        throw new IllegalArgumentException("Property '" + key + "' not found on block " + incomingBlock);
      }
      Optional<? extends Comparable<?>> propValue = prop.getValue(value);
      if (propValue.isPresent()) {
        templateState = uncheckedSet(prop, propValue.get(), templateState);
      } else {
        throw new IllegalArgumentException("Value '" + value + "' not found for property '" + key + "' on block " + incomingBlock);
      }
    }
    return new PartialBlockState(templateState, keys);
  }

  public static void toNetwork(RegistryFriendlyByteBuf buffer, PartialBlockState state) {
    ByteBufCodecs.registry(Registries.BLOCK).encode(buffer, state.block);
    buffer.writeVarInt(state.propertyMap.size());
    for (Map.Entry<String, Property.Value<?>> entry : state.propertyMap.entrySet()) {
      buffer.writeUtf(entry.getKey());
      buffer.writeUtf(uncheckedValue(entry.getValue().property(), entry.getValue().value()));
    }
  }

  @SuppressWarnings("unchecked")
  public static <T extends Comparable<T>, V extends T> BlockState uncheckedSet(Property<?> property, Comparable<?> value, BlockState state) {
    return state.setValue((Property<T>) property, (V) value);
  }

  @SuppressWarnings("unchecked")
  static <T extends Comparable<T>, V extends T> String uncheckedValue(Property<?> property, Comparable<?> value) {
    return ((Property<T>) property).getName((V) value);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  // I agree, these are terrible, but I do some worse stuff in other places too
  //properties are a terrible place. Mostly copied from net.minecraft.world.level.block.state.StateDefinition.appendPropertyCodec
  static Codec<Map<String, Property.Value<?>>> blockPropertiesOf(Block block) {
    MapCodec<Map<String, Property.Value<?>>> mapcodec = MapCodec.of(Encoder.empty(), Decoder.unit(HashMap::new));
    BlockState baseState = block.defaultBlockState();
    for (Property<?> property : baseState.getProperties()) {
      mapcodec = Codec.mapPair(mapcodec, property.valueCodec().optionalFieldOf(property.getName()))
          .xmap(
              thePair -> {
                Optional<? extends Property.Value<?>> optionalProperty = thePair.getSecond();
                if (optionalProperty.isPresent()) {
                  Property.Value<?> value = optionalProperty.get();
                  thePair.getFirst().put(value.property().getName(), value);
                }
                return thePair.getFirst();
              },
              theMap -> Pair.of(theMap, (Optional) Optional.ofNullable(theMap.get(property.getName())))
          );
    }
    return mapcodec.codec();
  }
}
