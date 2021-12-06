package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast
{
    private int updatedTimeTick;

    public TickBroadcast (int updatedTick)
    {
        this.updatedTimeTick = updatedTick;
    }

    public int getTick() {
        return updatedTimeTick;
    }

}
