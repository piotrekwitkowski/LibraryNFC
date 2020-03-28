# LibraryNFC
is a school project to emulate NXP's MIFARE DESFire-based library ID cards with Android's Host Card Emulation.

## LibraryHCE
This module offers emulation of a contactless library ID card. Emulation uses native MIFARE DESFire command set. The DESFire protocol implemented here is [this reverse engineered version](https://github.com/revk/DESFireAES/blob/master/DESFire.pdf). 

[See the list of supported commands.](nfc/src/main/java/com/piotrekwitkowski/nfc/desfire/Commands.java)

## LibraryReader
This module enables reading data from physical and emulated DESFire-based library ID cards.

Its [programmed use-case](LibraryReader/src/main/java/com/piotrekwitkowski/libraryreader) is to 
1. connect with a DESFire card (physical or with a specific Android application)
2. select a library DESFire Application
3. authenticate with AES key
4. read data from a DESFire File

## Configuration

### [HCE AID](https://developer.android.com/guide/topics/connectivity/nfc/hce#ManifestDeclaration)
The "Android" AID used by both HCE and Reader Android applications can be configured in the [values.xml](nfc/src/main/res/values.xml) file of the nfc helper library.

### Emulated library ID
The data (Application AID, AES key, Data Files) of the emulated DESFire Application can be configured [here](LibraryHCE/src/main/java/com/piotrekwitkowski/libraryhce/application).

### Library ID reader
The data (Application AID and AES key) of the Reader module can be configured [here](LibraryReader/src/main/java/com/piotrekwitkowski/libraryreader/LibraryReader.java).

## Deployment
To deploy the applications two NFC-capable Android phones are needed. My setup included motorola one (Emulator) and Nexus 4 (Reader). I have tested emulation with some Sony and Huawei phones and it didn't work so well. You can use [CTSVerifier](https://source.android.com/compatibility/cts/verifier) to test your phone's NFC capabilities.

I was using Android Studio version 3.6.1.
