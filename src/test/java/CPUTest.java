import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.DataBatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static bgu.spl.mics.application.objects.Data.Type;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class CPUTest {

    private static CPU cpu;
    private Cluster cluster;
    private LinkedList<DataBatch> dataList;
    private LinkedList <DataBatch> processedCpuData;
    private int cores;
    private int time;

    @Before
    public void setUp() throws Exception
    {
    cpu = new CPU(32);
    }


    @Test
    public void addDataBatch()
    {
        DataBatch d1 = null;
        assertThrows("Should throw exception (added null databatch)",Exception.class, () -> cpu.addDataBatch(d1));
        Data data = new Data(Type.Images, 3000);
        DataBatch d = new DataBatch(data);
        assertEquals(0,cpu.getData().size());
        cpu.addDataBatch(d);
        assertEquals(1,cpu.getData().size());
    }


    @Test
    public void testUpdateTime()
    {
        Data data1 = new Data(Type.Images, 1000);
        Data data2 = new Data(Type.Text, 1000);
        Data data3 = new Data(Type.Tabular, 1000);
        DataBatch d1 = new DataBatch(data1);
        DataBatch d2 = new DataBatch(data2);
        DataBatch d3 = new DataBatch(data3);
        assertThrows("updated tick to minus time",Exception.class, () -> cpu.updateTime(-1));
        cpu.addDataBatch(d1);
        cpu.addDataBatch(d2);
        cpu.addDataBatch(d3);
        assertEquals(3,cpu.getData().size());
        cpu.updateTime(1);
        assertEquals(2,cpu.getData().size());
        cpu.updateTime(2);
        assertEquals(1,cpu.getData().size());
        cpu.updateTime(3);
        assertEquals(1,cpu.getData().size());
        cpu.updateTime(4);
        assertEquals(0,cpu.getData().size());
        cpu.updateTime(5);
        assertEquals(0,cpu.getData().size());
    }
}