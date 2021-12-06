package bgu.spl.mics;

import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Cluster;

import java.util.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private HashMap <Class<?>,ArrayDeque> EventsSubscriptionList;
	private HashMap <MicroService,ArrayDeque> MicroServiceRegistered;
	private HashMap <Class<?>,LinkedList> BroadcastSubscriptionList;
	private HashMap <Event,Future> FutureList;

	public MessageBusImpl()
	{
		MicroServiceRegistered = new HashMap<MicroService,ArrayDeque>();
		EventsSubscriptionList = new HashMap<Class<?>,ArrayDeque>();
		BroadcastSubscriptionList = new HashMap<Class<?>, LinkedList>();
		FutureList = new HashMap<Event,Future>();

		LinkedList <MicroService> PublishConferenceBroadcastList = new LinkedList<MicroService>();
		LinkedList <MicroService> TickBroadcastList = new LinkedList<MicroService>();

		BroadcastSubscriptionList.put(PublishConferenceBroadcast.class, PublishConferenceBroadcastList);
		BroadcastSubscriptionList.put(TickBroadcast.class, TickBroadcastList);

		ArrayDeque <MicroService> TrainModelQueue = new ArrayDeque<MicroService>();
		ArrayDeque <MicroService> TestModelQueue = new ArrayDeque <MicroService>();
		ArrayDeque <MicroService> PublishResultsQueue = new ArrayDeque <MicroService>();

		EventsSubscriptionList.put(TrainModelEvent.class, TrainModelQueue);
		EventsSubscriptionList.put(TestModelEvent.class, TestModelQueue);
		EventsSubscriptionList.put(PublishResultsEvent.class, PublishResultsQueue);
	}


	public static MessageBus getInstance() {
		return MessageBusInstance;
	}

	static MessageBusImpl MessageBusInstance = new MessageBusImpl();


	/**
	 * @param m the microService wanting to subscribe to an event
	 * @pre EventsSubscriptionList.contains(type)
	 * @post EventsSubscriptionList.get(type).size() = @pre EventsSubscriptionList.get(type).size() + 1
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m)
	{
		if(EventsSubscriptionList.get(type).contains(m)==false)
			EventsSubscriptionList.get(type).add(m);
	}

	/**
	 * @param m  the microservice wanting to subscribe to the Broadcast
	 * @pre BroadcastSubscriptionList.contains(type)
	 * @post BroadcastSubscriptionList.get(type).size() = @pre BroadcastSubscriptionList.get(type).size() + 1
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m)
	{
		if(EventsSubscriptionList.get(type).contains(m)==false)
			BroadcastSubscriptionList.get(type).add(m);
	}

	/**
	 * @param e the event that was completed
	 * @param result the result of the process
	 * @post dataReadyForCPUProcessing.size() = @pre dataSentCPUProcessing.size() - 1
	 */
	@Override
	public <T> void complete(Event<T> e, T result) {
		Future<T> f = FutureList.get(e);
		f.resolve(result);
	}

	/**
	 * @param b  broadcast to be sent to relevant subscribers
	 * @pre no pre conditions
	 * @post messagebus.getMicroServiceRegistered().get(messagebus.getBroadcastSubscriptionList().get(b.class).(all element in linked list).contains(b)
	 */
	@Override
	public void sendBroadcast(Broadcast b)
	{
		for(int i=0; i<BroadcastSubscriptionList.get(b.getClass()).size(); i++) {
			MicroService m = (MicroService) BroadcastSubscriptionList.get(b.getClass()).get(i);
			MicroServiceRegistered.get(m).add(b);
		}
	}

	/**
	 * @param e  event to be processed
	 * @pre no pre conditions
	 * @post messagebus.getMicroServiceRegistered().get(messagebus.getEventsSubscriptionList().get(t).peek()).contains(e)
	 * @return the result of the event
	 */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> f = new Future<T>();
		FutureList.put(e,f);
		MicroService m = (MicroService) EventsSubscriptionList.get(e.getClass()).poll();
		MicroServiceRegistered.get(m).add(e);
		EventsSubscriptionList.get(e.getClass()).add(m);
		return f;
	}

	/**
	 * @param m  microservice to be added to the message bus
	 * @pre m!=null
	 * @post @post messagebus.getMicroServiceRegistered().size() = @pre messagebus.getMicroServiceRegistered().size()+1;
	 * @post messagebus.getMicroServiceRegistered().contains(m)==true;
	 */
	@Override
	public void register(MicroService m)
	{
		ArrayDeque <Message> q = new ArrayDeque<Message>();
		MicroServiceRegistered.putIfAbsent(m,q);
	}

	/**
	 * @param m  microservice to be removed from message bus
	 * @pre m!=null
	 * @post @post messagebus.getMicroServiceRegistered().size() = @pre messagebus.getMicroServiceRegistered().size()-1;
	 * @post messagebus.getMicroServiceRegistered().contains(m)==false;
	 */
	@Override
	public void unregister(MicroService m)
	{
		MicroServiceRegistered.remove(m);
	}

	/**
	 * @param m  microService waiting for a message
	 * @pre no pre-condition
	 * @post mesBUS.getMicroServiceRegistered().get(m).size() = @pre mesBUS.getMicroServiceRegistered().get(m).size() - 1
	 * @return the message that was in the micro-service queue
	 */
	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException
	{
		Message mes = (Message) MicroServiceRegistered.get(m).poll();
		return mes;
	}

	public HashMap getEventsSubscriptionList(){
		return EventsSubscriptionList;
	}

	public HashMap getMicroServiceRegistered(){
		return MicroServiceRegistered;
	}

	public HashMap getBroadcastSubscriptionList(){
		return BroadcastSubscriptionList;
	}

	public HashMap getFutureList(){
		return FutureList;
	}


	

}
