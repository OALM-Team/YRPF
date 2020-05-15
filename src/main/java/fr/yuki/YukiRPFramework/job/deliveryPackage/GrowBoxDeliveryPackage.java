package fr.yuki.YukiRPFramework.job.deliveryPackage;

import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.manager.JobManager;
import fr.yuki.YukiRPFramework.model.JobTool;
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
        return 10000;
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
        jobTool.setJobToolType("GROWBOX");
        jobTool.setReward(0);
        jobTool.setX(this.position.getX());
        jobTool.setY(this.position.getY());
        jobTool.setZ(this.position.getZ());
        jobTool.setRx(this.rotation.getX());
        jobTool.setRy(this.rotation.getY());
        jobTool.setRz(this.rotation.getZ());
        jobTool.setSx(1);
        jobTool.setSy(1);
        jobTool.setSz(1);
        JobManager.getJobTools().add(jobTool);
        jobTool.spawn(JobManager.getJobs().get(JobEnum.WEED));
    }
}
