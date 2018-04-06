package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.io.Files;

public class FileUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getPath() {

        // valid case
        assertEquals("folder" + File.separator + "sub-folder", FileUtil.getPath("folder/sub-folder"));

        // null parameter -> throws NullPointerException
        thrown.expect(NullPointerException.class);
        FileUtil.getPath(null);

        // no forwards slash -> assertion failure
        thrown.expect(AssertionError.class);
        FileUtil.getPath("folder");
    }

    @Test
    public void copyFile() {
        File inputFile = new File("src/main/resources/images/defaultprofilepic.png");
        File outputFile = new File("outputfile.png");
        FileUtil.copyFile(inputFile, outputFile);
        boolean result = false;
        try {
            result = Files.equal(inputFile, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(result);
    }

}
