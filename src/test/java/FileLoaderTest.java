import com.bar1g16.FileLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
            testLoader = new FileLoader("testfixture.osm", "xml_to_RDF.xsl");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        testStyles = testLoader.getStyleSheet();
    }

    @AfterEach
    void tearDown() {
        testLoader = null;
        testStyles = null;
    }

    @Test
    void testConstructorThrowsNotFoundException() {
        assertThrows(FileNotFoundException.class, () -> {
            new FileLoader("wibble","xml_to_RDF.xsl");
        });



    }

    @Test
    void testgetFileFindsDoc() {

            assertNotNull(testLoader.getXMLDocument());

    }

    @Test
    void testloadedDocHasNoAttributes() {


            assertFalse(testLoader.getXMLDocument().hasAttributes());

    }

    @Test
    void testloadedDocHasChildNodes() {


            assertTrue(testLoader.getXMLDocument().hasChildNodes());


    }

    @Test
    void testloadedDocHasCorrectNumElements() {
        Document doc = null;


            doc = testLoader.getXMLDocument();
            int testvar = doc.getElementsByTagName("SECT").getLength();

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
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //if we made this FileLoader successfully then no exception was generated so fail
        if (testLoader1 !=null){
            assertFalse(true);
        }


        //assertThrows(FileNotFoundException.class, () ->  testLoader1.getStylesheet());
    }

    @Test
    void testGetStyleSheetReturnsNotNull() {
        testStyles = testLoader.getStyleSheet();
        assertNotNull(testStyles);
    }




}
