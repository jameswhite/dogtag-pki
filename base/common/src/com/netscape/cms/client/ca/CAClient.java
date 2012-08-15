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
package com.netscape.cms.client.ca;

import java.net.URISyntaxException;
import java.util.Collection;

import com.netscape.certsrv.cert.CertData;
import com.netscape.certsrv.cert.CertDataInfos;
import com.netscape.certsrv.cert.CertEnrollmentRequest;
import com.netscape.certsrv.cert.CertRequestInfo;
import com.netscape.certsrv.cert.CertRequestInfos;
import com.netscape.certsrv.cert.CertRequestResource;
import com.netscape.certsrv.cert.CertResource;
import com.netscape.certsrv.cert.CertReviewResponse;
import com.netscape.certsrv.cert.CertSearchRequest;
import com.netscape.certsrv.dbs.certdb.CertId;
import com.netscape.certsrv.profile.ProfileData;
import com.netscape.certsrv.profile.ProfileDataInfos;
import com.netscape.certsrv.profile.ProfileResource;
import com.netscape.certsrv.request.RequestId;
import com.netscape.cms.client.ClientConfig;
import com.netscape.cms.client.PKIClient;

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

    public CertDataInfos searchCerts(CertSearchRequest data) {
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

    public CertData getCertData(CertId id) {

        if (id == null) {
            return null;
        }

        return certClient.getCert(id);

    }

    public CertRequestInfos enrollCertificate(CertEnrollmentRequest data) {
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

    public CertReviewResponse reviewRequest(RequestId id) {
        if (id == null) {
            return null;
        }
        return certRequestClient.reviewRequest(id);
    }

    public void approveRequest(RequestId id, CertReviewResponse data) {
        certRequestClient.approveRequest(id, data);
    }

    public void rejectRequest(RequestId id, CertReviewResponse data) {
        certRequestClient.rejectRequest(id, data);
    }

    public void cancelRequest(RequestId id, CertReviewResponse data) {
        certRequestClient.cancelRequest(id, data);
    }

    public void updateRequest(RequestId id, CertReviewResponse data) {
        certRequestClient.updateRequest(id, data);
    }

    public void validateRequest(RequestId id, CertReviewResponse data) {
        certRequestClient.validateRequest(id, data);
    }

    public void unassignRequest(RequestId id, CertReviewResponse data) {
        certRequestClient.unassignRequest(id, data);
    }

}