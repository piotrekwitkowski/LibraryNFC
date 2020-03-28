package com.piotrekwitkowski.libraryhce.se;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.desfire.AID;
import com.piotrekwitkowski.nfc.desfire.Application;
import com.piotrekwitkowski.nfc.desfire.states.ApplicationNotFoundException;
import com.piotrekwitkowski.nfc.desfire.states.ApplicationSelectedState;
import com.piotrekwitkowski.nfc.desfire.states.CommandResult;
import com.piotrekwitkowski.nfc.desfire.states.State;
import com.piotrekwitkowski.nfc.se.AuthenticationException;
import com.piotrekwitkowski.nfc.se.SEWrapper;

public class SoftwareSEWrapper extends SEWrapper {
    private static final String TAG = "SoftwareSEWrapper";
    private final Application[] applications;
    private Application selectedApplication;

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
    public CommandResult initiateAESAuthentication(byte keyNumber) throws AuthenticationException {
        if (this.selectedApplication == null) {
            throw new AuthenticationException();
        } else {
            return this.selectedApplication.initiateAESAuthentication(keyNumber);
        }
    }

}
