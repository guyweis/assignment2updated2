import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import bgu.spl.mics.application.services.GPUService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.LinkedList;

import static org.junit.Assert.*;


public class MessageBusImplTest {

    private Model m;
    private MessageBus mesBus;
    private MicroService d;
    private TrainModelEvent modelEvent;
    //MicroServices:
    private GPUService g;
    private GPUService g2;
    private Student s;
    private Data data;

    @Before
    public void setUp() throws Exception {
        g = new GPUService("gpu1");
        g2 = new GPUService("gpu2");
        mesBus = MessageBusImpl.getInstance();
        m = new Model("guy", data, s);
    }

    @After
    public void tearDown() {
        mesBus.unregister(g);
        mesBus.unregister(g2);
    }

    @Test
    public void testSubscribeEvent() {
        mesBus.register(g);
        mesBus.subscribeEvent(TrainModelEvent.class, g);
        ArrayDeque a = (ArrayDeque) mesBus.getBroadcastSubscriptionList().get(TickBroadcast.class);
        assertTrue(a.contains(g));//checks the queue
    }

    @Test
    public void testSubscribeBroadcast() {
        mesBus.register(g);
        mesBus.subscribeBroadcast(TickBroadcast.class, g);
        LinkedList l = (LinkedList) mesBus.getBroadcastSubscriptionList().get(TickBroadcast.class);
        assertTrue(l.contains(g));//checks the linkList
    }

    @Test
    public void testComplete() {
        data = new Data(Data.Type.Images, 3000);
        s = new Student();
        m = new Model("guy", data, s);
        modelEvent = new TrainModelEvent(m);
        mesBus.register(g);
        mesBus.subscribeEvent(TrainModelEvent.class, g);
        Future<Model> f = mesBus.sendEvent(modelEvent);
        mesBus.complete(modelEvent, m);
        assertEquals(m, f.get());
    }

    @Test
    public void testSendBroadcast() {
        Broadcast tickBroad = new TickBroadcast(2);
        mesBus.register(g);
        mesBus.register(g2);
        mesBus.subscribeBroadcast(TickBroadcast.class, g);
        mesBus.subscribeBroadcast(TickBroadcast.class, g2);
        mesBus.sendBroadcast(tickBroad);
        try {
            Broadcast b1 = (TickBroadcast) mesBus.awaitMessage(g);
            assertEquals(b1, tickBroad);
            Broadcast b2 = (TickBroadcast) mesBus.awaitMessage(g2);
            assertEquals(b2, tickBroad);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSendEvent() {
        data = new Data(Data.Type.Images, 3000);
        s = new Student();
        m = new Model("guy", data, s);
        modelEvent = new TrainModelEvent(m);
        mesBus.register(g);
        mesBus.subscribeEvent(TrainModelEvent.class, g);
        mesBus.sendEvent(modelEvent);
        try {
            Message m1 = (TrainModelEvent) mesBus.awaitMessage(g);
            assertEquals(m1, modelEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testRegister() {
        assertTrue(mesBus.getMicroServiceRegistered().size() == 0);
        assertFalse(mesBus.getMicroServiceRegistered().containsKey(g));
        assertThrows("trying to add a null microservice", Exception.class, () -> mesBus.register(null));
        mesBus.register(g);
        assertTrue(mesBus.getMicroServiceRegistered().size() == 1);
        assertTrue(mesBus.getMicroServiceRegistered().containsKey(g));
    }

    @Test
    public void testUnregister() {
        mesBus.register(g);
        d = new GPUService("amit");
        assertTrue(mesBus.getMicroServiceRegistered().size() == 1);
        assertTrue(mesBus.getMicroServiceRegistered().containsKey(g));
        assertThrows("trying to add a delete a microservice that doesnt exist", Exception.class, () -> mesBus.unregister(d));
        mesBus.unregister(g);
        assertTrue(mesBus.getMicroServiceRegistered().size() == 0);
        assertFalse(mesBus.getMicroServiceRegistered().containsKey(g));
    }

    @Test
    public void testAwaitMessage() {
        mesBus.register(g);
        mesBus.subscribeEvent(TrainModelEvent.class, g);
        data = new Data(Data.Type.Images, 3000);
        s = new Student();
        m = new Model("guy", data, s);
        modelEvent = new TrainModelEvent(m);
        mesBus.sendEvent(modelEvent);
        try {
            Message t = (TrainModelEvent) mesBus.awaitMessage(g);
            assertEquals(t, modelEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}