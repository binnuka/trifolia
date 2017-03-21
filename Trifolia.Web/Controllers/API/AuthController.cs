﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

using Trifolia.Authorization;
using Trifolia.Web.Models.Account;
using Trifolia.DB;

namespace Trifolia.Web.Controllers.API
{
    public class AuthController : ApiController
    {
        private IObjectRepository tdb;

        #region Construct/Dispose

        public AuthController(IObjectRepository tdb)
        {
            this.tdb = tdb;
        }

        public AuthController()
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

        [HttpGet, Route("api/Auth/WhoAmI")]
        public WhoAmIModel WhoAmI()
        {
            if (!CheckPoint.Instance.IsAuthenticated)
                return null;

            User user = CheckPoint.Instance.GetUser(this.tdb);

            if (user == null)
                return null;

            WhoAmIModel model = new WhoAmIModel()
            {
                Id = user.Id,
                Name = string.Format("{0} {1}", user.FirstName, user.LastName),
                UserName = user.UserName,
                Email = user.Email
            };

            model.Securables = (from r in user.Roles
                                join ras in this.tdb.RoleAppSecurables on r.RoleId equals ras.RoleId
                                select ras.AppSecurable.Name);

            return model;
        }
    }
}
