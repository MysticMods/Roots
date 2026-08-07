package mysticmods.roots.mixin.client.accessor;

import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemRenderer.class)
public interface AccessorMixinItemRenderer {
  @Accessor("itemModelShaper")
  ItemModelShaper roots$GetItemModelShaper();

  @Accessor("TRIDENT_MODEL")
  ModelResourceLocation roots$GetTridentModel();

  @Accessor("TRIDENT_IN_HAND_MODEL")
  ModelResourceLocation roots$GetTridentInHandModel();

  @Accessor("SPYGLASS_MODEL")
  ModelResourceLocation roots$GetSpyglassModel();

  @Accessor("SPYGLASS_IN_HAND_MODEL")
  ModelResourceLocation roots$GetSpyglassInHandModel();
}
