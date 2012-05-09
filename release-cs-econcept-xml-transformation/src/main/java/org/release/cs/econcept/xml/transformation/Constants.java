package org.release.cs.econcept.xml.transformation;

import java.util.UUID;

/**
 * Title: Constants : Declared all the constants used by changeset routines 
 * Copyright: Copyright (c) 2010 Company: IHTSDO
 * 
 * @author Varsha Parekh
 * @version 1.0
 */

public class Constants {

	public final static UUID workflowHistoryRefset = UUID.fromString("0b6f0e24-5fe2-3869-9342-c18008f53283");
	public final static UUID commitHistoryRefset = UUID.fromString("ea34d82a-a645-337b-88f4-77740dd683b9");
	public final static UUID conceptInConflictRefset = UUID.fromString("a9ac8e53-e904-3c72-aef0-b546f0977ed8");
	public final static UUID adjudicationRefset = UUID.fromString("dfe2c9dd-2da8-3980-879c-518c1a38907f");
	
	public final static UUID isCaseSensitive = UUID.fromString("0def37bc-7e1b-384b-a6a3-3e3ceee9c52e");
	public final static UUID isNotCaseSensitive = UUID.fromString("17915e0d-ed38-3488-a35c-cda966db306a");
	public final static UUID isFullyDefined = UUID.fromString("6d9cd46e-8a8f-310a-a298-3e55dcf7a986");
	public final static UUID isPrimitive = UUID.fromString("e1a12059-3b01-3296-9532-d10e49d0afc3");

	public final static UUID activeStatusRf2 = UUID.fromString("d12702ee-c37f-385f-a070-61d56d4d0f1f");
	public final static UUID conceptNotCurrentStatusRf2 = UUID.fromString("6cc3df26-661e-33cd-a93d-1c9e797c90e3");
	public final static UUID limitedStatusRf2 = UUID.fromString("0d1278d5-3718-36de-91fd-7c6c8d2d2521");
	public final static UUID activeStatusRf1 = UUID.fromString("32dc7b19-95cc-365e-99c9-5095124ebe72");
	public final static UUID currentStatusRf1 = UUID.fromString("2faa9261-8fb2-11db-b606-0800200c9a66");

	public final static UUID snomedCTRootUUID = UUID.fromString("ee9ac5d2-a07c-3981-a57a-f7f26baf38d8");
	
	public final static String currentEditCycleDate = "23/01/2012";
	
	public final static String relModifierId = "900000000000451002";
	
}
