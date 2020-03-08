package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PropertyPredicate extends StatePredicate {
  protected List<IProperty<?>> props;

  public PropertyPredicate(IBlockState state, IProperty<?> prop) {
    super(state);
    this.props = Collections.singletonList(prop);
  }

  public PropertyPredicate(IBlockState state, List<IProperty<?>> props) {
    super(state);
    this.props = props;
  }

  @Override
  public boolean test(IBlockState state) {
    Collection<IProperty<?>> incoming = state.getPropertyKeys();
    Collection<IProperty<?>> current = this.state.getPropertyKeys();
    return super.test(state) && props.stream().allMatch(prop -> incoming.contains(prop) && current.contains(prop) && state.getValue(prop).equals(this.state.getValue(prop)));
  }
}
