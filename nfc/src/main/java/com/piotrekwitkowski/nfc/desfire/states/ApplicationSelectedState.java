package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.desfire.Commands;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;
import com.piotrekwitkowski.nfc.desfire.Command;
import com.piotrekwitkowski.nfc.se.AuthenticationException;
import com.piotrekwitkowski.nfc.se.SEWrapper;

public class ApplicationSelectedState extends State {
    private static final String TAG = "ApplicationSelectedState";
    private final SEWrapper se;

    public ApplicationSelectedState(SEWrapper se) {
        this.se = se;
    }

    public CommandResult processCommand(Command command) {
        Log.i(TAG, "processCommand()");

        if (command.getCode() == Commands.AUTHENTICATE_AES) {
            byte[] commandData = command.getData();
            if (commandData.length == 1) {
                return authenticateAES(commandData);
            } else {
                return new CommandResult(this, ResponseCodes.LENGTH_ERROR);
            }
        } else {
            return new CommandResult(this, ResponseCodes.ILLEGAL_COMMAND);
        }
    }

    private CommandResult authenticateAES(byte[] commandData) {
        try {
            byte keyNumber = commandData[0];
            return se.initiateAESAuthentication(keyNumber);
        } catch (AuthenticationException authenticationException) {
            return new CommandResult(this, ResponseCodes.AUTHENTICATION_ERROR);
        }
    }

}