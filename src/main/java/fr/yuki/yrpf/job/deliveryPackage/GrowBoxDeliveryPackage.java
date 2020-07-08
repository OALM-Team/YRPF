package fr.yuki.yrpf.job.deliveryPackage;

import fr.yuki.yrpf.enums.JobEnum;
import fr.yuki.yrpf.job.tools.GrowBox;
import fr.yuki.yrpf.manager.JobManager;
import fr.yuki.yrpf.model.GrowboxModel;
import fr.yuki.yrpf.model.JobTool;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class GrowBoxDeliveryPackage extends DeliveryPackage {
    public GrowBoxDeliveryPackage(Player player, Vector position, Vector rotation) {
        super(player, position, rotation);
    }

    @Override
    public Vector getPackageScale() {
        return new Vector(2, 2, 2);
    }

    @Override
    public int getModelId() {
        return 505;
    }

    @Override
    public int getTimeForDelivery() {
        return 5000;
        //return (1000*60) * 3;
    }

    @Override
    public void onDelivered() {
        // Init a new job tool
        JobTool jobTool = new JobTool();
        jobTool.setId(-1);
        jobTool.setModelId(50007);
        jobTool.setName("GrowBox");
        jobTool.setJobType("WEED");
        jobTool.setLevelRequired(1);
        jobTool.setReward(0);
        jobTool.setX(this.position.getX());
        jobTool.setY(this.position.getY());
        jobTool.setZ(this.position.getZ());
        jobTool.setRX(this.rotation.getX());
        jobTool.setRY(this.rotation.getY());
        jobTool.setRZ(this.rotation.getZ());
        jobTool.setSX(1);
        jobTool.setSY(1);
        jobTool.setSZ(1);
        jobTool.setJobToolType("GROWBOX");
        JobManager.getJobTools().add(jobTool);
        jobTool.spawn(JobManager.getJobs().get(JobEnum.WEED.name()));

        // Insert new growbox
        GrowBox growBox = (GrowBox)jobTool.getJobToolHandler();
        GrowboxModel growboxModel = new GrowboxModel();
        growboxModel.setX(this.position.getX());
        growboxModel.setY(this.position.getY());
        growboxModel.setZ(this.position.getZ());
        growboxModel.setRx(this.rotation.getX());
        growboxModel.setRy(this.rotation.getY());
        growboxModel.setRz(this.rotation.getZ());
        growBox.setGrowboxModel(growboxModel);
        growboxModel.save();
    }
}
