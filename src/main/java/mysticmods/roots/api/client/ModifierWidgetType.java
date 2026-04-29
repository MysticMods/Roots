package mysticmods.roots.api.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.modifier.ModifierInfo;
import net.minecraft.resources.ResourceLocation;

public enum ModifierWidgetType {
  CONFLICTING(
      RootsAPI.rl("modifiers/conflicting_unlocked"),
      RootsAPI.rl("modifiers/conflicting_enabled"),
      RootsAPI.rl("modifiers/conflicting_locked"),
      RootsAPI.rl("modifiers/conflicting_restricted"),
      RootsAPI.rl("modifiers/conflicting_unlocked")
  ),
  NORMAL(
      RootsAPI.rl("modifiers/modifier_unlocked"),
      RootsAPI.rl("modifiers/modifier_enabled"),
      RootsAPI.rl("modifiers/modifier_locked"),
      RootsAPI.rl("modifiers/modifier_restricted"),
      RootsAPI.rl("modifiers/modifier_will_enable")
  );

  private final ResourceLocation unlockedTexture;
  private final ResourceLocation enabledTexture;
  private final ResourceLocation lockedTexture;
  private final ResourceLocation restrictedTexture;
  private final ResourceLocation willEnableTexture;

  ModifierWidgetType(ResourceLocation unlockedTexture, ResourceLocation enabledTexture, ResourceLocation lockedTexture, ResourceLocation restrictedTexture, ResourceLocation willEnableTexture) {
    this.unlockedTexture = unlockedTexture;
    this.enabledTexture = enabledTexture;
    this.lockedTexture = lockedTexture;
    this.restrictedTexture = restrictedTexture;
    this.willEnableTexture = willEnableTexture;
  }

  public static ResourceLocation getTexture(ModifierInfo info) {
    if (info == null) {
      return NORMAL.unlockedTexture;
    }

    ModifierWidgetType type = info.canEnable() ? NORMAL : CONFLICTING;
    if (!info.isUnlocked()) {
      return type.lockedTexture;
    }
    if (info.isEnabled()) {
      return type.enabledTexture;
    } else if (info.isRestricted()) {
      return type.restrictedTexture;
    } else {
      return type.unlockedTexture;
    }
  }
}
