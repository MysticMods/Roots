package mysticmods.roots.item;

import mysticmods.roots.util.TeleportUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;

import java.util.Set;

public class JarOfHomeItem extends Item {
  public JarOfHomeItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack itemstack = player.getItemInHand(hand);
    level.playSound(
        null,
        player.getX(),
        player.getY(),
        player.getZ(),
        SoundEvents.SPLASH_POTION_THROW,
        SoundSource.NEUTRAL,
        0.5F,
        0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
    );
    if (!level.isClientSide()) {
      ServerPlayer player2 = (ServerPlayer) player;
      var dim = player2.findRespawnPositionAndUseSpawnBlock(true, DimensionTransition.DO_NOTHING);
      TeleportUtil.teleportWithVehicle(player2, dim.newLevel(), dim.pos().x, dim.pos().y, dim.pos().z, Set.of(), dim.yRot(), dim.xRot());
    }

    player.awardStat(Stats.ITEM_USED.get(this));
    itemstack.consume(1, player);
    return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
  }
}
