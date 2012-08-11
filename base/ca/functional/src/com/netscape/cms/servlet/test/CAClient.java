//--- BEGIN COPYRIGHT BLOCK ---
//This program is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; version 2 of the License.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License along
//with this program; if not, write to the Free Software Foundation, Inc.,
//51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
//
//(C) 2012 Red Hat, Inc.
//All rights reserved.
//--- END COPYRIGHT BLOCK ---
package com.netscape.cms.servlet.test;

import java.net.URISyntaxException;
import java.util.Collection;

import com.netscape.certsrv.dbs.certdb.CertId;
import com.netscape.certsrv.request.RequestId;
import com.netscape.cms.client.cli.ClientConfig;
import com.netscape.cms.servlet.cert.CertResource;
import com.netscape.cms.servlet.cert.model.CertDataInfos;
import com.netscape.cms.servlet.cert.model.CertSearchData;
import com.netscape.cms.servlet.cert.model.CertificateData;
import com.netscape.cms.servlet.csadmin.PKIClient;
import com.netscape.cms.servlet.profile.ProfileResource;
import com.netscape.cms.servlet.profile.model.ProfileData;
import com.netscape.cms.servlet.profile.model.ProfileDataInfos;
import com.netscape.cms.servlet.request.CertRequestResource;
import com.netscape.cms.servlet.request.model.AgentEnrollmentRequestData;
import com.netscape.cms.servlet.request.model.CertRequestInfo;
import com.netscape.cms.servlet.request.model.CertRequestInfos;
import com.netscape.cms.servlet.request.model.EnrollmentRequestData;

public class CAClient extends PKIClient {

    private CertResource certClient;
    private CertRequestResource certRequestClient;
    private ProfileResource profileClient;

    public CAClient(ClientConfig config) throws URISyntaxException {
        super(config);

        certRequestClient = createProxy(CertRequestResource.class);
        certClient = createProxy(CertResource.class);
        profileClient = createProxy(ProfileResource.class);
    }

    public Collection<CertRequestInfo> listRequests(String requestState, String requestType) {
        CertRequestInfos infos = null;
        Collection<CertRequestInfo> list = null;
        infos = certRequestClient.listRequests(
                requestState, requestType, new RequestId(0), 100, 100, 10);
        list = infos.getRequests();

        return list;
    }

    public CertDataInfos listCerts(String status) {
        return certClient.listCerts(status, 100, 10);
    }

    public CertDataInfos searchCerts(CertSearchData data) {
        return certClient.searchCerts(data, 100, 10);
    }

    public ProfileDataInfos listProfiles() {
        return profileClient.listProfiles();
    }

    public ProfileData getProfile(String id) {

        if (id == null) {
            return null;
        }

        return profileClient.retrieveProfile(id);
    }

    public CertificateData getCertData(CertId id) {

        if (id == null) {
            return null;
        }

        return certClient.getCert(id);

    }

    public CertRequestInfos enrollCertificate(EnrollmentRequestData data) {
        if (data == null) {
            return null;
        }

        return certRequestClient.enrollCert(data);
    }

    public CertRequestInfo getRequest(RequestId id) {
        if (id == null) {
            return null;
        }
        return certRequestClient.getRequestInfo(id);
    }

    public AgentEnrollmentRequestData reviewRequest(RequestId id) {
        if (id == null) {
            return null;
        }
        return certRequestClient.reviewRequest(id);
    }

    public void approveRequest(RequestId id, AgentEnrollmentRequestData data) {
        certRequestClient.approveRequest(id, data);
    }

    public void rejectRequest(RequestId id, AgentEnrollmentRequestData data) {
        certRequestClient.rejectRequest(id, data);
    }

    public void cancelRequest(RequestId id, AgentEnrollmentRequestData data) {
        certRequestClient.cancelRequest(id, data);
    }

    public void updateRequest(RequestId id, AgentEnrollmentRequestData data) {
        certRequestClient.updateRequest(id, data);
    }

    public void validateRequest(RequestId id, AgentEnrollmentRequestData data) {
        certRequestClient.validateRequest(id, data);
    }

    public void unassignRequest(RequestId id, AgentEnrollmentRequestData data) {
        certRequestClient.unassignRequest(id, data);
    }

}