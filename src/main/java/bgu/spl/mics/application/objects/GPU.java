package bgu.spl.mics.application.objects;
import static bgu.spl.mics.application.objects.Cluster.getClusterInstance;
import static bgu.spl.mics.application.objects.Model.Status;
import bgu.spl.mics.Event;
import bgu.spl.mics.MessageBus;

import java.lang.reflect.Array;
import java.util.LinkedList;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    public enum Type {RTX3090, RTX2080, GTX1080}
    private int gpuNumber;
    private Type type;
    private int vramSize;
    private LinkedList <DataBatch> dataUnProcessed;
    private LinkedList <DataBatch> finalDataGPUProcessed;
    private LinkedList <DataBatch> dataReadyForCPUProcessing;
    private LinkedList <DataBatch> dataAfterCPUProcessing;
    int outForProcessing;
    private  Cluster cluster;
    private Model model;
    private int time;


    public GPU(Type t, int gpuSerialNum)
    {
        type=type;
        switch(type) {
            case RTX3090:
                vramSize =32;
                break;
            case RTX2080:
                vramSize =16;
                break;
            case GTX1080:
                vramSize =8;
                break;
            default:
                System.out.println("no such type");
        }
        outForProcessing =0;
        cluster = getClusterInstance();
        model =null;
        finalDataGPUProcessed = new LinkedList<DataBatch>();
        dataReadyForCPUProcessing=new LinkedList <DataBatch>();
        dataAfterCPUProcessing=new LinkedList <DataBatch>();
        gpuNumber=gpuSerialNum;
    }

    // getters
    public Type getType() { return type; }
    public LinkedList<DataBatch> getFinalData() { return finalDataGPUProcessed; }
    public LinkedList <DataBatch> getDataBeforeCPUProcessing() { return dataReadyForCPUProcessing; }
    public LinkedList <DataBatch> getDataAfterCPUProcessing() { return dataAfterCPUProcessing; }
    public int getNumOutForProcess() { return outForProcessing; }
    public Cluster getCluster() { return cluster; }
    public Model getModel() { return model; }
    public LinkedList <DataBatch> getDataUnProcessed() {return dataUnProcessed;}

    /**
     * @pre model==null
     * @post model == m;
     */
    public void addModel (Model m) // not processed
    {
        model = m;
        divide(m.getData());
    }
    /**
     * @pre model !=null && model.Data.size()>0 && model.Data.size()%1000==0
     * @post dataUnProcessed.size()==d.size()/1000
     */
    private void divide(Data d) // this function will divide the batches to 1000 samples unprocessed
    {
        int countSize = d.getSize();
        for (int i = countSize ; i>0; i= i-1000){
            DataBatch d1 = new DataBatch(d);
            dataUnProcessed.add(d1);
        }
    }

    /**
     * @pre no conditions
     * @inv outForProcesssing>0 && outForProcesssing<vramsize
     * @post no conditions
     * @return return value>=0 && return value <=vramsize
     */

    public int HowMuchSpaceInVram()
    {
        return vramSize-outForProcessing;
    }

    /**
     * @param num  number of batches to send for processing
     * @pre outForProcessing + num <= vramsize
     * @inv outForProcessing<= vramsize
     * @post dataReadyForCPUProcessing.size() = @pre dataReadyForCPUProcessing.size() + num
     */
    public void addBatchesToBeProcessed(int num)
    {
        outForProcessing++;
    }



    /**
     * @param d  batch processed by CPU
     * @pre dataAfterCPUProcessing + 1 <= vramsize && d.isCpuProcessed==true;
     * @inv dataAfterCPUProcessing <= vramsize
     * @post dataAfterCPUProcessing.size() = @pre dataSentCPUProcessing.size() + 1
     */
    public void addBatchesForGpuProcessing(DataBatch d)
    {
        dataAfterCPUProcessing.add(d);
    }

    /**
     * @param a  batchList needed to be processed by CPU
     * @pre dataReadyForCPUProcessing.size()!=null
     * @inv dataReadyForCPUProcessing <= vramsize
     * @post dataReadyForCPUProcessing.size() = @pre dataSentCPUProcessing.size() - 1
     */
    public DataBatch sendBatchForCpuProcessing(LinkedList <DataBatch> a)//from gpu list 2 to cluster
    {
        return a.removeFirst();
    }

    /**
     * @pre gpu.model.status=="training" && finalDataGPUProcessed.size()==gpu.model.data.size/1000
     * @post gpu.model.status = "Trained"
     */
    public void completed() // send if completed to the massageBus if all the dataBach precessed
    {
        model.setStatus(Status.Trained);
    }


    /**
     * @param t represents updated tick count
     * @pre  time <= t && t>=0
     * @inv t>=0
     * @post  time==t
     * @post if gpu.type==RTX3090 && t - dataAfterCPUProcessing.get(i).starttime == 1 ticks -> finalDataGPUProcessed.size() == @pre finalDataGPUProcessed.size() + 1 && dataAfterCPUProcessing.size() == @pre dataAfterCPUProcessing.size() - 1
     * @post if gpu.type==RTX2080 && t - dataAfterCPUProcessing.get(i).starttime ==  2 ticks -> finalDataGPUProcessed.size() == @pre finalDataGPUProcessed.size() + 1 && dataAfterCPUProcessing.size() == @pre dataAfterCPUProcessing.size() - 1
     * @post if gpu.type==GTX1080 s && t - dataAfterCPUProcessing.get(i).starttime == 4 ticks -> finalDataGPUProcessed.size() == @pre finalDataGPUProcessed.size() + 1 && dataAfterCPUProcessing.size() == @pre dataAfterCPUProcessing.size() - 1
     */
    public void updateTime(int t)
    {

    }


}