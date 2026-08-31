package mysticmods.roots.client.particle.world;


import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class BaseRootsParticle extends Particle {
  protected float quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
  protected TextureAtlasSprite sprite;

  protected BaseRootsParticle(ClientLevel level, double x, double y, double z) {
    super(level, x, y, z);
  }

  protected BaseRootsParticle(
      ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed
  ) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
  }

  protected void setSprite(TextureAtlasSprite sprite) {
    this.sprite = sprite;
  }

  protected float getU0() {
    return this.sprite.getU0();
  }

  protected float getU1() {
    return this.sprite.getU1();
  }

  protected float getV0() {
    return this.sprite.getV0();
  }

  protected float getV1() {
    return this.sprite.getV1();
  }

  public void pickSprite(SpriteSet sprite) {
    this.setSprite(sprite.get(this.random));
  }

  public void setSpriteFromAge(SpriteSet sprite) {
    if (!this.removed) {
      this.setSprite(sprite.get(this.age, this.lifetime));
    }
  }

  public SingleQuadParticle.FacingCameraMode getFacingCameraMode() {
    return SingleQuadParticle.FacingCameraMode.LOOKAT_XYZ;
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    Quaternionf quaternionf = new Quaternionf();
    this.getFacingCameraMode().setRotation(quaternionf, renderInfo, partialTicks);
    if (this.roll != 0.0F) {
      quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
    }

    this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
  }

  protected void renderRotatedQuad(VertexConsumer buffer, Camera camera, Quaternionf quaternion, float partialTicks) {
    Vec3 vec3 = camera.getPosition();
    float f = (float)(Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
    float f1 = (float)(Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
    float f2 = (float)(Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
    this.renderRotatedQuad(buffer, quaternion, f, f1, f2, partialTicks);
  }

  protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
    float f = this.getQuadSize(partialTicks);
    float f1 = this.getU0();
    float f2 = this.getU1();
    float f3 = this.getV0();
    float f4 = this.getV1();
    int i = this.getLightColor(partialTicks);
    this.renderVertex(buffer, quaternion, x, y, z, 1.0F, -1.0F, f, f2, f4, i);
    this.renderVertex(buffer, quaternion, x, y, z, 1.0F, 1.0F, f, f2, f3, i);
    this.renderVertex(buffer, quaternion, x, y, z, -1.0F, 1.0F, f, f1, f3, i);
    this.renderVertex(buffer, quaternion, x, y, z, -1.0F, -1.0F, f, f1, f4, i);
  }

  public void renderVertex(
      VertexConsumer buffer,
      Quaternionf quaternion,
      float x,
      float y,
      float z,
      float xOffset,
      float yOffset,
      float quadSize,
      float u,
      float v,
      int packedLight
  ) {
    Vector3f vector3f = new Vector3f(xOffset, yOffset, 0.0F).rotate(quaternion).mul(quadSize).add(x, y, z);
    buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z())
        .setUv(u, v)
        .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
        .setLight(packedLight);
  }

  @Override
  public AABB getRenderBoundingBox(float partialTicks) {
    float size = getQuadSize(partialTicks);
    return new AABB(this.x - size, this.y - size, this.z - size, this.x + size, this.y + size, this.z + size);
  }

  public float getQuadSize(float scaleFactor) {
    return this.quadSize;
  }

  @Override
  public Particle scale(float scale) {
    this.quadSize *= scale;
    return super.scale(scale);
  }
}

