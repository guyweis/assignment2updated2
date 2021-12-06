package bgu.spl.mics.application.objects;


import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	private ArrayDeque <CPU> CpuQueue;
	private HashMap <String,GPU> EventsSubscriptionList;
	private LinkedList <DataBatch> cpuProcessedList;

	public Cluster (ArrayDeque <CPU> CpuQueue, HashMap <String,GPU> )
	{
		cluster = getClusterInstance();
		data = new LinkedList<DataBatch>();
		processedCpuData = new LinkedList <DataBatch>();
		cores = c;
		time = 0;
	}

	public static Cluster getClusterInstance() {
		return clusterInstance;
	}

	static Cluster clusterInstance = new Cluster();


	public void addProcessedCpuDataBatchToCluster(DataBatch d)
	{
		cpuProcessedList.add(d);
	}
}
