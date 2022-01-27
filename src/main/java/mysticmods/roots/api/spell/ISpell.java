package mysticmods.roots.api.spell;

import mysticmods.roots.api.herbs.IHerb;
import net.minecraft.entity.player.PlayerEntity;
import noobanidus.libs.particleslib.client.Color;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * A Roots Spell: can be imbued into a staff and cast
 */
public interface ISpell {

  /**
   * Each spell has two colors to visually distinguish it from others
   * The first color in the pair is the primary spell color (used in tooltips)
   * @return a pair of spell colors
   */
  Pair<Color, Color> getColors();

  /**
   * (using the Map interface to leave)
   * @return a map with costs
   */
  Map<IHerb, Double> getTotalCosts();

  double getCost(IHerb herb);

  SpellCastingType getCastingType();

  /**
   * @return the spell cooldown time in ticks (can be 0)
   */
  int getCooldown();

  List<ISpellModifier> getAssociatedModfiers();

  /**
   * Called when the spell is cast by a staff user
   * <ul>
   *   <li>Called once per activation for {@link SpellCastingType#INSTANTANEOUS} spells</li>
   *   <li>Called Every tick for {@link SpellCastingType#CHANNELED} spells</li>
   * </ul>
   */
  void cast(PlayerEntity caster);

}
