package fr.yuki.YukiRPFramework.job.deliveryPackage;

import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.manager.JobManager;
import fr.yuki.YukiRPFramework.model.JobTool;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class GeneratorDeliveryPackage extends DeliveryPackage {
    public GeneratorDeliveryPackage(Player player, Vector position, Vector rotation) {
        super(player, position, rotation);
    }

    @Override
    public Vector getPackageScale() {
        return new Vector(1.3, 1.3, 1.3);
    }

    @Override
    public int getModelId() {
        return 505;
    }

    @Override
    public int getTimeForDelivery() {
        return 5000;
        //return (1000*60) * 2;
    }

    @Override
    public void onDelivered() {
        // Init a new job tool
        JobTool jobTool = new JobTool();
        jobTool.setId(-1);
        jobTool.setModelId(581);
        jobTool.setName("Generateur");
        jobTool.setJobType("WEED");
        jobTool.setLevelRequired(1);
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
        jobTool.setJobToolType("GENERATOR");
        JobManager.getJobTools().add(jobTool);
        jobTool.spawn(JobManager.getJobs().get(JobEnum.WEED));
    }
}
