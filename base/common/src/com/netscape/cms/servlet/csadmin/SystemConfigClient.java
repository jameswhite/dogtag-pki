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
package com.netscape.cms.servlet.csadmin;

import java.net.URISyntaxException;

import com.netscape.cms.client.cli.ClientConfig;
import com.netscape.cms.servlet.csadmin.model.ConfigurationData;
import com.netscape.cms.servlet.csadmin.model.ConfigurationResponseData;
import com.netscape.cms.servlet.csadmin.model.InstallToken;
import com.netscape.cms.servlet.csadmin.model.InstallTokenRequest;


/**
 * @author alee
 *
 */
public class SystemConfigClient extends PKIClient {

    private SystemConfigurationResource configClient;

    public SystemConfigClient(ClientConfig config) throws URISyntaxException {
        super(config);

        configClient = createProxy(SystemConfigurationResource.class);
    }

    public ConfigurationResponseData configure(ConfigurationData data) {
        return configClient.configure(data);
    }

    public InstallToken getInstallToken(InstallTokenRequest data) {
        return configClient.getInstallToken(data);
    }
}