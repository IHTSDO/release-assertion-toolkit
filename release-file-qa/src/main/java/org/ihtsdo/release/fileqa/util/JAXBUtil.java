package org.ihtsdo.release.fileqa.util;

import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.ihtsdo.release.fileqa.model.MessageType;
import org.ihtsdo.release.fileqa.model.Metadata;

public class JAXBUtil {

	public static Metadata getMetadata(String metaDataFile, WriteExcel writeExcel) {

		Metadata qa = null;
		try {

			JAXBContext context = JAXBContext.newInstance(Metadata.class);
			Unmarshaller u = context.createUnmarshaller();
    
			qa = (Metadata) u.unmarshal( new File( metaDataFile ) );

		} catch (JAXBException e) {
			writeExcel.addRow(MessageType.FAILURE,
					"MetadataTest,Current,Failed, ," + metaDataFile + " :"
							+ e.getMessage());
		} catch (Exception e) {
			writeExcel.addRow(MessageType.FAILURE,
					"MetadataTest,Current,Failed, ," + metaDataFile + " :"
							+ "MetaData file missing or error :" + e.getMessage());
		}
		return qa;
	}
}
