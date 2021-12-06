package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class PublishConferenceBroadcast implements Broadcast
{

    private int updatedTimeTick;

    public PublishConferenceBroadcast (int updatedTick)
    {
        this.updatedTimeTick = updatedTick;
    }

    public int getTick() {
        return updatedTimeTick;
    }

}
