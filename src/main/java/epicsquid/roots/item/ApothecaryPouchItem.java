package epicsquid.roots.item;

public class ApothecaryPouchItem extends PouchItem {
  public ApothecaryPouchItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean isApothecary() {
    return true;
  }
}
