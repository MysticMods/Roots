package mysticmods.roots.api.blockentity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import org.jetbrains.annotations.NotNull;

public interface FakeMenuBlockEntity extends MenuProvider {
  @Override
  default @NotNull Component getDisplayName() {
    return Component.empty();
  }

  boolean shouldShowInsert ();
}
