package com.imdb.bronze;

import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.imdb.config.LoaderYaml;
import com.imdb.config.SparkConfig;

public class Ingestion {
    private SparkConfig sparkconfig = new SparkConfig();
    private static Map<String, Object> config = LoaderYaml.loadConfig("/opt/airflow/config/bronze.yml");
    public void run() {
        List<Map<String, String>> tables = (List<Map<String, String>>) config.get("tables");
        String localPath = (String) config.get("local_path");
        String gcsPath = (String) config.get("gcs_path");
        SparkSession spark = sparkconfig.getSparkSession();
        for (Map<String, String> tableConfig : tables) {
            String tableName = tableConfig.get("name");
            String filePath = localPath + "/" + tableName;
            String name = tableName.replace(".tsv.gz", "").replace(".", "_");
            Dataset<Row> df = spark.read()
                    .option("header", "true")
                    .option("delimiter", "\t")
                    .option("inferSchema", "true")
                    .csv(filePath);
            df.write().mode("overwrite").parquet(gcsPath + "/" + name);
            df.printSchema();
            df.show(7, false);
        }
    }
    public static void main(String[] args) {
        Ingestion app = new Ingestion();
        app.run();
    }
}