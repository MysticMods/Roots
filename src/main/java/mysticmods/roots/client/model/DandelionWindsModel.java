package mysticmods.roots.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class DandelionWindsModel<T extends LivingEntity> extends AgeableListModel<T> {
  private final ModelPart largeWind;
  private final ModelPart smallWindTop;
  private final ModelPart smallWindBottom;

  public DandelionWindsModel(ModelPart root) {
    this.largeWind = root.getChild("large_wind");
    this.smallWindTop = root.getChild("small_wind_top");
    this.smallWindBottom = root.getChild("small_wind_bottom");
  }

  public static LayerDefinition createWindsLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition large_wind = partdefinition.addOrReplaceChild("large_wind", CubeListBuilder.create().texOffs(14, 26).addBox(-9.0F, -5.5F, -9.0F, 18.0F, 6.0F, 18.0F, new CubeDeformation(0.0F))
        .texOffs(0, 0).addBox(-11.0F, -4.5F, -11.0F, 22.0F, 4.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

    PartDefinition small_wind_top = partdefinition.addOrReplaceChild("small_wind_top", CubeListBuilder.create().texOffs(20, 66).addBox(-5.0F, -4.5F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
        .texOffs(0, 50).addBox(-7.0F, -3.5F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

    PartDefinition small_wind_bottom = partdefinition.addOrReplaceChild("small_wind_bottom", CubeListBuilder.create().texOffs(50, 66).addBox(-5.0F, -4.5F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
        .texOffs(31, 50).addBox(-7.0F, -3.5F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 128, 128);
  }

  @Override
  protected Iterable<ModelPart> headParts() {
    return ImmutableList.of();
  }

  @Override
  protected Iterable<ModelPart> bodyParts() {
    return ImmutableList.of(smallWindBottom, smallWindTop, largeWind);
  }

  @Override
  public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
  }
}
