package addressbook.export;

import addressbook.model.Person;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Exporter {
    String getFormatName();

    String getSuggestedExtension();

    void export(List<Person> persons, File file) throws IOException;
}
