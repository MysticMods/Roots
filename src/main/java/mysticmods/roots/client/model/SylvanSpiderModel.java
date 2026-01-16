package mysticmods.roots.client.model;


import mysticmods.roots.entity.SylvanSpiderEntity;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SylvanSpiderModel extends SpiderModel<SylvanSpiderEntity> {
  public SylvanSpiderModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createSpiderBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    int i = 15;
    partdefinition.addOrReplaceChild(
        "head", CubeListBuilder.create().texOffs(32, 4)
            .addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 15.0F, -3.0F)
    );
    partdefinition.addOrReplaceChild(
        "body0", CubeListBuilder.create().texOffs(0, 0)
            .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 15.0F, 0.0F)
    );
    partdefinition.addOrReplaceChild(
        "body1", CubeListBuilder.create().texOffs(0, 12)
            .addBox(-5.0F, -4.0F, -6.0F, 10.0F, 8.0F, 12.0F), PartPose.offset(0.0F, 15.0F, 9.0F)
    );
    CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(18, 0)
        .addBox(-8.0F, -1.0F, -1.0F, 9.0F, 2.0F, 2.0F);
    CubeListBuilder cubelistbuilder1 = CubeListBuilder.create().texOffs(18, 0).mirror()
        .addBox(-1.0F, -1.0F, -1.0F, 9.0F, 2.0F, 2.0F);
    partdefinition.addOrReplaceChild("right_hind_leg", cubelistbuilder, PartPose.offset(-4.0F, 15.0F, 2.0F));
    partdefinition.addOrReplaceChild("left_hind_leg", cubelistbuilder1, PartPose.offset(4.0F, 15.0F, 2.0F));
    partdefinition.addOrReplaceChild("right_middle_hind_leg", cubelistbuilder, PartPose.offset(-4.0F, 15.0F, 1.0F));
    partdefinition.addOrReplaceChild("left_middle_hind_leg", cubelistbuilder1, PartPose.offset(4.0F, 15.0F, 1.0F));
    partdefinition.addOrReplaceChild("right_middle_front_leg", cubelistbuilder, PartPose.offset(-4.0F, 15.0F, 0.0F));
    partdefinition.addOrReplaceChild("left_middle_front_leg", cubelistbuilder1, PartPose.offset(4.0F, 15.0F, 0.0F));
    partdefinition.addOrReplaceChild("right_front_leg", cubelistbuilder, PartPose.offset(-4.0F, 15.0F, -1.0F));
    partdefinition.addOrReplaceChild("left_front_leg", cubelistbuilder1, PartPose.offset(4.0F, 15.0F, -1.0F));
    return LayerDefinition.create(meshdefinition, 64, 32);
  }
}

