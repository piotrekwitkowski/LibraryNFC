package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.desfire.Command;
import com.piotrekwitkowski.nfc.desfire.Commands;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;
import com.piotrekwitkowski.nfc.desfire.aids.AID;
import com.piotrekwitkowski.nfc.desfire.aids.AIDWrongLengthException;
import com.piotrekwitkowski.nfc.desfire.applications.Application;
import com.piotrekwitkowski.nfc.desfire.applications.LibraryApplication;

public class InitialState extends State {
    private static final String TAG = "InitialState";
    private final Application[] applications;

    public InitialState() throws AIDWrongLengthException {
        this.applications = new Application[] {
                new LibraryApplication(),
                // here other applications
        };
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
            Application application = getApplication(aidToSelect);
            return new CommandResult(new ApplicationSelectedState(application), ResponseCodes.SUCCESS);
        } catch (AIDWrongLengthException ex) {
            return new CommandResult(this, ResponseCodes.LENGTH_ERROR);
        } catch (ApplicationNotFoundException ex) {
            return new CommandResult(this, ResponseCodes.APPLICATION_NOT_FOUND);
        }
    }

    private Application getApplication(AID aid) throws ApplicationNotFoundException {
        for (Application a : applications) {
            if (a.getAid().equals(aid)) {
                return a;
            }
        }
        throw new ApplicationNotFoundException();
    }

}
