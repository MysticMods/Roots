package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.GrowthFXPacket;
import mysticmods.roots.util.GrowthUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GrowthInfusionSpell extends Spell {
  public GrowthInfusionSpell(ChatFormatting color, CostInstance costs) {
    super(Type.CONTINUOUS, color, costs, 0x30ff30, 0xc0ffc0);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.GROWTH_INFUSION_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.DoubleProperty> getReachProperty() {
    return ModSpells.GROWTH_INFUSION_ADDED_REACH;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> result) {
    super.buildProperties(result);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
  }

  @Override
  public boolean hasBlockTarget(Player pPlayer) {
    return true;
  }

  @Override
  public @Nullable Vec3 getBlockTarget(Player pPlayer) {
    return pickBlock(pPlayer).getLocation();
  }

  @Override
  public int cast(Level level, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    BlockHitResult result = pickBlock(pPlayer);
    BlockPos pos = result.getBlockPos();
    BlockState at = level.getBlockState(pos);

    int doTicks = GrowthUtil.growthTicks(level, pos, at, pPlayer);
    if (doTicks > 0) {
      if (level.random.nextInt(doTicks) == 0) {
        at.randomTick((ServerLevel) level, pos, level.random);
        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, new ChunkPos(result.getBlockPos()), new GrowthFXPacket(pos));
      }
    } else {
      costs.noCharge();
      pPlayer.stopUsingItem();
      return -1;
    }

    return cooldown;
  }
}
