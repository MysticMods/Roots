package mysticmods.roots.util;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

public class FakePlayerUtil {
  public static final UUID ROOTS_UUID = UUID.fromString("d16d208c-3636-4341-ae0b-bc89e8866e95");
  public static final GameProfile ROOTS = new GameProfile(ROOTS_UUID, "[roots]");
}
