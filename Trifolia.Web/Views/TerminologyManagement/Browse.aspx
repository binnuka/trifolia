﻿<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.MVC.Master" Inherits="System.Web.Mvc.ViewPage<dynamic>" %>

<asp:Content ID="Content2" ContentPlaceHolderID="HeadContent" runat="server">
    <script type="text/javascript" src="/Scripts/lib/angular/angular.min.js"></script>
    <script type="text/javascript" src="/Scripts/lib/angular/angular-cookies.js"></script>
    <script type="text/javascript" src="/Scripts/lib/angular/ui-bootstrap-tpls-2.5.0.min.js"></script>
    <script type="text/javascript" src="/Scripts/TerminologyManagement/Browse.js?<%= ViewContext.Controller.GetType().Assembly.GetName().Version %>"></script>

    <style type="text/css">
        .table.identifiers tfoot tr td:not(:first-child) {
            padding-top: 28px;
            padding-bottom: 0px;
        }
    </style>
</asp:Content>

<asp:Content ID="Content1" ContentPlaceHolderID="MainContent" runat="server">
    <div id="BrowseTerminology" class="ng-cloak" ng-app="Trifolia" ng-controller="BrowseTerminologyController">
        <h3>Browse Terminology</h3>

        <uib-tabset active="currentTab">
            <uib-tab heading="Value Sets" ng-if="showValueSets" select="tabChanged(0)">
                <div ng-controller="BrowseValueSetsController" ng-init="contextTabChanged()">
                    <form id="ValueSetSearchForm">
                        <div class="input-group" style="padding-bottom: 10px;">
                            <input type="text" class="form-control" ng-model="criteria.query" />
                            <span class="input-group-btn">
                                <button class="btn btn-default" type="button" ng-click="criteria.query = ''; search();">
                                    <i class="glyphicon glyphicon-remove"></i>
                                </button>
                                <button class="btn btn-default" type="submit" ng-click="search()">Search</button>
                            </span>
                        </div>
                    </form>

                    <div class="row">
                        <div class="col-md-8">
                            <div ng-include="'valueSetPageNavigation'"></div>
                        </div>
                        <div class="col-md-4">
                            <div class="pull-right">
                                <button type="button" class="btn btn-primary" ng-if="canEdit" ng-click="editValueSet()">Add Value Set</button>
                            </div>
                        </div>
                    </div>

                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th ng-click="toggleSort('Name')" style="cursor: pointer;">
                                    Name
                                    <span ng-show="criteria.sort == 'Name'">
                                        <i ng-show="criteria.order == 'desc'" class="glyphicon glyphicon-chevron-down"></i>
                                        <i ng-show="criteria.order == 'asc'" class="glyphicon glyphicon-chevron-up"></i>
                                    </span>
                                </th>
                                <th ng-click="toggleSort('Identifiers')" style="cursor: pointer;">
                                    Identifier(s)
                                    <span ng-show="criteria.sort == 'Identifiers'">
                                        <i ng-show="criteria.order == 'desc'" class="glyphicon glyphicon-chevron-down"></i>
                                        <i ng-show="criteria.order == 'asc'" class="glyphicon glyphicon-chevron-up"></i>
                                    </span>
                                </th>
                                <th ng-click="toggleSort('IsComplete')" style="cursor: pointer;">
                                    Complete?
                                    <span ng-show="criteria.sort == 'IsComplete'">
                                        <i ng-show="criteria.order == 'desc'" class="glyphicon glyphicon-chevron-down"></i>
                                        <i ng-show="criteria.order == 'asc'" class="glyphicon glyphicon-chevron-up"></i>
                                    </span>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="r in searchResults.Items">
                                <td>
                                    <div class="dropdown">
                                        <a href="#" class="dropdown-toggle" role="menu" data-toggle="dropdown"><span>{{r.Name}}</span> <span class="caret"></span></a>
                                        <i ng-show="r.IsPublished" class="glyphicon glyphicon-exclamation-sign" title="This value set is used by a published implementation guide!"></i>
                                        <ul class="dropdown-menu">
                                            <li><a href="#" data-bind="attr: { href: '/TerminologyManagement/ValueSet/View/' + Id() }">View</a></li>
                                            <li ng-disabled="r.disableModify"><a href="#" ng-click="editValueSet(r)">Edit Value Set</a></li>
                                            <li ng-disabled="r.disableModify"><a href="#" ng-click="editConcepts(r)">Edit Concepts</a></li>
                                            <li ng-disabled="r.disableModify"><a href="#" ng-click="removeValueSet(r)">Remove</a></li>
                                        </ul>
                                    </div>
                                </td>
                                <td ng-bind-html="r.IdentifiersDisplay"></td>
                                <td>{{r.IsComplete ? 'Yes' : 'No'}}</td>
                            </tr>
                        </tbody>
                        <tfoot>
                            <tr ng-if="isSearching">
                                <td colspan="3">Searching...</td>
                            </tr>
                            <tr ng-if="!isSearching && (!searchResults.Items || searchResults.Items.length == 0)">
                                <td colspan="3">No value sets found.</td>
                            </tr>
                        </tfoot>
                    </table>

                    <div ng-include="'valueSetPageNavigation'"></div>
                </div>
            </uib-tab>
            <uib-tab heading="Code Systems" ng-if="showCodeSystems" select="tabChanged(1)">
                <div ng-controller="BrowseCodeSystemsController" ng-init="contextTabChanged()">
                    <form id="CodeSystemSearchForm">
                        <div class="input-group" style="padding-bottom: 10px;">
                            <input type="text" class="form-control" ng-model="criteria.query" />
                            <span class="input-group-btn">
                                <button class="btn btn-default" type="button" ng-click="criteria.query = ''; search();">
                                    <i class="glyphicon glyphicon-remove"></i>
                                </button>
                                <button class="btn btn-default" type="submit" ng-click="search()">Search</button>
                            </span>
                        </div>
                    </form>
                
                    <div class="row">
                        <div class="col-md-8">
                            <div ng-include="'codeSystemPageNavigation'"></div>
                        </div>
                        <div class="col-md-4">
                            <div class="pull-right" ng-if="canEdit">
                                <button type="button" class="btn btn-primary" ng-click="editCodeSystem()">Add Code System</button>
                            </div>
                        </div>
                    </div>

                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th ng-click="toggleSort('Name')" style="cursor: pointer;">
                                    Name
                                    <span ng-show="criteria.sort == 'Name'">
                                        <i ng-show="criteria.order == 'desc'" class="glyphicon glyphicon-chevron-down"></i>
                                        <i ng-show="criteria.order == 'asc'" class="glyphicon glyphicon-chevron-up"></i>
                                    </span>
                                </th>
                                <th data-bind="click: function () { $parent.ToggleCodeSystemSort('Oid'); }" style="cursor: pointer;">
                                    Identifier
                                    <span ng-show="criteria.sort == 'Oid'">
                                        <i ng-show="criteria.order == 'desc'" class="glyphicon glyphicon-chevron-down"></i>
                                        <i ng-show="criteria.order == 'asc'" class="glyphicon glyphicon-chevron-up"></i>
                                    </span>
                                </th>
                                <th data-bind="click: function () { $parent.ToggleCodeSystemSort('MemberCount'); }" style="cursor: pointer;">
                                    #/Members
                                    <span ng-show="criteria.sort == 'MemberCount'">
                                        <i ng-show="criteria.order == 'desc'" class="glyphicon glyphicon-chevron-down"></i>
                                        <i ng-show="criteria.order == 'asc'" class="glyphicon glyphicon-chevron-up"></i>
                                    </span>
                                </th>
                                <th data-bind="click: function () { $parent.ToggleCodeSystemSort('ConstraintCount'); }" style="cursor: pointer;">
                                    #/Constraints
                                    <span ng-show="criteria.sort == 'ConstraintCount'">
                                        <i ng-show="criteria.order == 'desc'" class="glyphicon glyphicon-chevron-down"></i>
                                        <i ng-show="criteria.order == 'asc'" class="glyphicon glyphicon-chevron-up"></i>
                                    </span>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="r in searchResults.rows">
                                <td>
                                    <div class="dropdown">
                                        <a href="#" class="dropdown-toggle" role="menu" data-toggle="dropdown">{{r.Name}} <span class="caret"></span></a>
                                        <i ng-show="r.IsPublished" class="glyphicon glyphicon-exclamation-sign" title="This code system is used by a published implementation guide!"></i>
                                        <ul class="dropdown-menu">
                                            <li ng-disabled="r.disableModify"><a href="#" ng-click="editCodeSystem(r)">Edit</a></li>
                                            <li ng-disabled="r.disableModify"><a href="#" ng-click="removeCodeSystem(r)">Remove</a></li>
                                        </ul>
                                    </div>
                                </td>
                                <td>{{r.Oid}}</td>
                                <td>{{r.MemberCount}}</td>
                                <td>{{r.ConstraintCount}}</td>
                            </tr>
                        </tbody>
                        <tfoot>
                            <tr ng-if="isSearching">
                                <td colspan="3">Searching...</td>
                            </tr>
                            <tr ng-if="!isSearching && (!searchResults.rows || searchResults.rows.length == 0)">
                                <td colspan="3">No code systems found.</td>
                            </tr>
                        </tfoot>
                    </table>

                    <div ng-include="'codeSystemPageNavigation'"></div>
                </div>
            </uib-tab>
        </uib-tabset>

        <script type="text/html" id="editValueSetModal.html">
            <form name="EditValueSetForm">
                <div class="modal-header">
                    <h4 class="modal-title">Edit Value Set</h4>
                </div>
                <div class="modal-body" style="max-height: 350px; overflow-y: scroll;" ng-init="init()">
                    <div class="form-group" ng-class="{ 'has-error': EditValueSetForm.name.$invalid }">
                        <label>Name</label>
                        <input type="text" class="form-control" name="name" ng-model="valueSet.Name" required maxlength="255" />
                        <span class="help-block" ng-show="EditValueSetForm.name.$error.required">Name is required</span>
                    </div>

                    <div class="panel panel-default panel-sm">
                        <div class="panel-heading">Identifiers</div>
                        <table class="table identifiers">
                            <thead>
                                <tr>
                                    <td>Type</td>
                                    <td>Identifier</td>
                                    <td>Default?</td>
                                    <td style="width: 50px;">&nbsp;</td>
                                </tr>
                            </thead>
                            <tbody ng-repeat="i in valueSet.Identifiers">
                                <tr ng-style="{ 'text-decoration': i.ShouldRemove ? 'line-through' : 'inherit' }">
                                    <td>{{(identifierOptions | filter: { value: i.Type })[0].display}}</td>
                                    <td>{{i.Identifier}}</td>
                                    <td>
                                        <input type="checkbox" ng-show="!i.IsRemoved" ng-model="i.IsDefault" ng-change="defaultIdentifierChanged(i)" />
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-default btn-sm" ng-show="!i.IsRemoved" ng-click="removeIdentifier(i)">
                                            <i class="glyphicon glyphicon-remove"></i>
                                        </button>
                                    </td>
                                </tr>
                            </tbod>
                            <tfoot>
                                <tr>
                                    <td>
                                        <span><strong>New Identifier</strong></span>
                                        <select class="form-control" ng-options="o.value as o.display for o in identifierOptions" ng-model="newIdentifier.Type" name="type"></select>
                                    </td>
                                    <td>
                                        <input type="text" class="form-control" ng-model="newIdentifier.Identifier" value-set-identifier name="identifier" ng-model-options="{ debounce: 500 }" ng-change="identifierChanged(newIdentifier)" />
                                        <span class="help-block" ng-show="!newIdentifier.Identifier">Identifier is required.</span>
                                        <span class="help-block" ng-show="newIdentifier.Identifier && !isNewIdentifierFormatValid()">{{isNewIdentifierFormatInvalid()}}</span>
                                        <span class="help-block" ng-show="newIdentifier.Identifier && !newIdentifierIsUnique">The identifier is already used.</span>
                                    </td>
                                    <td>
                                        <input type="checkbox" ng-model="newIdentifier.IsDefault" />
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-default btn-sm" ng-click="addIdentifier()" ng-disabled="isNewIdentifierFormatInvalid() || !newIdentifierIsUnique">
                                            <i class="glyphicon glyphicon-plus"></i>
                                        </button>
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                        
                    <div class="form-group">
                        <label>Code</label>
                        <input type="text" class="form-control" ng-model="valueSet.Code" maxlength="255" />
                    </div>
                        
                    <div class="form-group">
                        <label>Description</label>
                        <textarea class="form-control" ng-model="valueSet.Description" style="height: 50px;"></textarea>
                    </div>
                        
                    <div class="form-group">
                        <input type="checkbox" ng-model="valueSet.IsIntentional" />
                        <label>Intentional</label>
                    </div>
                        
                    <div class="form-group" ng-show="valueSet.IsIntentional">
                        <label for="valueSetIntentionalDefinition">Intentional Definition</label>
                        <textarea class="form-control" id="valueSetIntentionalDefinition" ng-model="valueSet.IntentionalDefinition"></textarea>
                    </div>
                        
                    <div class="form-group">
                        <input type="checkbox" ng-model="valueSet.IsComplete" />
                        <label>Complete?</label>
                        <span class="help-block">Indicates that the value set is defined completely in Trifolia, including all concepts.</span>
                    </div>
                        
                    <div class="form-group" ng-class="{ 'has-error': EditValueSetForm.sourceUrl.$invalid }">
                        <label>Source URL</label>
                        <input type="text" class="form-control" name="sourceUrl" ng-model="valueSet.SourceUrl" maxlength="255" ng-required="!valueSet.IsComplete" ng-pattern="urlRegex" />
                        <span class="help-block" ng-show="EditValueSetForm.sourceUrl.$error.required">Source URL is required when the value set is not complete.</span>
                        <span class="help-block" ng-show="EditValueSetForm.sourceUrl.$error.pattern">Source URL must be in the format of a URL (ex: http://www.google.com)</span>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary" ng-click="ok()" ng-disabled="EditValueSetForm.$invalid">OK</button>
                    <button type="button" class="btn btn-default" ng-click="cancel()">Cancel</button>
                </div>
            </form>
        </script>

        <script type="text/html" id="editCodeSystemModal.html">
            <form name="editCodeSystemForm">
                <div class="modal-header">
                    <h4 class="modal-title">Edit Code System</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="codeSystemName">Name</label>
                        <input type="text" class="form-control" id="codeSystemName" ng-model="codeSystem.Name" name="name" required maxlength="255" />
                        <span class="help-block" ng-show="editCodeSystemForm.name.$error.required">Name is required.</span>
                    </div>
                        
                    <div class="form-group">
                        <label for="codeSystemOid">Identifier</label>
                        <input type="text" class="form-control" id="codeSystemOid" ng-model="codeSystem.Oid" name="identifier" ng-pattern="identifierRegex" required maxlength="255" />
                        <span class="help-block" ng-show="editCodeSystemForm.identifier.$error.pattern">The identifier is not in the correct format. Acceptable formats are: http(s)://XXX or urn:oid:XXX or urn:hl7ii:XXX:YYY</span>
                        <span class="help-block" ng-show="editCodeSystemForm.identifier.$error.required">Identifier is required.</span>
                    </div>
                        
                    <div class="form-group">
                        <label>Description</label>
                        <textarea class="form-control" style="height: 100px;" ng-model="codeSystem.Description" name="description"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary" ng-click="ok()" ng-disabled="editCodeSystemForm.$invalid">OK</button>
                    <button type="button" class="btn btn-default" ng-click="cancel()">Cancel</button>
                </div>
            </form>
        </script>
        
        <script type="text/html" id="valueSetPageNavigation">
            Page {{criteria.page}} of {{totalPages}}, {{searchResults.TotalItems}} value sets
            <div class="btn-group btn-group-sm">
                <button type="button" class="btn btn-default btn-sm" ng-click="changePage('first')" ng-disabled="criteria.page <= 1" title="First Page">
                    <i class="glyphicon glyphicon-fast-backward"></i>
                </button>
                <button type="button" class="btn btn-default btn-sm" ng-click="changePage('previous')" ng-disabled="criteria.page <= 1" title="Previous Page">
                    <i class="glyphicon glyphicon-backward"></i>
                </button>
                <button type="button" class="btn btn-default btn-sm" ng-click="changePage('next')" ng-disabled="criteria.page >= totalPages" title="Next Page">
                    <i class="glyphicon glyphicon-forward"></i>
                </button>
                <button type="button" class="btn btn-default btn-sm" ng-click="changePage('last')" ng-disabled="criteria.page >= totalPages" title="Last Page">
                    <i class="glyphicon glyphicon-fast-forward"></i>
                </button>
                <button class="btn btn-default btn-sm dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                    {{criteria.rows}} per page
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li><a href="#" ng-click="criteria.rows = 10; search();">10</a></li>
                    <li><a href="#" ng-click="criteria.rows = 20; search();">20</a></li>
                    <li><a href="#" ng-click="criteria.rows = 50; search();">50</a></li>
                    <li><a href="#" ng-click="criteria.rows = 100; search();">100</a></li>
                </ul>
            </div>
        </script>

        <script type="text/html" id="codeSystemPageNavigation">
            Page {{criteria.page}} of {{totalPages}}, {{searchResults.total}} value sets
            <div class="btn-group btn-group-sm">
                <button type="button" class="btn btn-default btn-sm" ng-click="changePage('first')" ng-disabled="criteria.page <= 1" title="First Page">
                    <i class="glyphicon glyphicon-fast-backward"></i>
                </button>
                <button type="button" class="btn btn-default btn-sm" ng-click="changePage('previous')" ng-disabled="criteria.page <= 1" title="Previous Page">
                    <i class="glyphicon glyphicon-backward"></i>
                </button>
                <button type="button" class="btn btn-default btn-sm" ng-click="changePage('next')" ng-disabled="criteria.page >= totalPages" title="Next Page">
                    <i class="glyphicon glyphicon-forward"></i>
                </button>
                <button type="button" class="btn btn-default btn-sm" ng-click="changePage('last')" ng-disabled="criteria.page >= totalPages" title="Last Page">
                    <i class="glyphicon glyphicon-fast-forward"></i>
                </button>
                <button class="btn btn-default btn-sm dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                    {{criteria.rows}} per page
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li><a href="#" ng-click="criteria.rows = 10; search();">10</a></li>
                    <li><a href="#" ng-click="criteria.rows = 20; search();">20</a></li>
                    <li><a href="#" ng-click="criteria.rows = 50; search();">50</a></li>
                    <li><a href="#" ng-click="criteria.rows = 100; search();">100</a></li>
                </ul>
            </div>
        </script>
    </div>
</asp:Content>
