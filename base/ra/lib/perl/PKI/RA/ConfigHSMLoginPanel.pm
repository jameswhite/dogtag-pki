#!/usr/bin/perl
#
# --- BEGIN COPYRIGHT BLOCK ---
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
# Copyright (C) 2007 Red Hat, Inc.
# All rights reserved.
# --- END COPYRIGHT BLOCK ---
#
#
#
#

use strict;
use warnings;
use PKI::RA::GlobalVar;
use PKI::RA::Common;

package PKI::RA::ConfigHSMLoginPanel;
$PKI::RA::ConfigHSMLoginPanel::VERSION = '1.00';

use PKI::RA::BasePanel;
our @ISA = qw(PKI::RA::BasePanel);

sub new { 
    my $class = shift;
    my $self = {}; 

    $self->{"isSubPanel"} = \&is_sub_panel;
    $self->{"hasSubPanel"} = \&has_sub_panel;
    $self->{"isPanelDone"} = \&PKI::RA::Common::no;
    $self->{"getPanelNo"} = &PKI::RA::Common::r(9);
    $self->{"getName"} = &PKI::RA::Common::r("Security Modules Login");
    $self->{"vmfile"} = "config_hsmloginpanel.vm";
    $self->{"update"} = \&update;
    $self->{"panelvars"} = \&display;
    bless $self,$class; 
    return $self; 
}

sub is_sub_panel
{
    my ($q) = @_;
    return 1;
}

sub has_sub_panel
{
    my ($q) = @_;
    return 0;
}

sub validate
{
    my ($q) = @_;
    &PKI::RA::Wizard::debug_log("ConfigHSMLoginPanel: validate");
    return 1;
}

sub update
{
    my ($q) = @_;
    &PKI::RA::Wizard::debug_log("ConfigHSMLoginPanel: update");
    my $uTokName = $q->param('uTokName');
    my $uPasswd = $q->param('__uPasswd');

#    &PKI::RA::Wizard::debug_log("ConfigHSMLoginPanel: update tokname= $uTokName pwd =$uPasswd");

    $::pwdconf->put($uTokName, $uPasswd);
    $::pwdconf->commit();

    return 1;
}

sub display
{
    my ($q) = @_;
    use Data::Dumper;
    $Data::Dumper::Indent = 1;
#    &PKI::RA::Wizard::debug_log("ConfigHSMLoginPanel -> dump of q=  ". Dumper($q));
    $::symbol{SecToken} = $q->param('SecToken');
#   &PKI::RA::Wizard::debug_log("ConfigHSMLoginPanel -> display has ".$q->param('SecToken'));

    &PKI::RA::Wizard::debug_log("ConfigHSMLoginPanel -> display retrieving $q->param('SecToken') ");
    my $pwd = $::pwdconf->get( $q->param('SecToken'));
    if ($pwd ne "") {
        &PKI::RA::Wizard::debug_log("ConfigHSMLoginPanel -> display retrieved pwd from pwdconf");
    }

    return 1;
}

1;
