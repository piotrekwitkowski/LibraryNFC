package com.piotrekwitkowski.nfc.desfire;

import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.Response;

enum State {INITIAL, APPLICATION_SELECTED, AUTHENTICATING, APPLICATION_AUTHENTICATED}

public class DESFireEmulation {
    private static State STATE = State.INITIAL;

    public byte[] getResponse(byte[] apdu) throws DESFireException {
        Response command = new Response(apdu);

        if (STATE == State.INITIAL) {
            return onStateInitial(command);
        } else if (STATE == State.APPLICATION_SELECTED) {
            return onStateApplicationSelected(command);
        } else if (STATE == State.AUTHENTICATING) {
            return onStateAuthenticating(command);
        } else if (STATE == State.APPLICATION_AUTHENTICATED) {
            return onStateApplicationAuthenticated(command);
        } else {
            STATE = State.INITIAL;
            throw new DESFireException("DESFire emulation was in an unknown state. Emulation state set to INITIAL.");
        }
    }

    private byte[] onStateInitial(Response command) {
        return ByteUtils.toByteArray("9000");
    }

    private byte[] onStateApplicationSelected(Response command) {
        return new byte[0];
    }

    private byte[] onStateAuthenticating(Response command) {
        return new byte[0];
    }

    private byte[] onStateApplicationAuthenticated(Response command) {
        return new byte[0];
    }

}
