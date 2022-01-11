# ERCLabCrawler-backend (Java SpringBoot application)
This repository contains the code for ERCLab crawler back-end (Java SpringBoot, Spark, Kafka, MongoDB, &amp; HBase)
- Kafka broker implemented in ERCLabCrawler clinent in Python sends the extracted data to ERCLabCrawler backend.
- Kafka consumer receives the data from Client and apply data cleaning and ML operations simultanously using Spark & SparkML.
- Finally stores the cleaned data in MongoDB or HBase databases.
