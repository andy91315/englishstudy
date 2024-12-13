package com.englishstudy.englishstudy.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class FileTypeConverter implements AttributeConverter<FileType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(FileType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public FileType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return FileType.fromValue(dbData);
    }
}