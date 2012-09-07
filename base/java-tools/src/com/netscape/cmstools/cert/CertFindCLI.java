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
// (C) 2012 Red Hat, Inc.
// All rights reserved.
// --- END COPYRIGHT BLOCK ---

package com.netscape.cmstools.cert;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import com.netscape.certsrv.base.PKIException;
import com.netscape.certsrv.cert.CertDataInfo;
import com.netscape.certsrv.cert.CertDataInfos;
import com.netscape.certsrv.cert.CertSearchRequest;
import com.netscape.cmstools.cli.CLI;
import com.netscape.cmstools.cli.MainCLI;

/**
 * @author Endi S. Dewata
 */
public class CertFindCLI extends CLI {

    public CertCLI parent;

    public CertFindCLI(CertCLI parent) {
        super("find", "Find certificates");
        this.parent = parent;
    }

    public void printHelp() {
        formatter.printHelp(parent.name + "-" + name + " [OPTIONS...]", options);
    }

    public void execute(String[] args) {

        addOptions();

        CommandLine cmd = null;
        CertSearchRequest searchData = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            printHelp();
            System.exit(-1);
        }

        if (cmd.hasOption("help")) {
            printHelp();
            System.exit(-1);
        }

        String fileName = null;

        if (cmd.hasOption("input")) {
            fileName = cmd.getOptionValue("input");
            if (fileName == null || fileName.length() < 1) {
                System.err.println("Error: No file name specified.");
                printHelp();
                System.exit(-1);
            }
        }
        if (fileName != null) {
            FileReader reader = null;
            try {
                reader = new FileReader(fileName);
                searchData = CertSearchRequest.valueOf(reader);
            } catch (FileNotFoundException e) {
                System.err.println("Error: " + e.getMessage());
                System.exit(-1);
            } catch (JAXBException e) {
                System.err.println("Error: " + e.getMessage());
                System.exit(-1);
            } finally {
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } else {
            searchData = new CertSearchRequest();
            searchData.setSerialNumberRangeInUse(true);
        }
        String s = cmd.getOptionValue("start");
        Integer start = s == null ? null : Integer.valueOf(s);

        s = cmd.getOptionValue("size");
        Integer size = s == null ? null : Integer.valueOf(s);

        addSearchAttribute(cmd, searchData);
        CertDataInfos certs = null;
        try {
            certs = parent.client.findCerts(searchData, start, size);
        } catch (PKIException e) {
            System.err.println("Error: Cannot list certificates. " + e.getMessage());
            System.exit(-1);
        }
        if (certs.getCertInfos() == null || certs.getCertInfos().isEmpty()) {
            MainCLI.printMessage("No matches found.");
            System.exit(-1);
        }
        MainCLI.printMessage(certs.getCertInfos().size() + " certificate(s) matched");

        boolean first = true;

        for (CertDataInfo cert : certs.getCertInfos()) {
            if (first) {
                first = false;
            } else {
                System.out.println();
            }

            CertCLI.printCertInfo(cert);
        }

        MainCLI.printMessage("Number of entries returned " + certs.getCertInfos().size());
    }

    public void addOptions() {

        Option option = null;

        //pagination options
        option = new Option(null, "start", true, "Page start");
        option.setArgName("start");
        options.addOption(option);

        option = new Option(null, "size", true, "Page size");
        option.setArgName("size");
        options.addOption(option);

        //help
        options.addOption(null, "help", false, "Show help options");

        //file input
        option = new Option(null, "input", true, "File containing the search constraints");
        option.setArgName("file path");
        options.addOption(option);

        //serialNumberinUse
        option = new Option(null, "minSerialNumber", true, "Minimum serial number");
        option.setArgName("serial number");
        options.addOption(option);
        option = new Option(null, "maxSerialNumber", true, "Maximum serial number");
        option.setArgName("serial number");
        options.addOption(option);

        //subjectNameinUse
        option = new Option(null, "name", true, "Subject's common name");
        option.setArgName("name");
        options.addOption(option);
        option = new Option(null, "email", true, "Subject's email address");
        option.setArgName("email");
        options.addOption(option);
        option = new Option(null, "uid", true, "Subject's userid");
        option.setArgName("user id");
        options.addOption(option);
        option = new Option(null, "org", true, "Subject's organization");
        option.setArgName("name");
        options.addOption(option);
        option = new Option(null, "orgUnit", true, "Subject's organization unit");
        option.setArgName("name");
        options.addOption(option);
        option = new Option(null, "locality", true, "Subject's locality");
        option.setArgName("name");
        options.addOption(option);
        option = new Option(null, "state", true, "Subject's state");
        option.setArgName("name");
        options.addOption(option);
        option = new Option(null, "country", true, "Subject's country");
        option.setArgName("name");
        options.addOption(option);
        options.addOption(null, "matchExactly", false, "Match exactly with the details provided");

        //revokedByInUse
        option = new Option(null, "revokedBy", true, "Certificate revoked by");
        option.setArgName("user id");
        options.addOption(option);

        //revocationPeriod
        option = new Option(null, "revokedOnFrom", true, "Revoked on or after this date");
        option.setArgName("date");
        options.addOption(option);
        option = new Option(null, "revokedOnTo", true, "Revoked on or before this date");
        option.setArgName("date");
        options.addOption(option);

        //revocationReason
        option = new Option(null, "revocationReason", true, "Reason for revocation");
        option.setArgName("reason");
        options.addOption(option);

        //issuedBy
        option = new Option(null, "issuedBy", true, "Issued by");
        option.setArgName("user id");
        options.addOption(option);

        //issuedFor(period)
        option = new Option(null, "issuedOn", true, "Date issued");
        option.setArgName("date");
        options.addOption(option);

        //certTypeinUse
        option = new Option(null, "certTypeSubEmailCA", true, "Certifiate type: Subject Email CA");
        option.setArgName("on|off");
        options.addOption(option);
        option = new Option(null, "certTypeSubSSLCA", true, "Certificate type: Subject SSL CA");
        option.setArgName("on|off");
        options.addOption(option);
        option = new Option(null, "certTypeSecureEmail", true, "Certifiate Type: Secure Email");
        option.setArgName("on|off");
        options.addOption(option);
        option = new Option(null, "certTypeSSLClient", true, "Certifiate Type: SSL Client");
        option.setArgName("on|off");
        options.addOption(option);
        option = new Option(null, "certTypeSSLServer", true, "Certifiate Type: SSL Server");
        option.setArgName("on|off");
        options.addOption(option);

        //validationNotBeforeInUse
        option = new Option(null, "validNotBeforeFrom", true, "Valid not before start date");
        option.setArgName("date");
        options.addOption(option);
        option = new Option(null, "validNotBeforeTo", true, "Valid not before end date");
        option.setArgName("date");
        options.addOption(option);

        //validityNotAfterinUse
        option = new Option(null, "validNotAfterFrom", true, "Valid not after start date");
        option.setArgName("date");
        options.addOption(option);
        option = new Option(null, "validNotAfterTo", true, "Valid not after end date");
        option.setArgName("date");
        options.addOption(option);

        //validityLengthinUse
        option = new Option(null, "validityOperation", true, "Validity operation: \"<=\" or \">=\"");
        option.setArgName("operation");
        options.addOption(option);
        option = new Option(null, "validityCount", true, "Validity count");
        option.setArgName("count");
        options.addOption(option);
        option = new Option(null, "validityUnit", true, "Validity unit");
        option.setArgName("milliseconds");
        options.addOption(option);
    }

    public void addSearchAttribute(CommandLine cmd, CertSearchRequest csd) {
        if (cmd.hasOption("minSerialNumber")) {
            csd.setSerialNumberRangeInUse(true);
            csd.setSerialFrom(cmd.getOptionValue("minSerialNumber"));
        }
        if (cmd.hasOption("maxSerialNumber")) {
            csd.setSerialNumberRangeInUse(true);
            csd.setSerialTo(cmd.getOptionValue("maxSerialNumber"));
        }
        if (cmd.hasOption("name")) {
            csd.setSubjectInUse(true);
            csd.setCommonName(cmd.getOptionValue("name"));
        }
        if (cmd.hasOption("email")) {
            csd.setSubjectInUse(true);
            csd.setEmail(cmd.getOptionValue("email"));
        }
        if (cmd.hasOption("uid")) {
            csd.setSubjectInUse(true);
            csd.setUserID(cmd.getOptionValue("uid"));
        }
        if (cmd.hasOption("org")) {
            csd.setSubjectInUse(true);
            csd.setOrg(cmd.getOptionValue("org"));
        }
        if (cmd.hasOption("orgUnit")) {
            csd.setSubjectInUse(true);
            csd.setOrgUnit(cmd.getOptionValue("orgUnit"));
        }
        if (cmd.hasOption("locality")) {
            csd.setSubjectInUse(true);
            csd.setLocality(cmd.getOptionValue("locality"));
        }
        if (cmd.hasOption("state")) {
            csd.setSubjectInUse(true);
            csd.setState(cmd.getOptionValue("state"));
        }
        if (cmd.hasOption("country")) {
            csd.setSubjectInUse(true);
            csd.setCountry(cmd.getOptionValue("country"));
        }
        if (cmd.hasOption("matchExactly")) {
            csd.setMatchExactly(true);
        }
        if (cmd.hasOption("revokedBy")) {
            csd.setRevokedByInUse(true);
            csd.setRevokedBy(cmd.getOptionValue("revokedBy"));
        }
        if (cmd.hasOption("revokedOnFrom")) {
            csd.setRevokedOnInUse(true);
            csd.setRevokedOnFrom(cmd.getOptionValue("revokedOnFrom"));
        }
        if (cmd.hasOption("revokedOnTo")) {
            csd.setRevokedOnInUse(true);
            csd.setRevokedOnTo(cmd.getOptionValue("revokedOnTo"));
        }
        if (cmd.hasOption("revocationReason")) {
            csd.setRevocationReasonInUse(true);
            csd.setRevocationReason(cmd.getOptionValue("revocationReason"));
        }
        if (cmd.hasOption("issuedBy")) {
            csd.setIssuedByInUse(true);
            csd.setIssuedBy(cmd.getOptionValue("issuedBy"));
        }
        if (cmd.hasOption("issuedOn")) {
            csd.setIssuedOnInUse(true);
            csd.setIssuedOnFrom(cmd.getOptionValue("issuedOn"));
        }
        if (cmd.hasOption("certTypeSubEmailCA")) {
            csd.setCertTypeInUse(true);
            csd.setCertTypeSubEmailCA(cmd.getOptionValue("certTypeSubEmailCA"));
        }
        if (cmd.hasOption("certTypeSubSSLCA")) {
            csd.setCertTypeInUse(true);
            csd.setCertTypeSubSSLCA(cmd.getOptionValue("certTypeSubSSLCA"));
        }
        if (cmd.hasOption("certTypeSecureEmail")) {
            csd.setCertTypeInUse(true);
            csd.setCertTypeSecureEmail(cmd.getOptionValue("certTypeSecureEmail"));
        }
        if (cmd.hasOption("certTypeSSLClient")) {
            csd.setCertTypeInUse(true);
            csd.setCertTypeSSLClient(cmd.getOptionValue("certTypeSSLCllient"));
        }
        if (cmd.hasOption("certTypeSSLServer")) {
            csd.setCertTypeInUse(true);
            csd.setCertTypeSSLServer(cmd.getOptionValue("certTypeSSLServer"));
        }
        if (cmd.hasOption("validNotBeforeFrom")) {
            csd.setValidNotBeforeInUse(true);
            csd.setValidNotBeforeFrom(cmd.getOptionValue("validNotBeforeFrom"));
        }
        if (cmd.hasOption("validNotBeforeTo")) {
            csd.setValidNotBeforeInUse(true);
            csd.setValidNotBeforeTo(cmd.getOptionValue("validNotBeforeTo"));
        }
        if (cmd.hasOption("validNotAfterFrom")) {
            csd.setValidNotAfterInUse(true);
            csd.setValidNotAfterFrom(cmd.getOptionValue("validNotAfterFrom"));
        }
        if (cmd.hasOption("validNotAfterTo")) {
            csd.setValidNotAfterInUse(true);
            csd.setValidNotAfterTo(cmd.getOptionValue("validNotAfterTo"));
        }
        if (cmd.hasOption("validityOperation")) {
            csd.setValidityLengthInUse(true);
            csd.setValidityOperation(cmd.getOptionValue("validityOperation"));
        }
        if (cmd.hasOption("validityCount")) {
            csd.setValidityLengthInUse(true);
            csd.setValidityCount(cmd.getOptionValue("validityCount"));
        }
        if (cmd.hasOption("validityUnit")) {
            csd.setValidityLengthInUse(true);
            csd.setValidityUnit(cmd.getOptionValue("validityUnit"));
        }

    }
}