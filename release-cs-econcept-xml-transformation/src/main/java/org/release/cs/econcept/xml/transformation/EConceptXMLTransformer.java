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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dwfa.ace.api.I_ConfigAceFrame;
import org.dwfa.ace.api.I_DescriptionVersioned;
import org.dwfa.ace.api.I_GetConceptData;
import org.dwfa.ace.api.I_IdVersion;
import org.dwfa.ace.api.I_Identify;
import org.dwfa.ace.api.I_TermFactory;
import org.dwfa.ace.api.Terms;
import org.dwfa.cement.ArchitectonicAuxiliary;
import org.dwfa.tapi.TerminologyException;
import org.ihtsdo.db.bdb.Bdb;
import org.ihtsdo.db.bdb.BdbTermFactory;
import org.ihtsdo.db.bdb.computer.kindof.LineageHelper;
import org.ihtsdo.etypes.EConcept;
import org.ihtsdo.helper.time.TimeHelper;
import org.ihtsdo.tk.api.PathBI;
import org.ihtsdo.tk.api.Precedence;
import org.ihtsdo.tk.api.description.DescriptionVersionBI;
import org.ihtsdo.tk.binding.snomed.SnomedMetadataRf1;
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

/**
 * @author Jesse Efron
 *
 * @goal export-xml-builder
 * @requiresDependencyResolution compile
 */

public class EConceptXMLTransformer extends AbstractMojo {
	private static int snomedIdNid;
	private BufferedWriter writer = null;
	protected long myTime = 1263758387001L;
	private Long nextCommit = null;
	private File changeset;
	private boolean initialized = false;
	private DataInputStream dataStream;
	private final String wfPropertySuffix = "-transform";

	private final UUID workflowHistoryRefset = UUID.fromString("0b6f0e24-5fe2-3869-9342-c18008f53283");
	private final UUID commitHistoryRefset = UUID.fromString("ea34d82a-a645-337b-88f4-77740dd683b9");
	private final UUID conceptInConflictRefset = UUID.fromString("a9ac8e53-e904-3c72-aef0-b546f0977ed8");
	private final UUID adjudicationRefset = UUID.fromString("dfe2c9dd-2da8-3980-879c-518c1a38907f");

	private final UUID isCaseSensitive = UUID.fromString("0def37bc-7e1b-384b-a6a3-3e3ceee9c52e");
	private final UUID isNotCaseSensitive = UUID.fromString("17915e0d-ed38-3488-a35c-cda966db306a");
	private final UUID isFullyDefined = UUID.fromString("6d9cd46e-8a8f-310a-a298-3e55dcf7a986");
	private final UUID isPrimitive = UUID.fromString("e1a12059-3b01-3296-9532-d10e49d0afc3");

	private final UUID activeStatusRf2 = UUID.fromString("d12702ee-c37f-385f-a070-61d56d4d0f1f");
	private final UUID conceptNotCurrentStatusRf2 = UUID.fromString("6cc3df26-661e-33cd-a93d-1c9e797c90e3");
	private final UUID limitedStatusRf2 = UUID.fromString("0d1278d5-3718-36de-91fd-7c6c8d2d2521");
	private final UUID activeStatusRf1 = UUID.fromString("32dc7b19-95cc-365e-99c9-5095124ebe72");
	private final UUID currentStatusRf1 = UUID.fromString("2faa9261-8fb2-11db-b606-0800200c9a66");
	private static Set<Integer> activeStatusNids  = new HashSet<Integer>();

	private final UUID snomedCTRootUUID = UUID.fromString("ee9ac5d2-a07c-3981-a57a-f7f26baf38d8");
	private I_GetConceptData snomedCTRoot = null;

	private HashMap<UUID, String> uuidToSctIdMap = new HashMap<UUID, String>();
	private HashMap<UUID, String> pathUuidMap = new HashMap<UUID, String>();
	private Date currentEditCycleTime = null;
	private final String currentEditCycleDate = "23/01/2012";
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	private final String relModifierId = "900000000000451002";


	/**
	 * queryTimeOut
	 *
	 * @parameter
	 *
	 */
	private String changeSetDir;
	/**
	 * queryTimeOut
	 *
	 * @parameter
	 *
	 */
	private String database;

	/**
	 * queryTimeOut
	 *
	 * @parameter
	 *
	 */
	private String	outputFileName;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {

			// Get time from string date
			currentEditCycleTime = df.parse(currentEditCycleDate);

            I_TermFactory tf = Terms.get();

            if (tf == null) {
            	Bdb.setup(database);
            }

			snomedCTRoot = Terms.get().getConcept(snomedCTRootUUID);

			createAceConfig();

			activeStatusNids  .add(Terms.get().uuidToNative(activeStatusRf2));
			activeStatusNids.add(Terms.get().uuidToNative(activeStatusRf1));
			activeStatusNids.add(Terms.get().uuidToNative(currentStatusRf1));
			activeStatusNids.add(Terms.get().uuidToNative(conceptNotCurrentStatusRf2));
			activeStatusNids.add(Terms.get().uuidToNative(limitedStatusRf2));
			
			// helper fields
			snomedIdNid = ArchitectonicAuxiliary.Concept.SNOMED_INT_ID.localize().getNid();

			File searchPath = new File(changeSetDir);
			Set<File> children = recursiveCSIdentifier(searchPath);
			writer = new BufferedWriter(new FileWriter(new File(outputFileName)));
			EConcept eConcept = null;

			writer.append("<changesets>");
			writer.newLine();

			int filesExamined = 0;
			int filesTransformed = 0;
			for (File cs : children) {
				filesExamined++;

				changeset = cs;
				long commitTime = 0;
				long prevCommitTime = 0;
				boolean eofReached = false;
				boolean transformedInChangeset = false;
				boolean transformedInCommit = false;

				while (!eofReached && (commitTime = nextCommitTime()) != Long.MAX_VALUE) {
					try {
						// Must read from stream regardless if time is after release
						// time
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
							I_GetConceptData conceptToTransform = Terms.get().getConcept(eConcept.getPrimordialUuid());

//							if (isAncestor(conceptToTransform, snomedCTRoot)) {

							LineageHelper helper = new LineageHelper(Terms.get().getActiveAceFrameConfig());

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
							filesTransformed++;
							writer.append("\t</changeset>");
							writer.newLine();
						} else {
							System.out.println("No value in CS: " + cs.getName());
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

			Terms.get().close();

			System.out.println("Files examined: " + filesExamined);
			System.out.println("Files transformed: " + filesTransformed);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Set<File> recursiveCSIdentifier(File searchPath) {
		return recursiveCSIdentifier(searchPath, new HashSet<File>());
	}

	private Set<File> recursiveCSIdentifier(File potentialCSFile,
			Set<File> csFiles) {
		if (potentialCSFile.isDirectory()) {
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

	private boolean transformEConcept(EConcept eConcept, String conceptStartStr, long commitTime,
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

				if (componentChangedWithChangeSet(latestTime, commitTime, conceptStartStr,
												  conceptStartStringWritten, transformedInCommit,
												  transformedInChangeset))
				{
					conceptStartStringWritten = true;

					transformAttributes((TkConceptAttributesRevision) latestRev,
										eAttr.getPrimordialComponentUuid(),
										commitTime);
				}
			} else {
				if (componentChangedWithChangeSet(eAttr.getTime(), commitTime, conceptStartStr,
												  conceptStartStringWritten, transformedInCommit,
												  transformedInChangeset))
				{
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

					if (componentChangedWithChangeSet(latestTime, commitTime, conceptStartStr,
													  conceptStartStringWritten, transformedInCommit,
													  transformedInChangeset))
					{
						conceptStartStringWritten = true;
						transformDescriptionRevision((TkDescriptionRevision) latestRev,
													 ed.getPrimordialComponentUuid(),
													 eConcept.getPrimordialUuid(), commitTime);
					}
				} else {
					if (componentChangedWithChangeSet(ed.getTime(), commitTime, conceptStartStr,
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

					if (componentChangedWithChangeSet(latestTime, commitTime, conceptStartStr,
													  conceptStartStringWritten, transformedInCommit,
													  transformedInChangeset))
					{
						conceptStartStringWritten = true;
						transformRelationshipRevision((TkRelationshipRevision) latestRev,
													  er.getPrimordialComponentUuid(),
													  er.getC1Uuid(), er.getC2Uuid(), commitTime);
					}
				} else {
					if (componentChangedWithChangeSet(er.getTime(), commitTime, conceptStartStr,
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

				if (!refsetId.equals(workflowHistoryRefset) &&
					!refsetId.equals(commitHistoryRefset) &&
					!refsetId.equals(conceptInConflictRefset) &&
					!refsetId.equals(adjudicationRefset)) {
					if (member.getRevisionList() != null) {
						latestTime = 0;
						for (TkRevision rev : member.getRevisionList()) {
							if (rev.getTime() > latestTime) {
								latestTime = rev.getTime();
								latestRev = rev;
							}
						}

						if (componentChangedWithChangeSet(latestTime, commitTime, conceptStartStr,
														  conceptStartStringWritten, transformedInCommit,
														  transformedInChangeset))
						{
							conceptStartStringWritten = true;
							transformRefsetMembers(member, refsetId, refCompId, eConcept.getPrimordialUuid(),
												   refCompIdTypeMap.get(refCompId), commitTime, latestRev);
						}
					} else {
						if (componentChangedWithChangeSet(member.getTime(), commitTime, conceptStartStr,
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

	private void transformRefsetMembers(TkRefsetAbstractMember<?> member, UUID refsetUid, UUID refCompUid,
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
		addString("refsetName", getRefsetName(refsetUid));
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

	private void transformRelationshipRevision(TkRelationshipRevision latestRev, UUID relId, UUID source,
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

	private void transformNewConceptAttributes(TkConceptAttributes eAttr, long commitTime) throws IOException {
		boolean isDefined = eAttr.isDefined();

		UUID path = eAttr.getPathUuid();
		UUID status = eAttr.getStatusUuid();
		UUID author = eAttr.getAuthorUuid();
		long effectiveTime = eAttr.getTime();

		addPreceedingValues("attribute", eAttr.getPrimordialComponentUuid(), effectiveTime, status);
		addRf2FileAttributes(isDefined, eAttr.getPrimordialComponentUuid());
		addSapAndClose(path, author, commitTime, "attribute");
	}

	private void transformNewDescription(TkDescription ed, long commitTime) throws IOException {
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

	private boolean componentChangedWithChangeSet(long effectiveTime, long commitTime, String conceptStartStr,
												  boolean conceptStartStringWritten, boolean transformedInCommit,
												  boolean transformedInChangeset)
		throws IOException
	{
		if (effectiveTime > currentEditCycleTime.getTime() &&
			commitTime == effectiveTime) {
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

	private void processRefsetComponents(TK_REFSET_TYPE type, TkRefsetAbstractMember<?> member) throws IOException {
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

	private void transformRelationship(TkRelationship rel, long commitTime) throws IOException {
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

	private void transformDescriptionRevision(TkDescriptionRevision latestRev, UUID descId,
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

	private void transformAttributes(TkConceptAttributesRevision attr, UUID uid, long commitTime)
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

	private void addRf2FileAttributes(boolean isDefined, UUID conUuid) throws IOException {
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

	private String identifyRf2Time(long effectiveTime) {
		return TimeHelper.getShortFileDateFormat().format(effectiveTime);
	}

	private String identifyStatus(UUID statusUid) throws IOException {
		int statusNid = 0;
		
		try {
			statusNid = Terms.get().uuidToNative(statusUid);
		} catch (Exception e) {
			throw new IOException("identifyStatus() unable to transform UUID into sctId for: " + statusUid);
		}

		if (activeStatusNids.contains(statusNid)) {
			return "1";
		} else {
			return "0";
		}
	}

	private void addRelationship(UUID source, UUID target, UUID type, UUID characteristic,
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

	private void addDescription(UUID conId, String text, UUID type,
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

	private void addPreceedingValues(String type, UUID componentId, long effectiveTime, UUID status) throws IOException {
		writer.append("\t\t\t\t<" + type + ">");
		writer.newLine();

		String stat = identifyStatus(status);
		String time = identifyRf2Time(effectiveTime);

		addSctId("id", componentId);
		addString("effectiveTime", time);
		addString("active", stat);
	}

	private void addSapAndClose(UUID path, UUID author, long commitTime, String closeElement) throws IOException {
		addSctId("author", author);
		addPathUUID("path", path);
		addString("commitTime", Long.toString(commitTime));

		if (closeElement != null) {
			writer.append("\t\t\t\t</" + closeElement + ">");
			writer.newLine();
		}
	}

	private void addPathUUID(String key, UUID value) throws IOException {
		writer.append("\t\t\t\t\t<" + key + ">");

		if (!pathUuidMap.containsKey(value)) {
			try {
				PathBI path = Terms.get().getPath(value);
				pathUuidMap.put(value, path.toString());

			} catch (TerminologyException te) {
				throw new IOException(te.getMessage());
			}
		}

		writer.append(pathUuidMap.get(value));
		writer.append("</" + key + ">");

		writer.newLine();
	}

	private void endConcept() throws IOException {
		writer.append("\t\t\t</concept>");
		writer.newLine();
	}

	private String prepareTransformation(EConcept eConcept) throws IOException {
		UUID primUid = eConcept.getPrimordialUuid();
		String initialText = new String();
		String snomedId = new String();
		initialText = getDescTerm(primUid);
		initialText = cleanTerm(initialText);
		snomedId = getSnomedId(primUid);

		return new String("\t\t\t<concept id='" + snomedId + "' text='" + initialText + "'>");
	}

	private void addBoolean(String key, boolean value) throws IOException {
		writer.append("\t\t\t\t\t<" + key + ">");

		if (value) {
			writer.append("true");
		} else {
			writer.append("false");
		}

		writer.append("</" + key + ">");
		writer.newLine();
	}

	private void addString(String key, String value) throws IOException {
		writer.append("\t\t\t\t\t<" + key + ">");
		writer.append(value);
		writer.append("</" + key + ">");

		writer.newLine();
	}

	private void addUUID(String key, UUID uid) throws IOException {
		writer.append("\t\t\t\t\t<" + key + ">");
		writer.append(uid.toString());
		writer.append("</" + key + ">");

		writer.newLine();
	}

	private void addSctId(String key, UUID uid) throws IOException {
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

	private void lazyInit() throws FileNotFoundException, IOException, ClassNotFoundException {
		String lastImportSize = Terms.get().getProperty(changeset.getName() + wfPropertySuffix);
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

	private static String getSnomedId(UUID uid) throws IOException {
		Long sctId = null;
		I_Identify identify = Terms.get().getId(uid);

		if (identify != null) {
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
		}
		
		if (sctId == null) {
			return uid.toString();
		} else {
			return Long.toString(sctId);
		}
	}

	private static String getRefsetName(UUID conUid) throws IOException {
		String prefTerm = null;
		try {
			I_GetConceptData con = Terms.get().getConcept(conUid);

//			prefTerm = findDesc(con, Terms.get().uuidToNative(SnomedMetadataRf2.PREFERRED_RF2.getUuids()[0]), prefTerm);
//			prefTerm = findDesc(con, ArchitectonicAuxiliary.Concept.PREFERRED_DESCRIPTION_TYPE.localize().getNid(), prefTerm);
			prefTerm = findDesc(con, Terms.get().uuidToNative(SnomedMetadataRf2.SYNONYM_RF2.getUuids()[0]), prefTerm);
			prefTerm = findDesc(con, Terms.get().uuidToNative(SnomedMetadataRf1.SYNOMYM_DESCRIPTION_TYPE_RF1.getUuids()[0]), prefTerm);

			if (prefTerm == null) {
				prefTerm = "CouldNotIdentifyDescription";
			}
		} catch (TerminologyException e) {
			e.printStackTrace();
		}
		
		return prefTerm;
	}
	
	private static String getDescTerm(UUID conUid) throws IOException {
		String prefTerm = null;

		try {
			I_GetConceptData con = Terms.get().getConcept(conUid);

			prefTerm = findDesc(con, Terms.get().uuidToNative(SnomedMetadataRf2.FULLY_SPECIFIED_NAME_RF2.getUuids()[0]), prefTerm);
//			prefTerm = findDesc(con, Terms.get().uuidToNative(SnomedMetadataRf2.PREFERRED_RF2.getUuids()[0]), prefTerm);
			prefTerm = findDesc(con, Terms.get().uuidToNative(SnomedMetadataRf2.SYNONYM_RF2.getUuids()[0]), prefTerm);
			prefTerm = findDesc(con, ArchitectonicAuxiliary.Concept.FULLY_SPECIFIED_DESCRIPTION_TYPE.localize().getNid(), prefTerm);
//			prefTerm = findDesc(con, ArchitectonicAuxiliary.Concept.PREFERRED_DESCRIPTION_TYPE.localize().getNid(), prefTerm);
			prefTerm = findDesc(con, Terms.get().uuidToNative(SnomedMetadataRf1.SYNOMYM_DESCRIPTION_TYPE_RF1.getUuids()[0]), prefTerm);
			
			if (prefTerm == null) {
				prefTerm = "CouldNotIdentifyDescription";
			}
		} catch (TerminologyException e) {
			e.printStackTrace();
		}
		
		return prefTerm;
	}

	private static String findDesc(I_GetConceptData con, int descTypeNid, String prefTerm) throws TerminologyException, IOException {
		String retPrefTerm = null;
		long latestTime = 0;

		if (prefTerm == null) {
			Collection<? extends I_DescriptionVersioned> descs = con.getDescriptions();

			for (I_DescriptionVersioned<?> desc : descs) {
				if (desc.getTypeNid() == descTypeNid) {
					for (DescriptionVersionBI<?> dVersion : desc.getVersions()) {
						if ((dVersion.getLang().equals("en") || dVersion.getLang().equals("en-us")) &&
							(activeStatusNids.contains(dVersion.getStatusNid()))) {
							if (dVersion.getTime() > latestTime) {
								latestTime = dVersion.getTime();
								retPrefTerm = dVersion.getText();
							}
						}
					}
				}
			}

			return retPrefTerm;
		} else {
			return prefTerm;
		}
	}

	private String cleanTerm(String term) {
		return term.replaceAll("[^a-zA-Z0-9\\s]", "");
	}

	private String clearBrackets(String term) {
		String t = term.replaceAll("<", "");
		return t.replaceAll(">", "");
	}

	public static void createAceConfig() {
		try {

			I_ConfigAceFrame aceConfig = Terms.get().newAceFrameConfig();

			// config.addViewPosition(termFactory.newPosition(termFactory.getPath(new UUID[] { UUID.fromString(test_path_uuid) }),
			// termFactory.convertToThinVersion(df.parse(test_time).getTime())));

			// Added inferred promotion template to catch the context relationships
			Date d = new Date();
			aceConfig.addViewPosition(Terms.get().newPosition(Terms.get().getPath(new UUID[] { UUID.fromString("6fd2530f-0f93-5023-b5ef-d2dcecc6f0ae") }), d.getTime())); //b4f0899d-39db-5c3d-ae03-2bac05433162
			aceConfig.addEditingPath(Terms.get().getPath(new UUID[] { UUID.fromString("6fd2530f-0f93-5023-b5ef-d2dcecc6f0ae") })); //b4f0899d-39db-5c3d-ae03-2bac05433162

			BdbTermFactory tfb = (BdbTermFactory) Terms.get();
			aceConfig.setDbConfig(tfb.newAceDbConfig());
			aceConfig.getDbConfig().setUserConcept(Terms.get().getConcept(ArchitectonicAuxiliary.Concept.GENERIC_USER.getPrimoridalUid()));
			aceConfig.getDescTypes().add(ArchitectonicAuxiliary.Concept.FULLY_SPECIFIED_DESCRIPTION_TYPE.localize().getNid());
//			aceConfig.getDescTypes().add(SnomedMetadataRfx.getDES_FULL_SPECIFIED_NAME_NID());//Fully specified name
			aceConfig.getDescTypes().add(ArchitectonicAuxiliary.Concept.PREFERRED_DESCRIPTION_TYPE.localize().getNid());
			aceConfig.getDescTypes().add(ArchitectonicAuxiliary.Concept.SYNONYM_DESCRIPTION_TYPE.localize().getNid());
//			aceConfig.getDescTypes().add(SnomedMetadataRfx.getDES_SYNONYM_NID());

//			aceConfig.getDescTypes().add(ArchitectonicAuxiliary.Concept.TEXT_DEFINITION_TYPE.localize().getNid());

//			ConceptSpec definition = new ConceptSpec("Definition (core metadata concept)", UUID.fromString("700546a3-09c7-3fc2-9eb9-53d318659a09"));
//			aceConfig.getDescTypes().add(getNid(definition.getLenient().getUUIDs().get(0).toString()));

			aceConfig.getDestRelTypes().add(ArchitectonicAuxiliary.Concept.IS_A_REL.localize().getNid());
			aceConfig.getDestRelTypes().add(Terms.get().uuidToNative(UUID.fromString("c93a30b9-ba77-3adb-a9b8-4589c9f8fb25")));

			aceConfig.setDefaultStatus(Terms.get().getConcept(SnomedMetadataRfx.getSTATUS_CURRENT_NID())); // Current

			aceConfig.getAllowedStatus().add(SnomedMetadataRfx.getSTATUS_CURRENT_NID()); // Current

			aceConfig.getAllowedStatus().add(getNid("a5daba09-7feb-37f0-8d6d-c3cadfc7f724")); //Retired
			aceConfig.getAllowedStatus().add(getNid("6cc3df26-661e-33cd-a93d-1c9e797c90e3")); //Concept non-current (foundation metadata concept)
//			aceConfig.getAllowedStatus().add(getNid("9906317a-f50f-30f6-8b59-a751ae1cdeb9")); //Pending
//			aceConfig.getAllowedStatus().add(getNid("95028943-b11c-3509-b1c0-c4ae16aaad5c")); //Component Moved elsewhere	900000000000487009

//			aceConfig.getAllowedStatus().add(ArchitectonicAuxiliary.Concept.ACTIVE.localize().getNid());
			aceConfig.getAllowedStatus().add(ArchitectonicAuxiliary.Concept.CURRENT.localize().getNid());
//			aceConfig.getAllowedStatus().add(ArchitectonicAuxiliary.Concept.RETIRED.localize().getNid());

			aceConfig.setPrecedence(Precedence.PATH);

			Terms.get().setActiveAceFrameConfig(aceConfig);
		} catch (TerminologyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getNid(String struuid) throws TerminologyException, IOException {
		int nid = 0;
		ArrayList<UUID> uuidList = new ArrayList<UUID>();
		UUID uuid = UUID.fromString(struuid); // SNOMED Core Inferred 5e51196f-903e-5dd4-8b3e-658f7e0a4fe6
		uuidList.add(uuid);
		I_GetConceptData findPathCon = Terms.get().getConcept(uuidList);
		nid = findPathCon.getConceptNid();
		return nid;
	}

}
