package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public class MixinItem {
  @WrapOperation(method = "use", at = @At(target = "Lnet/minecraft/world/entity/player/Player;canEat(Z)Z", value = "INVOKE"))
  private boolean roots_canEat(Player instance, boolean canAlwaysEat, Operation<Boolean> original, @Local(argsOnly = true) InteractionHand hand, @Local ItemStack item) {
    original:
    {
      if (hand == InteractionHand.OFF_HAND) {
        ItemStack otherHand = instance.getMainHandItem();
        if (otherHand.is(RootsTags.Items.CASTING_TOOLS)) {
          SpellStorage storage = otherHand.get(ModAttachments.SPELL_STORAGE);
          if (storage == null) {
            break original;
          }

          ISpellInstance spell = storage.getCurrentSpell();

          if (spell == null) {
            break original;
          }

          if (spell.asSpell().is(RootsTags.Spells.BLOCKS_OFF_HAND_EATING)) {
            return false;
          }
        }
      }
    }
    return original.call(instance, canAlwaysEat);
  }
}
