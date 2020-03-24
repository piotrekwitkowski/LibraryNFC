package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.desfire.Commands;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;
import com.piotrekwitkowski.nfc.desfire.applications.Application;
import com.piotrekwitkowski.nfc.desfire.Command;

public class ApplicationSelectedState extends State {
    private static final String TAG = "ApplicationSelectedState";
    private final Application application;

    ApplicationSelectedState(Application application) {
        this.application = application;
    }

    public CommandResult processCommand(Command command) {
        Log.i(TAG, "processCommand()");
        Log.i(TAG, "current application: " + application.getClass().getSimpleName());

        if (command.getCode() == Commands.AUTHENTICATE_AES) {
            if (command.getData().length == 1) {
                return application.authenticateAES(command.getData()[0]);
            } else {
                return new CommandResult(this, ResponseCodes.LENGTH_ERROR);
            }
        } else {
            return new CommandResult(this, ResponseCodes.ILLEGAL_COMMAND);
        }
    }

}