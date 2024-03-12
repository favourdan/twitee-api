package org.tm30.security.util;

import com.lowagie.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {
	public String readFileToString(String fileName) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			char[] buffer = new char[10];
			while (reader.read(buffer) != -1) {
				stringBuilder.append(new String(buffer));
				buffer = new char[10];
			}
			reader.close();
		} catch (IOException ex) {
			throw new RuntimeException();
		}

		return stringBuilder.toString();
	}

	public void writeStringToFile(File newHtmlFile, String htmlString) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(newHtmlFile));
			writer.write(htmlString);

			writer.close();
		} catch (IOException ex) {

		}
	}

	public void toPDF(Document document, String pathPDF) {
		ITextRenderer renderer = new ITextRenderer();
		// FontResolver resolver = renderer.getFontResolver();
		// renderer.getFontResolver().addFont("src/main/resources/statement/CerebriSans-Book.ttf",
		// true);
		SharedContext sharedContext = renderer.getSharedContext();
		sharedContext.setPrint(true);
		sharedContext.setInteractive(false);
		renderer.setDocumentFromString(document.html());
		renderer.layout();
		try (OutputStream os = Files.newOutputStream(Paths.get(pathPDF))) {

			renderer.createPDF(os);

		} catch (IOException | DocumentException e) {
			log.error("Exception found: {}", e);
		}
	}
}
