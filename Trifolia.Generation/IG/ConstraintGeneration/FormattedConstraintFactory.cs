﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Reflection;

using Trifolia.DB;
using Trifolia.Shared;
using Trifolia.Shared.Plugins;

namespace Trifolia.Generation.IG.ConstraintGeneration
{
    public class FormattedConstraintFactory
    {
        private static Dictionary<DateTime, Type> versions = new Dictionary<DateTime, Type>()
        {
            { new DateTime(2014, 4, 21), typeof(FormattedConstraint20150421) },
            { new DateTime(2014, 4, 15), typeof(FormattedConstraint20140415) },
            { new DateTime(2016, 11, 28), typeof(FormattedConstraint20161128) }
        };

        public static IFormattedConstraint NewFormattedConstraint(
            IObjectRepository tdb,
            IGSettingsManager igSettings,
            IIGTypePlugin igTypePlugin,
            TemplateConstraint constraint,
            string templateLinkBase = null,
            string valueSetLinkBase = null,
            bool linkContainedTemplate = false,
            bool linkIsBookmark = false,
            bool createLinksForValueSets = false,
            bool includeCategory = true)
        {
            return NewFormattedConstraint(
                tdb,
                igSettings,
                igTypePlugin,
                (IConstraint)constraint,
                templateLinkBase,
                valueSetLinkBase,
                linkContainedTemplate,
                linkIsBookmark,
                createLinksForValueSets,
                includeCategory,
                constraint.ContainedTemplate,
                constraint.ValueSet,
                constraint.CodeSystem);
        }

        public static IFormattedConstraint NewFormattedConstraint(
            IObjectRepository tdb, 
            IGSettingsManager igSettings,
            IIGTypePlugin igTypePlugin,
            IConstraint constraint,
            string templateLinkBase = null,
            string valueSetLinkBase = null,
            bool linkContainedTemplate = false, 
            bool linkIsBookmark = false, 
            bool createLinksForValueSets = false,
            bool includeCategory = true,
            Template containedTemplate = null,
            ValueSet valueSet = null,
            CodeSystem codeSystem = null)
        {
            var selectedType = typeof(FormattedConstraint);

            if (igSettings == null || !igSettings.IsPublished)
            {
                var latestVersion = versions.Keys.OrderByDescending(y => y).First();
                selectedType = versions[latestVersion];
            }
            else
            {
                foreach (var versionDate in versions.Keys.OrderByDescending(y => y))
                {
                    if (igSettings.PublishDate > versionDate)
                    {
                        selectedType = versions[versionDate];
                        break;
                    }
                }
            }

            IFormattedConstraint formattedConstraint = (IFormattedConstraint)Activator.CreateInstance(selectedType);

            formattedConstraint.Tdb = tdb;
            formattedConstraint.IgSettings = igSettings;
            formattedConstraint.IncludeCategory = includeCategory;
            formattedConstraint.TemplateLinkBase = templateLinkBase;
            formattedConstraint.ValueSetLinkBase = valueSetLinkBase;
            formattedConstraint.LinkContainedTemplate = linkContainedTemplate;
            formattedConstraint.LinkIsBookmark = linkIsBookmark;
            formattedConstraint.CreateLinkForValueSets = createLinksForValueSets;

            // Set the properties in the FormattedConstraint based on the IConstraint
            formattedConstraint.ParseConstraint(igTypePlugin, constraint, containedTemplate, valueSet, codeSystem);

            // Pre-process the constraint so that calls to GetHtml(), GetPlainText(), etc. returns something
            formattedConstraint.ParseFormattedConstraint();

            return formattedConstraint;
        }
    }
}
