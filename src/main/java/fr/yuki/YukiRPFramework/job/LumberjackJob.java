package fr.yuki.YukiRPFramework.job;

import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.job.harvest.LumberjackTreeCommon;
import java.util.ArrayList;

/**
 * Lumberjack job
 */
public class LumberjackJob extends Job {

    public LumberjackJob() {
        super();

        this.harvestableObjectsTemplate = new ArrayList<>();
        this.harvestableObjectsTemplate.add(new LumberjackTreeCommon());

        if(this.setup()) {
            this.load();
        }

        this.spawnNpc();
        this.refillHarvestResources();
    }

    private static void spawnNpc() {

    }

    @Override
    public JobEnum getJobType() {
        return JobEnum.LUMBERJACK;
    }

    @Override
    public int getRefillInterval() {
        return 60000 * 5;
    }
}
