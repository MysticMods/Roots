package epicsquid.mysticallib.hax;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.BlockPartFace;
import net.minecraft.client.renderer.model.BlockPartRotation;

public class BPDeserializer implements JsonDeserializer<net.minecraft.client.renderer.model.BlockPart> {
  @Override
  public BlockPart deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
    JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
    Vector3f vector3f = this.parsePositionFrom(jsonobject);
    Vector3f vector3f1 = this.parsePositionTo(jsonobject);
    BlockPartRotation blockpartrotation = this.parseRotation(jsonobject);
    Map<Direction, net.minecraft.client.renderer.model.BlockPartFace> map = this.parseFacesCheck(p_deserialize_3_, jsonobject);

    if (jsonobject.has("shade") && !JSONUtils.isBoolean(jsonobject, "shade")) {
      throw new JsonParseException("Expected shade to be a Boolean");
    } else {
      boolean flag = JSONUtils.getBoolean(jsonobject, "shade", true);
      return new net.minecraft.client.renderer.model.BlockPart(vector3f, vector3f1, map, blockpartrotation, flag);
    }
  }

  @Nullable
  private BlockPartRotation parseRotation(JsonObject object) {
    BlockPartRotation blockpartrotation = null;

    if (object.has("rotation")) {
      JsonObject jsonobject = JSONUtils.getJsonObject(object, "rotation");
      Vector3f vector3f = this.parsePosition(jsonobject, "origin");
      vector3f.scale(0.0625F);
      Axis enumfacing$axis = this.parseAxis(jsonobject);
      float f = this.parseAngle(jsonobject);
      boolean flag = JSONUtils.getBoolean(jsonobject, "rescale", false);
      blockpartrotation = new BlockPartRotation(vector3f, enumfacing$axis, f, flag);
    }

    return blockpartrotation;
  }

  private float parseAngle(JsonObject object) {
    float f = JSONUtils.getFloat(object, "angle");

    return f;
  }

  private Axis parseAxis(JsonObject object) {
    String s = JSONUtils.getString(object, "axis");
    Axis enumfacing$axis = Axis.byName(s.toLowerCase(Locale.ROOT));

    if (enumfacing$axis == null) {
      throw new JsonParseException("Invalid rotation axis: " + s);
    } else {
      return enumfacing$axis;
    }
  }

  private Map<Direction, BlockPartFace> parseFacesCheck(JsonDeserializationContext deserializationContext, JsonObject object) {
    Map<Direction, net.minecraft.client.renderer.model.BlockPartFace> map = this.parseFaces(deserializationContext, object);

    if (map.isEmpty()) {
      throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
    } else {
      return map;
    }
  }

  private Map<Direction, BlockPartFace> parseFaces(JsonDeserializationContext deserializationContext, JsonObject object) {
    Map<Direction, BlockPartFace> map = Maps.newEnumMap(Direction.class);
    JsonObject jsonobject = JSONUtils.getJsonObject(object, "faces");

    for (Entry<String, JsonElement> entry : jsonobject.entrySet()) {
      Direction enumfacing = this.parseEnumFacing(entry.getKey());
      map.put(enumfacing, deserializationContext.deserialize(entry.getValue(), BlockPartFace.class));
    }

    return map;
  }

  private Direction parseEnumFacing(String name) {
    Direction enumfacing = Direction.byName(name);

    if (enumfacing == null) {
      throw new JsonParseException("Unknown facing: " + name);
    } else {
      return enumfacing;
    }
  }

  private Vector3f parsePositionTo(JsonObject object) {
    Vector3f vector3f = this.parsePosition(object, "to");

    return vector3f;
  }

  private Vector3f parsePositionFrom(JsonObject object) {
    Vector3f vector3f = this.parsePosition(object, "from");

    return vector3f;
  }

  private Vector3f parsePosition(JsonObject object, String memberName) {
    JsonArray jsonarray = JSONUtils.getJsonArray(object, memberName);

    if (jsonarray.size() != 3) {
      throw new JsonParseException("Expected 3 " + memberName + " values, found: " + jsonarray.size());
    } else {
      float[] afloat = new float[3];

      for (int i = 0; i < afloat.length; ++i) {
        afloat[i] = JSONUtils.getFloat(jsonarray.get(i), memberName + "[" + i + "]");
      }

      return new Vector3f(afloat[0], afloat[1], afloat[2]);
    }
  }
}
