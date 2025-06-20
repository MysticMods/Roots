package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.mixin.client.accessor.AccessorMixinBufferBuilder;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(ParticleEngine.class)
public class MixinParticleEngine {
  @Inject(method = "render(Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;Ljava/util/function/Predicate;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/MeshData;)V"))
  private void roots_1_21$render(LightTexture lightTexture, Camera camera, float partialTick, Frustum frustum, Predicate<ParticleRenderType> renderTypePredicate, CallbackInfo ci, @Local ParticleRenderType particlerendertype, @Local BufferBuilder bufferbuilder, @Local MeshData meshdata) {
    if (particlerendertype instanceof RootsParticleRenderTypes.RootsParticleRenderType rootsType) {
      if (rootsType.sortQuads()) {
        meshdata.sortQuads(((AccessorMixinBufferBuilder) bufferbuilder).roots_1_21$getBufferBuilder(), RenderSystem.getVertexSorting());
      }
    }
  }
}
