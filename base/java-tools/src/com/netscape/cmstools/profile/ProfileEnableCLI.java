package com.netscape.cmstools.profile;

import com.netscape.cmstools.cli.CLI;
import com.netscape.cmstools.cli.MainCLI;

public class ProfileEnableCLI extends CLI {

    public ProfileCLI profileCLI;

    public ProfileEnableCLI(ProfileCLI profileCLI) {
        super("enable", "Enable profiles", profileCLI);
        this.profileCLI = profileCLI;
    }

    public void printHelp() {
        formatter.printHelp(getFullName() + " <Profile ID>", options);
    }

    public void execute(String[] args) throws Exception {

        if (args.length != 1) {
            printHelp();
            System.exit(1);
        }

        String profileId = args[0];

        profileCLI.profileClient.enableProfile(profileId);

        MainCLI.printMessage("Enabled profile \"" + profileId + "\"");
    }

}
