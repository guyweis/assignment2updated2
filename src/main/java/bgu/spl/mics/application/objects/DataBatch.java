package bgu.spl.mics.application.objects;

import java.util.LinkedList;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {

    private boolean isCpuProcessed;
    private boolean isGpuProcessed;
    private Data data;
    private int index;
    private GPU gpuSource;
    private int startCpuProcessTime;
    private int startGpuProcessTime;
    private Data.Type type;


    public DataBatch(Data d)
    {
        isCpuProcessed = false;
        isGpuProcessed = false;
        data = d;
        index = 0;
        type = d.getType();
    }


    public void setCpuProcessed() {
        isCpuProcessed =true;
    }
    public void setGpuProcessed() {
        isGpuProcessed =true;
    }
    public void setStartCpuProcessTime(int time) {
        startCpuProcessTime = time;
    }
    public void setStartGpuProcessTime(int time) {
        startGpuProcessTime = time;
    }
    public Data.Type getType(){return type;}
    public int getStartCpuProcessTime(){return startCpuProcessTime;}
    public Boolean getIsCpuProcessed(){return isCpuProcessed;}
    public Boolean getIsGpuProcessed(){return isGpuProcessed;}
}
