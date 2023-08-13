package mysticmods.roots.spell;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.api.test.entity.EntityTestType;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class AcidCloudSpell extends TwoRadiusSpell {
  private float damage;
  private int count;

  public AcidCloudSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.CONTINUOUS, color, costs, 0x50a028, 0x405f20);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.ACID_CLOUD_COOLDOWN.get();
  }

  @Override
  public SpellProperty<Integer> getRadiusYProperty() {
    return ModSpells.ACID_CLOUD_RADIUS_Y.get();
  }

  @Override
  public SpellProperty<Integer> getRadiusZXProperty() {
    return ModSpells.ACID_CLOUD_RADIUS_ZX.get();
  }

  @Override
  public void initialize() {
    this.damage = ModSpells.ACID_CLOUD_DAMAGE.get().getValue();
    this.count = ModSpells.ACID_CLOUD_COUNT.get().getValue();
  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    Predicate<LivingEntity> entityTest = entity -> {
      if (entity == pPlayer || entity.isDeadOrDying() || entity.hurtTime > 0) {
        return false;
      }

      if (entity instanceof Player otherPlayer && pLevel.getServer() != null && pLevel.getServer().isPvpAllowed() && (pPlayer.getTeam() == null || pPlayer.getTeam().isAlliedTo(otherPlayer.getTeam()))) {
        return true;
      }

      if (entity instanceof NeutralMob neutral) {
        if (neutral.isAngryAt(pPlayer)) {
          return true;
        }
      }

      if (entity instanceof Mob) {
        return true;
      }

      return false;
    };
    List<LivingEntity> entities = pLevel.getEntities(EntityTypeTest.forClass(LivingEntity.class), getAABB().move(pPlayer.position()), entityTest);
    for (int damaged = 0; damaged < count; damaged++) {
      if (entities.isEmpty()) {
        break;
      }

      LivingEntity entity = entities.remove(pLevel.getRandom().nextInt(entities.size()));
      entity.hurt(DamageSource.playerAttack(pPlayer), damage);
    }
  }

  // From Stack Overflow: https://stackoverflow.com/posts/51618656/revisions
  public static <T> Iterable<T> shuffle (final List<T> input) {
    return () -> new Iterator<T>() {
      final Random randomizer = new Random();
      int i = 0;
      final int n = input.size();
      final Int2ObjectOpenHashMap<T> shuffled = new Int2ObjectOpenHashMap<>();

      @Override
      public boolean hasNext() {
        return i < n;
      }

      @Override
      public T next() {
        int j = i + randomizer.nextInt(n - i);
        T a = get(i), b = get(j);
        shuffled.put(j, a);
        shuffled.remove(i);
        ++i;
        return b;
      }

      T get(int i) {
        return shuffled.containsKey(i) ? shuffled.get(i) : input.get(i);
      }
    };
  }
}
