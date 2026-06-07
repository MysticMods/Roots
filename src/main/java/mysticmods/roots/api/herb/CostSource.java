package mysticmods.roots.api.herb;

enum CostSource {
  SPELL, // always additive
  MODIFIER, // multiplicative or additive
  DISCOUNT // always multiplicative
}
