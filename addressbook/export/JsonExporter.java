package addressbook.export;

import addressbook.model.Person;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.List;

public class JsonExporter implements Exporter {
    @Override
    public String getFormatName() {
        return "JSON";
    }

    @Override
    public String getSuggestedExtension() {
        return ".json";
    }

    @Override
    public void export(List<Person> persons, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            writer.println("[");
            for (int i = 0; i < persons.size(); i++) {
                Person person = persons.get(i);
                writer.println("  {");
                writer.printf("    \"name\": \"%s\",%n", escape(person.getName()));
                writer.printf("    \"phone\": \"%s\",%n", escape(person.getPhone()));
                writer.printf("    \"email\": \"%s\"%n", escape(person.getEmail()));
                writer.print(i == persons.size() - 1 ? "  }\n" : "  },\n");
            }
            writer.println("]");
        }
    }

    private String escape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    @Override
    public String toString() {
        return getFormatName();
    }
}
