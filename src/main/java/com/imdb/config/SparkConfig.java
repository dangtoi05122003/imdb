package com.imdb.config;
import org.apache.spark.sql.SparkSession;
public class SparkConfig {
    private final SparkSession spark;
    public SparkConfig() {
        this.spark = SparkSession.builder()
            .master("local[*]")
            .config("spark.driver.memory", "8g")
            .config("spark.hadoop.fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
            .config("google.cloud.auth.service.account.json.keyfile", "/opt/airflow/key.json")
            .appName("imdb").getOrCreate();
    }
    public SparkSession getSparkSession() {
        return this.spark;
    }
}
