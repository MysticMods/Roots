package mysticmods.roots.api.spell;

import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TransformingSpell extends Spell {
  protected final TransformationInfo baseSpell;
  protected final Map<ResourceKey<SpellModifier>, Spell> transformations = new HashMap<>();

  public TransformingSpell(TransformationInfo baseSpell, TransformationInfo ... spells) {
    super(SpellCastType.TRANSFORMING, (TextColor) null, CostInstance.NONE, ParentChargeType.TRANSFORMING, 0x0, 0x0);
    this.baseSpell = baseSpell;
    for (TransformationInfo info : spells) {

    }
  }

  @Override
  public abstract PropertyHolder<Property.IntegerProperty> getCooldownProperty();

  @Override
  public void initialize(Holder<Spell> holder) {

  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    return 0;
  }

  public record TransformationInfo (ResourceKey<SpellModifier> modifier, Spell spell) {

  }
}
