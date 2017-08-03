ALTER view [dbo].[v_templateusage] as
select distinct 
  t.id as templateId, 
  t.name + ' (' + t.oid + ')' as templateDisplay,
  ig.id as implementationGuideId,
  ig.name + ' (' + igt.name + ')' as implementationGuideDisplay
from template t
join implementationguide ig on t.owningImplementationGuideId = ig.id
join implementationguidetype igt on igt.id = ig.implementationGuideTypeId

union all

select distinct 
  t.id as templateId, 
  t.name + ' (' + t.oid + ')' as templateDisplay,
  ig.id as implementationGuideId,
  ig.name + ' (' + igt.name + ')' as implementationGuideDisplay
from template t
join template_constraint tc on t.id = tc.containedTemplateId
join template t2 on t2.id = tc.templateId
join implementationguide ig on t2.owningImplementationGuideId = ig.id
join implementationguidetype igt on igt.id = ig.implementationGuideTypeId

union all

select distinct 
  t.id as templateId,
  t.name + ' (' + t.oid + ')' as templateDisplay,
  ig.id as implementationGuideId,
  ig.name + ' (' + igt.name + ')' as implementationGuideDisplay
from template t
join template t2 on t2.impliedTemplateId = t.id
join implementationguide ig on t2.owningImplementationGuideId = ig.id
join implementationguidetype igt on igt.id = ig.implementationGuideTypeId