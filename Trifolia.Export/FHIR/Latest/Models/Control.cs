﻿using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Trifolia.Export.FHIR.Latest.Models
{
    public class Control
    {
        public Control()
        {
            this.tool = "jekyll";
            this.sct_edition = "http://snomed.info/sct/731000124108";
            this.paths = new Path();

            this.defaults = new Dictionary<string, TemplateReference>();
            this.defaults.Add("Any", new TemplateReference("instance-template-format.html", "instance-template-base.html"));
            this.defaults.Add("StructureDefinition", new TemplateReference(null, "instance-template-sd.html"));

            this.resources = new Dictionary<string, ResourceReference>();
            this.special_urls = new List<string>();
            this.dependencyList = new List<Dependency>();
        }

        public string tool { get; set; }
        public Path paths { get; set; }
        public Dictionary<string, TemplateReference> defaults { get; set; }
        public string canonicalBase { get; set; }
        public List<Dependency> dependencyList { get; set; }
        public string source { get; set; }

        [JsonProperty(PropertyName = "sct-edition", NullValueHandling = NullValueHandling.Ignore)]
        public string sct_edition { get; set; }

        [JsonProperty(PropertyName = "special-urls", NullValueHandling = NullValueHandling.Ignore)]
        public List<string> special_urls { get; set; }

        public Dictionary<string, ResourceReference> resources { get; set; }

        public class Dependency
        {
            public string name { get; set; }
            public string location { get; set; }
            public string source { get; set; }
        }

        public class Path
        {
            public Path()
            {
                this.resources = new List<string> { "resources" };
                this.pages = "pages";
                this.temp = "temp";
                this.output = "output";
                this.qa = "qa";
                this.specification = "http://hl7-fhir.github.io/";
            }

            public List<string> resources { get; set; }
            public string pages { get; set; }
            public string temp { get; set; }
            public string output { get; set; }
            public string qa { get; set; }
            public string specification { get; set; }
        }

        public class ResourceReference
        {
            public ResourceReference(string reference_base)
            {
                this.reference_base = reference_base;
            }

            public ResourceReference(string template_base, string reference_base)
            {
                this.template_base = template_base;
                this.reference_base = reference_base;
            }

            public ResourceReference()
            {

            }

            [JsonProperty(PropertyName = "template-base", NullValueHandling = NullValueHandling.Ignore)]
            public string template_base { get; set; }

            [JsonProperty(PropertyName = "base", NullValueHandling = NullValueHandling.Ignore)]
            public string reference_base { get; set; }
        }

        public class TemplateReference
        {
            public TemplateReference(string template_format, string template_base)
            {
                this.template_format = template_format;
                this.template_base = template_base;
            }

            public TemplateReference()
            {

            }

            [JsonProperty(PropertyName = "template-defns", NullValueHandling = NullValueHandling.Ignore)]
            public string template_defns { get; set; }

            [JsonProperty(PropertyName = "template-format", NullValueHandling = NullValueHandling.Ignore)]
            public string template_format { get; set; }

            [JsonProperty(PropertyName = "template-base", NullValueHandling = NullValueHandling.Ignore)]
            public string template_base { get; set; }
        }
    }
}
