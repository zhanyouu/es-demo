### elasticsearch+kibana安装命令

```java
docker network create es-net
docker run -d --name es -e "discovery.type=single-node" -v es-data:/usr/share/elasticsearch/data -v es-plugins:/usr/share/elasticsearch/plugins --privileged --network es-net -p 9200:9200 -p 9300:9300 elasticsearch:7.12.1
docker run -d --name kibana --network es-net -e ELASTICSEARCH_HOSTS=http://es:9200 -p 5601:5601 kibana:7.12.1
docker cp .\ik\ es:/usr/share/elasticsearch/plugins
```

### es常用DSL命令
```java
#创建索引
PUT /user
{
  "mappings": {
    "properties": {
      "name":{
        "type": "keyword"
      }
    , 
      "age":{
        "type": "integer",
        "index": false
      }
    ,      
    "desc":{
        "type": "text",
        "analyzer": "ik_smart"
      }
  }
  }
}

#查询索引
GET /user

#删除索引
DELETE /user

#修改索引的mapping
PUT /user/_mapping
{
  "properties":{
    "sex":{
      "type":"keyword"
    }
  }
}

#创建文档
POST /user/_doc/1
{
  "name":"zhanyou",
  "age":30,
  "desc":"我是中国人"
}

#全量更新文档（删除原来文档，创建新文档）
PUT /user/_doc/1
{
  "name":"zhanyou",
  "age":30,
  "desc":"我是中国人"
}

#增量更新文档
POST /user/_update/1
{
  "doc":{
    "age":20
  }
}

#查询文档
GET /user/_doc/2

#删除文档
DELETE /user/_doc/1


```
