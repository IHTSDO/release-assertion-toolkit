
/******************************************************************************** 
	component-centric-snapshot-stated-inactive-concepts
	
	Assertion:
	All stated relationships in which any of the three relationship concepts
	are inactive, are inactive relationships.

********************************************************************************/
	
	/* Inactive Source Id */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('RELATIONSHIP: id=',a.id, ': Active stated relationship is associated with an inactive sourceid concept.') 	
	
	from curr_stated_relationship_s a
	inner join curr_concept_s b on a.sourceid = b.id
	where a.active = '1'
	and b.active = '0';


	
	/* Inactive Destination Id */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('RELATIONSHIP: id=',a.id, ': Active stated relationship is associated with an inactive destinationid concept.') 	
	
	from curr_stated_relationship_s a
	inner join curr_concept_s b on a.destinationid = b.id
	where a.active = '1'
	and b.active = '0';
		
	
	
	/* Inactive Destination Id */
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('RELATIONSHIP: id=',a.id, ': Active stated relationship is associated with an inactive typeid concept.') 	
	
	from curr_stated_relationship_s a
	inner join curr_concept_s b on a.typeid = b.id
	where a.active = '1'
	and b.active = '0';

	