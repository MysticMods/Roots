package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.util.IFurnaceInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MixinAbstractFurnaceBlockEntity implements IFurnaceInfo {
  @Unique
  private boolean roots$hasValidRecipe = false;

  @Override
  public boolean roots$hasValidRecipe() {
    return roots$hasValidRecipe;
  }

  @Override
  public void roots$setHasValidRecipe(boolean value) {
    this.roots$hasValidRecipe = value;
  }

  @Inject(method="serverTick", at=@At("HEAD"))
  private static void roots$resetValidRecipe(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
    ((IFurnaceInfo) blockEntity).roots$setHasValidRecipe(false);
  }

  @WrapOperation(method="serverTick", at=@At(value="INVOKE", target="Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;canBurn(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/crafting/RecipeHolder;Lnet/minecraft/core/NonNullList;ILnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;)Z"))
  private static boolean roots$whenValidRecipe1 (RegistryAccess registryAccess, RecipeHolder<?> recipe, NonNullList<ItemStack> inventory, int maxStackSize, AbstractFurnaceBlockEntity furnace, Operation<Boolean> original) {
    var result = original.call(registryAccess, recipe, inventory, maxStackSize, furnace);
    if (result) {
      ((IFurnaceInfo) furnace).roots$setHasValidRecipe(true);
    }
    return result;
  }
}
