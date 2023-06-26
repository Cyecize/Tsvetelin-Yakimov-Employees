package com.cyecize.app.api.csv;

import com.cyecize.summer.areas.routing.interfaces.UploadedFile;
import java.util.List;

public interface CSVParser {

    <T> List<T> parse(UploadedFile csv, Class<T> type);
}
