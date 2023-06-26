package com.cyecize.app.api.csv;

import com.cyecize.ioc.annotations.Service;
import com.cyecize.summer.areas.routing.interfaces.UploadedFile;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CSVParserImpl implements CSVParser {

    @Override
    public <T> List<T> parse(UploadedFile csv, Class<T> type) {
        return List.of();
    }
}
