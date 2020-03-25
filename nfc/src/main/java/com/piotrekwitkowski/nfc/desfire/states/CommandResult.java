package com.piotrekwitkowski.nfc.desfire.states;

public class CommandResult {
    private final State state;
    private final byte[] response;

    public CommandResult(State state, byte responseCode) {
        this.state = state;
        this.response = new byte[]{responseCode};
    }

    public CommandResult(State state, byte[] responseBytes) {
        this.state = state;
        this.response = responseBytes;
    }

    public State getState() {
        return state;
    }

    public byte[] getResponse() {
        return response;
    }
}
