package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class FileParsingTests {

    private final ClassLoader classLoader = FileParsingTests.class.getClassLoader();

    //Обработка файла в формате PDF
    @Test
    void parsePdfTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File pdfDownload = $(byText("PDF download")).download();

        PDF parsed = new PDF(pdfDownload);
        assertThat(parsed.title).contains("JUnit 5 User Guide");
    }


    //Обработка файла в формате ексель
    @Test
    void parseXlsTest() throws Exception {
        try(InputStream stream = classLoader.getResourceAsStream("example2.xls"))
        {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue())
                    .isEqualTo("Brand");
        }
    }

    //Обработка файла в csv формате
    @Test
    void parseCsvTest() throws Exception {
        try(InputStream stream = classLoader.getResourceAsStream("example.csv"))
        {
            CSVReader reader = new CSVReader(new InputStreamReader(stream));
            List<String[]> list = reader.readAll();

            assertThat(list)
                    .hasSize(10)
                    .contains(
                            new String[] {
                                    "Brand", "Oem", "Count", "Pricelists", "Description"
                            }
                    );
        }
    }

    //Чтение зип архива
    @Test
    void zipTest() throws Exception {
        try (InputStream stream = classLoader.getResourceAsStream("example.zip");
             ZipInputStream zipInputStream = new ZipInputStream(stream))
            {
                ZipEntry zipEntry;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    assertThat(zipEntry.getName()).isEqualTo("example.csv");
                }
            }
    }
}


//Для дз: разборать ZipFile zf = new ZipFile(new File(classLoader.getResource("example.zip").toURI()))
//Он поможет прочитать содержимое файла из архива