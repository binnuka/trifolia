﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.42000
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace Trifolia.Web.Properties {
    
    
    [global::System.Runtime.CompilerServices.CompilerGeneratedAttribute()]
    [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.VisualStudio.Editors.SettingsDesigner.SettingsSingleFileGenerator", "14.0.0.0")]
    internal sealed partial class Settings : global::System.Configuration.ApplicationSettingsBase {
        
        private static Settings defaultInstance = ((Settings)(global::System.Configuration.ApplicationSettingsBase.Synchronized(new Settings())));
        
        public static Settings Default {
            get {
                return defaultInstance;
            }
        }
        
        [global::System.Configuration.ApplicationScopedSettingAttribute()]
        [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
        [global::System.Configuration.DefaultSettingValueAttribute("Request to join Trifolia group")]
        public string JoinGroupEmailSubject {
            get {
                return ((string)(this["JoinGroupEmailSubject"]));
            }
        }
        
        [global::System.Configuration.ApplicationScopedSettingAttribute()]
        [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
        [global::System.Configuration.DefaultSettingValueAttribute("<html><body>{0} {1} ({2}) has requested to join the group \"{3}\". Click <a href=\"{" +
            "4}\" target=\"_new\">here</a> to edit the group and grant the user membership.</bod" +
            "y></html>")]
        public string JoinGroupEmailBodyHtml {
            get {
                return ((string)(this["JoinGroupEmailBodyHtml"]));
            }
        }
        
        [global::System.Configuration.ApplicationScopedSettingAttribute()]
        [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
        [global::System.Configuration.DefaultSettingValueAttribute("{0} {1} ({2}) has requested to join the group \"{3}\". Go to {4} to edit the group " +
            "and grant the user membership.")]
        public string JoinGroupEmailBodyText {
            get {
                return ((string)(this["JoinGroupEmailBodyText"]));
            }
        }
    }
}