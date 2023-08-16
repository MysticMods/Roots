package mysticmods.roots.spell;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.util.EntityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;

import java.util.List;

public class DisarmSpell extends TwoRadiusSpell {
  private float dropChance;

  public DisarmSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0x3a3a3a, 0x7a0000);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.DISARM_COOLDOWN.get();
  }

  @Override
  public SpellProperty<Integer> getRadiusYProperty() {
    return ModSpells.DISARM_RADIUS_Y.get();
  }

  @Override
  public SpellProperty<Integer> getRadiusZXProperty() {
    return ModSpells.DISARM_RADIUS_ZX.get();
  }

  @Override
  public void initialize() {
    this.dropChance = ModSpells.DISARM_DROP_CHANCE.get().getValue();
  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    List<EquipmentSlot> slots = List.of(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
    List<LivingEntity> entities = pLevel.getEntities(EntityTypeTest.forClass(LivingEntity.class), getAABB().move(pPlayer.position()), EntityUtils.isHostileTo(pPlayer).and((o) -> !o.getType().is(RootsTags.Entities.DISABLE_DISARM)));

    int count = 0;

    for (LivingEntity entity : entities) {
      for (EquipmentSlot slot : slots) {
        ItemStack stack = entity.getItemBySlot(slot);
        if (stack.isEmpty()) {
          continue;
        }

        count++;

        if (pLevel.random.nextFloat() < dropChance) {
          entity.spawnAtLocation(stack);
        }

        entity.setItemSlot(slot, ItemStack.EMPTY);
      }
    }

    if (count == 0) {
      costs.noCharge();
    }
  }
}
