package mysticmods.roots.mixin.client.accessor;

import net.minecraft.client.gui.Gui;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface AccessorMixinGui {
  @Accessor("lastToolHighlight")
  void roots$SetLastToolHighlight(ItemStack stack);

  @Accessor("toolHighlightTimer")
  void roots$SetToolHighlightTimer(int timer);
}
