package com.msalah.contactlist.utils;

import com.msalah.contactlist.domain.Contact;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FileUtils {
    public static List<Contact> parseContactsListCSVFile(String filePath) throws IOException {
        String fileContent = getFileContentsAsString(filePath);

        // Will not use jackson-dataformat-csv to parse the csv file, because the extra commas are not escaped in the file
        // Instead, will build my own parser
        return Arrays.asList(fileContent.split("\n")).stream()
                .skip(1)
                .filter(StringUtils::isNoneBlank)
                .map(line -> {
                    String[] lineParts = line.split(",");
                    if (lineParts.length < 2) {
                        System.out.println("Something wrong happened 1");
                        System.out.println("Something wrong happened 1: " + line);
                        throw new IllegalArgumentException("Could not convert line [" + line + "] into Contact entity of name and url");
                    }

                    int urlIndex = -1;
                    for (int i = 0; i < lineParts.length; i++) {
                        if (lineParts[i].matches("^ ?(http|https)://.*\r?$")) {
                            urlIndex = i;
                            break;
                        }
                    }

                    if (urlIndex == -1) {
                        System.out.println("Something wrong happened 2");
                        System.out.println("Something wrong happened 2: " + line);
                        throw new IllegalArgumentException("Could not find a valid url in line[" + line + "]");
                    }

                    String name = IntStream.range(0, urlIndex).boxed()
                            .map(index -> lineParts[index])
                            .collect(Collectors.joining(","));
                    return new Contact(name.trim(), lineParts[urlIndex].trim());
                }).collect(Collectors.toList());
    }

    public static String getFileContentsAsString(String filePath) throws IOException {

        String fileContent;
        try (InputStream inputStream = ResourceUtils.getURL("classpath:static/" + filePath).openStream()) {
            fileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        return fileContent;
    }
}
