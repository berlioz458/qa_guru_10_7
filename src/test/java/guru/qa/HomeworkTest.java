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
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class HomeworkTest {
//1. Запаковать в zip архив несколько разных файлов - pdf, xlsx, csv DONE
//2. Положить его в ресурсы DONE - test.zip
//3. Реализовать чтение и проверку каждого файла в одном тесте

    private final ClassLoader classLoader = FileParsingTests.class.getClassLoader();

    @Test
    void checkFileInZip() throws Exception {
        ZipFile zf = new ZipFile(new File(classLoader.getResource("test.zip").toURI()));


        ZipEntry zipEntryCsv = zf.getEntry("example.csv");
        try(InputStream stream = zf.getInputStream(zipEntryCsv))
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

        ZipEntry zipEntryXls = zf.getEntry("example2.xlsx");
        try(InputStream stream = zf.getInputStream(zipEntryXls))
        {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue())
                    .isEqualTo("Brand");
        }

        ZipEntry zipEntryPdf = zf.getEntry("example3.pdf");
        try(InputStream stream = zf.getInputStream(zipEntryPdf))
        {
            PDF parsed = new PDF(stream);
            assertThat(parsed.title)
                    .isEqualTo("бланк договор купли-продажи");
        }
    }
}
