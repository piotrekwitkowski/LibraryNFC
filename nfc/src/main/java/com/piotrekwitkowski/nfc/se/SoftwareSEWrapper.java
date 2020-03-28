package com.piotrekwitkowski.nfc.se;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.desfire.AID;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;
import com.piotrekwitkowski.nfc.desfire.states.ApplicationAuthenticatedState;
import com.piotrekwitkowski.nfc.desfire.states.ApplicationNotFoundException;
import com.piotrekwitkowski.nfc.desfire.states.ApplicationSelectedState;
import com.piotrekwitkowski.nfc.desfire.states.CommandResult;
import com.piotrekwitkowski.nfc.desfire.states.State;

public class SoftwareSEWrapper extends SEWrapper {
    private static final String TAG = "SoftwareSEWrapper";
    private final Application[] applications;
    private Application selectedApplication;
    private Authentication authentication;
    private byte[] sessionKey;

    public SoftwareSEWrapper(Application[] applications) {
        this.applications = applications;
    }

    @Override
    public State selectApplication(AID aidToSelect) throws ApplicationNotFoundException {
        Log.i(TAG, "selectApplication()");
        for (Application a : applications) {
            if (a.getAid().equals(aidToSelect)) {
                this.selectedApplication = a;
                return new ApplicationSelectedState(this);
            }
        }
        throw new ApplicationNotFoundException();
    }

    @Override
    public byte[] initiateAESAuthentication(byte keyNumber) throws AuthenticationException, NoSuchKeyException {
        try {
            this.authentication = new Authentication(this.selectedApplication);
            return this.authentication.initiate(keyNumber);
        } catch (NoSuchKeyException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }

    @Override
    public CommandResult proceedAuthentication(byte[] readerChallenge) throws AuthenticationException {
        try {
            AuthenticationResponse authenticationResponse = this.authentication.proceed(readerChallenge);
            this.sessionKey = authenticationResponse.getSessionKey();

            byte[] response = ByteUtils.concatenate(ResponseCodes.SUCCESS, authenticationResponse.getEncryptedRotatedA());
            return new CommandResult(new ApplicationAuthenticatedState(), response);
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }


}
