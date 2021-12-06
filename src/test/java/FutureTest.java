import static org.junit.Assert.*;
import bgu.spl.mics.Future;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class FutureTest
{
    private static Future <String> future;
    private Thread Thread1;
    private Thread Thread2;
    private int x;

    @Before
    public void setUp() throws Exception
    {
        future = new Future();
        x =0;
    }

    @After
    public void tearDown() {
        future.resolve(null);
    }

    @Test
    public void testResolve ()
    {
        assertNull( future.get());
        future.resolve("good");
        assertEquals("good", future.get());
    }

    @Test
    public void testIsDone ()
    {
        assertFalse(future.isDone());
        future.resolve("good");
        assertTrue(future.isDone());
    }

    @Test
    public void TestGet()
    {
        assertNull(future.get());
        future.resolve("RESULT");
        assertEquals("RESULT", future.get());
    }

    @Test
    public void TestGetSec()
    {
        Thread t1 = new Thread(() ->{
            try{
                Thread.sleep(1000);}
            catch(Exception e)
            {
             e.printStackTrace();
            }
            future.resolve("RESULT");
        });
        t1.start();
        try
        {
            Thread.sleep(100);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        assertNull(future.get());
        try
        {
            Thread.sleep(5000);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        assertEquals("RESULT", future.get());
    }
}
