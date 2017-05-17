﻿extern alias fhir_stu3;
using fhir_stu3.Hl7.Fhir.Model;
using fhir_stu3.Hl7.Fhir.Serialization;
using fhir_stu3.Hl7.Fhir.Specification.Navigation;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;
using Trifolia.Config;
using Trifolia.DB;
using Trifolia.Generation.IG.ConstraintGeneration;
using Trifolia.Logging;
using Trifolia.Shared;
using Trifolia.Shared.FHIR;
using Trifolia.Shared.FHIR.Profiles.STU3;
using Trifolia.Shared.Plugins;
using ImplementationGuide = Trifolia.DB.ImplementationGuide;
using StructureDefinition = fhir_stu3.Hl7.Fhir.Model.StructureDefinition;

namespace Trifolia.Export.FHIR.STU3
{
    public class StructureDefinitionExporter
    {
        private IObjectRepository tdb;
        private string scheme;
        private string authority;
        private Dictionary<ImplementationGuide, IGSettingsManager> allIgSettings = new Dictionary<ImplementationGuide, IGSettingsManager>();
        private Dictionary<string, StructureDefinition> baseProfiles = new Dictionary<string, StructureDefinition>();
        private ImplementationGuideType implementationGuideType;
        private IIGTypePlugin igTypePlugin;

        public StructureDefinitionExporter(IObjectRepository tdb, string scheme, string authority)
        {
            this.tdb = tdb;
            this.scheme = scheme;
            this.authority = authority;
            this.implementationGuideType = STU3Helper.GetImplementationGuideType(this.tdb, true);
            this.igTypePlugin = this.implementationGuideType.GetPlugin();
        }

        public Extension Convert(TemplateExtension extension)
        {
            var fhirExtension = new Extension()
            {
                Url = extension.Identifier
            };

            switch (extension.Type)
            {
                case "String":
                    fhirExtension.Value = new FhirString(extension.Value);
                    break;
                case "Integer":
                    try
                    {
                        fhirExtension.Value = new Integer(Int32.Parse(extension.Value));
                    }
                    catch { }
                    break;
                case "Boolean":
                    try
                    {
                        fhirExtension.Value = new FhirBoolean(Boolean.Parse(extension.Value));
                    }
                    catch { }
                    break;
                case "Date":
                    fhirExtension.Value = new Date(extension.Value);
                    break;
                case "DateTime":
                    fhirExtension.Value = new FhirDateTime(extension.Value);
                    break;
                case "Code":
                    fhirExtension.Value = new Code(extension.Value);
                    break;
                case "Coding":
                case "CodeableConcept":
                    string[] valueSplit = extension.Value.Split('|');

                    if (valueSplit.Length == 3)
                    {
                        var coding = new Coding();
                        coding.Code = valueSplit[0];
                        coding.Display = valueSplit[1];
                        coding.System = valueSplit[2];

                        if (extension.Type == "Coding")
                        {
                            fhirExtension.Value = coding;
                        }
                        else
                        {
                            var codeableConcept = new CodeableConcept();
                            codeableConcept.Coding.Add(coding);
                            fhirExtension.Value = codeableConcept;
                        }
                    }
                    break;
            }

            if (fhirExtension.Value != null)
                return fhirExtension;

            return null;
        }

        public void CreateElementDefinition(
            StructureDefinition strucDef,
            TemplateConstraint constraint,
            SimpleSchema.SchemaObject schemaObject,
            string sliceName = null)
        {
            if (constraint.IsPrimitive)     // Skip primitives (for now, at least)
                return;

            string newSliceName = null;

            if (constraint.IsBranch)
                newSliceName = string.Format("{0}_slice_pos{1}", constraint.Context, constraint.Order);

            var igSettings = GetIGSettings(constraint);
            var constraintFormatter = FormattedConstraintFactory.NewFormattedConstraint(this.tdb, igSettings, this.igTypePlugin, constraint);
            string definition = constraintFormatter.GetPlainText(false, false, false);

            if (definition == null)
                definition = string.Empty;

            ElementDefinition newElementDef = new ElementDefinition()
            {
                ElementId = constraint.Id.ToString(),
                Short = !string.IsNullOrEmpty(constraint.Label) ? constraint.Label : constraint.Context,
                Label = !string.IsNullOrEmpty(constraint.Label) ? constraint.Label : null,
                Comment = !string.IsNullOrEmpty(constraint.Notes) ? constraint.Notes : null,
                Path = constraint.GetElementPath(strucDef.Type != null ? strucDef.Type.ToString() : null),
                SliceName = constraint.IsBranch ? newSliceName : sliceName,
                Definition = definition
            };

            if (constraint.IsChoice)
            {
                newElementDef.Slicing = new ElementDefinition.SlicingComponent();
                newElementDef.Slicing.Discriminator.Add(new ElementDefinition.DiscriminatorComponent()
                {
                    Type = ElementDefinition.DiscriminatorType.Value,
                    Path = "@type"
                });
                newElementDef.Slicing.Rules = ElementDefinition.SlicingRules.Open;
            }

            // Cardinality
            if (!string.IsNullOrEmpty(constraint.Cardinality))
            {
                newElementDef.Min = constraint.CardinalityType.Left;
                newElementDef.Max = constraint.CardinalityType.Right == Cardinality.MANY ? "*" : constraint.CardinalityType.Right.ToString();
            }

            // Binding
            string valueConformance = string.IsNullOrEmpty(constraint.ValueConformance) ? constraint.Conformance : constraint.ValueConformance;
            bool hasBinding = constraint.ContainedTemplate != null;

            if (constraint.ValueSet != null && valueConformance.IndexOf("NOT") < 0)
            {
                hasBinding = true;
                newElementDef.Binding = new ElementDefinition.ElementDefinitionBindingComponent()
                {
                    ValueSet = new ResourceReference()
                    {
                        Reference = constraint.ValueSet.GetReference(),
                        Display = constraint.ValueSet.Name
                    }
                };

                if (valueConformance == "SHALL")
                    newElementDef.Binding.Strength = BindingStrength.Required;
                else if (valueConformance == "SHOULD")
                    newElementDef.Binding.Strength = BindingStrength.Preferred;
                else if (valueConformance == "MAY")
                    newElementDef.Binding.Strength = BindingStrength.Example;
            }

            // Single-Value Binding
            if (schemaObject != null && (!string.IsNullOrEmpty(constraint.Value) || !string.IsNullOrEmpty(constraint.DisplayName)))
            {
                Element elementBinding = null;

                hasBinding = true;
                switch (schemaObject.DataType)
                {
                    case "CodeableConcept":
                        var codableConceptBinding = new CodeableConcept();
                        var coding = new Coding();
                        codableConceptBinding.Coding.Add(coding);

                        if (!string.IsNullOrEmpty(constraint.Value))
                            coding.Code = constraint.Value;

                        if (constraint.CodeSystem != null)
                            coding.System = constraint.CodeSystem.Oid;

                        if (!string.IsNullOrEmpty(constraint.DisplayName))
                            coding.Display = constraint.DisplayName;

                        elementBinding = codableConceptBinding;
                        break;
                    case "Coding":
                        var codingBinding = new Coding();

                        if (!string.IsNullOrEmpty(constraint.Value))
                            codingBinding.Code = constraint.Value;

                        if (constraint.CodeSystem != null)
                            codingBinding.System = constraint.CodeSystem.Oid;

                        if (!string.IsNullOrEmpty(constraint.DisplayName))
                            codingBinding.Display = constraint.DisplayName;

                        elementBinding = codingBinding;
                        break;
                    case "code":
                        var codeBinding = new Code();
                        codeBinding.Value = !string.IsNullOrEmpty(constraint.Value) ? constraint.Value : constraint.DisplayName;
                        elementBinding = codeBinding;
                        break;
                    default:
                        var stringBinding = new FhirString();
                        stringBinding.Value = !string.IsNullOrEmpty(constraint.Value) ? constraint.Value : constraint.DisplayName;
                        elementBinding = stringBinding;
                        break;
                }

                if (constraint.IsFixed)
                    newElementDef.Fixed = elementBinding;
                else
                    newElementDef.Pattern = elementBinding;
            }

            // Add the type of the element when bound to a value set
            if (hasBinding && schemaObject != null && !string.IsNullOrEmpty(schemaObject.DataType))
            {
                StructureDefinition profile = GetBaseProfile(constraint.Template);
                newElementDef.Type = GetProfileDataTypes(profile, constraint);

                // If there is a contained template/profile, make sure it supports a "Reference" type, and then output the profile identifier in the type
                if (constraint.ContainedTemplate != null && newElementDef.Type.Exists(y => y.Code == "Reference" || y.Code == "Extension"))
                {
                    // FHIR requires that referenced profiles be represented by multiple types on a single element
                    // TODO: There is some potential loss of properties on the second element definition
                    if (!string.IsNullOrEmpty(sliceName))
                    {
                        var foundMatchingElement = strucDef.Differential.Element.SingleOrDefault(y => y.Path == newElementDef.Path && y.SliceName == newElementDef.SliceName);

                        if (foundMatchingElement != null)
                        {
                            foundMatchingElement.Definition += " " + definition;
                            newElementDef = foundMatchingElement;
                        }
                    }

                    bool isExtension = constraint.ContainedTemplate.PrimaryContextType == "Extension" && newElementDef.Type.Exists(y => y.Code == "Extension");

                    var containedTypes = new List<ElementDefinition.TypeRefComponent>();
                    var typeRef = new ElementDefinition.TypeRefComponent()
                    {
                        Code = isExtension ? "Extension" : "Reference",
                        Profile = isExtension ? constraint.ContainedTemplate.Oid : null,
                        TargetProfile = !isExtension ? constraint.ContainedTemplate.Oid : null
                    };

                    containedTypes.Add(typeRef);
                    newElementDef.Type = containedTypes;
                }
            }

            // Add the element to the list if it's new
            if (!strucDef.Differential.Element.Contains(newElementDef))
                strucDef.Differential.Element.Add(newElementDef);

            // Children
            foreach (var childConstraint in constraint.ChildConstraints.OrderBy(y => y.Order))
            {
                var childSchemaObject = schemaObject != null ? schemaObject.Children.SingleOrDefault(y => y.Name == childConstraint.Context) : null;
                CreateElementDefinition(strucDef, childConstraint, childSchemaObject, newSliceName);
            }
        }

        public StructureDefinition Convert(Template template, SimpleSchema schema, SummaryType? summaryType = null)
        {
            var fhirStructureDef = new fhir_stu3.Hl7.Fhir.Model.StructureDefinition()
            {
                Id = template.FhirId(),
                Name = template.Name,
                Description = template.Description != null ? new Markdown(template.Description.RemoveInvalidUtf8Characters()) : null,
                Kind = StructureDefinition.StructureDefinitionKind.Resource,
                Url = template.FhirUrl(),
                Type = template.TemplateType.RootContextType,
                Context = new List<string> { template.PrimaryContextType },
                ContextType = template.PrimaryContextType == "Extension" ? StructureDefinition.ExtensionContext.Extension : StructureDefinition.ExtensionContext.Resource,
                Abstract = false,
                Derivation = StructureDefinition.TypeDerivationRule.Constraint
            };

            // Extensions
            foreach (var extension in template.Extensions)
            {
                var fhirExtension = Convert(extension);

                if (fhirExtension != null)
                    fhirStructureDef.Extension.Add(fhirExtension);
            }

            // Status
            if (template.Status == null || template.Status.IsDraft || template.Status.IsBallot)
                fhirStructureDef.Status = PublicationStatus.Draft;
            else if (template.Status.IsPublished)
                fhirStructureDef.Status = PublicationStatus.Active;
            else if (template.Status.IsDraft)
                fhirStructureDef.Status = PublicationStatus.Retired;

            // Publisher and Contact
            if (template.Author != null)
            {
                if (!string.IsNullOrEmpty(template.Author.ExternalOrganizationName))
                    fhirStructureDef.Publisher = template.Author.ExternalOrganizationName;

                var newContact = new ContactDetail();
                newContact.Name = string.Format("{0} {1}", template.Author.FirstName, template.Author.LastName);
                newContact.Telecom.Add(new ContactPoint()
                {
                    Value = template.Author.Phone,
                    Use = ContactPoint.ContactPointUse.Work,
                    System = ContactPoint.ContactPointSystem.Phone
                });
                newContact.Telecom.Add(new ContactPoint()
                {
                    Value = template.Author.Email,
                    Use = ContactPoint.ContactPointUse.Work,
                    System = ContactPoint.ContactPointSystem.Email
                });

                fhirStructureDef.Contact.Add(newContact);
            }

            // Base profile
            if (template.ImpliedTemplate != null)
                fhirStructureDef.BaseDefinitionElement = new FhirUri(template.ImpliedTemplate.FhirUrl());
            else
                fhirStructureDef.BaseDefinitionElement = new FhirUri(string.Format("http://hl7.org/fhir/StructureDefinition/{0}", template.TemplateType.RootContextType));

            // Constraints
            if (summaryType == null || summaryType == SummaryType.Data)
            {
                var differential = new StructureDefinition.DifferentialComponent();
                fhirStructureDef.Differential = differential;

                // Add base element for resource
                differential.Element.Add(new ElementDefinition()
                {
                    ElementId = string.Format("{0}-00001", template.Id.ToString()),
                    Path = template.PrimaryContextType
                });

                foreach (var constraint in template.ChildConstraints.Where(y => y.ParentConstraint == null).OrderBy(y => y.Order))
                {
                    SimpleSchema.SchemaObject schemaObject = null;

                    if (schema != null)
                        schemaObject = schema.Children.SingleOrDefault(y => y.Name == constraint.Context);

                    CreateElementDefinition(fhirStructureDef, constraint, schemaObject);
                }

                // Slices
                var slices = template.ChildConstraints.Where(y => y.IsBranch);
                var sliceGroups = slices.GroupBy(y => y.GetElementPath(template.TemplateType.RootContextType));
                int currentSliceGroupCount = 2;

                foreach (var sliceGroup in sliceGroups)
                {
                    ElementDefinition newElementDef = new ElementDefinition();
                    newElementDef.ElementId = string.Format("{0}-{1}", template.Id, currentSliceGroupCount.ToString("00"));
                    newElementDef.Path = sliceGroup.Key;

                    foreach (var branchConstraint in sliceGroup)
                    {
                        var igSettings = GetIGSettings(branchConstraint);
                        var constraintFormatter = FormattedConstraintFactory.NewFormattedConstraint(this.tdb, igSettings, this.igTypePlugin, branchConstraint);
                        var branchIdentifiers = branchConstraint.ChildConstraints.Where(y => y.IsBranchIdentifier);

                        newElementDef.Definition = constraintFormatter.GetPlainText(false, false, false);
                        newElementDef.Slicing = new ElementDefinition.SlicingComponent()
                        {
                            Rules = template.IsOpen ? ElementDefinition.SlicingRules.Open : ElementDefinition.SlicingRules.Closed
                        };

                        if (branchIdentifiers.Count() > 0)
                        {
                            newElementDef.Slicing.Discriminator = (from bi in branchIdentifiers
                                                                   select new ElementDefinition.DiscriminatorComponent()
                                                                   {
                                                                       Type = ElementDefinition.DiscriminatorType.Value,
                                                                       Path = bi.GetElementPath(null, branchConstraint)
                                                                   }).ToList();
                        }
                        else if (branchConstraint.Context == "extension")
                        {
                            newElementDef.Slicing.Discriminator.Add(new ElementDefinition.DiscriminatorComponent()
                            {
                                Type = ElementDefinition.DiscriminatorType.Value,
                                Path = "url"
                            });
                        }
                        else        // If no discriminators are specified, assume the child SHALL constraints are discriminators
                        {
                            var discriminatorConstraints = branchConstraint.ChildConstraints.Where(y => y.Conformance == "SHALL");

                            // If the slice referencing a contained template, use the constraints of the contained template instead of the 
                            // direct constraints of the branch
                            if (branchConstraint.ContainedTemplate != null)
                                discriminatorConstraints = branchConstraint.ContainedTemplate.ChildConstraints.Where(y => y.ParentConstraint == null & y.Conformance == "SHALL");

                            var singleValueDiscriminators = discriminatorConstraints.Where(y => !string.IsNullOrEmpty(y.Value));

                            // If there are constraints that have specific single-value bindings, prefer those
                            if (singleValueDiscriminators.Count() > 0 && singleValueDiscriminators.Count() != discriminatorConstraints.Count())
                                discriminatorConstraints = singleValueDiscriminators;

                            newElementDef.Slicing.Discriminator = (from d in discriminatorConstraints
                                                                   select new ElementDefinition.DiscriminatorComponent()
                                                                   {
                                                                       Type = ElementDefinition.DiscriminatorType.Value,
                                                                       Path = d.GetElementPath(template.PrimaryContextType, branchConstraint)
                                                                   }).ToList();
                        }
                    }

                    // Find where to insert the slice in the element list
                    var firstElement = fhirStructureDef.Differential.Element.First(y => y.Path == sliceGroup.Key);
                    var firstElementIndex = fhirStructureDef.Differential.Element.IndexOf(firstElement);
                    differential.Element.Insert(firstElementIndex, newElementDef);
                    currentSliceGroupCount++;
                }
            }

            return fhirStructureDef;
        }

        private IGSettingsManager GetIGSettings(TemplateConstraint constraint)
        {
            if (this.allIgSettings.ContainsKey(constraint.Template.OwningImplementationGuide))
                return this.allIgSettings[constraint.Template.OwningImplementationGuide];

            if (constraint.Template.OwningImplementationGuideId == 0)
                return null;

            IGSettingsManager igSettings = new IGSettingsManager(this.tdb, constraint.Template.OwningImplementationGuide.Id);
            this.allIgSettings.Add(constraint.Template.OwningImplementationGuide, igSettings);

            return igSettings;
        }

        private StructureDefinition GetBaseProfile(Template template)
        {
            var resourceType = template.TemplateType.RootContextType;

            if (this.baseProfiles.ContainsKey(resourceType))
                return this.baseProfiles[resourceType];

            var baseProfile = ProfileHelper.GetProfile(resourceType);
            this.baseProfiles[resourceType] = baseProfile;
            return baseProfile;
        }

        private List<ElementDefinition.TypeRefComponent> GetProfileDataTypes(StructureDefinition structure, TemplateConstraint constraint)
        {
            if (structure == null || structure.Snapshot == null)
                return null;

            string path = constraint.GetFhirPath();
            var element = structure.Snapshot.Element.FirstOrDefault(y => y.Path == path);

            if (element == null)
                return null;

            return element.Type;
        }
    }
}
