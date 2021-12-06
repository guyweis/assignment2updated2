package bgu.spl.mics.application.objects;

import java.util.Collection;
import java.util.LinkedList;

import static bgu.spl.mics.application.objects.Cluster.getClusterInstance;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private Cluster cluster;
    private LinkedList <DataBatch> data;
    private int cores;
    private int time;

    public CPU(int c)
    {
        cluster = getClusterInstance();
        data = new LinkedList <DataBatch>();
        cores = c;
        time = 0;
    }


    /**
     * @param: d is a new batch sent from the cluster to the cpu to process
     * @pre: d!=null
     * @post: @post cpu.collection.size() = @pre cpu.collection.size() +1;
     */
    public void addDataBatch(DataBatch d) // to send data from the cluster to cpu
    {
        d.setStartCpuProcessTime(time);
        data.add(d);
    }

    /**
     * @pre: no pre conditions
     * @post: no post conditions
     * @return the data Linked list
     */
    public LinkedList <DataBatch> getData()
    {
        return data;
    }


    /**
     * @pre: no pre conditions
     * @post: no post conditions
     * @return the number of cores
     */
    public int getCores()
    {
        return cores;
    }

    /**
     * @pre: no pre conditions
     * @post: no post conditions
     * @return the currentTime of the Cpu
     */
    public int getTime()
    {
        return time;
    }

    /**
     * @param t represents updated tick count
     * @pre  time <= t && t>=0
     * @inv t>=0
     * @post  time==t
     * @post if data.get(i).Type==Images && t - data[i].starttime == (32 / number of cores) * 4 ticks -> data.size() == @pre data.size() - 1 && processedData.size() == @pre data.size() + 1
     * @post if data.get(i).Type==Text && t - data[i].starttime == (32 / number of cores) * 2 ticks -> data.size() == @pre data.size() - 1 && processedData.size() == @pre data.size() + 1
     * @post if data.get(i).Type==Tabular s && t - data[i].starttime == (32 / number of cores) * 1 ticks -> data.size() == @pre data.size() -1 && processedData.size() == @pre data.size() + 1
     */
    public void updateTime(int t) throws
    {
        if (t<0)
            return;
        time =t;
        for (int i=0; i<data.size();i++){
            DataBatch d = data.get(i);
            if (data.get(i).getType() == Data.Type.Images)
            {
                if((time-d.getStartCpuProcessTime())==(32 /cores) * 4){
                    data.get(i).setCpuProcessed();
                    cluster.addProcessedCpuDataBatchToCluster(d);
                    data.remove(i);
                }
            }
            if (data.get(i).getType() == Data.Type.Text)
            {
                if ((time - d.getStartCpuProcessTime()) == (32 / cores) * 2) {
                    data.get(i).setCpuProcessed();
                    cluster.addProcessedCpuDataBatchToCluster(d);
                    data.remove(i);
                }
            }
            if (data.get(i).getType() == Data.Type.Tabular)
            {
                if((time-d.getStartCpuProcessTime())==(32 /cores) * 1)
                {
                    data.get(i).setCpuProcessed();
                    cluster.addProcessedCpuDataBatchToCluster(d);
                    data.remove(i);
                }
            }
        }

    }

}
