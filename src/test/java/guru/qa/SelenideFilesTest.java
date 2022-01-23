package guru.qa;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFilesTest {


    //SimpleExample
    @Test
    void downloadTest() throws Exception {
        open("https://github.com/junit-team/junit5/blob/main/LICENSE.md");
        File downloadedFile = $("#raw-url").download();

        try (InputStream is = new FileInputStream(downloadedFile)) {
            assertThat(new String(is.readAllBytes(), StandardCharsets.UTF_8)).contains("Eclipse");
        }
    }

    @Test
    void uploadFile() {
        open("https://the-internet.herokuapp.com/upload");
        $("input[type='file']").uploadFromClasspath("upload.txt");
        $("#file-submit").click();

        $("#uploaded-files").shouldHave(Condition.text("upload.txt"));
    }












}
