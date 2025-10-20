package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.api.RootsTags;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public class MixinGui$Tome {
  @Shadow
  private ItemStack lastToolHighlight;

  @Shadow
  private int toolHighlightTimer;

  @Definition(id = "getSelected", method = "Lnet/minecraft/world/entity/player/Inventory;getSelected()Lnet/minecraft/world/item/ItemStack;")
  @Expression("?.getSelected()")
  @WrapOperation(method = "tick()V", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
  private ItemStack roots$preventClearingTomeTooltipIfNecessary(Inventory instance, Operation<ItemStack> original) {
    if (this.toolHighlightTimer > 0 && this.lastToolHighlight.is(RootsTags.Items.GRAMARIES)) {
      return this.lastToolHighlight;
    }
    return original.call(instance);
  }
}
