
import static bgu.spl.mics.application.objects.Model.Status;
import static bgu.spl.mics.application.objects.Model.Result;
import bgu.spl.mics.application.objects.*;
import org.junit.Before;
import org.junit.Test;
import java.util.LinkedList;
import static org.junit.Assert.*;

public class GPUTest {
    private static GPU gpu;
    private static Cluster cluster;
    private static Model model;
    private int gpuNumber;
    private GPU.Type type;
    private int vramSize;
    private LinkedList<DataBatch> dataUnProcessed;
    private LinkedList<DataBatch> finalDataGPUProcessed;
    private LinkedList<DataBatch> dataReadyForCPUProcessing;
    private LinkedList<DataBatch> dataAfterCPUProcessing;
    int outForProcessing;
    private DataBatch d;
    private Data data;
    private Student s;
    private int time;

    @Before
    public void setUP() {
        s = new Student();
        gpu = new GPU(GPU.Type.RTX2080,2);
        data = new Data(Data.Type.Images, 3000);
        d = new DataBatch(data);
        model = new Model("guy", data, s);
    }

    @Test
    public void testAddModel() {
        Model m = null;
        assertThrows("Should not throw exception (added null model)", Exception.class, () -> gpu.addModel(m));
        assertNull(gpu.getModel());
        gpu.addModel(model);
        assertEquals(model, gpu.getModel());
    }

    @Test
    public void testHowMuchSpaceInVram() {
        assertEquals(16, gpu.HowMuchSpaceInVram());
        gpu.addModel(model);
        gpu.addBatchesToBeProcessed( 1);
        assertEquals(15, gpu.HowMuchSpaceInVram());
    }

    @Test
    public void testAddBatchesForGpuProcessing() {
        assertThrows("trying to add unprocessed dataBatch", Exception.class, () -> gpu.addBatchesForGpuProcessing(d));
        gpu.addBatchesForGpuProcessing(d);
        d.setCpuProcessed();
        assertEquals(1, dataAfterCPUProcessing.size());
    }

    @Test
    public void testSendBatchForCpuProcessing() {
        assertThrows("no databatches ready to be sent to cpu processing", Exception.class, () -> gpu.sendBatchForCpuProcessing(dataReadyForCPUProcessing));
        dataReadyForCPUProcessing.add(d);
        DataBatch d1 = gpu.sendBatchForCpuProcessing(dataReadyForCPUProcessing);
        assertEquals(d, d1);
        assertEquals(0, dataReadyForCPUProcessing.size());
    }

    @Test
    public void testCompleted() {
        gpu.addModel(model);
        assertEquals(Status.Training, gpu.getModel().getStatus());
        gpu.completed();
        assertEquals(Status.Trained, gpu.getModel().getStatus());
    }

    @Test
    public void testUpdateTime() {
        Data data1 = new Data(Data.Type.Images, 1000);
        Data data2 = new Data(Data.Type.Text, 1000);
        Data data3 = new Data(Data.Type.Tabular, 1000);
        DataBatch d1 = new DataBatch(data1);
        DataBatch d2 = new DataBatch(data2);
        DataBatch d3 = new DataBatch(data3);
        assertThrows("updated tick to minus time", Exception.class, () -> gpu.updateTime(-1));
        gpu.getDataAfterCPUProcessing().add(d1);
        gpu.getDataAfterCPUProcessing().add(d2);
        gpu.getDataAfterCPUProcessing().add(d3);
        assertEquals(3, gpu.getDataAfterCPUProcessing().size());
        assertEquals(0, gpu.getFinalData().size());
        gpu.updateTime(1);
        assertEquals(3, gpu.getDataAfterCPUProcessing().size());
        assertEquals(0, gpu.getFinalData().size());
        gpu.updateTime(2);
        assertEquals(2, gpu.getDataAfterCPUProcessing().size());
        assertEquals(1, gpu.getFinalData().size());
        gpu.updateTime(4);
        assertEquals(1, gpu.getDataAfterCPUProcessing().size());
        assertEquals(2, gpu.getFinalData().size());
        gpu.updateTime(5);
        assertEquals(1, gpu.getDataAfterCPUProcessing().size());
        assertEquals(2, gpu.getFinalData().size());
        gpu.updateTime(6);
        assertEquals(0, gpu.getDataAfterCPUProcessing().size());
        assertEquals(3, gpu.getFinalData().size());
    }

}

