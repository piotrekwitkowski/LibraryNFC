package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.desfire.Command;
import com.piotrekwitkowski.nfc.desfire.Commands;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;
import com.piotrekwitkowski.nfc.desfire.applications.Application;

@SuppressWarnings("FieldCanBeLocal")
class ApplicationAuthenticatedState extends State {
    private static final String TAG = "ApplicationAuthenticatedState";
    private final Application application;
    private final byte[] sessionKey;

    ApplicationAuthenticatedState(Application application, byte[] sessionKey) {
        this.application = application;
        this.sessionKey = sessionKey;
    }

    public CommandResult processCommand(Command command) {
        Log.i(TAG, "processCommand()");

        if (command.getCode() == Commands.READ_DATA) {
            return getValue(command.getData());
        } else {
            return new CommandResult(this, ResponseCodes.ILLEGAL_COMMAND);
        }
    }

    private CommandResult getValue(byte[] commandData) {


        // TODO: getValue using File Number, Offset, Length
        return new CommandResult(this, ByteUtils.toByteArray("000035383536383600000048554853303538353638363000000000000000000000B6BB1FF53D881894"));
    }
}

