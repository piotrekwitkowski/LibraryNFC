package com.piotrekwitkowski.nfc.se;

import com.piotrekwitkowski.nfc.desfire.AID;
import com.piotrekwitkowski.nfc.desfire.states.ApplicationNotFoundException;
import com.piotrekwitkowski.nfc.desfire.states.CommandResult;
import com.piotrekwitkowski.nfc.desfire.states.State;

public abstract class SEWrapper {
    public abstract State selectApplication(AID aidToSelect) throws ApplicationNotFoundException;

    public abstract byte[] initiateAESAuthentication(byte keyNumber) throws AuthenticationException, NoSuchKeyException;

    public abstract CommandResult proceedAuthentication(byte[] readerChallenge) throws AuthenticationException;
}
