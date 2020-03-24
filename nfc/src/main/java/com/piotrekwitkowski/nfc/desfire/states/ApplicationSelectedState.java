package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.desfire.applications.Application;
import com.piotrekwitkowski.nfc.desfire.Command;
import com.piotrekwitkowski.nfc.desfire.DESFireException;

public class ApplicationSelectedState extends State {
    private static final String TAG = "ApplicationSelectedState";
    private final Application application;

    ApplicationSelectedState(Application application) {
        this.application = application;
    }

    @Override
    public CommandResult processCommand(Command command) throws DESFireException {
        Log.i(TAG, "processCommand()");
        Log.i(TAG, "current application: " + application.getClass().getSimpleName());


//        if (command.getCode() == Commands.AUTHENTICATE_AES) {
//            return authenticateAES(command.getData());
//        } else {
        throw new DESFireException("Command unknown or not allowed in this state.");
//        }
    }

}