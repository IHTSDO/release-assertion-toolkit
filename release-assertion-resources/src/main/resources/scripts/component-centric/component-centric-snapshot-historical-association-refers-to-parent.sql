
/******************************************************************************** 
	component-centric-snapshot-historical-association-refers-to-parent

	Assertion:
	No active Historical Association member associates an inactive concept with its parent.

********************************************************************************/
	
	
	/* Test against Inferred Parents */	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.id, ': Historical association refset member associates inactive concept with a stated parent.') 
	from curr_associationrefset_s a, curr_stated_relationship_s b
	where a.active = '1'
	and b.active = '1'
	and b.typeid = '116680003'
	and a.referencedcomponentid = b.sourceid
	and a.targetcomponentid = b.destinationid;
		
		
		
		
	/* Test against Stated Parents */	
	insert into qa_result (runid, assertionuuid, assertiontext, details)
	select 
		<RUNID>,
		'<ASSERTIONUUID>',
		'<ASSERTIONTEXT>',
		concat('CONCEPT: id=',a.id, ': Historical association refset member associates inactive concept with an inferred parent.') 
	from curr_associationrefset_s a, curr_inferred_relationship_s b
	where a.active = '1'
	and b.active = '1'
	and b.typeid = '116680003'
	and a.referencedcomponentid = b.sourceid
	and a.targetcomponentid = b.destinationid;