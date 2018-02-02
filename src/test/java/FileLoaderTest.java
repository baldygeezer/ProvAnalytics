import com.bar1g16.FileLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.CharBuffer;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

public class FileLoaderTest {
    FileLoader testLoader;
    Transformer testStyles;

    @BeforeEach
    void setUp() {
        try {
            testLoader = new FileLoader("testfixture/", "xml_to_RDF.xsl");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        testStyles = testLoader.getStylesheet();
    }

    @AfterEach
    void tearDown() {
        testLoader = null;
        testStyles = null;
    }

    @Test
    void testGetFileThrowsNotFoundException() {
        assertThrows(FileNotFoundException.class, () -> {
            testLoader.getXMLDocument("test1.xml");
        });
    }

    @Test
    void testgetFileFindsDoc() {
        try {
            assertNotNull(testLoader.getXMLDocument("test.xml"));
        } catch (IOException e) {
        }
    }

    @Test
    void testloadedDocHasNoAttributes() {

        try {
            assertFalse(testLoader.getXMLDocument("test.xml").hasAttributes());
        } catch (IOException e) {
        }
    }

    @Test
    void testloadedDocHasChildNodes() {

        try {
            assertTrue(testLoader.getXMLDocument("test.xml").hasChildNodes());
        } catch (IOException e) {
        }
    }

    @Test
    void testloadedDocHasCorrectNumElements() {
        Document doc = null;

        try {
            doc = testLoader.getXMLDocument("test.xml");
            int testvar = doc.getElementsByTagName("SECT").getLength();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(doc.getElementsByTagName("SECT").getLength() == 2 && doc.getElementsByTagName("TITLE").getLength() == 1 && doc
                .getElementsByTagName("ARTICLE").getLength() == 1);
    }

    @Test
    void testGetStylesheetThrowsFileNotFound() {
        FileLoader testLoader1 = null;

        try {
            testLoader1 = new FileLoader("testfixture/", "xml_to_DF.xsf");
        } catch (FileNotFoundException e) {

            assertTrue(e.getClass().getCanonicalName().equals("java.io.FileNotFoundException"));
        }
        //if we made this FileLoader successfully then no exception was generated so fail
        if (testLoader1 !=null){
            assertFalse(true);
        }


        //assertThrows(FileNotFoundException.class, () ->  testLoader1.getStylesheet());
    }

    @Test
    void testGetStyleSheetReturnsNotNull() {
        testStyles = testLoader.getStylesheet();
        assertNotNull(testStyles);
    }




}
