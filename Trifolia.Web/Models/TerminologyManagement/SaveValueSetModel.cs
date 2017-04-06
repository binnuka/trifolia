﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Trifolia.Web.Models.TerminologyManagement
{
    public class SaveValueSetModel
    {
        public SaveValueSetModel()
        {
            this.Concepts = new List<ConceptItem>();
            this.RemovedConcepts = new List<ConceptItem>();
        }

        public ValueSetModel ValueSet { get; set; }
        public List<ConceptItem> Concepts { get; set; }
        public List<ConceptItem> RemovedConcepts { get; set; }
    }
}