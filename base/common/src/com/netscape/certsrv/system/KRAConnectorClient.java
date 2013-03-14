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

package com.netscape.certsrv.system;
import java.net.URISyntaxException;

import com.netscape.certsrv.client.ClientConfig;
import com.netscape.certsrv.client.PKIClient;
import com.netscape.certsrv.client.PKIConnection;

/**
 * @author Ade Lee
 */
public class KRAConnectorClient extends PKIClient {
    public KRAConnectorResource kraConnectorClient;

    public KRAConnectorClient(PKIConnection connection) throws URISyntaxException {
        super(connection);
        init();
    }

    public KRAConnectorClient(ClientConfig config) throws URISyntaxException {
        super(config);
        init();
    }

    public void init() throws URISyntaxException {
        kraConnectorClient = createProxy(KRAConnectorResource.class);
    }

    public void addConnector(KRAConnectorInfo info) {
        kraConnectorClient.addConnector(info);
    }

    public void removeConnector(String host, String port) {
        kraConnectorClient.removeConnector(host, port);
    }

}