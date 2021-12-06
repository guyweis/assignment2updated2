package bgu.spl.mics.application.messages;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.Model;

public class TestModelEvent {
    private Model model;

    public TestModelEvent (Model model1) {
        this.model = model1;
    }

    public Model getModel() {
        return model;
    }
}

