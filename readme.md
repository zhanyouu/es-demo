### elasticsearch+kibana安装命令

```java
docker network create es-net
docker run -d --name es -e "discovery.type=single-node" -v es-data:/usr/share/elasticsearch/data -v es-plugins:/usr/share/elasticsearch/plugins --privileged --network es-net -p 9200:9200 -p 9300:9300 elasticsearch:7.12.1
docker run -d --name kibana --network es-net -e ELASTICSEARCH_HOSTS=http://es:9200 -p 5601:5601 kibana:7.12.1
docker cp .\ik\ es:/usr/share/elasticsearch/plugins
```