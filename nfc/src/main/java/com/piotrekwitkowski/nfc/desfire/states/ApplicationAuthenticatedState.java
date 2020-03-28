package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.desfire.Command;
import com.piotrekwitkowski.nfc.desfire.Commands;
import com.piotrekwitkowski.nfc.desfire.File;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;
import com.piotrekwitkowski.nfc.se.Application;

@SuppressWarnings("FieldCanBeLocal")
public
class ApplicationAuthenticatedState extends State {
    private static final String TAG = "ApplicationAuthenticatedState";
    private final Application application;
    private final byte[] sessionKey;

    public ApplicationAuthenticatedState(Application application, byte[] sessionKey) {
        this.application = application;
        this.sessionKey = sessionKey;
    }

    public CommandResult processCommand(Command command) {
        Log.i(TAG, "processCommand()");

        if (command.getCode() == Commands.READ_DATA) {
            byte[] commandData = command.getData();
            if (commandData.length == 7) {
                return readData(commandData);
            } else {
                return new CommandResult(this, ResponseCodes.LENGTH_ERROR);
            }
        } else {
            return new CommandResult(this, ResponseCodes.ILLEGAL_COMMAND);
        }
    }

    private CommandResult readData(byte[] commandData) {
        byte fileNumber = commandData[0];
        if (fileNumber == 0) {
            File file = application.getFile0();
            return readFile(file, commandData);
        } else {
            return new CommandResult(this, ResponseCodes.FILE_NOT_FOUND);
        }
    }

    private CommandResult readFile(File file, byte[] commandData) {
        byte[] offsetBytes = new byte[] {commandData[1], commandData[2], commandData[3]};
        byte[] lengthBytes = new byte[] {commandData[4], commandData[5], commandData[6]};
        int offset = ByteUtils.threeBytesToInt(offsetBytes);
        int length = ByteUtils.threeBytesToInt(lengthBytes);

        try {
            byte[] data = file.readData(offset, length);
            data = ByteUtils.concatenate(data, getCRC(data));
            return new CommandResult(this, ByteUtils.concatenate(ResponseCodes.SUCCESS, data));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommandResult(this, ResponseCodes.BOUNDARY_ERROR);
        }
    }

    private byte[] getCRC(byte[] data) {
        // TODO: implement CRC
        return new byte[8];
    }

}

