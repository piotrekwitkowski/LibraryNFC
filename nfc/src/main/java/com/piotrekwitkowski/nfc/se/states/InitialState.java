package com.piotrekwitkowski.nfc.se.states;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.se.Command;
import com.piotrekwitkowski.nfc.desfire.Commands;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;
import com.piotrekwitkowski.nfc.desfire.AID;
import com.piotrekwitkowski.nfc.desfire.InvalidParameterException;
import com.piotrekwitkowski.nfc.se.Application;

public class InitialState extends State {
    private static final String TAG = "InitialState";
    private final Application[] applications;

    public InitialState(Application[] applications) {
        this.applications = applications;
    }

    public CommandResult processCommand(Command command) {
        Log.i(TAG, "processCommand()");

        if (command.getCode() == Commands.SELECT_APPLICATION) {
            return selectApplication(command.getData());
        } else {
            return new CommandResult(this, ResponseCodes.ILLEGAL_COMMAND);
        }
    }

    private CommandResult selectApplication(byte[] aid) {
        Log.i(TAG, "selectApplication()");

        try {
            AID aidToSelect = new AID(aid);
            return new CommandResult(selectApplication(aidToSelect), ResponseCodes.SUCCESS);
        } catch (InvalidParameterException ex) {
            return new CommandResult(this, ResponseCodes.LENGTH_ERROR);
        } catch (ApplicationNotFoundException ex) {
            return new CommandResult(this, ResponseCodes.APPLICATION_NOT_FOUND);
        }
    }

    ApplicationSelectedState selectApplication(AID aidToSelect) throws ApplicationNotFoundException {
        Log.i(TAG, "selectApplication()");
        for (Application a : applications) {
            if (a.getAid().equals(aidToSelect)) {
                return new ApplicationSelectedState(a);
            }
        }
        throw new ApplicationNotFoundException();
    }

}
