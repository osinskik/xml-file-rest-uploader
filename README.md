#Example Spring Boot program for updating XML files

##Endpoint description
### GET /epaperrequest
Provides all documents. 
```shell script
curl -XGET  "http://localhost:8080/epaperrequest"
```
```json
{
  "epaperRequests": [
    {
      "uploadTime": "2020-08-17T16:32:45.364Z",
      "fileName": "example2.xml",
      "deviceInfo": {
        "screenInfo": {
          "width": 1280,
          "height": 752,
          "dpi": 160
        },
        "appInfo": {
          "newspaperName": "abb"
        }
      }
    }
  ],
  "paginationData": {
    "pageSize": 10,
    "pageNumber": 1,
    "count": 1
  }
}
```
Request params:
- pageNumber - used to get page number. Default is 1
- pageSize - used to get page size. Default is 10
- orderBy - used to sort by a field. Default is upload_time. Available: 
    - upload_time
    - width
    - height
    - dpi
    - newspaper_name
- orderingAsc - flag(true/false) which triggers ascending order. Default is false.

## POST /epaperrequest/upload
Used to upload xml files. E.g. file to upload (named ./file.xml):
```xml
<?xml version="1.0" encoding="utf-8"?>
<epaperRequest>
    <deviceInfo name="some browser" id="idOfDevice">
        <screenInfo width="120" height="256" dpi="132" />
        <osInfo name="Browser" version="1.0" />
        <appInfo>
            <newspaperName>Newspapa</newspaperName>
            <version>1.0</version>
        </appInfo>
    </deviceInfo>
    <getPages editionDefId="11" publicationDate="2017-06-06"/>
</epaperRequest>
```
Example request:
```shell script
curl -XPOST -F 'file=@./file.xml' "http://localhost:8080/epaperrequest/upload"
```

#Example usage with MongoDB
1. Build an image in main folder:
```shell script
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=osinskik/xml-file-rest-uploader
```

2. Run docker-compose
```shell script
cd docker
docker-compose up
```
