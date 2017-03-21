﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

using Trifolia.DB;
using Trifolia.Config;
using Trifolia.Web.Models.RoleManagement;
using Trifolia.Authorization;
using Trifolia.Web.Models.Admin;

namespace Trifolia.Web.Controllers.API
{
    public class RoleController : ApiController
    {
        private IObjectRepository tdb;

        #region Construct/Dispose

        public RoleController(IObjectRepository tdb)
        {
            this.tdb = tdb;
        }

        public RoleController()
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

        #region Roles

        /// <summary>
        /// Gets all roles in Trifolia
        /// </summary>
        /// <returns>Trifolia.Web.Models.RoleManagement.RolesModel</returns>
        /// <permission cref="Trifolia.Authorization.SecurableNames.ADMIN">Only administrators are permitted</permission>
        [HttpGet, Route("api/Role"), SecurableAction()]
        public RolesModel GetRoles()
        {
            RolesModel model = new RolesModel();

            // Populate the model's list of Roles
            foreach (Role cRole in this.tdb.Roles)
            {
                // Create the model's role item
                RolesModel.RoleItem newRoleItem = new RolesModel.RoleItem()
                {
                    Id = cRole.Id,
                    Name = cRole.Name
                };

                // Add each assigned securable of the role to the model's role item
                foreach (RoleAppSecurable cRoleAppSecurable in cRole.AppSecurables)
                {
                    newRoleItem.AssignedSecurables.Add(
                        new RolesModel.SecurableItem()
                        {
                            Id = cRoleAppSecurable.AppSecurable.Id,
                            Key = cRoleAppSecurable.AppSecurable.Name,
                            Name = cRoleAppSecurable.AppSecurable.DisplayName,
                            Description = cRoleAppSecurable.AppSecurable.Description
                        });
                }

                newRoleItem.Organizations = (from r in this.tdb.Roles
                                       from o in this.tdb.Organizations
                                       where r.Id == cRole.Id
                                       select new RolesModel.Organization
                                       {
                                           Id = o.Id,
                                           Name = o.Name,
                                           Restricted = r.Restrictions.Count(y => y.OrganizationId == o.Id) > 0
                                       })
                                        .Distinct()
                                        .OrderBy(y => y.Name)
                                        .ToList();

                // Add the model's role item to the model
                model.Roles.Add(newRoleItem);
            }

            foreach (AppSecurable cAppSecurable in this.tdb.AppSecurables)
            {
                model.AllSecurables.Add(
                    new RolesModel.SecurableItem()
                    {
                        Id = cAppSecurable.Id,
                        Key = cAppSecurable.Name,
                        Name = cAppSecurable.DisplayName,
                        Description = cAppSecurable.Description
                    });
            }

            Role defaultRole = this.tdb.Roles.FirstOrDefault(y => y.IsDefault);
            model.DefaultRoleId = defaultRole != null ? (int?)defaultRole.Id : null;

            return model;
        }

        /// <summary>
        /// Assgns the specified securable to the role
        /// Both role and securable must exist.
        /// </summary>
        /// <permission cref="Trifolia.Authorization.SecurableNames.ADMIN">Only administrators are permitted</permission>
        [HttpPost, Route("api/Role/{roleId}/Assign/{securableId}"), SecurableAction(SecurableNames.ADMIN)]
        public void AssignRoleSecurableAction(int roleId, int securableId)
        {
            RoleAppSecurable newRoleAppSecurable = new RoleAppSecurable()
            {
                RoleId = roleId,
                AppSecurableId = securableId
            };

            this.tdb.RoleAppSecurables.Add(newRoleAppSecurable);

            this.tdb.SaveChanges();
        }

        /// <summary>
        /// Removes a securable from the specified role.
        /// Both role and securable must exist.
        /// </summary>
        /// <permission cref="Trifolia.Authorization.SecurableNames.ADMIN">Only administrators are permitted</permission>
        [HttpPost, Route("api/Role/{roleId}/Unassign/{securableId}"), SecurableAction(SecurableNames.ADMIN)]
        public void UnassignRoleSecurableAction(int roleId, int securableId)
        {
            RoleAppSecurable foundRoleAppSecurable =
                this.tdb.RoleAppSecurables.Single(y => y.RoleId == roleId && y.AppSecurableId == securableId);

            if (foundRoleAppSecurable == null)
                throw new Exception(
                    string.Format("Could not find securable {0} associated with role {1}", roleId, securableId));

            this.tdb.RoleAppSecurables.Remove(foundRoleAppSecurable);

            this.tdb.SaveChanges();
        }

        /// <summary>
        /// Adds a new role with the specified name.
        /// </summary>
        /// <permission cref="Trifolia.Authorization.SecurableNames.ADMIN">Only administrators are permitted</permission>
        [HttpPost, Route("api/Role"), SecurableAction(SecurableNames.ADMIN)]
        public RolesModel.RoleItem AddRole(string roleName)
        {
            Role newRole = new Role()
            {
                Name = roleName
            };

            this.tdb.Roles.Add(newRole);

            this.tdb.SaveChanges();

            RolesModel.RoleItem model = new RolesModel.RoleItem()
            {
                Id = newRole.Id,
                Name = newRole.Name
            };

            return model;
        }

        /// <summary>
        /// Removes the specified role
        /// Expects that a valid role is specified.
        /// </summary>
        /// <permission cref="Trifolia.Authorization.SecurableNames.ADMIN">Only administrators are permitted</permission>
        [HttpDelete, Route("api/Role/{roleId}"), SecurableAction(SecurableNames.ADMIN)]
        public void RemoveRole(int roleId)
        {
            Role foundRole = this.tdb.Roles.Single(y => y.Id == roleId);

            foundRole.Users.ToList().ForEach(ur => this.tdb.UserRoles.Remove(ur));        // Remove UserRole objects associating users to this role
            foundRole.AppSecurables.ToList().ForEach(ras => this.tdb.RoleAppSecurables.Remove(ras));      // Remove RoleAppSecurable objects associated securables to the role
            foundRole.Restrictions.ToList().ForEach(r => this.tdb.RoleRestrictions.Remove(r));
            this.tdb.Roles.Remove(foundRole);     // Remove the role itself

            this.tdb.SaveChanges();
        }


        /// <summary>
        /// Updates the organization restrictions for a given role.
        /// Role must be specified.
        /// Any organization passed in via the model is checked to determine if it should be added or removed as a restriction.
        /// </summary>
        /// <permission cref="Trifolia.Authorization.SecurableNames.ADMIN">Only administrators are permitted</permission>
        [HttpPost, Route("api/Role/{roleId}/Restrict/{organizationId}"), SecurableAction(SecurableNames.ADMIN)]
        public void RestrictOrganizations(int roleId, int organizationId)
        {
            Role role = this.tdb.Roles.Single(y => y.Id == roleId);
            Organization org = this.tdb.Organizations.Single(y => y.Id == organizationId);

            if (role.Restrictions.Count(y => y.Organization == org) > 0)
                return;

            RoleRestriction newRoleRestriction = new RoleRestriction();
            newRoleRestriction.Role = role;
            newRoleRestriction.Organization = org;
            this.tdb.RoleRestrictions.Add(newRoleRestriction);

            this.tdb.SaveChanges();
        }

        /// <summary>
        /// Removes the restriction on the specified organization from the specified role
        /// </summary>
        /// <param name="roleId">The id of the role to remove the organization restriction from</param>
        /// <param name="organizationId">The id of the organization</param>
        /// <permission cref="Trifolia.Authorization.SecurableNames.ADMIN">Only administrators are permitted</permission>
        [HttpPost, Route("api/Role/{roleId}/Unrestrict/{organizationId}"), SecurableAction(SecurableNames.ADMIN)]
        public void UnrestrictOrganization(int roleId, int organizationId)
        {
            Role role = this.tdb.Roles.Single(y => y.Id == roleId);
            Organization org = this.tdb.Organizations.Single(y => y.Id == organizationId);
            RoleRestriction roleRestriction = role.Restrictions.Single(y => y.Organization == org);

            this.tdb.RoleRestrictions.Remove(roleRestriction);

            this.tdb.SaveChanges();
        }

        /// <summary>
        /// Set the default role to the role specified in the roleId parameter.
        /// First removes the default flag from all roles, then sets the flag for the one specified (or none, if null is provided).
        /// </summary>
        /// <param name="roleId">If specified, the role to set as the new default.</param>
        /// <permission cref="Trifolia.Authorization.SecurableNames.ADMIN">Only administrators are permitted</permission>
        [HttpPost, Route("api/Role/{roleId}/SetDefault"), SecurableAction(SecurableNames.ADMIN)]
        public void UpdateDefaultRole(int? roleId)
        {
            // Find the role that is requested to be the default
            Role foundRole = this.tdb.Roles.SingleOrDefault(y => y.Id == roleId);

            // Make sure there are no other roles marked as default
            foreach (var cDefaultRole in this.tdb.Roles.Where(y => y.IsDefault))
            {
                cDefaultRole.IsDefault = false;
            }

            if (foundRole != null)
                foundRole.IsDefault = true;

            this.tdb.SaveChanges();
        }

        #endregion
    }
}
