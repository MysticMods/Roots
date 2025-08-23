package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.api.attachment.QuiverRecord;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.util.QuiverUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileWeaponItem.class)
public class MixinProjectileWeaponItem {
  @Inject(method = "useAmmo", at = @At(target = "Lnet/minecraft/world/entity/player/Inventory;removeItem(Lnet/minecraft/world/item/ItemStack;)V", value = "INVOKE"))
  private static void rootsUseAmmo(ItemStack weapon, ItemStack ammo, LivingEntity shooter, boolean intangable, CallbackInfoReturnable<ItemStack> cir, @Local Player player) {
    // Important: even though the component exists when you examine the item stack, if it evaluates to empty (i.e., count is 0, which is the case for this injection point to even reach), the components/patch that is consulted by `has` will be empty.
    ammo.setCount(1);
    if (ammo.has(ModAttachments.QUIVER_RECORD)) {
      QuiverRecord record = ammo.get(ModAttachments.QUIVER_RECORD);
      if (record != null) {
        QuiverUtil.consumeAmmunition(player, record);
      }
    }
  }
}
