package com.imdb.silver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import static org.apache.spark.sql.functions.col;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import com.imdb.config.LoaderYaml;
import com.imdb.config.SparkConfig;
import com.imdb.config.SparkSchemaBuilder;

public class Transform {
    private SparkConfig spark = new SparkConfig();
    private static Map<String, Object> config = LoaderYaml.loadConfig("/opt/airflow/config/silver.yml");
    public void run() {
        List<Map<String, Object>> tables = (List<Map<String, Object>>) config.get("tables");
        Map<String, String> storage = (Map<String, String>) config.get("storage");
        String bronzePath = storage.get("gcs_bronze");
        String silverPath = storage.get("gcs_silver");
        for (Map<String, Object> table : tables) {
            String tableName = (String) table.get("name");
            List<Map<String, Object>> schema = (List<Map<String, Object>>) table.get("schema");
            Dataset<Row> df = spark.getSparkSession().read().parquet(bronzePath + "/" + tableName);
            StructType targetSchema = SparkSchemaBuilder.buildSchema(schema, df);
            StructField[] fields = targetSchema.fields();
            List<Column> selectColumns = new ArrayList<>();
            for (int i = 0; i < schema.size(); i++) {
                Map<String, Object> fieldMeta = schema.get(i);
                String rawName = (String) fieldMeta.get("name");
                StructField targetField = fields[i];
                selectColumns.add(col(rawName).cast(targetField.dataType()).as(targetField.name()));
            }
            df = df.select(selectColumns.toArray(new Column[0]));
            df.write().mode("overwrite").parquet(silverPath + "/" + tableName);
        }
    }
    public static void main(String[] args) {
        Transform app = new Transform();
        app.run();
    }
}
