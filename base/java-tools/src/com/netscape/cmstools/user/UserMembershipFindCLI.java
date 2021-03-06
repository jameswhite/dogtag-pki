// --- BEGIN COPYRIGHT BLOCK ---
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; version 2 of the License.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along
// with this program; if not, write to the Free Software Foundation, Inc.,
// 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
//
// (C) 2013 Red Hat, Inc.
// All rights reserved.
// --- END COPYRIGHT BLOCK ---

package com.netscape.cmstools.user;

import java.util.Collection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import com.netscape.certsrv.user.UserMembershipCollection;
import com.netscape.certsrv.user.UserMembershipData;
import com.netscape.cmstools.cli.CLI;
import com.netscape.cmstools.cli.MainCLI;

/**
 * @author Endi S. Dewata
 */
public class UserMembershipFindCLI extends CLI {

    public UserMembershipCLI userMembershipCLI;

    public UserMembershipFindCLI(UserMembershipCLI userMembershipCLI) {
        super("find", "Find user memberships", userMembershipCLI);
        this.userMembershipCLI = userMembershipCLI;
    }

    public void printHelp() {
        formatter.printHelp(getFullName() + " <User ID> [OPTIONS...]", options);
    }

    public void execute(String[] args) throws Exception {

        Option option = new Option(null, "start", true, "Page start");
        option.setArgName("start");
        options.addOption(option);

        option = new Option(null, "size", true, "Page size");
        option.setArgName("size");
        options.addOption(option);

        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            printHelp();
            System.exit(1);
        }

        String[] cmdArgs = cmd.getArgs();

        if (cmdArgs.length != 1) {
            printHelp();
            System.exit(1);
        }

        String userID = cmdArgs[0];

        String s = cmd.getOptionValue("start");
        Integer start = s == null ? null : Integer.valueOf(s);

        s = cmd.getOptionValue("size");
        Integer size = s == null ? null : Integer.valueOf(s);

        UserMembershipCollection response = userMembershipCLI.userClient.findUserMemberships(userID, start, size);

        MainCLI.printMessage(response.getTotal() + " entries matched");
        if (response.getTotal() == 0) return;

        Collection<UserMembershipData> entries = response.getEntries();
        boolean first = true;

        for (UserMembershipData userMembershipData : entries) {

            if (first) {
                first = false;
            } else {
                System.out.println();
            }

            UserMembershipCLI.printUserMembership(userMembershipData);
        }

        MainCLI.printMessage("Number of entries returned "+entries.size());
    }
}
