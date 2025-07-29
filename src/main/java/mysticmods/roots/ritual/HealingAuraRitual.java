package mysticmods.roots.ritual;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
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
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;

public class HealingAuraRitual extends Ritual {
  private float playerHeal, entityHeal;

  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (duration % getInterval() == 0) {
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
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  protected void initialize(Holder<Ritual> holder) {
    var proprties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    this.entityHeal = proprties.get(ModRituals.HEALING_AURA_ENTITY_HEAL_AMOUNT);
    this.playerHeal = proprties.get(ModRituals.HEALING_AURA_PLAYER_HEAL_AMOUNT);
  }

  @Override
  protected void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModRituals.HEALING_AURA_ENTITY_HEAL_AMOUNT);
    properties.add(ModRituals.HEALING_AURA_PLAYER_HEAL_AMOUNT);
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.HEALING_AURA_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.HEALING_AURA_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.HEALING_AURA_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.HEALING_AURA_INTERVAL;
  }

  @Override
  public boolean requiresCache() {
    return true;
  }
}
