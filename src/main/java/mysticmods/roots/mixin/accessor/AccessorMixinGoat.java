package mysticmods.roots.mixin.accessor;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.animal.goat.Goat;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Goat.class)
public interface AccessorMixinGoat {
  @Accessor("DATA_IS_SCREAMING_GOAT")
  static EntityDataAccessor<Boolean> roots$getDataIsScreamingGoat() {
    throw new NotImplementedException();
  }

  @Accessor("DATA_HAS_LEFT_HORN")
  static EntityDataAccessor<Boolean> roots$getDataHasLeftHorn() {
    throw new NotImplementedException();
  }

  @Accessor("DATA_HAS_RIGHT_HORN")
  static EntityDataAccessor<Boolean> roots$getDataHasRightHorn() {
    throw new NotImplementedException();
  }
}
