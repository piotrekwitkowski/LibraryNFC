package com.piotrekwitkowski.libraryhce;

import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.Response;

class DESFireEmulation {
    private static final DESFireState STATE = DESFireState.INITIAL;

    byte[] getResponse(byte[] apdu) throws DESFireException {
        Response command = new Response(apdu);

        if (STATE == DESFireState.INITIAL) {
            return onStateInitial(command);
        } else if (STATE == DESFireState.APPLICATION_SELECTED) {
            return onStateApplicationSelected(command);
        } else if (STATE == DESFireState.AUTHENTICATING) {
            return onStateAuthenticating(command);
        } else if (STATE == DESFireState.APPLICATION_AUTHENTICATED) {
            return onStateApplicationAuthenticated(command);
        } else {
            throw new DESFireException();
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
