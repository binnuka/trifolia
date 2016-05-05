using System;
using System.Xml.Serialization;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Reflection;

using Trifolia.Shared.ImportExport.Model;
using Trifolia.Shared.ImportExport;
using LantanaGroup.ValidationUtility;
using Trifolia.DB;

namespace Trifolia.Generation.XML.FHIR.DSTU1
{
    public class FHIRImporter
    {
        private const string StylesheetResource = "Trifolia.Generation.XML.FHIRProfile2Trifolia.xslt";
        private const string StylesheetUri = "http://www.w3.org/1999/XSL/Transform";

        public ImplementationGuide DefaultImplementationGuide
        {
            get { return this.importer.DefaultImplementationGuide; }
            set { this.importer.DefaultImplementationGuide = value; }
        }

        public User DefaultAuthorUser
        {
            get { return this.importer.DefaultAuthorUser; }
            set { this.importer.DefaultAuthorUser = value; }
        }

        private IObjectRepository tdb;
        private TemplateImporter importer;

        #region Constructors

        public FHIRImporter(IObjectRepository tdb, bool shouldUpdate)
        {
            this.tdb = tdb;

            this.importer = new TemplateImporter(this.tdb, shouldUpdate);
        }

        #endregion

        public void Import(string bundleXml)
        {
            string templatesXml = TransformBundle(bundleXml);
            XmlSerializer serializer = new XmlSerializer(typeof(TemplateExport));

            List<Template> templates = this.importer.Import(templatesXml);

            if (importer.Errors.Count > 0)
            {
                string errors = String.Join(". ", importer.Errors);
                throw new Exception("Importing FHIR Profiles as templates failed: " + errors);
            }
        }

        private string TransformBundle(string bundle)
        {
            string stylesheetContent = string.Empty;
            LantanaXmlResolver resolver = new LantanaXmlResolver();

            using (StreamReader stylesheetReader = new StreamReader(Assembly.GetExecutingAssembly().GetManifestResourceStream(StylesheetResource)))
            {
                stylesheetContent = stylesheetReader.ReadToEnd();
            }

            return TransformFactory.Transform(bundle, stylesheetContent, StylesheetUri, resolver);
        }
    }
}
