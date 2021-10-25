package epicsquid.mysticallib.hax;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class IODeserializer implements JsonDeserializer<ItemOverride> {
  @Override
  public ItemOverride deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
    JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
    ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "model"));
    Map<ResourceLocation, Float> map = this.makeMapResourceValues(jsonobject);
    return new ItemOverride(resourcelocation, map);
  }

  protected Map<ResourceLocation, Float> makeMapResourceValues(JsonObject p_188025_1_) {
    Map<ResourceLocation, Float> map = Maps.<ResourceLocation, Float>newLinkedHashMap();
    JsonObject jsonobject = JsonUtils.getJsonObject(p_188025_1_, "predicate");

    for (Entry<String, JsonElement> entry : jsonobject.entrySet()) {
      map.put(new ResourceLocation(entry.getKey()), Float.valueOf(JsonUtils.getFloat(entry.getValue(), entry.getKey())));
    }

    return map;
  }
}
