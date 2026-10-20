package mysticmods.roots.mixin.accessor;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AccessorMixinAbstractFurnaceBlockEntity {
  @Accessor("litDuration")
  void roots$setLitDuration (int newValue);

  @Accessor("litDuration")
  int roots$getLitDuration ();

  @Accessor("litTime")
  void roots$setLitTime (int newValue);

  @Accessor("litTime")
  int roots$getLitTime ();

  @Accessor("cookingProgress")
  void roots$setCookingProgress (int newValue);

  @Accessor("cookingProgress")
  int roots$getCookingProgress ();

  @Accessor("cookingTotalTime")
  void roots$setCookingTotalTime (int newValue);

  @Accessor("cookingTotalTime")
  int roots$getCookingTotalTime ();
}
