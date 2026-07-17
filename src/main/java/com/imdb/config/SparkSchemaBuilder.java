package com.imdb.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class SparkSchemaBuilder {
    private static final Map<String, DataType> TYPE_MAP = new HashMap<>();
    static {
        TYPE_MAP.put("STRING", DataTypes.StringType);
        TYPE_MAP.put("INTEGER", DataTypes.IntegerType);
        TYPE_MAP.put("FLOAT", DataTypes.FloatType);
        TYPE_MAP.put("BOOLEAN", DataTypes.BooleanType);
    }
    public static DataType getType(String yamlType) {
        return TYPE_MAP.getOrDefault(yamlType, DataTypes.StringType);
    }
    public static StructType buildSchema(List<Map<String, Object>> schemaConfig, Dataset<Row> df) {
        List<StructField> fields = new ArrayList<>();
        for (Map<String, Object> fieldMeta : schemaConfig) {
            String rawName = (String) fieldMeta.get("name");
            String reName = fieldMeta.containsKey("rename") ? (String) fieldMeta.get("rename") : rawName;
            String yamlType = ((String) fieldMeta.get("type"));
            DataType sparkType = TYPE_MAP.getOrDefault(yamlType, DataTypes.StringType);
            fields.add(DataTypes.createStructField(reName, sparkType, true));
        }
        return DataTypes.createStructType(fields);
    }
}
