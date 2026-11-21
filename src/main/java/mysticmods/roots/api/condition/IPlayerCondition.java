package mysticmods.roots.api.condition;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public interface IPlayerCondition extends ICondition {
  boolean test(Level level, @Nonnull Player player);

}
