package org.release.cs.econcept.xml.transformation;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import org.ihtsdo.db.bdb.BdbTermFactory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dwfa.ace.api.DatabaseSetupConfig;
import org.dwfa.ace.api.I_ConfigAceFrame;
import org.dwfa.ace.api.I_DescriptionVersioned;
import org.dwfa.ace.api.I_GetConceptData;
import org.dwfa.ace.api.I_IdVersion;
import org.dwfa.ace.api.I_Identify;
import org.dwfa.ace.api.I_TermFactory;
import org.dwfa.ace.api.Terms;
import org.dwfa.ace.task.cs.transform.ChangeSetTransformer;
import org.dwfa.cement.ArchitectonicAuxiliary;
import org.dwfa.tapi.TerminologyException;

import org.ihtsdo.db.bdb.computer.kindof.LineageHelper;
import org.ihtsdo.etypes.EConcept;
import org.ihtsdo.helper.time.TimeHelper;
import org.ihtsdo.tk.api.PathBI;
import org.ihtsdo.tk.api.Precedence;
import org.ihtsdo.tk.binding.snomed.SnomedMetadataRf2;
import org.ihtsdo.tk.binding.snomed.SnomedMetadataRfx;
import org.ihtsdo.tk.dto.concept.component.TkRevision;
import org.ihtsdo.tk.dto.concept.component.attribute.TkConceptAttributes;
import org.ihtsdo.tk.dto.concept.component.attribute.TkConceptAttributesRevision;
import org.ihtsdo.tk.dto.concept.component.description.TkDescription;
import org.ihtsdo.tk.dto.concept.component.description.TkDescriptionRevision;
import org.ihtsdo.tk.dto.concept.component.refset.TK_REFSET_TYPE;
import org.ihtsdo.tk.dto.concept.component.refset.TkRefsetAbstractMember;
import org.ihtsdo.tk.dto.concept.component.refset.Boolean.TkRefsetBooleanMember;
import org.ihtsdo.tk.dto.concept.component.refset.cid.TkRefsetCidMember;
import org.ihtsdo.tk.dto.concept.component.refset.cidcid.TkRefsetCidCidMember;
import org.ihtsdo.tk.dto.concept.component.refset.cidcidcid.TkRefsetCidCidCidMember;
import org.ihtsdo.tk.dto.concept.component.refset.cidcidstr.TkRefsetCidCidStrMember;
import org.ihtsdo.tk.dto.concept.component.refset.cidflt.TkRefsetCidFloatMember;
import org.ihtsdo.tk.dto.concept.component.refset.cidint.TkRefsetCidIntMember;
import org.ihtsdo.tk.dto.concept.component.refset.cidlong.TkRefsetCidLongMember;
import org.ihtsdo.tk.dto.concept.component.refset.cidstr.TkRefsetCidStrMember;
import org.ihtsdo.tk.dto.concept.component.refset.integer.TkRefsetIntMember;
import org.ihtsdo.tk.dto.concept.component.refset.str.TkRefsetStrMember;
import org.ihtsdo.tk.dto.concept.component.relationship.TkRelationship;
import org.ihtsdo.tk.dto.concept.component.relationship.TkRelationshipRevision;
import com.sleepycat.je.Database;
import org.ihtsdo.tk.Ts;
//import org.dwfa.tapi.spec.ConceptSpec;

import org.ihtsdo.tk.spec.ConceptSpec;
/**
 * @author Varsha Parekh
 * 
 * @goal econcept-xml-builder
 * @requiresDependencyResolution compile
 */

public class EConceptXMLTransformer extends AbstractMojo {
	public static int snomedIdNid;
	public BufferedWriter writer = null;
	protected long myTime = 1263758387001L;
	public Long nextCommit = null;
	public File changeset;
	public boolean initialized = false;
	public DataInputStream dataStream;
	public final String wfPropertySuffix = "-transform";
	public LineageHelper helper = null;

	public final UUID workflowHistoryRefset = UUID.fromString("0b6f0e24-5fe2-3869-9342-c18008f53283");
	public final UUID commitHistoryRefset = UUID.fromString("ea34d82a-a645-337b-88f4-77740dd683b9");

	public final UUID isCaseSensitive = UUID.fromString("0def37bc-7e1b-384b-a6a3-3e3ceee9c52e");
	public final UUID isNotCaseSensitive = UUID.fromString("17915e0d-ed38-3488-a35c-cda966db306a");
	public final UUID isFullyDefined = UUID.fromString("6d9cd46e-8a8f-310a-a298-3e55dcf7a986");
	public final UUID isPrimitive = UUID.fromString("e1a12059-3b01-3296-9532-d10e49d0afc3");

	public final UUID activeStatus = UUID.fromString("d12702ee-c37f-385f-a070-61d56d4d0f1f");
	public final UUID inactiveStatus = UUID.fromString("a5daba09-7feb-37f0-8d6d-c3cadfc7f724");

	public final UUID snomedCTRootUUID = UUID.fromString("ee9ac5d2-a07c-3981-a57a-f7f26baf38d8");
	public I_GetConceptData snomedCTRoot = null;

	public HashMap<UUID, String> uuidToSctIdMap = new HashMap<UUID, String>();
	public HashMap<UUID, String> pathUuidMap = new HashMap<UUID, String>();
	public Date currentEditCycleTime = null;
	public final String currentEditCycleDate = "31/01/2012";
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	public final String relModifierId = "900000000000451002";

	public static Logger logger = Logger.getLogger(EConceptXMLTransformer.class);
	/** The read only. */
	public static boolean readOnly = false;
	/** The cache size. */
	public static Long cacheSize = Long.getLong("600000000");

	// store the handle for the log files
	private static I_ConfigAceFrame aceConfig;

	
	public static void createTermFactory(String db) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		File vodbDirectory = new File(db);
		DatabaseSetupConfig dbSetupConfig = new DatabaseSetupConfig();
		Terms.createFactory(vodbDirectory, readOnly, cacheSize, dbSetupConfig);
	}

	public static I_TermFactory getTermFactory() {
		// since we are using mojo this handles the return of the opened database
		I_TermFactory termFactory = Terms.get();
		return termFactory;
	}
	
	public static int getNid(String struuid) throws TerminologyException, IOException {
		int nid = 0;
		ArrayList<UUID> uuidList = new ArrayList<UUID>();
		UUID uuid = UUID.fromString(struuid); // SNOMED Core Inferred 5e51196f-903e-5dd4-8b3e-658f7e0a4fe6
		uuidList.add(uuid);
		I_GetConceptData findPathCon = getTermFactory().getConcept(uuidList);
		nid = findPathCon.getConceptNid();
		return nid;
	}
	
	public static I_ConfigAceFrame getAceConfig() {
		return aceConfig;
	}
	
	public static void createAceConfig() {
		try {

			aceConfig = getTermFactory().newAceFrameConfig();

			DateFormat df = new SimpleDateFormat("yyyy.mm.dd hh:mm:ss zzz");
			// config.addViewPosition(termFactory.newPosition(termFactory.getPath(new UUID[] { UUID.fromString(test_path_uuid) }),
			// termFactory.convertToThinVersion(df.parse(test_time).getTime())));

			// Added inferred promotion template to catch the context relationships
			aceConfig.addViewPosition(getTermFactory().newPosition(getTermFactory().getPath(new UUID[] { UUID.fromString("6fd2530f-0f93-5023-b5ef-d2dcecc6f0ae") }), Integer.MAX_VALUE)); //6fd2530f-0f93-5023-b5ef-d2dcecc6f0ae
			aceConfig.addEditingPath(getTermFactory().getPath(new UUID[] { UUID.fromString("6fd2530f-0f93-5023-b5ef-d2dcecc6f0ae") })); //SCT ID PROD 2012 All development path
			
			//aceConfig.addViewPosition(getTermFactory().newPosition(getTermFactory().getPath(new UUID[] { UUID.fromString("b4f0899d-39db-5c3d-ae03-2bac05433162") }), Integer.MAX_VALUE)); //b4f0899d-39db-5c3d-ae03-2bac05433162
			//aceConfig.addEditingPath(getTermFactory().getPath(new UUID[] { UUID.fromString("b4f0899d-39db-5c3d-ae03-2bac05433162") })); //b4f0899d-39db-5c3d-ae03-2bac05433162
			
			aceConfig.getDescTypes().add(ArchitectonicAuxiliary.Concept.FULLY_SPECIFIED_DESCRIPTION_TYPE.localize().getNid());
			aceConfig.getDescTypes().add(SnomedMetadataRfx.getDES_FULL_SPECIFIED_NAME_NID());//Fully specified name	
	
			aceConfig.getDescTypes().add(ArchitectonicAuxiliary.Concept.PREFERRED_DESCRIPTION_TYPE.localize().getNid());
			aceConfig.getDescTypes().add(ArchitectonicAuxiliary.Concept.SYNONYM_DESCRIPTION_TYPE.localize().getNid());
			aceConfig.getDescTypes().add(SnomedMetadataRfx.getDES_SYNONYM_NID());
		    
			aceConfig.getDescTypes().add(ArchitectonicAuxiliary.Concept.TEXT_DEFINITION_TYPE.localize().getNid());
			
			ConceptSpec definition = new ConceptSpec("Definition (core metadata concept)", UUID.fromString("700546a3-09c7-3fc2-9eb9-53d318659a09"));
			aceConfig.getDescTypes().add(getNid(definition.getLenient().getUUIDs().get(0).toString()));
			
			aceConfig.getDestRelTypes().add(ArchitectonicAuxiliary.Concept.IS_A_REL.localize().getNid());
			aceConfig.getDestRelTypes().add(ArchitectonicAuxiliary.Concept.IS_A_DUP_REL.localize().getNid());
			
			ConceptSpec isa = new ConceptSpec("Is a (attribute)", UUID.fromString("c93a30b9-ba77-3adb-a9b8-4589c9f8fb25"));
			aceConfig.getDescTypes().add(getNid(isa.getLenient().getUUIDs().get(0).toString()));
			aceConfig.setDefaultStatus(getTermFactory().getConcept(SnomedMetadataRfx.getSTATUS_CURRENT_NID())); // Current
		
			aceConfig.getAllowedStatus().add(SnomedMetadataRfx.getSTATUS_CURRENT_NID()); // Current
			
			aceConfig.getAllowedStatus().add(getNid("a5daba09-7feb-37f0-8d6d-c3cadfc7f724")); //Retired
			aceConfig.getAllowedStatus().add(getNid("6cc3df26-661e-33cd-a93d-1c9e797c90e3")); //Concept non-current (foundation metadata concept)
			aceConfig.getAllowedStatus().add(getNid("9906317a-f50f-30f6-8b59-a751ae1cdeb9")); //Pending
			aceConfig.getAllowedStatus().add(getNid("95028943-b11c-3509-b1c0-c4ae16aaad5c")); //Component Moved elsewhere	900000000000487009
			
			aceConfig.getAllowedStatus().add(SnomedMetadataRfx.getSTATUS_INAPPROPRIATE_NID()); 					//In-appropriate	900000000000494007
			aceConfig.getAllowedStatus().add(SnomedMetadataRfx.getSTATUS_LIMITED_NID()); 						//Limited	900000000000486000
			aceConfig.getAllowedStatus().add(SnomedMetadataRfx.getSTATUS_OUTDATED().getLenient().getNid()); 	//Outdated	900000000000483008
			aceConfig.getAllowedStatus().add(SnomedMetadataRfx.getSTATUS_INAPPROPRIATE_NID()); 					//In-appropriate	900000000000494007
			aceConfig.getAllowedStatus().add(SnomedMetadataRfx.getSTATUS_ERRONEOUS().getLenient().getNid()); 	//Erroneous component (foundation metadata concept)	900000000000485001
			aceConfig.getAllowedStatus().add(SnomedMetadataRfx.getSTATUS_AMBIGUOUS().getLenient().getNid()); 	//Ambiguous component (foundation metadata concept)	900000000000484002
			aceConfig.getAllowedStatus().add(SnomedMetadataRfx.getSTATUS_DUPLICATE().getLenient().getNid()); 	//Duplicates	900000000000482003
			
			aceConfig.getAllowedStatus().add(ArchitectonicAuxiliary.Concept.ACTIVE.localize().getNid());
			aceConfig.getAllowedStatus().add(ArchitectonicAuxiliary.Concept.CURRENT.localize().getNid());
			aceConfig.getAllowedStatus().add(ArchitectonicAuxiliary.Concept.RETIRED.localize().getNid());
			
			aceConfig.setPrecedence(Precedence.TIME);
		} catch (TerminologyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void transform(File searchPath , File outputFileName) throws IOException,
			FileNotFoundException, ClassNotFoundException {
		try {
			// Get time from string date
			currentEditCycleTime = df.parse(currentEditCycleDate);
			logger.info("currentEditCycleTime..." + currentEditCycleTime);
			// helper fields
			
			logger.info("getAceConfig()..." + getAceConfig().toString());
			//helper = new LineageHelper(getTermFactory().getActiveAceFrameConfig());
			helper = new LineageHelper(getAceConfig());
			
			snomedCTRoot = getTermFactory().getConcept(snomedCTRootUUID);			
			logger.info("snomedCTRoot..." + snomedCTRoot);
			
			snomedIdNid = ArchitectonicAuxiliary.Concept.SNOMED_INT_ID.localize().getNid();
			logger.info("snomedIdNid..." + snomedIdNid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Set<File> children = recursiveCSIdentifier(searchPath);
		logger.info("children..." + children.size());
		
		
		//writer = new BufferedWriter(new FileWriter(new File("TestFile.xml")));
		writer = new BufferedWriter(new FileWriter(outputFileName));
		logger.info("outputFileName..." + outputFileName);
		
		EConcept eConcept = null;

		writer.append("<changesets>");
		writer.newLine();

		for (File cs : children) {
			changeset = cs;
			long commitTime = 0;
			long prevCommitTime = 0;
			boolean eofReached = false;
			boolean transformedInChangeset = false;
			boolean transformedInCommit = false;

			while (!eofReached && (commitTime = nextCommitTime()) != Long.MAX_VALUE) {
				try {
					// Must read from stream regardless if time is after release time
					// in order to update commitTime for next read
					eConcept = new EConcept(dataStream);

					if (commitTime > currentEditCycleTime.getTime()) {
						if (transformedInCommit) {
							if (commitTime != prevCommitTime) {
								if (prevCommitTime > 0 && transformedInCommit) {
									writer.append("\t\t</commit>");
									writer.newLine();
									transformedInCommit = false;
								}
							}
						}

						prevCommitTime = commitTime;
						I_GetConceptData conceptToTransform = getTermFactory().getConcept(eConcept.getPrimordialUuid());

						if (helper.hasAncestor(conceptToTransform, snomedCTRoot)) {
							String conceptStartStr = prepareTransformation(eConcept);
							if (transformEConcept(eConcept, conceptStartStr, commitTime, 
												  transformedInCommit, transformedInChangeset)) {
								endConcept();
								transformedInCommit = true;
								transformedInChangeset = true;
							}
						}

						writer.flush();
					}
					nextCommit = dataStream.readLong();
				} catch (EOFException ex) {
					if (transformedInCommit) {
						writer.append("\t\t</commit>");
						writer.newLine();
					}

					if (transformedInChangeset) {
						writer.append("\t</changeset>");
						writer.newLine();
					}
					writer.flush();

					dataStream.close();
					dataStream = null;
					nextCommit = null;
					initialized = false;
					eofReached = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		writer.append("</changesets>");
		writer.newLine();
		writer.flush();
		writer.close();
		logger.info("outputFileName closed..." + outputFileName);
	}

	public Set<File> recursiveCSIdentifier(File searchPath) {
		return recursiveCSIdentifier(searchPath, new HashSet<File>());
	}

	public Set<File> recursiveCSIdentifier(File potentialCSFile,
			Set<File> csFiles) {
		if (potentialCSFile.isDirectory()) {
			
			//System.out.println("cs file name" + potentialCSFile.getName());
			if (!potentialCSFile.isHidden() && !potentialCSFile.getName().startsWith(".")) {
				File[] children = potentialCSFile.listFiles();
			
				 for (int i = 0; i < children.length; i++) {
						recursiveCSIdentifier(children[i], csFiles);
				}
			}

			return csFiles;
		} else {
			if (!potentialCSFile.isHidden() && 
				!potentialCSFile.getName().startsWith(".") && 
				potentialCSFile.getName().endsWith(".eccs")) {
				csFiles.add(potentialCSFile);
			}
			
			return csFiles;
		}
	}

	public boolean transformEConcept(EConcept eConcept, String conceptStartStr, long commitTime,
									  boolean transformedInCommit, boolean transformedInChangeset)
		throws IOException 
	{
		Map<UUID, ArrayList<TkRefsetAbstractMember<?>>> refsetMembers = new HashMap<UUID, ArrayList<TkRefsetAbstractMember<?>>>();
		Map<UUID, String> refCompIdTypeMap = new HashMap<UUID, String>();
		boolean conceptStartStringWritten = false;
		TkConceptAttributes eAttr = eConcept.getConceptAttributes();
		long latestTime = 0;
		TkRevision latestRev = null;

		if (eAttr != null) {
			if (eAttr.getRevisionList() != null) {
				for (TkConceptAttributesRevision rev : eAttr.getRevisionList()) {
					if (rev.getTime() > latestTime) {
						latestTime = rev.getTime();
						latestRev = rev;
					}
				}
	
				if (componentChangedDuringRelease(latestTime, commitTime, conceptStartStr, 
												  conceptStartStringWritten, transformedInCommit, 
												  transformedInChangeset)) 
				{
					System.out.println("componentChangedDuringRelease if");
					conceptStartStringWritten = true;

					transformAttributes((TkConceptAttributesRevision) latestRev,
										eAttr.getPrimordialComponentUuid(), 
										commitTime);
				}
			} else {
				if (componentChangedDuringRelease(eAttr.getTime(), commitTime, conceptStartStr, 
												  conceptStartStringWritten, transformedInCommit, 
												  transformedInChangeset)) 
				{
					System.out.println("componentChangedDuringRelease else");
					conceptStartStringWritten = true;
					transformNewConceptAttributes(eAttr, commitTime);
				}
			}

			if (eAttr.getAnnotations() != null) {
				ArrayList<TkRefsetAbstractMember<?>> attrMembers = new ArrayList<TkRefsetAbstractMember<?>>();

				if (eAttr.getAnnotations() != null) {
					attrMembers.addAll(eAttr.getAnnotations());
				}

				refsetMembers.put(eConcept.getPrimordialUuid(), attrMembers);

				refCompIdTypeMap.put(eConcept.getPrimordialUuid(), "concept");
			}
		}

		if ((eConcept.getDescriptions() != null)) {
			for (TkDescription ed : eConcept.getDescriptions()) {
				latestTime = 0;
				if (ed.getRevisionList() != null) {
					for (TkDescriptionRevision rev : ed.getRevisionList()) {
						if (rev.getTime() > latestTime) {
							latestTime = rev.getTime();
							latestRev = rev;
						}
					}

					if (componentChangedDuringRelease(latestTime, commitTime, conceptStartStr, 
													  conceptStartStringWritten, transformedInCommit, 
													  transformedInChangeset)) 
					{
						conceptStartStringWritten = true;
						transformDescriptionRevision((TkDescriptionRevision) latestRev,
													 ed.getPrimordialComponentUuid(),
													 eConcept.getPrimordialUuid(), commitTime);
					}
				} else {
					if (componentChangedDuringRelease(ed.getTime(), commitTime, conceptStartStr, 
													  conceptStartStringWritten, transformedInCommit, 
													  transformedInChangeset)) 
					{
						conceptStartStringWritten = true;
						transformNewDescription(ed, commitTime);
					}
				}

				List<TkRefsetAbstractMember<?>> descriptionRefsetMembers = ed.getAnnotations();
				if (descriptionRefsetMembers != null) {
					ArrayList<TkRefsetAbstractMember<?>> descMembers = new ArrayList<TkRefsetAbstractMember<?>>();
					descMembers.addAll(ed.getAnnotations());
					
					refsetMembers.put(ed.getPrimordialComponentUuid(), descMembers);
					refCompIdTypeMap.put(ed.getPrimordialComponentUuid(), "description");
				}
			}
		}

		// Relationships currently don't have revisions
		if ((eConcept.getRelationships() != null)) {
			for (TkRelationship er : eConcept.getRelationships()) {
				if (er.getRevisionList() != null) {
					// Currently null for inactivations etc, but leaving in for
					// future use
					latestTime = 0;
					for (TkRelationshipRevision rev : er.getRevisionList()) {
						if (rev.getTime() > latestTime) {
							latestTime = rev.getTime();
							latestRev = rev;
						}
					}

					if (componentChangedDuringRelease(latestTime, commitTime, conceptStartStr, 
													  conceptStartStringWritten, transformedInCommit, 
													  transformedInChangeset)) 
					{
						conceptStartStringWritten = true;
						transformRelationshipRevision((TkRelationshipRevision) latestRev,
													  er.getPrimordialComponentUuid(),
													  er.getC1Uuid(), er.getC2Uuid(), commitTime);
					}
				} else {
					if (componentChangedDuringRelease(er.getTime(), commitTime, conceptStartStr, 
													  conceptStartStringWritten, transformedInCommit, 
													  transformedInChangeset)) 
					{
						conceptStartStringWritten = true;
						transformRelationship(er, commitTime);
					}
				}

				List<TkRefsetAbstractMember<?>> relationshipRefsetMembers = er.getAnnotations();
				if (relationshipRefsetMembers != null) {
					ArrayList<TkRefsetAbstractMember<?>> relMembers = new ArrayList<TkRefsetAbstractMember<?>>();
					relMembers.addAll(er.getAnnotations());

					refsetMembers.put(er.getPrimordialComponentUuid(), relMembers);
					refCompIdTypeMap.put(er.getPrimordialComponentUuid(), "relationship");
				}
			}
		}

		if (eConcept.getRefsetMembers() != null) {
			ArrayList<TkRefsetAbstractMember<?>> attrMembers = new ArrayList<TkRefsetAbstractMember<?>>();

			attrMembers.addAll(eConcept.getRefsetMembers());
			refsetMembers.put(eConcept.getPrimordialUuid(), attrMembers);

			refCompIdTypeMap.put(eConcept.getPrimordialUuid(), "concept");
		}

		// Refsets currently don't have revisions
		for (UUID refCompId : refsetMembers.keySet()) {
			ArrayList<TkRefsetAbstractMember<?>> members = refsetMembers.get(refCompId);

			for (TkRefsetAbstractMember<?> member : members) {
				UUID refsetId = member.getRefsetUuid();

				if (!refsetId.equals(workflowHistoryRefset) && !refsetId.equals(commitHistoryRefset)) {
					if (member.getRevisionList() != null) {
						latestTime = 0;
						for (TkRevision rev : member.getRevisionList()) {
							if (rev.getTime() > latestTime) {
								latestTime = rev.getTime();
								latestRev = rev;
							}
						}

						if (componentChangedDuringRelease(latestTime, commitTime, conceptStartStr,
														  conceptStartStringWritten, transformedInCommit,
														  transformedInChangeset)) 
						{
							conceptStartStringWritten = true;
							transformRefsetMembers(member, refsetId, refCompId, eConcept.getPrimordialUuid(),
												   refCompIdTypeMap.get(refCompId), commitTime, latestRev);
						}
					} else {
						if (componentChangedDuringRelease(member.getTime(), commitTime, conceptStartStr,
														  conceptStartStringWritten, transformedInCommit,
														  transformedInChangeset)) 
						{
							conceptStartStringWritten = true;
							transformRefsetMembers(member, refsetId, refCompId, eConcept.getPrimordialUuid(),
												   refCompIdTypeMap.get(refCompId), commitTime, null);
						}
					}
				}
			}
		}

		return conceptStartStringWritten;
	}

	public void transformRefsetMembers(TkRefsetAbstractMember<?> member, UUID refsetUid, UUID refCompUid,  
										UUID conceptUid, String refCompType, long commitTime, TkRevision latestRev)
		throws IOException 
	{
		UUID path = null;
		UUID status = null;
		UUID author = null;
		long effectiveTime = 0;
		TK_REFSET_TYPE type = member.getType();
		UUID memberId = member.getPrimordialComponentUuid();
		
		if (latestRev != null) {
			path = latestRev.getPathUuid();
			status = latestRev.getStatusUuid();
			author = latestRev.getAuthorUuid();
			effectiveTime = latestRev.getTime();
		} else {
			path = member.getPathUuid();
			status = member.getStatusUuid();
			author = member.getAuthorUuid();
			effectiveTime = member.getTime();
		}
		
		addPreceedingValues("refsetMember", memberId, effectiveTime, status);

		addSctId("refsetId", refsetUid);
		addString("refsetName", getPrefTerm(refsetUid));
		addSctId("concept", conceptUid);
		addSctId("refCompId", refCompUid);
		addString("refCompType", refCompType);
		addUUID("refset-uuid", refsetUid);
		addUUID("concept-uuid", conceptUid);
		addUUID("refCompId-uuid", refCompUid);

		addSapAndClose(path, author, commitTime, null);
		processRefsetComponents(type, member);

		writer.append("\t\t\t\t</" + "refsetMember" + ">");
		writer.newLine();
	}

	public void transformRelationshipRevision(TkRelationshipRevision latestRev, UUID relId, UUID source,
											   UUID target, long commitTime) throws IOException 
	{
		UUID type = latestRev.getTypeUuid();
		UUID characteristic = latestRev.getCharacteristicUuid();
		UUID refinability = latestRev.getRefinabilityUuid();
		int roleGroup = latestRev.getRelGroup();

		UUID path = latestRev.getPathUuid();
		UUID status = latestRev.getStatusUuid();
		UUID author = latestRev.getAuthorUuid();
		long effectiveTime = latestRev.getTime();

		addPreceedingValues("relationship", relId, effectiveTime, status);
		addRelationship(source, target, type, characteristic, refinability, roleGroup, relId);
		addSapAndClose(path, author, commitTime, "relationship");
	}

	public void transformNewConceptAttributes(TkConceptAttributes eAttr, long commitTime) throws IOException {
		boolean isDefined = eAttr.isDefined();

		UUID path = eAttr.getPathUuid();
		UUID status = eAttr.getStatusUuid();
		UUID author = eAttr.getAuthorUuid();
		long effectiveTime = eAttr.getTime();

		addPreceedingValues("attribute", eAttr.getPrimordialComponentUuid(), effectiveTime, status);
		addRf2FileAttributes(isDefined, eAttr.getPrimordialComponentUuid());
		addSapAndClose(path, author, commitTime, "attribute");
	}

	public void transformNewDescription(TkDescription ed, long commitTime) throws IOException {
		UUID conId = ed.getConceptUuid();
		String text = ed.getText();
		text = clearBrackets(text);
		UUID type = ed.getTypeUuid();
		boolean isCaseSig = ed.isInitialCaseSignificant();
		String lang = ed.getLang();

		UUID path = ed.getPathUuid();
		UUID status = ed.getStatusUuid();
		UUID author = ed.getAuthorUuid();
		long effectiveTime = ed.getTime();

		addPreceedingValues("description", ed.getPrimordialComponentUuid(), effectiveTime, status);
		addDescription(conId, text, type, isCaseSig, lang, ed.getPrimordialComponentUuid());
		addSapAndClose(path, author, commitTime, "description");
	}

	public boolean componentChangedDuringRelease(long effectiveTime, long commitTime, String conceptStartStr,
												  boolean conceptStartStringWritten, boolean transformedInCommit,
												  boolean transformedInChangeset) 
		throws IOException 
	{
		if (effectiveTime > currentEditCycleTime.getTime()) {
			if (!transformedInChangeset && !conceptStartStringWritten) {
				writer.append("\t<changeset name='" + changeset.getName() + "'>");
				writer.newLine();

				transformedInChangeset = true;
			}

			if (!transformedInCommit && !conceptStartStringWritten) {
				String cTime = TimeHelper.getFileDateFormat().format(commitTime);

				writer.append("\t\t<commit time='" + cTime + "'>");
				writer.newLine();

				transformedInCommit = true;
			}

			if (!conceptStartStringWritten) {
				writer.append(conceptStartStr);
				writer.newLine();
			}

			return true;
		}

		return false;
	}

	public void processRefsetComponents(TK_REFSET_TYPE type, TkRefsetAbstractMember<?> member) throws IOException {
		switch (type) {
		case MEMBER:
			addString("type", "Member");
			break;
			
		case CID:
			addString("type", "Cid");
			addSctId("CID1", ((TkRefsetCidMember) member).getC1Uuid());
			addUUID("CID1-uuid", ((TkRefsetCidMember) member).getC1Uuid());
			break;
			
		case STR:
			addString("type", "Str");
			addString("STR1", ((TkRefsetStrMember) member).getStrValue());
			break;
			
		case CID_CID:
			addString("type", "Cid_Cid");
			addSctId("CID1", ((TkRefsetCidCidMember) member).getC1Uuid());
			addUUID("CID1-uuid", ((TkRefsetCidCidMember) member).getC1Uuid());
			addSctId("CID2", ((TkRefsetCidCidMember) member).getC2Uuid());
			addUUID("CID2-uuid", ((TkRefsetCidCidMember) member).getC2Uuid());
			break;
			
		case CID_CID_CID:
			addString("type", "Cid_Cid_Cid");
			addSctId("CID1", ((TkRefsetCidCidCidMember) member).getC1Uuid());
			addUUID("CID1-uuid", ((TkRefsetCidCidCidMember) member).getC1Uuid());
			addSctId("CID2", ((TkRefsetCidCidCidMember) member).getC2Uuid());
			addUUID("CID2-uuid", ((TkRefsetCidCidCidMember) member).getC2Uuid());
			addSctId("CID3", ((TkRefsetCidCidCidMember) member).getC3Uuid());
			addUUID("CID3-uuid", ((TkRefsetCidCidCidMember) member).getC3Uuid());
			break;
			
		case CID_CID_STR:
			addString("type", "Cid_Cid_Str");
			addSctId("CID1", ((TkRefsetCidCidStrMember) member).getC1Uuid());
			addUUID("CID1-uuid", ((TkRefsetCidCidStrMember) member).getC1Uuid());
			addSctId("CID2", ((TkRefsetCidCidStrMember) member).getC2Uuid());
			addUUID("CID2-uuid", ((TkRefsetCidCidStrMember) member).getC2Uuid());
			addString("STR3", ((TkRefsetStrMember) member).getStrValue());
			break;
			
		case INT:
			addString("type", "Int");
			addString("Int1", Integer.toString(((TkRefsetIntMember) member)
					.getIntValue()));
			break;
			
		case CID_INT:
			addString("type", "Cid_Int");
			addSctId("CID1", ((TkRefsetCidIntMember) member).getC1Uuid());
			addUUID("CID1-uuid", ((TkRefsetCidIntMember) member).getC1Uuid());
			addString("Int2", Integer.toString(((TkRefsetCidIntMember) member)
					.getIntValue()));
			break;
			
		case BOOLEAN:
			addString("type", "Boolean");
			addBoolean("BOOL1",
					((TkRefsetBooleanMember) member).getBooleanValue());
			break;
			
		case CID_STR:
			addString("type", "Cid_Str");
			addSctId("CID1", ((TkRefsetCidStrMember) member).getC1Uuid());
			addUUID("CID1-uuid", ((TkRefsetCidStrMember) member).getC1Uuid());
			addString("STR2", ((TkRefsetCidStrMember) member).getStrValue());
			break;
			
		case CID_FLOAT:
			addString("type", "Cid_Float");
			addSctId("CID1", ((TkRefsetCidFloatMember) member).getC1Uuid());
			addUUID("CID1-uuid", ((TkRefsetCidFloatMember) member).getC1Uuid());
			addString("FLOAT2",
					Float.toString(((TkRefsetCidFloatMember) member)
							.getFloatValue()));
			break;
			
		case CID_LONG:
			addString("type", "Cid_Long");
			addSctId("CID1", ((TkRefsetCidLongMember) member).getC1Uuid());
			addUUID("CID1-uuid", ((TkRefsetCidLongMember) member).getC1Uuid());
			addString("LONG2", Long.toString(((TkRefsetCidLongMember) member)
					.getLongValue()));
			break;

		case LONG:
			addString("type", "Long");
			addString("LONG1", Long.toString(((TkRefsetCidLongMember) member)
					.getLongValue()));
			break;

		case ARRAY_BYTEARRAY:
		case UNKNOWN:
		default:
			break;
		}
	}

	public void transformRelationship(TkRelationship rel, long commitTime) throws IOException {
		UUID source = rel.getC1Uuid();
		UUID target = rel.getC2Uuid();
		UUID type = rel.getTypeUuid();
		UUID characteristic = rel.getCharacteristicUuid();
		UUID refinability = rel.getRefinabilityUuid();
		int roleGroup = rel.getRelGroup();

		UUID path = rel.getPathUuid();
		UUID status = rel.getStatusUuid();
		UUID author = rel.getAuthorUuid();
		long effectiveTime = rel.getTime();

		addPreceedingValues("relationship", rel.getPrimordialComponentUuid(), effectiveTime, status);
		addRelationship(source, target, type, characteristic, refinability,roleGroup, rel.getPrimordialComponentUuid());
		addSapAndClose(path, author, commitTime, "relationship");
	}

	public void transformDescriptionRevision(TkDescriptionRevision latestRev, UUID descId, 
											  UUID conceptId, long commitTime) throws IOException 
	{
		UUID conId = conceptId;
		String text = latestRev.getText();
		text = clearBrackets(text);
		UUID type = latestRev.getTypeUuid();
		boolean isCaseSig = latestRev.isInitialCaseSignificant();
		String lang = latestRev.getLang();

		UUID path = latestRev.getPathUuid();
		UUID status = latestRev.getStatusUuid();
		UUID author = latestRev.getAuthorUuid();
		long effectiveTime = latestRev.getTime();

		addPreceedingValues("description", descId, effectiveTime, status);
		addDescription(conId, text, type, isCaseSig, lang, descId);
		addSapAndClose(path, author, commitTime, "description");
	}

	public void transformAttributes(TkConceptAttributesRevision attr, UUID uid, long commitTime)
		throws IOException 
	{
		boolean isDefined = attr.isDefined();

		UUID path = attr.getPathUuid();
		UUID status = attr.getStatusUuid();
		UUID author = attr.getAuthorUuid();
		long effectiveTime = attr.getTime();

		addPreceedingValues("attribute", uid, effectiveTime, status);
		addRf2FileAttributes(isDefined, uid);
		addSapAndClose(path, author, commitTime, "attribute");
	}

	public void addRf2FileAttributes(boolean isDefined, UUID conUuid) throws IOException {
		UUID isDefinedValue;
		if (isDefined) {
			isDefinedValue = isFullyDefined;
		} else {
			isDefinedValue = isPrimitive;
		}

		addSctId("isDefined", isDefinedValue);
		addUUID("concept-uuid", conUuid);
		addUUID("isDefined-uuid", isDefinedValue);
	}

	public String identifyRf2Time(long effectiveTime) {
		return TimeHelper.getShortFileDateFormat().format(effectiveTime);
	}

	public String identifyStatus(UUID status) {
		if (status.equals(activeStatus)) {
			return "1";
		} else {
			return "0";
		}
	}

	public void addRelationship(UUID source, UUID target, UUID type, UUID characteristic, 
								 UUID refinability, int roleGroup, UUID relUid) throws IOException 
	{
		addSctId("source", source);
		addSctId("target", target);
		addString("roleGroup", Integer.toString(roleGroup));
		addSctId("type", type);
		addSctId("characteristic", characteristic);
		addString("modifierId", relModifierId);

		addUUID("relationship-uuid", relUid);
		addUUID("source-uuid", source);
		addUUID("target-uuid", target);
		addUUID("type-uuid", type);
		addUUID("characteristic-uuid", characteristic);
		// addUUID("refinability-uuid", refinability);
	}

	public void addDescription(UUID conId, String text, UUID type,
			boolean isCaseSig, String lang, UUID descUuid) throws IOException {
		UUID caseSigUid;
		if (isCaseSig) {
			caseSigUid = isCaseSensitive;
		} else {
			caseSigUid = isNotCaseSensitive;
		}

		addSctId("conceptId", conId);
		addString("lang", lang);
		addSctId("type", type);
		addString("text", text);
		addSctId("isCaseSig", caseSigUid);

		addUUID("description-uuid", descUuid);
		addUUID("concept-uuid", conId);
		addUUID("type-uuid", type);
		addUUID("isCaseSig-uuid", caseSigUid);
	}

	public void addPreceedingValues(String type, UUID componentId, long effectiveTime, UUID status) throws IOException {
		writer.append("\t\t\t\t<" + type + ">");
		writer.newLine();

		String stat = identifyStatus(status);
		String time = identifyRf2Time(effectiveTime);

		addSctId("id", componentId);
		addString("effectiveTime", time);
		addString("active", stat);
	}

	public void addSapAndClose(UUID path, UUID author, long commitTime, String closeElement) throws IOException {
		addSctId("author", author);
		addPathUUID("path", path);
		addString("commitTime", Long.toString(commitTime));

		if (closeElement != null) {
			writer.append("\t\t\t\t</" + closeElement + ">");
			writer.newLine();
		}
	}

	public void addPathUUID(String key, UUID value) throws IOException {
		writer.append("\t\t\t\t\t<" + key + ">");

		if (!pathUuidMap.containsKey(value)) {
			try {
				PathBI path = getTermFactory().getPath(value);
				pathUuidMap.put(value, path.toString());

			} catch (TerminologyException te) {
				throw new IOException(te.getMessage());
			}
		}

		writer.append(pathUuidMap.get(value));
		writer.append("</" + key + ">");

		writer.newLine();
	}

	public void endConcept() throws IOException {
		writer.append("\t\t\t</concept>");
		writer.newLine();
	}

	public String prepareTransformation(EConcept eConcept) throws IOException {
		UUID primUid = eConcept.getPrimordialUuid();
		String initialText = new String();
		String snomedId = new String();
		initialText = getPrefTerm(primUid);
		initialText = cleanTerm(initialText);
		snomedId = getSnomedId(primUid);

		return new String("\t\t\t<concept id='" + snomedId + "' text='" + initialText + "'>");
	}

	public void addBoolean(String key, boolean value) throws IOException {
		writer.append("\t\t\t\t\t<" + key + ">");

		if (value) {
			writer.append("true");
		} else {
			writer.append("false");
		}

		writer.append("</" + key + ">");
		writer.newLine();
	}

	public void addString(String key, String value) throws IOException {
		writer.append("\t\t\t\t\t<" + key + ">");
		writer.append(value);
		writer.append("</" + key + ">");

		writer.newLine();
	}

	public void addUUID(String key, UUID uid) throws IOException {
		writer.append("\t\t\t\t\t<" + key + ">");
		writer.append(uid.toString());
		writer.append("</" + key + ">");

		writer.newLine();
	}

	public void addSctId(String key, UUID uid) throws IOException {
		writer.append("\t\t\t\t\t<" + key + ">");

		if (!uuidToSctIdMap.containsKey(uid)) {
			String snomedId = getSnomedId(uid);
			uuidToSctIdMap.put(uid, snomedId);
		}

		writer.append(uuidToSctIdMap.get(uid));
		writer.append("</" + key + ">");

		writer.newLine();
	}

	public long nextCommitTime() throws IOException, ClassNotFoundException {
		lazyInit();
		if (nextCommit == null) {
			if (dataStream == null) {
				nextCommit = Long.MAX_VALUE;
			} else {
				try {
					nextCommit = dataStream.readLong();
					assert nextCommit != Long.MAX_VALUE;
				} catch (EOFException e) {
					nextCommit = Long.MAX_VALUE;
				}
			}
		}
		return nextCommit;
	}

	public void lazyInit() throws FileNotFoundException, IOException, ClassNotFoundException {
		
		logger.info("changeset.getName()..." + changeset.getName());
		
		logger.info("wfPropertySuffix..." + wfPropertySuffix);
		
		logger.info("getTermFactory()..." + getTermFactory().toString());
		
		String lastImportSize = getTermFactory().getProperty(changeset.getName() + wfPropertySuffix);
		
		logger.info("lastImportSize..." + lastImportSize);
		
		if (lastImportSize != null) {
			long lastSize = Long.parseLong(lastImportSize);
			if (lastSize == changeset.length()) {
				nextCommit = Long.MAX_VALUE;
				initialized = true;
			}
		}
		
		if (initialized == false) {
			FileInputStream fis = new FileInputStream(changeset);
			BufferedInputStream bis = new BufferedInputStream(fis);
			dataStream = new DataInputStream(bis);
			initialized = true;
		}
		
		
	}

	public static String getSnomedId(UUID uid) throws IOException {
		Long sctId = null;
		I_Identify identify = getTermFactory().getId(uid);
		List<? extends I_IdVersion> i_IdentifyList = identify.getIdVersions();

		if (i_IdentifyList.size() > 0) {
			for (int i = 0; i < i_IdentifyList.size(); i++) {
				I_IdVersion i_IdVersion = (I_IdVersion) i_IdentifyList.get(i);
				Object denotion = (Object) i_IdVersion.getDenotation();
				int snomedIntegerNid = i_IdVersion.getAuthorityNid();

				if (snomedIntegerNid == snomedIdNid) {
					sctId = (Long) denotion;
				}
			}
		}

		if (sctId == null) {
			return uid.toString();
		} else {
			return Long.toString(sctId);
		}
	}

	public static String getPrefTerm(UUID uid) throws IOException {
		long latestTime = 0;
		String prefTerm = "Not Identified";

		try {
			I_GetConceptData con = getTermFactory().getConcept(uid);
			int descTypeNid = ArchitectonicAuxiliary.Concept.PREFERRED_DESCRIPTION_TYPE.localize().getNid();
			int rf2DescTypeNid = getTermFactory().uuidToNative(SnomedMetadataRf2.SYNONYM_RF2.getUuids()[0]);

			Collection<? extends I_DescriptionVersioned> descs = con.getDescriptions();

			for (I_DescriptionVersioned<?> desc : descs) {
				if ((desc.getTypeNid() == descTypeNid || desc.getTypeNid() == rf2DescTypeNid) && 
					(desc.getLang().equals("en") || desc.getLang().equals("en-us"))) 
				{
					if (desc.getTime() > latestTime) {
						latestTime = desc.getTime();
						prefTerm = desc.getText();
					}
				}
			}
		} catch (TerminologyException e) {
			e.printStackTrace();
		}

		return prefTerm;
	}

	public String cleanTerm(String term) {
		return term.replaceAll("[^a-zA-Z0-9\\s]", "");
	}

	public String clearBrackets(String term) {
		String t = term.replaceAll("<", "");
		
		if (term.contains("response curve measurement ")) {
			int a = 1;
		}
		return t.replaceAll(">", "");
	}


	/**
	 * xmlFilePath
	 * 
	 * @parameter
	 * @required
	 */
	public String changeSetDir;
	
	/**
	 * outputFileName
	 * 
	 * @parameter
	 * @required
	 */
	public String outputFileName;
	
	/**
	 * database
	 * 
	 * @parameter
	 * @required
	 */
	public String database;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		//File testDir = new File("profiles\\");
		try {	
			logger.info("Changeset XML transformation started...");
			
			logger.info("TermFactory Creating..." + database);
			createTermFactory(database);
			logger.info("TermFactory Finished...");
		
			File testDir = new File(changeSetDir);
			logger.info("changeSetDir..." + changeSetDir);
			
			File outputFile = new File(outputFileName);
			logger.info("outputFileName..." + outputFileName);
			
			logger.info("createAceConfig Creating...");
			createAceConfig();
			logger.info("createAceConfig finished...");
			
			EConceptXMLTransformer transformer = new EConceptXMLTransformer();
			transformer.transform(testDir , outputFile);
			
			logger.info("Changeset XML transformation finished...");
		} catch (InstantiationException e) {
			System.out.println("===InstantiationException==" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("===IllegalAccessException==" + e.getMessage());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("===FileNotFoundException==" + e.getMessage());
		} catch (IOException e) {			
			e.printStackTrace();
			System.out.println("===IOException==" + e.getMessage());
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
			System.out.println("===ClassNotFoundException==" + e.getMessage());
		}
	}
}
