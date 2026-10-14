package mysticmods.roots.ritual;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.ritual.IRitualInstance;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.network.client.fx.HealFXPacket;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;

public class HealingAuraRitual extends Ritual {
  private float playerHeal, entityHeal;

  public HealingAuraRitual(Properties properties) {
    super(properties);
  }

  @Override
  protected void functionalTick(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    assert pCache != null;

    if (duration % instance.getInterval() == 0) {
      List<Player> players = pLevel.getEntitiesOfClass(Player.class, pCache.getAABB());
      for (Player player : players) {
        float amount = Math.min(player.getMaxHealth() - player.getHealth(), playerHeal);
        if (amount > 0) {
          player.heal(amount);
          PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new HealFXPacket(player.getId(), amount));
        }
      }

      // TODO: Tags
      List<LivingEntity> entities = pLevel.getEntitiesOfClass(LivingEntity.class, pCache.getAABB(), o -> !(o instanceof Player));
      for (LivingEntity entity : entities) {
        float amount = Math.min(entity.getMaxHealth() - entity.getHealth(), entityHeal);
        if (amount > 0) {
          entity.heal(amount);
          PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new HealFXPacket(entity.getId(), amount));
        }
      }
    }
  }

  @Override
  protected void initialize(Holder<Ritual> holder) {
    var properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    assert properties != null;
    this.entityHeal = properties.get(ModRituals.HEALING_AURA_ENTITY_HEAL_AMOUNT);
    this.playerHeal = properties.get(ModRituals.HEALING_AURA_PLAYER_HEAL_AMOUNT);
  }

  @Override
  public boolean requiresCache(IRitualInstance iRitualInstance) {
    return true;
  }
}
