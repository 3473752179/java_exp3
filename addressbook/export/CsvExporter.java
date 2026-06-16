package addressbook.export;

import addressbook.model.Person;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.List;

public class CsvExporter implements Exporter {
    @Override
    public String getFormatName() {
        return "CSV";
    }

    @Override
    public String getSuggestedExtension() {
        return ".csv";
    }

    @Override
    public void export(List<Person> persons, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            writer.println("name,phone,email");
            for (Person person : persons) {
                writer.printf(
                        "\"%s\",\"%s\",\"%s\"%n",
                        escape(person.getName()),
                        escape(person.getPhone()),
                        escape(person.getEmail())
                );
            }
        }
    }

    private String escape(String value) {
        return value.replace("\"", "\"\"");
    }

    @Override
    public String toString() {
        return getFormatName();
    }
}
