#!/usr/bin/python -t
# Authors:
#     Matthew Harmsen <mharmsen@redhat.com>
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; version 2 of the License.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
#
# Copyright (C) 2012 Red Hat, Inc.
# All rights reserved.
#

# PKI Deployment Imports
import pkiconfig as config
from pkiconfig import pki_master_dict as master
import pkihelper as util
import pkimessages as log
import pkiscriptlet


# PKI Deployment Initialization Scriptlet
class PkiScriptlet(pkiscriptlet.AbstractBasePkiScriptlet):
    rv = 0

    def spawn(self):
        # begin official logging
        config.pki_log.info(log.PKISPAWN_BEGIN_MESSAGE_2,
                            master['pki_subsystem'],
                            master['pki_instance_name'],
                            extra=config.PKI_INDENTATION_LEVEL_0)
        if config.str2bool(master['pki_skip_installation']):
            config.pki_log.info(log.SKIP_INITIALIZATION_SPAWN_1, __name__,
                                extra=config.PKI_INDENTATION_LEVEL_1)
            return self.rv
        else:
            config.pki_log.info(log.INITIALIZATION_SPAWN_1, __name__,
                                extra=config.PKI_INDENTATION_LEVEL_1)
            if master['pki_subsystem'] == "CA" and\
               config.str2bool(master['pki_external_step_two']):
                # verify that this type of "subsystem" currently EXISTS
                # for this "instance" (External CA Step 2)
                util.instance.verify_subsystem_exists()
                master['pki_skip_installation'] = "True";
            else:
                # verify that this type of "subsystem" does NOT yet
                # exist for this "instance"
                util.instance.verify_subsystem_does_not_exist()
                # detect and avoid any namespace collisions
                util.namespace.collision_detection()
        # initialize 'uid' and 'gid'
        util.identity.add_uid_and_gid(master['pki_user'], master['pki_group'])
        # establish 'uid' and 'gid'
        util.identity.set_uid(master['pki_user'])
        util.identity.set_gid(master['pki_group'])
        # verify existence of SENSITIVE configuration file data
        util.configuration_file.verify_sensitive_data()
        # verify existence of MUTUALLY EXCLUSIVE configuration file data
        util.configuration_file.verify_mutually_exclusive_data()
        # verify existence of PREDEFINED configuration file data
        util.configuration_file.verify_predefined_configuration_file_data()
        # verify selinux context of selected ports
        util.configuration_file.populate_non_default_ports()
        util.configuration_file.verify_selinux_ports()
        return self.rv

    def respawn(self):
        # begin official logging
        config.pki_log.info(log.PKIRESPAWN_BEGIN_MESSAGE_2,
                            master['pki_subsystem'],
                            master['pki_instance_name'],
                            extra=config.PKI_INDENTATION_LEVEL_0)
        config.pki_log.info(log.INITIALIZATION_RESPAWN_1, __name__,
                            extra=config.PKI_INDENTATION_LEVEL_1)
        # verify that this type of "subsystem" currently EXISTS
        # for this "instance"
        util.instance.verify_subsystem_exists()
        return self.rv

    def destroy(self):
        # begin official logging
        config.pki_log.info(log.PKIDESTROY_BEGIN_MESSAGE_2,
                            master['pki_subsystem'],
                            master['pki_instance_name'],
                            extra=config.PKI_INDENTATION_LEVEL_0)
        config.pki_log.info(log.INITIALIZATION_DESTROY_1, __name__,
                            extra=config.PKI_INDENTATION_LEVEL_1)
        # verify that this type of "subsystem" currently EXISTS
        # for this "instance"
        util.instance.verify_subsystem_exists()
        # verify that the command-line parameters match the values
        # that are present in the corresponding configuration file
        util.configuration_file.verify_command_matches_configuration_file()
        # establish 'uid' and 'gid'
        util.identity.set_uid(master['pki_user'])
        util.identity.set_gid(master['pki_group'])
        # get ports to remove selinux context
        util.configuration_file.populate_non_default_ports()
        # de-register instance from its Security Domain
        #
        #     NOTE:  Since the security domain of an instance must be up
        #            and running in order to be de-registered, this step
        #            must be done PRIOR to instance shutdown because this
        #            instance's security domain may be a part of a
        #            tightly-coupled shared instance.
        #
        util.security_domain.deregister()
        # ALWAYS Stop this Apache/Tomcat PKI Process
        util.systemd.stop()
        return self.rv
