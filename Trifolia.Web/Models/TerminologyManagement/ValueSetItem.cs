﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Trifolia.DB;

namespace Trifolia.Web.Models.TerminologyManagement
{
    public class ValueSetItem
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Identifiers { get; set; }
        public string Description { get; set; }
        public string Code { get; set; }
        public bool IsIntentional { get; set; }
        public string IntentionalDefinition { get; set; }
        public string SourceUrl { get; set; }
        public bool IsComplete { get; set; }
        public bool PermitModify { get; set; }
        public bool CanModify { get; set; }
        public bool CanOverride { get; set; }
        public ValueSetImportSources? ImportSource { get; set; }
        public string ImportSourceId { get; set; }
    }
}