﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Trifolia.Authorization;
using Trifolia.DB;
using ImportModel = Trifolia.Shared.ImportExport.Model.Trifolia;
using Trifolia.Shared.ImportExport;
using System.Data.Entity.Core.Objects;
using Trifolia.Import.Models;
using Trifolia.Import.Native;

namespace Trifolia.Web.Controllers.API
{
    public class ImportController : ApiController
    {
        private IObjectRepository tdb;

        #region Construct/Dispose

        public ImportController(IObjectRepository tdb)
        {
            this.tdb = tdb;
        }

        public ImportController()
            : this(DBContext.Create())
        {

        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
                this.tdb.Dispose();

            base.Dispose(disposing);
        }

        #endregion

        /// <summary>
        /// Imports data from the native Trifolia format. See https://github.com/lantanagroup/trifolia/blob/master/Trifolia.Shared/ImportExport/Model/TemplateExport.xsd for the schema that is used by the import.
        /// </summary>
        /// <param name="model">The data to import (including implementation guides and templates)</param>
        /// <returns></returns>
        [HttpPost, Route("api/Import/Trifolia"), SecurableAction(SecurableNames.IMPORT)]
        public ImportStatusModel ImportTrifoliaModel(ImportModel model)
        {
            TrifoliaImporter importer = new TrifoliaImporter(this.tdb);
            return importer.Import(model);
        }
    }
}
