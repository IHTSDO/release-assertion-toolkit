
/******************************************************************************** 
	component-centric-snapshot-language-lang-specific-words-us-def-proc

	Assertion:	
	Defines procedure that tests that terms that contain EN-US 
	language-specific words are in the same US language refset.

********************************************************************************/

	drop procedure if exists usTerm_procedure;
	CREATE PROCEDURE usTerm_procedure() 
	begin 
		declare no_more_rows boolean default false; 
		declare usTerm VARCHAR(255); 
		declare term_cursor cursor for 
		select term from res_usTerms; 

		declare continue handler for not found set no_more_rows := true; 

		open term_cursor; 

		LOOP1: 
			loop fetch term_cursor into usTerm; 

			if no_more_rows 
				then close term_cursor; 
				leave LOOP1; 
			end if; 

			insert into qa_result (runid, assertionuuid, assertiontext, details)
			select 
				<RUNID>,
				'<ASSERTIONUUID>',
				'<ASSERTIONTEXT>',
				concat('CONCEPT: id=',a.id, ': Description is in US Language refset and refers to a term that is in en-us spelling.') 
			from v_curr_snapshot a 
			where locate(usTerm, a.term) >= 1;		

		end loop LOOP1; 
	end;
