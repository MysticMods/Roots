package mysticmods.roots.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import javax.annotation.Nullable;
import java.util.Arrays;

// Copied from Mekanism, MIT license
// https://github.com/mekanism/Mekanism/blob/1e5ff6a4a3ef4abc2f9866ca21a2f7ee09664989/src/main/java/mekanism/client/render/MekanismRenderer.java#L464
public class Model3D {

  public float minX, minY, minZ;
  public float maxX, maxY, maxZ;

  private final SpriteInfo[] textures = new SpriteInfo[6];
  private final boolean[] renderSides = new boolean[]{true, true, true, true, true, true};

  public void setSideRender(Direction side, boolean value) {
    renderSides[side.ordinal()] = value;
  }

  public Model3D copy() {
    Model3D copy = new Model3D();
    System.arraycopy(textures, 0, copy.textures, 0, textures.length);
    System.arraycopy(renderSides, 0, copy.renderSides, 0, renderSides.length);
    copy.minX = minX;
    copy.minY = minY;
    copy.minZ = minZ;
    copy.maxX = maxX;
    copy.maxY = maxY;
    copy.maxZ = maxZ;
    return copy;
  }

  @Nullable
  public SpriteInfo getSpriteToRender(Direction side) {
    int ordinal = side.ordinal();
    if (renderSides[ordinal]) {
      return textures[ordinal];
    }
    return null;
  }

  public void setTexture(Direction side, SpriteInfo spriteInfo) {
    textures[side.ordinal()] = spriteInfo;
  }

  public void setTexture(TextureAtlasSprite tex) {
    setTexture(tex, 16);
  }

  public void setTexture(TextureAtlasSprite tex, int size) {
    Arrays.fill(textures, new SpriteInfo(tex, size));
  }

  public void setTextures(SpriteInfo down, SpriteInfo up, SpriteInfo north, SpriteInfo south, SpriteInfo west, SpriteInfo east) {
    textures[0] = down;
    textures[1] = up;
    textures[2] = north;
    textures[3] = south;
    textures[4] = west;
    textures[5] = east;
  }

  public record SpriteInfo(TextureAtlasSprite sprite, int size) {
  }
}
