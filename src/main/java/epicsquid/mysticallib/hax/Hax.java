package epicsquid.mysticallib.hax;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.google.gson.GsonBuilder;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@SuppressWarnings("deprecation")
public class Hax {

  public static Field bakedQuadFace, bakedQuadDiffuse, bakedQuadTint;

  public static Field field_ModelBakery_blockModelShapes;

  public static void init() throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
    Field f = ObfuscationReflectionHelper.findField(ModelBlock.class, "field_178319_a");
    f.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);

    f.set(null,
        (new GsonBuilder()).registerTypeAdapter(ModelBlock.class, new ModelBlock.Deserializer()).registerTypeAdapter(BlockPart.class, new BPDeserializer())
            .registerTypeAdapter(BlockPartFace.class, new BPFDeserializer()).registerTypeAdapter(BlockFaceUV.class, new BFUVDeserializer())
            .registerTypeAdapter(ItemTransformVec3f.class, new ITV3FDeserializer()).registerTypeAdapter(ItemCameraTransforms.class, new ICTDeserializer())
            .registerTypeAdapter(ItemOverride.class, new IODeserializer()).create());
    field_ModelBakery_blockModelShapes = ObfuscationReflectionHelper.findField(ModelBakery.class, "field_177610_k");
    field_ModelBakery_blockModelShapes.setAccessible(true);

    bakedQuadFace = ObfuscationReflectionHelper.findField(BakedQuad.class, "field_178214_c");
    bakedQuadFace.setAccessible(true);
    bakedQuadTint = ObfuscationReflectionHelper.findField(BakedQuad.class, "field_178213_b");
    bakedQuadTint.setAccessible(true);
    bakedQuadDiffuse = ObfuscationReflectionHelper.findField(BakedQuad.class, "applyDiffuseLighting");
    bakedQuadDiffuse.setAccessible(true);
  }
}
