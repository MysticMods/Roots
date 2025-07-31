package mysticmods.roots.mixin.client.accessor;

import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemRenderer.class)
public interface AccessorMixinItemRenderer {
  @Accessor("itemModelShaper")
  ItemModelShaper RootsGetItemModelShaper();

  @Accessor("TRIDENT_MODEL")
  ModelResourceLocation RootsGetTridentModel();

  @Accessor("TRIDENT_IN_HAND_MODEL")
  ModelResourceLocation RootsGetTridentInHandModel();

  @Accessor("SPYGLASS_MODEL")
  ModelResourceLocation RootsGetSpyglassModel();

  @Accessor("SPYGLASS_IN_HAND_MODEL")
  ModelResourceLocation RootsGetSpyglassInHandModel();
}
