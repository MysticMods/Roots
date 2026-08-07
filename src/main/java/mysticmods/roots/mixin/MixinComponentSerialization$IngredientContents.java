package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.content.IngredientContents;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.StringRepresentable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(ComponentSerialization.class)
public class MixinComponentSerialization$IngredientContents {
  @WrapOperation(method = "createCodec", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/ComponentSerialization;createLegacyComponentMatcher([Lnet/minecraft/util/StringRepresentable;Ljava/util/function/Function;Ljava/util/function/Function;Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;"))
  private static MapCodec<ComponentContents> roots$injectIngredientContents(StringRepresentable[] types, Function<StringRepresentable, MapCodec<? extends ComponentContents>> codecGetter, Function<ComponentContents, StringRepresentable> typeGetter, String typeFieldName, Operation<MapCodec<ComponentContents>> original) {
    List<ComponentContents.Type<?>> newTypes = Stream.of(types).map(o -> (ComponentContents.Type<?>) o)
        .collect(Collectors.toList());
    newTypes.add(IngredientContents.TYPE);
    return original.call(newTypes.toArray(ComponentContents.Type[]::new), codecGetter, typeGetter, typeFieldName);
  }
}
