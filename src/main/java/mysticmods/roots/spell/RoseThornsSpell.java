package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.*;
import mysticmods.roots.entity.other.RoseThornsEntity;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.snapshot.RoseThornsEntitySnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class RoseThornsSpell extends Spell {
  private static final AABB entityBoundsCheck = new AABB(-1.8, -1.8, -1.8, 1.8, 1.8, 1.8);
  private double radiusZX, radiusY;
  private int duration;
  private float damage;

  public RoseThornsSpell(ChatFormatting color, CostInstance costs) {
    super(SpellCastType.INSTANT, color, costs, ParentChargeType.INSTANCE, 0xff2040, 0x20ff60);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.ROSE_THORNS_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    radiusZX = properties.get(ModSpells.ROSE_THORNS_RADIUS_ZX);
    radiusY = properties.get(ModSpells.ROSE_THORNS_RADIUS_Y);
    duration = properties.get(ModSpells.ROSE_THORNS_DURATION);
    damage = properties.get(ModSpells.ROSE_THORNS_DAMAGE);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.ROSE_THORNS_RADIUS_ZX);
    properties.add(ModSpells.ROSE_THORNS_RADIUS_Y);
    properties.add(ModSpells.ROSE_THORNS_DURATION);
    properties.add(ModSpells.ROSE_THORNS_DAMAGE);
  }

  @Override
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    BlockHitResult result = pickBlock(pPlayer, instance);

    BlockPos starting = result.getBlockPos();
    BlockPos supporting = starting;

    BlockState stateAt = pLevel.getBlockState(supporting);

    while (!Block.isFaceFull(stateAt.getBlockSupportShape(pLevel, supporting), Direction.UP)) {
      supporting = supporting.relative(Direction.DOWN);
      stateAt = pLevel.getBlockState(supporting);
      if (supporting.getY() - starting.getY() > 15) {
        break;
      }
    }

    if (!pLevel.getEntitiesOfClass(RoseThornsEntity.class, entityBoundsCheck.move(supporting)).isEmpty()) {
      costs.noCharge();
      return SpellCastResult.nothing();
    }

    RoseThornsEntity rose = ModEntities.ROSE_THORNS.get().create(pLevel);
    if (rose != null) {
      rose.setOwner(pPlayer);
      rose.setLifetime(duration);
      rose.setPos(result.getLocation());
      pLevel.addFreshEntity(rose);
      SnapshotHelper.addLiving(rose, ModSerializers.ROSE_THORNS.get(), new RoseThornsEntitySnapshot(rose.tickCount, -1, radiusZX, radiusY, duration, damage));
      return SpellCastResult.success(cooldown);
    } else {
      costs.noCharge();
      return SpellCastResult.nothing();
    }
  }
}
