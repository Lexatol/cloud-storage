package ru.lexp00.storage.cloud.network.common;


public abstract class Message {
    private State state;

    public Message(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}

