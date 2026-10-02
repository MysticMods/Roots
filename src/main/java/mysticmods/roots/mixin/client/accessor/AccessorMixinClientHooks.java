package mysticmods.roots.mixin.client.accessor;

import net.minecraft.client.gui.screens.Screen;
import net.neoforged.neoforge.client.ClientHooks;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Stack;

@SuppressWarnings("CollectionDeclaredAsConcreteClass")
@Mixin(ClientHooks.class)
public interface AccessorMixinClientHooks {
  @Accessor("guiLayers")
  static Stack<Screen> roots$getGuiLayers () {
    throw new NotImplementedException();
  }
}
