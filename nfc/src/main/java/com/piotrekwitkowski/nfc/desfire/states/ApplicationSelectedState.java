package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.desfire.Commands;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;
import com.piotrekwitkowski.nfc.desfire.Command;
import com.piotrekwitkowski.nfc.se.AuthenticationException;
import com.piotrekwitkowski.nfc.se.NoSuchKeyException;
import com.piotrekwitkowski.nfc.se.SEWrapper;

public class ApplicationSelectedState extends State {
    private static final String TAG = "ApplicationSelectedState";
    private final SEWrapper se;
    private boolean authenticationInProgress = false;

    public ApplicationSelectedState(SEWrapper se) {
        this.se = se;
    }

    public CommandResult processCommand(Command command) {
        Log.i(TAG, "processCommand()");

        byte commandCode = command.getCode();
        byte[] commandData = command.getData();

        try {
            if (!authenticationInProgress && commandCode == Commands.AUTHENTICATE_AES) {
                return new CommandResult(this, ByteUtils.concatenate(ResponseCodes.ADDITIONAL_FRAME, authenticateAES(commandData)));
            } else if (authenticationInProgress && commandCode == Commands.ADDITIONAL_FRAME) {
                return proceedAuthentication(commandData);
            } else {
                return new CommandResult(this, ResponseCodes.ILLEGAL_COMMAND);
            }
        } catch (AuthenticationException e) {
            return new CommandResult(this, ResponseCodes.AUTHENTICATION_ERROR);
        } catch (CommandDataLengthException e) {
            return new CommandResult(this, ResponseCodes.LENGTH_ERROR);
        } catch (NoSuchKeyException e) {
            return new CommandResult(this, ResponseCodes.NO_SUCH_KEY);
        }
    }

    private byte[] authenticateAES(byte[] commandData) throws AuthenticationException, CommandDataLengthException, NoSuchKeyException {
        if (commandData.length == 1) {
            byte[] challenge = se.initiateAESAuthentication(commandData[0]);
            Log.i(TAG, "challenge: " + ByteUtils.toHexString(challenge));
            this.authenticationInProgress = true;
            return challenge;
        } else {
            throw new CommandDataLengthException();
        }
    }

    private CommandResult proceedAuthentication(byte[] commandData) throws AuthenticationException, CommandDataLengthException {
        if (commandData.length == 16) {
            CommandResult commandResult = se.proceedAuthentication(commandData);
            this.authenticationInProgress = false;
            return commandResult;
        } else {
            throw new CommandDataLengthException();
        }
    }

}