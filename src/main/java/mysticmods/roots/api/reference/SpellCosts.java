package mysticmods.roots.api.reference;

public interface SpellCosts {
  double BASE_1000 = 1.0;
  double BASE_0750 = 0.75;
  double BASE_0500 = 0.5;
  double BASE_0250 = 0.25;
  double BASE_0125 = 0.125;
  double BASE_0063 = 0.063;

  double COMPLEX_1750 = BASE_1000 + BASE_0750;
  double COMPLEX_1875 = COMPLEX_1750 + BASE_0125;
  double COMPLEX_0625 = BASE_0500 + BASE_0125;
  double COMPLEX_0375 = BASE_0250 + BASE_0125;
  double COMPLEX_0188 = BASE_0125 + BASE_0063;

}
