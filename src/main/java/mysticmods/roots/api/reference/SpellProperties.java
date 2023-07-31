package mysticmods.roots.api.reference;

public interface SpellProperties {
  String INTERVAL = "How often the spell is executed while channeling";
  String COOLDOWN = "How long before the player can cast the spell again";

  String DAMAGE = "How much damage the spell will cause";

  String COUNT = "How many entities the spell will affect per interval";
  String ADDED_REACH = "How much additional reach will be added to the default entity reach";
  String DURATION = "How long the spell will last";
  String EXTENDED_DURATION = "How much time will be added to the standard duration when boosted";
}
