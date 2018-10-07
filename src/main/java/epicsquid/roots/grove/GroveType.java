package epicsquid.roots.grove;

public enum GroveType {

  FAIRY("Fairy"),
  FORBIDDEN("Forbidden"),
  NATURAL("Natural"),
  MYSTIC("Mystic"),
  WILD("Wild"),
  FUNGAL("Fungal");

  private final String name;

  private GroveType(String s){
    this.name = s;
  }

  public String toString() {
    return this.name;
  }
}
